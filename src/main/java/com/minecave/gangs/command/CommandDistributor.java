package com.minecave.gangs.command;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.Admin;
import com.minecave.gangs.command.commands.Management;
import com.minecave.gangs.command.commands.Misc;
import com.minecave.gangs.command.commands.User;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Carter on 5/25/2015.
 */
public class CommandDistributor implements CommandExecutor{

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
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            User.leave(player);//done
                            break;
                        case "home": //THIS IS THE SINGLE ARGUMENT VERSION
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            User.goHome(player);//done
                            break;
                        case "claim":
                            if(!Misc.checkRole(player, GangRole.SUPER_MODERATOR)) break;
                            Management.claim(player);//done
                            break;
                        case "disband": //THIS IS THE SINGLE ARGUMENT VERSION
                            if(!Misc.checkRole(player, GangRole.LEADER)) break;
                            Management.disband(player);
                            break;
                        case "unclaim":
                            if(!Misc.checkRole(player, GangRole.SUPER_MODERATOR)) break;
                            Management.unclaim(player);//done
                            break;
                        case "unclaimall":
                            if(!Misc.checkRole(player, GangRole.LEADER)) break;
                            Management.unclaimAll(player);
                            break;
                        case "power": //THIS IS THE SINGLE ARGUMENT VERSION
                            if(!Misc.checkRole(player, GangRole.GANGLESS)) break;
                            User.power(player);//done
                            break;
                        case "info": //THIS IS THE SINGLE ARGUMENT VERSION
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            User.info(player);//done
                            break;
                        case "sethome":
                            if(!Misc.checkRole(player, GangRole.SUPER_MODERATOR)) break;
                            Management.setHome(player);
                            break;
                        case "help":
                            if(!Misc.checkRole(player, GangRole.GANGLESS)) break;
                            Misc.showHelp(player);
                            break;
                        case "invitations":
                            if(!Misc.checkRole(player, GangRole.GANGLESS)) break;
                            User.showInvitations(player);
                            break;
                        case "confirm":
                            if(!Misc.checkRole(player, GangRole.GANGLESS)) break;
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
                            if(!Misc.checks(player, GangRole.SERVER_ADMIN, args[1].toLowerCase())) break;
                            Admin.disband(player, args[1].toLowerCase(), false);
                            break;
                        case "silentdisband": //ADMIN
                            if(!Misc.checks(player, GangRole.SERVER_ADMIN, args[1].toLowerCase())) break;
                            Admin.disband(player, args[1].toLowerCase(), true);
                            break;
                        case "mod":
                            if(!Misc.checksAndPlayer(player, GangRole.SUPER_MODERATOR, args[1].toLowerCase())) break;
                            Management.mod(player, args[1].toLowerCase());
                            break;
                        case "supermod":
                            if(!Misc.checksAndPlayer(player, GangRole.LEADER, args[1].toLowerCase())) break;
                            Management.supermod(player, args[1].toLowerCase());
                            break;
                        case "leader":
                            if(!Misc.checksAndPlayer(player, GangRole.LEADER, args[1].toLowerCase())) break;
                            Management.leader(player, args[1].toLowerCase());
                            break;
                        case "create":
                            if(Misc.checkGang(args[1].toLowerCase())) break;
                            User.create(player, args[1]);
                            //This one will remain regular-case because of naming things outside storage
                            break;
                        case "kick": //Non-admin version
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Management.kick(player, args[1].toLowerCase());
                            break;
                        case "forcekick":
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.kick(player, args[1].toLowerCase());
                            break;
                        case "join": //ADMIN
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.join(player, args[1].toLowerCase());
                            break;
                        case "invite":
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Management.invite(player, args[1].toLowerCase());
                            break;
                        case "accept":
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            User.acceptInvite(player, args[1].toLowerCase());
                            break;
                        case "deny":
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            User.denyInvite(player, args[1].toLowerCase());
                            break;
                        case "power": //ADMIN
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.power(player, args[1].toLowerCase());
                            break;
                        case "info": //ADMIN
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.info(player, args[1].toLowerCase());
                            break;
                        case "home": //ADMIN
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
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
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.addPower(player, args[1].toLowerCase());
                            break;
                        case "takepower":
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.takePower(player, args[1].toLowerCase());
                            break;
                        case "setpower":
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.setPower(player, args[1].toLowerCase());
                            break;
                        case "setmaxpower":
                            if(!Misc.checkRole(player, GangRole.MEMBER)) break;
                            Admin.setMaxPower(player, args[1].toLowerCase());
                    }
                default:
                    Misc.showHelp(player);
                    break;
            }
        }

        return false;
    }
}
