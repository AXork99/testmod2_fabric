package com.axork99.liminalmod.datagen;

import com.axork99.liminalmod.block.ModBlocks;
import com.axork99.liminalmod.utils.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ModTags.Blocks.CAN_GROW_EXOTIC)
                .add(ModBlocks.END_FOLIAGE)
                .add(Blocks.FARMLAND);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.END_FOLIAGE);

    getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(ModBlocks.CONDENSED_EXOTIC_MATTER);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.END_FOLIAGE);
    }
}
