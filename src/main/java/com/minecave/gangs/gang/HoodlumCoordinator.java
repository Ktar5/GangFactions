/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.gang;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HoodlumCoordinator {

    private final Map<UUID, Hoodlum> hoodlumMap;

    public HoodlumCoordinator() {
        this.hoodlumMap = new HashMap<>();
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

    /**
     * @return returns the hoodlum of that uuid, otherwise--
     * it returns null if it cannot find the hoodlum
     * for whateverthefuck reason
     * <p/>
     * ktar: there's no reason for a containsKey check, get returns null if the map doesn't have said key
     * its a basic hashmap so its not gonna NPE as its not restricted
     */
    public Hoodlum getHoodlum(UUID playerUUID) {
        return hoodlumMap.get(playerUUID);
    }

    public Hoodlum getHoodlum(Player player) {
        return hoodlumMap.get(player.getUniqueId());
    }
}
