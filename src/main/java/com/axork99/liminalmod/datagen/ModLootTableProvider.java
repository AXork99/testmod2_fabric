package com.axork99.liminalmod.datagen;

import com.axork99.liminalmod.block.ModBlocks;
import com.axork99.liminalmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {


    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.END_FOLIAGE, oreDrops(ModBlocks.END_FOLIAGE, Items.END_STONE));
        addDrop(ModBlocks.CONDENSED_EXOTIC_MATTER);
    }
}
