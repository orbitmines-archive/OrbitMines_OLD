package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.Database;
import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.KitPvP;

import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class TopKillsRunnable extends OMRunnable {

    private KitPvP kitPvP;

    public TopKillsRunnable() {
        super(TimeUnit.HOUR, 1);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void run() {
        Map<String, Integer> allVoters = Database.get().getIntEntries(Database.Table.PLAYERS, Database.Column.UUID, Database.Column.KITPVPKILLS);

        PodiumPlayer top1 = new PodiumPlayer(null, 0);
        PodiumPlayer top2 = new PodiumPlayer(null, 0);
        PodiumPlayer top3 = new PodiumPlayer(null, 0);

        for (String uuidString : allVoters.keySet()) {
            UUID uuid = UUID.fromString(uuidString);
            int votes = allVoters.get(uuidString);

            if (votes >= top1.getVotes()) {
                top3 = top2;
                top2 = top1;
                top1 = new PodiumPlayer(uuid, votes);
            } else if (votes >= top2.getVotes()) {
                top3 = top2;
                top2 = new PodiumPlayer(uuid, votes);
            } else if (votes >= top3.getVotes()) {
                top3 = new PodiumPlayer(uuid, votes);
            }
        }

        PodiumPlayer[] topKills = kitPvP.getTopKills();
        topKills[0] = top1;
        topKills[1] = top2;
        topKills[2] = top3;
    }
}
