package com.orbitmines.api.spigot.events;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.kit.KitInteractive;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 9-8-2017
*/
public class InteractiveKitEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        if (omp == null)
            return;

        ItemStack itemStack = event.getItem();

        KitInteractive last = omp.getLastInteractiveKit();
        if (last == null)
            return;

        if (ItemUtils.isNull(itemStack))
            return;

        for (KitInteractive.InteractAction action : last.getInteractions()) {
            if (action.equals(itemStack)) {
                action.onInteract(event, omp);
                break;
            }
        }
    }
}
