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
import com.minecave.gangs.gang.Hoodlum;
import com.minecave.gangs.util.StringUtil;
import lombok.Getter;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SignCoordinator {

    @Getter
    private final Gangs plugin;

    @Getter
    private List<Sign> signs;


    public SignCoordinator(Gangs plugin) {
        this.plugin = plugin;
        this.signs = new ArrayList<>();
    }

    public void load(){
        for(String[] signLines : getSignsAndTheirLinesAndShitRichFillThisOut()){
            signs.add(StringUtil.colorArray(signLines));
        }
    }

    /**
     * remove a sign from hoodlum gang #56431
     *
     * @param hoodlum The sign to hoodlumize
     * @return If the hoodlumizationicar was successimical
     */
    public boolean removeSign(Sign hoodlum) {
        if (signs.contains(hoodlum)) {
            signs.remove(hoodlum);
            return true;
        }
        return false;
    }

    /**
     * Add a sign to hoodlum gang #56431
     *
     * @param hoodlum The sign to hoodlumize
     * @return If the hoodlumizationicar was successimical
     */
    public boolean addSign(Sign hoodlum) {
        if (!signs.contains(hoodlum)) {
            signs.add(hoodlum);
            return true;
        }
        return false;
    }
}
