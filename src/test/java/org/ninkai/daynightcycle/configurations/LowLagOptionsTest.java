package org.ninkai.daynightcycle.configurations;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LowLagOptionsTest {

    @Test
    void testSerialization() {
        LowLagOptions options = new LowLagOptions(false, 1);

        Map<String, Object> serialized = options.serialize();
        assertNotNull(serialized);
        assertEquals(false, serialized.get("enabled"));
        assertEquals(1, serialized.get("interval_hour"));
    }

    @Test
    void testDeserialization() {
        Map<String, Object> data = Map.of(
                "enabled", false,
                "interval_hour", 1
        );

        LowLagOptions options = LowLagOptions.deserialize(data);
        assertNotNull(options);
        assertEquals(false, options.getEnabled());
        assertEquals(1, options.getIntervalHour());
    }
}