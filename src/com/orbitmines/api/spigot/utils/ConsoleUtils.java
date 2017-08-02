package com.orbitmines.api.spigot.utils;

import org.bukkit.Bukkit;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public class ConsoleUtils {

    public static void empty() {
        Bukkit.getLogger().info("");
    }

    public static void msg(String msg) {
        Bukkit.getConsoleSender().sendMessage("[OrbitMines API] " + msg);
    }

    public static void warn(String msg) {
        Bukkit.getConsoleSender().sendMessage("[OrbitMines API] §c" + msg);
    }

    public static void succes(String msg) {
        Bukkit.getConsoleSender().sendMessage("[OrbitMines API] §a" + msg);
    }
}
