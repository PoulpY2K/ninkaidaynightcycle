package org.ninkai.daynightcycle;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameRule;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;
import org.ninkai.daynightcycle.configurations.DayNightCycleOptions;
import org.ninkai.daynightcycle.configurations.LowLagOptions;
import org.ninkai.daynightcycle.configurations.WeatherOptions;
import org.ninkai.daynightcycle.tasks.SyncTimeTask;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_COMMAND;
import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_CYCLE_REAL_SECOND;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.convertTimeToTicks;
import static org.ninkai.daynightcycle.utils.SyncTimeUtils.getCurrentTimeWithOffset;

@Getter
@Setter
public class DayNightCycle extends JavaPlugin {

    private DayNightCycleOptions pluginConfig;
    private BukkitTask timeSyncTask;
    private BukkitScheduler scheduler;

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(LowLagOptions.class);
        ConfigurationSerialization.registerClass(WeatherOptions.class);
        ConfigurationSerialization.registerClass(DayNightCycleOptions.class);
        saveDefaultConfig();
        loadConfiguration();
    }

    @Override
    public void onEnable() {
        if (getPluginConfig() == null) {
            throw new NullPointerException("Plugin configuration is malformed. Please correct the config.yml file and use /reload or enable plugin.");
        }

        // Create the command
        DayNightCycleCommand command = new DayNightCycleCommand();
        // Set scheduler
        setScheduler(this.getServer().getScheduler());
        // Register the command
        Objects.requireNonNull(getCommand(DAYNIGHTCYCLE_COMMAND)).setExecutor(command);

        if (getPluginConfig().getEnabled()) {
            initializeSyncTimeTask();
        }
    }

    /**
     * Initializes the task to synchronize time in the specified worlds.
     * The task is scheduled to run at a fixed interval based on the cycle period.
     */
    private void initializeSyncTimeTask() {
        ZoneId timezone = ZoneId.of(getPluginConfig().getTimeZone());
        int timeOffset = getPluginConfig().getTimeOffset();
        ZonedDateTime finalCurrentTime = getCurrentTimeWithOffset(timezone, timeOffset);

        getServer().getWorlds().stream()
                .filter(world -> getPluginConfig().getWorlds().contains(world.getName()))
                .forEach(world -> {
                    world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, Boolean.FALSE);
                    world.setTime(convertTimeToTicks(finalCurrentTime));
                });

        // Create sync task
        setTimeSyncTask(scheduler.runTaskTimer(this, new SyncTimeTask(this, getPluginConfig().getWorlds()), 0L, DAYNIGHTCYCLE_CYCLE_REAL_SECOND));
    }

    /**
     * Load the configuration from the config.yml file.
     * If the file is missing values, a NullPointerException is thrown.
     */
    public void loadConfiguration() {
        // Load the config file
        try {
            setPluginConfig(DayNightCycleOptions.deserialize(getConfig().getValues(true)));
        } catch (NullPointerException e) {
            throw new NullPointerException("Plugin is missing values in configuration. Please check the config.yml file or remove it to generate a new one. Using default config as fallback.");
        }
    }

    @Override
    public void onDisable() {
        // Cancel the task if it is running
        getScheduler().getPendingTasks().forEach(task -> {
            if (task != null && !task.isCancelled()) {
                task.cancel();
            }
        });
    }
}
