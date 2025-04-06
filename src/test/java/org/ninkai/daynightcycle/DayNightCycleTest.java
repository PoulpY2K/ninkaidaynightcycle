package org.ninkai.daynightcycle;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.AssertionsKt.*;


class DayNightCycleTest {

    private DayNightCycle plugin;

    @BeforeEach
    void setUp() {
        // Start the mock server
        ServerMock server = MockBukkit.mock();

        // Load your plugin
        plugin = MockBukkit.load(DayNightCycle.class);
        server.addSimpleWorld("world");
    }

    @AfterEach
    void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Test
    void testOnLoadOk() {
        plugin.onLoad();
        // This is a simple test to check if the plugin loads correctly
        assertNotNull(plugin);
    }
}