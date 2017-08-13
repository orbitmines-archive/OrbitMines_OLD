package com.orbitmines.survival.handlers;

import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.survival.handlers.region.Region;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class WorldPortal {

    private static List<WorldPortal> portals = new ArrayList<>();

    private Cooldown USAGE = new Cooldown(2000, Cooldown.Action.OTHER);

    private World world;
    private List<Block> blocks;

    public WorldPortal(World world, List<Block> blocks) {
        portals.add(this);

        this.world = world;
        this.blocks = blocks;
    }

    public World getWorld() {
        return world;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public boolean inPortal(SurvivalPlayer omp, Block block) {
        if (blocks.contains(block)) {
            if (!omp.onCooldown(USAGE)) {
                Region.random().teleport(omp);
                omp.resetCooldown(USAGE);
            }
            return true;
        }
        return false;
    }

    public static List<WorldPortal> getPortals() {
        return portals;
    }
}
