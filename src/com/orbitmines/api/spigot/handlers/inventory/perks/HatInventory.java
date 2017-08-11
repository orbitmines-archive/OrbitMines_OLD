package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.HatData;
import com.orbitmines.api.spigot.perks.Hat;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class HatInventory extends PerkInventory {

    private int HATS_PER_PAGE = 36;
    private int NEW_PER_PAGE = HATS_PER_PAGE / 9;

    public HatInventory() {
        super(54, "§0§lHats", 48);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        HatData data = omp.hats();

        int slot = 0;
        for (Hat hat : getHatsForPage(data.getPage())) {
            if (hat == null) {
                inventory.setItem(slot, null);
                slot++;
                continue;
            }

            add(slot, new ConfirmItemInstance(toItemBuilder(hat, omp).build()) {

                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addHat(hat);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!hat.hasAccess(omp)) {
                        if (hat.obtainable().isPurchasable() && hat.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, hat, this);
                        } else {
                            hat.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();
                        data.setHat(hat);
                    }
                }
            });

            slot++;
        }

        add(49, new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, data.hasHatsBlockTrail() ? 5 : 14, "Hat Block Trail: " + omp.statusString(data.hasHatsBlockTrail()),
                "", omp.isEligible(data.getBlockTrailObtainable().getVipRank()) ? "§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) : data.getBlockTrailObtainable().getRequiredLore(omp), "").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                if (omp.isEligible(data.getBlockTrailObtainable().getVipRank())) {
                    omp.getPlayer().closeInventory();
                    data.setHatsBlockTrail(!data.hasHatsBlockTrail());
                } else {
                    data.getBlockTrailObtainable().msgNoAccess(omp);
                }
            }
        });

        if (data.getPage() != 0)
            add(36, new ItemInstance(new ItemBuilder(Material.EMPTY_MAP, 1, 0, "§e§n<< " + omp.getMessage(new Message("Meer Hats", "More Hats"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    data.setPage(data.getPage() -1);
                    clearInstances();
                    open(omp);
                }
            });
        else
            inventory.setItem(36, null);

        if (canHaveMorePages(data.getPage()))
            add(44, new ItemInstance(new ItemBuilder(Material.EMPTY_MAP, 1, 0, "§e§n" + omp.getMessage(new Message("Meer Hats", "More Hats")) + " >>").build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    data.setPage(data.getPage() +1);
                    clearInstances();
                    open(omp);
                }
            });
        else
            inventory.setItem(44, null);

        if (data.hasHatEnabled())
            add(50, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.getMessage(new Message("§4§nZet Hat UIT", "§4§nDisable Hat"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5, 1);
                    data.disableHat();
                }
            });
    }

    @Override
    protected OMInventory returnInventory() {
        return new HatInventory();
    }

    @Override
    protected boolean isDisabled() {
        return !api.isHatEnabled();
    }

    private Hat[] getHatsForPage(int pageNumber) {
        Hat[] hats = Hat.values();
        Hat[] pageHats = new Hat[HATS_PER_PAGE];

        for (int i = 0; i < HATS_PER_PAGE; i++) {
            if (hats.length > i)
                pageHats[i] = hats[i];
        }

        if (pageNumber != 0) {
            for (int i = 0; i < pageNumber; i++) {
                for (int j = 0; j < HATS_PER_PAGE; j++) {
                    int check = -1;
                    if ((j + 1) % 9 == 0)
                        check = (j + 1) / 9;

                    if (check != -1) {
                        int next = HATS_PER_PAGE + check + (NEW_PER_PAGE * i);
                        if (hats.length > next)
                            pageHats[j] = hats[next];
                        else
                            pageHats[j] = null;
                    } else {
                        pageHats[j] = pageHats[j + 1];
                    }
                }
            }
        }

        return pageHats;
    }

    private boolean canHaveMorePages(int pageNumber) {
        int hatAmount = Hat.values().length;

        if (hatAmount <= HATS_PER_PAGE)
            return false;

        int maxPage = hatAmount % NEW_PER_PAGE;
        if (maxPage != 0)
            maxPage = (hatAmount - HATS_PER_PAGE + (NEW_PER_PAGE - maxPage)) / NEW_PER_PAGE;
        else
            maxPage = (hatAmount - HATS_PER_PAGE) / NEW_PER_PAGE;

        return maxPage > pageNumber;
    }
}
