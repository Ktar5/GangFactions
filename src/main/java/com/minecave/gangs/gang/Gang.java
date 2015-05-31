package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.Management;
import com.minecave.gangs.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
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
    private final Set<UUID> members;
    @Getter
    private final Set<Chunk> claims;
    @Getter
    private int totalFarm;
    //For stuffs, and also for signups?
    @Getter
    private UUID uuid;
    @Getter
    @Setter
    private String name;
    @Getter
    private Location home;
    @Getter
    @Setter
    private OfflinePlayer owner;
    @Getter
    @Setter
    private LocalDateTime lastOnline;

    public Gang(String name, OfflinePlayer owner) {
        //not sure but we might decide to use map later if we need key->value pairing
        members = new HashSet<>();
        claims = new HashSet<>();
        //just using a random uuid for gang creation, will be used when adding the gang to GangCoordinator
        uuid = UUID.randomUUID();
        while (Gangs.getInstance().getGangCoordinator().gangExists(uuid)) {
            uuid = UUID.randomUUID();
        }
        this.name = name;
        this.owner = owner;
    }

    public void setHome(Location location) {
        if (!claims.contains(location.getChunk())) {
            home = location;
            String message = Gangs.getInstance().getMessages().get("gangs.setHome", String.class);
            message = StringUtil.replace(message, "{x}", String.valueOf(location.getBlockX()));
            message = StringUtil.replace(message, "{y}", String.valueOf(location.getBlockY()));
            final String finalMessage = StringUtil.replaceAndColor(message, "{z}", String.valueOf(location.getBlockZ()));
            members.forEach(u -> Gangs.getInstance().getHoodlumCoordinator().getHoodlum(u).sendMessage(finalMessage));
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
        return members.contains(player.getUniqueId());
    }

    public void addPlayer(Hoodlum player) {
        if (!hasPlayer(player.getPlayer())) {
            player.setRole(GangRole.MEMBER);
            player.setGang(this);
            player.setGangUUID(uuid);
            members.add(player.getPlayerUUID());
        }
    }

    public void removePlayer(Hoodlum player) {
        if (hasPlayer(player.getPlayer())) {
            player.setRole(GangRole.GANGLESS);
            if (player.hasRole(GangRole.LEADER)) {
                Management.disband(player);
                player.setGang(null);
                player.setGangUUID(null);
                members.remove(player.getPlayerUUID());
            }
        }
    }

    public boolean isRaidable() {
        int powerToChunkRatio = Gangs.getInstance().getConfiguration().get("power.powerToChunkRatio", Integer.class);
        return getPower() / (claims.size() != 0 ? claims.size() : 1) < powerToChunkRatio;
    }

    public boolean canClaimChunks() {
        return isRaidable();
    }

    public int getPower() {
        int power = 0;
        for (UUID u : members) {
            Hoodlum h = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(u);
            if (h != null) {
                power += h.getPower();
            }
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
        int percent = Gangs.getInstance().getConfiguration().get("farm.percentFarmablePerChunk", Integer.class);
        int totalFarmable = (percent / 100) * (claims.size() - 1);
        if (totalFarm >= totalFarmable) {
            Gangs.getInstance().getSignCoordinator().addAlert(home, this);
        } else {
            Gangs.getInstance().getSignCoordinator().removeAlert(home);
        }
    }

    public void setTotalFarm(int totalFarm) {
        this.totalFarm = totalFarm;
        checkPercentage();
    }
}
