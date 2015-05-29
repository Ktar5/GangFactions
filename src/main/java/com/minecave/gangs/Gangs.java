package com.minecave.gangs;

import com.minecave.gangs.gang.GangCoordinator;
import com.minecave.gangs.gang.HoodlumCoordinator;
import com.minecave.gangs.storage.CustomConfig;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Gangs extends JavaPlugin {

    @Getter
    private CustomConfig configuration;
    @Getter
    private CustomConfig messages;

    @Getter
    private HoodlumCoordinator hoodlumCoordinator;
    @Getter
    private GangCoordinator gangCoordinator;

    @Getter
    private static Gangs instance = null;

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

        scheduleTimer();
    }

    @Override
    public void onDisable() {
        instance = null;
    }


    private void scheduleTimer() {
        int minutes = configuration.get("power.time", int.class);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {

        }, 0L, 20 * 60 * minutes);
    }
}
