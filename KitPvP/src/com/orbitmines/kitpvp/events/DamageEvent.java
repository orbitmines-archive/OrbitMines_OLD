package com.orbitmines.kitpvp.events;

import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.EntityDamageEventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class DamageEvent implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player pE = (Player) event.getEntity();
            KitPvPPlayer ompE = KitPvPPlayer.getPlayer(pE);

            if (ompE.getKitActive() == null) {
                event.setCancelled(true);
                return;
            }

            for (ItemStack item : pE.getInventory().getArmorContents()) {
                Passive passive = ompE.getKitActive().getPassive(item);

                if (passive == null || !(passive instanceof EntityDamageEventHandler))
                    return;

                ((EntityDamageEventHandler) passive).onDamage(event, ompE);
            }
        }
    }
}
