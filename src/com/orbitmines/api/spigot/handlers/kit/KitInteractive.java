package com.orbitmines.api.spigot.handlers.kit;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 9-8-2017
*/
public class KitInteractive extends Kit {

    private List<InteractAction> interactions;

    public KitInteractive(String name) {
        super(name);

        interactions = new ArrayList<>();
        OrbitMinesApi.getApi().setupInteractiveKits();
    }

    public void setItem(int index, InteractAction action) {
        setItem(index, action.getItemStack());

        interactions.add(action);
    }

    @Override
    public void setItems(Player p) {
        super.setItems(p);

        registerLast(p);
    }

    @Override
    public void addItems(Player p) {
        super.addItems(p);

        registerLast(p);
    }

    @Override
    public void replaceItems(Player p) {
        super.replaceItems(p);

        registerLast(p);
    }

    public List<InteractAction> getInteractions() {
        return interactions;
    }

    private void registerLast(Player p) {
        OMPlayer.getPlayer(p).setLastInteractiveKit(this);
    }

    public static abstract class InteractAction {

        private ItemStack itemStack;

        public InteractAction(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public abstract void onInteract(PlayerInteractEvent event, OMPlayer omp);

        /* Override to change */
        public boolean equals(ItemStack item) {
            return item.getType() == itemStack.getType() && item.getDurability() == itemStack.getDurability() && item.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName());
        }

        public ItemStack getItemStack() {
            return itemStack;
        }
    }
}
