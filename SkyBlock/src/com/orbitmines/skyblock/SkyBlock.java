package com.orbitmines.skyblock;

import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class SkyBlock extends JavaPlugin {

    private static SkyBlock instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static SkyBlock getInstance() {
        return instance;
    }
}
