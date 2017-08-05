package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.inventory.ConfirmInventory;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.TrailData;
import com.orbitmines.api.spigot.perks.TrailType;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class TrailSettingsInventory extends PerkInventory {

    public TrailSettingsInventory() {
        super(36, "§0§lTrail Settings", 31);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        TrailData data = omp.trails();

        int slot = 9;
        for (TrailType trailType : TrailType.values()) {
            add(slot, new ConfirmItemInstance(toItemBuilder(trailType, omp).build()) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addTrailType(trailType);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!trailType.hasAccess(omp)) {
                        if (trailType.obtainable().isPurchasable() && trailType.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, trailType);
                        } else {
                            trailType.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();
                        returnInventory().open(omp);
                    }
                }
            });

            slot++;
        }

        add(4, new EmptyItemInstance(new ItemBuilder(Material.COMPASS, 1, 0, "§7Trail Types").build()));

        add(19, new ConfirmItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, data.isSpecial() ? 5 : 14, "§7§lSpecial Trail: " + omp.statusString(data.isSpecial()),
                "", data.hasUnlockedSpecialTrail() ? "§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) : data.getSpecialTrailObtainable().getRequiredLore(omp), "").build()) {

            @Override
            protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                data.setUnlockedSpecialTrail(true);
            }

            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                Obtainable obtainable = data.getSpecialTrailObtainable();

                if (!data.hasUnlockedSpecialTrail()) {
                    if (obtainable.isPurchasable() && obtainable.canPurchase(omp)) {
                        new ConfirmInventory("§7§lSpecial Trail", new ItemSet(Material.STRING), obtainable) {
                            @Override
                            public void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                                obtainable.purchase(omp);
                                ((ConfirmItemInstance) itemInstances[event.getSlot()]).onConfirm(event, omp);
                                returnInventory().open(omp);
                            }

                            @Override
                            public void onCancel(InventoryClickEvent event, OMPlayer omp) {
                                returnInventory().open(omp);
                            }
                        }.open(omp);
                    } else {
                        obtainable.msgNoAccess(omp);
                    }
                } else {
                    omp.getPlayer().closeInventory();
                    returnInventory().open(omp);
                }
            }
        });

        add(35, new ItemInstance(new ItemBuilder(Material.NETHER_STAR, data.getParticleAmount(), 0, "§7§lParticle Amount: §f§l" + data.getParticleAmount(),
                "", omp.isEligible(data.getParticleAmountObtainable().getVipRank()) ? "§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) : data.getParticleAmountObtainable().getRequiredLore(omp), "").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                if (omp.isEligible(data.getParticleAmountObtainable().getVipRank())) {
                    data.addParticleAmount();

                    ItemStack item = event.getCurrentItem();
                    item.setAmount(data.getParticleAmount());
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("§7§lParticle Amount: §f§l" + data.getParticleAmount());
                    item.setItemMeta(meta);
                } else {
                    data.getParticleAmountObtainable().msgNoAccess(omp);
                }
            }
        });
    }

    @Override
    protected OMInventory returnInventory() {
        return new TrailSettingsInventory();
    }

    @Override
    protected boolean isDisabled() {
        return !api.isTrailEnabled();
    }
}
