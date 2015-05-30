package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.Management;
import com.minecave.gangs.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Carter on 5/25/2015.
 */
public class Gang {

    @Getter
    private final Set<Hoodlum> members;
    @Getter
    private final Set<Chunk> claims;
    @Getter
    private int totalFarm;
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

    public void setHome(Location location) {
        if(!claims.contains(location.getChunk())) {
            home = location;
            String message = Gangs.getInstance().getMessages().get("gangs.setHome", String.class);
            message = StringUtil.replace(message, "{x}", String.valueOf(location.getBlockX()));
            message = StringUtil.replace(message, "{y}", String.valueOf(location.getBlockY()));
            final String finalMessage = StringUtil.replaceAndColor(message, "{z}", String.valueOf(location.getBlockZ()));
            members.forEach(h -> h.sendMessage(finalMessage));
        }
    }

    public boolean isChunkClaimed(Chunk chunk) {
        return claims.contains(chunk);
    }

    public boolean isSpawnChunk(Chunk chunk) {
        return chunk.equals(home.getChunk());
    }

    public boolean claimChunk(Chunk chunk) {
        return claims.add(chunk);
    }

    public boolean unclaimChunk(Chunk chunk) {
        return claims.remove(chunk);
    }

    public void unclaimAllChunks() {
        this.claims.clear();
    }

    public boolean hasPlayer(Player player) {
        for (Hoodlum h : members) {
            if (h.getPlayerUUID().equals(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }


    public void addPlayer(Hoodlum player) {
        if (!hasPlayer(player.getPlayer())) {
            player.setRole(GangRole.MEMBER);
            player.setGang(this);
        }
    }

    public void removePlayer(Hoodlum player) {
        if (hasPlayer(player.getPlayer())) {
            player.setRole(GangRole.GANGLESS);
            if (player.hasRole(GangRole.LEADER)) {
                Management.disband(player);
                player.setGang(null);
            }
        }
    }

    public boolean isRaidable() {
        int powerToChunkRatio = Gangs.getInstance().getConfiguration().get("power.powerToChunkRatio", int.class);
        return getPower() / claims.size() > powerToChunkRatio;
    }

    public int getPower() {
        int power = 0;
        for (Hoodlum h : members) {
            power += h.getPower();
        }
        return power;
    }

    public void subtractFromFarmTotal() {
        this.totalFarm += 1;
        checkPercentage();
    }

    public void addToFarmTotal() {
        this.totalFarm -= 1;
        checkPercentage();
    }

    public void checkPercentage() {
        int percent = Gangs.getInstance().getConfiguration().get("farm.percentFarmablePerChunk", int.class);
        int totalFarmable = (percent / 100) * (claims.size() - 1);
        if(totalFarm >= totalFarmable) {
            //mark for signs later
        }
    }
}
