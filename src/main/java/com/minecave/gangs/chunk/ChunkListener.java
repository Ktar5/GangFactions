/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.chunk;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.util.StringUtil;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ChunkListener implements Listener {

    private final Gangs plugin;

    public ChunkListener(Gangs plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChunkMove(PlayerMoveEvent event) {
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            return;
        }
        Player player = event.getPlayer();
        Chunk chunk = event.getTo().getChunk();
        Gang gang = plugin.getGangCoordinator().getGang(chunk);
        if (gang != null) {
            player.sendMessage(StringUtil.replaceAndColor(plugin.getMessages().get("gang.territory.playerEnter", String.class), "{gang}", gang.getName()));
        } else {
            Chunk prevChunk = event.getFrom().getChunk();
            gang = plugin.getGangCoordinator().getGang(prevChunk);
            if (gang != null) {
                player.sendMessage(StringUtil.replaceAndColor(plugin.getMessages().get("gang.territory.playerLeave", String.class), "{gang}", gang.getName()));
            }
        }
    }
}
