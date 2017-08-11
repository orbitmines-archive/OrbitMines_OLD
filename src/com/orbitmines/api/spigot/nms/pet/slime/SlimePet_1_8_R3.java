package com.orbitmines.api.spigot.nms.pet.slime;

import com.orbitmines.api.spigot.Mob;
import com.orbitmines.api.spigot.OrbitMinesApi;
import com.orbitmines.api.spigot.nms.npc.NpcNms_1_8_R3;
import com.orbitmines.api.spigot.nms.pet.Pet_1_8_R3;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSlime;
import org.bukkit.entity.Entity;

import java.util.List;

/**
 * Created by Fadi on 30-4-2016.
 */
public class SlimePet_1_8_R3 implements SlimePet {

    public SlimePet_1_8_R3() {
        OrbitMinesApi.getApi().getNms().npc().addCustomEntity(CustomNPC.class, "CustomSlime", Mob.SLIME.getEggId());
    }

    @Override
    public Entity spawn(Location location, String displayName) {
        World nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        CustomNPC e = new CustomNPC(nmsWorld);
        e.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        nmsWorld.addEntity(e);
        e.setCustomName(displayName);
        e.setCustomNameVisible(true);
        ((CraftSlime) e.getBukkitEntity()).setRemoveWhenFarAway(false);

        return e.getBukkitEntity();
    }

    public class CustomNPC extends EntitySlime {

        private Pet_1_8_R3 pI;

        public CustomNPC(World world) {
            super(world);

            List goalB = (List) NpcNms_1_8_R3.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
            goalB.clear();
            List targetB = (List) NpcNms_1_8_R3.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
            targetB.clear();

            this.pI = new Pet_1_8_R3() {
                @Override
                protected void setYawPitchPet(float yaw, float pitch) {
                    setYawPitch(yaw, pitch);
                }
            };
        }

        @Override
        public void g(float sideMot, float forMot) {
            if (this.passenger == null || !(this.passenger instanceof EntityHuman)) {
                super.g(sideMot, forMot);
                this.S = 0.5F;
                return;
            }

            float[] f = pI.handleMovement(this, sideMot, forMot, 0.35F);
            super.g(f[0], f[1]);

            pI.handleJump(this);
        }
    }
}
