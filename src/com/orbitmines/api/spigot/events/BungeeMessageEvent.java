package com.orbitmines.api.spigot.events;

import com.orbitmines.api.Database;
import com.orbitmines.api.PluginMessageType;
import com.orbitmines.api.Server;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class BungeeMessageEvent implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord") && !channel.equals(PluginMessageType.CHANNEL))
            return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subChannel = in.readUTF();

            if (subChannel.equals("PlayerCount")) {
                String server = in.readUTF();

                if (!server.equalsIgnoreCase("all")) {
                    Server.valueOf(server.toUpperCase()).setOnlinePlayers(in.available() > 0 ? in.readInt() : 0);
                }
            } else if (channel.equals(PluginMessageType.CHANNEL)) {
                switch (PluginMessageType.valueOf(subChannel)) {
                    case CHECK_VOTES: {
                        UUID uuid = UUID.fromString(in.readUTF());
                        Player p = Bukkit.getPlayer(uuid);

                        if (p != null) {
                            OMPlayer omp = OMPlayer.getPlayer(p);
                            omp.updateVotes();
                        }
                        break;
                    }
                    case UPDATE_SILENT: {
                        UUID uuid = UUID.fromString(in.readUTF());
                        Player p = Bukkit.getPlayer(uuid);

                        if (p != null) {
                            OMPlayer omp = OMPlayer.getPlayer(p);
                            omp.setSilent(Boolean.parseBoolean(Database.get().getString(Database.Table.PLAYERS, Database.Column.SILENT, new Database.Where(Database.Column.UUID, uuid.toString()))));
                        }
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
