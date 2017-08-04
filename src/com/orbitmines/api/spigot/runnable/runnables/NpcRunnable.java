package com.orbitmines.api.spigot.runnable.runnables;

import com.orbitmines.api.spigot.handlers.npc.Npc;
import com.orbitmines.api.spigot.handlers.npc.NpcArmorStand;
import com.orbitmines.api.spigot.runnable.OMRunnable;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class NpcRunnable extends OMRunnable {

    public NpcRunnable() {
        super(TimeUnit.TICK, 3);
    }

    /* Override to use */
    protected void run(NpcArmorStand npc) { }

    /* Override to use */
    protected void run(Npc npc) { }

    @Override
    public void run() {
        for (NpcArmorStand npc : NpcArmorStand.getNpcArmorStands()) {
            run(npc);
        }

        for (Npc npc : Npc.getNpcs()) {
            //TODO npc.checkEntity();

            run(npc);
        }
    }
}
