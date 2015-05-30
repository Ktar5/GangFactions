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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        plugin.getConfiguration().get("power.onlineTime", int.class);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getPlayer());
        h.updateLastTimes();
        plugin.getHoodlumCoordinator().loadHoodlum(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Hoodlum h = plugin.getHoodlumCoordinator().getHoodlum(event.getPlayer());
        Instant lastOnline = TimeUtil.localDateTimeToInstant(h.getLastOnline());
        if (ChronoUnit.MINUTES.between(lastOnline, Instant.now()) > onlineMinutes) {
            h.addPower(plugin.getConfiguration().get("power.online", int.class));
        }
        h.updateLastTimes();
        plugin.getHoodlumCoordinator().unloadHoodlum(event.getPlayer());
    }
}
