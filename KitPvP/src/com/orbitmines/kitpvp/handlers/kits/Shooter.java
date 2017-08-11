package com.orbitmines.kitpvp.handlers.kits;

import com.orbitmines.api.spigot.runnable.OMRunnable;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public interface Shooter {

    OMRunnable.Time getReceiveTime();

    int getMaxProjectiles();

}
