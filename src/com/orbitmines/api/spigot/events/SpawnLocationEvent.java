package com.orbitmines.api.spigot.events;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class SpawnLocationEvent implements Listener {

    private OrbitMinesApi api;

    public SpawnLocationEvent() {
        this.api = OrbitMinesApi.getApi();
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        if (api.server().teleportToSpawn(event.getPlayer()))
            event.setSpawnLocation(api.server().getSpawnLocation());
    }
}
