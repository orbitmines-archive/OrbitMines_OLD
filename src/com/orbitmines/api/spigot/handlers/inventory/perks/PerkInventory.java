package com.orbitmines.api.spigot.handlers.inventory.perks;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.ConfirmInventory;
import com.orbitmines.api.spigot.handlers.inventory.OMInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.handlers.itembuilders.LeatherArmorBuilder;
import com.orbitmines.api.spigot.perks.Perk;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class PerkInventory extends OMInventory {

    protected OrbitMinesApi api;
    private int cosmeticPerksSlot;
    private ItemInstance cosmeticPerks;

    public PerkInventory(int size, String title) {
        this(size, title, -1);
    }

    public PerkInventory(int size, String title, int cosmeticPerksSlot) {
        api = OrbitMinesApi.getApi();

        this.cosmeticPerksSlot = cosmeticPerksSlot;

        newInventory(size, title);

        if (cosmeticPerksSlot == -1)
            return;

        cosmeticPerks = new ItemInstance(new ItemBuilder(Material.ENDER_CHEST, 1, 0, "§9§nCosmetic Perks").build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                new CosmeticPerksInventory().open(omp);
            }
        };
    }

    protected abstract void setPerkItems(OMPlayer omp);

    protected abstract boolean isDisabled();

    protected abstract OMInventory returnInventory();

    @Override
    protected boolean onOpen(OMPlayer omp) {
        if (isDisabled()) {
            omp.sendMessage(new Message("§7Dat kan niet op deze server!", "§7You cannot do that on this server!"));
            return false;
        }

        if (cosmeticPerks != null)
            add(cosmeticPerksSlot, cosmeticPerks);

        setPerkItems(omp);

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    public void confirmPurchase(OMPlayer omp, Perk perk, ConfirmItemInstance itemInstance) {
        if (!perk.obtainable().isPurchasable())
            return;

        new ConfirmInventory(perk.getDisplayName(), perk.item(), perk.obtainable()) {
            @Override
            public void onConfirm(InventoryClickEvent event, OMPlayer omp) {
                perk.obtainable().purchase(omp);
                itemInstance.onConfirm(event, omp);
                returnInventory().open(omp);
            }

            @Override
            public void onCancel(InventoryClickEvent event, OMPlayer omp) {
                returnInventory().open(omp);
            }
        }.open(omp);
    }

    public ItemBuilder toItemBuilder(Perk perk, OMPlayer omp) {
        return new ItemBuilder(perk.item().getMaterial(), perk.item().getAmount(), perk.item().getDurability(), perk.getDisplayName(), "", perk.hasAccess(omp) ? "§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) : perk.obtainable().getRequiredLore(omp), "");
    }

    public LeatherArmorBuilder toLeatherArmorBuilder(Perk perk, OMPlayer omp, LeatherArmorBuilder.Type type, Color color) {
        return new LeatherArmorBuilder(type, color, perk.item().getAmount(), perk.getDisplayName(), "", perk.hasAccess(omp) ? "§a§l" + omp.getMessage(new Message("Ontgrendeld", "Unlocked")) : perk.obtainable().getRequiredLore(omp), "");
    }

    protected abstract class ConfirmItemInstance extends ItemInstance {

        public ConfirmItemInstance(ItemStack itemStack) {
            super(itemStack);
        }

        /* Called after a player has made the payment, give their perks here */
        protected abstract void onConfirm(InventoryClickEvent event, OMPlayer omp);

    }
}
