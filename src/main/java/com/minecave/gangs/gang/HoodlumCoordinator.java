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
import com.minecave.gangs.util.UUIDFetcher;
import lombok.Getter;
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
        MESSAGES,
        LAST_OFFLINE
    }

    public Hoodlum loadHoodlum(UUID uuid) {
        CustomConfig config = gangs.getHoodlumConfig();
        String uuidString = uuid.toString();
        Hoodlum hoodlum = new Hoodlum(uuid);
        if (config.getConfig().contains(uuidString)) {
            hoodlum.setPower(config.get(uuidString + "." + HoodlumConfig.POWER, Integer.class));
            hoodlum.setMaxPower(config.get(uuidString + "." + HoodlumConfig.MAX_POWER, Integer.class));
            if(config.has(uuidString + "." + HoodlumConfig.GANG_UUID)){
                hoodlum.setGangUUID(UUID.fromString(config.get(uuidString + "." + HoodlumConfig.GANG_UUID, String.class)));
            }
            hoodlum.setGang(gangs.getGangCoordinator().getGang(hoodlum.getGangUUID()));
            hoodlum.setRole(GangRole.valueOf(config.get(uuidString + "." + HoodlumConfig.GANG_ROLE, String.class)));
            hoodlum.setLastLogon(LocalDateTime.parse(config.get(uuidString + "." + HoodlumConfig.LAST_ONLINE, String.class)));
            hoodlum.setLastLogoff(LocalDateTime.parse(config.get(uuidString + "." + HoodlumConfig.LAST_OFFLINE, String.class)));

            if(config.has(uuidString + "." + HoodlumConfig.MESSAGES)) {
                hoodlum.loadNotices(config.getConfig().getStringList(uuidString + "." + HoodlumConfig.MESSAGES));
            }

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
        String uuidString = uuid.toString();
        Hoodlum hoodlum = hoodlumMap.get(uuid);
        gangs.getHoodlumConfig().set(uuidString + "." + HoodlumConfig.POWER, hoodlum.getPower());
        gangs.getHoodlumConfig().set(uuidString + "." + HoodlumConfig.MAX_POWER, hoodlum.getMaxPower());
        if(hoodlum.isInGang()){
            gangs.getHoodlumConfig().set(uuidString + "." + HoodlumConfig.GANG_UUID, hoodlum.getGangUUID().toString());
        }
        gangs.getHoodlumConfig().set(uuidString + "." + HoodlumConfig.GANG_ROLE, hoodlum.getRole().toString());
        gangs.getHoodlumConfig().set(uuidString + "." + HoodlumConfig.LAST_ONLINE, hoodlum.getLastLogon().toString());
        gangs.getHoodlumConfig().set(uuidString + "." + HoodlumConfig.LAST_OFFLINE, hoodlum.getLastLogoff().toString());
        gangs.getHoodlumConfig().set(uuidString + "." + HoodlumConfig.MESSAGES, hoodlum.getNotices());
    }

    public Hoodlum getHoodlum(UUID playerUUID) {
        if(hoodlumMap.containsKey(playerUUID)){
            return hoodlumMap.get(playerUUID);
        }else{
            return loadHoodlum(playerUUID);
        }
    }

    public Hoodlum getHoodlum(Player player) {
        return getHoodlum(player.getUniqueId());
    }

    public Hoodlum getHoodlum(String name) {
        try {
            UUID uuid = UUIDFetcher.getUUIDOf(name.toLowerCase());
            return getHoodlum(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
