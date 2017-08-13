package com.orbitmines.survival.handlers.inventory;

import com.orbitmines.api.Message;
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
public class WarpEditorInventory extends OMInventory {

    private Warp warp;

    public WarpEditorInventory(Warp warp) {
        this.warp = warp;

        newInventory(9, "§0§lEdit Warp: " + (warp == null ? "" : warp.getName()));
    }

    @Override
    protected boolean onOpen(OMPlayer player) {
        SurvivalPlayer omp = (SurvivalPlayer) player;

        add(1, new ItemInstance(new ItemBuilder(Material.NAME_TAG, 1, 0, omp.getMessage(new Message("§e§lHernoem Warp", "§e§lRename Warp"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer player) {
                new WarpRenameGui(omp, warp).open();
            }
        });
        add(3, new ItemInstance(new ItemBuilder(warp.getItemStack().getType(), 1, warp.getItemStack().getDurability(), omp.getMessage(new Message("§7§lVerander Item", "§7§lChange Item"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer player) {
                new WarpItemEditorInventory(warp).open(omp);
            }
        });
        add(3, new ItemInstance(new ItemBuilder(Material.MAP, 1, 0, omp.getMessage(new Message("§6§lZet Locatie", "§6§lSet Location"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer player) {
                warp.setLocation(omp.getPlayer().getLocation());
                Warp.saveToConfig();

                omp.getPlayer().closeInventory();
                omp.sendMessage(new Message("§7De locatie van de warp '§6" + warp.getName() + "§7' veranderd!", "§7Changed the location of warp '§6" + warp.getName() + "§7' to your position!"));
            }
        });
        add(7, new ItemInstance(new ItemBuilder(warp.isEnabled() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL, 1, 0, "§7§lWarp " + omp.statusString(warp.isEnabled())).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer player) {
                warp.setEnabled(!warp.isEnabled());
                Warp.saveToConfig();
                new WarpEditorInventory(warp).open(omp);
            }
        });

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }
}
