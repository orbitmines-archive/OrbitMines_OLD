package com.orbitmines.api.spigot.nms.npc.silverfish;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_10_R1;
import com.orbitmines.api.spigot.nms.npc.silverfish.custom.EntitySilverfish_1_10_R1;
import net.minecraft.server.v1_10_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftSilverfish;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SilverfishNpc_1_10_R1 implements SilverfishNpc {

    public SilverfishNpc_1_10_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntitySilverfish_1_10_R1.class, "CustomSilverfish", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntitySilverfish_1_10_R1 e = new EntitySilverfish_1_10_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftSilverfish) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving) {
            e.setNoGravity(true);
            NpcNms_1_10_R1.setNoAI(e.getBukkitEntity());
        }

        return e.getBukkitEntity();
    }
}
