package com.alejvndro.untamed.item;

import com.alejvndro.untamed.Untamed;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Untamed.MOD_ID);

    public static final RegistryObject<CreativeModeTab> UNTAMED_TAB = CREATIVE_MODE_TABS.register("untamed_tab",
        () -> CreativeModeTab.builder()
                .icon(() -> new ItemStack(ModItems.LOGO_TAB.get()))
                .title(Component.translatable("creativetab.untamed_tab"))
                .displayItems((pParameters, pOutput) -> {
                    pOutput.accept(ModItems.BUTTERFLY_NECTAR.get());
                    pOutput.accept(ModItems.POLLEN.get());
                    pOutput.accept(ModItems.MORPHO_WING_SCALE.get());
                    pOutput.accept(ModItems.BUTTERFLY_SPAWN_EGG.get());
                })
                .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}