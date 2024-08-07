package com.axork99.liminalmod.block;

import com.axork99.liminalmod.LiminalMod;
import com.axork99.liminalmod.LiminalModClient;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Contract;

public class ModBlocks {

    public static final Block END_FOLIAGE = registerBlock("end_foliage",
            new Block(FabricBlockSettings.copyOf(Blocks.END_STONE)));

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(LiminalMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(LiminalMod.MOD_ID, name), block);
    }

    public static void registerModBlocks () {
        LiminalMod.LOGGER.info("Registering Blocks for " + LiminalMod.MOD_ID);
    }
}
