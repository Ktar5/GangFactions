/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.util;

import com.minecave.gangs.Gangs;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;

public class ChunkUtil {

    private final String gmap = "gmap.";
    private final char[] chars;

    public ChunkUtil() {
        chars = new char[26];
        for(int i = 0; i < 26; i++) {
            chars[i] = (char)(i + 65);
        }

    }

    public String[] generateClaimMap(Player player) {
        List<String> otherChars = Gangs.getInstance().getMessages().getConfig().getStringList(gmap + "symbols");
        char[] chars = new char[ChunkUtil.chars.length + otherChars.size()];
        for (int i = 0; i < chars.length; i++) {

        }
    }
}
