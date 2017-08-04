package com.orbitmines.api.spigot.nms.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface EntityNms {

    void setInvisible(Player player, boolean bl);

    void destoryEntity(Player player, Entity entity);

    void setSpeed(Entity entity, double d);

    double getSpeed(Entity entity);

    void disguiseAsBlock(Player player, int Id, Player... players);

    Entity disguiseAsMob(Player player, EntityType type, boolean baby, String displayName, Player... players);

    Entity disguiseAsMob(Player player, EntityType type, boolean baby, Player... players);

    void mountUpdate(Entity vehicle);

    void navigate(LivingEntity le, Location location, double v);
}
