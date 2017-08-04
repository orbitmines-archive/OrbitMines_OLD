package com.orbitmines.api.spigot.runnable.runnables;

import com.orbitmines.api.Server;
import com.orbitmines.api.spigot.handlers.inventory.ServerSelectorInventory;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
* OrbitMines - @author Fadi Shawki - 29-7-2017
*/
public class ServerCountRunnable extends OMRunnable {

    private final Server[] servers;

    public ServerCountRunnable() {
        super(TimeUnit.SECOND, 1);

        servers = Server.values();
    }

    @Override
    public void run() {
        for (Server server : servers) {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            try {
                out.writeUTF("PlayerCount");
                out.writeUTF(server.toString().toLowerCase());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bukkit.getServer().sendPluginMessage(api, "BungeeCord", b.toByteArray());
        }

        ServerSelectorInventory.update();
    }
}
