package com.minecave.gangs.command.commands;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.gang.GangRole;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.gang.Pledge;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import com.minecave.gangs.util.misc.ChunkOutliner;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Carter on 5/27/2015.
 */
public class User {
    public static void leave(Hoodlum player) {
        if(player.hasRole(GangRole.LEADER)){
            Management.disband(player);
        }else{
            player.sendMessage(Messages.get("gang.leave.leave", MsgVar.GANG.var(), player.getGang().getName()));
            player.getGang().removePlayer(player);
        }
    }

    public static void goHome(Hoodlum player) {
        if(player.getGang().getHome() != null){
            player.getPlayer().teleport(player.getGang().getHome());
            player.sendMessage(Messages.get("gang.home.welcome",
                    MsgVar.GANG.var(), player.getGang().getName()));
        }else{
            player.sendMessage(Messages.get("gang.home.noHome"));
        }
    }

    public static void power(Hoodlum player) {
        player.sendMessage(Messages.get("power.self",
                MsgVar.POWER.var(), String.valueOf(player.getPower()),
                MsgVar.MAX_POWER.var(), String.valueOf(player.getMaxPower())));
    }

    public static void info(Hoodlum player, Gang gang) {
        List<String> messages = new ArrayList<>();
        messages.add(Messages.get("info.header",
                MsgVar.GANG.var(), gang.getName()));
        Location loc = gang.getHome();
        if(loc != null){
            messages.add(Messages.get("info.home",
                    "{LOCATION}",
                    "x" + loc.getBlockX() + " y" + loc.getBlockY() + " z" + loc.getBlockZ()));
        }else{
            messages.add(Messages.get("info.noHome"));
        }
        messages.add(Messages.get("info.power",
                MsgVar.POWER.var(), String.valueOf(gang.getPower()),
                MsgVar.MAX_POWER.var(), String.valueOf(gang.getMaxPower())));
        messages.add(Messages.get("info.claims",
                "{CLAIMS_AMOUNT}", String.valueOf(gang.getClaims().size())));
        messages.add(Messages.get("info.leader",
                MsgVar.PLAYER.var(), gang.getOwner().getName()));

        String supers = "";
        String mods = "";
        String members = "";
        for (UUID u : gang.getMembers()) {
            Hoodlum h = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(u);
            if (h != null) {
                switch (h.getRole()){
                    case MEMBER:
                        members += " " + h.getName();
                        break;
                    case MODERATOR:
                        mods += " " + h.getName();
                        break;
                    case SUPER_MODERATOR:
                        supers += " " + h.getName();
                        break;
                }
            }
        }
        messages.add(Messages.get("info.farms",
                "{PERCENTAGE}", String.valueOf(gang.getPercentage()),
                "{TOTAL_FARM}", String.valueOf(gang.getTotalFarm())));
        messages.add(Messages.get("info.supermod", MsgVar.PLAYER.var(), supers));
        messages.add(Messages.get("info.mod", MsgVar.PLAYER.var(), mods));
        messages.add(Messages.get("info.members", MsgVar.PLAYER.var(), members));

        for(String message : messages){
            player.sendMessage(message);
        }
    }

    public static boolean create(Hoodlum player, String gangname) {
        if(!player.isInGang()){
            if(!Gangs.getInstance().getGangCoordinator().gangExists(gangname)){
                Gangs.getInstance().getGangCoordinator().createGang(gangname, player);
                Bukkit.getServer().broadcastMessage(Messages.get("gang.create.success",
                        MsgVar.GANG.var(), gangname,
                        MsgVar.PLAYER.var(), player.getPlayer().getName()));
                return true;
            }else player.sendMessage(Messages.get("gang.create.nameAlreadyExists", MsgVar.GANG.var(), gangname));
        }else player.sendMessage(Messages.get("gang.create.alreadyInGang",
                MsgVar.GANG.var(), player.getGang().getName(),
                MsgVar.ROLE.var(), player.getRole().toString()));
        return false;
    }

    public static void acceptInvite(Hoodlum player, String para) {
        if(player.hasInvite(para)){
            player.sendMessage(Messages.get("gang.invite.accepted", MsgVar.GANG.var(), para));
            player.getInvites().remove(para);
            Gangs.getInstance().getGangCoordinator().getGang(para.toLowerCase()).addPlayer(player);
        }else player.sendMessage(Messages.get("gang.invite.noInvite", MsgVar.GANG.var(), para));
    }

    public static void denyInvite(Hoodlum player, String para) {
        if(player.hasInvite(para)){
            player.sendMessage(Messages.get("gang.invite.denied", MsgVar.GANG.var(), para));
            player.getInvites().remove(para);
        }else player.sendMessage(Messages.get("gang.invite.noInvite", MsgVar.GANG.var(), para));
    }

    public static void showInvitations(Hoodlum player) {
        player.sendMessage(Messages.get("gang.invite.list.heading"));
        if(player.getInvites().size() == 0){
            player.sendMessage(Messages.get("gang.invite.list.none"));
            return;
        }
        for(String string : player.getInvites()){
            player.sendMessage(Messages.get("gang.invite.item", MsgVar.GANG.var(), string));
        }
    }

    public static void showLand(Hoodlum player) {
        List<Chunk> chunks = new ArrayList<Chunk>();
        chunks.addAll(player.getGang().getClaims());
        new ChunkOutliner(chunks, 20*20, player.getPlayer());
    }

    public static void createFromPledge(Hoodlum player) {
        Pledge pledge = Gangs.getInstance().getPledgeCoordinator().getPledge(player.getPlayerUUID());
        if(pledge != null){
            if(pledge.getLeader().getPlayerUUID().equals(player.getPlayerUUID())){
                pledge.create();
            }else
                player.sendMessage(Messages.get("pledge.notLeader", MsgVar.PLAYER.var(), pledge.getLeader().getPlayer().getName()));
        }else
            player.sendMessage(Messages.get("pledge.notPledged"));
    }
}
