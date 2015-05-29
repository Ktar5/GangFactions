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
            player.sendMessage(Messages.get("welcomeHome",
                    MsgVar.GANG.var(), gang.getName()));
        }else{
            player.sendMessage(Messages.get("noHome"));
        }
    }

    public static void disband(Hoodlum player, Gang gang, boolean silent) {
        Gangs.getInstance().getGangCoordinator().disbandGang(gang.getName(), silent);
    }

    public static void power(Hoodlum player, String hoodlum) {
        if(Misc.checkPlayer(hoodlum)){
            player.sendMessage(Messages.get("power",
                    MsgVar.PLAYER.var(), hoodlum,
                    MsgVar.POWER.var(), String.valueOf(Misc.getHoodlum(hoodlum).getPower()),
                    MsgVar.MAX_POWER.var(), String.valueOf(Misc.getHoodlum(hoodlum).getMaxPower())));
        }else{
            player.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), hoodlum));
        }
    }

    public static void info(Hoodlum player, Gang gang) {

    }

    public static void join(Hoodlum player, Gang gang) {
        gang.addPlayer(player);
        player.sendMessage(Messages.get("joinedGang", MsgVar.GANG.var(), gang.getName()));
    }

    public static void kick(Hoodlum player, Gang gang, String hooldum) {

    }

    public static void setMaxPower(Hoodlum player, String para) {

    }

    public static void setPower(Hoodlum player, String para) {

        
    }

    public static void takePower(Hoodlum player, String para) {

    }

    public static void addPower(Hoodlum player, String para) {

    }
}
