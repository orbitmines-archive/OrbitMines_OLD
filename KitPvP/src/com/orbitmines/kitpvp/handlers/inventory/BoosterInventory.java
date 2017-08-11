package com.orbitmines.kitpvp.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.ConfirmInventory;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.ActiveBooster;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class BoosterInventory extends OMInventory {

    private KitPvP kitPvP;

    public BoosterInventory() {
        newInventory(9, "§0§lBoosters");

        kitPvP = KitPvP.getInstance();
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        int slot = 1;
        for (ActiveBooster.Type type : ActiveBooster.Type.values()) {
            add(slot, new ItemInstance(new ItemBuilder(type.item().getMaterial(), type.item().getAmount(), type.item().getDurability(), type.getDisplayName(),
                    "", "§7Multiplier: §ax" + type.getMultiplier(), omp.getMessage(new Message("§7Lengte: §a30 Minuten", "§7Duration: §a30 Minutes")), "", type.obtainable().getRequiredLore(omp), "").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!omp.isEligible(type.getVipRank())) {
                        omp.msgRequiredVipRank(type.getVipRank());
                    } else if (type.obtainable().isPurchasable() && type.obtainable().canPurchase(omp)) {
                        if (kitPvP.getBooster() == null) {
                            new ConfirmInventory(type.getDisplayName(), type.item(), type.obtainable()) {
                                @Override
                                public void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                                    type.obtainable().purchase(omp);
                                    omp.getPlayer().closeInventory();

                                    kitPvP.setBooster(new ActiveBooster(type, omp.getPlayer().getName(), 30, 0));
                                    omp.broadcastMessage(new Message(omp.getName() + " §7heeft een §aBooster§7 geactiveerd! (§ax" + type.getMultiplier() + "§7)", omp.getName() + " §7activated a §aBooster§7! (§ax" + type.getMultiplier() + "§7)"));
                                }

                                @Override
                                public void onCancel(InventoryClickEvent event, OMPlayer omp) {
                                    open(omp);
                                }
                            }.open(omp);
                        } else {
                            omp.sendMessage(new Message("§a" + kitPvP.getBooster().getPlayer() + "'s Booster§7 is op het moment actief.", "§a" + kitPvP.getBooster().getPlayer() + "'s Booster§7 is currently active."));
                            omp.getPlayer().playSound(omp.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_SCREAM, 5, 1);
                        }
                    } else {
                        type.obtainable().msgNoAccess(omp);
                    }
                }
            });

            slot += 2;
        }

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }
}
