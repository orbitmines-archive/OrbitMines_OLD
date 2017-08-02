package com.orbitmines.api.spigot.nms.npc.wither;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_8_R2;
import com.orbitmines.api.spigot.nms.npc.wither.custom.EntityWither_1_8_R2;
import net.minecraft.server.v1_8_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftWither;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class WitherNpc_1_8_R2 implements WitherNpc {

    public WitherNpc_1_8_R2() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityWither_1_8_R2.class, "CustomWither", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityWither_1_8_R2 e = new EntityWither_1_8_R2(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftWither) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R2.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
