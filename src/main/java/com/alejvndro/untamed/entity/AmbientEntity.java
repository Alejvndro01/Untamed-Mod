package com.alejvndro.untamed.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class AmbientEntity extends BaseAnimalEntity {
    protected AmbientEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }
    
    // Los animales ambientales suelen ser pasivos y no atacan
}