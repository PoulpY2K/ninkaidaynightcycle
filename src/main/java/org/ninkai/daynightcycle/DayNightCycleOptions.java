package org.ninkai.daynightcycle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class DayNightCycleOptions implements ConfigurationSerializable {

    @NotNull
    private Boolean enabled;
    @NotNull
    private List<String> worlds;
    @NotNull
    private String timeZone;
    @NotNull
    private Integer timeOffset;
    @NotNull
    private Boolean hourMode;
    @NotNull
    private Integer intervalHour;
    @NotNull
    private String fixedTime;

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "worlds", worlds,
                "timezone", timeZone,
                "time_offset", timeOffset,
                "hour_mode", hourMode,
                "interval_hour", intervalHour,
                "fixed_time", fixedTime
        );
    }

    @SuppressWarnings("unchecked")
    public static @NotNull DayNightCycleOptions deserialize(Map<String, Object> args) {
        return new DayNightCycleOptions(
                (Boolean) args.get("enabled"),
                (List<String>) args.get("worlds"),
                (String) args.get("timezone"),
                (Integer) args.get("time_offset"),
                (Boolean) args.get("hour_mode"),
                (Integer) args.get("interval_hour"),
                (String) args.get("fixed_time")
        );
    }
}
