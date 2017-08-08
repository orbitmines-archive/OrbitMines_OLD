package com.orbitmines.api.spigot.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.orbitmines.api.PluginMessageType;
import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class BungeeMessage {

    public static void send(PluginMessageType type, Player player, String... data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(type.toString());

        for (String string : data) {
            out.writeUTF(string);
        }

        player.sendPluginMessage(OrbitMinesApi.getApi(), PluginMessageType.CHANNEL, out.toByteArray());
    }
}
