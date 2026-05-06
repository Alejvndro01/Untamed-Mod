package com.alejvndro.untamed.entity;

import com.alejvndro.untamed.registry.Untamed_Items;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ButterflyEntity extends Animal implements GeoEntity {
    // Definición de animaciones de butterfly.animation.json[cite: 1]
    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("animation.butterfly.fly");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.butterfly.idle");
    protected static final RawAnimation POLLINATE_ANIM = RawAnimation.begin().thenLoop("animation.butterfly.pollinate");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    
    private int pollinateTicks = 0;
    @Nullable
    private BlockPos flowerPos = null;

    public ButterflyEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.FLYING_SPEED, 0.8D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false; // Inmune al daño de caída[cite: 1]
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        // Ignora cálculos de caída[cite: 1]
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ButterflyPollinateGoal(this));
        // Prioridad al vuelo para que no pase mucho tiempo en el suelo[cite: 1]
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        return navigation;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHit) {
        super.dropCustomDeathLoot(source, looting, recentlyHit);
        // Drop raro: 10% de probabilidad de soltar escamas[cite: 1]
        if (this.random.nextFloat() < 0.10f) {
            this.spawnAtLocation(Untamed_Items.MORPHO_WING_SCALE.get());
        }
    }

    // --- Gestión de Animaciones GeckoLib ---

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::deployAnimation));
    }

    private PlayState deployAnimation(AnimationState<ButterflyEntity> state) {
        if (this.isPollinating()) {
            return state.setAndContinue(POLLINATE_ANIM);
        }

        Vec3 velocity = this.getDeltaMovement();
        if (velocity.horizontalDistanceSqr() > 0.002 || !this.onGround()) {
            return state.setAndContinue(FLY_ANIM);
        }

        return state.setAndContinue(IDLE_ANIM);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean isPollinating() {
        return this.pollinateTicks > 0;
    }

    // --- IA de Polinización ---

    static class ButterflyPollinateGoal extends Goal {
        private final ButterflyEntity butterfly;
        private int timer = 0;

        public ButterflyPollinateGoal(ButterflyEntity butterfly) {
            this.butterfly = butterfly;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (butterfly.pollinateTicks > 0) return false;
            
            if (butterfly.getRandom().nextInt(60) == 0) {
                BlockPos pos = butterfly.blockPosition();
                for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-5, -3, -5), pos.offset(5, 3, 5))) {
                    if (butterfly.level().getBlockState(nearbyPos).getBlock() instanceof FlowerBlock) {
                        butterfly.flowerPos = nearbyPos;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void tick() {
            if (butterfly.flowerPos != null) {
                // Navegación baja para que se pose sobre los pétalos (+0.1D)[cite: 1]
                butterfly.getNavigation().moveTo(
                    butterfly.flowerPos.getX() + 0.5D, 
                    butterfly.flowerPos.getY() + 0.1D, 
                    butterfly.flowerPos.getZ() + 0.5D, 
                    1.1D
                );

                // Detección de contacto con la flor[cite: 1]
                if (butterfly.distanceToSqr(butterfly.flowerPos.getX() + 0.5, butterfly.flowerPos.getY() + 0.2, butterfly.flowerPos.getZ() + 0.5) < 0.4D) {
                    butterfly.setDeltaMovement(Vec3.ZERO);
                    butterfly.pollinateTicks = 80;
                    timer++;

                    if (timer >= 80) {
                        // Drop corregido: Monarch Pollen[cite: 1]
                        butterfly.spawnAtLocation(Untamed_Items.POLLEN.get());
                        butterfly.pollinateTicks = 0;
                        butterfly.flowerPos = null;
                        timer = 0;
                    }
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return butterfly.flowerPos != null && timer < 85;
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("PollinateTicks", this.pollinateTicks);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.pollinateTicks = compound.getInt("PollinateTicks");
    }

    @Override
    public ButterflyEntity getBreedOffspring(net.minecraft.server.level.ServerLevel level, net.minecraft.world.entity.AgeableMob mob) {
        return null;
    }
}