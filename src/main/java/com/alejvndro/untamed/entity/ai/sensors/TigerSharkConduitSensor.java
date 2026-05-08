package com.alejvndro.untamed.entity.ai.sensors;

import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import java.util.Set;

public class TigerSharkConduitSensor extends Sensor<TigerSharkEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    @Override
    protected void doTick(ServerLevel level, TigerSharkEntity shark) {
        shark.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent(list -> {
            LivingEntity target = list.findClosest(e -> e.hasEffect(MobEffects.CONDUIT_POWER)).orElse(null);
            if (target != null) {
                shark.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
                shark.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new net.minecraft.world.entity.ai.behavior.EntityTracker(target, true));
            }
        });
    }
}