package com.orbitmines.survival.events;

import com.orbitmines.survival.Survival;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Created by Fadi-Laptop on 16-9-2016.
 */
public class DamageEvent implements Listener {

    private Survival survival;

    public DamageEvent() {
        survival = Survival.getInstance();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || event.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;

        Player pE = (Player) event.getEntity();

        if (!survival.getNoFall().contains(pE))
            return;

        event.setCancelled(true);
        survival.getNoFall().remove(pE);
    }
}
