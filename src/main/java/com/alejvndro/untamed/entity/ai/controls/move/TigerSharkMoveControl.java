package com.alejvndro.untamed.entity.ai.controls.move;

import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;

public class TigerSharkMoveControl extends MoveControl {
    private final TigerSharkEntity shark;

    public TigerSharkMoveControl(TigerSharkEntity shark) {
        super(shark);
        this.shark = shark;
    }

    @Override
    public void tick() {
        if (this.shark.isInWater()) {
            this.shark.setDeltaMovement(this.shark.getDeltaMovement().add(0, 0.005, 0));
        }

        if (this.operation == Operation.MOVE_TO && !this.shark.getNavigation().isDone()) {
            double d0 = this.wantedX - this.shark.getX();
            double d1 = this.wantedY - this.shark.getY();
            double d2 = this.wantedZ - this.shark.getZ();
            double distanceSq = d0 * d0 + d1 * d1 + d2 * d2;

            if (distanceSq < 2.5E-7) {
                this.shark.setZza(0.0F);
            } else {
                float f = (float)(Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
                this.shark.setYRot(this.rotlerp(this.shark.getYRot(), f, 10.0F));
                float speed = (float)(this.speedModifier * this.shark.getAttributeValue(Attributes.MOVEMENT_SPEED));
                
                if (this.shark.isInWater()) {
                    // Implementación del SurfaceFactor de Primal
                    float surfaceFactor = getSurfaceFactor();
                    this.shark.setSpeed(speed * surfaceFactor);
                    
                    double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                    float pitch = -((float)(Mth.atan2(d1, d4) * (180D / Math.PI)));
                    this.shark.setXRot(this.rotlerp(this.shark.getXRot(), pitch, 5.0F));
                    
                    this.shark.zza = Mth.cos(this.shark.getXRot() * (float)(Math.PI / 180.0)) * speed;
                    this.shark.yya = -Mth.sin(this.shark.getXRot() * (float)(Math.PI / 180.0)) * speed;
                } else {
                    this.shark.setSpeed(speed * 0.15F); // Torpe en tierra
                }
            }
        } else {
            this.shark.setSpeed(0.0F);
        }
    }

    private float getSurfaceFactor() {
        BlockPos.MutableBlockPos pos = this.shark.blockPosition().mutable();
        for (int i = 0; i < 16; ++i) {
            pos.move(Direction.UP);
            if (!this.shark.level().getFluidState(pos).is(FluidTags.WATER)) {
                double dist = (double)pos.getY() - this.shark.getY();
                return (dist < 5.0D) ? (float)Mth.clamp(dist / 5.0D, 0.15D, 1.0D) : 1.0F;
            }
        }
        return 1.0F;
    }
}