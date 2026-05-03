package com.alejvndro.untamed.client;

import com.alejvndro.untamed.Untamed;
import com.alejvndro.untamed.entity.ButterflyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ButterflyModel extends GeoModel<ButterflyEntity> {
    @Override
    public ResourceLocation getModelResource(ButterflyEntity animatable) {
        return new ResourceLocation(Untamed.MOD_ID, "geo/butterfly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ButterflyEntity animatable) {
        return new ResourceLocation(Untamed.MOD_ID, "textures/entity/butterfly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ButterflyEntity animatable) {
        return new ResourceLocation(Untamed.MOD_ID, "animations/butterfly.animation.json");
    }
}