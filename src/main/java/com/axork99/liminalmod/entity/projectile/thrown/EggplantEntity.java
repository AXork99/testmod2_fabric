package com.axork99.liminalmod.entity.projectile.thrown;

import com.axork99.liminalmod.LiminalMod;
import com.axork99.liminalmod.entity.Bouncing;
import com.axork99.liminalmod.entity.ModEntityTypes;
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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class EggplantEntity extends ThrownItemEntity implements Bouncing {
    public static final int DAMAGE = 1000;

    public EggplantEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public EggplantEntity(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public EggplantEntity(EntityType<? extends ThrownItemEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    public EggplantEntity(double d, double e, double f, World world) {
        this(ModEntityTypes.EGGPLANT, d, e, f, world);
    }

    public EggplantEntity(LivingEntity livingEntity, World world) {
        this(ModEntityTypes.EGGPLANT, livingEntity, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.EGGPLANT;
    }

    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return (ParticleEffect)(new ItemStackParticleEffect(ParticleTypes.ITEM,
                (itemStack.isEmpty() ? new ItemStack(ModItems.EGGPLANT) : itemStack)));
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES) {
            ParticleEffect particleEffect = this.getParticleParameters();

            for (int i = 0; i < 8; i++) {
                this.getWorld().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        int i = entity instanceof WolfEntity ? 0 : DAMAGE;
        entity.damage(this.getDamageSources().thrown(this, this.getOwner()), (float)i);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            if (!this.isOnGround()) {
                if (!this.getWorld().isClient)
                    this.getWorld().playSound(null, blockHitResult.getBlockPos(), SoundEvents.ENTITY_SLIME_JUMP, SoundCategory.MASTER);
                Vec3i v = blockHitResult.getSide().getVector();
                Vec3d normal = new Vec3d(v.getX(), v.getY(), v.getZ()).normalize();
                this.bounce(normal);
            } else {
                this.setVelocity(this.getVelocity().multiply(1.0D, 0.0D, 1.0D));
            }
        } else {
            super.onCollision(hitResult);
            if (!this.getWorld().isClient) {
                LiminalMod.LOGGER.info("Killed Eggplant!");
                this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_DEATH_SOUND_OR_ADD_PROJECTILE_HIT_PARTICLES);
                this.discard();
            }
        }
    }

    @Override
    public void bounce(Vec3d normal) {
        if (this.getVelocity().lengthSquared() > 0.01)
            Bouncing.super.bounce(normal);
        else {
            this.setVelocity(this.getVelocity().x, 0, this.getVelocity().z);
            this.setOnGround(true);
        }
    }

    @Override
    public Type getBouncngType() {
        return Type.PLASTIC;
    }
}
