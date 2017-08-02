package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.DisguiseData;
import com.orbitmines.api.spigot.perks.Disguise;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class DisguiseInventory extends PerkInventory {

    public DisguiseInventory() {
        super(54, "§0§lDisguises", 48);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        DisguiseData data = omp.disguises();

        int slot = 0;
        for (Disguise disguise : Disguise.values()) {
            add(slot, new ConfirmItemInstance(toItemBuilder(disguise, omp).build()) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addDisguise(disguise);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!disguise.hasAccess(omp)) {
                        if (disguise.obtainable().isPurchasable() && disguise.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, disguise);
                        } else {
                            disguise.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();
                        data.disguiseAsMob(disguise.mob());
                    }
                }
            });

            slot++;
        }

        if (data.isDisguised())
            add(50, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.getMessage(new Message("§4§nZet Disguise UIT", "§4§nDisable Disguise"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5, 1);
                    data.undisguise();
                }
            });
    }

    @Override
    protected OMInventory returnInventory() {
        return new DisguiseInventory();
    }
}