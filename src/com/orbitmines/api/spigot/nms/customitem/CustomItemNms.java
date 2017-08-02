package com.orbitmines.api.spigot.nms.customitem;

import com.orbitmines.api.spigot.Mob;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface CustomItemNms {

    public ItemStack hideFlags(ItemStack item, int... hideFlags);

    public ItemStack setUnbreakable(ItemStack item);

    public ItemStack setEggId(ItemStack item, Mob mob);

}
