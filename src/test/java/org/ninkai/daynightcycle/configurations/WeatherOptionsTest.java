package org.ninkai.daynightcycle.configurations;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WeatherOptionsTest {

    @Test
    void testSerialization() {
        WeatherOptions options = new WeatherOptions(false, "dummy", "paris");

        Map<String, Object> serialized = options.serialize();
        assertNotNull(serialized);
        assertEquals(false, serialized.get("enabled"));
        assertEquals("dummy", serialized.get("api_key"));
        assertEquals("paris", serialized.get("city"));
    }

    @Test
    void testDeserialization() {
        Map<String, Object> data = Map.of(
                "enabled", false,
                "api_key", "dummy",
                "city", "paris"
        );

        WeatherOptions options = WeatherOptions.deserialize(data);
        assertNotNull(options);
        assertEquals(false, options.getEnabled());
        assertEquals("dummy", options.getApiKey());
        assertEquals("paris", options.getCity());
    }
}