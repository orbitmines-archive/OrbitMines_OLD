package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class CosmeticPerksInventory extends OMInventory {

    private OrbitMinesApi api;

    public CosmeticPerksInventory() {
        newInventory(36, "§0§lCosmetic Perks");

        api = OrbitMinesApi.getApi();
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, 5, 1);

        add(10, new ItemInstance(api.getNms().customItem().setEggId(new ItemBuilder(Material.MONSTER_EGG, 1, 0, "§7Pets" + (api.isPetEnabled() ? "" : " §8| " + omp.statusString(false))).build(), Mob.WOLF)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new PetInventory().open(omp);
            }
        });
        add(12, new ItemInstance(new ItemBuilder(Material.INK_SACK, 1, 1, "§4ChatColors").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new ChatColorInventory().open(omp);
            }
        });
        add(14, new ItemInstance(new ItemBuilder(Material.SKULL_ITEM, 1, 2, "§2Disguises" + (api.isDisguiseEnabled() ? "" : " §8| " + omp.statusString(false))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new DisguiseInventory().open(omp);
            }
        });
        add(16, new ItemInstance(new ItemBuilder(Material.COMPASS, 1, 0, "§bGadgets" + (api.isGadgetEnabled() ? "" : " §8| " + omp.statusString(false))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new GadgetInventory().open(omp);
            }
        });
        add(19, new ItemInstance(api.getNms().customItem().hideFlags(new LeatherArmorBuilder(LeatherArmorBuilder.Type.CHESTPLATE, Color.fromBGR(204, 100, 2), 1, "§1Wardrobe" + (api.isWardrobeEnabled() ? "" : " §8| " + omp.statusString(false))).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new WardrobeInventory().open(omp);
            }
        });
        add(21, new ItemInstance(new ItemBuilder(Material.STRING, 1, 0, "§fTrails" + (api.isTrailEnabled() ? "" : " §8| " + omp.statusString(false))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new TrailInventory().open(omp);
            }
        });
        add(23, new ItemInstance(new ItemBuilder(Material.PUMPKIN, 1, 0, "§6Hats" + (api.isHatEnabled() ? "" : " §8| " + omp.statusString(false))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new HatInventory().open(omp);
            }
        });
        add(25, new ItemInstance(new ItemBuilder(Material.FIREWORK, 1, 0, "§cFireworks" + (api.isGadgetEnabled() ? "" : " §8| " + omp.statusString(false))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new FireworkInventory().open(omp);
            }
        });

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }
}
