package org.ninkai.daynightcycle;

import org.bukkit.plugin.java.JavaPlugin;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;

import java.util.Objects;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_COMMAND;

public class DayNightCycle extends JavaPlugin {

    @Override
    public void onLoad() {
        // Plugin setup logic
        // Load the default config file
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Register the command
        Objects.requireNonNull(getCommand(DAYNIGHTCYCLE_COMMAND)).setExecutor(new DayNightCycleCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
