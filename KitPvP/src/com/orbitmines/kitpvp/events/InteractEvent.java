package com.orbitmines.kitpvp.events;

import com.orbitmines.kitpvp.handlers.KitPvPMap;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class InteractEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(p);

        if (omp.isSpectator()) {
            event.setCancelled(true);
            return;
        }

        if (omp.getKitActive() != null || event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        Block b = event.getClickedBlock();
        if (b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN) {
            KitPvPMap map = KitPvPMap.getKitPvPMap(b.getLocation());

            if (map == null || omp.hasVoted())
                return;

            omp.getPlayer().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 5, 1);
            omp.vote(map);
        }
    }
}
