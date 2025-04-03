package org.ninkai.daynightcycle;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;
import org.ninkai.daynightcycle.tasks.SyncTimeTask;

import static org.ninkai.daynightcycle.utils.TimeUtils.*;

import java.time.ZoneId;
import java.util.Objects;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_COMMAND;
import static org.ninkai.daynightcycle.utils.TimeUtils.convertTimeToTicks;

@Getter
@Setter
public class DayNightCycle extends JavaPlugin {

    private DayNightCycleOptions pluginConfig;
    private BukkitTask timeSyncTask;
    private BukkitScheduler scheduler;

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(DayNightCycleOptions.class);

        saveDefaultConfig();
        // Load the config file
        try {
            pluginConfig = DayNightCycleOptions.deserialize(getConfig().getValues(true));
        } catch (NullPointerException e) {
            throw new NullPointerException("Plugin is missing values in configuration. Please check the config.yml file or remove it to generate a new one. Using default config as fallback.");
        }
    }

    @Override
    public void onEnable() {
        if (pluginConfig == null) {
            throw new NullPointerException("Plugin configuration is malformed. Please correct the config.yml file and use /reload or enable plugin.");
        }

        // Create the command
        DayNightCycleCommand command = new DayNightCycleCommand();
        // Register the command
        Objects.requireNonNull(getCommand(DAYNIGHTCYCLE_COMMAND)).setExecutor(command);

        // Set scheduler
        scheduler = this.getServer().getScheduler();

        // Set real time in worlds listed in config
        ZoneId timezone = ZoneId.of(pluginConfig.getTimeZone());
        getServer().getWorlds().stream()
                .filter(world -> pluginConfig.getWorlds().contains(world.getName()))
                .forEach(world ->
                        world.setTime(convertTimeToTicks(getCurrentTime(timezone)))
                );

        // Create sync task
        timeSyncTask = scheduler.runTaskTimer(this, new SyncTimeTask(this, pluginConfig.getWorlds()), 0L, 72L);
    }

    @Override
    public void onDisable() {
        // Cancel the task if it is running
        if (timeSyncTask != null && !timeSyncTask.isCancelled()) {
            timeSyncTask.cancel();
        }
    }
}
