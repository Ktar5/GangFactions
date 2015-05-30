package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;

/**
 * Created by Carter on 5/27/2015.
 */
public class Misc {
    public static void showHelp(Hoodlum player) {

    }

    public static void confirm(Hoodlum player) {

    }

    /**
     *
     * @param sender Person to check
     * @param role Has this role or higher?
     * @param gang The gang
     * @return true if the gang exists and the player has that role
     */
    public static boolean checks(Hoodlum sender, GangRole role, String gang){
        if(sender.hasRole(role))
            if(Gangs.getInstance().getGangCoordinator().gangExists(gang))
                return true;
            else sender.sendMessage(Messages.get("gangNotExist", MsgVar.GANG.var(), gang));
        else sender.sendMessage(Messages.get("noPermission", MsgVar.ROLE.var()));
        return false;
    }

    public static boolean checksAndPlayer(Hoodlum sender, GangRole role, String playerName) {
        if(sender.hasRole(role))
            if(Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName) != null)
                return true;
            else sender.sendMessage(Messages.get("playerDoesntExist", MsgVar.PLAYER.var(), playerName));
        else sender.sendMessage(Messages.get("noPermission", MsgVar.ROLE.var()));
        return false;
    }

    public static boolean checkGang(String gangName) {
        return Gangs.getInstance().getGangCoordinator().gangExists(gangName);
    }

    public static Gang getGang(String gangName) {
        return Gangs.getInstance().getGangCoordinator().getGang(gangName);
    }
}
