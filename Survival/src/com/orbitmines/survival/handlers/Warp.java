package com.orbitmines.survival.handlers;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.ConfigHandler;
import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.utils.Serializer;
import com.orbitmines.api.utils.uuid.UUIDUtils;
import com.orbitmines.survival.Survival;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Warp implements Teleportable {

    private static List<Warp> warps = new ArrayList<>();

    private int id;
    private UUID uuid;
    private String name;
    private Location location;
    private boolean enabled;
    private ItemStack item;

    public Warp(int id, UUID uuid, String name, Location location, boolean enabled, Material material, short durability) {
        warps.add(this);

        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.location = location;
        this.enabled = enabled;

        updateItemStack(material, durability);
    }

    @Override
    public void teleportInstantly(SurvivalPlayer omp) {
        omp.setBackLocation(omp.getPlayer().getLocation());
        omp.getPlayer().teleport(this.location);
    }

    @Override
    public Color getColor() {
        return Color.TEAL;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getUUIDName() {
        Player p = Bukkit.getPlayer(this.uuid);

        return p != null ? p.getName() : UUIDUtils.getName(this.uuid);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ItemStack getItemStack() {
        return item;
    }

    public void updateItemStack(Material material, int durability) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§7§lWarp: §3§l" + name);
        List<String> lore = new ArrayList<>();
        lore.add(" §7Owner: §3" + getUUIDName());
        lore.add(" §7XZ: §3" + location.getBlockX() + "§7 / §3" + location.getBlockZ());
        meta.setLore(lore);
        item.setItemMeta(meta);
        item.setDurability((short) durability);

        this.item = item;
    }

    public void teleport(SurvivalPlayer omp) {
        omp.resetCooldown(Cooldown.TELEPORTING);
        omp.setTeleportingTo(this);

        omp.getPlayer().sendMessage("§7" + omp.getMessage(new Message("Teleporten naar", "Teleporting to")) + " §3" + this.name + "§7...");
    }

    public static void setup() {
        for (String warpString : Survival.getInstance().getConfigHandler().get("warps").getStringList("warps")) {
            String[] warpParts = warpString.split(";");

            warps.add(new Warp(Integer.parseInt(warpParts[0]), UUID.fromString(warpParts[6]), warpParts[1], Serializer.parseLocation(warpParts[2]), Boolean.parseBoolean(warpParts[5]), Material.valueOf(warpParts[3]), Short.parseShort(warpParts[4])));
        }
    }

    public static void saveToConfig() {
        List<String> list = new ArrayList<>();

        for (Warp warp : warps) {
            list.add(warp.getId() + ";" + warp.getName() + ";" + Serializer.serialize(warp.getLocation()) + ";" + warp.getItemStack().getType().toString() + ";" + warp.getItemStack().getDurability() + ";" + warp.isEnabled() + ";" + warp.getUUID().toString());
        }

        ConfigHandler configHandler = Survival.getInstance().getConfigHandler();
        configHandler.get("warps").set("warps", list);
        configHandler.save("warps");
    }

    public static List<Warp> getWarps(Player player) {
        List<Warp> warps = new ArrayList<>();

        for (Warp warp : warps) {
            if (warp.getUUID().toString().equals(player.getUniqueId().toString()))
                warps.add(warp);
        }
        return warps;
    }

    public static Warp getWarp(int id) {
        for (Warp warp : warps) {
            if (warp.getId() == id)
                return warp;
        }
        return null;
    }

    public static Warp getWarp(String name) {
        for (Warp warp : warps) {
            if (warp.getName().equalsIgnoreCase(name))
                return warp;
        }
        return null;
    }

    public static List<Warp> getWarps() {
        return warps;
    }
}
