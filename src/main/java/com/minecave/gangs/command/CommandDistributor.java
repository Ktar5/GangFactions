package com.minecave.gangs.command;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.Admin;
import com.minecave.gangs.command.commands.Management;
import com.minecave.gangs.command.commands.Misc;
import com.minecave.gangs.command.commands.User;
import com.minecave.gangs.gang.Hoodlum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Carter on 5/25/2015.
 */
public class CommandDistributor implements CommandExecutor{
    /*
    Admin:
    setPower <>
    addPower<>
    removePower<>
    disbandGang<>
    renameGang

     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String something, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            Hoodlum player = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(p);
            switch(args.length){
                case 0:
                    Misc.showHelp(player);
                    break;
                case 1:
                    switch (args[0].toLowerCase()){
                        case "leave":
                            User.leave(player);
                            break;
                        case "home": //THIS IS THE SINGLE ARGUMENT VERSION
                            User.goHome(player);
                            break;
                        case "claim":
                            Management.claim(player);
                            break;
                        case "disband": //THIS IS THE SINGLE ARGUMENT VERSION
                            Management.disband(player);
                            break;
                        case "unclaim":
                            Management.unclaim(player);
                            break;
                        case "unclaimall":
                            Management.unclaimAll(player);
                            break;
                        case "power": //THIS IS THE SINGLE ARGUMENT VERSION
                            User.power(player);
                            break;
                        case "info": //THIS IS THE SINGLE ARGUMENT VERSION
                            User.info(player);
                            break;
                        case "sethome":
                            Management.setHome(player);
                            break;
                        case "help":
                            Misc.showHelp(player);
                            break;
                        case "invitations":
                            User.showInvitations(player);
                            break;
                        case "confirm":
                            Misc.confirm(player);
                            break;
                        default:
                            Misc.showHelp(player);
                            break;
                    }
                    break;
                case 2:
                    switch (args[0].toLowerCase()){
                        case "disband": //ADMIN
                            Admin.disband(player, args[1].toLowerCase(), false);
                            break;
                        case "silentdisband": //ADMIN
                            Admin.disband(player, args[1].toLowerCase(), true);
                            break;
                        case "mod":
                            Management.mod(player, args[1].toLowerCase());
                            break;
                        case "supermod":
                            Management.supermod(player, args[1].toLowerCase());
                            break;
                        case "leader":
                            Management.leader(player, args[1].toLowerCase());
                            break;
                        case "create":
                            User.create(player, args[1]);
                            //This one will remain regular-case because of naming things outside storage
                            break;
                        case "kick": //Non-admin version
                            Management.kick(player, args[1].toLowerCase());
                            break;
                        case "forcekick":
                            Admin.kick(player, args[1].toLowerCase());
                        case "join": //ADMIN
                            Admin.join(player, args[1].toLowerCase());
                            break;
                        case "invite":
                            Management.invite(player, args[1].toLowerCase());
                            break;
                        case "accept":
                            User.acceptInvite(player, args[1].toLowerCase());
                            break;
                        case "deny":
                            User.denyInvite(player, args[1].toLowerCase());
                            break;
                        case "power": //ADMIN
                            Admin.power(player, args[1].toLowerCase());
                            break;
                        case "info": //ADMIN
                            Admin.info(player, args[1].toLowerCase());
                            break;
                        case "home": //ADMIN
                            Admin.breakIn(player, args[1].toLowerCase());
                            break;
                        default:
                            Misc.showHelp(player);
                            break;
                    }
                    break;
                case 3:
                    switch (args[0].toLowerCase()) {
                        case "addpower":
                            Admin.addPower(player, args[1].toLowerCase(), false);
                            break;
                        case "takepower":
                            Admin.takePower(player, args[1].toLowerCase(), false);
                            break;
                        case "setpower":
                            Admin.setPower(player, args[1].toLowerCase(), false);
                            break;
                        case "addpowerplayer":
                            Admin.addPower(player, args[1].toLowerCase(), true);
                            break;
                        case "takepowerplayer":
                            Admin.takePower(player, args[1].toLowerCase(), true);
                            break;
                        case "setpowerplayer":
                            Admin.setPower(player, args[1].toLowerCase(), true);
                            break;
                        case "setmaxpower":
                            Admin.setMaxPower(player, args[1].toLowerCase(), false);
                            break;
                        case "setmaxpowerplayer":
                            Admin.setMaxPower(player, args[1].toLowerCase(), true);//The fuck?
                            break;
                    }
                default:
                    Misc.showHelp(player);
                    break;
            }
        }

        return false;
    }
}
