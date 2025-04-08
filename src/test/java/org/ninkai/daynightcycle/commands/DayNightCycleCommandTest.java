package org.ninkai.daynightcycle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.MockBukkitExtension;
import org.mockbukkit.mockbukkit.MockBukkitInject;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;
import org.ninkai.daynightcycle.DayNightCycle;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.*;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.getInstantTimeWithOffset;

@ExtendWith(MockBukkitExtension.class)
class DayNightCycleCommandTest {

    @MockBukkitInject
    ServerMock server;
    DayNightCycle plugin;
    PlayerMock player;
    Command command;

    public static Stream<Arguments> provideCommands() {
        return Stream.of(
                Arguments.of(new String[]{DAYNIGHTCYCLE_SUBCOMMAND_INIT}, DAYNIGHTCYCLE_MESSAGE_INIT),
                Arguments.of(new String[]{DAYNIGHTCYCLE_SUBCOMMAND_RELOAD}, DAYNIGHTCYCLE_MESSAGE_RELOAD),
                Arguments.of(new String[]{DAYNIGHTCYCLE_SUBCOMMAND_STATUS}, DAYNIGHTCYCLE_MESSAGE_STATUS + "true")
        );
    }

    @BeforeEach
    void setUp() {
        plugin = MockBukkit.load(DayNightCycle.class);
        server.addSimpleWorld("world");

        PlayerMock playerMock = server.addPlayer();
        playerMock.setOp(true);
        player = playerMock;

        PluginCommand pluginCommand = plugin.getCommand(DAYNIGHTCYCLE_COMMAND);
        assertNotNull(pluginCommand);
        command = pluginCommand;
    }

    @ParameterizedTest
    @MethodSource("provideCommands")
    void testOnCommand(String[] expectedCommand, String expectedMessage) {
        boolean result = command.execute(player, DAYNIGHTCYCLE_COMMAND, expectedCommand);
        assertTrue(result);
        assertEquals(expectedMessage, player.nextMessage());
    }

    @Test
    void testOnStopAndStartCommand() {
        String[] stopArgs = {DAYNIGHTCYCLE_SUBCOMMAND_STOP};
        boolean stopResult = command.execute(player, DAYNIGHTCYCLE_COMMAND, stopArgs);

        assertTrue(stopResult);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_STOP, player.nextMessage());

        ZoneId timezone = ZoneId.of(plugin.getPluginConfig().getTimeZone());
        int timeOffset = plugin.getPluginConfig().getTimeOffset();
        ZonedDateTime finalCurrentTime = getInstantTimeWithOffset(timezone, timeOffset);

        String[] startArgs = {DAYNIGHTCYCLE_SUBCOMMAND_START};
        boolean startResult = command.execute(player, DAYNIGHTCYCLE_COMMAND, startArgs);

        assertTrue(startResult);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_START.formatted(
                        plugin.getPluginConfig().getTimeOffset(),
                        plugin.getPluginConfig().getTimeZone(),
                        finalCurrentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss").withLocale(Locale.FRENCH))),
                player.nextMessage()
        );
    }

    @Test
    void testOnStartCommandAlreadyStarted() {
        String[] startArgs = {DAYNIGHTCYCLE_SUBCOMMAND_START};
        boolean alreadyStartedResult = command.execute(player, DAYNIGHTCYCLE_COMMAND, startArgs);
        assertFalse(alreadyStartedResult);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_ALREADY_STARTED, player.nextMessage());
    }

    @Test
    void testOnStopCommandAlreadyStopped() {
        String[] stopArgs = {DAYNIGHTCYCLE_SUBCOMMAND_STOP};
        boolean stopResult = command.execute(player, DAYNIGHTCYCLE_COMMAND, stopArgs);
        assertTrue(stopResult);
        player.nextMessage();

        // Execute the stop command without starting the cycle
        boolean alreadyStoppedResult = command.execute(player, DAYNIGHTCYCLE_COMMAND, stopArgs);
        // Assert that the command fails and the correct message is sent
        assertFalse(alreadyStoppedResult);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_ALREADY_STOPPED, player.nextMessage());
    }

    @Test
    void testOnEmptyCommand() {
        String[] args = {};
        boolean result = command.execute(player, DAYNIGHTCYCLE_COMMAND, args);
        assertFalse(result);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_USAGE, player.nextMessage());
    }

    @Test
    void testOnUnknownCommand() {
        String[] args = {"unknown"};
        boolean result = command.execute(player, DAYNIGHTCYCLE_COMMAND, args);
        assertFalse(result);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_UNKNOWN, player.nextMessage());
    }

    @Test
    void testOnTabComplete() {
        // Check sender permission
        assertTrue(player.hasPermission(DAYNIGHTCYCLE_PERMISSION));

        // Check command completion
        DayNightCycleCommand executor = new DayNightCycleCommand();
        String[] args = {""};
        List<String> completions = executor.onTabComplete(player, command, DAYNIGHTCYCLE_COMMAND, args);
        // Assert that the completions contain the expected subcommands
        assertNotNull(completions);
        assertTrue(completions.contains(DAYNIGHTCYCLE_SUBCOMMAND_INIT));
        assertTrue(completions.contains(DAYNIGHTCYCLE_SUBCOMMAND_START));
        assertTrue(completions.contains(DAYNIGHTCYCLE_SUBCOMMAND_STOP));
        assertTrue(completions.contains(DAYNIGHTCYCLE_SUBCOMMAND_STATUS));
        assertTrue(completions.contains(DAYNIGHTCYCLE_SUBCOMMAND_RELOAD));
    }

    @Test
    void testOnTabCompleteNoPermissions() {
        // Set player not op
        player.setOp(false);

        // Check sender permission
        assertFalse(player.hasPermission(DAYNIGHTCYCLE_PERMISSION));

        // Check command completion
        DayNightCycleCommand executor = new DayNightCycleCommand();
        String[] args = {""};
        assertNotNull(args);
        assertEquals(1, args.length);
        List<String> completions = executor.onTabComplete(player, command, DAYNIGHTCYCLE_COMMAND, args);
        // Assert empty array
        assertNotNull(completions);
        assertTrue(completions.isEmpty());
    }

    @Test
    void testOnTabCompleteEmpty() {
        // Check sender permission
        assertTrue(player.hasPermission(DAYNIGHTCYCLE_PERMISSION));

        // Check command completion
        DayNightCycleCommand executor = new DayNightCycleCommand();
        String[] args = {};
        assertEquals(0, args.length);
        List<String> completions = executor.onTabComplete(player, command, DAYNIGHTCYCLE_COMMAND, args);
        // Assert empty array
        assertNotNull(completions);
        assertTrue(completions.isEmpty());
    }
}
