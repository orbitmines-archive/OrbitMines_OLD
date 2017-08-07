package com.orbitmines.bungee.runnable.runnables;

import com.orbitmines.api.Message;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.runnable.BungeeRunnable;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class AnnouncementsRunnable extends BungeeRunnable {

    public AnnouncementsRunnable() {
        super(TimeUnit.MINUTE, 5);
    }

    @Override
    public void run() {
        Message message = bungee.getAnnouncements().next();
        for (BungeePlayer bungeePlayer : BungeePlayer.getPlayers()) {
            bungeePlayer.sendMessage(message);
        }
    }
}
