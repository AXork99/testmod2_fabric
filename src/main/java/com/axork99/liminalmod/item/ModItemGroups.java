package com.axork99.liminalmod.item;

import com.axork99.liminalmod.LiminalMod;
import com.axork99.liminalmod.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static ItemGroup LIMINAL_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(LiminalMod.MOD_ID, "liminal"),
            FabricItemGroup.builder()
                    .displayName(Text.translatable("itemgroup.liminal"))
                    .icon(() -> new ItemStack(ModItems.EGGPLANT))
                    .entries((displayContext, entries) -> {
                        entries.add(ModItems.EGGPLANT);
                        entries.add(ModBlocks.END_FOLIAGE);
                        entries.add(ModItems.EXOTIC_MATTER);
                        entries.add(ModBlocks.CONDENSED_EXOTIC_MATTER);
                    }).build());

    public static void registerItemGroups() {
        LiminalMod.LOGGER.info("Registered Mod Item Groups for " + LiminalMod.MOD_ID);
    }
}
