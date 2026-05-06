package com.alejvndro.untamed.datagen;

import com.alejvndro.untamed.Untamed;
import com.alejvndro.untamed.datagen.providers.Untamed_ItemModelGenerator;
import com.alejvndro.untamed.datagen.providers.Untamed_LanguageFileGenerator;
import com.alejvndro.untamed.datagen.providers.Untamed_LootTableGenerator;
import com.alejvndro.untamed.datagen.providers.Untamed_RecipeGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Untamed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Untamed_DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // --- Proveedores del lado del cliente ---
        generator.addProvider(event.includeClient(),
                new Untamed_ItemModelGenerator(packOutput, existingFileHelper));

        generator.addProvider(event.includeClient(),
                new Untamed_LanguageFileGenerator(packOutput, Untamed.MOD_ID, "en_us"));

        generator.addProvider(event.includeClient(),
                new Untamed_LanguageFileGenerator(packOutput, Untamed.MOD_ID, "es_es"));

        // --- Proveedores del lado del servidor ---
        generator.addProvider(event.includeServer(),
                new Untamed_RecipeGenerator(packOutput));

        generator.addProvider(event.includeServer(),
                new Untamed_LootTableGenerator(packOutput));
    }
}
