/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.UUID;

public class ConfigUtil {

    public static Location deserializeLocation(String input) {
        String[] split = input.split(";");
        if(split.length != 4) {
            return null;
        }
        return new Location(Bukkit.getWorld(UUID.fromString(split[0])),
                Double.parseDouble(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]));
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getUID().toString() + ";" +
                location.getX() + ";" +
                location.getY() + ";" +
                location.getZ();
    }

    public static Chunk deserializeChunk(String input) {
        Location location = deserializeLocation(input);
        if(location == null) {
            return null;
        }
        return location.getChunk();
    }

    public static String serializeChunk(Chunk chunk) {
        return serializeLocation(new Location(chunk.getWorld(), chunk.getX(), 0, chunk.getZ()));
    }
}
