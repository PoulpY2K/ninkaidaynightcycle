package org.ninkai.daynightcycle.commands;

import lombok.Getter;

@Getter
public final class DayNightCycleConstants {
    // Plugin constants
    public static final String DAYNIGHTCYCLE_PLUGIN_NAME = "daynightcycle";
    // Permission constants
    public static final String DAYNIGHTCYCLE_PERMISSION = "daynightcycle";
    // Command constants
    public static final String DAYNIGHTCYCLE_COMMAND = "daynightcycle";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_INIT = "init";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_START = "start";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_STOP = "stop";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_STATUS = "status";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_RELOAD = "reload";
    // Message constants
    public static final String DAYNIGHTCYCLE_MESSAGE_INIT = "Day-night cycle initialized. Use /daynightcycle start to begin.";
    public static final String DAYNIGHTCYCLE_MESSAGE_START = "DNC started. Offset: %sH, Timezone: %s, World: %s";
    public static final String DAYNIGHTCYCLE_MESSAGE_STOP = "DNC stopped. Back to normal Minecraft cycle.";
    public static final String DAYNIGHTCYCLE_MESSAGE_STATUS = "Day-night cycle is currently ";
    public static final String DAYNIGHTCYCLE_MESSAGE_RELOAD = "Day-night cycle configuration reloaded.";
    public static final String DAYNIGHTCYCLE_MESSAGE_USAGE = "Usage: /daynightcycle <init|start|stop|status|reload>";
    public static final String DAYNIGHTCYCLE_MESSAGE_UNKNOWN = "Unknown command. Usage: /daynightcycle <init|start|stop|status>";
    public static final String DAYNIGHTCYCLE_MESSAGE_NO_PERMISSION = "You do not have permission to use this command.";
    public static final String DAYNIGHTCYCLE_MESSAGE_ALREADY_STARTED = "Day-night cycle is already started.";

    // Plugin values
    public static final Long DAYNIGHTCYCLE_CYCLE_REAL_SECOND = 72L;

    private DayNightCycleConstants() {
    }
}
