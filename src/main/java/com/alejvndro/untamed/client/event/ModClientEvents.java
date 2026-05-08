package com.alejvndro.untamed.client.event;

import com.alejvndro.untamed.Untamed_Main;
import com.alejvndro.untamed.client.renderer.entity.TigerSharkRenderer;
import com.alejvndro.untamed.registry.Untamed_Entities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Untamed_Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // Esto ahora funcionará porque TigerSharkRenderer extiende de GeoEntityRenderer
        event.registerEntityRenderer(Untamed_Entities.TIGER_SHARK.get(), TigerSharkRenderer::new);
    }
}