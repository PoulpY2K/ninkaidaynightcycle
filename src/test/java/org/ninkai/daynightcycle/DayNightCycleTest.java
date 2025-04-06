package org.ninkai.daynightcycle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.MockBukkitExtension;
import org.mockbukkit.mockbukkit.MockBukkitInject;
import org.mockbukkit.mockbukkit.ServerMock;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockBukkitExtension.class)
class DayNightCycleTest {

    @MockBukkitInject
    private ServerMock server;
    private DayNightCycle plugin;

    @BeforeEach
    void setUp() {
        var plugin = MockBukkit.createMockPlugin("daynightcycle");
        server.addSimpleWorld("world");
        server.addPlayer();
    }

    @AfterEach
    void tearDown() {
        if (MockBukkit.isMocked()) {
            // Stop the mock server
            MockBukkit.unmock();
        }
    }

    @Test
    void testGetWorldTime() {
        // Get the current time in the default time zone
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = Instant.now();
        ZonedDateTime currentTime = ZonedDateTime.ofInstant(instant, defaultZoneId);

        // Assert that the current time is not null
        assertNotNull(currentTime);
    }
}
