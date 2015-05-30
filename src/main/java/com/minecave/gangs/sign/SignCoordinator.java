/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of gangs.
 * 
 * gangs can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.gangs.sign;

import com.minecave.gangs.Gangs;
import com.minecave.gangs.gang.Gang;
import com.minecave.gangs.storage.CustomConfig;
import com.minecave.gangs.util.ConfigUtil;
import com.minecave.gangs.util.StringUtil;
import lombok.Getter;
import lombok.Value;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

public class SignCoordinator {

    @Getter
    private final Gangs plugin;
    @Getter
    private List<Sign> signs;
    @Getter
    private List<Alert> alerts;
    private Alert currentAlert;
    private String sign = "sign";

    public SignCoordinator(Gangs plugin) {
        this.plugin = plugin;
        this.signs = new ArrayList<>();
        this.alerts = new ArrayList<>();
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this::rotateAlerts,
                0, 20 * plugin.getConfiguration().get("sign.rotationTime", Integer.class));
    }

    @Value
    private class Alert {
        private final Location location;
        private final Gang gang;

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Alert)) {
                return false;
            }
            final Alert other = (Alert) o;
            final Object this$location = this.location;
            final Object other$location = other.location;
            return !(this$location == null ? other$location != null : !this$location.equals(other$location));
        }
    }

    private void rotateAlerts() {
        CustomConfig config = plugin.getSignConfig();
        if (!alerts.isEmpty()) {
            int index = currentAlert == null ? 0 : alerts.indexOf(currentAlert) + 1;
            currentAlert = alerts.get(index);
        } else {
            currentAlert = null;
        }
        setUpdateSign(config);
    }

    private void setUpdateSign(CustomConfig config) {
        String firstLine;
        String secondLine;
        String thirdLine;
        String fourthLine;
        if (currentAlert == null) {
            firstLine = config.get("sign.noAlerts", String.class);
            secondLine = "";
            thirdLine = "";
            fourthLine = "";
        } else {
            firstLine = config.get("sign.firstLine", String.class)
                    .replace("{gang}", currentAlert.getGang().getName())
                    .replace("{x}", String.valueOf(currentAlert.getLocation().getBlockX()))
                    .replace("{y}", String.valueOf(currentAlert.getLocation().getBlockY()))
                    .replace("{z}", String.valueOf(currentAlert.getLocation().getBlockZ()));
            secondLine = config.get("sign.secondLine", String.class)
                    .replace("{gang}", currentAlert.getGang().getName())
                    .replace("{x}", String.valueOf(currentAlert.getLocation().getBlockX()))
                    .replace("{y}", String.valueOf(currentAlert.getLocation().getBlockY()))
                    .replace("{z}", String.valueOf(currentAlert.getLocation().getBlockZ()));
            thirdLine = config.get("sign.thirdLine", String.class)
                    .replace("{gang}", currentAlert.getGang().getName())
                    .replace("{x}", String.valueOf(currentAlert.getLocation().getBlockX()))
                    .replace("{y}", String.valueOf(currentAlert.getLocation().getBlockY()))
                    .replace("{z}", String.valueOf(currentAlert.getLocation().getBlockZ()));
            fourthLine = config.get("sign.fourthLine", String.class)
                    .replace("{gang}", currentAlert.getGang().getName())
                    .replace("{x}", String.valueOf(currentAlert.getLocation().getBlockX()))
                    .replace("{y}", String.valueOf(currentAlert.getLocation().getBlockY()))
                    .replace("{z}", String.valueOf(currentAlert.getLocation().getBlockZ()));
        }
        for (int i = 0; i < signs.size(); i++) {
            Sign sign = signs.get(i);
            sign.setLine(0, StringUtil.colorString(firstLine));
            sign.setLine(1, StringUtil.colorString(secondLine));
            sign.setLine(2, StringUtil.colorString(thirdLine));
            sign.setLine(3, StringUtil.colorString(fourthLine));
            plugin.getServer().getScheduler().runTaskLater(plugin, sign::update, 10L);
        }
    }

    public void load() {
        CustomConfig config = plugin.getSignConfig();
        for (String s : config.getConfig().getStringList(sign)) {
            Location l = ConfigUtil.deserializeLocation(config.get(s, String.class));
            if (l == null) {
                continue;
            }
            if (l.getBlock().getType() == Material.SIGN ||
                    l.getBlock().getType() == Material.SIGN_POST ||
                    l.getBlock().getType() == Material.WALL_SIGN ||
                    l.getBlock().getState() instanceof Sign) {
                signs.add((Sign) l.getBlock().getState());
            }
        }
    }

    public void unload() {
        CustomConfig config = plugin.getSignConfig();
        List<String> signList = new ArrayList<>();
        for (Sign sign : signs) {
            Location l = sign.getLocation();
            signList.add(ConfigUtil.serializeLocation(l));
        }
        config.set(sign, signList);
    }

    public boolean removeSign(Sign sign) {
        if (signs.contains(sign)) {
            signs.remove(sign);
            return true;
        }
        return false;
    }

    public boolean addSign(Sign sign) {
        if (!signs.contains(sign)) {
            signs.add(sign);
            setUpdateSign(plugin.getSignConfig());
            return true;
        }
        return false;
    }

    public void addAlert(Location location, Gang gang) {
        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            if (alert.getLocation().equals(location)) {
                return;
            }
        }
        alerts.add(new Alert(location, gang));
    }

    public void removeAlert(Location location) {
        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            if (alert.getLocation().equals(location)) {
                alerts.remove(alert);
            }
        }
    }
}
