package com.orbitmines.api.spigot.events;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PlayerChatEvent implements Listener {

    private OrbitMinesApi api;

    public PlayerChatEvent() {
        api = OrbitMinesApi.getApi();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

        api.server().format(event, omp);
    }
}
