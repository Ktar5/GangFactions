/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoodlumCoordinator {

    @Getter
    private final Map<UUID, Hoodlum> hoodlumMap;
    @Getter
    private final Gangs gangs;

    public HoodlumCoordinator(Gangs gangs) {
        this.hoodlumMap = new HashMap<>();
        this.gangs = gangs;
    }

    public boolean removeHoodlum(UUID playerUUID) {
        if (hoodlumMap.containsKey(playerUUID)) {
            hoodlumMap.remove(playerUUID);
            return true;
        }
        return false;
    }

    public boolean addHoodlum(Hoodlum hoodlum) {
        if (!hoodlumMap.containsKey(hoodlum.getPlayerUUID())) {
            hoodlumMap.put(hoodlum.getPlayerUUID(), hoodlum);
            return true;
        }
        return false;
    }

    @Deprecated
    public Hoodlum getHoodlum(String playerName){return null;}

    public Hoodlum getHoodlum(UUID playerUUID) {
        return hoodlumMap.get(playerUUID);
    }

    public Hoodlum getHoodlum(Player player) {
        return hoodlumMap.get(player.getUniqueId());
    }
}
