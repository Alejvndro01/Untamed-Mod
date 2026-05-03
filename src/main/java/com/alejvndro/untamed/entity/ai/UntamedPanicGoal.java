package com.alejvndro.untamed.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;

public class UntamedPanicGoal extends PanicGoal {
    public UntamedPanicGoal(PathfinderMob mob, double speedModifier) {
        super(mob, speedModifier);
    }
    // Aquí podrías añadir lógica para que busquen agua o se escondan en arbustos
}