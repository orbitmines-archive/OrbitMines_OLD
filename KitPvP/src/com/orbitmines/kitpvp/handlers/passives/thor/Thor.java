package com.orbitmines.kitpvp.handlers.passives.thor;

import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.EntityDamageByEntityEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class Thor extends Passive implements EntityDamageByEntityEventHandler {

    public Thor(int level) {
        super(Type.THOR, level);
    }

    public abstract boolean strikeLightning();

    public abstract boolean onlyTarget();

    public abstract double getDamage();

    @Override
    public void onDamage(EntityDamageByEntityEvent event, KitPvPPlayer ompD, KitPvPPlayer ompE) {
        if (!strikeLightning())
            return;

        Player pE = ompE.getPlayer();
        Player pD = ompD.getPlayer();

        pE.getWorld().strikeLightningEffect(pE.getLocation());
        lightningDamage(pE);

        for (Entity near : pE.getNearbyEntities(0.5, 0.5, 0.5)) {
            if (near instanceof Player && (near != pD || !onlyTarget()))
                lightningDamage(near);
        }
    }

    private void lightningDamage(Entity entity) {
        EntityDamageEvent e = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.LIGHTNING, getDamage());
        entity.setLastDamageCause(e);
        Bukkit.getServer().getPluginManager().callEvent(e);
    }
}
