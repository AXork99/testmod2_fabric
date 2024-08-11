package com.axork99.liminalmod.utils;

import com.axork99.liminalmod.LiminalMod;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Blocks {
        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(LiminalMod.MOD_ID, name));
        }

        public static TagKey<Block> CAN_GROW_EXOTIC = createTag("can_grow_exotic");
    }

    public static  class Items {
        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(LiminalMod.MOD_ID, name));
        }
    }
}
