package com.orbitmines.api.spigot.handlers.worlds.voidgenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class ChunkGeneratorVoid extends ChunkGenerator {

    public Location getFixedSpawnLocation(World w, Random r) {
        Location l = new Location(w, 0, 70, 0);

        return l;
    }

    public List<BlockPopulator> getWorldPopulators(World w) {
        List<BlockPopulator> list = new ArrayList<>();

        return list;
    }

    public byte[][] generateEmptyWorld(World w, Random r, int cX, int cY, int cZ, BiomeGrid bg) {
        byte[][] res = new byte[w.getMaxHeight() / 16][];

        int x;
        int z;

        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                setBlock(res, x, 0, z, (byte) 0);
            }
        }

        return res;
    }

    @Override
    public short[][] generateExtBlockSections(World w, Random r, int xC, int zC, BiomeGrid biomes) {
        short[][] res = new short[w.getMaxHeight() / 16][];

        int x;
        int z;

        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                setBlock(res, x, 0, z, (short) 0);
            }
        }

        return res;
    }

    private void setBlock(byte[][] res, int x, int y, int z, byte b) {
        if (res[y >> 4] == null) {
            res[y >> 4] = new byte[4096];
        }
        res[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = b;
    }

    private void setBlock(short[][] res, int x, int y, int z, short s) {
        if (res[y >> 4] == null) {
            res[y >> 4] = new short[4096];
        }
        res[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = s;
    }
}
