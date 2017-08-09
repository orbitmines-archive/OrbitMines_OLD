package com.orbitmines.hub.runnables;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.api.spigot.runnable.PlayerRunnable;
import com.orbitmines.hub.Hub;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 9-8-2017
*/
public class SpawnBoundariesRunnable extends PlayerRunnable {

    private Hub hub;

    public SpawnBoundariesRunnable() {
        super(OMRunnable.TimeUnit.SECOND, 1);

        hub = Hub.getInstance();
    }

    @Override
    public void run(OMPlayer omp) {
        Player player = omp.getPlayer();

        if (player.getLocation().getY() < 50 || player.getLocation().distance(hub.getSpawnLocation()) > 100)
            player.teleport(hub.getSpawnLocation());
    }
}
