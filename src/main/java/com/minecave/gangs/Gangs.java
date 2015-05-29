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

    //@Getter
    /*
    Sorry rich, that fucker was causing too many troubles for me.
     */
    private static Gangs instance = null;

    public static Gangs getInstance() {
        return instance;
    }

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

    }

    @Override
    public void onDisable() {
        instance = null;
    }


    public GangCoordinator getGangCoordinator() {
        return gangCoordinator;
    }
}
