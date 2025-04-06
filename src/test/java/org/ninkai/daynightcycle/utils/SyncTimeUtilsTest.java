package org.ninkai.daynightcycle.utils;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SyncTimeUtilsTest {

    @Test
    void getInstantTimeWithOffset() {
        // Test with positive offset
        assertEquals(3, SyncTimeUtils.getInstantTimeWithOffset(ZoneId.of("Europe/Paris"), 2).getHour());
        // Test with negative offset
        assertEquals(23, SyncTimeUtils.getInstantTimeWithOffset(ZoneId.of("Europe/Paris"), -2).getHour());
        // Test with zero offset
        assertEquals(1, SyncTimeUtils.getInstantTimeWithOffset(ZoneId.of("Europe/Paris"), 0).getHour());
    }

    @Test
    void convertTimeToTicks() {
        // Test with a specific time
        assertEquals(18000, SyncTimeUtils.convertTimeToTicks(ZonedDateTime.of(2023, 10, 1, 0, 0, 0, 0, ZoneId.of("Europe/Paris"))));
        assertEquals(0, SyncTimeUtils.convertTimeToTicks(ZonedDateTime.of(2023, 10, 1, 6, 0, 0, 0, ZoneId.of("Europe/Paris"))));
        assertEquals(6000, SyncTimeUtils.convertTimeToTicks(ZonedDateTime.of(2023, 10, 1, 12, 0, 0, 0, ZoneId.of("Europe/Paris"))));
        assertEquals(12000, SyncTimeUtils.convertTimeToTicks(ZonedDateTime.of(2023, 10, 1, 18, 0, 0, 0, ZoneId.of("Europe/Paris"))));
    }
}