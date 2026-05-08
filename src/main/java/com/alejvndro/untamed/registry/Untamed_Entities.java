package com.alejvndro.untamed.registry;

import com.alejvndro.untamed.Untamed_Main;
import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Untamed_Entities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Untamed_Main.MOD_ID);

    public static final RegistryObject<EntityType<TigerSharkEntity>> TIGER_SHARK = ENTITIES.register("tiger_shark",
            () -> EntityType.Builder.of(TigerSharkEntity::new, MobCategory.WATER_CREATURE)
                    .sized(2.0f, 1.0f) // Ajusta al tamaño de tu tiburón
                    .build("tiger_shark"));
}