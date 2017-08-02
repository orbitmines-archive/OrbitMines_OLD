package com.orbitmines.api.spigot.nms.npc.horse;

import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_8_R1;
import com.orbitmines.api.spigot.nms.npc.horse.custom.EntityHorse_1_8_R1;
import net.minecraft.server.v1_8_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftHorse;
import org.bukkit.entity.Entity;

/**
 * Created by Fadi on 30-4-2016.
 */
public class HorseNpc_1_8_R1 implements HorseNpc {

    public HorseNpc_1_8_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(EntityHorse_1_8_R1.class, "CustomHorse", Id);
    }

    @Override
    public Entity spawn(Location location, String displayName, boolean moving, boolean noAttack) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        EntityHorse_1_8_R1 e = new EntityHorse_1_8_R1(nmsWorld, moving, noAttack);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftHorse) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        if (!moving)
            NpcNms_1_8_R1.setNoAI(e.getBukkitEntity());

        return e.getBukkitEntity();
    }
}
