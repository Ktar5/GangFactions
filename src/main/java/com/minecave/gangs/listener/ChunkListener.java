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
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.storage.Messages;
import com.minecave.gangs.storage.MsgVar;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ChunkListener implements Listener {

    private final Gangs plugin;

    public ChunkListener(Gangs plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            String to = plugin.getGangCoordinator().getGangName(event.getTo());
            String from = plugin.getGangCoordinator().getGangName(event.getFrom());
            if (!to.equals(from)) {
                String gangName = Gangs.getInstance().getHoodlumCoordinator().getHoodlum(event.getPlayer()).getGang().getName();
                if(gangName.equalsIgnoreCase(from)){
                    event.getPlayer().sendMessage(Messages.get("gang.enter.your", MsgVar.GANG.var(), to));
                    return;
                }
                event.getPlayer().sendMessage(Messages.get("gang.enter.other", MsgVar.GANG.var(), to));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.SUGAR_CANE_BLOCK ||
                event.getBlock().getType() != Material.SUGAR_CANE) {
            return;
        }
        Material blockBelow = event.getBlock().getLocation().subtract(0, 1, 0).getBlock().getType();
        if (blockBelow == Material.SUGAR_CANE_BLOCK ||
                blockBelow == Material.SUGAR_CANE) {
            return;
        }
        Chunk chunk = event.getBlock().getChunk();
        Gang gang = plugin.getGangCoordinator().getGang(chunk);
        if (gang != null) {
            if (gang.isSpawnChunk(chunk)) {
                return;
            }
            gang.subtractFromFarmTotal();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType() != Material.SUGAR_CANE_BLOCK ||
                event.getBlock().getType() != Material.SUGAR_CANE) {
            return;
        }
        Chunk chunk = event.getBlock().getChunk();
        Gang gang = plugin.getGangCoordinator().getGang(chunk);
        if (gang != null) {
            if (gang.isSpawnChunk(chunk)) {
                return;
            }
            gang.addToFarmTotal();
        }
    }
}
