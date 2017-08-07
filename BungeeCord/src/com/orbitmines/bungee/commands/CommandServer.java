package com.orbitmines.bungee.commands;

import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.bungee.handlers.BungeePlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandServer extends Command {

    private final String[] alias = {"/server"};

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        ProxiedPlayer player = omp.getPlayer();

        if (a.length == 1) {
            Server server = omp.getServer();
            player.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Verbonden met", "Connected to")) + " " + server.getColor() + "§l" + server.getName() + "§7."));
        } else if (a.length == 2) {
            try {
                if (a[1].equalsIgnoreCase("minigames")) {
                    a[1] = "hub";
                    //omp.toMiniGameArea();//TODO
                }

                omp.connect(Server.valueOf(a[1].toUpperCase()));
            } catch (IllegalArgumentException ex) {
                omp.sendMessage(new Message("§7Server §6" + a[1] + "§7 bestaat niet.", "§7Server §6" + a[1] + "§7 doesn't exist."));
            }
        } else {
            player.sendMessage(new TextComponent("§7" + omp.getMessage(new Message("Gebruik", "Use")) + " §6/server <server>§7."));
        }
    }
}
