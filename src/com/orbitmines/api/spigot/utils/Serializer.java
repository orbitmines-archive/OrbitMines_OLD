package com.orbitmines.api.spigot.utils;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.handlers.firework.FireworkSettings;
import com.orbitmines.api.spigot.handlers.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public class Serializer {

    /* Character used to serialize PlayerData: '~', '-', '<', '>', '=' DO NOT USE */

    /* In order to serialize use the following order: '|', ';', ':', '/' */

    /* Location */
    public static String serialize(Location location) {
        if (location == null)
            return null;
        return location.getWorld().getName() + "|" + location.getX() + "|" + location.getY() + "|" + location.getZ() + "|" + location.getYaw() + "|" + location.getPitch();
    }

    public static Location parseLocation(String string) {
        if (string == null)
            return null;

        String[] data = string.split("\\|");
        return new Location(Bukkit.getWorld(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
    }

    /* FireworkSettings */
    public static String serialize(FireworkSettings settings) {
        return settings.getColor1().toString() + "|" + settings.getColor2().toString() + "|" + settings.getFade1().toString() + "|" + settings.getFade2().toString() + "|" + settings.hasFlicker() + "|" + settings.hasTrail() + "|" + settings.getType().toString();
    }

    public static FireworkSettings parseFireworkSettings(String string) {
        String[] data = string.split("|");
        return new FireworkSettings(Color.valueOf(data[0]), Color.valueOf(data[1]), Color.valueOf(data[2]), Color.valueOf(data[3]), Boolean.parseBoolean(data[4]), Boolean.parseBoolean(data[5]), FireworkEffect.Type.valueOf(data[6]));
    }

    /* Kit */
    public static String serialize(Kit kit, Material... bannedMaterials) {
        return kit.getName() + "|" + kit.getItemOffHand() + "|" + serialize(kit.getArmorContents(), bannedMaterials) + "|" + serialize(kit.getContents(), bannedMaterials);
    }

    public static Kit parseKit(String string) {
        String[] data = string.split("\\|");
        Kit kit = new Kit(data[0]);
        kit.setItemOffHand(parseItemStack(data[1]));
        kit.setArmorContents(parseItemStackArray(data[2]));
        kit.setContents(parseItemStackArray(data[3]));

        return kit;
    }

    /* ItemStack[] */
    public static String serialize(ItemStack[] contents, Material... bannedMaterials) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ItemStack item : contents) {
            if (bannedMaterials != null) {
                for (Material bannedMaterial : bannedMaterials) {
                    if (item.getType() == bannedMaterial) {
                        stringBuilder.append("null");
                        stringBuilder.append(";");
                        continue;
                    }
                }
            }

            stringBuilder.append(serialize(item));
            stringBuilder.append(";");
        }

        return stringBuilder.toString().substring(0, stringBuilder.length() -1);
    }

    public static ItemStack[] parseItemStackArray(String string) {
        String[] data = string.split(";");
        ItemStack[] items = new ItemStack[data.length];

        int index = 0;
        for (String itemString : data) {
            items[index] = parseItemStack(itemString);
            index++;
        }

        return items;
    }

    /* ItemStack */
    public static String serialize(ItemStack item) {
        // TYPE|AMOUNT|DURABILITY|Enchantments;ENCHANTMENT(LEVEL);ENCHANTMENT(LEVEL)|DISPLAYNAME|LORE;LORE
        if (item == null)
            return null;

        String enchantmentsString = "Enchantments";
        Map<Enchantment, Integer> enchantments = item.getEnchantments();
        for (Enchantment ench : enchantments.keySet()) {
            if (enchantments.get(ench) > 0) {
                enchantmentsString = enchantmentsString + ";" + ench.getName() + "(" + enchantments.get(ench) + ")";
            }
        }

        String itemLoreString = "null";
        if (item.getItemMeta().getLore() != null) {
            for (String line : item.getItemMeta().getLore()) {
                if (itemLoreString.equals("null")) {
                    itemLoreString = line;
                } else {
                    itemLoreString += ";" + line;
                }
            }
        }

        return item.getType().toString() + "|" + item.getAmount() + "|" + item.getDurability() + "|" + enchantmentsString + "|" + item.getItemMeta().getDisplayName() + "|" + itemLoreString;
    }

    public static ItemStack parseItemStack(String string) {
        if (string == null || string.equals("null"))
            return null;

        String[] data = string.split("|");

        ItemStack item = new ItemStack(Material.valueOf(data[0]), Integer.parseInt(data[1]));
        item.setDurability(Short.parseShort(data[2]));

        if (data[3].contains(";")) {
            String[] enchData = data[3].split(";");
            for (String ench : enchData) {
                if (!ench.equals("Enchantments")) {
                    String ench2 = ench.replace("(", "|").replace(")", "");
                    String[] enchdata2 = ench2.split("\\|");

                    item.addUnsafeEnchantment(Enchantment.getByName(enchdata2[0]), Integer.parseInt(enchdata2[1]));
                }
            }
        }

        ItemMeta meta = item.getItemMeta();
        if (!data[4].equals("null")) {
            meta.setDisplayName(data[4]);
        }
        if (!data[5].equals("null")) {
            meta.setLore(Arrays.asList(data[5].split(";")));
        }
        item.setItemMeta(meta);

        return item;
    }
}
