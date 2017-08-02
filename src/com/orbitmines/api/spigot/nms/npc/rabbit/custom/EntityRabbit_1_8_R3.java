package com.orbitmines.api.spigot.nms.npc.rabbit.custom;

import com.orbitmines.api.spigot.nms.npc.NpcNms_1_8_R3;
import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

import java.util.List;

/**
 * Created by Fadi on 30-4-2016.
 */
public class EntityRabbit_1_8_R3 extends EntityRabbit {

    private boolean moving;

    public EntityRabbit_1_8_R3(World world) {
        super(world);
    }

    public EntityRabbit_1_8_R3(World world, boolean moving, boolean noAttack) {
        super(world);

        this.moving = moving;

        List targetB = (List) NpcNms_1_8_R3.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
        targetB.clear();

        if (noAttack)
            return;

        List goalB = (List) NpcNms_1_8_R3.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
        goalB.clear();
        List goalC = (List) NpcNms_1_8_R3.getPrivateField("c", PathfinderGoalSelector.class, this.goalSelector);
        goalC.clear();
        List targetC = (List) NpcNms_1_8_R3.getPrivateField("c", PathfinderGoalSelector.class, this.targetSelector);
        targetC.clear();
    }

    @Override
    public void g(double x, double y, double z) {
    }

    @Override
    public void m() {
        super.m();

        if (moving)
            return;

        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Double.MAX_VALUE);
        this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
    }
}
