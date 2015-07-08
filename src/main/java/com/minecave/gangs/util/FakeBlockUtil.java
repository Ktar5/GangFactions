package com.minecave.gangs.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Created by Carter on 7/8/2015.
 */
public class FakeBlockUtil {

    public void setBlock(Location location, Player[] players){
        Arrays.stream(players).forEach(player -> player.sendBlockChange(location, Material.ENDER_PORTAL, (byte) 0));
    }

    public void setBlock(Location location, Player player){
        setBlock(location, new Player[]{player});
    }

}
