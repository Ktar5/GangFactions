package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Carter on 5/27/2015.
 */
public class Misc {

    public static void showHelp(Hoodlum player, String page) {
        List<String> helps = Messages.getHelp(page);
        if(helps != null){
            player.getPlayer().sendMessage(helps.toArray(new String[helps.size()]));
        }else
            player.sendMessage(Messages.get("helpPageNoExist", MsgVar.SECTION.var(), page));
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
            else sender.sendMessage(Messages.get("gang.noExist", MsgVar.GANG.var(), gang));
        else sender.sendMessage(Messages.get("noPermission", MsgVar.ROLE.var()));
        return false;
    }

    public static boolean checksAndPlayer(Hoodlum sender, GangRole role, String playerName) {
        if(sender.hasRole(role))
            if(Gangs.getInstance().getHoodlumCoordinator().getHoodlum(playerName) != null)
                return true;
            else sender.sendMessage(Messages.get("player.noExist", MsgVar.PLAYER.var(), playerName));
        else sender.sendMessage(Messages.get("noPermission", MsgVar.ROLE.var()));
        return false;
    }

    /*
    is static classes bad? >-< donthurtme
     */
    static class GangComparator implements Comparator<Gang> {
        @Override
        public int compare(Gang g1, Gang g2){
            return g1.onlinePlayers() > g2.onlinePlayers() ? 1 : 0;
        }
    }

    public static void list(Hoodlum sender){
        List<Gang> gangs = new ArrayList<>();
        gangs.addAll(Gangs.getInstance().getGangCoordinator().getGangMap().values());
        gangs.sort(new GangComparator());
        for(Gang gang : gangs){
            sender.sendMessage(gang.getName() + " (" + gang.onlinePlayers() + "/" + gang.getMembers().size() + ")");
        }
    }

    public static boolean checkGang(String gangName) {
        return Gangs.getInstance().getGangCoordinator().gangExists(gangName);
    }

    public static Gang getGang(String gangName) {
        return Gangs.getInstance().getGangCoordinator().getGang(gangName);
    }
}
