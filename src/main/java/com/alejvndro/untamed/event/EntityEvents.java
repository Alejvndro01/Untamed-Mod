package com.alejvndro.untamed.event;

import com.alejvndro.untamed.Untamed;
import com.alejvndro.untamed.client.ButterflyRenderer;
import com.alejvndro.untamed.entity.ButterflyEntity;
import com.alejvndro.untamed.entity.ModEntities;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Untamed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        // Asignamos vida y velocidad base
        event.put(ModEntities.BUTTERFLY.get(), ButterflyEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 2.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FLYING_SPEED, 0.4D)
                .build());
    }

    @Mod.EventBusSubscriber(modid = Untamed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            // Vinculamos la entidad con el renderizador GeckoLib[cite: 2]
            event.registerEntityRenderer(ModEntities.BUTTERFLY.get(), ButterflyRenderer::new);
        }
    }
}