package com.orbitmines.api.spigot.events.npc;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class WorldSwitchEvent implements Listener {

    private OrbitMinesApi api;

    public WorldSwitchEvent() {
        api = OrbitMinesApi.getApi();
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                omp.destroyHiddenNpcs();
            }
        }.runTaskLater(api, 1);
    }
}
