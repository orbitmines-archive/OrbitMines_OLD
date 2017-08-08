package com.orbitmines.api.spigot.handlers.worlds.voidgenerator;

import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class WorldCreatorVoid extends WorldCreator {

    private ChunkGenerator chunkGenerator = new ChunkGeneratorVoid();

    public WorldCreatorVoid(String name) {
        super(name);
    }

    @Override
    public ChunkGenerator generator() {
        return chunkGenerator;
    }
}
