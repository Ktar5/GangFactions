package com.minecave.gangs;

import com.minecave.gangs.gang.GangCoordinator;
import com.minecave.gangs.gang.HoodlumCoordinator;
import com.minecave.gangs.storage.CustomConfig;
import com.minecave.gangs.util.TimeUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        int onlineMinutes = configuration.get("power.onlineTime", int.class);
        int offlineMinutes = configuration.get("power.offlineTime", int.class);
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            //runs every second for offline time count
            hoodlumCoordinator.getHoodlumMap().values().forEach(h -> {
                Instant lastOnline = TimeUtil.localDateTimeToInstant(h.getLastOnline());
                if(h.getPlayer().isOnline()) {
                    if(ChronoUnit.MINUTES.between(lastOnline, Instant.now()) > onlineMinutes) {
                        h.addPower(configuration.get("power.online", int.class));
                        h.setLastOnline(LocalDateTime.now());
                    }
                }
            });
        }, 0L, 20 * 60); //runs once a minute, should be plenty of time for computation power
    }
}
