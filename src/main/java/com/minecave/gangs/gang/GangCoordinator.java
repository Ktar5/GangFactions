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
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import com.minecave.gangs.util.ConfigUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GangCoordinator {

    @Getter
    private final Map<String, Gang> gangMap;
    @Getter
    private final Gangs gangs;

    public GangCoordinator(Gangs gangs) {
        gangMap = new HashMap<>();
        this.gangs = gangs;
    }

    //probably should fix the logic here, its kinda backwards lol

    public static boolean isChunkClaimed(Chunk chunk) {
        return Gangs.getInstance().getGangCoordinator().getGangMap().values().stream().anyMatch(g -> g.isChunkClaimed(chunk));
    }

    //NOTE: farmable and spawn chunks should be found via the Gang object
    // which can be referenced from the Hoodlum object

    public void createGang(String name, Player owner) {
        String keyName = name.toLowerCase();
        if (!gangMap.containsKey(keyName)) {
            Gang gang = new Gang(name, owner);
            gangMap.put(keyName, gang);
        }
    }

    public void disbandGang(String name, boolean silent) {
        String keyName = name.toLowerCase();
        if (gangMap.containsKey(keyName)) {
            Gang gang = gangMap.remove(keyName);
            gang.getMembers().forEach(u -> {
                Hoodlum h = gangs.getHoodlumCoordinator().getHoodlum(u);
                if(h != null) {
                    h.setGang(null);
                    h.setGangUUID(null);
                    h.setRole(GangRole.GANGLESS);
                }
            });
            if(!silent){
                Bukkit.getServer().broadcastMessage(Messages.get("disbandFaction",
                        MsgVar.GANG.var(), gang.getName()));
            }
        }
    }

    public enum GangConfig {
        NAME,
        OWNER,
        MEMBERS,
        CLAIMS,
        TOTAL_FARM,
        HOME,
        LAST_ONLINE
    }

    public void loadGangs() {
        gangs.getGangConfig().getConfig().getKeys(false).stream().forEach(this::loadGang);
    }

    private Gang loadGang(String uuidString) {
        return loadGang(UUID.fromString(uuidString));
    }

    private Gang loadGang(UUID gangUUID) {
        CustomConfig config = gangs.getGangConfig();
        String uuidString = gangUUID.toString();
        if (config.getConfig().contains(uuidString)) {
            String name = config.get(uuidString + "." + GangConfig.NAME, String.class);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(config.get(uuidString + "." + GangConfig.OWNER, String.class)));
            Gang gang = new Gang(name, owner);
            gang.setTotalFarm(config.get(uuidString + "." + GangConfig.TOTAL_FARM, Integer.class));
            gang.setHome(ConfigUtil.deserializeLocation(config.get(uuidString + "." + GangConfig.HOME, String.class)));
            gang.setLastOnline(LocalDateTime.parse(config.get(uuidString + "." + GangConfig.LAST_ONLINE, String.class)));
            List<String> membersList = config.getConfig().getStringList(uuidString + "." + GangConfig.MEMBERS);
            for(String s : membersList) {
                gang.addMember(UUID.fromString(s));
            }
            List<String> claimsList = config.getConfig().getStringList(uuidString + "." + GangConfig.CLAIMS);
            for(String s : claimsList) {
                gang.getClaims().add(ConfigUtil.deserializeChunk(s));
            }

            gangMap.put(name.toLowerCase(), gang);
            return gang;
        }
        return null;
    }

    public void unloadGangs() {
        gangMap.values().forEach(this::unloadGang);
    }

    private void unloadGang(Gang gang) {
        CustomConfig config = gangs.getGangConfig();
        String uuidString = gang.getUuid().toString();

        config.set(uuidString + "." + GangConfig.NAME, gang.getName());
        config.set(uuidString + "." + GangConfig.OWNER, gang.getOwner().getUniqueId().toString());
        config.set(uuidString + "." + GangConfig.TOTAL_FARM, gang.getTotalFarm());
        config.set(uuidString + "." + GangConfig.HOME, ConfigUtil.serializeLocation(gang.getHome()));
        config.set(uuidString + "." + GangConfig.LAST_ONLINE, gang.getLastOnline().toString());

        List<String> membersList = gang.getMembers().stream().map(UUID::toString).collect(Collectors.toList());
        config.set(uuidString + "." + GangConfig.MEMBERS, membersList);
        List<String> claimsList = gang.getClaims().stream().map(ConfigUtil::serializeChunk).collect(Collectors.toList());
        config.set(uuidString + "." + GangConfig.CLAIMS, claimsList);

        gangMap.remove(gang.getName().toLowerCase());
    }

    public Gang getGang(Chunk chunk) {
        for(Gang gang : gangMap.values()) {
            if(gang.getClaims().contains(chunk)) {
                return gang;
            }
        }
        return null;
    }

    public void updateGangName(String name, Gang gang) {
        String keyName = name.toLowerCase();
        if(!gangMap.containsKey(keyName)) {
            gangMap.remove(gang.getName().toLowerCase());
            gang.setName(name);
            gangMap.put(keyName, gang);
        }
    }

    public boolean gangExists(String name){
        return gangMap.containsKey(name.toLowerCase());
    }


    public boolean gangExists(UUID uuid) {
        for(Gang gang : gangMap.values()) {
            if(gang.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public Gang getGang(String name){
        return gangMap.get(name.toLowerCase());
    }

    public Gang getGang(UUID gangUUID) {
        for(Gang gang : gangMap.values()) {
            if(gang.getUuid().equals(gangUUID)) {
                return gang;
            }
        }
        return null;
    }
}

