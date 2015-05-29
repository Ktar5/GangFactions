package com.minecave.gangs;

import com.minecave.gangs.gang.GangCoordinator;
import com.minecave.gangs.gang.HoodlumCoordinator;
import com.minecave.gangs.storage.CustomConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Gangs extends JavaPlugin {

    @Getter
    private CustomConfig config;
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

        chests = new CustomConfig(getDataFolder(), "chests.yml");
        messages = new CustomConfig(getDataFolder(), "messages.yml");

        Loader.load();

        Bukkit.getServer().getPluginManager().registerEvents(new LogOffListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getCommand("rc").setExecutor(new CommandListener());
    }

    @Override
    public void onDisable() {
        Loader.unload();
        instance = null;
    }

}
