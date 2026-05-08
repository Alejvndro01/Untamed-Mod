package com.alejvndro.untamed.entity.ai.behavior.shark;

import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class SharkGoesToConduit extends Behavior<TigerSharkEntity> {
    public SharkGoesToConduit() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.NEAREST_REPELLENT, MemoryStatus.VALUE_PRESENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT
        ));
    }

    @Override
    protected void start(ServerLevel level, TigerSharkEntity shark, long gameTime) {
        shark.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT).ifPresent(pos -> {
            // CORRECCIÓN: Envolvemos el tracker en un WalkTarget (Velocidad 1.0F, llega a 2 bloques)
            shark.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(pos), 1.0F, 2));
        });
    }
}