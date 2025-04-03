package org.ninkai.daynightcycle.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ninkai.daynightcycle.DayNightCycle;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SyncTimeTask implements Runnable {

    private final DayNightCycle plugin;
    private final List<String> worlds;

    @Override
    public void run() {
        plugin.getServer().getWorlds().stream()
                // Filter the worlds to only those listed in the config
                .filter(world -> worlds.contains(world.getName()))
                // Set the time in the world
                .forEach(world ->
                    world.setTime(world.getTime() + 1L)
                );
    }
}
