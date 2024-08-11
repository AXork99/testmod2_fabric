package com.axork99.liminalmod.item;

import com.axork99.liminalmod.LiminalMod;
import com.axork99.liminalmod.entity.projectile.thrown.EggplantEntity;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SnowballItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item EGGPLANT = registerItem("eggplant",
            new ThrowableItem(new ThrowableItem.Settings<>().entityThrown(EggplantEntity::new)));
    public static final Item EXOTIC_MATTER = registerItem("exotic_matter", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientsItemGroup(FabricItemGroupEntries entries) {
        entries.add(EGGPLANT);
        entries.add(EXOTIC_MATTER);
    }

    public static void registerModItems() {
        LiminalMod.LOGGER.info("Registered Mod Items for " + LiminalMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientsItemGroup);
    }

    public static <T extends Item> T registerItem(String name, T item) {
        return Registry.register(Registries.ITEM, new Identifier(LiminalMod.MOD_ID, name), item);
    }
}
