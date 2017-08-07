package com.orbitmines.bungee.runnable;

import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public abstract class ProxiedPlayerRunnable {

    private HashMap<Long, List<ProxiedPlayerRunnable>> playerRunnables = new HashMap<>();

    protected OrbitMinesBungee bungee;
    private BungeeRunnable.Time time;
    private ScheduledTask task;

    public ProxiedPlayerRunnable(BungeeRunnable.TimeUnit timeUnit, int amount) {
        this.bungee = OrbitMinesBungee.getBungee();
        this.time = new BungeeRunnable.Time(timeUnit, amount);

        start();
    }

    public abstract void run(BungeePlayer omp);

    public BungeeRunnable.TimeUnit getTimeUnit() {
        return time.getTimeUnit();
    }

    public int getAmount() {
        return time.getAmount();
    }

    public void cancel() {
        if (task != null && playerRunnables.get(time.getSeconds()).size() > 1) {
            task.cancel();
            playerRunnables.remove(time.getSeconds());
        }

        playerRunnables.get(time.getSeconds()).remove(this);
    }

    private void start() {
        long seconds = time.getSeconds();

        if (playerRunnables.containsKey(seconds)) {
            playerRunnables.get(seconds).add(this);
            return;
        }

        playerRunnables.put(seconds, new ArrayList<>(Collections.singletonList(this)));

        task = bungee.getProxy().getScheduler().schedule(bungee, () -> {
            List<ProxiedPlayerRunnable> runnables = playerRunnables.get(time.getSeconds());
            for (BungeePlayer omp : BungeePlayer.getPlayers()) {
                for (ProxiedPlayerRunnable runnable : runnables) {
                    runnable.run(omp);
                }
            }
        }, 0, time.getSeconds(), java.util.concurrent.TimeUnit.SECONDS);
    }
}
