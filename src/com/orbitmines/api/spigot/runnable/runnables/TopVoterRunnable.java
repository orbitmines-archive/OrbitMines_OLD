package com.orbitmines.api.spigot.runnable.runnables;

import com.orbitmines.api.Database;
import com.orbitmines.api.spigot.handlers.podium.PodiumPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;

import java.util.Map;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class TopVoterRunnable extends OMRunnable {

    public TopVoterRunnable() {
        super(TimeUnit.HOUR, 1);
    }

    @Override
    public void run() {
        Map<String, Integer> allVoters = Database.get().getIntEntries(Database.Table.PLAYERS, Database.Column.UUID, Database.Column.VOTES);

        PodiumPlayer voter1 = new PodiumPlayer(null, 0);
        PodiumPlayer voter2 = new PodiumPlayer(null, 0);
        PodiumPlayer voter3 = new PodiumPlayer(null, 0);
        PodiumPlayer voter4 = new PodiumPlayer(null, 0);
        PodiumPlayer voter5 = new PodiumPlayer(null, 0);

        for (String uuidString : allVoters.keySet()) {
            UUID uuid = UUID.fromString(uuidString);
            int votes = allVoters.get(uuidString);

            if (votes >= voter1.getVotes()) {
                voter5 = voter4;
                voter4 = voter3;
                voter3 = voter2;
                voter2 = voter1;
                voter1 = new PodiumPlayer(uuid, votes);
            } else if (votes >= voter2.getVotes()) {
                voter5 = voter4;
                voter4 = voter3;
                voter3 = voter2;
                voter2 = new PodiumPlayer(uuid, votes);
            } else if (votes >= voter3.getVotes()) {
                voter5 = voter4;
                voter4 = voter3;
                voter3 = new PodiumPlayer(uuid, votes);
            } else if (votes >= voter4.getVotes()) {
                voter5 = voter4;
                voter4 = new PodiumPlayer(uuid, votes);
            } else if (votes >= voter5.getVotes()) {
                voter5 = new PodiumPlayer(uuid, votes);
            }
        }

        PodiumPlayer[] topVoters = api.getTopVoters();
        topVoters[0] = voter1;
        topVoters[1] = voter2;
        topVoters[2] = voter3;
        topVoters[3] = voter4;
        topVoters[4] = voter5;
    }
}
