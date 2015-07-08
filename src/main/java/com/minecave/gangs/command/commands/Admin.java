package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;

/**
 * Created by Carter on 5/27/2015.
 */
public class Admin {

    public static void breakIn(Hoodlum player, Gang gang) {
        if(gang.getHome() != null){
            player.getPlayer().teleport(gang.getHome());
            player.sendMessage(Messages.get("welcomeHome", MsgVar.GANG.var(), gang.getName()));
        }else{
            player.sendMessage(Messages.get("noHome"));
        }
    }

    public static void disband(Hoodlum player, Gang gang, boolean silent) {
        Gangs.getInstance().getGangCoordinator().disbandGang(gang.getName(), silent);
    }

    public static void power(Hoodlum player, String playerName) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            player.sendMessage(Messages.get("power",
                    MsgVar.PLAYER.var(), playerName,
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
        }else{
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    @Deprecated
    public static void info(Hoodlum player, Gang gang) {

    }

    public static void join(Hoodlum player, Gang gang) {
        gang.addPlayer(player);
        player.sendMessage(Messages.get("joinedGang", MsgVar.GANG.var(), gang.getName()));
    }

    public static void kick(Hoodlum player, String playerName) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            if(hoodlum.getGang() != null) {
                player.sendMessage(Messages.get("kickedPlayer_executor",
                        MsgVar.GANG.var(), hoodlum.getGang().getName(),
                        MsgVar.PLAYER.var(), hoodlum.getPlayer().getName()));
                hoodlum.sendMessage(Messages.get("kickedPlayer",
                        MsgVar.GANG.var(), hoodlum.getGang().getName(),
                        MsgVar.PLAYER.var(), player.getPlayer().getName()));
                hoodlum.getGang().removePlayer(hoodlum);
            }else{
                player.sendMessage(Messages.get("playerNotInGang", MsgVar.PLAYER.var(), playerName));
            }
        }else{
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    public static void setMaxPower(Hoodlum player, String playerName, int amount) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            hoodlum.setMaxPower(amount);
            hoodlum.sendMessage(Messages.get("maxPowerSet",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
            player.sendMessage(Messages.get("setMaxPower",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower()),
                    MsgVar.PLAYER.var(), hoodlum.getPlayer().getName()));
        }else{
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    public static void takePower(Hoodlum player, String playerName, int amount) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            hoodlum.removePower(amount, true);
            hoodlum.sendMessage(Messages.get("powerTaken",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
            player.sendMessage(Messages.get("takePower",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower()),
                    MsgVar.PLAYER.var(), hoodlum.getPlayer().getName()));
        }else{
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    public static void addPower(Hoodlum player, String playerName, int amount) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            hoodlum.addPower(amount, true);
            hoodlum.sendMessage(Messages.get("powerAdded",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
            player.sendMessage(Messages.get("addPower",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower()),
                    MsgVar.PLAYER.var(), hoodlum.getPlayer().getName()));
        }else{
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
        }
    }
}
