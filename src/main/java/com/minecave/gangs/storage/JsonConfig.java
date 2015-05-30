/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.storage;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.minecave.gangs.Gangs;
import lombok.Getter;

import java.io.*;

public class JsonConfig<T extends JsonConfigurable> {
    private String fileName;
    private File configFile;
    private Gson gson;
    private Class<T> tClass;
    @Getter
    private T object;

    public JsonConfig(Class<T> tClass, File folder, String fileName) {
        this.fileName = fileName;
        this.tClass = tClass;
        configFile = new File(folder, fileName);
        gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
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
        try {
            object = gson.fromJson(new FileReader(configFile), tClass);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            writer.write(gson.toJson(object));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Gangs.getInstance().getLogger().severe(String.format("Couldn't save '%s', because: '%s'", fileName, e.getMessage()));
        }
    }
}
