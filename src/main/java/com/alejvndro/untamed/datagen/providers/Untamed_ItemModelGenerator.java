package com.alejvndro.untamed.datagen.providers;

import com.alejvndro.untamed.Untamed;
import com.alejvndro.untamed.registry.Untamed_Items;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class Untamed_ItemModelGenerator extends ItemModelProvider {

    public Untamed_ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Untamed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Ítems básicos (usa el parent minecraft:item/generated)
        basicItem(Untamed_Items.LOGO_TAB.get());
        basicItem(Untamed_Items.POLLEN.get());
        basicItem(Untamed_Items.MORPHO_WING_SCALE.get());
        basicItem(Untamed_Items.BUTTERFLY_NECTAR.get());

        // Spawn Egg (usa el parent minecraft:item/template_spawn_egg)
        withExistingParent(getItemPath(Untamed_Items.BUTTERFLY_SPAWN_EGG.get()),
                mcLoc("item/template_spawn_egg"));
    }

    private String getItemPath(Item item) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
    }
}
