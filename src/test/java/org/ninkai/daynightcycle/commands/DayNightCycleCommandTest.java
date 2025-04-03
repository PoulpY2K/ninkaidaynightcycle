package org.ninkai.daynightcycle.commands;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ninkai.daynightcycle.DayNightCycle;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.*;

class DayNightCycleCommandTest {

    static ServerMock server;
    static DayNightCycle plugin;
    static CommandSender sender;
    static Command command;

    @BeforeAll
    static void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(DayNightCycle.class);

        sender = server.addPlayer();
        sender.setOp(true);

        PluginCommand pluginCommand = plugin.getCommand(DAYNIGHTCYCLE_COMMAND);
        assertNotNull(pluginCommand);
        command = pluginCommand;
    }

    @AfterAll
    static void tearDown() {
        MockBukkit.unmock();
    }

    public static Stream<Arguments> provideCommands() {
        return Stream.of(
                Arguments.of(new String[]{DAYNIGHTCYCLE_SUBCOMMAND_INIT}, DAYNIGHTCYCLE_MESSAGE_INIT),
                Arguments.of(new String[]{DAYNIGHTCYCLE_SUBCOMMAND_START}, DAYNIGHTCYCLE_MESSAGE_START),
                Arguments.of(new String[]{DAYNIGHTCYCLE_SUBCOMMAND_STOP}, DAYNIGHTCYCLE_MESSAGE_STOP),
                Arguments.of(new String[]{DAYNIGHTCYCLE_SUBCOMMAND_STATUS}, DAYNIGHTCYCLE_MESSAGE_STATUS + "true")
        );
    }

    @ParameterizedTest
    @MethodSource("provideCommands")
    void testOnCommand(String[] expectedCommand, String expectedMessage) {
        boolean result = command.execute(sender, DAYNIGHTCYCLE_COMMAND, expectedCommand);
        assertTrue(result);
        assertEquals(expectedMessage, ((PlayerMock) sender).nextMessage());
    }

    @Test
    void testOnCommandReload() {
        String[] args = {DAYNIGHTCYCLE_SUBCOMMAND_RELOAD};
        boolean result = command.execute(sender, DAYNIGHTCYCLE_COMMAND, args);
        assertTrue(result);

        Plugin localPlugin = sender.getServer().getPluginManager().getPlugin(DAYNIGHTCYCLE_PLUGIN_NAME);
        // Reload config
        assertNotNull(plugin);
        assertNotNull(localPlugin);
        localPlugin.reloadConfig();

        assertEquals(DAYNIGHTCYCLE_MESSAGE_RELOAD, ((PlayerMock) sender).nextMessage());
    }

    @Test
    void testOnEmptyCommand() {
        String[] args = {};
        boolean result = command.execute(sender, DAYNIGHTCYCLE_COMMAND, args);
        assertFalse(result);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_USAGE, ((PlayerMock) sender).nextMessage());
    }

    @Test
    void testOnUnknownCommand() {
        String[] args = {"unknown"};
        boolean result = command.execute(sender, DAYNIGHTCYCLE_COMMAND, args);
        assertFalse(result);
        assertEquals(DAYNIGHTCYCLE_MESSAGE_UNKNOWN, ((PlayerMock) sender).nextMessage());
    }

    @Test
    void testOnTabComplete() {
        // Check sender permission
        assertTrue(sender.hasPermission(DAYNIGHTCYCLE_PERMISSION));

        // Check command completion
        DayNightCycleCommand executor = new DayNightCycleCommand();
        String[] args = {""};
        List<String> completions = executor.onTabComplete(sender, command, DAYNIGHTCYCLE_COMMAND, args);
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
        sender.setOp(false);

        // Check sender permission
        assertFalse(sender.hasPermission(DAYNIGHTCYCLE_PERMISSION));

        // Check command completion
        DayNightCycleCommand executor = new DayNightCycleCommand();
        String[] args = {""};
        assertNotNull(args);
        assertEquals(1, args.length);
        List<String> completions = executor.onTabComplete(sender, command, DAYNIGHTCYCLE_COMMAND, args);
        // Assert empty array
        assertNotNull(completions);
        assertTrue(completions.isEmpty());
    }

    @Test
    void testOnTabCompleteEmpty() {
        // Check sender permission
        assertTrue(sender.hasPermission(DAYNIGHTCYCLE_PERMISSION));

        // Check command completion
        DayNightCycleCommand executor = new DayNightCycleCommand();
        String[] args = {};
        assertEquals(0, args.length);
        List<String> completions = executor.onTabComplete(sender, command, DAYNIGHTCYCLE_COMMAND, args);
        // Assert empty array
        assertNotNull(completions);
        assertTrue(completions.isEmpty());
    }
}