package com.orbitmines.api.spigot.nms.pet.creeper;

import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_10_R1;
import com.orbitmines.api.spigot.nms.pet.Pet_1_10_R1;
import net.minecraft.server.v1_10_R1.EntityCreeper;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftCreeper;
import org.bukkit.entity.Entity;

import java.util.LinkedHashSet;

/**
 * Created by Fadi on 30-4-2016.
 */
public class CreeperPet_1_10_R1 implements CreeperPet {

    public CreeperPet_1_10_R1() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(CustomNPC.class, "CustomCreeper", Mob.CREEPER.getEggId());
    }

    @Override
    public Entity spawn(Location location, String displayName) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        CustomNPC e = new CustomNPC(nmsWorld);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftCreeper) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        return e.getBukkitEntity();
    }

    public class CustomNPC extends EntityCreeper {

        private Pet_1_10_R1 pI;

        public CustomNPC(World world) {
            super(world);

            LinkedHashSet goalB = (LinkedHashSet) NpcNms_1_10_R1.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
            goalB.clear();
            LinkedHashSet targetB = (LinkedHashSet) NpcNms_1_10_R1.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
            targetB.clear();

            this.pI = new Pet_1_10_R1() {
                @Override
                protected void setYawPitchPet(float yaw, float pitch) {
                    setYawPitch(yaw, pitch);
                }
            };
        }

        @Override
        public void g(float sideMot, float forMot) {
            if (this.passengers.size() == 0 || !(this.passengers.get(0) instanceof EntityHuman)) {
                super.g(sideMot, forMot);
                this.P = 0.5F;
                return;
            }

            float[] f = pI.handleMovement(this, sideMot, forMot, 0.35F);
            super.g(f[0], f[1]);

            pI.handleJump(this);
        }
    }
}
