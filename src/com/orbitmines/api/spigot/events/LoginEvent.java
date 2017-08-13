package com.orbitmines.api.spigot.events;

import com.orbitmines.api.PluginMessageType;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.utils.BungeeMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class LoginEvent implements Listener {

    private OrbitMinesApi api;

    private boolean updatedCommands = false;

    public LoginEvent() {
        api = OrbitMinesApi.getApi();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        OMPlayer omp = api.server().newPlayerInstance(event.getPlayer());
        omp.login();

        omp.destroyHiddenNpcs();

        if (updatedCommands)
            return;

        updatedCommands = true;

        BungeeMessage.send(PluginMessageType.UPDATE_COMMANDS, event.getPlayer(), api.server().getServerType().toString());
    }
}
