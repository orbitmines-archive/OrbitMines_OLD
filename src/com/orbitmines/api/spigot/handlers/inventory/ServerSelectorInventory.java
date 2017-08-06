package com.orbitmines.api.spigot.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.Server;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.itembuilders.ItemBuilder;
import com.orbitmines.api.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 3-8-2017
*/
public class ServerSelectorInventory extends OMInventory {

    private OrbitMinesApi api;

    public ServerSelectorInventory() {
        newInventory(18, "§0§lServer Selector");

        api = OrbitMinesApi.getApi();
    }

    @Override
    protected boolean onOpen(OMPlayer omp) {

        int slot = 0;
        for (Server server : Server.values()) {
            Server.Status status = server.getStatus();
            boolean thisServer = server == api.server().getServerType();

            ItemBuilder itemBuilder = new ItemBuilder(getMaterial(server), 1, 0, server.getColor() + server.getName() + " ");
            List<String> lore = new ArrayList<>();

            lore.add("§7Status: " + status.getColor() + status.getName());
            lore.add("§7Players: " + status.getColor() + server.getOnlinePlayers() + "§7/" + server.getColor() + server.getMaxPlayers());

            if (server.is(Server.Status.ONLINE))
                lore.add(thisServer ? omp.getMessage(new Message("§7§oKlik hier om te verbinden", "§7§oClick here to connect")) : omp.getMessage(new Message("§7§oVerbonden", "§7§oConnected")));

            itemBuilder.setLore(lore);

            if (thisServer)
                itemBuilder.glow();

            add(slot, new ItemInstance(api.getNms().customItem().hideFlags(itemBuilder.build(), ItemUtils.FLAG_ATTRIBUTES_MODIFIERS)) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    if (!thisServer)
                        omp.connect(server);
                    else
                        omp.sendMessage(new Message("§7Je bent al verbonden met die server!", "§7You are already connected to that server!"));
                }
            });

            slot++;
        }

        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    private Material getMaterial(Server server) {
        switch (server) {

            case KITPVP:
                return Material.IRON_SWORD;
            case PRISON:
                return Material.DIAMOND_PICKAXE;
            case CREATIVE:
                return Material.WOOD_AXE;
            case HUB:
                return Material.WATCH;
            case SURVIVAL:
                return Material.STONE_HOE;
            case SKYBLOCK:
                return Material.GRASS;
            case FOG:
                return Material.MAP;
            case MINIGAMES:
                return Material.SNOW_BALL;
        }
        return Material.STONE;
    }

    public static void update() {
        for (OMPlayer omp : OMPlayer.getPlayers()) {
            if (!(omp.getLastInventory() instanceof ServerSelectorInventory))
                continue;

            Player p = omp.getPlayer();
            Inventory inv = p.getOpenInventory().getTopInventory();
            if (inv != null && inv.getName() != null && inv.getName().equals(omp.getLastInventory().getInventory().getName()))
                omp.getLastInventory().onOpen(omp);
        }
    }
}
