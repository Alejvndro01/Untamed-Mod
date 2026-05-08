package com.alejvndro.untamed.client.model;

import com.alejvndro.untamed.Untamed_Main;
import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TigerSharkModel extends GeoModel<TigerSharkEntity> {

    @Override
    public ResourceLocation getModelResource(TigerSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Untamed_Main.MOD_ID, "geo/entity/tiger_shark.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TigerSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Untamed_Main.MOD_ID, "textures/entity/tiger_shark.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TigerSharkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Untamed_Main.MOD_ID, "animations/entity/tiger_shark.animation.json");
    }
}