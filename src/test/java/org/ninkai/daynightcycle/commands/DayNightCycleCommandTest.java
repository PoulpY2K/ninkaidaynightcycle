package org.ninkai.daynightcycle.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.junit.jupiter.api.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ninkai.daynightcycle.DayNightCycle;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DayNightCycleCommandTest {

    static ServerMock server;
    static DayNightCycle plugin;
    static CommandSender sender;
    static Command command;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DayNightCycle.class);
        sender = server.addPlayer();
        sender.setOp(true);
        PluginCommand pluginCommand = plugin.getCommand("daynightcycle");
        assertNotNull(pluginCommand);
        command = pluginCommand;
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @MethodSource("provideCommands")
    void testOnCommandInit(String[] expectedCommand, String expectedMessage) {
        boolean result = command.execute(sender, "daynightcycle", expectedCommand);
        assertTrue(result);
        assertEquals(expectedMessage, ((PlayerMock) sender).nextMessage());
    }

    @Test
    void testOnUnknownCommand() {
        String[] args = {"unknown"};
        boolean result = command.execute(sender, "daynightcycle", args);
        assertFalse(result);
        assertEquals("Unknown command. Usage: /daynightcycle <init|start|stop|status>", ((PlayerMock) sender).nextMessage());
    }

    public static Stream<Arguments> provideCommands() {
        return Stream.of(
                Arguments.of(new String[]{"init"}, "Day-night cycle initialized. Use /daynightcycle start to begin."),
                Arguments.of(new String[]{"start"}, "Day-night cycle started. Use /daynightcycle status to check current time status."),
                Arguments.of(new String[]{"stop"}, "Day-night cycle stopped. Back to normal Minecraft day-night cycle."),
                Arguments.of(new String[]{"status"}, "Day-night cycle is currently true"),
                Arguments.of(new String[]{"reload"}, "Day-night cycle configuration reloaded.")
                );
    }

    @Test
    void testOnTabComplete() {
        DayNightCycleCommand executor = new DayNightCycleCommand();
        String[] args = {""};
        List<String> completions = executor.onTabComplete(sender, command, "daynightcycle", args);
        assertNotNull(completions);
        assertTrue(completions.contains("init"));
        assertTrue(completions.contains("start"));
        assertTrue(completions.contains("stop"));
        assertTrue(completions.contains("status"));
        assertTrue(completions.contains("reload"));
    }
}