package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.perks.Gadget;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetCreeperLauncher extends GadgetHandler implements Listener {

    private final Cooldown COOLDOWN = new Cooldown(6000, Gadget.CREEPER_LAUNCHER.getName(), Gadget.CREEPER_LAUNCHER.getName(), Cooldown.Action.LEFT_CLICK);

    private List<Entity> entities;

    public GadgetCreeperLauncher() {
        super(Gadget.CREEPER_LAUNCHER);

        api.getServer().getPluginManager().registerEvents(this, api);
        entities = new ArrayList<>();
    }

    @Override
    public void onRun() {

    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);
        omp.updateInventory();

        if (!omp.canReceiveVelocity() || omp.onCooldown(COOLDOWN))
            return;

        Player p = omp.getPlayer();
        Creeper creeper = (Creeper) p.getWorld().spawnEntity(p.getLocation(), EntityType.CREEPER);
        creeper.setPowered(true);
        creeper.setVelocity(p.getLocation().getDirection().normalize().multiply(1.5));

        entities.add(creeper);

        omp.resetCooldown(COOLDOWN);
    }

    @EventHandler
    public void EntityExplodeEvent(ExplosionPrimeEvent event) {
        event.setFire(false);

        if (!(event.getEntity() instanceof Creeper) || !entities.contains(event.getEntity()))
            return;

        for (Entity en : event.getEntity().getNearbyEntities(3, 3, 3)) {
            if (!(en instanceof Player))
                continue;

            Player p = (Player) en;
            OMPlayer omp = OMPlayer.getPlayer(p);

            if (omp.canReceiveVelocity()) {
                Vector v = p.getLocation().getDirection();
                p.setVelocity(v.multiply(-1).add(new Vector(0, 1.3, 0)));
            }
        }
    }

    @EventHandler
    public void EntityExplodeEvent(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper)
            event.blockList().clear();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e){
        if (e.getEntity() instanceof Creeper)
            e.getDrops().clear();
    }
}
