package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.User;
import lombok.Getter;

import java.util.Set;

/**
 * Created by Carter on 7/14/2015.
 */
public class Pledge {

    @Getter
    private final Hoodlum leader;
    private final String name;
    private Set<Hoodlum> members;

    private static final int MINIMUM_NEEDED = 5;

    public Pledge(Hoodlum leader, String name){
        this.name = name;
        this.leader = leader;
    }

    public void join(Hoodlum hoodlum){
        members.add(hoodlum);
    }

    public boolean isPledged(Hoodlum hoodlum){
        return leader == hoodlum || members.contains(hoodlum);
    }

    private void remove(){
        Gangs.getInstance().getPledgeCoordinator().remove(leader.getPlayer().getUniqueId());
        this.leader.setPledged(false);
        for(Hoodlum hoodlum : members){
           this.leave(hoodlum);
        }
    }

    private void leave(Hoodlum hoodlum){
        members.remove(hoodlum);
        hoodlum.setPledged(false);
    }

    private void create(){
        if(members.size() + 1 >= MINIMUM_NEEDED){
            if(User.create(leader, name)){
                Gang gang = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(leader.getPlayerUUID()).getGang();
                for(Hoodlum hoodlum : members){
                    gang.addPlayer(hoodlum);
                }
            }
        }
    }

}
