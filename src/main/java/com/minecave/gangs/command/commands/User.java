package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import com.minecave.gangs.util.ChunkOutliner.ChunkOutliner;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carter on 5/27/2015.
 */
public class User {
    public static void leave(Hoodlum player) {
        if(player.hasRole(GangRole.LEADER)){
            Management.disband(player);
        }else{
            player.sendMessage(Messages.get("gang.leave", MsgVar.GANG.var(), player.getGang().getName()));
            player.getGang().removePlayer(player);
        }
    }

    public static void goHome(Hoodlum player) {
        if(player.getGang().getHome() != null){
            player.getPlayer().teleport(player.getGang().getHome());
            player.sendMessage(Messages.get("gang.home.welcome",
                    MsgVar.GANG.var(), player.getGang().getName()));
        }else{
            player.sendMessage(Messages.get("gang.home.noHome"));
        }
    }

    public static void power(Hoodlum player) {
        player.sendMessage(Messages.get("power.self",
                MsgVar.POWER.var(), String.valueOf(player.getPower()),
                MsgVar.MAX_POWER.var(), String.valueOf(player.getMaxPower())));
    }

    public static void info(Hoodlum player) {

    }

    public static void create(Hoodlum player, String gangname) {
        if(player.getGang() == null){
            if(!Gangs.getInstance().getGangCoordinator().gangExists(gangname)){
                Gangs.getInstance().getGangCoordinator().createGang(gangname, player);
                Bukkit.getServer().broadcastMessage(Messages.get("gang.create.success",
                        MsgVar.GANG.var(), gangname,
                        MsgVar.PLAYER.var(), player.getPlayer().getName()));
            }else player.sendMessage(Messages.get("gang.create.nameAlreadyExists", MsgVar.GANG.var(), gangname));
        }else player.sendMessage(Messages.get("gang.create.alreadyInGang",
                MsgVar.GANG.var(), player.getGang().getName(),
                MsgVar.ROLE.var(), player.getRole().toString()));
    }

    public static void acceptInvite(Hoodlum player, String para) {
        if(player.hasInvite(para)){
            player.sendMessage(Messages.get("gang.invite.accepted", MsgVar.GANG.var(), para));
            player.getInvites().remove(para);
            Gangs.getInstance().getGangCoordinator().getGang(para.toLowerCase()).addPlayer(player);
        }else player.sendMessage(Messages.get("gang.invite.noInvite", MsgVar.GANG.var(), para));
    }

    public static void denyInvite(Hoodlum player, String para) {
        if(player.hasInvite(para)){
            player.sendMessage(Messages.get("gang.invite.denied", MsgVar.GANG.var(), para));
            player.getInvites().remove(para);
        }else player.sendMessage(Messages.get("gang.invite.noInvite", MsgVar.GANG.var(), para));
    }

    public static void showInvitations(Hoodlum player) {
        for(String string : player.getInvites()){
            player.sendMessage(Messages.get("gang.invite.list", MsgVar.GANG.var(), string));
        }
    }

    public static void showLand(Hoodlum player) {
        List<Chunk> chunks = new ArrayList<Chunk>();
        chunks.addAll(player.getGang().getClaims());
        new ChunkOutliner(chunks, 20*20, player.getPlayer());
    }
}
