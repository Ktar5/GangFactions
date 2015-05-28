package com.minecave.gangs.gang;

/**
 * Created by Carter on 5/27/2015.
 */
public enum GangRole {
    GANGLESS(0),
    MEMBER(1),
    MODERATOR(2),
    SUPER_MODERATOR(3),
    LEADER(4),
    SERVER_ADMIN(5);

    public int rank;

    GangRole(int rank){
        this.rank = rank;
    }
}
