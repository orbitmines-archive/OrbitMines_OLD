package com.orbitmines.api.spigot.events;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.SchematicGenerator;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 12-8-2017
*/
public class SchematicGeneratorEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());
        if (omp == null)
            return;

        ItemStack itemStack = event.getItem();

        if (omp.isOpMode() && SchematicGenerator.getGenerators().containsKey(omp) && itemStack != null && itemStack.getType() == SchematicGenerator.WAND.getType()) {
            event.setCancelled(true);

            Block b = event.getClickedBlock();
            if (b == null)
                return;

            Location l = b.getLocation();

            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK:
                    if (SchematicGenerator.getGenerators().containsKey(omp)) {
                        SchematicGenerator.getGenerators().get(omp).setL1(l);
                        omp.getPlayer().sendMessage("§7Set §eLocation 1§7 to §e" + b.getLocation().getBlockX() + "§7, §e" + b.getLocation().getBlockY() + "§7, §e" + b.getLocation().getBlockZ() + "§7.");
                    } else {
                        omp.sendMessage(new Message("§7Je moet eerst een nieuwe Schematic Generator maken!", "§7You have to create a new Schematic Generator first!"));
                    }
                    break;
                case RIGHT_CLICK_BLOCK:
                    if (SchematicGenerator.getGenerators().containsKey(omp)) {
                        SchematicGenerator.getGenerators().get(omp).setL2(l);
                        omp.getPlayer().sendMessage("§7Set §eLocation 2§7 to §e" + b.getLocation().getBlockX() + "§7, §e" + b.getLocation().getBlockY() + "§7, §e" + b.getLocation().getBlockZ() + "§7.");
                    } else {
                        omp.sendMessage(new Message("§7Je moet eerst een nieuwe Schematic Generator maken!", "§7You have to create a new Schematic Generator first!"));
                    }
                    break;
            }
        }
    }
}
