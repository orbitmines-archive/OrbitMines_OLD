package com.orbitmines.kitpvp.events;

import com.orbitmines.api.spigot.utils.ItemUtils;
import com.orbitmines.kitpvp.KitPvP;
import com.orbitmines.kitpvp.handlers.KitPvPPlayer;
import com.orbitmines.kitpvp.handlers.inventory.KitPvPTeleporterInventory;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

/*
* OrbitMines - @author Fadi Shawki - 11-8-2017
*/
public class ClickEvent implements Listener {

    private KitPvP kitPvP;

    public ClickEvent() {
        kitPvP = KitPvP.getInstance();
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        KitPvPPlayer omp = KitPvPPlayer.getPlayer(player);

        if (event.getSlotType() == InventoryType.SlotType.ARMOR || omp.isSpectator()) {
            event.setResult(Event.Result.DENY);
            omp.updateInventory();

            if (!omp.isSpectator() || event.getClickedInventory() == null || event.getClickedInventory().getType() != InventoryType.PLAYER)
                return;

            ItemStack item = event.getCurrentItem();
            if (ItemUtils.isNull(item))
                return;

            if (item.getType() == Material.NAME_TAG && item.getItemMeta().getDisplayName().equals("§e§nTeleporter")) {
                event.setCancelled(true);
                new KitPvPTeleporterInventory().open(omp);

                return;
            } else if (item.getType() == Material.ENDER_PEARL && item.getItemMeta().getDisplayName().endsWith("Lobby")) {
                event.setCancelled(true);

                omp.setSpectator(false);

                Player p = omp.getPlayer();
                p.teleport(kitPvP.getSpawnLocation());
                omp.clearInventory();
                kitPvP.getLobbyKit(omp).setItems(p);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 5, 1);
                p.setFlying(false);
                p.setAllowFlight(false);
                omp.shown();

                return;
            }
        }
    }
}
