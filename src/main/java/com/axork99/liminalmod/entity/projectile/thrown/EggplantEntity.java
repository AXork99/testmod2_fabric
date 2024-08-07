package com.axork99.liminalmod.entity.projectile.thrown;

import com.axork99.liminalmod.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class EggplantEntity extends SnowballEntity {
    public static final int DAMAGE = 1000;

    public EggplantEntity(EntityType<? extends EggplantEntity> entityType, World world) {
        super(entityType, world);
    }

    public EggplantEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    public EggplantEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    //@Override
    //protected Item getDefaultItem() {
        //return ModItems.EGGPLANT;
    //}

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        int i = entity instanceof WolfEntity ? 0 : DAMAGE;
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), (float)i);
    }

}
