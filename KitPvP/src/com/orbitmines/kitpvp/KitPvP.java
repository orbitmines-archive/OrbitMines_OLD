package com.orbitmines.kitpvp;

import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 2-8-2017
*/
public class KitPvP extends JavaPlugin {

    private static KitPvP instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static KitPvP getInstance() {
        return instance;
    }
}
