package com.alejvndro.untamed.datagen.providers;

import com.alejvndro.untamed.registry.Untamed_Entities;
import com.alejvndro.untamed.registry.Untamed_Items;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class Untamed_LanguageFileGenerator extends LanguageProvider {

    private final String locale;

    public Untamed_LanguageFileGenerator(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if ("en_us".equals(locale)) {
            addEnglish();
        } else if ("es_es".equals(locale)) {
            addSpanish();
        }
    }

    private void addEnglish() {
        // Pestaña de modo creativo
        add("creativetab.untamed_tab", "Untamed Mod");

        // Ítems
        add(Untamed_Items.LOGO_TAB.get(), "Untamed Logo");
        add(Untamed_Items.BUTTERFLY_NECTAR.get(), "Butterfly Nectar");
        add(Untamed_Items.POLLEN.get(), "Pollen");
        add(Untamed_Items.MORPHO_WING_SCALE.get(), "Morpho Wing Scale");
        add(Untamed_Items.BUTTERFLY_SPAWN_EGG.get(), "Morpho Butterfly Spawn Egg");

        // Entidades
        add(Untamed_Entities.BUTTERFLY.get(), "Butterfly");
    }

    private void addSpanish() {
        // Pestaña de modo creativo
        add("creativetab.untamed_tab", "Untamed Mod");

        // Ítems
        add(Untamed_Items.LOGO_TAB.get(), "Logo de Untamed");
        add(Untamed_Items.BUTTERFLY_NECTAR.get(), "Néctar de Mariposa");
        add(Untamed_Items.POLLEN.get(), "Polen");
        add(Untamed_Items.MORPHO_WING_SCALE.get(), "Escama de Ala de Morpho");
        add(Untamed_Items.BUTTERFLY_SPAWN_EGG.get(), "Generador de Mariposa Morpho");

        // Entidades
        add(Untamed_Entities.BUTTERFLY.get(), "Mariposa Morpho");
    }
}
