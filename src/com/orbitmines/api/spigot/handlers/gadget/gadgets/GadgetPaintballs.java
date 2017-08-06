package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.spigot.utils.LocationUtils;
import com.orbitmines.api.utils.RandomUtils;
import com.orbitmines.api.spigot.utils.WorldUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetPaintballs extends GadgetHandler implements Listener {

    private List<Entity> entities;

    public GadgetPaintballs() {
        super(Gadget.PAINTBALLS);

        api.getServer().getPluginManager().registerEvents(this, api);

        entities = new ArrayList<>();
    }

    @Override
    public void onRun() {

    }

    @Override
    public void onLogout(OMPlayer omp) {

    }

    @Override
    public void onInteract(PlayerInteractEvent event, OMPlayer omp) {
        event.setCancelled(true);
        omp.updateInventory();

        entities.add(omp.getPlayer().launchProjectile(Snowball.class));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity ball = event.getEntity();

        if (!(ball instanceof Snowball) || !entities.contains(ball))
            return;

        List<Location> locations = getLocations(ball.getLocation());
        for (Location location : new ArrayList<>(locations)) {
            Block b = location.getBlock();

            if (b.isEmpty() || WorldUtils.CANNOT_TRANSFORM.contains(b.getType()))
                locations.remove(location);
        }

        int r = RandomUtils.RANDOM.nextInt(16);
        for (Player player : ball.getWorld().getPlayers()) {
            for (Location location : locations) {
                player.sendBlockChange(location, Material.STAINED_CLAY, (byte) r);
            }
        }

        new BukkitRunnable() {
            public void run() {
                for (Player player : ball.getWorld().getPlayers()) {
                    for (Location location : locations) {
                        player.sendBlockChange(location, location.getBlock().getType(), location.getBlock().getData());
                    }
                }

            }
        }.runTaskLater(api, 200);
    }

    private List<Location> getLocations(Location l){
        return new ArrayList<>(Arrays.asList(LocationUtils.asNewLocation(l, 0, -1, 0), LocationUtils.asNewLocation(l, 0, 0, 0), LocationUtils.asNewLocation(l, 1, -1, 0), LocationUtils.asNewLocation(l, 0, -1, 1), LocationUtils.asNewLocation(l, -1, -1, 0), LocationUtils.asNewLocation(l, 0, -2, 0), LocationUtils.asNewLocation(l, 0, -1, -1), LocationUtils.asNewLocation(l, 0, +1, 0), LocationUtils.asNewLocation(l, 1, 0, 0), LocationUtils.asNewLocation(l, -1, 0, 0), LocationUtils.asNewLocation(l, 0, 0, 1), LocationUtils.asNewLocation(l, 0, 0, -1), LocationUtils.asNewLocation(l, 2, -1, 0), LocationUtils.asNewLocation(l, 0, -1, 2), LocationUtils.asNewLocation(l, -2, -1, 0), LocationUtils.asNewLocation(l, 0, -1, -2), LocationUtils.asNewLocation(l, 1, -1, 1), LocationUtils.asNewLocation(l, 1, -1, -1), LocationUtils.asNewLocation(l, -1, -1, 1), LocationUtils.asNewLocation(l, -1, -1, -1), LocationUtils.asNewLocation(l, 0, -3, 0), LocationUtils.asNewLocation(l, 1, -2, 0), LocationUtils.asNewLocation(l, -1, -2, 0), LocationUtils.asNewLocation(l, 0, -2, 1), LocationUtils.asNewLocation(l, 0, -2, -1)));
    }
}
