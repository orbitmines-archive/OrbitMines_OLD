package com.orbitmines.api.spigot.nms.npc.sheep;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_8_R3;
import com.orbitmines.api.spigot.nms.npc.sheep.custom.EntitySheep_1_8_R3;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSheep;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SheepNpc_1_8_R3 implements SheepNpc {

    public SheepNpc_1_8_R3() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntitySheep_1_8_R3.class, "CustomSheep", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntitySheep_1_8_R3 e = new EntitySheep_1_8_R3(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftSheep) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R3.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
