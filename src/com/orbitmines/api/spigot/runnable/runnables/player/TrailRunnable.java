package com.orbitmines.api.spigot.runnable.runnables.player;

import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.playerdata.TrailData;
import com.orbitmines.api.spigot.perks.Trail;
import com.orbitmines.api.spigot.perks.TrailType;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class TrailRunnable extends PlayerRunnable {

    public TrailRunnable() {
        super(OMRunnable.TimeUnit.TICK, 2);
    }

    @Override
    public void run(OMPlayer omp) {
        TrailData data = omp.trails();

        if (omp.getCooldowns().containsKey(Cooldown.TELEPORTING)) {
            if (omp.onCooldown(Cooldown.TELEPORTING))
                data.play(Trail.FIREWORK_SPARK, TrailType.CYLINDER_TRAIL, 1, 0);
        } else {
            data.play();
        }

        data.checkLastLocation();
    }
}
