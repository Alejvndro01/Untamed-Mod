package com.alejvndro.untamed.entity;

import com.alejvndro.untamed.Untamed;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = 
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Untamed.MOD_ID);

    public static final RegistryObject<EntityType<ButterflyEntity>> BUTTERFLY = 
            ENTITIES.register("butterfly", () -> EntityType.Builder.of(ButterflyEntity::new, MobCategory.AMBIENT)
                    .sized(0.5f, 0.5f) // Tamaño del hitbox
                    .build("butterfly"));

    public static final RegistryObject<EntityType<TigerSharkEntity>> TIGER_SHARK =
            ENTITIES.register("tiger_shark", () -> EntityType.Builder
                    .<TigerSharkEntity>of(TigerSharkEntity::new, MobCategory.WATER_CREATURE)
                    .sized(2.0f, 1.0f) // Tamaño del hitbox del tiburón tigre
                    .build("tiger_shark"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}