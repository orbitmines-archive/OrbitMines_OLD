package com.orbitmines.api.spigot.runnables;

import com.orbitmines.api.spigot.handlers.npc.Npc;
import com.orbitmines.api.spigot.handlers.npc.NpcMoving;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class NpcMovingRunnable extends OMRunnable {

    public NpcMovingRunnable() {
        super(TimeUnit.SECOND, 1);
    }

    @Override
    public void run() {
        for (Npc npc : Npc.getNpcs()) {
            if (!(npc instanceof NpcMoving))
                continue;

            NpcMoving npcMoving = (NpcMoving) npc;

            if (npcMoving.getMoveLocations().size() > 0) {
                if (npcMoving.getMovingTo() != null) {
                    if (npcMoving.isAtLocation(npcMoving.getMovingTo())) {
                        int index = npcMoving.getMovingToIndex();
                        npcMoving.setSecondsToStay(npcMoving.getSecondsToStay() - 1);

                        if (npcMoving.getSecondsToStay() == 0) {
                            npcMoving.setMovingTo(npcMoving.nextLocation());
                            npcMoving.setSecondsToStay(npcMoving.getSecondsToStay(npcMoving.getMovingTo()));

                            if (npcMoving.getSecondsToStay() == 0) {
                                npcMoving.setMovingTo(npcMoving.nextLocation());
                                npcMoving.setSecondsToStay(npcMoving.getSecondsToStay(npcMoving.getMovingTo()));
                            }
                        } else {
                            int seconds = npcMoving.getSecondsToStay();

                            npcMoving.arrive(index, seconds);
                        }
                    }
                } else {
                    npcMoving.setMovingTo(npcMoving.nextLocation());
                }
            }

            npcMoving.moveToLocation(npcMoving.getMovingTo());
        }
    }
}
