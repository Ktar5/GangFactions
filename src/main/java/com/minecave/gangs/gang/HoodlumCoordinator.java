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
import com.minecave.gangs.storage.CustomConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
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

    public enum HoodlumConfig {
        POWER,
        MAX_POWER,
        GANG_UUID,
        GANG_ROLE,
        LAST_ONLINE,
        LAST_OFFLINE
    }

    public Hoodlum loadHoodlum(UUID uuid) {
        CustomConfig config = gangs.getHoodlumConfig();
        String uuidString = uuid.toString();
        Hoodlum hoodlum = new Hoodlum(uuid);
        if (config.getConfig().contains(uuidString)) {
            hoodlum.setPower(config.get(uuidString + "." + HoodlumConfig.POWER, Integer.class));
            hoodlum.setMaxPower(config.get(uuidString + "." + HoodlumConfig.MAX_POWER, Integer.class));
            hoodlum.setGangUUID(UUID.fromString(config.get(uuidString + "." + HoodlumConfig.GANG_UUID, String.class)));
            hoodlum.setGang(gangs.getGangCoordinator().getGang(hoodlum.getGangUUID()));
            hoodlum.setRole(GangRole.valueOf(config.get(uuidString + "." + HoodlumConfig.GANG_ROLE, String.class)));
            hoodlum.setLastOnline(LocalDateTime.parse(config.get(uuidString + "." + HoodlumConfig.LAST_ONLINE, String.class)));
            hoodlum.setLastOffline(LocalDateTime.parse(config.get(uuidString + "." + HoodlumConfig.LAST_OFFLINE, String.class)));
        }
        hoodlumMap.put(uuid, hoodlum);
        return hoodlum;
    }

    public Hoodlum loadHoodlum(Player player) {
        return loadHoodlum(player.getUniqueId());
    }

    public void unloadHoodlum(Player player) {
        unloadHoodlum(player.getUniqueId());
    }

    public void unloadHoodlum(UUID uuid) {
        CustomConfig config = gangs.getHoodlumConfig();
        String uuidString = uuid.toString();
        Hoodlum hoodlum = hoodlumMap.get(uuid);
        config.set(uuidString + "." + HoodlumConfig.POWER, hoodlum.getPower());
        config.set(uuidString + "." + HoodlumConfig.MAX_POWER, hoodlum.getMaxPower());
        config.set(uuidString + "." + HoodlumConfig.GANG_UUID, hoodlum.getGangUUID().toString());
        config.set(uuidString + "." + HoodlumConfig.GANG_ROLE, hoodlum.getRole().toString());
        config.set(uuidString + "." + HoodlumConfig.LAST_ONLINE, hoodlum.getLastOnline().toString());
        config.set(uuidString + "." + HoodlumConfig.LAST_OFFLINE, hoodlum.getLastOffline().toString());
        hoodlumMap.remove(uuid);
    }

    public Hoodlum getHoodlum(UUID playerUUID) {
        return hoodlumMap.get(playerUUID);
    }

    public Hoodlum getHoodlum(Player player) {
        return hoodlumMap.get(player.getUniqueId());
    }

    public Hoodlum getHoodlum(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            return null;
        }
        return hoodlumMap.get(player.getUniqueId());
    }
}
