package org.ninkai.daynightcycle;

import org.bukkit.plugin.java.JavaPlugin;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;

import java.util.Objects;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_COMMAND;

public final class DayNightCycle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand(DAYNIGHTCYCLE_COMMAND)).setExecutor(new DayNightCycleCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
