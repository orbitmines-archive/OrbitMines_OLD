package com.orbitmines.api.spigot.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.currency.Currency;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class ConfirmInventory extends OMInventory {

    protected final String displayName;
    protected final ItemSet item;
    protected final Obtainable obtainable;

    public ConfirmInventory(String displayName, ItemSet item, Obtainable obtainable) {
        if (obtainable.getCurrency() == null)
            throw new IllegalArgumentException("You can only use Currencies here!");

        this.displayName = displayName;
        this.item = item;
        this.obtainable = obtainable;
    }

    /* Called after a purchase has been completed */
    public abstract void onConfirm(InventoryClickEvent event, OMPlayer omp);

    /* Called after a purchase has been cancelled */
    public abstract void onCancel(InventoryClickEvent event, OMPlayer omp);

    @Override
    protected boolean onOpen(OMPlayer omp) {
        newInventory(45, omp.getMessage(new Message("§0§lBevestig je aankoop", "§0§lConfirm your Purchase")));

        ItemInstance confirm = new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 5, omp.getMessage(new Message("§a§lBevestigen", "§a§lConfirm"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                Player p = omp.getPlayer();

                p.sendMessage("");
                omp.sendMessage(new Message("§7Item Gekocht: " + displayName + "§7.", "§7Item Bought: " + displayName + "§7."));
                p.sendMessage("§7" + omp.getMessage(new Message("Prijs", "Price")) + ": " + getItem(Slot.CURRENCY).getItemMeta().getDisplayName() + "§7.");
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);

                onConfirm(event, omp);
            }
        };

        ItemInstance cancel = new ItemInstance(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, 14, omp.getMessage(new Message("§c§lAnnuleren", "§a§lCancel"))).build()) {
            @Override
            public void onClick(InventoryClickEvent event, OMPlayer omp) {
                omp.sendMessage(new Message("§7Aankoop §c§lGeannuleerd §7! (" + displayName + "§7)", "§c§lCancelled §7purchase! (" + displayName + "§7)"));

                onCancel(event, omp);
            }
        };

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (slot % 9 <= 2) {
                add(slot, confirm);
            } else if (slot % 9 >= 6) {
                add(slot, cancel);
            } else if (slot == Slot.PURCHASING.slot) {
                add(slot, new EmptyItemInstance(new ItemBuilder(item.getMaterial(), item.getAmount(), item.getDurability(), displayName).build()));
            } else if (slot == Slot.CURRENCY.slot) {
                ItemSet item = obtainable.item();
                Currency currency = obtainable.getCurrency();
                add(slot, new EmptyItemInstance(new ItemBuilder(item.getMaterial(), item.getAmount(), item.getDurability(), currency.color().getChatColor() + "§l" + obtainable.getPrice() + " " + (obtainable.getPrice() == 1 ? currency.getSingleName() : currency.getMultipleName())).build()));
            }
        }

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    protected ItemStack getItem(Slot slot) {
        return inventory.getItem(slot.slot);
    }

    public enum Slot {

        PURCHASING(13),
        CURRENCY(31);

        private final int slot;

        Slot(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }
}
