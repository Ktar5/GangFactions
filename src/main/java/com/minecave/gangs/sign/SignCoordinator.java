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
import lombok.Getter;

public class SignCoordinator {

    @Getter
    private final Gangs plugin;

    public SignCoordinator(Gangs plugin) {
        this.plugin = plugin;
    }
}
