package org.ninkai.daynightcycle;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.*;


class DayNightCycleTest {

    static ServerMock server;
    static DayNightCycle plugin;

    @BeforeEach
    void setUp() {
        // Start the mock server
        server = MockBukkit.mock();
        // Load your plugin
        plugin = MockBukkit.load(DayNightCycle.class);
    }

    @AfterEach
    void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Test
    void testPluginLoad() {
        Assertions.assertNotNull(plugin, "Plugin should be loaded");
    }
}