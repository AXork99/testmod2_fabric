package com.axork99.liminalmod.entity;

import com.axork99.liminalmod.utils.MCRigidBody;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractRigidBodyEntity extends Entity implements MCRigidBody {
    private Vec3d force;
    private boolean clipping;

    @Override
    public boolean canHit(Entity entity) {
        return true;
    }

    @Override
    public void setForce (Vec3d force) {
        this.force = force;
    }
    @Override
    public Vec3d getForce () {
        return this.force;
    }

    public AbstractRigidBodyEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void checkBlockCollision() {
        MCRigidBody.super.checkBlockCollision();
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
