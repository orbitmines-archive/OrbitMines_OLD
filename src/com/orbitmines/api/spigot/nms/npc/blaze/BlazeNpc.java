package com.orbitmines.api.spigot.nms.npc.blaze;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface BlazeNpc {

    int Id = 61;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
