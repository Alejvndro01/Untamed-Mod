package com.alejvndro.untamed.client;

import com.alejvndro.untamed.Untamed;
import com.alejvndro.untamed.entity.TigerSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TigerSharkModel extends GeoModel<TigerSharkEntity> {

    @Override
    public ResourceLocation getModelResource(TigerSharkEntity animatable) {
        return new ResourceLocation(Untamed.MOD_ID, "geo/tiger_shark.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TigerSharkEntity animatable) {
        return new ResourceLocation(Untamed.MOD_ID, "textures/entity/tiger_shark.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TigerSharkEntity animatable) {
        return new ResourceLocation(Untamed.MOD_ID, "animations/tiger_shark.animation.json");
    }
}
