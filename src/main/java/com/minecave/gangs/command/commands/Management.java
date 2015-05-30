package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import org.bukkit.Chunk;

/**
 * Created by Carter on 5/27/2015.
 */
public class Management {

    public static void disband(Hoodlum player) {

    }

    public static void claim(Hoodlum player) {
        Chunk chunk = player.getPlayer().getLocation().getChunk();
        if(Gangs.getInstance().getGangCoordinator().isChunkClaimed(chunk)){
            Gang gang = Gangs.getInstance().getGangCoordinator().getGang(chunk);
            if(gang.equals(player.getGang())){
                player.sendMessage(Messages.get("alreadyClaimed"));
            }else
                player.sendMessage(Messages.get("claimedByAnother", MsgVar.GANG.var(), gang.getName()));
        }else player.sendMessage(Messages.get("chunkClaimed"));
    }

    public static void unclaim(Hoodlum player) {

    }

    public static void unclaimAll(Hoodlum player) {

    }


    public static void setHome(Hoodlum player) {

    }

    public static void kick(Hoodlum player, String para) {
    }

    public static void invite(Hoodlum player, String para) {
    }

    public static void supermod(Hoodlum player, String para) {
    }

    public static void mod(Hoodlum player, String para) {
    }

    public static void leader(Hoodlum player, String para) {
    }
}
