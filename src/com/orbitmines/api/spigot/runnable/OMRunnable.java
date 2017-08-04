package com.orbitmines.api.spigot.runnable;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public abstract class OMRunnable {

    private HashMap<Long, List<OMRunnable>> runnables = new HashMap<>();

    protected OrbitMinesApi api;
    private Time time;
    private BukkitTask task;

    public OMRunnable(TimeUnit timeUnit, int amount) {
        this.api = OrbitMinesApi.getApi();
        this.time = new Time(timeUnit, amount);

        start();
    }

    public abstract void run();

    public TimeUnit getTimeUnit() {
        return time.getTimeUnit();
    }

    public int getAmount() {
        return time.getAmount();
    }

    public void cancel() {
        if (task != null && runnables.get(time.getTicks()).size() > 1) {
            task.cancel();
            runnables.remove(time.getTicks());
        }

        runnables.get(time.getTicks()).remove(this);
    }

    private void start() {
        long ticks = time.getTicks();

        if (runnables.containsKey(ticks)) {
            runnables.get(ticks).add(this);
            return;
        }

        runnables.put(ticks, new ArrayList<>(Collections.singletonList(this)));

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                for (OMRunnable runnable : runnables.get(time.getTicks())) {
                    runnable.run();
                }
            }
        }.runTaskTimer(api, 0, time.getTicks());
    }

    public static class Time {

        private TimeUnit timeUnit;
        private int amount;

        public Time(TimeUnit timeUnit, int amount) {
            this.timeUnit = timeUnit;
            this.amount = amount;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public int getAmount() {
            return amount;
        }

        public long getTicks() {
            return timeUnit.getTicks() * amount;
        }

        public boolean equals(Time time) {
            return getTicks() == time.getTicks();
        }
    }

    public enum TimeUnit {

        TICK(1),
        SECOND(20),
        MINUTE(1200),
        HOUR(72000);

        private long ticks;

        TimeUnit(long ticks) {
            this.ticks = ticks;
        }

        public long getTicks() {
            return ticks;
        }
    }
}
