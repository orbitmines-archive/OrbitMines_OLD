package com.orbitmines.api.spigot.nms.npc.vindicator;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_11_R1;
import com.orbitmines.api.spigot.nms.npc.vindicator.custom.EntityVindicator_1_11_R1;
import net.minecraft.server.v1_11_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_11_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftVindicator;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class VindicatorNpc_1_11_R1 implements VindicatorNpc {

    public VindicatorNpc_1_11_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityVindicator_1_11_R1.class, "CustomVindicator", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityVindicator_1_11_R1 e = new EntityVindicator_1_11_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftVindicator) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_11_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}
