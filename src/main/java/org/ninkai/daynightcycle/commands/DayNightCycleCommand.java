package org.ninkai.daynightcycle.commands;

import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ninkai.daynightcycle.DayNightCycle;
import org.ninkai.daynightcycle.tasks.SyncTimeRunnable;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.*;
import static org.ninkai.daynightcycle.configurations.DayNightCycleOptions.saveDayNightCycleConfig;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.convertTimeToTicks;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.getInstantTimeWithOffset;

public class DayNightCycleCommand implements TabExecutor {

    /**
     * Executes the reload command for the DayNightCycle plugin.
     * It reloads the plugin configuration and updates the task if necessary.
     *
     * @param plugin The DayNightCycle plugin instance.
     */
    private static void executeReloadCommand(DayNightCycle plugin) {
        plugin.reloadConfig();
        plugin.loadConfiguration();
    }

    /**
     * Handles tab completion for the DayNightCycle command.
     * It provides suggestions based on the arguments provided.
     *
     * @param sender  The command sender.
     * @param command The command being executed.
     * @param label   The alias of the command used.
     * @param args    The arguments passed to the command.
     * @return A list of suggestions for tab completion.
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Check if the sender has permission to use the command
        if (!sender.hasPermission(DAYNIGHTCYCLE_PERMISSION)) {
            return List.of();
        } else {
            final List<String> availableArguments = new ArrayList<>();
            if (args.length == 1) {
                StringUtil.copyPartialMatches(args[0], List.of(
                        DAYNIGHTCYCLE_SUBCOMMAND_INIT,
                        DAYNIGHTCYCLE_SUBCOMMAND_START,
                        DAYNIGHTCYCLE_SUBCOMMAND_STOP,
                        DAYNIGHTCYCLE_SUBCOMMAND_STATUS,
                        DAYNIGHTCYCLE_SUBCOMMAND_RELOAD), availableArguments);
                return availableArguments;
            }
        }

        return List.of();
    }

    /**
     * Executes the command for the DayNightCycle plugin.
     * It handles the subcommands and their respective actions.
     *
     * @param sender  The command sender.
     * @param command The command being executed.
     * @param label   The alias of the command used.
     * @param args    The arguments passed to the command.
     * @return true if the command was executed successfully, false otherwise.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Check if args are present
        if (args.length == 0) {
            sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_USAGE);
            return false;
        }

        Server server = sender.getServer();
        DayNightCycle plugin = (DayNightCycle) server.getPluginManager().getPlugin(DAYNIGHTCYCLE_PLUGIN_NAME);
        // Set doDaylightCycle to false
        switch (args[0].toLowerCase()) {
            case DAYNIGHTCYCLE_SUBCOMMAND_INIT:
                server.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false));
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_INIT);
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_START:
                if (plugin == null || plugin.getSyncTimeTask() == null) {
                    return false;
                }
                return executeStartCommand(plugin, sender);
            case DAYNIGHTCYCLE_SUBCOMMAND_STOP:
                // Stop the task if it is running
                if (plugin == null || plugin.getSyncTimeTask() == null) {
                    return false;
                }
                executeStopCommand(plugin, server);
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_STOP);
                return true;
            case DAYNIGHTCYCLE_SUBCOMMAND_STATUS:
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_STATUS + (server.getWorlds().getFirst().getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)));
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_RELOAD:
                // Reload the plugin configuration
                if (plugin != null) {
                    executeReloadCommand(plugin);
                }
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_RELOAD);
                break;
            default:
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_UNKNOWN);
                return false;
        }

        return true;
    }

    /**
     * Executes the start command for the DayNightCycle plugin.
     * It initializes the task to synchronize time in the specified worlds.
     *
     * @param plugin The DayNightCycle plugin instance.
     * @param sender The command sender.
     * @return true if the command was executed successfully, false otherwise.
     */
    private boolean executeStartCommand(DayNightCycle plugin, CommandSender sender) {
        plugin.getPluginConfig().setEnabled(true);
        saveDayNightCycleConfig(plugin, plugin.getPluginConfig().serialize());

        // Check if the task is already running
        if (!plugin.getSyncTimeTask().isCancelled()) {
            sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_ALREADY_STARTED);
            return false;
        } else {
            startTimeSyncTask(plugin, sender);
            return true;
        }
    }

    /**
     * Executes the stop command for the DayNightCycle plugin.
     * It cancels the task and sets the game rule to true in all worlds included in pluginConfig.
     *
     * @param plugin The DayNightCycle plugin instance.
     * @param server The server instance.
     */
    private void executeStopCommand(DayNightCycle plugin, Server server) {
        plugin.getSyncTimeTask().cancel();
        plugin.getPluginConfig().setEnabled(false);
        saveDayNightCycleConfig(plugin, plugin.getPluginConfig().serialize());
        // Set doDaylightCycle to true in all worlds included in pluginConfig
        server.getWorlds().stream()
                .filter(world -> plugin.getPluginConfig().getWorlds().contains(world.getName()))
                .forEach(world ->
                        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
                );
    }

    /**
     * Creates a task to synchronize time in the specified worlds.
     * The task is scheduled to run at a fixed interval based on the cycle period.
     *
     * @param plugin The DayNightCycle plugin instance.
     * @param sender The command sender.
     * @return The current time with offset for logging purposes.
     */
    private ZonedDateTime createTask(DayNightCycle plugin, CommandSender sender) {
        // Set real time in worlds listed in config
        ZoneId timezone = ZoneId.of(plugin.getPluginConfig().getTimeZone());
        int timeOffset = plugin.getPluginConfig().getTimeOffset();
        ZonedDateTime finalCurrentTime = getInstantTimeWithOffset(timezone, timeOffset);

        sender.getServer().getWorlds().stream()
                .filter(world -> plugin.getPluginConfig().getWorlds().contains(world.getName()))
                .forEach(world -> {
                    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
                    world.setTime(convertTimeToTicks(finalCurrentTime));
                });

        // Create sync task
        BukkitTask timeSyncTask = plugin.getScheduler().runTaskTimer(plugin, new SyncTimeRunnable(plugin, plugin.getPluginConfig().getWorlds()), 0L, DAYNIGHTCYCLE_CYCLE_REAL_SECOND);
        plugin.setSyncTimeTask(timeSyncTask);

        // Return time for logging purposes
        return finalCurrentTime;
    }

    /**
     * Starts the time synchronization task and sends a message to the sender.
     *
     * @param plugin The DayNightCycle plugin instance.
     * @param sender The command sender.
     */
    private void startTimeSyncTask(DayNightCycle plugin, CommandSender sender) {
        ZonedDateTime time = createTask(plugin, sender);
        sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_START.formatted(
                plugin.getPluginConfig().getTimeOffset(),
                plugin.getPluginConfig().getTimeZone(),
                time.format(DateTimeFormatter.ofPattern("hh:mm:ss").withLocale(Locale.FRENCH))
        ));
    }
}
