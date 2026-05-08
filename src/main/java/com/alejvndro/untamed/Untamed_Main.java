package com.alejvndro.untamed;

import com.alejvndro.untamed.entity.animal.TigerSharkEntity;
import com.alejvndro.untamed.registry.*; // Importamos todos los registros
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Untamed_Main.MOD_ID)
public class Untamed_Main {
    public static final String MOD_ID = "untamed";

    public Untamed_Main(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // 1. Registro de contenido base
        Untamed_Items.ITEMS.register(modEventBus);
        Untamed_Entities.ENTITIES.register(modEventBus);
        Untamed_Tabs.CREATIVE_MODE_TABS.register(modEventBus);

        // 2. Registro de Sistemas de IA (Crucial para el comportamiento de Primal)
        // Estos registros permiten que el TigerSharkAi use sensores y memorias nuevas
        Untamed_Sensors.SENSOR_TYPES.register(modEventBus);
        Untamed_MemoryModules.MEMORY_MODULES.register(modEventBus);

        // 3. Registro de sonidos y bloques (si tienes)
        // Untamed_Sounds.SOUND_EVENTS.register(modEventBus);

        // 4. Registro de eventos del bus del Mod
        modEventBus.addListener(this::registerAttributes);

        // 5. Registro en el bus de Forge
        MinecraftForge.EVENT_BUS.register(this);
    }

    // Inyección de Atributos (Estilo Primal con Armadura y Rango de Seguimiento)
    private void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(Untamed_Entities.TIGER_SHARK.get(), TigerSharkEntity.createAttributes().build());
    }
}