package com.alejvndro.untamed.registry;

import com.alejvndro.untamed.Untamed;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Untamed_Blocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Untamed.MOD_ID);

    // Future block entries go here

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
