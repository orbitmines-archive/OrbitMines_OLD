package com.orbitmines.api.spigot.nms.armorstand;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ArmorStandNms_1_8_R2 implements ArmorStandNms {

    @Override
    public ArmorStand spawn(Location location, boolean visible) {
        ArmorStand as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        ((CraftArmorStand) as).getHandle().setInvisible(!visible);
        return as;
    }
}
