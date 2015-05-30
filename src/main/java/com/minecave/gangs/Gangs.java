package com.minecave.gangs;

import com.minecave.gangs.gang.GangCoordinator;
import com.minecave.gangs.gang.HoodlumCoordinator;
import com.minecave.gangs.storage.CustomConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Gangs extends JavaPlugin {

    @Getter
    private CustomConfig configuration;
    @Getter
    private CustomConfig messages;
    @Getter
    private CustomConfig hoodlumConfig;
    @Getter
    private CustomConfig gangConfig;

    @Getter
    private HoodlumCoordinator hoodlumCoordinator;
    @Getter
    private GangCoordinator gangCoordinator;

    @Getter
    private static Gangs instance = null;

    private BukkitTask offlineTimer;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        hoodlumCoordinator = new HoodlumCoordinator(this);
        gangCoordinator = new GangCoordinator(this);

        configuration = new CustomConfig(getDataFolder(), "config.yml");
        messages = new CustomConfig(getDataFolder(), "messages.yml");
        hoodlumConfig = new CustomConfig(getDataFolder(), "hoodlum.yml");
        gangConfig = new CustomConfig(getDataFolder(), "gangs.yml");

        gangCoordinator.loadGangs();
        scheduleTimer();
    }

    @Override
    public void onDisable() {
        gangCoordinator.unloadGangs();
        offlineTimer.cancel();
        checkOfflinePlayers();
        instance = null;
    }


    private void scheduleTimer() {
        //runs once an hour for offline players
        offlineTimer = this.getServer().getScheduler().runTaskTimerAsynchronously(this,
                this::checkOfflinePlayers, 0L, 20L * 60L * 60L);
    }

    private void checkOfflinePlayers() {
        int offlineMinutes = configuration.get("power.offlineTime", int.class);

    }
}
