package com.orbitmines.api.spigot.runnable;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public abstract class PlayerRunnable {

    /* Works just like BungeeRunnable, but with uses all players now */

    private HashMap<Long, List<PlayerRunnable>> playerRunnables = new HashMap<>();

    protected OrbitMinesApi api;
    private OMRunnable.Time time;
    private BukkitTask task;

    public PlayerRunnable(OMRunnable.TimeUnit timeUnit, int amount) {
        this.api = OrbitMinesApi.getApi();
        this.time = new OMRunnable.Time(timeUnit, amount);

        start();
    }

    public abstract void run(OMPlayer omp);

    public OMRunnable.TimeUnit getTimeUnit() {
        return time.getTimeUnit();
    }

    public int getAmount() {
        return time.getAmount();
    }

    public void cancel() {
        if (task != null && playerRunnables.get(time.getTicks()).size() > 1) {
            task.cancel();
            playerRunnables.remove(time.getTicks());
        }

        playerRunnables.get(time.getTicks()).remove(this);
    }

    private void start() {
        long ticks = time.getTicks();

        if (playerRunnables.containsKey(ticks)) {
            playerRunnables.get(ticks).add(this);
            return;
        }

        playerRunnables.put(ticks, new ArrayList<>(Collections.singletonList(this)));

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                List<PlayerRunnable> runnables = playerRunnables.get(time.getTicks());
                for (OMPlayer omp : OMPlayer.getPlayers()) {
                    for (PlayerRunnable runnable : runnables) {
                        runnable.run(omp);
                    }
                }
            }
        }.runTaskTimer(api, 0, time.getTicks());
    }
}
