package com.orbitmines.api.spigot.events.npc;

import com.orbitmines.api.spigot.handlers.Cooldown;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.npc.FloatingItem;
import com.orbitmines.api.spigot.handlers.npc.Hologram;
import com.orbitmines.api.spigot.handlers.npc.NpcArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class NpcInteractAtEntityEvent implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand))
            return;

        Player player = event.getPlayer();
        OMPlayer omp = OMPlayer.getPlayer(player);

        ArmorStand armorStand = (ArmorStand) event.getRightClicked();

        Hologram hologram = Hologram.getHologram(armorStand);

        if (hologram != null) {
            event.setCancelled(true);
            omp.updateInventory();

            return;
        }

        NpcArmorStand npcArmorStand = NpcArmorStand.getNpcArmorStand(armorStand);

        if (npcArmorStand != null) {
            event.setCancelled(true);

            if (npcArmorStand.canClick() && !omp.onCooldown(Cooldown.NPC_INTERACT)) {
                npcArmorStand.click(event, omp);

                omp.resetCooldown(Cooldown.NPC_INTERACT);
            }

            omp.updateInventory();

            return;
        }

        FloatingItem floatingItem = FloatingItem.getFloatingItem(armorStand);

        if (floatingItem != null) {
            event.setCancelled(true);

            if (floatingItem.canClick() && !omp.onCooldown(Cooldown.NPC_INTERACT)) {
                floatingItem.click(event, omp);

                omp.resetCooldown(Cooldown.NPC_INTERACT);
            }

            omp.updateInventory();

//            return;
        }
    }
}
