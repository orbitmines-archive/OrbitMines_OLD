package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class SchematicGenerator {

    public static String CONFIG = "schematicgenerator";
    public static ItemStack WAND = new ItemBuilder(Material.GOLD_AXE, 1, 0, "§eSchematic Generator").build();

    private OrbitMinesApi api;
    private static Map<OMPlayer, SchematicGenerator> generators = new HashMap<>();

    private String name;
    private Material air;
    private String output;

    private Location spawn;
    private Location l1;
    private Location l2;

    public SchematicGenerator(OMPlayer omp, String name, Material air, String output, Location spawn) {
        api = OrbitMinesApi.getApi();
        generators.put(omp, this);

        this.name = name;
        this.air = air;
        this.output = output;
        this.spawn = spawn;
    }

    public String getName() {
        return name;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public Location getL1() {
        return l1;
    }

    public void setL1(Location l1) {
        this.l1 = l1;
    }

    public Location getL2() {
        return l2;
    }

    public void setL2(Location l2) {
        this.l2 = l2;
    }

    public void generate() {
        List<String> list = new ArrayList<>();
        for (Block b : LocationUtils.getBlocksBetween(l1, l2)) {
            if (!b.isEmpty()) {
                Location l1 = getSpawn();
                Location l2 = b.getLocation();

                Material material = b.getType();

                if (air != null && b.getType() == air)
                    material = Material.AIR;

                list.add(output.replace("%x", "" + (l2.getBlockX() - l1.getBlockX())).replace("%y", "" + (l2.getBlockY() - l1.getBlockY())).replace("%z", "" + (l2.getBlockZ() - l1.getBlockZ())).replace("%m", "Material." + material.toString()).replace("%d", "" + b.getData()));
            }
        }

        api.getConfigHandler().get(CONFIG).set(name, list);
        api.getConfigHandler().save(CONFIG);
    }

    public static Map<OMPlayer, SchematicGenerator> getGenerators() {
        return generators;
    }
}
