package com.orbitmines.survival.events;

import com.orbitmines.survival.Survival;
import com.orbitmines.survival.handlers.SurvivalPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class InteractEvent implements Listener {

    private Survival survival;

    public InteractEvent() {
        survival = Survival.getInstance();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();

        if (p.getWorld().getName().equals(survival.getWorld().getName()) || item == null || item.getType() != Material.STONE_HOE)
            return;

        event.setCancelled(true);
        SurvivalPlayer.getPlayer(p).updateInventory();
    }
}
