package org.ninkai.daynightcycle;

import org.bukkit.plugin.java.JavaPlugin;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;

import java.util.Objects;

public final class DayNightCycle extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(getCommand("daynightcycle")).setExecutor(new DayNightCycleCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
