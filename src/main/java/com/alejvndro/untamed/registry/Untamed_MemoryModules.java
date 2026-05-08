package com.alejvndro.untamed.registry;

import com.alejvndro.untamed.Untamed_Main;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

public class Untamed_MemoryModules {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES = 
            DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, Untamed_Main.MOD_ID);

    // Ejemplo: Si quieres la memoria de "bloque importante" que usa Primal
    public static final RegistryObject<MemoryModuleType<net.minecraft.core.BlockPos>> NEAREST_IMPORTANT_BLOCK = 
            MEMORY_MODULES.register("nearest_important_block", () -> new MemoryModuleType<>(Optional.empty()));
}