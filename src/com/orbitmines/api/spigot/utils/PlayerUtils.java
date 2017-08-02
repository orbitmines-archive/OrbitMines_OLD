package com.orbitmines.api.spigot.utils;

import com.orbitmines.api.spigot.handlers.ItemSet;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {

    public static GameMode getGameMode(String string) {
        if (string.equalsIgnoreCase("a") || string.equalsIgnoreCase("2") || string.equalsIgnoreCase("adventure"))
            return GameMode.ADVENTURE;
        else if (string.equalsIgnoreCase("c") || string.equalsIgnoreCase("1") || string.equalsIgnoreCase("creative"))
            return GameMode.CREATIVE;
        else if (string.equalsIgnoreCase("spec") || string.equalsIgnoreCase("3") || string.equalsIgnoreCase("spectate"))
            return GameMode.SPECTATOR;
        else if (string.equalsIgnoreCase("s") || string.equalsIgnoreCase("0") || string.equalsIgnoreCase("survival"))
            return GameMode.SURVIVAL;
        else
            return null;
    }

    public static int getEmptySlots(Inventory inventory) {
        int amount = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR)
                amount++;
        }
        return amount;
    }

    public static int getSlotsRequired(Material material, int amount) {
        int maxStackSize = material.getMaxStackSize();
        int leftOver = maxStackSize % amount;
        return leftOver == 0 ? amount / maxStackSize : (amount + (maxStackSize - leftOver)) / maxStackSize;
    }

    /* Inventory */
    public static boolean hasItems(Player player, ItemSet itemSet, int amount) {
        int iAmount = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == itemSet.getMaterial() && item.getDurability() == itemSet.getDurability())
                iAmount += item.getAmount();
        }

        return iAmount >= amount;
    }

    public static void removeItems(Player player, ItemSet itemSet, int amount) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == itemSet.getMaterial() && item.getDurability() == itemSet.getDurability()) {
                player.getInventory().remove(item);
                items.add(item);
            }
        }
        List<ItemStack> itemsToRemove = new ArrayList<>();
        List<ItemStack> itemsToAdd = new ArrayList<>();

        int iAmount = 0;
        for (ItemStack item : items) {
            if (iAmount != amount) {
                if (iAmount + item.getAmount() <= amount) {
                    itemsToRemove.add(item);

                    iAmount += item.getAmount();
                } else {
                    itemsToRemove.add(item);

                    ItemStack item3 = new ItemStack(item);
                    item3.setAmount(item.getAmount() - (amount - iAmount));
                    itemsToAdd.add(item3);

                    iAmount = amount;
                }
            }
        }

        for (ItemStack item : itemsToRemove) {
            items.remove(item);
        }
        for (ItemStack item : itemsToAdd) {
            items.add(item);
        }

        player.getInventory().addItem(items.toArray(new ItemStack[items.size()]));
    }

    public static int removeAll(Player player, ItemSet itemSet) {
        int amount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == itemSet.getMaterial() && item.getDurability() == itemSet.getDurability()) {
                player.getInventory().remove(item);
                amount += item.getAmount();
            }
        }
        return amount;
    }

    public static int getAmount(Player player, ItemSet itemSet) {
        int amount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == itemSet.getMaterial() && item.getDurability() == itemSet.getDurability())
                amount += item.getAmount();
        }
        return amount;
    }
}
