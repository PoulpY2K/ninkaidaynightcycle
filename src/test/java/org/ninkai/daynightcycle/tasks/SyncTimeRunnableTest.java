package org.ninkai.daynightcycle.tasks;

import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.MockBukkitExtension;
import org.mockbukkit.mockbukkit.MockBukkitInject;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.world.WorldMock;
import org.ninkai.daynightcycle.DayNightCycle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockBukkitExtension.class)
class SyncTimeRunnableTest {

    @MockBukkitInject
    ServerMock server;
    DayNightCycle plugin;
    List<String> worlds;

    @BeforeEach
    void setUp() {
        plugin = MockBukkit.load(DayNightCycle.class);
        WorldMock world = server.addSimpleWorld("world");
        world.setTime(1000L);
        worlds = List.of("world");
    }

    @Test
    void testRun() {
        SyncTimeRunnable syncTimeRunnable = new SyncTimeRunnable(plugin, worlds);
        syncTimeRunnable.run();

        World world = server.getWorld(worlds.getFirst());
        assertNotNull(world);
        assertEquals(1001L, world.getTime());
    }
}