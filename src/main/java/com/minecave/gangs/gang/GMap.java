/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.gang;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.storage.CustomConfig;
import com.minecave.gangs.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GMap {

    private final String gmap = "gmap.";
    private final char[] chars;

    public GMap() {
        List<String> otherChars = Gangs.getInstance().getMessages().getConfig().getStringList(gmap + "symbols");
        chars = new char[26 + otherChars.size()];
        for (int i = 0; i < otherChars.size(); i++) {
            String s = otherChars.get(i);
            chars[i] = s == null ? (char) i : s.charAt(0);
        }
        for (int i = otherChars.size(); i < otherChars.size() + 26; i++) {
            chars[i] = (char) ((i - otherChars.size()) + 65);
        }
    }

    public void generateSendClaimMap(Player player) {
        CustomConfig messages = Gangs.getInstance().getMessages();
        GangCoordinator gangCoordinator = Gangs.getInstance().getGangCoordinator();
        String[] display = new String[10];
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        Direction direction = getDirection(player);
        display[0] = StringUtil.replace(messages.get(gmap + "title", String.class),
                "{X}", String.valueOf(x));
        display[0] = StringUtil.replace(display[0],
                "{Y}", String.valueOf(y));
        display[0] = StringUtil.replace(display[0],
                "{GANG}", gangCoordinator.getGangName(player.getLocation()));
        List<String> compass = messages.getConfig().getStringList(gmap + "compass");
        Map<Character, String> foundGangs = new HashMap<>();

         /*
        N //neg Z
        E //pos X
        S //pos Z
        W //neg X
         */

        int h = 0;
        int g = 0;
        for (int j = -4; j < 4; j++) {
            StringBuilder builder = new StringBuilder("&7");
            for (int i = -19; i < 20; i++) {
                if (h >= 3) {
                    Chunk chunk = player.getWorld().getChunkAt((int) Math.floor(x) + j * 16,
                            (int) Math.floor(y) + i * 16);
                    String gangName = gangCoordinator.getGangName(chunk);
                    if(gangName.equalsIgnoreCase("Wilderness")) {
                        builder.append('-');
                    } else {
                        char c = chars[foundGangs.size()];
                        builder.append(c);
                        foundGangs.put(c, gangName);
                    }
                } else {
                    builder.append(checkForCardinal(direction, String.valueOf(compass.get(h).charAt(g))));
                }
            }
            g++;
            if(h < 9) {
                display[++h] = builder.toString();
            }
        }
        StringBuilder builder = new StringBuilder("&7");
        int i = 0;
        for(Map.Entry<Character, String> entry : foundGangs.entrySet()) {
            if(i++ != 0) {
                builder.append(", ");
            }
            //{SYMBOL}: {GANG}
            String append = StringUtil.replace(messages.get(gmap + "gangsFormat", String.class),
                    "{SYMBOL}", String.valueOf(entry.getKey()));
            append = StringUtil.replace(append, "{GANG}", entry.getValue());
            builder.append(append);
        }
        display[9] = builder.toString();

        for (String s : display) {
            player.sendMessage(StringUtil.colorString(s));
        }
    }

    private String checkForCardinal(Direction direction, String input) {
        if (input.contains(direction.name())) {
            input = StringUtil.replace(input, direction.name(), ChatColor.RED + direction.name());
        }
        return input;
    }

    public Direction getDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360;
        }
        if (0 <= rotation && rotation < 45) {
            return Direction.N;
        } else if (45 <= rotation && rotation < 135) {
            return Direction.E;
        } else if (135 <= rotation && rotation < 225) {
            return Direction.S;
        } else if (225 <= rotation && rotation < 315) {
            return Direction.W;
        } else {
            return Direction.N;
        }
    }

    private enum Direction {
        N, //neg Z
        E, //pos X
        S, //pos Z
        W, //neg X
    }
}
