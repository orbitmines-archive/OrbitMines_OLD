package com.orbitmines.api.spigot.nms.pet;

import net.minecraft.server.v1_10_R1.EntityLiving;

import java.lang.reflect.Field;

/**
 * Created by Fadi on 14-7-2016.
 */
public abstract class Pet_1_10_R1 {

    protected abstract void setYawPitchPet(float yaw, float pitch);

    public float[] handleMovement(EntityLiving pet, float sideMot, float forMot, float speed){
        pet.lastYaw = pet.yaw = pet.passengers.get(0).yaw;
        pet.pitch = pet.passengers.get(0).pitch * 0.5F;

        setYawPitchPet(pet.yaw, pet.pitch);
        pet.aO = pet.aM = pet.yaw;

        pet.P = 1.0F;

        sideMot = ((EntityLiving) pet.passengers.get(0)).bf * 0.5F;
        forMot = ((EntityLiving) pet.passengers.get(0)).bg;

        if (forMot <= 0.0F) {
            forMot *= 0.3F;
        }
        sideMot *= 0.75F;

        pet.l(speed);

        float[] xy = { sideMot, forMot };
        return xy;
    }

    public void handleJump(EntityLiving pet){
        Field jump;
        try{
            jump = EntityLiving.class.getDeclaredField("be");
        }catch(NoSuchFieldException ex){
            ex.printStackTrace();
            return;
        }

        jump.setAccessible(true);

        try{
            if(pet.onGround && jump.getBoolean(pet.passengers.get(0))){
                double jumpHeight = 0.5D;
                pet.motY = jumpHeight;
            }
        }catch(IllegalAccessException ex){
            ex.printStackTrace();
        }
    }
}
