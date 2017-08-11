package com.orbitmines.kitpvp.handlers.passives.firetrail;

import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.Passive;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.passives.BootsMoveEventHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

/*
* OrbitMines - @author Fadi Shawki - 10-8-2017
*/
public abstract class FireTrail extends Passive implements BootsMoveEventHandler {

    private KitPvP kitPvP;

    public FireTrail(int level) {
        super(Type.FIRE_TRAIL, level);

        kitPvP = KitPvP.getInstance();
    }

    @Override
    public void onMove(PlayerMoveEvent event, KitPvPPlayer omp) {
        Player p = omp.getPlayer();
        Block bl = p.getWorld().getBlockAt(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());

        if (bl.isEmpty() && bl.getType() != Material.FIRE) {
            bl.setType(Material.FIRE);

            new BukkitRunnable() {
                public void run() {
                    bl.setType(Material.AIR);
                }
            }.runTaskLater(kitPvP, 20);
        }
    }
}
