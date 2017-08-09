package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.GadgetData;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class GadgetInventory extends PerkInventory {

    public GadgetInventory() {
        super(27, "§0§lGadgets", 22);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        GadgetData data = omp.gadgets();

        int slot = 0;
        for (Gadget gadget : Gadget.values()) {
            add(slot, new ConfirmItemInstance(toItemBuilder(gadget, omp).build()) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addGadget(gadget);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!gadget.hasAccess(omp)) {
                        if (gadget.obtainable().isPurchasable() && gadget.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, gadget);
                        } else {
                            gadget.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();
                        data.enableGadget(gadget);
                    }
                }
            });

            slot++;
        }

        if (data.hasGadgetEnabled())
            add(23, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.getMessage(new Message("§4§nZet Gadget UIT", "§4§nDisable Gadget"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5, 1);
                    data.disableGadget();
                }
            });
    }

    @Override
    protected OMInventory returnInventory() {
        return new GadgetInventory();
    }

    @Override
    protected boolean isDisabled() {
        return !api.isGadgetEnabled();
    }
}
