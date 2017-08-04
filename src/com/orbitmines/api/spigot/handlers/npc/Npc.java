package com.orbitmines.api.spigot.handlers.npc;


import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.nms.Nms;
import com.orbitmines.api.spigot.utils.ConsoleUtils;
import com.orbitmines.api.spigot.utils.LocationUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fadi on 15-5-2016.
 */
public class Npc {

    private static List<Npc> npcs = new ArrayList<>();

    protected OrbitMinesApi api;
    private boolean fire;

    protected Mob mob;
    protected Location location;
    protected String displayName;
    protected Entity entity;

    private ItemStack itemInHand;
    private ItemStack helmet;
    private ItemStack chestPlate;
    private ItemStack leggings;
    private ItemStack boots;

    private InteractAction interactAction;

    public Npc(Mob mob, Location location, String displayName) {
        this(mob, location, displayName, null);
    }

    public Npc(Mob mob, Location location, String displayName, InteractAction interactAction) {
        npcs.add(this);
        api = OrbitMinesApi.getApi();

        this.mob = mob;
        this.location = location;
        this.displayName = displayName;
        this.interactAction = interactAction;
        this.fire = false;

        spawn();
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.mob = mob;

        spawn();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;

        entity.setCustomName(displayName);
        entity.setCustomNameVisible(true);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;

        remove();
        spawn();
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isFire() {
        return fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public Location getFixedLocation() {
        return mob == Mob.BAT || mob == Mob.SQUID || mob == Mob.GUARDIAN ? LocationUtils.asNewLocation(location, 0, 1, 0) : location;
    }

    public void spawn() {
        remove();

        Chunk c = location.getChunk();
        c.load();
        if (!api.getChunks().contains(c))
            api.getChunks().add(c);

        if (this instanceof NpcMoving)
            this.entity = mob.spawnMoving(getFixedLocation(), displayName);
        else
            this.entity = mob.spawn(getFixedLocation(), displayName);

        if (entity != null) {
//            for(Entity en : entity.getNearbyEntities(0.1, 0.1, 0.1)){
//                if(en instanceof Player || en instanceof ArmorStand && (Hologram.getHologram((ArmorStand) en) != null || NpcArmorStand.getNPCArmorStand((ArmorStand) en) != null) || Npc.getNpc(en) != null)
//                    continue;
//
//                en.remove();
//            }
        } else {
            ConsoleUtils.warn("Error while spawning Mob " + mob.toString() + "!");
        }
    }

    public void remove() {
        if (entity != null)
            entity.remove();
    }

    public void click(PlayerInteractEntityEvent event, OMPlayer omp) {
        if (interactAction != null)
            interactAction.click(event, omp);
    }

    public void checkEntity() {
        if (entity == null)
            return;

        if (entity.isDead() || !entity.isValid()) {
            spawn();
            setItemInHand(itemInHand);
            setHelmet(helmet);
            setChestplate(chestPlate);
            setLeggings(leggings);
            setBoots(boots);
        }

        entity.setTicksLived(1);

        if (!isFire())
            entity.setFireTicks(0);
        else
            entity.setFireTicks(Integer.MAX_VALUE);

        Location l = getFixedLocation();
        if (!(this instanceof NpcMoving) && entity.getLocation().distance(l) >= 0.1)
            entity.teleport(l);
    }

    public void setItemInHand(ItemStack item) {
        itemInHand = item;
        ((LivingEntity) entity).getEquipment().setItemInMainHand(item);
    }

    public void setHelmet(ItemStack item) {
        helmet = item;
        ((LivingEntity) entity).getEquipment().setHelmet(item);
    }

    public void setChestplate(ItemStack item) {
        chestPlate = item;
        ((LivingEntity) entity).getEquipment().setChestplate(item);
    }

    public void setLeggings(ItemStack item) {
        leggings = item;
        ((LivingEntity) entity).getEquipment().setLeggings(item);
    }

    public void setBoots(ItemStack item) {
        boots = item;
        ((LivingEntity) entity).getEquipment().setBoots(item);
    }

    public void setVillagerProfession(Villager.Profession villagerProfession) {
        if (entity != null && getMob() == Mob.VILLAGER)
            ((Villager) entity).setProfession(villagerProfession);
    }

    public static Npc getNpc(Entity entity) {
        for (Npc npc : npcs) {
            if (npc.entity == entity)
                return npc;
        }

        return null;
    }

    public static List<Npc> getNpcs() {
        return npcs;
    }

    public static abstract class InteractAction {

        public abstract void click(PlayerInteractEntityEvent event, OMPlayer omp);

    }
}
