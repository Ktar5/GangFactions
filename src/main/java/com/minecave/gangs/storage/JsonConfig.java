/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.storage;

import com.minecave.gangs.Gangs;

import java.io.File;

public class JsonConfig<T extends JsonConfigurable> {
    private String fileName;
    private File configFile;
    private T object;

    public JsonConfig(File folder, String fileName) {
        this.fileName = fileName;
        configFile = new File(folder, fileName);
        reloadConfig();
    }

    public String getFileName() {
        return fileName;
    }

    public File getConfigFile() {
        return configFile;
    }

    public void reloadConfig() {
        if (!configFile.exists())
            Gangs.getInstance().saveResource(fileName, true);

    }

    public void saveConfig() {
        try {

        } catch (Exception e) {
            Gangs.getInstance().getLogger().severe(String.format("Couldn't save '%s', because: '%s'", fileName, e.getMessage()));
        }
    }
}
