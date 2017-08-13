package com.orbitmines.survival.runnables;

import com.orbitmines.api.Database;
import com.orbitmines.api.VipRank;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.survival.Survival;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class MobHeadRunnable extends OMRunnable {

    private Survival survival;

    public MobHeadRunnable() {
        super(TimeUnit.HOUR, 1);

        survival = Survival.getInstance();
    }

    @Override
    public void run() {
        List<Map<Database.Column, String>> entries = Database.get().getEntries(Database.Table.PLAYERS, Database.Column.UUID, Database.Column.VIPRANK);

        Map<VipRank, List<UUID>> mobHeads = survival.getMobHeads();
        for (VipRank vipRank : VipRank.values()) {
            mobHeads.get(vipRank).clear();
        }

        for (Map<Database.Column, String> entry : entries) {
            VipRank vipRank = VipRank.valueOf(entry.get(Database.Column.VIPRANK));
            if (vipRank == VipRank.NONE)
                continue;

            mobHeads.get(vipRank).add(UUID.fromString(entry.get(Database.Column.UUID)));
        }
    }
}
