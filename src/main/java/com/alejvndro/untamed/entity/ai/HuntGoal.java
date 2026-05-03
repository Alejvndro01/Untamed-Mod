package com.alejvndro.untamed.entity.ai;

import com.alejvndro.untamed.entity.PreyEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;

public class HuntGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    public HuntGoal(Animal predator, Class<T> targetClass, boolean checkSight) {
        super(predator, targetClass, checkSight);
    }

    @Override
    public boolean canUse() {
        // Solo cazan si no están domesticados o si tienen hambre (lógica futura)
        return super.canUse();
    }
}