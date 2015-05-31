package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.gang.GangCoordinator;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import org.bukkit.Chunk;

/**
 * Created by Carter on 5/27/2015.
 */
public class Management {

    public static void disband(Hoodlum player) {
        Gangs.getInstance().getGangCoordinator().disbandGang(player.getGang().getName(), false);
    }

    public static void claim(Hoodlum player) {
        Chunk chunk = player.getPlayer().getLocation().getChunk();
        if(player.isInGang()){
            if(GangCoordinator.isChunkClaimed(chunk)){
                Gang gang = Gangs.getInstance().getGangCoordinator().getGang(chunk);
                if(gang.equals(player.getGang())){
                    player.sendMessage(Messages.get("alreadyClaimed"));
                }else {
                    player.sendMessage(Messages.get("claimedByAnother", MsgVar.GANG.var(), gang.getName()));
                }
            }else{
                if(player.getGang().canClaimChunks()){
                    player.getGang().claimChunk(chunk);
                    player.sendMessage(Messages.get("chunkClaimed"));
                }else{
                    player.sendMessage(Messages.get("notEnoughPower", MsgVar.POWER.var(), String.valueOf(player.getGang().getPower())));
                }
            }
        }player.sendMessage(Messages.get("notInGang"));
    }

    public static void unclaim(Hoodlum player) {
        Chunk chunk = player.getPlayer().getLocation().getChunk();
        if(!GangCoordinator.isChunkClaimed(chunk)){
            player.sendMessage(Messages.get("notClaimed"));
        }else{
            Gang gang = Gangs.getInstance().getGangCoordinator().getGang(chunk);
            if(gang.equals(player.getGang())){
                player.getGang().unclaimChunk(chunk);
                player.sendMessage(Messages.get("chunkUnclaimed"));
            }else
                player.sendMessage(Messages.get("claimedByAnother", MsgVar.GANG.var(), gang.getName()));
        }
    }

    public static void unclaimAll(Hoodlum player) {
        player.getGang().unclaimAllChunks();
        player.sendMessage(Messages.get("unclaimedAll"));
    }


    public static void setHome(Hoodlum player) {
        player.getGang().setHome(player.getPlayer().getLocation());
        player.sendMessage(Messages.get("homeSet"));
    }

    public static void kick(Hoodlum player, String playerName) {
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            if(hoodlum.getGang() != null){
                if(hoodlum.getGang().equals(player.getGang())){
                player.sendMessage(Messages.get("kickedPlayer_executor",
                        MsgVar.GANG.var(), hoodlum.getGang().getName(),
                        MsgVar.PLAYER.var(), hoodlum.getPlayer().getName()));
                hoodlum.sendMessage(Messages.get("kickedPlayer",
                        MsgVar.GANG.var(), hoodlum.getGang().getName(),
                        MsgVar.PLAYER.var(), player.getPlayer().getName()));
                hoodlum.getGang().removePlayer(hoodlum);
                }else
                    player.sendMessage(Messages.get("playerNotInGang", MsgVar.PLAYER.var(), playerName));
            }else
                player.sendMessage(Messages.get("playerNotInGang", MsgVar.PLAYER.var(), playerName));
        }else
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
    }

    public static void invite(Hoodlum player, String playerName) {
    }

    public static void promote(Hoodlum player, String playerName, GangRole role){
        Hoodlum hoodlum = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName);
        if(hoodlum != null){
            if(hoodlum.getGang() != null){
                if(hoodlum.getGang().equals(player.getGang())){
                    player.sendMessage(Messages.get("promoter",
                            MsgVar.ROLE.var(), hoodlum.getGang().getName(),
                            MsgVar.GANG.var(), hoodlum.getGang().getName(),
                            MsgVar.PLAYER.var(), hoodlum.getPlayer().getName()));
                    hoodlum.sendMessage(Messages.get("promoted",
                            MsgVar.GANG.var(), hoodlum.getGang().getName(),
                            MsgVar.ROLE.var(), hoodlum.getGang().getName(),
                            MsgVar.PLAYER.var(), player.getPlayer().getName()));
                    hoodlum.setRole(role);
                }else
                    player.sendMessage(Messages.get("playerNotInGang", MsgVar.PLAYER.var(), playerName));
            }else
                player.sendMessage(Messages.get("playerNotInGang", MsgVar.PLAYER.var(), playerName));
        }else
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
    }
}
