package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.command.commands.Management;
import com.minecave.gangs.util.LimitedQueue;
import com.minecave.gangs.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

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
    @Getter
    private LimitedQueue<String> messageBoard;


    public Gang(String name, OfflinePlayer owner, UUID uuid) {
        //not sure but we might decide to use map later if we need key->value pairing
        members = new HashSet<>();
        claims = new HashSet<>();
        //just using a random uuid for gang creation, will be used when adding the gang to GangCoordinator
        this.uuid = uuid;
        while (Gangs.getInstance().getGangCoordinator().gangExists(uuid)) {
            System.out.println("Gang already existed, needing to generate new uuid");
            this.uuid = UUID.randomUUID();
        }
        this.name = name;
        this.owner = owner;
        this.messageBoard = new LimitedQueue<>(5);
    }

    public Gang(String name, OfflinePlayer owner){
        this(name, owner, UUID.randomUUID());
    }

    public void setHome(Location location) {
        if (claims.contains(location.getChunk())) {
            home = location;
        }
    }

    public void addChunk(Chunk chunk){
        this.claims.add(chunk);
    }

    public boolean isChunkClaimed(Chunk chunk) {
        return claims.contains(chunk);
    }

    public boolean isSpawnChunk(Chunk chunk) {
        return chunk.equals(home.getChunk());
    }

    public boolean claimChunk(Location home, Chunk chunk) {
        if(claims.isEmpty()){
            this.home = home;
        }
        return claims.add(chunk);
    }

    public boolean unclaimChunk(Chunk chunk) {
        if(home != null){
            if(home.getChunk().equals(chunk)){
                return false; //cannot unclaim home chunk
            }
        }
        return claims.remove(chunk);
    }

    public int onlinePlayers(){
        int n = 0;
        for(UUID uuid : members){
            n += Bukkit.getOfflinePlayer(uuid).isOnline() ? 1 : 0;
        }
        return n;
    }

    public void unclaimAllChunks() {
        new HashSet<>(claims).forEach(this::unclaimChunk);
    }

    public boolean hasPlayer(Player player) {
        return members.contains(player.getUniqueId());
    }

    public boolean hasPlayer(UUID uuid){
        return members.contains(uuid);
    }

    public void addPlayer(Hoodlum player) {
        Bukkit.getServer().broadcastMessage(this.getClass().getName() + "1");
        if (!hasPlayer(player.getPlayerUUID())) {
            Bukkit.getServer().broadcastMessage(this.getClass().getName() + "2");

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
        return !isRaidable();
    }

    public int getPower() {
        int power = 0;
        for (UUID u : members) {
            Hoodlum h = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(u);
            if (h != null) {
                power += h.getPower();
            }
        }
        power += Gangs.getInstance().getHoodlumCoordinator().getHoodlum(owner.getUniqueId()).getPower();
        return power;
    }

    public int getMaxPower() {
        int power = 0;
        for (UUID u : members) {
            Hoodlum h = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(u);
            if (h != null) {
                power += h.getMaxPower();
            }
        }
        power += Gangs.getInstance().getHoodlumCoordinator().getHoodlum(owner.getUniqueId()).getMaxPower();
        return power;
    }

    public void subtractFromFarmTotal() {
        this.totalFarm -= 1;
        checkPercentage();
    }

    public void addToFarmTotal() {
        this.totalFarm += 1;
        checkPercentage();
    }

    public void checkPercentage() {
        if (totalFarm >= getPercentage() && !getClaims().isEmpty()) {
            Gangs.getInstance().getSignCoordinator().addAlert(home, this);
        } else {
            Gangs.getInstance().getSignCoordinator().removeAlert(home);
        }
    }

    public int getPercentage(){
        int percent = Gangs.getInstance().getConfiguration().get("farm.percentFarmablePerChunk", Integer.class);
        int totalFarmable = (percent / 100) * (claims.size() - 1);
        return totalFarmable;
    }

    public void setTotalFarm(int totalFarm) {
        this.totalFarm = totalFarm;
        checkPercentage();
    }

    public void addMessage(String string) {
        LocalDate date = LocalDate.now();
        String timestamp = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + date.getDayOfMonth();
        this.messageBoard.add(timestamp + ": " + StringUtil.colorString(string));
    }

    protected void loadMessages(List<String> strings){
        this.messageBoard.addAll(strings);
    }
}
