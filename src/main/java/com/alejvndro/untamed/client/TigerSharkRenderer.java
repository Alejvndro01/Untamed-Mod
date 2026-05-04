package com.alejvndro.untamed.client;

import com.alejvndro.untamed.entity.TigerSharkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TigerSharkRenderer extends GeoEntityRenderer<TigerSharkEntity> {

    public TigerSharkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TigerSharkModel());
    }
}
