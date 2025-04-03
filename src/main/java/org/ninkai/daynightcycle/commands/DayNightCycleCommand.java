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
import org.ninkai.daynightcycle.tasks.SyncTimeTask;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.*;
import static org.ninkai.daynightcycle.configurations.DayNightCycleOptions.saveDayNightCycleConfig;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.convertTimeToTicks;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.getCurrentTimeWithOffset;

public class DayNightCycleCommand implements TabExecutor {

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
                if (plugin != null) {
                    plugin.getPluginConfig().setEnabled(true);
                    saveDayNightCycleConfig(plugin, plugin.getPluginConfig().serialize());
                    // Check if the task is already running
                    if (plugin.getTimeSyncTask() != null && !plugin.getTimeSyncTask().isCancelled()) {
                        sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_ALREADY_STARTED);
                        return false;
                    } else {
                        ZonedDateTime time = startTask(plugin, sender);
                        sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_START.formatted(
                                plugin.getPluginConfig().getTimeOffset(),
                                plugin.getPluginConfig().getTimeZone(),
                                time.format(DateTimeFormatter.ofPattern("hh:mm:ss"))
                        ));
                        return true;
                    }
                }
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_STOP:
                // Stop the task if it is running
                if (plugin != null && plugin.getTimeSyncTask() != null) {
                    plugin.getTimeSyncTask().cancel();
                    plugin.getPluginConfig().setEnabled(false);
                    saveDayNightCycleConfig(plugin, plugin.getPluginConfig().serialize());
                    // Set doDaylightCycle to true in all worlds included in pluginConfig
                    server.getWorlds().stream()
                            .filter(world -> plugin.getPluginConfig().getWorlds().contains(world.getName()))
                            .forEach(world ->
                                    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true)
                            );
                }
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_STOP);
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_STATUS:
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_STATUS + (server.getWorlds().getFirst().getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)));
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_RELOAD:
                // Reload the plugin configuration
                if (plugin != null) {
                    plugin.reloadConfig();
                    plugin.loadConfiguration();
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
     * Initializes the task to synchronize time in the specified worlds.
     * The task is scheduled to run at a fixed interval based on the cycle period.
     */
    private ZonedDateTime startTask(DayNightCycle plugin, CommandSender sender) {
        // Set real time in worlds listed in config
        ZoneId timezone = ZoneId.of(plugin.getPluginConfig().getTimeZone());
        int timeOffset = plugin.getPluginConfig().getTimeOffset();
        ZonedDateTime finalCurrentTime = getCurrentTimeWithOffset(timezone, timeOffset);

        sender.getServer().getWorlds().stream()
                .filter(world -> plugin.getPluginConfig().getWorlds().contains(world.getName()))
                .forEach(world -> {
                    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
                    world.setTime(convertTimeToTicks(finalCurrentTime));
                });

        // Create sync task
        BukkitTask timeSyncTask = plugin.getScheduler().runTaskTimer(plugin, new SyncTimeTask(plugin, plugin.getPluginConfig().getWorlds()), 0L, DAYNIGHTCYCLE_CYCLE_REAL_SECOND);
        plugin.setTimeSyncTask(timeSyncTask);

        // Return time for logging purposes
        return finalCurrentTime;
    }
}
