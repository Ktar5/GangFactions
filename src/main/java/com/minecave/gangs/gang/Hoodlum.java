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

    /**
     * @return returns the hoodlum of that uuid, otherwise--
     * it returns null if it cannot find the hoodlum
     * for whateverthefuck reason
     */
    public static Hoodlum getHoodlum(UUID playerUUID){
        if(hoodlums.containsKey(playerUUID))
            return hoodlums.get(playerUUID);
        return null;
    }


    //-----------------------HOODLUM OBJECT---------------------------------------------------
    private final UUID playerUUID;
    private int power, maxPower;

    public Hoodlum(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    /**
     * Loads things from the database
     */
    private void loadPlayerFromUuid(){

    }

    public int getPower(){
        return this.power;
    }

    public void setPower(int amount){
        power = amount;
    }

    public int getMaxPower(){
        return this.maxPower;
    }

    public void addPower(int amount){
        addPower(amount, false);
    }

    public void addPower(int amount, boolean force){
        power = (power + amount > maxPower ? (force ? amount+power : maxPower) : amount+power);
    }

    public void removePower(int amount){
        removePower(amount, false);
    }

    public void removePower(int amount, boolean force){
        power = (power - amount < -maxPower ? (force ? power-amount : maxPower) : power-amount);
    }

    public Gang getGang(){

    }

}
