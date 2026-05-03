package com.alejvndro.untamed.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import java.util.List;

public class FlockGoal extends Goal {
    private final PathfinderMob mob;
    private final double speed;
    private final float range = 8.0f;

    public FlockGoal(PathfinderMob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        List<? extends PathfinderMob> list = this.mob.level().getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(range));
        return list.size() > 1; // Se activa si hay compañeros cerca
    }

    @Override
    public void tick() {
        // Lógica para seguir al "líder" o moverse al centro del grupo
    }
}