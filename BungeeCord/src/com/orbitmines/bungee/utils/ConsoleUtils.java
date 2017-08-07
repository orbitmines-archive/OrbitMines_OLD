package com.orbitmines.bungee.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public class ConsoleUtils {

    public static void empty() {
        ProxyServer.getInstance().getLogger().info("");
    }

    public static void msg(String msg) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("[OrbitMines API] " + msg));
    }

    public static void warn(String msg) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("[OrbitMines API] §c" + msg));
    }

    public static void succes(String msg) {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("[OrbitMines API] §a" + msg));
    }
}
