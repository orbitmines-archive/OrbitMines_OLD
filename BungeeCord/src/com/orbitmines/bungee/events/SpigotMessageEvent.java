package com.orbitmines.bungee.events;

import com.orbitmines.api.Language;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.utils.ConsoleUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class SpigotMessageEvent implements Listener {

    private ProxyServer proxy;

    public SpigotMessageEvent() {
        proxy = ProxyServer.getInstance();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (!e.getTag().equals("OrbitMinesApi") || !(e.getSender() instanceof Server)) {
            return;
        }

        ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
        DataInputStream in = new DataInputStream(stream);

        try {
            String action = in.readUTF();

            if (action.equals("SetLanguage")) {
                Language language = Language.valueOf(in.readUTF());
                ProxiedPlayer p = proxy.getPlayer(in.readUTF());

                if (p != null) {
                    BungeePlayer omp = BungeePlayer.getBungeePlayer(p);
                    omp.setLanguage(language);
                }
            }
        } catch (IOException ex) {
            ConsoleUtils.warn("Error while receiving data from OrbitMinesApi:");
            ex.printStackTrace();
        }
    }
}