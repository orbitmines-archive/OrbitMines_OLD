package com.orbitmines.api.spigot.perks;

import com.orbitmines.api.spigot.Color;
import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.handlers.ItemSet;
import com.orbitmines.api.spigot.handlers.MobItemSet;
import com.orbitmines.api.spigot.handlers.OMPlayer;
import com.orbitmines.api.spigot.handlers.Obtainable;
import com.orbitmines.api.spigot.handlers.gadget.petride.PetHandler;
import com.orbitmines.api.spigot.nms.Nms;
import com.orbitmines.api.spigot.nms.entity.EntityNms;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/*
* OrbitMines - @author Fadi Shawki - 7/28/2017
*/
public enum Pet implements Perk {

    MUSHROOM_COW(Mob.MUSHROOM_COW, Color.MAROON, new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    PIG(Mob.PIG, Color.FUCHSIA, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    WOLF(Mob.WOLF, Color.SILVER, new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    SHEEP(Mob.SHEEP, Color.WHITE, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    HORSE(Mob.HORSE, Color.ORANGE, new Obtainable(OrbitMinesApi.VIP_POINTS, 525)),
    MAGMA_CUBE(Mob.MAGMA_CUBE, Color.RED, new Obtainable(OrbitMinesApi.VIP_POINTS, 500)),
    SLIME(Mob.SLIME, Color.LIME, new Obtainable(OrbitMinesApi.VIP_POINTS, 475)),
    COW(Mob.COW, Color.GRAY, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    SILVERFISH(Mob.SILVERFISH, Color.SILVER, new Obtainable(OrbitMinesApi.VIP_POINTS, 450)),
    OCELOT(Mob.OCELOT, Color.YELLOW, new Obtainable(OrbitMinesApi.VIP_POINTS, 450)),
    CREEPER(Mob.CREEPER, Color.LIME, new Obtainable(OrbitMinesApi.VIP_POINTS, 425)),
    SQUID(Mob.SQUID, Color.BLUE, new Obtainable(OrbitMinesApi.VIP_POINTS, 600)),
    SPIDER(Mob.SPIDER, Color.GRAY, new Obtainable(OrbitMinesApi.VIP_POINTS, 525)),
    CHICKEN(Mob.CHICKEN, Color.SILVER, new Obtainable(OrbitMinesApi.VIP_POINTS, 425));

    private static List<Entity> entities = new ArrayList<>();

    private final Mob mob;
    private final Color color;

    private final MobItemSet item;

    private final Obtainable obtainable;

    private PetHandler petHandler;

    Pet(Mob mob, Color color, Obtainable obtainable) {
        this.mob = mob;
        this.color = color;
        this.item = new MobItemSet(mob);
        this.obtainable = obtainable;
    }

    public Mob getMob() {
        return mob;
    }

    public Color color() {
        return color;
    }

    @Override
    public ItemSet item() {
        return item;
    }

    @Override
    public Obtainable obtainable() {
        return obtainable;
    }

    @Override
    public String getDisplayName() {
        return color.getChatColor() + mob.getName() + " Pet";
    }

    @Override
    public boolean hasAccess(OMPlayer omp) {
        return omp.pets().hasPet(this);
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    public PetHandler getHandler() {
        return petHandler;
    }

    public void setHandler(PetHandler petHandler) {
        this.petHandler = petHandler;
    }

    public Entity spawn(OMPlayer omp) {
        Location location = omp.getPlayer().getLocation();
        String displayName = omp.pets().getPetName(this);

        Entity entity = null;
        Nms nms = OrbitMinesApi.getApi().getNms();
        switch (this) {
            case CHICKEN:
                entity = nms.getChickenPet().spawn(location, displayName);
                break;
            case COW:
                entity = nms.getCowPet().spawn(location, displayName);
                break;
            case CREEPER:
                entity = nms.getCreeperPet().spawn(location, displayName);
                break;
            case HORSE:
                Horse horse = (Horse) location.getWorld().spawnEntity(location, EntityType.HORSE);
                horse.setAdult();
                horse.setTamed(true);
                horse.setRemoveWhenFarAway(false);
                horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                horse.setColor(Horse.Color.BROWN);
                horse.setStyle(Horse.Style.WHITE);

                nms.entity().setAttribute(horse, EntityNms.Attribute.MOVEMENT_SPEED, 0.25);

                horse.setCustomName(displayName);
                horse.setCustomNameVisible(true);
                entity = horse;
                break;
            case MAGMA_CUBE:
                entity = nms.getMagmaCubePet().spawn(location, displayName);
                break;
            case MUSHROOM_COW:
                entity = nms.getMushroomCowPet().spawn(location, displayName);
                break;
            case OCELOT:
                entity = nms.getOcelotPet().spawn(location, displayName);
                break;
            case PIG:
                entity = nms.getPigPet().spawn(location, displayName);
                break;
            case SHEEP:
                entity = nms.getSheepPet().spawn(location, displayName);
                break;
            case SILVERFISH:
                entity = nms.getSilverfishPet().spawn(location, displayName);
                break;
            case SLIME:
                entity = nms.getSlimePet().spawn(location, displayName);
                break;
            case SPIDER:
                entity = nms.getSpiderPet().spawn(location, displayName);
                break;
            case SQUID:
                entity = nms.getSquidPet().spawn(location, displayName);
                break;
            case WOLF:
                entity = nms.getWolfPet().spawn(location, displayName);
                break;
        }

        if (entity instanceof Ageable) {
            ((Ageable) entity).setAdult();
        } else if (entity instanceof Slime) {
            ((Slime) entity).setSize(2);
        }

        return entity;
    }
}
