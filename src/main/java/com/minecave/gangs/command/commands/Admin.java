package com.minecave.gangs.command.commands;

import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;

/**
 * Created by Carter on 5/27/2015.
 */
public class Admin {

    public static void breakIn(Hoodlum player, String faction) {
        if(player.getRole().rank >= 5){

        }else{
            player.sendMessage(Messages.get("noPermission"));
        }
    }

    public static void disband(Hoodlum player, String faction, boolean silent) {
    }

    public static void power(Hoodlum player, String hoodlum) {
    }

    public static void info(Hoodlum player, String faction) {
    }

    public static void join(Hoodlum player, String faction) {
    }

    public static void kick(Hoodlum player, String faction) {
    }

    public static void setMaxPower(Hoodlum player, String para, boolean hoodlum) {
    }

    public static void setPower(Hoodlum player, String para, boolean hoodlum) {
        
    }

    public static void takePower(Hoodlum player, String para, boolean hoodlum) {
    }

    public static void addPower(Hoodlum player, String para, boolean hoodlum) {
    }
}
