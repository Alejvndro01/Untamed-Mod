package com.alejvndro.untamed.entity;

import com.alejvndro.untamed.entity.ai.UntamedPanicGoal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class PreyEntity extends BaseAnimalEntity {
    protected PreyEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
protected void registerGoals() {
    this.goalSelector.addGoal(1, new UntamedPanicGoal(this, 2.0D));
    this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal(this, 1.0D));
    this.goalSelector.addGoal(4, new net.minecraft.world.entity.ai.goal.LookAtPlayerGoal(this, net.minecraft.world.entity.player.Player.class, 6.0F));
}
}