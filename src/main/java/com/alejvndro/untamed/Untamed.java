package com.alejvndro.untamed;

import com.alejvndro.untamed.item.ModCreativeModeTabs;
import com.alejvndro.untamed.registry.Untamed_Blocks;
import com.alejvndro.untamed.registry.Untamed_Entities;
import com.alejvndro.untamed.registry.Untamed_Items;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

@Mod(Untamed.MOD_ID)
public class Untamed {
    public static final String MOD_ID = "untamed";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Untamed() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Registrar la pestaña del modo creativo (Debe ir antes de los ítems preferiblemente)
        ModCreativeModeTabs.register(modEventBus);

        // Registrar los ítems (incluye néctar, polen, escama y huevo)
        Untamed_Items.register(modEventBus);

        // Registrar entidades
        Untamed_Entities.register(modEventBus);

        // Registrar bloques
        Untamed_Blocks.register(modEventBus);

        // Inicializar GeckoLib para las animaciones
        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }
}