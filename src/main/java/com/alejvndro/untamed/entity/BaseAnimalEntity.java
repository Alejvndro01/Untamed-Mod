package com.alejvndro.untamed.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseAnimalEntity extends Animal implements GeoEntity {
    // El cache es necesario para que GeckoLib gestione las animaciones de cada instancia
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected BaseAnimalEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Aquí registraremos las animaciones más adelante (idle, caminar, etc.)
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}