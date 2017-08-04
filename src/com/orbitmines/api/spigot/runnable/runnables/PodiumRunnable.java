package com.orbitmines.api.spigot.runnable.runnables;

import com.orbitmines.api.spigot.handlers.podium.Podium;
import com.orbitmines.api.spigot.runnable.OMRunnable;

public class PodiumRunnable extends OMRunnable {

    public PodiumRunnable() {
        super(TimeUnit.SECOND, 1);
    }

    @Override
    public void run() {
        for (Podium podium : Podium.getPodia()) {
            podium.update();
        }
    }
}
