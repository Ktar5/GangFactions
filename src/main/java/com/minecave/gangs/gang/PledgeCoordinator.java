package com.minecave.gangs.gang;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Carter on 7/14/2015.
 */
public class PledgeCoordinator {

    private Map<UUID, Pledge> pledgeMap;

    public PledgeCoordinator() {
        pledgeMap = new HashMap<>();
    }

    public void remove(UUID leaderId) {
        if(pledgeMap.containsKey(leaderId)){
            pledgeMap.remove(leaderId);
        }
    }
}
