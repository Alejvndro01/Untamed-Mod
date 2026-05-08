package com.alejvndro.untamed.entity.ai.behavior.shark;

import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;

public class SharkJumpOutWater extends Behavior<TigerSharkEntity> {
    public SharkJumpOutWater() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 20);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, TigerSharkEntity shark) {
        return shark.isInWater() && shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).map(target -> 
            !target.isInWater() && target.getY() > shark.getY()
        ).orElse(false);
    }

    @Override
    protected void start(ServerLevel level, TigerSharkEntity shark, long gameTime) {
        LivingEntity target = shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        Vec3 direction = (new Vec3(target.getX() - shark.getX(), target.getY() - shark.getY(), target.getZ() - shark.getZ())).normalize();
        
        // Impulso hacia afuera del agua
        shark.setDeltaMovement(direction.scale(0.8D).add(0, 0.25D, 0));
        shark.hasImpulse = true;
    }
}