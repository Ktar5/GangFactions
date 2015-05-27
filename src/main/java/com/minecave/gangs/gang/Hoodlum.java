package com.minecave.gangs.gang;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Carter on 5/25/2015.
 */
public class Hoodlum {

    @Getter
    private final UUID playerUUID;
    @Getter
    @Setter
    private int power;
    @Getter
    @Setter
    private int maxPower;
    @Getter
    @Setter
    private Gang gang;

    public Hoodlum(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    /**
     * Loads things from the database
     */
    private void loadPlayerFromUuid() {

    }

    public void addPower(int amount) {
        addPower(amount, false);
    }

    public void addPower(int amount, boolean force) {
        power = (power + amount > maxPower ? (force ? amount + power : maxPower) : amount + power);
    }

    public void removePower(int amount) {
        removePower(amount, false);
    }

    public void removePower(int amount, boolean force) {
        this.power = (power - amount < -maxPower ? (force ? power - amount : maxPower) : power - amount);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    public boolean isInGang() {
        return gang != null;
    }
}
