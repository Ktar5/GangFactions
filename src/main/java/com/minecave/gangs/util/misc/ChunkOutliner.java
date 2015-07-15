package com.minecave.gangs.util.misc;

import com.google.common.collect.Lists;
import com.minecave.gangs.Gangs;
import com.minecave.gangs.util.FakeBlockUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carter on 7/8/2015.
 */
public class ChunkOutliner {

    public ChunkOutliner(Chunk chunk, int ticks, Player player){
        List<Chunk> chnk = new ArrayList<>();
        chnk.add(chunk);
        new ChunkOutliner(chnk, ticks, player);
    }

    public ChunkOutliner(List<Chunk> chunks, int ticks, Player player){
        List<Block> blocks = new ArrayList<>();
        for(Chunk chunk : chunks){
            blocks.addAll(getBlocks(chunk, player.getLocation().getBlockY() - 1));
        }
        this.setBlocks(blocks, Material.GLOWSTONE, ticks, player);
    }
/*
ignore the terribly redundant code please >--<
 */
    private ArrayList<Block> getBlocks(Chunk chunk, int initY){
        Bukkit.getServer().broadcastMessage("7");
        int xmin = chunk.getX() * 16;
        int xmax = xmin + 15;
        int y;
        int zmin = chunk.getZ() * 16;
        int zmax = zmin + 15;

        int x = xmin;
        int z = zmin;

        int lastY = -1;
        Block block;

        ArrayList<Block> blocksToSet = new ArrayList<>();

        List<Material> mats = Lists.newArrayList(
                Material.AIR, Material.LOG, Material.LOG_2,
                Material.SUGAR_CANE_BLOCK, Material.ARMOR_STAND,
                Material.LEAVES, Material.LEAVES_2, Material.BANNER,
                Material.BED_BLOCK, Material.BARRIER, Material.BEACON,
                Material.BEDROCK, Material.CACTUS, Material.CAKE_BLOCK);

        while (x <= xmax) {
            y = initY;
            block = chunk.getBlock(x,y,zmax);
            while(mats.contains(block.getType())){
                block = chunk.getBlock(x, --y, zmax);
            }
            blocksToSet.add(block);
            if(lastY != -1){
                int amount = block.getY() - lastY;
                if(amount < 0){
                    while(amount < 0){
                        blocksToSet.add(chunk.getBlock(x-1,y++, zmax));
                        amount++;
                    }
                }else if(amount > 0){
                    while(amount > 0){
                        blocksToSet.add(chunk.getBlock(x,--y, zmax));
                        amount--;
                    }
                }
            }
            lastY = block.getY();
            x++;
        }

        lastY = -1;

        while (z  <= zmax){
            y = initY;
            block = chunk.getBlock(x,y,z);
            while(mats.contains(block.getType())){
                block = chunk.getBlock(x, --y, z);
            }
            blocksToSet.add(block);
            if(lastY != -1){
                int amount = block.getY() - lastY;
                if(amount < 0){
                    while(amount < 0){
                        blocksToSet.add(chunk.getBlock(x,y++, z-1));
                        amount++;
                    }
                }else if(amount > 0){
                    while(amount > 0){
                        blocksToSet.add(chunk.getBlock(x,--y, z));
                        amount--;
                    }
                }
            }
            lastY = block.getY();
            z++;
        }

        lastY = -1;

        while (x  >= xmin) {
            y = initY;
            block = chunk.getBlock(x,y,z);
            while(mats.contains(block.getType())){
                block = chunk.getBlock(x, --y, z);
            }
            blocksToSet.add(block);
            if(lastY != -1){
                int amount = block.getY() - lastY;
                if(amount < 0){
                    while(amount < 0){
                        blocksToSet.add(chunk.getBlock(x+1,y++, z));
                        amount++;
                    }
                }else if(amount > 0){
                    while(amount > 0){
                        blocksToSet.add(chunk.getBlock(x,--y, z));
                        amount--;
                    }
                }
            }
            lastY = block.getY();
            x--;
        }

        lastY = -1;

        while (z  >= zmin) {
            y = initY;
            block = chunk.getBlock(x,y,z);
            while(mats.contains(block.getType())){
                block = chunk.getBlock(x, --y, z);
            }
            blocksToSet.add(block);
            if(lastY != -1){
                int amount = block.getY() - lastY;
                if(amount < 0){
                    while(amount < 0){
                        blocksToSet.add(chunk.getBlock(x,y++, z+1));
                        amount++;
                    }
                }else if(amount > 0){
                    while(amount > 0){
                        blocksToSet.add(chunk.getBlock(x,--y, z));
                        amount--;
                    }
                }
            }
            lastY = block.getY();
            z--;
        }
        return (ArrayList<Block>) blocksToSet.clone();
    }

    private void setBlocks(List<Block> blocks, Material material, int ticks, Player player){
        blocks.stream().forEach(block -> FakeBlockUtil.setBlock(block, player));
        Bukkit.getScheduler().runTaskLater(Gangs.getInstance(), () -> blocks.stream().forEach(block -> block.getState().update()), ticks);
    }

}
