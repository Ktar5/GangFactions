package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import com.minecave.gangs.util.LimitedQueue;
import com.minecave.gangs.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Carter on 5/25/2015.
 */
public class Hoodlum {

    @Getter
    private final UUID playerUUID;
    @Setter
    private volatile int power;
    @Getter
    @Setter
    private int maxPower;
    @Getter
    @Setter
    private Gang gang;
    @Getter
    @Setter
    private boolean pledged;
    @Getter
    @Setter
    private UUID gangUUID;
    @Getter
    @Setter
    private GangRole role;
    @Getter
    @Setter
    private LocalDateTime lastLogon;
    @Getter
    @Setter
    private LocalDateTime lastLogoff;
    @Getter
    private List<String> invites;
    @Getter
    @Setter
    private boolean autoClaim;
    private LimitedQueue<String> notices;

    public Hoodlum(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.power = 10;
        this.maxPower = 10;
        this.autoClaim = false;
        this.invites = new ArrayList<>();
        this.role = GangRole.GANGLESS;
        this.notices = new LimitedQueue<>(5);
    }

    public synchronized void addPower(int amount) {
        addPower(amount, false);
    }

    public synchronized void addPower(int amount, boolean force) {
        power = (power + amount > maxPower ? (force ? amount + power : maxPower) : amount + power);
    }

    public synchronized void removePower(int amount) {
        removePower(amount, false);
    }

    public synchronized void removePower(int amount, boolean force) {
        this.power = (power - amount < -maxPower ? (force ? power - amount : maxPower) : power - amount);
    }

    public synchronized int getPower() {
        return power;
    }

    public String getName(){
        return Bukkit.getOfflinePlayer(this.getPlayerUUID()).getName();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public boolean isInGang() {
        return gang != null;
    }

    public boolean hasRole(GangRole role){
        if(role.equals(GangRole.SERVER_ADMIN)){
            return this.getPlayer().hasPermission("gangs.admin");
        }else{
            Bukkit.getServer().broadcastMessage(this.getClass().getName() + "1");
            Bukkit.getServer().broadcastMessage(this.role.ordinal() + "" + role.ordinal());

            return this.role.ordinal() >= role.ordinal();
        }
    }

    public int getMinPower() {
        return -maxPower;
    }

    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(playerUUID);
        if(player.isOnline())
            player.sendMessage(message);
    }

    //useless
    @Deprecated
    /*public void updateLastTimes() {
        setLastLogon(LocalDateTime.now());
        if(role != GangRole.GANGLESS && gang != null) {
            gang.setLastOnline(LocalDateTime.now());
        }
        setLastLogoff(LocalDateTime.now());
    }*/

    public boolean hasInvite(String gangName){
        return invites.contains(gangName.toLowerCase());
    }

    public boolean isOnline() {
        Player player = getPlayer();
        return player != null && player.isOnline();
    }

    public void addNotice(String string) {
        LocalDate date = LocalDate.now();
        String timestamp = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + date.getDayOfMonth();
        this.notices.add(timestamp + ": " + StringUtil.colorString(string));
    }

    public void removeInvite(String string){
        if(invites.contains(string)){
            invites.remove(string);
        }
    }

    public List<String> getWelcomer(){
        List<String> messages = new ArrayList<>();
        messages.add(Messages.get("player.welcome.header", MsgVar.PLAYER.var(), getPlayer().getName()));
        if(isInGang()){
            messages.addAll(getGang().getMessageBoard());
        }
        if(isPledged()){
            messages.add(Messages.get("pledge.pledgedWelcomer", MsgVar.GANG.var(),
                    Gangs.getInstance().getPledgeCoordinator()
                            .getPledge(getPlayerUUID()).getName()));
        }
        if(invites.size() != 0){
            messages.add(Messages.get("player.welcome.inviteHeader", "{SIZE}", String.valueOf(invites.size())));
            messages.addAll(invites.stream().map(string -> Messages.get("player.welcome.invite", MsgVar.GANG.var(), string)).collect(Collectors.toList()));
        }
        messages.add("");
        messages.addAll(notices);
        return StringUtil.colorList(messages);
    }

    public List<String> getNotices(){
        List<String> messages = new ArrayList<>();
        messages.addAll(notices);
        return StringUtil.colorList(messages);
    }

    public void loadNotices(List<String> strings){
        notices.addAll(strings);
    }
}
