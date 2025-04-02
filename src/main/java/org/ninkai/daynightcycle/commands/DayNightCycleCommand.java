package org.ninkai.daynightcycle.commands;

import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.*;

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
        // Set doDaylightCycle to false
        switch (args[0].toLowerCase()) {
            case DAYNIGHTCYCLE_SUBCOMMAND_INIT:
                server.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false));
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_INIT);
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_START:
                server.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false));
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_START);
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_STOP:
                server.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true));
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_STOP);
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_STATUS:
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_STATUS + (server.getWorlds().getFirst().getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)));
                break;
            case DAYNIGHTCYCLE_SUBCOMMAND_RELOAD:
                // Reload the plugin configuration
                Plugin plugin = sender.getServer().getPluginManager().getPlugin(DAYNIGHTCYCLE_PLUGIN_NAME);
                if (plugin != null) {
                    plugin.reloadConfig();
                }
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_RELOAD);
                break;
            default:
                sender.sendMessage(DAYNIGHTCYCLE_MESSAGE_UNKNOWN);
                return false;
        }

        return true;
    }
}
