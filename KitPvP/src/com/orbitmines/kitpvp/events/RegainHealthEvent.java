package com.orbitmines.kitpvp.events;

import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class RegainHealthEvent implements Listener {

    @EventHandler
    public void onRegain(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getRegainReason() != EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() != EntityRegainHealthEvent.RegainReason.REGEN)
            return;

        Player p = (Player) event.getEntity();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(p);

        if (omp.getKitActive() == null)
            return;

        /* Increase health regen */
        double amount = event.getAmount() * 1.5;
        amount *= omp.getKitActive().getHealthRegenType(omp).getMultiplier();

        event.setAmount(amount);
    }
}
