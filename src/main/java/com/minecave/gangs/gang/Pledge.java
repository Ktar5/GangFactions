package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.User;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import lombok.Getter;

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
        this.name = name;
        this.leader = leader;
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

    private void remove(){
        Gangs.getInstance().getPledgeCoordinator().remove(name.toLowerCase());
        this.leader.setPledged(false);
        members.forEach(this::leave);
    }

    private void leave(Hoodlum hoodlum){
        members.remove(hoodlum);
        hoodlum.setPledged(false);
    }

    private void create(){
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
