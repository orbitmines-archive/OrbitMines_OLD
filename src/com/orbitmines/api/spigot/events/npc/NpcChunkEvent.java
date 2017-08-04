package com.orbitmines.api.spigot.events.npc;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class NpcChunkEvent implements Listener {

    private OrbitMinesApi api;

    public NpcChunkEvent() {
        api = OrbitMinesApi.getApi();
    }

    @EventHandler
    public void onUnload(ChunkUnloadEvent event) {
        if (api.getChunks().contains(event.getChunk()))
            return;
    }
}
