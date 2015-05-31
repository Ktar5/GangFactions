package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import org.bukkit.Bukkit;

/**
 * Created by Carter on 5/27/2015.
 */
public class User {
    public static void leave(Hoodlum player) {
        if(player.hasRole(GangRole.LEADER)){
            Management.disband(player);
        }else{
            player.sendMessage(Messages.get("leaveGang", MsgVar.GANG.var(), player.getGang().getName()));
            player.getGang().removePlayer(player);
        }
    }

    public static void goHome(Hoodlum player) {
        if(player.getGang().getHome() != null){
            player.getPlayer().teleport(player.getGang().getHome());
            player.sendMessage(Messages.get("welcomeHome",
                    MsgVar.GANG.var(), player.getGang().getName()));
        }else{
            player.sendMessage(Messages.get("noHome"));
        }
    }

    public static void power(Hoodlum player) {
        player.sendMessage(Messages.get("powerSelf",
                MsgVar.POWER.var(), String.valueOf(player.getPower()),
                MsgVar.MAX_POWER.var(), String.valueOf(player.getMaxPower())));
    }

    public static void info(Hoodlum player) {

    }

    public static void create(Hoodlum player, String gangname) {
        if(!player.hasRole(GangRole.MEMBER)){
            if(!Gangs.getInstance().getGangCoordinator().gangExists(gangname)){
                Gangs.getInstance().getGangCoordinator().createGang(gangname, player);
                Bukkit.getServer().broadcastMessage(Messages.get("gangCreated",
                        MsgVar.GANG.var(), gangname,
                        MsgVar.PLAYER.var(), player.getPlayer().getName()));
            }else player.sendMessage(Messages.get("gangExists", MsgVar.GANG.var(), gangname));
        }else player.sendMessage(Messages.get("alreadyInGang",
                MsgVar.GANG.var(), player.getGang().getName(),
                MsgVar.ROLE.var(), player.getRole().toString()));
    }

    public static void acceptInvite(Hoodlum player, String para) {
        if(player.hasInvite(para)){
            player.sendMessage(Messages.get("inviteAccepted", MsgVar.GANG.var(), para));
            player.getInvites().remove(para);
            Gangs.getInstance().getGangCoordinator().getGang(para.toLowerCase()).addPlayer(player);
        }else player.sendMessage(Messages.get("haventBeenInvited", MsgVar.GANG.var(), para));
    }

    public static void denyInvite(Hoodlum player, String para) {
        if(player.hasInvite(para)){
            player.sendMessage(Messages.get("inviteDenied", MsgVar.GANG.var(), para));
            player.getInvites().remove(para);
        }else player.sendMessage(Messages.get("haventBeenInvited", MsgVar.GANG.var(), para));
    }

    public static void showInvitations(Hoodlum player) {
        for(String string : player.getInvites()){
            player.sendMessage(Messages.get("inviteList", MsgVar.GANG.var(), string));
        }
    }
}
