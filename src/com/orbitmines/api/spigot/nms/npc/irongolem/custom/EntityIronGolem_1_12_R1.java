package com.orbitmines.api.spigot.nms.npc.irongolem.custom;

import com.orbitmines.api.spigot.nms.npc.NpcNms_1_12_R1;
import net.minecraft.server.v1_12_R1.EntityIronGolem;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import net.minecraft.server.v1_12_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_12_R1.World;

import java.util.LinkedHashSet;

/**
 * Created by Fadi on 30-4-2016.
 */
public class EntityIronGolem_1_12_R1 extends EntityIronGolem {

    private boolean moving;

    public EntityIronGolem_1_12_R1(World world) {
        super(world);
    }

    public EntityIronGolem_1_12_R1(World world, boolean moving, boolean noAttack) {
        super(world);

        this.moving = moving;

        LinkedHashSet targetB = (LinkedHashSet) NpcNms_1_12_R1.getPrivateField("b", PathfinderGoalSelector.class, this.targetSelector);
        targetB.clear();

        if (noAttack)
            return;

        LinkedHashSet goalB = (LinkedHashSet) NpcNms_1_12_R1.getPrivateField("b", PathfinderGoalSelector.class, this.goalSelector);
        goalB.clear();
        LinkedHashSet goalC = (LinkedHashSet) NpcNms_1_12_R1.getPrivateField("c", PathfinderGoalSelector.class, this.goalSelector);
        goalC.clear();
        LinkedHashSet targetC = (LinkedHashSet) NpcNms_1_12_R1.getPrivateField("c", PathfinderGoalSelector.class, this.targetSelector);
        targetC.clear();
    }

    @Override
    public void f(double x, double y, double z) {
    }

    @Override
    public void B_() {
        super.B_();

        if (moving)
            return;

        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Double.MAX_VALUE);
        this.getAttributeInstance(GenericAttributes.c).setValue(Double.MAX_VALUE);
    }
}