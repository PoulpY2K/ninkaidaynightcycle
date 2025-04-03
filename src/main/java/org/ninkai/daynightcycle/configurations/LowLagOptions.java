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
public class LowLagOptions implements ConfigurationSerializable {
    @NotNull
    private Boolean enabled;
    @NotNull
    private Integer intervalHour;

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "enabled", enabled,
                "interval_hour", intervalHour
        );
    }
}
