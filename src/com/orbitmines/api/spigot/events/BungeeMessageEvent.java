package com.orbitmines.api.spigot.events;

import com.orbitmines.api.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class BungeeMessageEvent implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord") && !channel.equals("OrbitMinesApi"))
            return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subChannel = in.readUTF();

            if (subChannel.equals("PlayerCount")) {
                String server = in.readUTF();

                if (!server.equalsIgnoreCase("all")) {
                    Server.valueOf(server.toUpperCase()).setOnlinePlayers(in.available() > 0 ? in.readInt() : 0);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
