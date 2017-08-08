package com.orbitmines.minigames;

import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class MiniGames extends JavaPlugin {

    private static MiniGames instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static MiniGames getInstance() {
        return instance;
    }
}
