package com.orbitmines.api.spigot.events;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        event.setQuitMessage(null);

        omp.logout();
    }
}
