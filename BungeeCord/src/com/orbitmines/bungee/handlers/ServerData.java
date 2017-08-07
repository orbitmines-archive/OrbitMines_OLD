package com.orbitmines.bungee.handlers;

import com.orbitmines.api.Server;
import com.orbitmines.bungee.utils.ServerUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
import java.util.Collections;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class ServerData {

    private Server server;
    private String playersString;
    private int silentPlayers;
    private Collection<ProxiedPlayer> players;

    public ServerData(Server server) {
        this.server = server;
        this.silentPlayers = 0;

        ServerInfo serverInfo = ServerUtils.getServerInfo(server);
        if (serverInfo != null)
            this.players = serverInfo.getPlayers();
        else
            this.players = Collections.emptyList();

        if (players.size() == 0) {
            playersString = "";
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (ProxiedPlayer player : players) {
            BungeePlayer bungeePlayer = BungeePlayer.getBungeePlayer(player);

            if (!bungeePlayer.isSilent()) {
                stringBuilder.append(bungeePlayer.getColorName());
                stringBuilder.append("§7, ");
            } else {
                silentPlayers++;
            }
        }

        playersString = stringBuilder.toString().substring(0, stringBuilder.length() -4);
    }

    public Server getServer() {
        return server;
    }

    public int getPlayerCount() {
        return players.size() - silentPlayers;
    }

    public String parse() {
        switch (server.getStatus()) {

            case ONLINE:
                return " " + server.getColor() + "§l" + server.getName() + "§7(" + getPlayerCount() + "§7) " + playersString;
            default:
                return " " + server.getColor() + "§l" + server.getName() + "§7(" + server.getStatus().getColor() + "§l" + server.getStatus().getName() + "§7)";
        }
    }
}
