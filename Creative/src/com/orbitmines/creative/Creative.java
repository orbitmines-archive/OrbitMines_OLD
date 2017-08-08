package com.orbitmines.creative;

import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class Creative extends JavaPlugin {

    private static Creative instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static Creative getInstance() {
        return instance;
    }
}
