package com.orbitmines.api.spigot.nms.npc.chicken;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_8_R3;
import com.orbitmines.api.spigot.nms.npc.chicken.custom.EntityChicken_1_8_R3;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftChicken;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class ChickenNpc_1_8_R3 implements ChickenNpc {

    public ChickenNpc_1_8_R3() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityChicken_1_8_R3.class, "CustomChicken", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityChicken_1_8_R3 e = new EntityChicken_1_8_R3(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftChicken) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R3.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
