package com.orbitmines.api.spigot.nms.npc.wolf;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public interface WolfNpc {

    int Id = 95;

    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack);

}
