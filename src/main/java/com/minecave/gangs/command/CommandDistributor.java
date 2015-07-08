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
public class CommandDistributor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String something, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Hoodlum player = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(p);
            switch (args.length) {
                case 0:
                    Misc.showHelp(player, "default");
                    break;
                case 1:
                    switch (args[0].toLowerCase()) {
                        case "leave":
                            if (player.hasRole(GangRole.MEMBER))
                                User.leave(player);//done
                            break;
                        case "home": //THIS IS THE SINGLE ARGUMENT VERSION
                            if (player.hasRole(GangRole.MEMBER))
                                User.goHome(player);//done
                            break;
                        case "claim":
                            if (player.hasRole(GangRole.SUPER_MODERATOR))
                                Management.claim(player);//done
                            break;
                        case "disband": //THIS IS THE SINGLE ARGUMENT VERSION
                            if (player.hasRole(GangRole.LEADER))
                                Management.disband(player);
                            break;
                        case "unclaim":
                            if (player.hasRole(GangRole.SUPER_MODERATOR))
                                Management.unclaim(player);//done
                            break;
                        case "unclaimall":
                            if (player.hasRole(GangRole.LEADER))
                                Management.unclaimAll(player);
                            break;
                        case "power": //THIS IS THE SINGLE ARGUMENT VERSION
                            User.power(player);//done
                            break;
                        case "info": //THIS IS THE SINGLE ARGUMENT VERSION
                            if (player.hasRole(GangRole.MEMBER))
                                User.info(player);//done
                            break;
                        case "sethome":
                            if (player.hasRole(GangRole.SUPER_MODERATOR))
                                Management.setHome(player);
                            break;
                        case "help":
                            Misc.showHelp(player, "default");
                            break;
                        case "invitations":
                            User.showInvitations(player);
                            break;
                        case "map":
                            Gangs.getInstance().getGMap().generateSendClaimMap(player.getPlayer());
                            break;
                        default:
                            Misc.showHelp(player, "default");
                            break;
                    }
                    break;
                case 2:
                    switch (args[0].toLowerCase()) {
                        case "disband": //ADMIN
                            if (!Misc.checks(player, GangRole.SERVER_ADMIN, args[1].toLowerCase())) break;
                                Admin.disband(player, Misc.getGang(args[1].toLowerCase()), false);
                            break;
                        case "silentdisband": //ADMIN
                            if (!Misc.checks(player, GangRole.SERVER_ADMIN, args[1].toLowerCase())) break;
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
                            if (Misc.checkGang(args[1].toLowerCase())) break;
                                User.create(player, args[1]);
                            //This one will remain regular-case because of naming things outside storage
                            break;
                        case "kick": //Non-admin version
                            if (player.hasRole(GangRole.MODERATOR))
                                Management.kick(player, args[1].toLowerCase());
                            break;
                        case "forcekick":
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.kick(player, args[1].toLowerCase());
                            break;
                        case "join": //ADMIN
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.join(player, Misc.getGang(args[1].toLowerCase()));
                            break;
                        case "invite":
                            if (player.hasRole(GangRole.MODERATOR))
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
                            break;
                        case "info": //ADMIN
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.info(player, Misc.getGang(args[1].toLowerCase()));
                            break;
                        case "home": //ADMIN
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.breakIn(player, Misc.getGang(args[1].toLowerCase()));
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
                            break;
                        case "takepower":
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.takePower(player, args[1].toLowerCase(), Integer.valueOf(args[2]));
                            break;
                        case "setmaxpower":
                            if (player.hasRole(GangRole.SERVER_ADMIN))
                                Admin.setMaxPower(player, args[1].toLowerCase(), Integer.valueOf(args[2]));
                    }
                default:
                    Misc.showHelp(player, "default");
                    break;
            }
        }

        return false;
    }
}
