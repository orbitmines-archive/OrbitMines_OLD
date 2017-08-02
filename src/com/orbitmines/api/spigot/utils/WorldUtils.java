package com.orbitmines.api.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public class WorldUtils {

    public static void removeAllEntities() {
        for (World world : Bukkit.getWorlds()) {
            removeEntities(world);
        }
    }

    public static void removeEntities(World world) {
        for (Entity en : world.getEntities()) {
            if (en instanceof Player)
                continue;

            en.remove();
        }
    }
}
