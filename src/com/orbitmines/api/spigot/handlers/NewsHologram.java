package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.Database;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.npc.Hologram;
import com.orbitmines.api.spigot.utils.Serializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.HashMap;

/*
* OrbitMines - @author Fadi Shawki - 9-8-2017
*/
public class NewsHologram {

    private static HashMap<String, NewsHologram> holograms = new HashMap<>();

    private OrbitMinesApi api;
    private String name;
    private Hologram hologram;

    public NewsHologram(String name) {
        this(name, null);
    }

    public NewsHologram(String name, Location location) {
        api = OrbitMinesApi.getApi();
        holograms.put(name, this);

        this.name = name;

        if (!loadedFromDatabase()) {
            if (location == null)
                throw new NullPointerException();

            this.hologram = new Hologram(location);
            this.hologram.addLine("§a§l" + name);

            insert();
        }

        this.hologram.create();
    }

    public String getName() {
        return name;
    }

    public Hologram getHologram() {
        return hologram;
    }

    public void addLine(String line) {
        this.hologram.addLine(ChatColor.translateAlternateColorCodes('&', line));
        this.hologram.create();

        update();
    }

    public void setLine(int index, String line) {
        this.hologram.setLine(index, ChatColor.translateAlternateColorCodes('&', line));
        this.hologram.create();

        update();
    }

    public void removeLine(int index) {
        this.hologram.removeLine(index);

        if (hologram.getLines().size() != 0) {
            this.hologram.create();
            update();
        } else {
            delete(true);
        }
    }

    public void relocate(Location location) {
        this.hologram.setLocation(location);
        this.hologram.create();

        update();
    }

    public void delete(boolean update) {
        this.hologram.delete();
        holograms.remove(name);

        if (update)
            remove();
    }

    public String serialize() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : hologram.getLines()) {
            stringBuilder.append(line.replaceAll("§", "&"));
            stringBuilder.append(";");
        }

        return Serializer.serialize(hologram.getLocation()) + "~" + stringBuilder.toString().substring(0, stringBuilder.length() -1);
    }

    private void loadHologram(String string) {
        String[] data = string.split("~");

        if (this.hologram == null) {
            this.hologram = new Hologram(Serializer.parseLocation(data[0]));
        } else {
            this.hologram.setLocation(Serializer.parseLocation(data[0]));
            this.hologram.clearLines();
        }

        for (String line : data[1].split(";")) {
            this.hologram.addLine(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    private void insert() {
        Database.get().insert(Database.Table.HOLOGRAMS, Database.get().values(api.server().getServerType().toString(), getVariableName(), serialize()));
    }

    private void update() {
        Database.get().update(Database.Table.HOLOGRAMS, new Database.Where(Database.Column.HOLOGRAMNAME, getVariableName()), new Database.Set(Database.Column.DATA, serialize()));
    }

    private void remove() {
        Database.get().delete(Database.Table.HOLOGRAMS, new Database.Where(Database.Column.HOLOGRAMNAME, getVariableName()));
    }

    private String getVariableName() {
        return api.server().getServerType().toString() + "|" + this.name;
    }

    public boolean loadedFromDatabase() {
        if (!Database.get().contains(Database.Table.HOLOGRAMS, Database.Column.HOLOGRAMNAME, new Database.Where(Database.Column.HOLOGRAMNAME, getVariableName())))
            return false;

        String data = Database.get().getString(Database.Table.HOLOGRAMS, Database.Column.DATA, new Database.Where(Database.Column.HOLOGRAMNAME, getVariableName()));
        loadHologram(data);

        return true;
    }

    public static HashMap<String, NewsHologram> getHolograms() {
        return holograms;
    }

    public static NewsHologram getHologram(String name) {
        for (NewsHologram hologram : holograms.values()) {
            if (hologram.getName().equals(name))
                return hologram;
        }
        return null;
    }
}
