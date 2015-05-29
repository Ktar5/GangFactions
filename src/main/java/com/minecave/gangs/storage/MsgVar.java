package com.minecave.gangs.storage;

/**
 * Created by Carter on 5/28/2015.
 */
public enum MsgVar {
    ROLE,
    GANG,
    PLAYER,
    POWER, MAX_POWER;

    public String var(){
        return "{" + this.toString() + "}";
    }


}
