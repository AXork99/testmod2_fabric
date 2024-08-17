package com.axork99.liminalmod.utils;

import com.axork99.liminalmod.LiminalMod;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class MultiHitResult extends HitResult {
    private Collection<HitResult> hits;
    private Vec3d normal = Vec3d.ZERO;
    private Type type = Type.MISS;

    protected MultiHitResult(Vec3d pos, Collection<HitResult> hits) {
        super(pos);
        this.hits = new HashSet<>();
        for (HitResult hit : hits) {
            if (hit.getType() == Type.ENTITY) {
                this.type = Type.ENTITY;
                this.hits = Set.of(hit);
                this.normal = Vec3d.ZERO;
                return;
            } else if (hit.getType() == Type.BLOCK) {
                //LiminalMod.LOGGER.info("hit at: " + hit.getPos());
                this.type = Type.BLOCK;
                Vec3i i = ((BlockHitResult) hit).getSide().getVector();
                this.normal = this.normal.add(new Vec3d(i.getX(), i.getY(), i.getZ()));
                this.hits.add(hit);
            }
        }
    }

    public MultiHitResult(Collection<HitResult> hits) {
        this(PhysicsUtil.findAverage(hits.stream().map(HitResult::getPos).collect(Collectors.toSet())), hits);
    }

    public Collection<HitResult> getHits() {
        return hits;
    }

    public Vec3d getNormal () {
        return normal.normalize();
    }

    public HitResult.Type getType() {
        return type;
    }
}