package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.nms.customitem.CustomItemNms;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetGrapplingHook extends GadgetHandler implements Listener {

    public GadgetGrapplingHook() {
        super(Gadget.GRAPPLING_HOOK);
    }

    @Override
    public void onRun() {
        api.getServer().getPluginManager().registerEvents(this, api);
    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(false);
    }

    @Override
    public ItemStack getItem(OMPlayer omp) {
        CustomItemNms nms = api.getNms().customItem();
        return nms.hideFlags(nms.setUnbreakable(super.getItem(omp)), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS);
    }

    @EventHandler
    public void onHit(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();

        if (!(projectile instanceof FishHook) || !(projectile.getShooter() instanceof Player))
            return;

        Player p = (Player) projectile.getShooter();

        if (p.getInventory().getHeldItemSlot() == api.gadgets().getGadgetSlot()) {
            Location l1 = projectile.getLocation();
            Location l2 = p.getLocation();

            p.setVelocity(l1.toVector().subtract(l2.toVector()).multiply(1.1).add(new Vector(0, 0.5, 0)));

            projectile.remove();
        }
    }
}
