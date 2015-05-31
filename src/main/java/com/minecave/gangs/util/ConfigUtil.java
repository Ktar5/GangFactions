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
        String[] split = input.split(";");
        if(split.length != 3) {
            return null;
        }
        return (Bukkit.getWorld(UUID.fromString(split[0])).getChunkAt(Integer.valueOf(split[1]), Integer.valueOf(split[2])));
    }

    public static String serializeChunk(Chunk chunk) {
        return chunk.getWorld().getUID().toString() + ";" + chunk.getX() + ";" + chunk.getZ();
    }
}
