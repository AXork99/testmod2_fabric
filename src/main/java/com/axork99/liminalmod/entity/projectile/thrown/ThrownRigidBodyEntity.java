package com.axork99.liminalmod.entity.projectile.thrown;

import com.axork99.liminalmod.utils.MCRigidBody;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public abstract class ThrownRigidBodyEntity extends ThrownItemEntity implements MCRigidBody {

    private boolean leftOwner;
    private boolean shot;
    private boolean clipping;

    public ThrownRigidBodyEntity(EntityType<? extends ThrownRigidBodyEntity> entityType, World world) {
        super(entityType, world);
    }

    public ThrownRigidBodyEntity(EntityType<? extends ThrownRigidBodyEntity> entityType, double d, double e, double f, World world) {
        super(entityType, d, e, f, world);
    }

    public ThrownRigidBodyEntity(EntityType<? extends ThrownRigidBodyEntity> entityType, LivingEntity livingEntity, World world) {
        super(entityType, livingEntity, world);
    }

    private Vec3d force;

    @Override
    public void setForce (Vec3d force) {
        this.force = force;
    }
    @Override
    public Vec3d getForce () {
        return this.force;
    }

    @Override
    public void checkBlockCollision() {
        MCRigidBody.super.checkBlockCollision();
    }

    @Override
    public boolean canHit(Entity entity) {
        if (!entity.canBeHitByProjectile()) {
            return false;
        } else {
            Entity entity2 = this.getOwner();
            return entity2 == null || this.getLeftStatus();
        }
    }

    @Override
    public void onCollision(HitResult hitResult) {
        MCRigidBody.super.onCollision(hitResult);
    }

    protected ParticleEffect getParticleParameters() {
        ItemStack itemStack = this.getItem();
        return new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
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
    public void tick() {
        if (!this.getShot()) {
            this.emitGameEvent(GameEvent.PROJECTILE_SHOOT, this.getOwner());
            this.setShot(true);
        }
        if (!this.getLeftStatus()) {
            this.setLeftStatus(this.shouldLeaveOwner());
        }
        MCRigidBody.super.tick();
    }

    protected boolean shouldLeaveOwner() {
        Entity entity = this.getOwner();
        if (entity != null) {
            for (Entity entity2 : this.getWorld()
                    .getOtherEntities(this, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0), entityx -> !entityx.isSpectator() && entityx.canHit())) {
                if (entity2.getRootVehicle() == entity.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean getLeftStatus() {
        return this.leftOwner;
    }

    protected void setLeftStatus(boolean leftStatus) {
        this.leftOwner = leftStatus;
    }

    protected boolean getShot() {
        return this.shot;
    }

    protected void setShot(boolean shot) {
        this.shot = shot;
    }

    @Override
    public boolean isClipping() {
        return this.clipping;
    }

    @Override
    public void setClipping(boolean clipping) {
        this.clipping = clipping;
    }
}
