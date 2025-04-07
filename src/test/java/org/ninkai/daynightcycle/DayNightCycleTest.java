package org.ninkai.daynightcycle;

import org.bukkit.GameRule;
import org.bukkit.command.PluginCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.MockBukkitExtension;
import org.mockbukkit.mockbukkit.MockBukkitInject;
import org.mockbukkit.mockbukkit.ServerMock;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;
import org.ninkai.daynightcycle.configurations.DayNightCycleOptions;
import org.ninkai.daynightcycle.configurations.LowLagOptions;
import org.ninkai.daynightcycle.configurations.WeatherOptions;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.convertTimeToTicks;

@ExtendWith(MockBukkitExtension.class)
class DayNightCycleTest {

    @MockBukkitInject
    ServerMock server;
    DayNightCycle plugin;
    DayNightCycleOptions pluginOptions;

    @BeforeEach
    void setUp() {
        plugin = MockBukkit.load(DayNightCycle.class);
        server.addSimpleWorld("world");

        pluginOptions = new DayNightCycleOptions(
                true, // enabled
                List.of("world"), // worlds
                "Europe/Paris", // timeZone
                0, // timeOffset
                new LowLagOptions(false, 0), // lowLag
                "", // fixedTime
                new WeatherOptions(false, "dummyApiKey", "Paris") // weather
        );
        plugin.setPluginConfig(pluginOptions);
    }

    @Test
    void testPluginCommandIsNull() {
        // Set the plugin configuration to null
        plugin.setDayNightCycleCommand(null);

        PluginCommand pluginCommand = plugin.getDayNightCycleCommand();
        DayNightCycleCommand dayNightCycleCommand = new DayNightCycleCommand();

        // Check that the plugin throws a NullPointerException when the command is null
        assertThrows(NullPointerException.class, () -> pluginCommand.setExecutor(dayNightCycleCommand));
    }

    @Test
    void testPluginConfigurationBadValues() {
        // Set the plugin configuration to null
        plugin.setPluginConfig(null);

        // Check that the plugin throws a NullPointerException when the configuration is null
        assertThrows(NullPointerException.class, () -> plugin.onEnable());
    }

    @Test
    void testGetWorldTime() {
        // Get the current time in the default time zone
        ZoneId zoneId = ZoneId.of("Europe/Paris");
        Instant instant = Instant.now();
        ZonedDateTime testTime = ZonedDateTime.ofInstant(instant, zoneId);

        ZonedDateTime currentTime = plugin.getWorldTime();

        // Assert that the current time is not null
        assertNotNull(currentTime);
        // Assert that the current time is equal to the test time
        assertEquals(testTime.getSecond(), currentTime.getSecond());
    }

    @Test
    void prepareWorlds() {
        // Prepare the worlds specified in the config by setting the GameRule doDaylightCycle to false
        // and setting the time to the current time with offset in ticks
        plugin.prepareWorlds();

        // Check that the GameRule doDaylightCycle is set to false
        assertNotEquals(Boolean.TRUE, server.getWorlds().getFirst().getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE));

        // Check that the time is set to the current time with offset in ticks
        long expectedTime = convertTimeToTicks(plugin.getWorldTime());
        assertEquals(expectedTime, server.getWorlds().getFirst().getTime());
    }
}
