package org.ninkai.daynightcycle.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class DayNightCycleCommandTest {

    @Mock
    private CommandSender sender;
    @Mock
    private Command command;
    @Mock
    private Server server;

    private DayNightCycleCommand dayNightCycleCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dayNightCycleCommand = new DayNightCycleCommand();
        when(sender.getServer()).thenReturn(server);
    }

    @ParameterizedTest
    @MethodSource("provideCommands")
    void testOnCommand(String expectedArg, String expectedMessage) {
        when(sender.hasPermission(anyString())).thenReturn(true);
        when(server.getWorlds()).thenReturn(List.of(mock(org.bukkit.World.class)));

        boolean result = dayNightCycleCommand.onCommand(sender, command, "daynightcycle", new String[]{expectedArg});

        assertTrue(result);
        verify(sender).sendMessage(expectedMessage);
    }

    static Stream<Arguments> provideCommands() {
        return Stream.of(
                Arguments.of("init", "Day-night cycle initialized. Use /daynightcycle start to begin."),
                Arguments.of("start", "Day-night cycle started. Use /daynightcycle status to check current time status."),
                Arguments.of("stop", "Day-night cycle stopped. Back to normal Minecraft day-night cycle."),
                Arguments.of("status", "Day-night cycle is currently null"),
                Arguments.of("reload", "Day-night cycle configuration reloaded.")
        );
    }
}