package org.ninkai.daynightcycle.commands;

import lombok.Getter;

@Getter
public final class DayNightCycleConstants {
    private DayNightCycleConstants() {}

    public static final String DAYNIGHTCYCLE_PERMISSION = "daynightcycle";
    public static final String DAYNIGHTCYCLE_COMMAND = "daynightcycle";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_INIT = "init";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_START = "start";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_STOP = "stop";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_STATUS = "status";
    public static final String DAYNIGHTCYCLE_SUBCOMMAND_RELOAD = "reload";
}
