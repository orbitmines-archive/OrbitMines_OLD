package com.orbitmines.api.spigot.runnable.runnables;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class ScoreboardRunnable extends OMRunnable {

    public ScoreboardRunnable() {
        super(OMRunnable.TimeUnit.TICK, 5);
    }

    @Override
    public void run() {
        api.getScoreboardTitles().next();

        for (OMPlayer omp : OMPlayer.getPlayers()) {
            omp.updateScoreboard();
        }
    }
}
