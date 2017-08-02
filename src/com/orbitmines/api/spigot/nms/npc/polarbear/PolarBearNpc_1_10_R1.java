package com.orbitmines.api.spigot.nms.npc.polarbear;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_10_R1;
import com.orbitmines.api.spigot.nms.npc.polarbear.custom.EntityPolarBear_1_10_R1;
import net.minecraft.server.v1_10_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPolarBear;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class PolarBearNpc_1_10_R1 implements PolarBearNpc {

    public PolarBearNpc_1_10_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityPolarBear_1_10_R1.class, "CustomPolarBear", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityPolarBear_1_10_R1 e = new EntityPolarBear_1_10_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftPolarBear) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_10_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}