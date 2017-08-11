package com.orbitmines.kitpvp.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class FadeEvent implements Listener {

    @EventHandler
    public void onFade(BlockFadeEvent e) {
        Block b = e.getBlock();

        if (b.getType() == Material.ICE)
            e.setCancelled(true);
    }
}
