package com.orbitmines.bungee.runnable;

import com.orbitmines.api.Database;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class DatabaseRunnable implements Runnable {

    @Override
    public void run() {
        Database.get().openConnection();
    }

}
