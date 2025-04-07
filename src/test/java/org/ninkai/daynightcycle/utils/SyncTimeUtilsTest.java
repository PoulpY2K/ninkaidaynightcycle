package org.ninkai.daynightcycle.utils;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SyncTimeUtilsTest {

    @Test
    void getInstantTimeWithOffset() {
        // Test with positive offset
        assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Paris")).plusHours(2).getHour(), SyncTimeUtils.getInstantTimeWithOffset(ZoneId.of("Europe/Paris"), 2).getHour());
        // Test with negative offset
        assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Paris")).minusHours(2).getHour(), SyncTimeUtils.getInstantTimeWithOffset(ZoneId.of("Europe/Paris"), -2).getHour());
        // Test with zero offset
        assertEquals(ZonedDateTime.now(ZoneId.of("Europe/Paris")).getHour(), SyncTimeUtils.getInstantTimeWithOffset(ZoneId.of("Europe/Paris"), 0).getHour());
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