package com.orbitmines.kitpvp.handlers.passives.lightning;

import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.ShootBowEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class Lightning extends Passive implements ShootBowEventHandler {

    public Lightning(int level) {
        super(Type.LIGHTNING, level);
    }

    public abstract boolean strikeLightning();

    public abstract boolean onlyTarget();

    public abstract double getDamage();

    public abstract int amount();

    @Override
    public void onProjectileHit(ProjectileHitEvent event, KitPvPPlayer shooter) {
        if (!(event.getHitEntity() instanceof Player))
            return;

        Player entity = (Player) event.getHitEntity();

        if (!strikeLightning())
            return;

        Player player = shooter.getPlayer();
        for (int i = 0; i < amount(); i++) {
            entity.getWorld().strikeLightningEffect(entity.getLocation());
            lightningDamage(entity);

            for (Entity near : entity.getNearbyEntities(0.5, 0.5, 0.5)) {
                if (near instanceof Player && (near != player || !onlyTarget()))
                    lightningDamage(near);
            }
        }
    }

    private void lightningDamage(Entity entity) {
        EntityDamageEvent e = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.LIGHTNING, getDamage());
        entity.setLastDamageCause(e);
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    @Override
    public void onShootBowEvent(EntityShootBowEvent event, KitPvPPlayer shooter) {

    }
}
