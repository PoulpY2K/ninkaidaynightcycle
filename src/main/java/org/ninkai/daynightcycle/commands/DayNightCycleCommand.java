package org.ninkai.daynightcycle.commands;

import org.bukkit.GameRule;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.ninkai.daynightcycle.DayNightCycle;

public class DayNightCycleCommand implements CommandExecutor {
    private final DayNightCycle plugin;

    public DayNightCycleCommand(DayNightCycle plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Check if args are present
        if (args.length == 0) {
            sender.sendMessage("Usage: /daynightcycle <init|start|stop|status|reload>");
            return false;
        }

        Server server = sender.getServer();
        // Set doDaylightCycle to false
        switch (args[0].toLowerCase()) {
            case "init":
                server.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false));
                sender.sendMessage("Day-night cycle initialized. Use /daynightcycle start to begin.");
                break;
            case "start":
                server.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false));
                sender.sendMessage("Day-night cycle started. Use /daynightcycle status to check current time status.");
                break;
            case "stop":
                server.getWorlds().forEach(world -> world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true));
                sender.sendMessage("Day-night cycle stopped. Back to normal Minecraft day-night cycle.");
                break;
            case "status":
                sender.sendMessage("Day-night cycle is currently " + (server.getWorlds().getFirst().getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)));
                break;
            case "reload":
                sender.sendMessage("Day-night cycle configuration reloaded.");
                break;
            default:
                sender.sendMessage("Unknown command. Usage: /daynightcycle <init|start|stop|status>");
                return false;
        }

        return true;
    }
}
