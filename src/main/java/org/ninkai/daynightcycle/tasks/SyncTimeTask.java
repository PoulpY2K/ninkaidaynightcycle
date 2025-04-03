package org.ninkai.daynightcycle.tasks;

import org.bukkit.World;
import org.ninkai.daynightcycle.DayNightCycle;

public class SyncTimeTask implements Runnable {

    private final DayNightCycle plugin;
    //private final DayNightCycleConfig config;

    public SyncTimeTask(DayNightCycle plugin) {
        this.plugin = plugin;
        //this.config = plugin.getConfig();
    }

    @Override
    public void run() {
        World mainWorld = plugin.getServer().getWorlds().getFirst();
        plugin.getServer().getWorlds().forEach(world -> {
            // Get the current time in the world
            long worldTime = mainWorld.getTime();
            // Set the time in the world
            world.setTime(worldTime + 1L);
            // Send a message to the console
        });
        plugin.getLogger().info("The time has been set to " + mainWorld.getTime());
    }
}
