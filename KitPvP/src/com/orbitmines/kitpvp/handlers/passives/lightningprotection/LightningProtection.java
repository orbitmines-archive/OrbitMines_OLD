package com.orbitmines.kitpvp.handlers.passives.lightningprotection;

import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.EntityDamageEventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class LightningProtection extends Passive implements EntityDamageEventHandler {

    public Map<Player, EntityDamageEvent> previous = new HashMap<>();

    public LightningProtection(int level) {
        super(Type.LIGHTNING, level);
    }

    @Override
    public void onDamage(EntityDamageEvent event, KitPvPPlayer damaged) {
        if (event.getCause() != EntityDamageEvent.DamageCause.LIGHTNING)
            return;

        Player p = damaged.getPlayer();

        if (previous.containsKey(p) && previous.get(p) == event) {
            event.setDamage(event.getDamage() * (1 - ((0.075 * level) * 0.9)));
        } else {
            event.setDamage(event.getDamage() * (1 - (0.075 * level)));
            previous.put(p, event);
        }
    }
}
