package com.minecave.gangs.storage;

import com.minecave.gangs.Gangs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author Ktar5
 */
public class CustomConfig {
    private String fileName;
    private FileConfiguration config;
    private File configFile;

    public CustomConfig(File folder, String fileName) {
        this.fileName = fileName;
        configFile = new File(folder, fileName);
        reloadConfig();
    }

    public String getFileName() {
        return fileName;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void reloadConfig() {
        if (!configFile.exists())
            Gangs.getInstance().saveResource(fileName, true);
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (Exception e) {
            Gangs.getInstance().getLogger().severe(String.format("Couldn't save '%s', because: '%s'", fileName, e.getMessage()));
        }
    }

    public void set(String path, Object value, boolean save) {
        config.set(path, value);
        if (save)
            saveConfig();
    }

    public void set(String path, Object value) {
        set(path, value, false);
    }
}
