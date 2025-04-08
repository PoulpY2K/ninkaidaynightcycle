package org.ninkai.daynightcycle.configurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.MockBukkitExtension;
import org.mockbukkit.mockbukkit.MockBukkitInject;
import org.mockbukkit.mockbukkit.ServerMock;
import org.ninkai.daynightcycle.DayNightCycle;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockBukkitExtension.class)
class DayNightCycleOptionsTest {

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
    @SuppressWarnings("unchecked")
    void testSerialization() {
        DayNightCycleOptions options = new DayNightCycleOptions(
                true,
                List.of("world"),
                "Europe/Paris",
                0,
                new LowLagOptions(false, 1),
                "",
                new WeatherOptions(false, "dummyApiKey", "Paris")
        );

        Map<String, Object> serialized = options.serialize();
        assertNotNull(serialized);
        assertEquals(true, serialized.get("enabled"));
        assertEquals(List.of("world"), serialized.get("worlds"));
        assertEquals("Europe/Paris", serialized.get("timezone"));
        assertEquals(0, serialized.get("time_offset"));
        assertEquals(false, ((Map<String, Object>) serialized.get("low_lag")).get("enabled"));
        assertEquals(1, ((Map<String, Object>) serialized.get("low_lag")).get("interval_hour"));
        assertEquals("", serialized.get("fixed_time"));
        assertEquals(false, ((Map<String, Object>) serialized.get("weather")).get("enabled"));
        assertEquals("dummyApiKey", ((Map<String, Object>) serialized.get("weather")).get("api_key"));
        assertEquals("Paris", ((Map<String, Object>) serialized.get("weather")).get("city"));
    }

    @Test
    void testDeserialization() {
        Map<String, Object> data = Map.of(
                "enabled", true,
                "worlds", List.of("world"),
                "timezone", "Europe/Paris",
                "time_offset", 0,
                "low_lag.enabled", false,
                "low_lag.interval_hour", 1,
                "fixed_time", "",
                "weather.enabled", false,
                "weather.api_key", "dummyApiKey",
                "weather.city", "Paris"
        );

        DayNightCycleOptions options = DayNightCycleOptions.deserialize(data);
        assertNotNull(options);
        assertEquals(true, options.getEnabled());
        assertEquals(List.of("world"), options.getWorlds());
        assertEquals("Europe/Paris", options.getTimeZone());
        assertEquals(0, options.getTimeOffset());
        assertEquals(false, options.getLowLag().getEnabled());
        assertEquals(1, options.getLowLag().getIntervalHour());
        assertEquals("", options.getFixedTime());
        assertEquals(false, options.getWeather().getEnabled());
        assertEquals("dummyApiKey", options.getWeather().getApiKey());
        assertEquals("Paris", options.getWeather().getCity());
    }

    @Test
    void saveDayNightCycleConfig() {
        Map<String, Object> options = Map.of(
                "enabled", true,
                "worlds", List.of("world"),
                "timezone", "Europe/Paris",
                "time_offset", 0,
                "low_lag.enabled", false,
                "low_lag.interval_hour", 1,
                "fixed_time", "",
                "weather.enabled", false,
                "weather.api_key", "dummyApiKey",
                "weather.city", "Paris"
        );

        DayNightCycleOptions.saveDayNightCycleConfig(plugin, options);

        // Verify that the configuration was saved correctly
        assertTrue(plugin.getConfig().getBoolean("enabled"));
        assertEquals(List.of("world"), plugin.getConfig().getStringList("worlds"));
        assertEquals("Europe/Paris", plugin.getConfig().getString("timezone"));
        assertEquals(0, plugin.getConfig().getInt("time_offset"));
        assertFalse(plugin.getConfig().getBoolean("low_lag.enabled"));
        assertEquals(1, plugin.getConfig().getInt("low_lag.interval_hour"));
        assertEquals("", plugin.getConfig().getString("fixed_time"));
        assertFalse(plugin.getConfig().getBoolean("weather.enabled"));
        assertEquals("dummyApiKey", plugin.getConfig().getString("weather.api_key"));
        assertEquals("Paris", plugin.getConfig().getString("weather.city"));
    }
}