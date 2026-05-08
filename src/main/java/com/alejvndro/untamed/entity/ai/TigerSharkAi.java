package com.alejvndro.untamed.entity.ai;

import com.alejvndro.untamed.entity.ai.behavior.shark.SharkFollowConduitPlayer;
import com.alejvndro.untamed.entity.ai.behavior.shark.SharkGoesToConduit;
import com.alejvndro.untamed.entity.ai.behavior.shark.SharkJumpOutWater;
import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import com.alejvndro.untamed.registry.Untamed_Sensors;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

public class TigerSharkAi {
    private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);

    public static final ImmutableList<? extends SensorType<? extends Sensor<? super TigerSharkEntity>>> SENSOR_TYPES = 
        ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, 
            SensorType.NEAREST_PLAYERS, 
            SensorType.HURT_BY,
            Untamed_Sensors.TIGER_SHARK_CONDUIT_PLAYER.get(),
            Untamed_Sensors.TIGER_SHARK_NEAREST_CONDUIT.get()
        );

    public static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = 
        ImmutableList.of(
            MemoryModuleType.LOOK_TARGET, 
            MemoryModuleType.WALK_TARGET, 
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, 
            MemoryModuleType.PATH, 
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, 
            MemoryModuleType.ATTACK_TARGET, 
            MemoryModuleType.ATTACK_COOLING_DOWN, 
            MemoryModuleType.NEAREST_REPELLENT, 
            MemoryModuleType.AVOID_TARGET,
            MemoryModuleType.TEMPTING_PLAYER
        );

    public static Brain<TigerSharkEntity> makeBrain(Brain<TigerSharkEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initRetreatActivity(brain);
        
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<TigerSharkEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
            new LookAtTargetSink(45, 90), 
            new MoveToTargetSink(), 
            new Swim(0.7F), // Un poco más lento en CORE para control
            EraseMemoryIf.create(shark -> 
                shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() && 
                !shark.canAttack(shark.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get()), 
                MemoryModuleType.ATTACK_TARGET)
        ));
    }

    private static void initIdleActivity(Brain<TigerSharkEntity> brain) {
        brain.addActivity(Activity.IDLE, 10, ImmutableList.of(
            StartAttacking.create(TigerSharkAi::findNearestValidAttackTarget),
            new SharkFollowConduitPlayer(),
            new SharkGoesToConduit(),
            createIdleMovementBehaviors()
        ));
    }

    private static void initFightActivity(Brain<TigerSharkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
            new SharkJumpOutWater(),
            // Velocidad de persecución más agresiva
            SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.2F), 
            MeleeAttack.create(10), // Ataque más frecuente
            StopAttackingIfTargetInvalid.create()
        ), MemoryModuleType.ATTACK_TARGET);
    }

    private static void initRetreatActivity(Brain<TigerSharkEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
            new AnimalPanic(1.5F),
            createIdleMovementBehaviors()
        ), MemoryModuleType.AVOID_TARGET);
    }

    private static RunOne<TigerSharkEntity> createIdleMovementBehaviors() {
        return new RunOne<>(ImmutableList.of(
            Pair.of(RandomStroll.stroll(0.6F), 1),
            Pair.of(new DoNothing(30, 60), 1)
        ));
    }

    public static void updateActivity(TigerSharkEntity shark) {
        Brain<TigerSharkEntity> brain = shark.getBrain();
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.AVOID, Activity.FIGHT, Activity.IDLE));
        shark.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(TigerSharkEntity shark) {
        return shark.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                .flatMap(list -> list.findClosest(entity -> 
                    shark.canAttack(entity) && (
                        entity.getType() == EntityType.PLAYER || 
                        entity.getType() == EntityType.SALMON ||
                        entity.getType() == EntityType.SQUID ||
                        entity.getType() == EntityType.COD
                    )
                ));
    }
}