package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.User;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Carter on 7/14/2015.
 */
public class Pledge {

    @Getter
    private final Hoodlum leader;
    @Getter
    private final String name;
    private Set<Hoodlum> members;
    private Set<UUID> invited;

    private static final int MINIMUM_NEEDED = 5;

    public Pledge(Hoodlum leader, String name){
        members = new HashSet<>();
        invited = new HashSet<>();
        this.name = name;
        this.leader = leader;
    }

    public void invite(Hoodlum hoodlum){
        if(!isInvited(hoodlum.getPlayerUUID())){
            if(!hoodlum.isInGang()){
                if(!hoodlum.isPledged()){
                    invited.add(hoodlum.getPlayerUUID());
                    hoodlum.sendMessage(Messages.get("pledge.invited",
                            MsgVar.PLAYER.var(), leader.getPlayer().getName(),
                            MsgVar.GANG.var(), name));
                    leader.sendMessage(Messages.get("pledge.inviter",
                            MsgVar.PLAYER.var(), hoodlum.getPlayer().getName(),
                            MsgVar.PLEDGES.var(), String.valueOf(members.size()),
                            MsgVar.PLEDGES_NEEDED.var(), String.valueOf(MINIMUM_NEEDED),
                            MsgVar.PLEDGES_LEFT.var(), String.valueOf(MINIMUM_NEEDED - members.size() < 0 ? 0 : MINIMUM_NEEDED - members.size())));
                }else
                    leader.sendMessage(Messages.get("pledge.invitedAlreadyPledged",
                            MsgVar.PLAYER.var(), hoodlum.getPlayer().getName(),
                            MsgVar.GANG.var(), Gangs.getInstance().getPledgeCoordinator().getPledge(hoodlum.getPlayerUUID()).getName()));
            }else
                leader.sendMessage(Messages.get("pledge.alreadyInGang",
                        MsgVar.PLAYER.var(), hoodlum.getPlayer().getName(),
                        MsgVar.GANG.var(), hoodlum.getGang().getName()));
        }else
            leader.sendMessage(Messages.get("pledge.alreadyInvited",
                    MsgVar.PLAYER.var(), hoodlum.getPlayer().getName()));
    }

    public void join(Hoodlum hoodlum){
        members.add(hoodlum);
        if(invited.contains(hoodlum.getPlayerUUID())){
            invited.remove(hoodlum.getPlayerUUID());
        }
    }

    public boolean isInvited(UUID playerUUID){
        return invited.contains(playerUUID);
    }

    public boolean isPledged(Hoodlum hoodlum){
        return leader == hoodlum || members.contains(hoodlum);
    }

    public boolean isPledged(UUID uuid){
        for(Hoodlum hoodlum : members){
            if(hoodlum.getPlayerUUID().equals(uuid)){
                return true;
            }
        }
        return leader.getPlayerUUID() == uuid;
    }

    public void remove(){
        Gangs.getInstance().getPledgeCoordinator().remove(name.toLowerCase());
        leader.setPledged(false);
        members.forEach(hoodlum -> hoodlum.setPledged(false));
        members.clear();
    }

    public void leave(Hoodlum hoodlum){
        if(hoodlum.getPlayerUUID().equals(leader.getPlayerUUID())){
            remove();
            return;
        }
        members.remove(hoodlum);
        hoodlum.setPledged(false);
    }

    public void create(){
        if(members.size() >= MINIMUM_NEEDED){
            if(User.create(leader, name)){
                Gang gang = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(leader.getPlayerUUID()).getGang();
                for(Hoodlum hoodlum : members){
                    gang.addPlayer(hoodlum);
                    hoodlum.sendMessage(Messages.get("pledge.gangCreated", MsgVar.GANG.var(), name));
                }
            }
        }else
            leader.sendMessage(Messages.get("pledge.notEnoughPledges",
                    MsgVar.PLEDGES.var(), String.valueOf(members.size()),
                    MsgVar.PLEDGES_NEEDED.var(), String.valueOf(MINIMUM_NEEDED),
                    MsgVar.PLEDGES_LEFT.var(), String.valueOf(MINIMUM_NEEDED - members.size() < 0 ? 0 : MINIMUM_NEEDED - members.size())));
    }

}
