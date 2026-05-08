package com.alejvndro.untamed.entity.ai.behavior.shark;

import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.effect.MobEffects;

public class SharkFollowConduitPlayer extends Behavior<TigerSharkEntity> {
    public SharkFollowConduitPlayer() {
        super(ImmutableMap.of(
                MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, TigerSharkEntity shark) {
        return shark.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }

    @Override
    protected void start(ServerLevel level, TigerSharkEntity shark, long gameTime) {
        shark.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(target -> {
            if (target instanceof EntityTracker tracker) {
                if (tracker.getEntity() instanceof LivingEntity player) {
                    if (player.hasEffect(MobEffects.CONDUIT_POWER)) {
                        // CORRECCIÓN: Envolvemos el tracker en un WalkTarget (Velocidad 1.2F, llega a 3 bloques)
                        shark.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(player.blockPosition()), 1.2F, 3));
                    }
                }
            }
        });
    }
}