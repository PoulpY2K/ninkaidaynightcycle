package org.ninkai.daynightcycle;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class DayNightCycleTest {

    static ServerMock server;
    static DayNightCycle plugin;

    @BeforeAll
    static void setUp() {
        // Start the mock server
        server = MockBukkit.mock();
        // Load your plugin
        plugin = MockBukkit.load(DayNightCycle.class);
    }

    @AfterAll
    static void tearDown() {
        // Stop the mock server
        MockBukkit.unmock();
    }

    @Test
    void testPluginLoad() {
        Assertions.assertNotNull(plugin, "Plugin should be loaded");
    }

    @Test
    void testPluginEnable() {
        // Check if the plugin is enabled
        Assertions.assertTrue(plugin.isEnabled(), "Plugin should be enabled");
    }

    @Test
    void testPluginDisable() {
        // Disable the plugin
        plugin.setEnabled(false);
        // Check if the plugin is disabled
        Assertions.assertFalse(plugin.isEnabled(), "Plugin should be disabled");
        // Reenables the plugin
        plugin.setEnabled(true);
    }

    @Test
    void testSaveDefaultConfig() {
        // Check if the default config is saved
        plugin.saveDefaultConfig();
        Assertions.assertNotNull(plugin.getConfig(), "Default config should be saved");
    }

    @Test
    void testConfigLoad() {
        // Check if the config is loaded correctly
        Assertions.assertNotNull(plugin.getConfig(), "Config should be loaded");
        Assertions.assertTrue(plugin.getConfig().isSet("enabled"), "Config should contain 'enabled'");
    }
}