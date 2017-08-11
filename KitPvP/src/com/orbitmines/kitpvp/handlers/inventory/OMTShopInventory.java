package com.orbitmines.kitpvp.handlers.inventory;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.inventory.ConfirmInventory;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class OMTShopInventory extends OMInventory {

    public OMTShopInventory() {
        newInventory(27, "§6§lOMT Shop");
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {
        coins(omp, 200, 1, 2, new Obtainable(OrbitMinesApi.TOKENS, 1));
        coins(omp, 1000, 4, 10, new Obtainable(OrbitMinesApi.TOKENS, 4));
        coins(omp, 2500, 9, 25, new Obtainable(OrbitMinesApi.TOKENS, 9));
        coins(omp, 5000, 16, 50, new Obtainable(OrbitMinesApi.TOKENS, 16));

        return true;
    }

    private void coins(OMPlayer omp, int coins, int itemAmount, int slot, Obtainable obtainable) {
        add(slot, new ItemInstance(new ItemBuilder(KitPvP.COINS.item().getMaterial(), itemAmount, KitPvP.COINS.item().getDurability(), "§6§l+" + coins,
                obtainable.getRequiredLore(omp)).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                if (obtainable.isPurchasable() && obtainable.canPurchase(omp)) {
                    new ConfirmInventory("§6§l" + coins, new ItemSet(KitPvP.COINS.item().getMaterial(), itemAmount, KitPvP.COINS.item().getDurability()), obtainable) {
                        @Override
                        public void onConfirm(InventoryClickEvent event, OMPlayer player) {
                            KitPvPPlayer omp = (KitPvPPlayer) player;
                            KitPvPData data = omp.kitPvP();

                            obtainable.purchase(omp);
                            omp.getPlayer().closeInventory();

                            data.addCoins(coins);

                            new OMTShopInventory().open(omp);
                        }

                        @Override
                        public void onCancel(InventoryClickEvent event, OMPlayer omp) {
                            new OMTShopInventory().open(omp);
                        }
                    }.open(omp);
                } else {
                    obtainable.msgNoAccess(omp);
                }
            }
        });
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }
}
