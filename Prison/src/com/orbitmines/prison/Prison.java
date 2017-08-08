package com.orbitmines.prison;

import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class Prison extends JavaPlugin {

    private static Prison instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static Prison getInstance() {
        return instance;
    }
}
