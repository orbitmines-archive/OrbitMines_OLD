package com.orbitmines.api.spigot.events.npc;

import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.npc.Npc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class NpcInteractEntityEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Npc npc = Npc.getNpc(event.getRightClicked());

        if (npc == null)
            return;

        Player player = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(player);

        event.setCancelled(true);
        omp.updateInventory();

        if (!omp.onCooldown(Cooldown.NPC_INTERACT)) {
            npc.click(event, omp);

            omp.resetCooldown(Cooldown.NPC_INTERACT);
        }
    }
}
