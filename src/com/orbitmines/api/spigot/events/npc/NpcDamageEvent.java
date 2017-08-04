package com.orbitmines.api.spigot.events.npc;

import com.orbitmines.api.spigot.handlers.npc.Npc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class NpcDamageEvent implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled())
            return;

        Npc npc = Npc.getNpc(event.getEntity());

        if (npc != null)
            event.setCancelled(true);
    }
}
