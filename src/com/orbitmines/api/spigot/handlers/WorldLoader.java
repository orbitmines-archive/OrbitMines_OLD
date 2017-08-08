package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
* OrbitMines - @author Fadi Shawki - 8-8-2017
*/
public class WorldLoader {

    /*
    *   The following is only for lobbies & maps that don't need saving.
    *   They are stored in plugins/<plugin name>/<world name>.zip
    *   The zip will look like this:
    *
    *   DO NOT use spaces in world names.
    *
    *   World.zip
    *      -> World
    *         -> regions
    *         -> data
    *         etc.
    * */

    private List<World> worlds;
    private boolean cleanUpPlayerData;

    /* If there are no worlds on the server that need inventories to be saved set cleanUpPlayerData = true */
    public WorldLoader(boolean cleanUpPlayerData) {
        this.worlds = new ArrayList<>();
        this.cleanUpPlayerData = cleanUpPlayerData;
    }

    public World loadWorld(String worldFile) {
        return loadWorld(worldFile, false);
    }

    public World loadWorld(String worldFile, boolean removeEntities) {
        try {
            extractZip(new File(OrbitMinesApi.getApi().getDataFolder() + "/" + worldFile + ".zip"), Bukkit.getWorldContainer().getAbsoluteFile());
            Bukkit.createWorld(new WorldCreator(worldFile));
            World world = Bukkit.getWorld(worldFile);
            worlds.add(world);

            if (removeEntities)
                WorldUtils.removeEntities(world);

            return world;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<World> getWorlds() {
        return worlds;
    }

    private void extractZip(File archive, File destDir) throws IOException {
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        ZipFile zipFile = new ZipFile(archive);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        byte[] buffer = new byte[16384];
        int len;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            String entryFileName = entry.getName();
            File dir = buildDirectoryHierarchyFor(entryFileName, destDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (!entry.isDirectory()) {
                File file = new File(destDir, entryFileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                while ((len = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }

                bos.flush();
                bos.close();
                bis.close();

            }
        }
        zipFile.close();
    }

    private File buildDirectoryHierarchyFor(String entryName, File destDir) {
        int lastIndex = entryName.lastIndexOf('/');
        String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        return new File(destDir, internalPathToEntry);
    }

    public void cleanUp() {
        worlds.forEach(world -> {
            Bukkit.unloadWorld(world, false);
            deleteDirectory(world.getWorldFolder());
        });
        if (cleanUpPlayerData) {
            deletePlayerData("world");
            deletePlayerData("world_nether");
            deletePlayerData("world_the_end");
        }
        deleteDirectory(new File(Bukkit.getWorldContainer().getAbsoluteFile().getPath() + "/__MACOSX"));
    }

    private boolean deleteDirectory(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            assert files != null;
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    private void deletePlayerData(String worldName) {
        File playerFilesDir = new File(worldName + "/playerdata");
        if (playerFilesDir.isDirectory()) {
            String[] playerDats = playerFilesDir.list();
            for (String playerDat : playerDats) {
                File datFile = new File(playerFilesDir, playerDat);
                datFile.delete();
            }
        }
    }
}
