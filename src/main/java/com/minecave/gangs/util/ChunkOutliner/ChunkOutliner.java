package com.minecave.gangs.util.ChunkOutliner;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Carter on 7/8/2015.
 */
public class ChunkOutliner {

    private final List<Location> oldBlocks;

    public ChunkOutliner(Chunk chunk, Material material, int ticks, Player player){

        oldBlocks = null;
    }

    private void showChunk(Chunk chunk, int initY){
        int xmin = chunk.getX() * 16;
        int xmax = xmin + 15;
        int y = initY;
        int zmin = chunk.getZ() * 16;
        int zmax = zmin + 15;

        int x = 0;
        int z = 0;

        World world = chunk.getWorld();

        while (x < xmax) {
            Block block = chunk.getBlock(x,y,z);
            chunk.getBlock(x,y,z)
        }

        // Add #2
        while (z + 1 <= zmax)
        {
            z++;
            i++;
            if (i % steps == step && (i % keepEvery == 0 && i % skipEvery != 0)) ret.add(new Location(world, x + 0.5, y + 0.5, z + 0.5));
        }

        // Add #3
        while (x - 1 >= xmin)
        {
            x--;
            i++;
            if (i % steps == step && (i % keepEvery == 0 && i % skipEvery != 0)) ret.add(new Location(world, x + 0.5, y + 0.5, z + 0.5));
        }

        // Add #4
        while (z - 1 >= zmin)
        {
            z--;
            i++;
            if (i % steps == step && (i % keepEvery == 0 && i % skipEvery != 0)) ret.add(new Location(world, x + 0.5, y + 0.5, z + 0.5));
        }


    }


}
