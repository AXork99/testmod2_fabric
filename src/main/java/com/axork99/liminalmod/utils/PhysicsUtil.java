package com.axork99.liminalmod.utils;

import com.axork99.liminalmod.LiminalMod;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PhysicsUtil {

    public static Vec3d findAverage(Collection<Vec3d> points) {
        return points.stream().reduce(Vec3d.ZERO, Vec3d::add).multiply((double) 1 / points.size());
    }

    public static Vec3d calculateBuoyancy(MCRigidBody body, BlockPos pos, BlockState state) {
        Box me = body.getBoundingBox();
        double mevolume = me.getXLength() * me.getYLength() * me.getZLength();
        Vec3d accumulatedPush = Vec3d.ZERO;
        for (Box box : state.getCollisionShape(body.getWorld(), pos).getBoundingBoxes()) {
            Box hitBox = box.offset(pos);
            if (me.intersects(hitBox)) {
                Box inter = me.intersection(hitBox);
                double intervolume = inter.getYLength() * inter.getXLength() * inter.getZLength();
                Vec3d dist = me.getCenter().subtract(inter.getCenter()).normalize().multiply(intervolume / mevolume);
                accumulatedPush = accumulatedPush.add(dist);
            }
        }
        //LiminalMod.LOGGER.info("Bouyancy: " + accumulatedPush);
        if (accumulatedPush.length() > 0.001)
            body.setClipping(true);
        return accumulatedPush.multiply(0.01);
    }
    public static Vec3d calculateBuoyancy(MCRigidBody body, BlockPos pos, FluidState state) {
        Box me = body.getBoundingBox();
        double volume = me.getXLength() * me.getYLength() * me.getZLength();
        double submerged = 0;
        Box hitBox = state.getShape(body.getWorld(), pos).getBoundingBox();
        if (me.intersects(hitBox)) {
            Box inter = me.intersection(hitBox);
            submerged = inter.getYLength() * inter.getXLength() * inter.getZLength();
        }
        return body.getGravityVector().multiply(-body.getDensity() * (submerged/volume));
    }

    public static MultiHitResult getCollision(MCRigidBody body, Predicate<Entity> predicate) {
        Vec3d vec3d = body.getVelocity();
        World world = body.getWorld();
        return getCollision(body, predicate, vec3d, world);
    }

    private static MultiHitResult getCollision(MCRigidBody body, Predicate<Entity> predicate, Vec3d velocity, World world) {
        //LiminalMod.LOGGER.info("body at:" + body.getPos() + " velocity:" + velocity);
        return new MultiHitResult(body.getCollisionPoints(velocity).stream()
                .map(point -> {
                    Vec3d end = point.add(velocity);
                    HitResult hitResult = world.raycast(new RaycastContext(point, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, body.asEntity()));
                    if (hitResult.getType() != HitResult.Type.MISS) {
                        end = hitResult.getPos();
                    }
                    HitResult hitResult2 = ProjectileUtil.getEntityCollision(world, body.asEntity(), end, velocity, body.getBoundingBox().stretch(velocity).expand(1.0), predicate);
                    if (hitResult2 != null) {
                        hitResult = hitResult2;
                    }
                    //LiminalMod.LOGGER.info("corner:" + point + " hit:" + hitResult.getType());
                    return hitResult;
                }).collect(Collectors.toSet()));
    }
}
