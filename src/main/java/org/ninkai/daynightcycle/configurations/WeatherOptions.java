package org.ninkai.daynightcycle.configurations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class WeatherOptions implements ConfigurationSerializable {
    @NotNull
    private Boolean enabled;
    @NotNull
    private String apiKey;
    @NotNull
    private String city;

    public static @NotNull WeatherOptions deserialize(Map<String, Object> args) throws NullPointerException {
        return new WeatherOptions(
                (Boolean) args.get("enabled"),
                (String) args.get("api_key"),
                (String) args.get("city")
        );
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "api_key", apiKey,
                "city", city
        );
    }
}
