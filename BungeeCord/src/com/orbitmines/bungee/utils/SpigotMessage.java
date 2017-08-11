package com.orbitmines.bungee.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.orbitmines.api.PluginMessageType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class SpigotMessage {

    public static void send(PluginMessageType type, ProxiedPlayer player, String... data) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(type.toString());

        for (String string : data) {
            out.writeUTF(string);
        }

        player.getServer().getInfo().sendData(PluginMessageType.CHANNEL, out.toByteArray());
    }
}
