package com.orbitmines.survival;

import org.bukkit.plugin.java.JavaPlugin;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class Survival extends JavaPlugin {

    private static Survival instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static Survival getInstance() {
        return instance;
    }
}
