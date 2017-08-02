package com.orbitmines.api.spigot.nms.npc.pig;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_9_R1;
import com.orbitmines.api.spigot.nms.npc.pig.custom.EntityPig_1_9_R1;
import net.minecraft.server.v1_9_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPig;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class PigNpc_1_9_R1 implements PigNpc {

    public PigNpc_1_9_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityPig_1_9_R1.class, "CustomPig", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityPig_1_9_R1 e = new EntityPig_1_9_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftPig) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_9_R1.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
