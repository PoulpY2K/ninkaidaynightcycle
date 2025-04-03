package org.ninkai.daynightcycle;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.ninkai.daynightcycle.commands.DayNightCycleCommand;
import org.ninkai.daynightcycle.tasks.SyncTimeTask;

import static org.ninkai.daynightcycle.utils.TimeUtils.*;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Objects;

import static org.ninkai.daynightcycle.commands.DayNightCycleConstants.DAYNIGHTCYCLE_COMMAND;
import static org.ninkai.daynightcycle.utils.TimeUtils.convertTimeToTicks;

@Getter
@Setter
public class DayNightCycle extends JavaPlugin {

    //private DayNightCycleConfig config;
    private BukkitTask timeSyncTask;

    @Override
    public void onLoad() {
        //Try to load the config file but if not present add the default config file
        try {
            getConfig().load("config.yml");
        } catch (IOException e) {
            getLogger().warning("Could not find plugins/daynightcycle/config.yml, creating file with default values.");
            saveDefaultConfig();
        } catch (InvalidConfigurationException e) {
            getLogger().severe("Something went wrong when trying to read the config file. Please check the file.");
        }

        // Load the config file
        //config = new DayNightCycleConfig(this);
    }

    @Override
    public void onEnable() {
        // Create the command
        DayNightCycleCommand command = new DayNightCycleCommand();
        // Register the command
        Objects.requireNonNull(getCommand(DAYNIGHTCYCLE_COMMAND)).setExecutor(command);

        // Set real time in first world
        this.getServer().getWorlds().getFirst().setTime(convertTimeToTicks(getCurrentTime(ZoneId.systemDefault())));

        BukkitScheduler scheduler = this.getServer().getScheduler();
        timeSyncTask = scheduler.runTaskTimer(this, new SyncTimeTask(this), 0L, 72L);
    }

    @Override
    public void onDisable() {
        // Cancel the task if it is running
        if (timeSyncTask != null && !timeSyncTask.isCancelled()) {
            timeSyncTask.cancel();
        }
    }
}
