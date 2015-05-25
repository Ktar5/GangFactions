package com.minecave.gangs.gang;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Carter on 5/25/2015.
 */
public class Gang {

    private static final Map<String, Gang> gangs = new HashMap<>();



    private final Set<Hoodlum> members;
    private final Set<Chunk> claims;
    private String name;

    //For stuffs, and also for signups?
    private final UUID uuid;

    public Gang(String name, Player owner){

    }

}
