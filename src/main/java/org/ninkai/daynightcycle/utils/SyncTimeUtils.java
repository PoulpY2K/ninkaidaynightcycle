package org.ninkai.daynightcycle.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SyncTimeUtils {

    private SyncTimeUtils() {
    }


    /**
     * Gets the current real time in the specified time zone.
     *
     * @param zoneId The time zone to get the current time in.
     * @return The current real time in the specified time zone.
     */
    public static ZonedDateTime getInstantTimeWithOffset(ZoneId zoneId, int offset) {
        Instant instant = Instant.now();
        ZonedDateTime currentTime = ZonedDateTime.ofInstant(instant, zoneId);

        // If offset is != 0 then add it or substract it to the current time
        if (offset > 0)
            currentTime = currentTime.plusHours(offset);
        if (offset < 0)
            currentTime = currentTime.minusHours(Math.abs(offset));


        return currentTime;
    }

    /**
     * Converts the current real time to Minecraft ticks.
     * The conversion is based on the fact that 1 Minecraft day is 20 minutes (1200 seconds),
     * and there are 24000 ticks in a Minecraft day.
     *
     * @return The current real time in Minecraft ticks.
     */
    public static long convertTimeToTicks(ZonedDateTime zonedDateTime) {
        long realTimeInTicks = (zonedDateTime.getHour() * 1000 + zonedDateTime.getMinute() * 100 / 6 - 6000) % 24000;
        if (realTimeInTicks < 0) {
            realTimeInTicks += 24000;
        }
        return realTimeInTicks;
    }
}
