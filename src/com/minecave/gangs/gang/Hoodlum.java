package com.minecave.gangs.gang;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Carter on 5/25/2015.
 */
public class Hoodlum {

    private static final Map<UUID, Hoodlum> hoodlums = new HashMap<>();

    public static boolean removeHoodlum(UUID playerUUID){
        if(hoodlums.containsKey(playerUUID)){
            hoodlums.remove(playerUUID);
            return true;
        }
        return false;
    }

    public static boolean addHoodlum(Hoodlum hoodlum){
        if(!hoodlums.containsKey(hoodlum.playerUUID)){
            hoodlums.put(hoodlum.playerUUID, hoodlum);
            return true;
        }
        return false;
    }

    public Hoodlum getHoodlum(UUID playerUUID){
        if(){

        }
    }



    private final UUID playerUUID;
    private int power, maxPower;

    public Hoodlum(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }
}
