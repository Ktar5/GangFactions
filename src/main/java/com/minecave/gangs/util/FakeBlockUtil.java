package com.minecave.gangs.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Carter on 7/8/2015.
 */
public class FakeBlockUtil {

    public static void setBlock(Block block, Player[] players){
        Arrays.stream(players).forEach(player -> player.sendBlockChange(block.getLocation(), Material.ENDER_PORTAL, (byte) 0));
    }

    public static void setBlock(Block block, Player player){
        setBlock(block, new Player[]{player});
    }

}
