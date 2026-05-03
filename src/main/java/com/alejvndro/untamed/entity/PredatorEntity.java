package com.alejvndro.untamed.entity;

import com.alejvndro.untamed.entity.ai.HuntGoal;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class PredatorEntity extends BaseAnimalEntity {
    protected PredatorEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(1, new HuntGoal<>(this, PreyEntity.class, true));
        this.goalSelector.addGoal(2, new net.minecraft.world.entity.ai.goal.MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(3, new net.minecraft.world.entity.ai.goal.RandomStrollGoal(this, 1.0D));
    }
}