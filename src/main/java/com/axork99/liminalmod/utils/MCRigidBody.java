package com.axork99.liminalmod.utils;

import com.axork99.liminalmod.LiminalMod;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.State;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import javax.swing.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public interface MCRigidBody extends RigidBody {

    default Entity asEntity(){
         return (Entity) this;
    }

    @Override
    default Box getBoundingBox() {
        return this.asEntity().getBoundingBox();
    }
    @Override
    default World getWorld() {
        return this.asEntity().getWorld();
    }
    @Override
    default Vec3d getGravityVector() {
        return this.asEntity().hasNoGravity() ? Vec3d.ZERO : new Vec3d(0, -0.06, 0);
    }
    @Override
    default float getMass() {
        return 1.0f;
    }
    @Override
    default float getFriction() {
        return 0.05f;
    }
    @Override
    default float getAirResistance() {
        return 0.005f;
    }
    @Override
    default float getHeight() {
        return this.asEntity().getHeight();
    }
    @Override
    default void updatePos() {
        Vec3d velocity = this.getVelocity();
        Vec3d pos = this.getPos();
        double d = pos.getX() + velocity.x;
        double e = pos.getY() + velocity.y;
        double f = pos.getZ() + velocity.z;
        float h;
        if (this.asEntity().isTouchingWater()) {
            for (int i = 0; i < 4; i++) {
                float g = 0.25F;
                this.getWorld().addParticle(
                        ParticleTypes.BUBBLE, d - velocity.x * 0.25, e - velocity.y * 0.25,
                        f - velocity.z * 0.25, velocity.x, velocity.y, velocity.z);
            }
            h = 0.8F;
        } else {
            h = 0.99F;
        }
        this.setPosition(new Vec3d(d, e, f));
    }

    default Collection<Vec3d> getCorners () {
        return this.getBoundingBoxes().stream().flatMap(box -> Set.of(
                new Vec3d(box.minX, box.minY, box.minZ),
                new Vec3d(box.minX, box.minY, box.maxZ),
                new Vec3d(box.minX, box.maxY, box.minZ),
                new Vec3d(box.minX, box.maxY, box.maxZ),
                new Vec3d(box.maxX, box.minY, box.minZ),
                new Vec3d(box.maxX, box.minY, box.maxZ),
                new Vec3d(box.maxX, box.maxY, box.minZ),
                new Vec3d(box.maxX, box.maxY, box.maxZ)
        ).stream()).collect(Collectors.toSet());
    }
    default Collection<Vec3d> getCollisionPoints (Vec3d direction) {
        Vec3d center = this.getCenterOfMass();
        return this.getCorners().stream().filter(vec ->
                MathHelper.sign(vec.subtract(center).getX()) == MathHelper.sign(direction.getX()) ||
                MathHelper.sign(vec.subtract(center).getY()) == MathHelper.sign(direction.getY()) ||
                MathHelper.sign(vec.subtract(center).getZ()) == MathHelper.sign(direction.getZ())
        ).collect(Collectors.toSet());
    }

    default void checkBlockCollision() {
        Box box = this.getBoundingBox();
        BlockPos blockPos = BlockPos.ofFloored(box.minX + 1.0E-7, box.minY + 1.0E-7, box.minZ + 1.0E-7);
        BlockPos blockPos2 = BlockPos.ofFloored(box.maxX - 1.0E-7, box.maxY - 1.0E-7, box.maxZ - 1.0E-7);
        if (this.getWorld().isRegionLoaded(blockPos, blockPos2)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Vec3d accumulatedPush = Vec3d.ZERO;
            for (int i = blockPos.getX(); i <= blockPos2.getX(); i++) {
                for (int j = blockPos.getY(); j <= blockPos2.getY(); j++) {
                    for (int k = blockPos.getZ(); k <= blockPos2.getZ(); k++) {
                        mutable.set(i, j, k);
                        BlockPos pos = new BlockPos(mutable);
                        BlockState blockState = this.getWorld().getBlockState(mutable);
                        FluidState fluidState = this.getWorld().getFluidState(mutable);
                        try {
                            if (!blockState.isAir()) {
                                blockState.onEntityCollision(this.getWorld(), mutable, this.asEntity());
                                this.onBlockCollision(pos, blockState);
                            } else if (!fluidState.isEmpty()) {
                                this.onBlockCollision(pos, fluidState);
                            }
                        } catch (Throwable var12) {
                            CrashReport crashReport = CrashReport.create(var12, "Colliding entity with block");
                            CrashReportSection crashReportSection = crashReport.addElement("Block being collided with");
                            CrashReportSection.addBlockInfo(crashReportSection, this.getWorld(), mutable, blockState);
                            throw new CrashException(crashReport);
                        }
                    }
                }
            }
        }
        if (this.isClipping())
            this.bounce(this.getForce().normalize());
    }
    default  <T, U> void onBlockCollision(BlockPos pos, State<T, U> state) {
        Vec3d force;
        if (state instanceof BlockState) force = PhysicsUtil.calculateBuoyancy(this, pos, (BlockState) state);
        else if (state instanceof FluidState) force = PhysicsUtil.calculateBuoyancy(this, pos, (FluidState) state);
        else throw new IllegalArgumentException("State argument not Block or Fluid!");
        this.applyForce(force);
    }

    boolean canHit(Entity entity);

    default void onCollision(HitResult hitResult) {
        MultiHitResult hitResult1 = (MultiHitResult) hitResult;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            onBlockHit(hitResult1);
        } else if (hitResult.getType() == HitResult.Type.ENTITY) {
            LiminalMod.LOGGER.info("Hit entity!");
            onEntityHit(hitResult1);
        }
    }
    default void onEntityHit (MultiHitResult hitResult) {
    }
    default void onBlockHit (HitResult hitResult1) {
        MultiHitResult hitResult = (MultiHitResult) hitResult1;
        Vec3d normal = hitResult.getNormal();
        this.bounce(normal);
    }

    @Override
    default void bounce(Vec3d normal) {
        if (normal.dotProduct(this.getGravityVector()) < 0)
            this.asEntity().setOnGround(true);
        Vec3d velocity = this.getVelocity();
        RigidBody.super.bounce(normal);
        //this.setPosition(hitResult1.getPos());
        if (!velocity.equals(this.getVelocity())) {
            this.setVelocity(this.getVelocity().multiply(1-this.getFriction()));
            if (!this.isClipping())
                adjustMovementForCollisions();
        }
    }

    default void adjustMovementForCollisions() {
        MultiHitResult hitResult = (MultiHitResult) PhysicsUtil.getCollision(this, this::canHit);
        //LiminalMod.LOGGER.info("normal: " + hitResult.getNormal().toString());
        boolean bl = false;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            for (HitResult hit : hitResult.getHits()) {
                BlockHitResult hit2 = (BlockHitResult) hit;
                BlockPos blockPos = hit2.getBlockPos();
                BlockState blockState = this.getWorld().getBlockState(blockPos);
                if (blockState.isOf(Blocks.NETHER_PORTAL)) {
                    this.asEntity().setInNetherPortal(blockPos);
                    bl = true;
                } else if (blockState.isOf(Blocks.END_GATEWAY)) {
                    BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                    if (blockEntity instanceof EndGatewayBlockEntity && EndGatewayBlockEntity.canTeleport(this.asEntity() )) {
                        EndGatewayBlockEntity.tryTeleportingEntity(this.getWorld(), blockPos, blockState, this.asEntity(), (EndGatewayBlockEntity)blockEntity);
                    }
                    bl = true;
                }
            }
        }
        if (hitResult.getType() != HitResult.Type.MISS && !bl) {
            this.onCollision(hitResult);
        } else {
            this.asEntity().setOnGround(false);
        }
    }

    @Override
    default void updateVelocity() {
        RigidBody.super.updateVelocity();
        this.setForce(Vec3d.ZERO);
    }

    default void tick() {
        this.asEntity().baseTick();
        this.setForce(Vec3d.ZERO);
        this.checkBlockCollision();
        if (!this.isClipping()) {
            this.setForce(
                    this.getVelocity().normalize().multiply(
                            -this.getVolume() * this.getAirResistance()));
            this.applyForce(this.getGravityVector().multiply(this.getMass()));
        }
        this.updateVelocity();
        if (!this.isClipping())
            this.adjustMovementForCollisions();

        if (this.asEntity().isOnGround()) {
            if (this.getVelocity().length() < 0.1f) {
                this.setVelocity(Vec3d.ZERO);
            } else if (!isClipping()) {
                this.applyForce(
                        this.getVelocity().normalize().multiply(
                                -this.getGravityVector().length() * this.getMass() * this.getFriction()));
            }
        }
        //LiminalMod.LOGGER.info("force: " + this.getForce() + "clipping: " + this.isClipping());
        this.updateVelocity();
        this.updatePos();
        this.setClipping(false);
    }
}
