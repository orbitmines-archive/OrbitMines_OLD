package com.orbitmines.api.spigot.nms.npc.magmacube;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_9_R2;
import com.orbitmines.api.spigot.nms.npc.magmacube.custom.EntityMagmaCube_1_9_R2;
import net.minecraft.server.v1_9_R2.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftMagmaCube;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class MagmaCubeNpc_1_9_R2 implements MagmaCubeNpc {

    public MagmaCubeNpc_1_9_R2() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityMagmaCube_1_9_R2.class, "CustomMagmaCube", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityMagmaCube_1_9_R2 e = new EntityMagmaCube_1_9_R2(nmsWorld, moving, noAttack);
        ((CraftMagmaCube) e.getBukkitEntity()).setSize(2);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftMagmaCube) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_9_R2.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
