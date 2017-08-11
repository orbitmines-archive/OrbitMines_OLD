package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class BleedRunnable extends PlayerRunnable {

    public BleedRunnable() {
        super(OMRunnable.TimeUnit.TICK, 10);
    }

    @Override
    public void run(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;

        if (omp.getKitActive() == null)
            return;

        omp.getKitActive().update(omp);

        if (omp.getBleedingTime() == -1)
            return;

        Player p = omp.getPlayer();
        p.damage(0.5);
        p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, 152);
        omp.tickBleedingTime();
    }
}
