package com.orbitmines.api.spigot.runnable.runnables.player;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class ScoreboardRunnable extends PlayerRunnable {

    public ScoreboardRunnable() {
        super(OMRunnable.TimeUnit.TICK, 5);
    }

    @Override
    public void run(OMPlayer omp) {
        omp.updateScoreboard();
    }
}
