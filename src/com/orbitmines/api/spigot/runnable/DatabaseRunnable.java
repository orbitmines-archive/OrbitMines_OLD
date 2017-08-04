package com.orbitmines.api.spigot.runnable;

import com.orbitmines.api.Database;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class DatabaseRunnable extends BukkitRunnable {

    @Override
    public void run() {
        Database.get().openConnection();
    }
}
