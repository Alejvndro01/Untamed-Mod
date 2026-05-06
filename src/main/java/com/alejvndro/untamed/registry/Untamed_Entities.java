package com.alejvndro.untamed.registry;

import com.alejvndro.untamed.Untamed;
import com.alejvndro.untamed.entity.ButterflyEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Untamed_Entities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Untamed.MOD_ID);

    public static final RegistryObject<EntityType<ButterflyEntity>> BUTTERFLY =
            ENTITIES.register("butterfly", () -> EntityType.Builder.of(ButterflyEntity::new, MobCategory.AMBIENT)
                    .sized(0.5f, 0.5f) // Tamaño del hitbox
                    .build("butterfly"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
