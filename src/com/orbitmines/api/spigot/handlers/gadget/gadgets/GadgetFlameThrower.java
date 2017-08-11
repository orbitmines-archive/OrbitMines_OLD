package com.orbitmines.api.spigot.handlers.gadget.gadgets;

import com.orbitmines.api.spigot.handlers.BlockFalling;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.gadget.GadgetHandler;
import com.orbitmines.api.spigot.perks.Gadget;
import com.orbitmines.api.spigot.utils.LocationUtils;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class GadgetFlameThrower extends GadgetHandler implements Listener {

    private List<FallingBlock> entities;

    public GadgetFlameThrower() {
        super(Gadget.FLAME_THROWER);

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

        Player p = omp.getPlayer();
        BlockFalling falling = new BlockFalling(LocationUtils.asNewLocation(p.getLocation(), 0, 1, 0), new ItemSet(Material.FIRE));
        falling.setDrop(false);

        FallingBlock fallingBlock = falling.spawn();
        fallingBlock.setVelocity(p.getLocation().getDirection().multiply(1.1));

        entities.add(fallingBlock);
    }

    @EventHandler
    public void onChange(EntityChangeBlockEvent e) {
        Entity en = e.getEntity();

        if (!(en instanceof FallingBlock))
            return;

        FallingBlock b = (FallingBlock) en;

        if (b.getMaterial() == Material.FIRE && entities.contains(b)) {
            entities.remove(b);

            new BukkitRunnable() {
                public void run() {
                    e.getBlock().setType(Material.AIR);
                }
            }.runTaskLater(api, 40);
        }
    }

    @EventHandler
    public void onSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }
}
