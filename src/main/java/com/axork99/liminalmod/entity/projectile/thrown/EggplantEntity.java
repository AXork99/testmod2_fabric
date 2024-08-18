package com.axork99.liminalmod.entity.projectile.thrown;

import com.axork99.liminalmod.LiminalMod;
import com.axork99.liminalmod.entity.ModEntityTypes;
import com.axork99.liminalmod.item.ModItems;
import com.axork99.liminalmod.utils.MultiHitResult;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class EggplantEntity extends ThrownRigidBodyEntity {

    public EggplantEntity(EntityType<? extends EggplantEntity> entityType, World world) {
        super(entityType, world);
    }

    public EggplantEntity(EntityType<? extends EggplantEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public EggplantEntity(EntityType<? extends EggplantEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    public EggplantEntity(double d, double e, double f, World world) {
        this(ModEntityTypes.EGGPLANT, d, e, f, world);
    }

    public EggplantEntity(LivingEntity livingEntity, World world) {
        this(ModEntityTypes.EGGPLANT, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {return ModItems.EGGPLANT;}

    @Override
    public void onEntityHit(EntityHitResult entityHitResult) {
        LiminalMod.LOGGER.info("Hit: " + entityHitResult.getEntity().getEntityName());
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        int i = entity instanceof WolfEntity ? 0 : 1000;
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), (float)i);
        this.discard();
    }

    @Override
    public void onEntityHit(MultiHitResult entityHitResult) {
        this.onEntityHit((EntityHitResult) entityHitResult.getHits().stream().findFirst().get());
    }

    @Override
    public Type getBouncingType () {
        return Type.PLASTIC;
    }

    @Override
    public float getMass() {
        return 0.5f;
    }
}
