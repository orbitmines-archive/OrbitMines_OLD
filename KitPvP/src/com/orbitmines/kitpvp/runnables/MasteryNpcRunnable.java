package com.orbitmines.kitpvp.runnables;

import com.orbitmines.api.spigot.handlers.npc.FloatingItem;
import com.orbitmines.api.spigot.runnable.OMRunnable;
import com.orbitmines.kitpvp.KitPvP;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class MasteryNpcRunnable extends OMRunnable {

    private KitPvP kitPvP;

    public MasteryNpcRunnable() {
        super(TimeUnit.SECOND, 5);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void run() {
        FloatingItem npc = kitPvP.getMasteryNpc();
        FloatingItem.ItemInstance itemInstance = npc.getItemInstances().get(0);

        if (itemInstance.getItem() == null)
            return;

        ItemStack item = itemInstance.getItem().getItemStack();
        if (item.getType() == Material.DIAMOND_SWORD)
            item.setType(Material.IRON_CHESTPLATE);
        else if (item.getType() == Material.IRON_CHESTPLATE)
            item.setType(Material.BOW);
        else if (item.getType() == Material.BOW)
            item.setType(Material.ARROW);
        else if (item.getType() == Material.ARROW)
            item.setType(Material.DIAMOND_SWORD);

        itemInstance.getItem().setItemStack(item);
    }
}
