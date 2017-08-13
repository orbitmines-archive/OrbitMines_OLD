package com.orbitmines.survival.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.nms.customitem.CustomItemNms;
import com.orbitmines.api.spigot.utils.ItemUtils;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import com.orbitmines.survival.handlers.region.Region;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class RegionInventory extends OMInventory {

//    private final int MAX_X = (Region.REGION_SIZE - 1) / 2 - 5;
//    private final int MAX_Y = (Region.REGION_SIZE - 1) / 2 - 3;

    private int x;
    private int y;

    public RegionInventory(int x, int y) {
        this.x = x;
        this.y = y;

        newInventory(45, "§0§lRegion Teleporter");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        for (Region region : Region.getRegions()) {
            int x = region.getInventoryX();
            int y = region.getInventoryY();

            if (x > this.x - 5 && x < this.x + 5 && y > this.y - 5 && y < this.y + 3) {
                add(omp, region, x - this.x, y - this.y);
            }
        }

        CustomItemNms nms = OrbitMinesApi.getApi().getNms().customItem();

        add(0, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                 new RegionInventory(x - 1, y + 1).open(omp);
            }
        });

        add(4, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                 new RegionInventory(x, y + 1).open(omp);
            }
        });

        add(8, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                 new RegionInventory(x + 1, y + 1).open(omp);
            }
        });

        add(18, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                 new RegionInventory(x - 1, y).open(omp);
            }
        });

        add(26, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                 new RegionInventory(x + 1, y).open(omp);
            }
        });

        add(36, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                 new RegionInventory(x - 1, y - 1).open(omp);
            }
        });

        add(40, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new RegionInventory(x, y - 1).open(omp);
            }
        });

        add(44, new ItemInstance(nms.hideFlags(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6Meer", "§6More"))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                 new RegionInventory(x + 1, y - 1).open(omp);
            }
        });

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private void add(OMPlayer omp, Region region, int x, int y) {
        int slot = 22 + x - (9 * y);

        if (slot < 0 || slot > 44)
            return;

        if (slot == 0 || slot == 4 || slot == 8 || slot == 18 || slot == 26 || slot == 36 || slot == 40 || slot == 44)
            return;

        if (region.getId() < Region.TELEPORTABLE)
            add(slot, new ItemInstance(region.getItemStack()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    region.teleport((SurvivalPlayer) omp);
                }
            });
        else
            add(slot, new EmptyItemInstance(new ItemBuilder(Material.EMPTY_MAP, 1, 0, omp.getMessage(new Message("§eNog niet open.", "§eHasn't been opened yet."))).build()));
    }
}
