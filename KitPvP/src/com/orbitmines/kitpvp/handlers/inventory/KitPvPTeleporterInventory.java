package com.orbitmines.kitpvp.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.inventory.TeleporterInventory;
import com.orbitmines.api.spigot.handlers.itembuilders.PlayerSkullBuilder;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.playerdata.KitPvPData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class KitPvPTeleporterInventory extends TeleporterInventory {

    @Override
    protected List<OMPlayer> getPlayers() {
        List<OMPlayer> players = new ArrayList<>();
        for (KitPvPPlayer omp : KitPvPPlayer.getKitPvPPlayers()) {
            if (omp.isSpectator() || omp.getKitActive() == null)
                continue;

            players.add(omp);
        }
        return players;
    }

    @Override
    protected ItemStack getItemStack(OMPlayer player) {
        KitPvPPlayer omp = (KitPvPPlayer) player;
        KitPvPData data = omp.kitPvP();

        ItemStack item = new PlayerSkullBuilder(omp.getPlayer().getName()).build();
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(omp.getName());
        List<String> itemLore = new ArrayList<>();
        itemLore.add("");
        itemLore.add("§7Kit: " + omp.getKitActive().getType().getSelectedKitName(omp.getKitActive().getLevel()));
        itemLore.add("§cHealth: §f" + String.format("%.1f", omp.getPlayer().getHealth() / 2).replaceAll(",", ".") + "/10.0");
        itemLore.add("§6Food: §f" + String.format("%.1f", (double) omp.getPlayer().getFoodLevel() / 2).replaceAll(",", ".") + "/10.0");
        itemLore.add("§9" + omp.getMessage(new Message("Huidige", "Current")) + " Streak: §f" + omp.getCurrentStreak());
        itemLore.add("");
        itemLore.add("§c§lKitPvP Stats:");
        itemLore.add("§cKills: §f" + data.getKills());
        itemLore.add("§4Deaths: §f" + data.getDeaths());
        itemLore.add("§eLevel: §f" + data.getLevel());
        itemLore.add("§b" + omp.getMessage(new Message("Beste", "Best")) + " Streak: §f" + data.getBestStreak());
        itemLore.add("");
        itemLore.add(omp.getMessage(new Message("§e§lKlik hier om te Teleporteren", "§e§lClick Here to Teleport")));
        itemLore.add("");
        itemmeta.setLore(itemLore);
        item.setItemMeta(itemmeta);

        return item;
    }
}
