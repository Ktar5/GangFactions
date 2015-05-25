package com.minecave.gangs;

import com.minecave.gangs.storage.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Gangs extends JavaPlugin{

	private static Gangs instance = null;

	public static CustomConfig chests, items, messages, chesttypes;

	@Override
	public void onLoad(){
		instance = this;
	}

	@Override
	public void onEnable(){
		chests = new CustomConfig(getDataFolder(), "chests.yml");
		messages = new CustomConfig(getDataFolder(), "messages.yml");

        Loader.load();

        Bukkit.getServer().getPluginManager().registerEvents(new LogOffListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getCommand("rc").setExecutor(new CommandListener());
    }

	@Override
	public void onDisable(){
        Loader.unload();
		instance = null;
	}

	public static Gangs getInstance(){
		return instance;
	}

}
