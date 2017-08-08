package com.orbitmines.bungee.events;

import com.orbitmines.api.Server;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.utils.ServerUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.getServer().getInfo() == event.getTarget())
            BungeePlayer.getBungeePlayer(player).onQuit();
    }

    @EventHandler
    public void onQuit(ServerKickEvent event) {
        event.setCancelled(true);
        event.setCancelServer(ServerUtils.getServerInfo(Server.HUB));
    }
}
