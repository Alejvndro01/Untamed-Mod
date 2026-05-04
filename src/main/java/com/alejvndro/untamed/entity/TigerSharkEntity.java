package com.alejvndro.untamed.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TigerSharkEntity extends WaterAnimal implements GeoEntity {

    // Animation definitions matching tiger_shark.animation.json
    private static final RawAnimation SWIM_ANIM =
            RawAnimation.begin().thenLoop("animation.tiger_shark.master_swim");
    private static final RawAnimation IDLE_ANIM =
            RawAnimation.begin().thenLoop("animation.tiger_shark.ocean_idle");
    private static final RawAnimation ATTACK_ANIM =
            RawAnimation.begin().thenLoop("animation.tiger_shark.dynamic_attack");
    private static final RawAnimation DEATH_ANIM =
            RawAnimation.begin().thenPlay("animation.tiger_shark.death_logic");
    private static final RawAnimation BREACH_ANIM =
            RawAnimation.begin().thenPlay("animation.tiger_shark.breach_jump");
    private static final RawAnimation STRUGGLE_ANIM =
            RawAnimation.begin().thenLoop("animation.tiger_shark.injured_struggle");
    private static final RawAnimation FRENZY_ANIM =
            RawAnimation.begin().thenLoop("animation.tiger_shark.sensory_frenzy");

    // Synched data so client can play the correct animation
    private static final EntityDataAccessor<Boolean> IS_ATTACKING =
            SynchedEntityData.defineId(TigerSharkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FRENZY =
            SynchedEntityData.defineId(TigerSharkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_INJURED =
            SynchedEntityData.defineId(TigerSharkEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    /** Ticks remaining for the injured-struggle animation after taking damage. */
    private int injuredTicks = 0;

    public TigerSharkEntity(EntityType<? extends WaterAnimal> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return WaterAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ATTACKING, false);
        this.entityData.define(IS_FRENZY, false);
        this.entityData.define(IS_INJURED, false);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        // Land → water
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        // Attack
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, true));
        // Wander
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0D, 40));
        // Look
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));

        // Retaliate against anything that hits it
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // Proactively hunt players
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        // Hunt nearby animals (fish, squid, etc.)
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PathfinderMob.class, true));
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            LivingEntity target = this.getTarget();

            // Attack state: targeting something within lunge range
            boolean attacking = target != null && this.distanceToSqr(target) < 25.0D;
            this.entityData.set(IS_ATTACKING, attacking);

            // Frenzy: below half health while still targeting prey
            boolean lowHealth = this.getHealth() < this.getMaxHealth() * 0.5f;
            this.entityData.set(IS_FRENZY, lowHealth && target != null);

            if (injuredTicks > 0) {
                injuredTicks--;
            }
            this.entityData.set(IS_INJURED, injuredTicks > 0);
        }
    }

    /**
     * Restrict the melee attack range so the shark only deals damage when its
     * body (teeth) physically overlaps the target's hitbox — no more "at-a-distance" hits.
     * Formula: (half-width of shark + half-width of target + small bite buffer)²
     */
    @Override
    public double getMeleeAttackRangeSqr(LivingEntity target) {
        float reach = (this.getBbWidth() + target.getBbWidth()) * 0.5F + 0.25F;
        return reach * reach;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide()) {
            injuredTicks = 60; // 3 seconds of struggle animation
        }
        return result;
    }

    // ── GeckoLib animation wiring ──────────────────────────────────────────────

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 5, this::handleAnimation));
    }

    private PlayState handleAnimation(AnimationState<TigerSharkEntity> state) {
        if (this.isDeadOrDying()) {
            return state.setAndContinue(DEATH_ANIM);
        }
        // Breach: shark is out of water (works on client via Entity.isInWater())
        if (!this.isInWater() && this.isAlive()) {
            return state.setAndContinue(BREACH_ANIM);
        }
        if (this.entityData.get(IS_INJURED)) {
            return state.setAndContinue(STRUGGLE_ANIM);
        }
        if (this.entityData.get(IS_FRENZY)) {
            return state.setAndContinue(FRENZY_ANIM);
        }
        if (this.entityData.get(IS_ATTACKING)) {
            return state.setAndContinue(ATTACK_ANIM);
        }
        if (state.isMoving()) {
            return state.setAndContinue(SWIM_ANIM);
        }
        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // ── NBT persistence ────────────────────────────────────────────────────────

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("InjuredTicks", injuredTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        injuredTicks = compound.getInt("InjuredTicks");
    }
}
