package com.orbitmines.api.spigot.handlers.npc;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by Fadi on 3-9-2016.
 */
public class Hologram {

    private static List<Hologram> holograms = new ArrayList<>();

    private OrbitMinesApi api;

    private Location location;
    private List<ArmorStand> armorStands;
    private List<String> lines;
    private Map<String, ArmorStand> displayNames;

    private boolean hideOnJoin;

    public Hologram(Location location) {
        this(location, false);
    }

    public Hologram(Location location, boolean hideOnJoin) {
        holograms.add(this);
        api = OrbitMinesApi.getApi();

        this.location = location;
        this.hideOnJoin = hideOnJoin;
        this.armorStands = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.displayNames = new HashMap<>();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean hideOnJoin() {
        return hideOnJoin;
    }

    public List<ArmorStand> getArmorStands() {
        return armorStands;
    }

    public List<String> getLines() {
        return lines;
    }

    public Map<String, ArmorStand> getDisplayNames() {
        return displayNames;
    }

    public void addLine(String line) {
        lines.add(line);
        displayNames.put(line, null);
    }

    public void setLine(int index, String line) {
        if (lines.size() < index)
            return;

        String fromLine = lines.get(index - 1);
        lines.set(index - 1, line);
        displayNames.remove(fromLine);
        displayNames.put(line, null);
    }

    public void updateLine(int index, String line) {
        if (lines.size() < index)
            return;

        String fromLine = lines.get(index - 1);
        lines.set(index - 1, line);

        ArmorStand armorStand = displayNames.get(fromLine);
        armorStand.setCustomName(line);
        armorStand.setCustomNameVisible(true);

        displayNames.remove(fromLine);
        displayNames.put(line, armorStand);
    }

    public void hideLines() {
        int index = 1;
        for (String line : lines) {
            hideLine(index);
            index++;
        }
    }

    public void hideLine(int index) {
        ArmorStand armorStand = displayNames.get(lines.get(index - 1));
        armorStand.setCustomName(null);
        armorStand.setCustomNameVisible(false);
    }

    public void removeLine(int index) {
        if (lines.size() < index)
            return;

        displayNames.remove(lines.get(index - 1));
        lines.remove(index - 1);
    }

    public void clearLines() {
        displayNames.clear();
        lines.clear();
    }

    public void create() {
        create((List<Player>) null);
    }

    public void create(Player... players) {
        create(Arrays.asList(players));
    }

    public void createHideFor(Player... players) {
        ArrayList<Player> createFor = new ArrayList<>();
        createFor.addAll(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            createFor.remove(player);
        }

        create(createFor);
    }

    /* null: all players */
    public void create(Collection<? extends Player> createFor) {
        if (armorStands.size() != 0) {
            delete();
            armorStands.clear();
        }

        if (location == null)
            return;

        Chunk chunk = location.getChunk();
        chunk.load();
        api.getChunks().add(chunk);

        int index = 0;
        for (String displayName : lines) {
            ArmorStand armorStand = api.getNms().armorStand().spawn(new Location(location.getWorld(), location.getX(), location.getY() - (index * 0.25), location.getZ()), false);
            armorStand.setCustomName(displayName);
            armorStand.setCustomNameVisible(true);
            armorStand.setGravity(false);

            displayNames.put(displayName, armorStand);
            armorStands.add(armorStand);

            index++;

            if (createFor == null)
                continue;

            List<Player> hideFor = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!createFor.contains(player))
                    hideFor.add(player);
            }

            hideFor(hideFor);
        }
    }

    public void clear() {
        for (ArmorStand as : armorStands) {
            as.remove();
        }
    }

    public void delete() {
        clear();
        holograms.remove(this);
    }

    public void hideFor(Player player) {
        for (ArmorStand armorStand : armorStands) {
            api.getNms().entity().destroyEntityFor(player, armorStand);
        }
    }

    public void hideFor(Collection<? extends Player> players) {
        for (ArmorStand armorStand : armorStands) {
            api.getNms().entity().destroyEntityFor(players, armorStand);
        }
    }

    public static Hologram getHologram(ArmorStand armorStand) {
        for (Hologram hologram : holograms) {
            if (hologram.getArmorStands().contains(armorStand))
                return hologram;
        }
        return null;
    }

    public static List<Hologram> getHolograms() {
        return holograms;
    }
}