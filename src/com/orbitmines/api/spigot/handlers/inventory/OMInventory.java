package com.orbitmines.api.spigot.handlers.inventory;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class OMInventory {

	protected Inventory inventory;
	protected ItemInstance[] itemInstances;

	/* Called before inventory is opened, return false to cancel */
    protected abstract boolean onOpen(OMPlayer omp);

    /* Called after a player clicks a registered item */
    protected abstract void onClick(InventoryClickEvent event, OMPlayer omp);

	public Inventory getInventory(){
		return inventory;
	}

	protected void newInventory(int size, String title) {
	    this.inventory = Bukkit.createInventory(null, size, title);
	    this.itemInstances = new ItemInstance[inventory.getSize()];
    }

    protected void add(int slot, ItemInstance itemInstance) {
	    itemInstances[slot] = itemInstance;
    }

    public void open(OMPlayer omp) {
	    if (!onOpen(omp))
	        return;

        omp.getPlayer().openInventory(inventory);
        omp.setLastInventory(this);
    }

    /* Event Cancelled on default, use Event#setCancelled(false) in ItemInstance#onClick in in order to undo */
    public void processClickEvent(InventoryClickEvent event, OMPlayer omp) {
        Inventory clicked = event.getClickedInventory();
        if (clicked == null || clicked.getTitle() == null || !clicked.getTitle().equals(inventory.getTitle()))
            return;

        event.setCancelled(true);

        ItemInstance itemInstance = itemInstances[event.getSlot()];
        if (itemInstance == null)
            return;

        itemInstance.onClick(event, omp);
        onClick(event, omp);
    }

    public void clearInstances() {
        for (int i = 0; i < itemInstances.length; i++) {
            itemInstances[i] = null;
        }
    }

    public abstract class ItemInstance {

	    protected final ItemStack itemStack;

	    public ItemInstance(ItemStack itemStack) {
	        this.itemStack = itemStack;
        }

        public abstract void onClick(InventoryClickEvent event, OMPlayer omp);

        public ItemStack getItemStack() {
            return itemStack;
        }
    }

    public class EmptyItemInstance extends ItemInstance {

        public EmptyItemInstance(ItemStack itemStack) {
            super(itemStack);
        }

        @Override
        public void onClick(InventoryClickEvent event, OMPlayer omp) {

        }
    }
}
