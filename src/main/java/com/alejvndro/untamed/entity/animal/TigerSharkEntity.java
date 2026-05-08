package com.alejvndro.untamed.entity.animal;

import com.alejvndro.untamed.entity.ai.TigerSharkAi;
import com.alejvndro.untamed.entity.ai.controls.move.TigerSharkMoveControl;
import com.alejvndro.untamed.entity.ai.controls.navigation.TigerSharkPathNavigation;
import com.alejvndro.untamed.registry.Untamed_Sensors;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TigerSharkEntity extends WaterAnimal implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(TigerSharkEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(TigerSharkEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final ImmutableList<? extends SensorType<? extends Sensor<? super TigerSharkEntity>>> SENSOR_TYPES = 
        ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, 
            SensorType.NEAREST_PLAYERS, 
            SensorType.HURT_BY,
            Untamed_Sensors.TIGER_SHARK_CONDUIT_PLAYER.get(),
            Untamed_Sensors.TIGER_SHARK_NEAREST_CONDUIT.get()
        );

    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = 
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

    public TigerSharkEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new TigerSharkMoveControl(this);
        this.lookControl = new net.minecraft.world.entity.ai.control.LookControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VARIANT_ID, 0);
        this.entityData.define(IS_ATTACKING, false);
    }

    public int getVariant() { return this.entityData.get(DATA_VARIANT_ID); }
    public void setVariant(int variant) { this.entityData.set(DATA_VARIANT_ID, variant); }
    public boolean isAttacking() { return this.entityData.get(IS_ATTACKING); }
    public void setAttacking(boolean attacking) { this.entityData.set(IS_ATTACKING, attacking); }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag tag) {
        BlockPos pos = this.blockPosition();
        Holder<Biome> holder = level.getBiome(pos);
        float temperatura = holder.get().getBaseTemperature();
        this.setVariant(temperatura > 0.9F ? 1 : 0);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, tag);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                // REDUCIDO: Bajamos a 0.5D para que no sea incontrolable
                .add(Attributes.MOVEMENT_SPEED, 0.5D) 
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    protected Brain.Provider<TigerSharkEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return TigerSharkAi.makeBrain((Brain<TigerSharkEntity>) this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<TigerSharkEntity> getBrain() {
        return (Brain<TigerSharkEntity>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("tigerSharkBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();
        TigerSharkAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            
            // FRICCIÓN AUMENTADA: 0.6D hace que el agua lo frene mucho más rápido
            this.setDeltaMovement(this.getDeltaMovement().scale(0.6D)); 
            
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.002D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "locomotion", 5, event -> {
            if (event.isMoving() && this.isInWater()) {
                String anim = this.isAggressive() ? "animation.tiger_shark.swim_fast" : "animation.tiger_shark.swim";
                return event.setAndContinue(RawAnimation.begin().thenLoop(anim));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.tiger_shark.idle"));
        }));

        controllers.add(new AnimationController<>(this, "attack_controller", 1, event -> {
            if (this.isAttacking()) {
                event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.tiger_shark.attack"));
                // Importante: No resetear el boolean aquí, dejar que el tick() lo haga
                return PlayState.CONTINUE;
            }
            event.getController().forceAnimationReset();
            return PlayState.STOP;
        }));
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        // Activamos la animación
        this.setAttacking(true);
        // Enviamos el evento de brazo/mordida estándar de Minecraft para asegurar el golpe
        this.level().broadcastEntityEvent(this, (byte) 4); 
        return super.doHurtTarget(target);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.setAttacking(true);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public void tick() {
        super.tick();
        // Reset del estado de ataque tras un breve periodo
        if (this.isAttacking() && this.tickCount % 10 == 0) {
            this.setAttacking(false);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }
    
    @Override
    public boolean canBreatheUnderwater() { return true; }
    
    @Override
    protected @NotNull net.minecraft.world.entity.ai.navigation.PathNavigation createNavigation(@NotNull Level level) {
        return new TigerSharkPathNavigation(this, level);
    }
}