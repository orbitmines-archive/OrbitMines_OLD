package com.orbitmines.survival.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.Warp;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class WarpItemEditorInventory extends OMInventory {

    private final ItemSet[] sets = {
            new ItemSet(Material.SAPLING, 1, 2, "§fBirch Sapling"),
            new ItemSet(Material.SAND, 1, 0, "§eSand"),
            new ItemSet(Material.RED_ROSE, 1, 0, "§cRed Rose"),
            new ItemSet(Material.SAPLING, 1, 0, "§aOak Sapling"),
            new ItemSet(Material.PACKED_ICE, 1, 0, "§bPacked Ice"),
            new ItemSet(Material.ICE, 1, 0, "§bIce"),
            new ItemSet(Material.SAPLING, 1, 3, "§2Jungle Sapling"),
            new ItemSet(Material.SAPLING, 1, 1, "§2Spruce Sapling"),
            new ItemSet(Material.STAINED_CLAY, 1, 4, "§6Yellow Stained Clay"),
            new ItemSet(Material.HUGE_MUSHROOM_2, 1, 2, "§cMushroom Block"),
            new ItemSet(Material.WATER_BUCKET, 1, 0, "§9Water Bucket"),
            new ItemSet(Material.GRASS, 1, 0, "§aGrass Block"),
            new ItemSet(Material.SAPLING, 1, 5, "§2Dark Oak Sapling"),
            new ItemSet(Material.SAPLING, 1, 4, "§eAcacia Sapling"),
            new ItemSet(Material.SNOW_BLOCK, 1, 0, "§fSnow Block"),
            new ItemSet(Material.STONE, 1, 0, "§7Stone"),
            new ItemSet(Material.DOUBLE_PLANT, 1, 0, "§eSun Flower"),
            new ItemSet(Material.VINE, 1, 0, "§2Vine"),
    };

    private Warp warp;

    public WarpItemEditorInventory(Warp warp) {
        this.warp = warp;
        newInventory(27, "§0§lWarp Item: " + (warp == null ? "" : warp.getName()));
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        int slot = 0;
        for (ItemSet set : sets) {
            add(slot, getItem(omp, set));
            slot++;
        }

        add(22, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.getMessage(new Message("§c§lAnnuleer", "§c§lCancel"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new WarpEditorInventory(warp).open(omp);
            }
        });

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private ItemInstance getItem(SurvivalPlayer omp, ItemSet set){
        ItemBuilder builder = new ItemBuilder(set.getMaterial(), 1, set.getDurability(), "§7" + omp.getMessage(new Message("Verander in", "Change to")) + " " + set.getDisplayName());
        if (warp.getItemStack().getType() == set.getMaterial())
            builder.glow();

        return new ItemInstance(builder.build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                warp.updateItemStack(set.getMaterial(), set.getDurability());
                Warp.saveToConfig();

                omp.getPlayer().closeInventory();
                omp.sendMessage(new Message("§7Warp item verandert in " + set.getDisplayName() + "§7.", "§7Changed the warp item to " + set.getDisplayName() + "§7."));
            }
        };
    }
}
