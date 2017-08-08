package com.orbitmines.fog;

import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class FoG extends JavaPlugin {

    private static FoG instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static FoG getInstance() {
        return instance;
    }
}
