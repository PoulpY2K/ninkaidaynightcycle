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

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "api_key", apiKey,
                "city", city
        );
    }
}
