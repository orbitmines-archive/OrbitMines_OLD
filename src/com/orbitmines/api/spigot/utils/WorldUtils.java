package com.orbitmines.api.spigot.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 28-7-2017
*/
public class WorldUtils {

    public static final List<Material> CANNOT_TRANSFORM = Arrays.asList(Material.LONG_GRASS, Material.YELLOW_FLOWER, Material.RED_ROSE, Material.DOUBLE_PLANT, Material.WOOD_STEP, Material.WOOD_STAIRS, Material.COBBLESTONE_STAIRS, Material.TRAP_DOOR, Material.IRON_TRAPDOOR, Material.IRON_TRAPDOOR, Material.SKULL, Material.WATER_LILY, Material.SIGN_POST, Material.WALL_SIGN, Material.TORCH, Material.FENCE, Material.WATER, Material.STATIONARY_WATER);

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
