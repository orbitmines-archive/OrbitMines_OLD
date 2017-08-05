package com.orbitmines.api.spigot.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class SpawnLocationEvent implements Listener {

    private Location location;

    public SpawnLocationEvent(Location location) {
        this.location = location;
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(location);
    }
}
