package org.ninkai.daynightcycle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.GameRule;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;
import org.ninkai.daynightcycle.configurations.DayNightCycleOptions;
import org.ninkai.daynightcycle.configurations.LowLagOptions;
import org.ninkai.daynightcycle.configurations.WeatherOptions;
import org.ninkai.daynightcycle.tasks.SyncTimeRunnable;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_COMMAND;
import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_CYCLE_REAL_SECOND;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.convertTimeToTicks;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.getInstantTimeWithOffset;

@Getter
public class DayNightCycle extends JavaPlugin {

    private DayNightCycleOptions pluginConfig;

    /**
     * Registers serialization classes for configuration options and loads the configuration.
     */
    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(LowLagOptions.class);
        ConfigurationSerialization.registerClass(WeatherOptions.class);
        ConfigurationSerialization.registerClass(DayNightCycleOptions.class);
        saveDefaultConfig();
        loadConfiguration();
    }

    /**
     * Initializes the plugin, sets up the command executor, and starts the time synchronization task if enabled.
     */
    @Override
    public void onEnable() {
        // Check that we successfully
        if (pluginConfig == null) {
            throw new NullPointerException("Plugin configuration is malformed. Please fix the config.yml file and use /reload.");
        }

        // Get plugin command and check if it exists
        PluginCommand pluginCommand = getCommand(DAYNIGHTCYCLE_COMMAND);
        if (pluginCommand == null) {
            getLogger().warning("Plugin command is null. Please check the plugin.yml file.");
            return;
        }

        // Register the command executor
        pluginCommand.setExecutor(new DayNightCycleCommand());

        // Starts the time synchronization task if plugin is enabled in the config
        if (pluginConfig.getEnabled()) {
            // Prepare the worlds specified in the config by setting the GameRule doDaylightCycle to false
            // and setting the time to the current time with offset in ticks
            prepareWorlds();

            // Create the sync task and set it
            getServer().getScheduler().runTaskTimer(
                    this,
                    new SyncTimeRunnable(this, pluginConfig.getWorlds()),
                    0L,
                    DAYNIGHTCYCLE_CYCLE_REAL_SECOND
            );
        }
    }

    /**
     * Cancels the time synchronization task if it is running on disable.
     */
    @Override
    public void onDisable() {
        // Cancel the tasks if they are running
        getServer().getScheduler().getPendingTasks().forEach(task -> {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        });
    }

    /**
     * Load the configuration from the config.yml file.
     * If the file is missing values, a NullPointerException is thrown.
     */
    public void loadConfiguration() {
        // Load the config file
        try {
            pluginConfig = DayNightCycleOptions.deserialize(getConfig().getValues(true));
        } catch (NullPointerException e) {
            throw new NullPointerException("Plugin is missing values in configuration. Please check the config.yml file or remove it to generate a new one.");
        }
    }

    /**
     * Prepares the worlds by setting the GameRule doDaylightCycle to false and setting the time
     */
    public void prepareWorlds() {
        // Set GameRule doDaylightCycle to false in all worlds included in pluginConfig
        // and set the time to the current time with offset in ticks
        getServer().getWorlds().stream().filter(world -> pluginConfig.getWorlds().contains(world.getName())).forEach(world -> {
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
            world.setTime(convertTimeToTicks(getWorldTime()));
        });
    }

    /**
     * Gets the current time with the specified timezone and offset.
     *
     * @return The current time with offset.
     */
    public ZonedDateTime getWorldTime() {
        ZoneId timezone = ZoneId.of(pluginConfig.getTimeZone());
        int timeOffset = pluginConfig.getTimeOffset();
        return getInstantTimeWithOffset(timezone, timeOffset);
    }
}
