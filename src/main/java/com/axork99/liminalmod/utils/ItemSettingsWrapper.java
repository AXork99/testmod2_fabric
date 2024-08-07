package com.axork99.liminalmod.utils;

import com.axork99.liminalmod.item.ThrowableItem;
import net.fabricmc.fabric.api.item.v1.CustomDamageHandler;
import net.fabricmc.fabric.api.item.v1.EquipmentSlotProvider;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.util.Rarity;

/**
 * Wraps FabricItemSettings with an interface for better extension.<br>
 * Use <p>class A extends FabricSettingsWrapper<A></p> instead of <p>class A extends FabricSettings</p>
 * and there is no longer a need to Override all previous methods
**/
public abstract class ItemSettingsWrapper<T extends FabricItemSettings> extends FabricItemSettings {

    @Override
    public T equipmentSlot(EquipmentSlotProvider equipmentSlotProvider) {
        super.equipmentSlot(equipmentSlotProvider);
        return (T) this;
    }

    @Override
    public T customDamage(CustomDamageHandler handler) {
        super.customDamage(handler);
        return (T) this;
    }

    @Override
    public T food(FoodComponent foodComponent) {
        super.food(foodComponent);
        return (T) this;
    }

    @Override
    public T maxCount(int maxCount) {
        super.maxCount(maxCount);
        return (T) this;
    }

    @Override
    public T maxDamageIfAbsent(int maxDamage) {
        super.maxDamageIfAbsent(maxDamage);
        return (T) this;
    }

    @Override
    public T maxDamage(int maxDamage) {
        super.maxDamage(maxDamage);
        return (T) this;
    }

    @Override
    public T recipeRemainder(Item recipeRemainder) {
        super.recipeRemainder(recipeRemainder);
        return (T) this;
    }

    @Override
    public T rarity(Rarity rarity) {
        super.rarity(rarity);
        return (T) this;
    }

    @Override
    public T fireproof() {
        super.fireproof();
        return (T) this;
    }

    @Override
    public T requires(FeatureFlag... features) {
        super.requires(features);
        return (T) this;
    }
}
