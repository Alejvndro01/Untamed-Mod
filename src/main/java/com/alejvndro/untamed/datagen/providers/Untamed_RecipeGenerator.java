package com.alejvndro.untamed.datagen.providers;

import com.alejvndro.untamed.registry.Untamed_Items;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class Untamed_RecipeGenerator extends RecipeProvider {

    public Untamed_RecipeGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        // Néctar de Mariposa: 8 Polen rodeando una botella de cristal
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, Untamed_Items.BUTTERFLY_NECTAR.get())
                .define('P', Untamed_Items.POLLEN.get())
                .define('B', Items.GLASS_BOTTLE)
                .pattern("PPP")
                .pattern("PBP")
                .pattern("PPP")
                .unlockedBy("has_pollen", has(Untamed_Items.POLLEN.get()))
                .save(writer);
    }
}
