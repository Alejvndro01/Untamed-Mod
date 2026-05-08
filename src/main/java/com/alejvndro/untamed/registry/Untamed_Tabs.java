package com.alejvndro.untamed.registry;

import com.alejvndro.untamed.Untamed_Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Untamed_Tabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Untamed_Main.MOD_ID);

    public static final RegistryObject<CreativeModeTab> UNTAMED_TAB = CREATIVE_MODE_TABS.register("untamed_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Untamed_Items.LOGO_TAB.get())) // Icono de la pestaña
                    .title(Component.translatable("creativetab.untamed_tab")) // Nombre visible
                    .displayItems((parameters, output) -> {
                        // Aquí añades todos tus ítems a la pestaña
                        output.accept(Untamed_Items.TIGER_SHARK_SPAWN_EGG.get());
                        // output.accept(Untamed_Items.OTRO_ITEM.get());
                    }).build());
}