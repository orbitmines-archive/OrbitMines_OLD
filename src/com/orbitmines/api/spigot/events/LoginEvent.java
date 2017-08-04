package com.orbitmines.api.spigot.events;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class LoginEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        event.setJoinMessage(null);

        omp.login();
    }
}
