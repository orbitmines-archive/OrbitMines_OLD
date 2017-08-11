package com.orbitmines.kitpvp.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class TeleportEvent implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
            e.setCancelled(true);
    }
}
