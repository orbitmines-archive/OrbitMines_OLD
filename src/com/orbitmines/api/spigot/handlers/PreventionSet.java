package com.orbitmines.api.spigot.handlers;

import com.orbitmines.api.spigot.OrbitMinesApi;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 4-8-2017
*/
public class PreventionSet {

    private OrbitMinesApi api;

    private List<Listener> listeners;

    public PreventionSet(Prevention... preventions) {
        api = OrbitMinesApi.getApi();

        listeners = new ArrayList<>();

        for (Prevention prevention : preventions) {
            prevent(prevention);
        }
    }

    public void prevent(Prevention prevention) {
        Listener listener = null;

        switch (prevention) {

            case FOOD_CHANGE:
                listener = new PreventFood();
                break;
            case PVP:
                listener = new PreventPvP();
                break;
            case MOB_SPAWN:
                listener = new PreventMobSpawn();
                break;
            case WEATHER_CHANGE:
                listener = new PreventWeatherChange();
                break;
            case FALL_DAMAGE:
                listener = new PreventFallDamage();
                break;
            case BLOCK_PLACE:
                listener = new PreventBlockPlace();
                break;
            case BLOCK_BREAK:
                listener = new PreventBlockBreak();
                break;
            case BLOCK_INTERACTING:
                listener = new PreventBlockInteracting();
                break;
            case MONSTER_EGG_USAGE:
                listener = new PreventMonsterEggUsage();
                break;
        }

        listeners.add(listener);

        api.getServer().getPluginManager().registerEvents(listener, api);
    }

    public void unregister() {
        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }
    }

    public enum Prevention {

        FOOD_CHANGE,
        PVP,
        MOB_SPAWN,
        WEATHER_CHANGE,
        FALL_DAMAGE,
        BLOCK_PLACE,
        BLOCK_BREAK,
        BLOCK_INTERACTING,
        MONSTER_EGG_USAGE;

    }

    public class PreventFood implements Listener {

        @EventHandler
        public void preventFood(FoodLevelChangeEvent event) {
            event.setCancelled(true);
        }
    }

    public class PreventPvP implements Listener {

        @EventHandler
        public void preventPvP(EntityDamageByEntityEvent event) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
                event.setCancelled(true);
        }
    }

    public class PreventMobSpawn implements Listener {

        @EventHandler
        public void preventMobSpawn(CreatureSpawnEvent event) {
            if (!(event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM))
                event.setCancelled(true);
        }
    }

    public class PreventWeatherChange implements Listener {

        @EventHandler
        public void preventWeatherChange(WeatherChangeEvent event) {
            event.setCancelled(true);
        }
    }

    public class PreventFallDamage implements Listener {

        @EventHandler
        public void preventFallDamage(EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player))
                return;

            if (event.getCause() == EntityDamageEvent.DamageCause.FALL)
                event.setCancelled(true);
        }
    }

    public class PreventBlockPlace implements Listener {

        @EventHandler
        public void preventBlockPlace(BlockPlaceEvent event) {
            OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

            if (!omp.isOpMode())
                event.setCancelled(true);
        }
    }

    public class PreventBlockBreak implements Listener {

        @EventHandler
        public void preventBlockBreak(BlockBreakEvent event) {
            OMPlayer omp = OMPlayer.getPlayer(event.getPlayer());

            if (!omp.isOpMode())
                event.setCancelled(true);
        }
    }

    public class PreventBlockInteracting implements Listener {

        private final List<Material> notClickable = Arrays.asList(Material.CHEST, Material.ENDER_CHEST, Material.TRAPPED_CHEST, Material.FURNACE, Material.WORKBENCH, Material.ANVIL, Material.ENCHANTMENT_TABLE, Material.DISPENSER, Material.HOPPER, Material.DROPPER, Material.TRAP_DOOR, Material.LEVER, Material.STONE_BUTTON, Material.WOOD_BUTTON, Material.ACACIA_DOOR, Material.ACACIA_FENCE_GATE, Material.BED_BLOCK, Material.BIRCH_DOOR, Material.BIRCH_FENCE_GATE, Material.BREWING_STAND, Material.BURNING_FURNACE, Material.CAKE_BLOCK, Material.CAULDRON, Material.COMMAND, Material.DARK_OAK_DOOR, Material.DARK_OAK_FENCE_GATE, Material.ENDER_PORTAL, Material.FENCE_GATE, Material.FIRE, Material.FLOWER_POT, Material.JUNGLE_DOOR, Material.JUNGLE_FENCE_GATE, Material.JUKEBOX, Material.OBSERVER, Material.REDSTONE_COMPARATOR_OFF, Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_COMPARATOR, Material.SPRUCE_DOOR, Material.SPRUCE_FENCE_GATE, Material.TRAP_DOOR, Material.WOOD_DOOR, Material.WOODEN_DOOR);

        @EventHandler
        public void preventBlockInteracting(PlayerInteractEvent event) {
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
                return;

            Block block = event.getClickedBlock();

            if (notClickable.contains(block.getType()))
                event.setCancelled(true);
        }
    }

    public class PreventMonsterEggUsage implements Listener {

        @EventHandler
        public void preventMonsterEggUsage(PlayerInteractEntityEvent event) {
            Player player = event.getPlayer();

            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getType() != Material.MONSTER_EGG && item.getType() != Material.EGG)
                return;

            event.setCancelled(true);
            OMPlayer.getPlayer(player).updateInventory();
        }

        @EventHandler
        public void preventMonsterEggUsage(PlayerInteractAtEntityEvent event) {
            Player player = event.getPlayer();

            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getType() != Material.MONSTER_EGG && item.getType() != Material.EGG)
                return;

            event.setCancelled(true);
            OMPlayer.getPlayer(player).updateInventory();
        }

        @EventHandler
        public void preventMonsterEggUsage(PlayerInteractEvent event) {
            Player player = event.getPlayer();

            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || item.getType() != Material.MONSTER_EGG && item.getType() != Material.EGG)
                return;

            event.setCancelled(true);
            OMPlayer.getPlayer(player).updateInventory();
        }
    }
}
