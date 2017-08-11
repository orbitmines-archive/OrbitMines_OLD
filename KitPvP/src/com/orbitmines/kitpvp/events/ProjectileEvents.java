package com.orbitmines.kitpvp.events;

import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitHandler;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.ShootBowEventHandler;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.HashMap;
import java.util.Map;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public class ProjectileEvents implements Listener {

    private Map<Entity, ShootBowEventHandler> entities = new HashMap<>();

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(player);

        KitHandler kit = omp.getKitActive();
        if (kit == null)
            return;

        Passive passive = kit.getPassive(event.getBow());
        if (passive == null || !(passive instanceof ShootBowEventHandler))
            return;

        ShootBowEventHandler handler = (ShootBowEventHandler) passive;
        entities.put(event.getProjectile(), handler);

        handler.onShootBowEvent(event, omp);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        Entity entity = event.getEntity();

        if (entities.containsKey(entity)) {
            entities.get(entity).onProjectileHit(event, KitPvPPlayer.getPlayer((Player) ((Projectile) entity).getShooter()));
            entities.remove(entity);
        }

        if (entity instanceof Arrow)
            entity.remove();
    }
}
