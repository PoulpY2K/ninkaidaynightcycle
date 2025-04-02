package org.ninkai.daynightcycle.commands;

import lombok.Getter;

@Getter
public final class DayNightCycleConstants {
    private DayNightCycleConstants() {}

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
    public static final String DAYNIGHTCYCLE_MESSAGE_START = "Day-night cycle started. Use /daynightcycle status to check current time status.";
    public static final String DAYNIGHTCYCLE_MESSAGE_STOP = "Day-night cycle stopped. Back to normal Minecraft day-night cycle.";
    public static final String DAYNIGHTCYCLE_MESSAGE_STATUS = "Day-night cycle is currently ";
    public static final String DAYNIGHTCYCLE_MESSAGE_RELOAD = "Day-night cycle configuration reloaded.";
    public static final String DAYNIGHTCYCLE_MESSAGE_USAGE = "Usage: /daynightcycle <init|start|stop|status|reload>";
    public static final String DAYNIGHTCYCLE_MESSAGE_UNKNOWN = "Unknown command. Usage: /daynightcycle <init|start|stop|status>";
}
