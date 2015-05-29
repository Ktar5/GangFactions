package com.minecave.gangs.gang;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Carter on 5/25/2015.
 */
public class Gang {

    @Getter
    private final Set<Hoodlum> members;
    @Getter
    private final Set<Chunk> claims;
    //For stuffs, and also for signups?
    @Getter
    private final UUID uuid;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private Location home;
    @Getter
    @Setter
    private Player owner;
    @Getter
    @Setter
    private LocalDateTime lastOnline;

    public Gang(String name, Player owner) {
        //not sure but we might decide to use map later if we need key->value pairing
        members = new HashSet<>();
        claims = new HashSet<>();
        //just using a random uuid for gang creation, will be used when adding the gang to GangCoordinator
        uuid = UUID.randomUUID();
        this.name = name;
        this.owner = owner;
    }

    public boolean isChunkClaimed(Chunk chunk) {
        return claims.contains(chunk);
    }

    public boolean isChunkClaimed(Block block) {
        return claims.stream().anyMatch(block.getChunk()::equals);
    }

    public boolean isSpawnChunk(Chunk chunk) {
        return chunk.equals(home.getChunk());
    }

    public boolean isSpawnChunk(Location location) {
        return location.getChunk().equals(home.getChunk());
    }
}
