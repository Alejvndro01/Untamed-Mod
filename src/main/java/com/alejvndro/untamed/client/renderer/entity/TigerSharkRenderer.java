package com.alejvndro.untamed.client.renderer.entity;

import com.alejvndro.untamed.client.model.TigerSharkModel;
import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TigerSharkRenderer extends GeoEntityRenderer<TigerSharkEntity> {
    public TigerSharkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TigerSharkModel());
        this.shadowRadius = 0.7f;
    }
}