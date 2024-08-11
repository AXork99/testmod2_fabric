package com.axork99.liminalmod.datagen;

import com.axork99.liminalmod.block.ModBlocks;
import com.axork99.liminalmod.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.CONDENSED_EXOTIC_MATTER);
        blockStateModelGenerator.registerRandomHorizontalRotations(topSoils(Blocks.END_STONE), ModBlocks.END_FOLIAGE);
    }

    public TexturedModel.Factory topSoils(Identifier bottomTexture) {
        return TexturedModel.CUBE_BOTTOM_TOP.andThen(textures -> textures.put(TextureKey.BOTTOM, bottomTexture));
    }

    public TexturedModel.Factory topSoils(Block bottomBlock) {
        return this.topSoils(TextureMap.getId(bottomBlock));
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.EGGPLANT, Models.GENERATED);
        itemModelGenerator.register(ModItems.EXOTIC_MATTER, Models.GENERATED);
    }
}
