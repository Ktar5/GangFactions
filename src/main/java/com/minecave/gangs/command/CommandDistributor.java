package com.minecave.gangs.command;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.Admin;
import com.minecave.gangs.command.commands.Management;
import com.minecave.gangs.command.commands.Misc;
import com.minecave.gangs.command.commands.User;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Carter on 5/25/2015.
 */
public class CommandDistributor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String something, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(!p.hasPermission("gangs.use")){
                return false;
            }
            Hoodlum player = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(p);
            switch (args.length) {
                case 0:
                    Misc.showHelp(player, "default");
                    break;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "leave":
                            if (player.hasRole(GangRole.MEMBER))
                                User.leave(player);
                            else player.sendMessage(Messages.get("gang.error.notInGang"));
                            break;
                        case "home": //THIS IS THE SINGLE ARGUMENT VERSION
                            if (player.hasRole(GangRole.MEMBER))
                                User.goHome(player);//done
                            else player.sendMessage(Messages.get("gang.error.notInGang"));
                            break;
                        case "claim":
                            if (player.hasRole(GangRole.SUPER_MODERATOR))
                                Management.claim(player);
                            else player.sendMessage(Messages.get("gang.error.tooLowRanked",
                                    MsgVar.ROLE.var(), player.getRole().toString(),
                                    "{ROLE_NEEDED}", GangRole.SUPER_MODERATOR.toString()));
                            break;
                        case "notices":
                            player.getNotices().forEach(player::sendMessage);
                            break;
                        case "msgboard":
                            if (player.hasRole(GangRole.MEMBER))
                                player.getGang().getMessageBoard().forEach(player::sendMessage);
                            else player.sendMessage(Messages.get("gang.error.notInGang"));
                            break;
                        case "show":
                            if(player.hasRole(GangRole.MEMBER))
                                User.showLand(player);
                            else player.sendMessage(Messages.get("gang.error.notInGang"));
                            break;
                        case "disband": //THIS IS THE SINGLE ARGUMENT VERSION
                            if (player.hasRole(GangRole.LEADER))
                                Management.disband(player);
                            else player.sendMessage(Messages.get("gang.error.tooLowRanked",
                                    MsgVar.ROLE.var(), player.getRole().toString(),
                                    "{ROLE_NEEDED}", GangRole.LEADER.toString()));
                            break;
                        case "unclaim":
                            if (player.hasRole(GangRole.SUPER_MODERATOR))
                                Management.unclaim(player);
                            else player.sendMessage(Messages.get("gang.error.tooLowRanked",
                                    MsgVar.ROLE.var(), player.getRole().toString(),
                                    "{ROLE_NEEDED}", GangRole.SUPER_MODERATOR.toString()));
                            break;
                        case "unclaimall":
                            if (player.hasRole(GangRole.LEADER))
                                Management.unclaimAll(player);
                            else player.sendMessage(Messages.get("gang.error.tooLowRanked",
                                    MsgVar.ROLE.var(), player.getRole().toString(),
                                    "{ROLE_NEEDED}", GangRole.LEADER.toString()));
                            break;
                        case "power": //THIS IS THE SINGLE ARGUMENT VERSION
                            User.power(player);//done
                            break;
                        case "info": //THIS IS THE SINGLE ARGUMENT VERSION
                            if (player.hasRole(GangRole.MEMBER))
                                User.info(player, player.getGang());
                            else player.sendMessage(Messages.get("gang.error.notInGang"));
                            break;
                        case "sethome":
                            if (player.hasRole(GangRole.SUPER_MODERATOR))
                                Management.setHome(player);
                            else player.sendMessage(Messages.get("gang.error.tooLowRanked",
                                    MsgVar.ROLE.var(), player.getRole().toString(),
                                    "{ROLE_NEEDED}", GangRole.SUPER_MODERATOR.toString()));
                            break;
                        case "help":
                            Misc.showHelp(player, "default");
                            break;
                        case "invites":
                            User.showInvitations(player);
                            break;
                        case "list":
                            Misc.list(player);
                            break;
                        case "map":
                            Gangs.getInstance().getGMap().generateSendClaimMap(player.getPlayer());
                            break;
                        case "unpledge":
                            Gangs.getInstance().getPledgeCoordinator().unpledge(player);
                            break;
                        case "create":
                            User.createFromPledge(player);
                            break;
                        default:
                            Misc.showHelp(player, "default");
                            break;
                    }
                    break;
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "disband": //ADMIN
                            if (!Misc.checks(player, GangRole.SERVER_ADMIN, args[1].toLowerCase())){
                                player.sendMessage(Messages.get("noPermission"));
                                break;
                            }
                            Admin.disband(player, Misc.getGang(args[1].toLowerCase()), false);
                            break;
                        case "silentdisband": //ADMIN
                            if (!Misc.checks(player, GangRole.SERVER_ADMIN, args[1].toLowerCase())){
                                player.sendMessage(Messages.get("noPermission"));
                                break;
                            }
                            Admin.disband(player, Misc.getGang(args[1].toLowerCase()), true);
                            break;
                        case "help":
                            Misc.showHelp(player, args[1]);
                            break;
                        case "mod":
                            if (!Misc.checksAndPlayer(player, GangRole.SUPER_MODERATOR, args[1].toLowerCase())) break;
                            Management.promote(player, args[1].toLowerCase(), GangRole.MODERATOR);
                            break;
                        case "supermod":
                            if (!Misc.checksAndPlayer(player, GangRole.LEADER, args[1].toLowerCase())) break;
                            Management.promote(player, args[1].toLowerCase(), GangRole.LEADER);
                            break;
                        case "leader":
                            if (!Misc.checksAndPlayer(player, GangRole.LEADER, args[1].toLowerCase())) break;
                            Management.promote(player, args[1].toLowerCase(), GangRole.LEADER);
                            break;
                        case "create":
                            Gangs.getInstance().getPledgeCoordinator().create(player, args[1]);
                            break;
                        case "pledge":
                            Gangs.getInstance().getPledgeCoordinator().join(player, args[1]);
                            break;
                        case "kick": //Non-admin version
                            if (player.hasRole(GangRole.MODERATOR))
                                Management.kick(player, args[1].toLowerCase());
                            else player.sendMessage(Messages.get("gang.error.tooLowRanked",
                                    MsgVar.ROLE.var(), player.getRole().toString(),
                                    "{ROLE_NEEDED}", GangRole.MODERATOR.toString()));
                            break;
                        case "forcekick":
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.kick(player, args[1].toLowerCase());
                            else player.sendMessage(Messages.get("noPermission"));
                            break;
                        case "join": //ADMIN
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.join(player, Misc.getGang(args[1].toLowerCase()));
                            else player.sendMessage(Messages.get("noPermission"));
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
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.power(player, args[1].toLowerCase());
                            else player.sendMessage(Messages.get("noPermission"));
                            break;
                        case "info": //ADMIN
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                User.info(player, Misc.getGang(args[1].toLowerCase()));
                            else player.sendMessage(Messages.get("noPermission"));
                            break;
                        case "home": //ADMIN
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.breakIn(player, Misc.getGang(args[1].toLowerCase()));
                            else player.sendMessage(Messages.get("noPermission"));
                            break;
                        default:
                            Misc.showHelp(player, "default");
                            break;
                    }
                    break;
                case 3:
                    switch (args[0].toLowerCase()) {
                        case "addpower":
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.addPower(player, args[1].toLowerCase(), Integer.valueOf(args[2]));
                            else player.sendMessage(Messages.get("noPermission"));
                            break;
                        case "takepower":
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.takePower(player, args[1].toLowerCase(), Integer.valueOf(args[2]));
                            else player.sendMessage(Messages.get("noPermission"));
                            break;
                        case "setmaxpower":
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.setMaxPower(player, args[1].toLowerCase(), Integer.valueOf(args[2]));
                            else player.sendMessage(Messages.get("noPermission"));
                            break;
                    }
                default:
                    switch(args[0].toLowerCase()){
                        case "addmessage":
                            if(player.hasRole(GangRole.SUPER_MODERATOR)){
                                if(args.length > 1){
                                    String message = "";
                                    for(int i = 1 ; i < args.length ; i++){
                                        message += " " + args[i];
                                    }
                                    player.getGang().addMessage(message);
                                } else player.sendMessage(Messages.get("notEnoughArguments"));
                            }
                            break;
                        default:
                            Misc.showHelp(player, "default");
                            break;
                    }
            }
        }

        return false;
    }
}
