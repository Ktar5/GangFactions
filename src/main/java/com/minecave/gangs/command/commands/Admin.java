package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Carter on 5/27/2015.
 */
public class Admin {

    public static void breakIn(Hoodlum player, Gang gang) {
        if(gang.getHome() != null){
            player.getPlayer().teleport(gang.getHome());
            player.sendMessage(Messages.get("gang.home.welcome", MsgVar.GANG.var(), gang.getName()));
        }else{
            player.sendMessage(Messages.get("gang.home.noHome"));
        }
    }

    public static void disband(Hoodlum player, Gang gang, boolean silent) {
        Gangs.getInstance().getGangCoordinator().disbandGang(gang.getName(), silent);
    }

    public static void power(Hoodlum player, String playerName) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            player.sendMessage(Messages.get("power.admin",
                    MsgVar.PLAYER.var(), playerName,
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
        }else{
            player.sendMessage(Messages.get("player.noExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    public static void join(Hoodlum player, Gang gang) {
        if(player.isInGang()){
            player.sendMessage(Messages.get("gang.create.alreadyInGang",
                    MsgVar.GANG.var(), player.getGang().getName(),
                    MsgVar.ROLE.var(), player.getRole().toString()));
            return;
        }
        gang.addPlayer(player);
        String message = Messages.get("gang.join.playerJoined", MsgVar.PLAYER.var(), player.getName());
        for(UUID uuid : gang.getMembers()){
            Player p = Bukkit.getPlayer(uuid);
            if(p.isOnline()){
                p.sendMessage(message);
            }
        }
        player.sendMessage(Messages.get("gang.join.youJoined", MsgVar.GANG.var(), gang.getName()));
    }

    public static void kick(Hoodlum player, String playerName) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            if(hoodlum.getGang() != null) {
                player.sendMessage(Messages.get("gang.kick.kicker",
                        MsgVar.GANG.var(), hoodlum.getGang().getName(),
                        MsgVar.PLAYER.var(), hoodlum.getName()));
                hoodlum.sendMessage(Messages.get("gang.kick.kicked",
                        MsgVar.GANG.var(), hoodlum.getGang().getName(),
                        MsgVar.PLAYER.var(), player.getName()));
                hoodlum.getGang().removePlayer(hoodlum);
            }else{
                player.sendMessage(Messages.get("gang.error.notInGang", MsgVar.PLAYER.var(), playerName));
            }
        }else{
            player.sendMessage(Messages.get("player.noExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    public static void setMaxPower(Hoodlum player, String playerName, int amount) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            hoodlum.setMaxPower(amount);
            hoodlum.sendMessage(Messages.get("power.max.set",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
            player.sendMessage(Messages.get("power.max.setter",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower()),
                    MsgVar.PLAYER.var(), hoodlum.getName()));
        }else{
            player.sendMessage(Messages.get("player.noExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    public static void takePower(Hoodlum player, String playerName, int amount) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            hoodlum.removePower(amount, true);
            hoodlum.sendMessage(Messages.get("power.take.taken",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
            player.sendMessage(Messages.get("power.take.taker",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower()),
                    MsgVar.PLAYER.var(), hoodlum.getName()));
        }else{
            player.sendMessage(Messages.get("player.noExist", MsgVar.PLAYER.var(), playerName));
        }
    }

    public static void addPower(Hoodlum player, String playerName, int amount) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            hoodlum.addPower(amount, true);
            hoodlum.sendMessage(Messages.get("power.add.added",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower())));
            player.sendMessage(Messages.get("power.add.adder",
                    MsgVar.POWER.var(), String.valueOf(hoodlum.getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(hoodlum.getMaxPower()),
                    MsgVar.PLAYER.var(), hoodlum.getName()));
        }else{
            player.sendMessage(Messages.get("player.noExist", MsgVar.PLAYER.var(), playerName));
        }
    }
}
