package com.orbitmines.bungee.utils;

import com.orbitmines.api.Server;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

/*
* OrbitMines - @author Fadi Shawki - 7-8-2017
*/
public class ServerUtils {

    public static ServerInfo getServerInfo(Server server) {
        return ProxyServer.getInstance().getServerInfo(server.toString().toLowerCase());
    }

    public static Server getServer(ServerInfo serverInfo) {
        return Server.valueOf(serverInfo.getName().toUpperCase());
    }
}
