package com.minecave.gangs.command.commands;

import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;

/**
 * Created by Carter on 5/27/2015.
 */
public class User {
    public static void leave(Hoodlum player) {
        if(Misc.checkRole(player, GangRole.LEADER)){
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

    }

    public static void info(Hoodlum player) {

    }

    public static void create(Hoodlum player, String arg) {
    }

    public static void acceptInvite(Hoodlum player, String para) {
    }

    public static void denyInvite(Hoodlum player, String para) {
    }

    public static void showInvitations(Hoodlum player) {
    }
}
