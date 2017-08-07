package com.orbitmines.bungee.commands;

import com.orbitmines.api.Server;
import com.orbitmines.bungee.handlers.BungeePlayer;
import com.orbitmines.bungee.handlers.ServerData;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class CommandList extends Command {

    private final String[] alias = {"/list","/glist"};

    private final Server[] values = Server.values();

    @Override
    public String[] getAlias() {
        return alias;
    }

    @Override
    public void dispatch(BungeePlayer omp, String[] a) {
        int playerCount = 0;
        List<ServerData> dataList = new ArrayList<>();

        for (Server server : values) {
            ServerData data = new ServerData(server);
            playerCount += data.getPlayerCount();

            dataList.add(data);
        }

        ProxiedPlayer player = omp.getPlayer();
        player.sendMessage(new TextComponent("§6§lOrbitMines§4§lNetwork§7(" + playerCount + "§7)"));

        for (ServerData data : dataList) {
            player.sendMessage(new TextComponent(data.parse()));
        }
    }
}
