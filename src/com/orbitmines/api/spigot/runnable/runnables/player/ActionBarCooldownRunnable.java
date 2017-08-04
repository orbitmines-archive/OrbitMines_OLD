package com.orbitmines.api.spigot.runnable.runnables.player;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class ActionBarCooldownRunnable extends PlayerRunnable {

    public ActionBarCooldownRunnable() {
        super(OMRunnable.TimeUnit.TICK, 2);
    }

    @Override
    public void run(OMPlayer omp) {
        omp.updateCooldownActionBar();
    }
}
