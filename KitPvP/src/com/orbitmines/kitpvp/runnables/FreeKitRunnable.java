package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.KitPvP;

import java.util.Calendar;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class FreeKitRunnable extends OMRunnable {

    private KitPvP kitPvP;

    public FreeKitRunnable() {
        super(TimeUnit.HOUR, 1);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void run() {
        kitPvP.setFreeKitEnabled(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY);
    }
}
