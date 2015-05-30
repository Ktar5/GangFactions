package com.minecave.gangs;

import com.minecave.gangs.command.CommandDistributor;
import com.minecave.gangs.gang.GangCoordinator;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.gang.HoodlumCoordinator;
import com.minecave.gangs.listener.ChunkListener;
import com.minecave.gangs.listener.PlayerListener;
import com.minecave.gangs.listener.RaidListener;
import com.minecave.gangs.sign.SignCoordinator;
import com.minecave.gangs.storage.CustomConfig;
import com.minecave.gangs.util.TimeUtil;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

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
    private CustomConfig signConfig;

    @Getter
    private HoodlumCoordinator hoodlumCoordinator;
    @Getter
    private GangCoordinator gangCoordinator;
    @Getter
    private SignCoordinator signCoordinator;

    @Getter
    private static Gangs instance = null;

    private BukkitTask offlineTimer;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        configuration = new CustomConfig(getDataFolder(), "config.yml");
        messages = new CustomConfig(getDataFolder(), "messages.yml");
        hoodlumConfig = new CustomConfig(getDataFolder(), "hoodlum.yml");
        gangConfig = new CustomConfig(getDataFolder(), "gangs.yml");
        signConfig = new CustomConfig(getDataFolder(), "signs.yml");


        hoodlumCoordinator = new HoodlumCoordinator(this);
        gangCoordinator = new GangCoordinator(this);
        signCoordinator = new SignCoordinator(this);

        gangCoordinator.loadGangs();
        signCoordinator.load();

        CommandDistributor distributor = new CommandDistributor();

        this.getCommand("g").setExecutor(distributor);
        this.getCommand("gang").setExecutor(distributor);
        this.getCommand("gangs").setExecutor(distributor);

        registerListeners();
        scheduleTimer();
    }

    private void registerListeners() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ChunkListener(this), this);
        pluginManager.registerEvents(new PlayerListener(this), this);
        pluginManager.registerEvents(new RaidListener(this), this);
    }

    @Override
    public void onDisable() {
        gangCoordinator.unloadGangs();
        signCoordinator.unload();

        offlineTimer.cancel();
        checkOfflinePlayers();

        configuration.saveConfig();
        messages.saveConfig();
        hoodlumConfig.saveConfig();
        gangConfig.saveConfig();
        signConfig.saveConfig();

        instance = null;
    }


    private void scheduleTimer() {
        //runs once an hour for offline players
        offlineTimer = this.getServer().getScheduler().runTaskTimerAsynchronously(this,
                this::checkOfflinePlayers, 0L, 20L * 60L * 60L *
                        configuration.get("power.offlineTimer", int.class));
    }

    private void checkOfflinePlayers() {
        int offlineMinutes = configuration.get("power.offlineTime", int.class);
        hoodlumConfig.getConfig().getKeys(false).forEach(s -> {
            UUID uuid = UUID.fromString(s);
            hoodlumCoordinator.loadHoodlum(uuid);
            Hoodlum hoodlum = hoodlumCoordinator.getHoodlum(uuid);
            Instant lastOffline = TimeUtil.localDateTimeToInstant(hoodlum.getLastOffline());
            if (ChronoUnit.MINUTES.between(lastOffline, Instant.now()) > offlineMinutes) {
                hoodlum.removePower(configuration.get("power.offline", int.class));
                hoodlum.setLastOffline(LocalDateTime.now());
            }
            if (!hoodlum.isOnline()) {
                hoodlumCoordinator.unloadHoodlum(uuid);
            }
        });
    }
}
