/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.gang;

import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;

public class GangCoordinator {

    @Getter
    private final Map<String, Gang> gangMap;

    public GangCoordinator() {
        gangMap = new HashMap<>();
    }

    //probably should fix the logic here, its kinda backwards lol

    public boolean isChunkAvailable(Chunk chunk) {
        return gangMap.values().stream().noneMatch(g -> g.isChunkClaimed(chunk));
    }

    public boolean isChunkAvailable(Block block) {
        return gangMap.values().stream().noneMatch(g -> g.isChunkClaimed(block));
    }
}

