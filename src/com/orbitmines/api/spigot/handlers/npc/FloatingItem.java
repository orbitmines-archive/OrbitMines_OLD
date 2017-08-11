package com.orbitmines.api.spigot.handlers.npc;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Created by Fadi on 16-5-2016.
 */
public class FloatingItem {

    private static List<FloatingItem> floatingItems = new ArrayList<>();

    private OrbitMinesApi api;

    private Location location;
    private ArmorStand armorStand;

    private ArrayList<ItemInstance> itemInstances;
    private BukkitTask task;

    private PickUpAction pickUpAction;
    private ClickAction clickAction;

    public FloatingItem(Location location) {
        this(location, null, null);
    }

    public FloatingItem(Location location, PickUpAction pickUpAction) {
        this(location, pickUpAction, null);
    }

    public FloatingItem(Location location, ClickAction clickAction) {
        this(location, null, clickAction);
    }

    public FloatingItem(Location location, PickUpAction pickUpAction, ClickAction clickAction) {
        api = OrbitMinesApi.getApi();

        floatingItems.add(this);

        this.location = location;
        this.itemInstances = new ArrayList<>();
        this.pickUpAction = pickUpAction;
        this.clickAction = clickAction;
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public boolean canPickUp() {
        return pickUpAction != null;
    }

    public boolean canClick() {
        return clickAction != null;
    }

    public ArrayList<ItemInstance> getItemInstances() {
        return itemInstances;
    }

    public void spawn() {
        String armorStandDisplayName = null;
        boolean armorStandDisplayNameVisible = false;
        if (armorStand != null) {
            armorStandDisplayName = armorStand.getCustomName();
            armorStandDisplayNameVisible = armorStand.isCustomNameVisible();
        }
        clear();

        Chunk chunk = location.getChunk();
        chunk.load();
        api.getChunks().add(chunk);

        armorStand = api.getNms().armorStand().spawn(location, false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setGravity(false);

        if (armorStandDisplayName != null) {
            armorStand.setCustomName(armorStandDisplayName);
            armorStand.setCustomNameVisible(armorStandDisplayNameVisible);
        }

        for (ItemInstance itemInstance : itemInstances) {
            itemInstance.spawn();
        }

        FloatingItem floatingItem = this;
        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemInstance itemInstance : itemInstances) {
                    Item item = itemInstance.getItem();

                    if (item == null || item.isDead()) {
                        task.cancel();

                        if (floatingItems.contains(floatingItem))
                            spawn();
                    } else {
                        item.setTicksLived(1);
                    }
                }
            }
        }.runTaskTimer(api, 0, 1);
    }

    public void clear() {
        for (ItemInstance itemInstance : itemInstances) {
            itemInstance.delete();
        }

        if(armorStand != null)
            armorStand.remove();
    }

    public void delete() {
        clear();
        floatingItems.remove(this);
    }

    public void pickUp(PlayerPickupItemEvent event, OMPlayer player) {
        pickUpAction.pickUp(event, player, this);

        if (!event.isCancelled()) {
            event.setCancelled(true);
            delete();
        }
    }

    public void click(PlayerInteractAtEntityEvent event, OMPlayer player) {
        clickAction.click(event, player, this);
    }

    public static FloatingItem getFloatingItem(Item item) {
        for (FloatingItem floatingItem : floatingItems) {
            for (ItemInstance itemInstance : floatingItem.itemInstances) {
                if (itemInstance.item != null && itemInstance.item.getEntityId() == item.getEntityId())
                    return floatingItem;
            }
        }

        return null;
    }

    public static FloatingItem getFloatingItem(ArmorStand armorStand) {
        for (FloatingItem floatingItem : floatingItems) {
            if (floatingItem.getArmorStand().getEntityId() == armorStand.getEntityId())
                return floatingItem;
        }

        return null;
    }

    public static List<FloatingItem> getFloatingItems() {
        return floatingItems;
    }

    public static abstract class PickUpAction {

        public abstract void pickUp(PlayerPickupItemEvent event, OMPlayer player, FloatingItem item);

    }

    public static abstract class ClickAction {

        public abstract void click(PlayerInteractAtEntityEvent event, OMPlayer player, FloatingItem item);

    }

    public static abstract class ItemInstance {

        private FloatingItem floatingItem;
        private Item item;
        private boolean hideOnJoin;
        private List<Player> watchers;

        public ItemInstance(FloatingItem floatingItem, boolean hideOnJoin) {
            this.floatingItem = floatingItem;
            this.hideOnJoin = hideOnJoin;
            this.watchers = new ArrayList<>();
            this.floatingItem.itemInstances.add(this);
        }

        public abstract ItemStack getItemStack();

        public abstract String getDisplayName();

        public abstract boolean isDisplayNameVisible();

        public FloatingItem getFloatingItem() {
            return floatingItem;
        }

        public Item getItem() {
            return item;
        }

        public boolean hideOnJoin() {
            return hideOnJoin;
        }

        public void spawn() {
            spawn((Collection<? extends Player>) null);
        }

        public void spawn(Player... createFor) {
            spawn(Arrays.asList(createFor));
        }

        public void spawn(Collection<? extends Player> createFor) {
            if (watchers.size() != 0)
                createFor = watchers;

            item = floatingItem.location.getWorld().dropItem(floatingItem.location, getItemStack());

            if (!floatingItem.canPickUp())
                item.setPickupDelay(Integer.MAX_VALUE);

            item.setCustomName(getDisplayName());
            item.setCustomNameVisible(isDisplayNameVisible());

            floatingItem.armorStand.addPassenger(item);

            if (createFor == null)
                return;

            watchers.addAll(createFor);

            createForWatchers();
        }

        public void delete() {
            if (item != null)
                item.remove();
        }

        public void createForWatchers() {
            List<Player> hideFor = new ArrayList<>();
            for (Player player : floatingItem.location.getWorld().getPlayers()) {
                if (!watchers.contains(player))
                    hideFor.add(player);
            }

            hideFor(hideFor);
        }

        public List<Player> getWatchers() {
            return watchers;
        }

        public void hideFor(Player player) {
            hideFor(Collections.singletonList(player));
        }

        public void hideFor(Collection<? extends Player> players) {
            OrbitMinesApi.getApi().getNms().entity().destroyEntityFor(players, item);
        }

        public void updateItemStack() {
            item.setItemStack(getItemStack());
        }

        public void updateName() {
            item.setCustomName(getDisplayName());
            item.setCustomNameVisible(isDisplayNameVisible());

            createForWatchers();
        }
    }
}
