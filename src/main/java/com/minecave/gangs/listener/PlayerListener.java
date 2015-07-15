/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.listener;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.util.TimeUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class PlayerListener implements Listener {

    @Getter
    private final Gangs plugin;
    int onlineMinutes;

    public PlayerListener(Gangs plugin) {
        this.plugin = plugin;
        plugin.getConfiguration().get("power.onlineTime", Integer.class);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getPlayer());
        if(h == null) {
            h = plugin.getHoodlumCoordinator().loadHoodlum(event.getPlayer().getUniqueId());
        }
        h.updateLastTimes();
        plugin.getHoodlumCoordinator().loadHoodlum(event.getPlayer());

        //NOW FOR THE AUTOMESSAGER THING
        for(String string : h.getWelcomer()){
            h.sendMessage(string);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getPlayer());
        if(h == null) {
            return;
        }
        Instant lastOnline = TimeUtil.localDateTimeToInstant(h.getLastOnline());
        if (ChronoUnit.MINUTES.between(lastOnline, Instant.now()) > onlineMinutes) {
            h.addPower(plugin.getConfiguration().get("power.online", Integer.class));
        }
        h.updateLastTimes();
        plugin.getHoodlumCoordinator().unloadHoodlum(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getEntity());
        if(h == null) {
            return;
        }
        h.removePower(plugin.getConfiguration().get("power.deathLoss", Integer.class));
    }

    @EventHandler
    public void signEdit(SignChangeEvent event) {
        String[] lines = event.getLines();
        boolean isDisplaySign = false;
        for(String s : lines) {
            if(s.equalsIgnoreCase(plugin.getConfiguration().get("sign.signCreationKey", String.class))) {
                isDisplaySign = true;
                break;
            }
        }
        if(isDisplaySign) {
            plugin.getSignCoordinator().addSign((Sign) event.getBlock().getState());
        }
    }

    @EventHandler
    public void signBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if(block.getType() == Material.SIGN ||
                block.getType() == Material.SIGN_POST ||
                block.getType() == Material.WALL_SIGN ||
                block.getState() instanceof Sign) {
            plugin.getSignCoordinator().removeSign((Sign) block.getState());
        }
    }
}
