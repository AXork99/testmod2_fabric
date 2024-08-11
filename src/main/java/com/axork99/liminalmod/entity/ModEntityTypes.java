package com.axork99.liminalmod.entity;

import com.axork99.liminalmod.LiminalMod;
import com.axork99.liminalmod.entity.projectile.thrown.EggplantEntity;
import com.axork99.liminalmod.entity.renderer.EggplantRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityTypes {
    public static final EntityType<EggplantEntity> EGGPLANT =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(LiminalMod.MOD_ID, "eggplant"),
                    FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<EggplantEntity>) EggplantEntity::new)
                            .dimensions(EntityDimensions.fixed(1.0f, 1.0f)).build()
            );
    public static void RegisterEntityTypes () {
        LiminalMod.LOGGER.info("Registered Mod Entity Types for " + LiminalMod.MOD_ID);
        EntityRendererRegistry.register(ModEntityTypes.EGGPLANT, EggplantRenderer::new);
    }
}
