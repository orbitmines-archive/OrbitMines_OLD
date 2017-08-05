package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.api.spigot.handlers.playerdata.WardrobeData;
import com.orbitmines.api.spigot.perks.Wardrobe;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public class WardrobeInventory extends PerkInventory {

    public WardrobeInventory() {
        super(54, "§0§lWardrobe", 48);
    }

    @Override
    protected void setPerkItems(OMPlayer omp) {
        WardrobeData data = omp.wardrobe();

        int slot = 9;
        for (Wardrobe wardrobe : Wardrobe.values()) {
            ItemBuilder itemBuilder = wardrobe.item().getMaterial() == Material.LEATHER_CHESTPLATE ? toLeatherArmorBuilder(wardrobe, omp, LeatherArmorBuilder.Type.CHESTPLATE, wardrobe.color().getBukkitColor()) : toItemBuilder(wardrobe, omp);

            add(slot, new ConfirmItemInstance(api.getNms().customItem().hideFlags(itemBuilder.build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
                @Override
                protected void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                    data.addWardrobe(wardrobe);
                }

                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!wardrobe.hasAccess(omp)) {
                        if (wardrobe.obtainable().isPurchasable() && wardrobe.obtainable().canPurchase(omp)) {
                            confirmPurchase(omp, wardrobe);
                        } else {
                            wardrobe.obtainable().msgNoAccess(omp);
                        }
                    } else {
                        omp.getPlayer().closeInventory();
                        data.wardrobe(wardrobe);
                    }
                }
            });

            slot++;
        }

        add(31, new ItemInstance(new ItemBuilder(Material.LEATHER_CHESTPLATE).build()) {

            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                if (omp.isEligible(data.getWardrobeDiscoObtainable().getVipRank())) {
                    if (data.getDiscoWardrobe().size() > 1) {
                        omp.getPlayer().closeInventory();
                        data.setWardrobeDisco(true);
                    } else {
                        omp.sendMessage(new Message("§7Je moet minimaal twee items hebben van de Wardrobe!", "§7You need at least two Wardrobe items!"));
                    }
                } else {
                    data.getWardrobeDiscoObtainable().msgNoAccess(omp);
                }
            }
        });
        discoItem(omp, this);

        if (data.hasWardrobeEnabled())
            add(50, new ItemInstance(new ItemBuilder(Material.BARRIER, 1, 0, omp.getMessage(new Message("§4§nZet Wardrobe UIT", "§4§nDisable Wardrobe"))).build()) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 5, 1);
                    data.disableWardrobe();
                }
            });
    }

    @Override
    protected OMInventory returnInventory() {
        return new WardrobeInventory();
    }

    @Override
    protected boolean isDisabled() {
        return !api.isWardrobeEnabled();
    }

    public static void discoItem(OMPlayer omp, WardrobeInventory inventory) {
        WardrobeData data = omp.wardrobe();
        Wardrobe wardrobe = Wardrobe.random(Arrays.asList(Wardrobe.COLORS));

        inventory.getInventory().setItem(31, new LeatherArmorBuilder(LeatherArmorBuilder.Type.CHESTPLATE, wardrobe.color().getBukkitColor(), 1, wardrobe.color() + "Disco Armor", "", omp.isEligible(data.getWardrobeDiscoObtainable().getVipRank()) ? "§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) : data.getWardrobeDiscoObtainable().getRequiredLore(omp), "").build());
    }
}
