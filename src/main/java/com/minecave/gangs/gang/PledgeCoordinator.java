package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Carter on 7/14/2015.
 */
public class PledgeCoordinator {

    private Map<String, Pledge> pledgeMap;

    public PledgeCoordinator() {
        pledgeMap = new HashMap<>();
    }

    public void remove(String name) {
        if(pledgeMap.containsKey(name.toLowerCase())){
            pledgeMap.remove(name.toLowerCase());
        }
    }

    public void join(Hoodlum player, String name){
        Pledge pledge = getPledge(name);
        if(!player.isInGang()){
            if(!player.isPledged()){
                if(pledge != null){
                    if(pledge.isInvited(player.getPlayerUUID())){
                        pledge.join(player);
                        player.setPledged(true);
                        pledge.getLeader().sendMessage(Messages.get("pledge.personJoined",
                                MsgVar.PLAYER.var(), player.getName(),
                                MsgVar.PLEDGES.var(), String.valueOf(pledge.getMemberSize()),
                                MsgVar.PLEDGES_NEEDED.var(), String.valueOf(Pledge.MINIMUM_NEEDED),
                                MsgVar.PLEDGES_LEFT.var(), String.valueOf(Pledge.MINIMUM_NEEDED - pledge.getMemberSize() < 0 ? 0 : Pledge.MINIMUM_NEEDED - pledge.getMemberSize())));
                        player.sendMessage(Messages.get("pledge.pledged", MsgVar.GANG.var(), pledge.getName()));
                    }else
                        player.sendMessage(Messages.get("pledge.notInvited", MsgVar.GANG.var(), pledge.getName()));
                }else
                    player.sendMessage(Messages.get("pledge.doesntExist", MsgVar.GANG.var(), name));
            }else
                player.sendMessage(Messages.get("pledge.selfAlreadyPledged", MsgVar.GANG.var(), getPledge(player.getPlayerUUID()).getName()));
        }else
            player.sendMessage(Messages.get("pledge.youreAlreadyInGang", MsgVar.GANG.var(), player.getGang().getName()));
    }

    public void create(Hoodlum leader, String name){
        if(!leader.isInGang()){
            if(!leader.isPledged()){
                if(!pledgeMap.containsKey(name.toLowerCase())){
                    if(!Gangs.getInstance().getGangCoordinator().gangExists(name)){
                        leader.sendMessage(Messages.get("pledge.pledgeCreated", MsgVar.GANG.var(), name));
                        leader.setPledged(true);
                        pledgeMap.put(name.toLowerCase(), new Pledge(leader, name));
                    } else
                        leader.sendMessage(Messages.get("pledge.gangAlreadyExists", MsgVar.GANG.var(), name));
                } else
                    leader.sendMessage(Messages.get("pledge.alreadyExists", MsgVar.GANG.var(), name));
            } else
                leader.sendMessage(Messages.get("pledge.selfAlreadyPledged"));
        } else
            leader.sendMessage(Messages.get("pledge.youreAlreadyInGang", MsgVar.GANG.var(), leader.getGang().getName()));
    }

    public void unpledge(Hoodlum player){
        Pledge pledge = getPledge(player.getPlayerUUID());
        if(pledge != null){
            player.sendMessage(Messages.get("pledge.unpledged", MsgVar.GANG.var(), pledge.getName()));
            pledge.leave(player);
        }else
            player.sendMessage(Messages.get("pledge.notPledged"));
    }

    public Pledge getPledge(String name){
        return pledgeMap.get(name.toLowerCase());
    }

    public Pledge getPledge(UUID playerUUID){
        for(Pledge pledge : pledgeMap.values()){
            if(pledge.isPledged(playerUUID)){
                return pledge;
            }
        }
        return null;
    }
}
