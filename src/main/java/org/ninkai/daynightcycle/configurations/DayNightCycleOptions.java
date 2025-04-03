package org.ninkai.daynightcycle.configurations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.ninkai.daynightcycle.DayNightCycle;

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
    private LowLagOptions lowLag;
    @NotNull
    private String fixedTime;
    @NotNull
    private WeatherOptions weather;

    @SuppressWarnings("unchecked")
    public static @NotNull DayNightCycleOptions deserialize(Map<String, Object> args) {

        LowLagOptions lowLagOptions = new LowLagOptions(
                (Boolean) args.get("low_lag.enabled"),
                (Integer) args.get("low_lag.interval_hour")
        );
        WeatherOptions weatherOptions = new WeatherOptions(
                (Boolean) args.get("weather.enabled"),
                (String) args.get("weather.api_key"),
                (String) args.get("weather.city")
        );
        return new DayNightCycleOptions(
                (Boolean) args.get("enabled"),
                (List<String>) args.get("worlds"),
                (String) args.get("timezone"),
                (Integer) args.get("time_offset"),
                lowLagOptions,
                (String) args.get("fixed_time"),
                weatherOptions
        );
    }

    public static void saveDayNightCycleConfig(DayNightCycle plugin, Map<String, Object> options) {
        // Save the configuration to the plugin's config file
        options.forEach((key, value) -> plugin.getConfig().set(key, value));
        plugin.saveConfig();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "worlds", worlds,
                "timezone", timeZone,
                "time_offset", timeOffset,
                "low_lag", lowLag.serialize(),
                "fixed_time", fixedTime,
                "weather", weather.serialize()
        );
    }
}
