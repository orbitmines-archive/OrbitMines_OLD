package com.orbitmines.bungee.handlers;

import com.google.common.base.Charsets;
import com.orbitmines.bungee.OrbitMinesBungee;
import com.orbitmines.bungee.utils.ConsoleUtils;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 6-8-2017
*/
public class ConfigHandler {

    private OrbitMinesBungee bungee;

    private Map<String, Configuration> configs;
    private Map<String, File> files;

    public ConfigHandler() {
        bungee = OrbitMinesBungee.getBungee();
        configs = new HashMap<>();
        files = new HashMap<>();
    }

    public void setup(String... configs) {
        if (!bungee.getDataFolder().exists()) {
            bungee.getDataFolder().mkdir();
        }

        File fD = new File(bungee.getDataFolder() + "/configs");
        if (!fD.exists()) {
            fD.mkdir();
        }

        for (String config : configs) {
            File f = new File(bungee.getDataFolder() + "/configs", config + ".yml");
            files.put(config, f);

            if (!f.exists()) {
                copyFile(config + ".yml", f.toPath());
            }

            try {
                this.configs.put(config, YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(f), Charsets.UTF_8)));
            } catch (IOException e) {
                ConsoleUtils.warn("Error while loading " + config + ".yml");
                e.printStackTrace();
            }
        }
    }

    public Configuration get(String config) {
        return configs.get(config);
    }

    public void save(String config) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(get(config), new File(bungee.getDataFolder() + "/configs", config + ".yml"));
        } catch (IOException ex) {
            ConsoleUtils.warn("Error while saving " + config + ".yml");
            ex.printStackTrace();
        }
    }

    public void reload(String config) {
        try {
            this.configs.put(config, YamlConfiguration.getProvider(YamlConfiguration.class).load(new InputStreamReader(new FileInputStream(this.files.get(config)), Charsets.UTF_8)));
        } catch (IOException ex) {
            ConsoleUtils.warn("Error while reloading " + config + ".yml");
            ex.printStackTrace();
        }
    }

    private void copyFile(String filename, Path path) {
        try {
            Files.copy(bungee.getResourceAsStream(filename), path);
        } catch (IOException ex) {
            ConsoleUtils.warn("Error while creating " + filename);
            ex.printStackTrace();
        }
    }
}
