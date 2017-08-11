package com.orbitmines.api.spigot.runnable.runnables;

import com.orbitmines.api.Server;
import com.orbitmines.api.spigot.runnable.OMRunnable;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class ServerCheckRunnable extends OMRunnable {

    private Server[] values;

    public ServerCheckRunnable() {
        super(TimeUnit.MINUTE, 1);

        values = Server.values();
    }


    @Override
    public void run() {
        for (Server server : values) {
            server.updateStatus();
        }
    }
}
