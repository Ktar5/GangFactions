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
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalDateTime;

public class PlayerListener implements Listener {

    @Getter
    private final Gangs plugin;

    public PlayerListener(Gangs plugin) {
        this.plugin = plugin;
        plugin.getConfiguration().get("power.onlineTime", Integer.class);
    }

    public boolean isWorldAllowed(World world){
        return Gangs.getInstance().getAllowedWorlds().contains(world.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getPlayer());
        if(h == null) {
            h = plugin.getHoodlumCoordinator().loadHoodlum(event.getPlayer().getUniqueId());
        }
        h.setLastLogon(LocalDateTime.now());
        //NOW FOR THE AUTOMESSAGER THING
        final Hoodlum finalH = h;
        Bukkit.getScheduler().runTaskLater(plugin, () -> finalH.getWelcomer().forEach(finalH::sendMessage),40L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getPlayer());
        if(h == null) {
            return;
        }
        h.setLastLogoff(LocalDateTime.now());
        plugin.getHoodlumCoordinator().unloadHoodlum(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(isWorldAllowed(event.getEntity().getLocation().getWorld())){
            return;
        }

        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getEntity());
        if(h == null) {
            return;
        }
        h.removePower(plugin.getConfiguration().get("power.deathLoss", Integer.class));
    }

    @EventHandler
    public void signEdit(SignChangeEvent event) {
        if(isWorldAllowed(event.getPlayer().getLocation().getWorld())){
            return;
        }

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
        if(isWorldAllowed(event.getPlayer().getLocation().getWorld())){
            return;
        }

        Block block = event.getBlock();
        if(block.getType() == Material.SIGN ||
                block.getType() == Material.SIGN_POST ||
                block.getType() == Material.WALL_SIGN ||
                block.getState() instanceof Sign) {
            plugin.getSignCoordinator().removeSign((Sign) block.getState());
        }
    }
}
