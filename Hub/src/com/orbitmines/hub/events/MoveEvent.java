package com.orbitmines.hub.events;

import com.orbitmines.hub.handlers.HubPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/*
* OrbitMines - @author Fadi Shawki - 9-8-2017
*/
public class MoveEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        HubPlayer omp = HubPlayer.getPlayer(player);

        if (!omp.canChat())
            omp.setCanChat();
    }
}
