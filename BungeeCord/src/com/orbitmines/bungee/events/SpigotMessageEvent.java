package com.orbitmines.bungee.events;

import com.orbitmines.api.*;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.utils.ConsoleUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

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
        if (!e.getTag().equals("OrbitMinesApi") || !(e.getSender() instanceof  net.md_5.bungee.api.connection.Server)) {
            return;
        }

        ByteArrayInputStream stream = new ByteArrayInputStream(e.getData());
        DataInputStream in = new DataInputStream(stream);

        try {
            switch (PluginMessageType.valueOf(in.readUTF())) {

                case SET_LANGUAGE:
                    ProxiedPlayer player = proxy.getPlayer(UUID.fromString(in.readUTF()));
                    Language language = Language.valueOf(in.readUTF());

                    if (player != null) {

                        BungeePlayer omp = BungeePlayer.getBungeePlayer(player);
                        omp.setLanguage(language);
                    }
                    break;
                case UPDATE_COMMANDS:
                    Server server = Server.valueOf(in.readUTF());
                    String dataType = BungeeDatabaseType.COMMANDS.toString() + "_" + server.toString();
                    String[] commands = Database.get().getString(Database.Table.BUNGEE, Database.Column.DATA, new Database.Where(Database.Column.TYPE, dataType)).split("\\|");

                    OrbitMinesBungee.getBungee().getServerCommands().put(server, Arrays.asList(commands));
                    break;
            }

        } catch (IOException ex) {
            ConsoleUtils.warn("Error while receiving data from OrbitMinesApi:");
            ex.printStackTrace();
        }
    }
}
