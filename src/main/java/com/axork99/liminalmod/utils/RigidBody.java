package com.axork99.liminalmod.utils;

import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface RigidBody extends Bouncing {
    boolean isClipping();
    void setClipping(boolean clipping);

    default Type getBouncingType() {
        return Type.PLASTIC;
    }
    default Collection<Box> getBoundingBoxes() {
        return Set.of(this.getBoundingBox());
    }
    Box getBoundingBox();
    World getWorld();
    Vec3d getGravityVector();
    float getMass();
    float getFriction();
    float getAirResistance();
    default float getHeight() {
        return MathHelper.abs((float) new Vec3d(this.getBoundingBox().getXLength(), this.getBoundingBox().getYLength(), this.getBoundingBox().getZLength())
                .dotProduct(this.getGravityVector().normalize()));
    }

    Vec3d getPos();
    void setPosition(Vec3d pos);
    default void updatePos() {
        this.setPosition(this.getPos().add(this.getVelocity()));
    }

    Vec3d getForce ();
    void setForce (Vec3d force);
    default void applyForce(Vec3d force) {
        this.setForce(this.getForce().add(force));
    }
    default Vec3d getAcceleration() {
        return this.getForce().multiply((double) 1 / this.getMass());
    }
    default void updateVelocity() {
        this.setVelocity(this.getVelocity().add(this.getAcceleration()));
    }

    default Vec3d getCenterOfMass() {
        int n = this.getBoundingBoxes().size();
        return PhysicsUtil.findAverage(this.getBoundingBoxes().stream()
                .map(Box::getCenter).collect(Collectors.toSet()));
    };
    default float getVolume() {
        return (float) this.getBoundingBoxes().stream()
                .mapToDouble(box -> box.getXLength() * box.getYLength() * box.getZLength())
                .sum();
    }
    default float getDensity() {
        return this.getMass()/this.getVolume();
    }
}
