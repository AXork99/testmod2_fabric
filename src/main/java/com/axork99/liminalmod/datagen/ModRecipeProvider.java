package com.axork99.liminalmod.datagen;

import com.axork99.liminalmod.LiminalMod;
import com.axork99.liminalmod.block.ModBlocks;
import com.axork99.liminalmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        offerReversibleCompactingRecipes(exporter,
                RecipeCategory.MISC, ModItems.EXOTIC_MATTER, RecipeCategory.MISC, ModBlocks.CONDENSED_EXOTIC_MATTER);
        ShapelessRecipeJsonBuilder
                .create(RecipeCategory.MISC, ModItems.EGGPLANT, 1)
                .input(ModItems.EGGPLANT)
                .criterion(hasItem(ModItems.EXOTIC_MATTER), conditionsFromItem(ModItems.EXOTIC_MATTER))
                .offerTo(exporter, new Identifier(getRecipeName(ModItems.EGGPLANT)));
    }
}
