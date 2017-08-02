package com.orbitmines.api.spigot.handlers.inventory;

import com.orbitmines.api.Message;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 30-7-2017
*/
public abstract class TeleporterInventory extends OMInventory {

    public TeleporterInventory() {
        newInventory(27, "§0§lTeleporter");
    }

    protected abstract List<OMPlayer> getPlayers();

    protected abstract ItemStack getItemStack(OMPlayer omp);

    @Override
    protected boolean onOpen(OMPlayer omp) {
        return true;
    }

    @Override
    protected void onClick(InventoryClickEvent event, OMPlayer omp) {

    }

    public void update() {
        List<OMPlayer> players = getPlayers();

        int leftOver = players.size() % 9;
        int inventorySize = leftOver == 0 ? players.size() / 9 : (players.size() + (9 - leftOver) / 9);
        if (inventorySize > 54)
            inventorySize = 54;

        newInventory(inventorySize, "§0§lTeleporter");

        int slot = 0;
        for (OMPlayer omp : players) {
            if (slot > 54)
                break;

            add(slot, new ItemInstance(getItemStack(omp)) {
                @Override
                public void onClick(InventoryClickEvent event, OMPlayer omp) {
                    Player teleportTo;
                    String displayName = itemStack.getItemMeta().getDisplayName();

                    String[] data = displayName.split(" ");
                    if(data.length > 1)
                        teleportTo = Bukkit.getPlayer(data[1].substring(2));
                    else
                        teleportTo = Bukkit.getPlayer(displayName.substring(2));

                    if (teleportTo == null)
                        return;

                    Player p = omp.getPlayer();

                    p.closeInventory();
                    p.teleport(teleportTo);
                    p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);
                    omp.sendMessage(new Message("§eTeleporten naar " + displayName + "§e...", "§eTeleporting to " + displayName + "§e..."));
                }
            });

            slot++;
        }
    }
}
