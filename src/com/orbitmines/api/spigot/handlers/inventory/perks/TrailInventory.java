package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.TrailData;
import com.orbitmines.api.spigot.perks.Trail;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class TrailInventory extends PerkInventory {

    public TrailInventory() {
        super(45, "§0§lTrails", 39);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        TrailData data = omp.trails();

        int slot = 9;
        for (Trail trail : Trail.values()) {
            add(slot, new ConfirmItemInstance((trail != Trail.CRIT ? toItemBuilder(trail, omp).build() : api.getNms().customItem().hideFlags(toItemBuilder(trail, omp).build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS))) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addTrail(trail);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!trail.hasAccess(omp)) {
                        if (trail.obtainable().isPurchasable() && trail.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, trail, this);
                        } else {
                            trail.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();
                        data.setTrail(trail);
                    }
                }
            });

            slot++;
        }

        add(40, new ItemInstance(new ItemBuilder(Material.REDSTONE_COMPARATOR, 1, 0, omp.getMessage(new Message("§f§nTrail Instellingen", "§f§nTrail Settings"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new TrailSettingsInventory().open(omp);
            }
        });

        if (data.hasTrailEnabled())
            add(41, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.getMessage(new Message("§4§nZet Trail UIT", "§4§nDisable Trail"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5, 1);
                    data.disableTrail();
                }
            });
    }

    @Override
    protected OMInventory returnInventory() {
        return new TrailInventory();
    }

    @Override
    protected boolean isDisabled() {
        return !api.isTrailEnabled();
    }
}
