package com.axork99.liminalmod.entity;

import net.minecraft.util.math.Vec3d;

public interface Bouncing {

    Vec3d getVelocity();

    Type getBouncngType ();

    void setVelocity(Vec3d velocity);

    default void bounce (Vec3d normal) {
        if (getVelocity().dotProduct(normal) < 0)
            this.setVelocity(normal.multiply(normal.dotProduct(this.getVelocity()) * -2)
                            .add(this.getVelocity())
                            .multiply(1 - this.getBouncngType().getEnergyLoss()));
    }

    public static enum Type {
        PERFECT_ELASTIC(0.0),
        ELASTIC_80(0.2),
        ELASTIC_50(0.5),
        ELASTIC_25(0.75),
        PLASTIC(1.0);

        private final double energy_loss;

        Type (double energy_loss) {
            this.energy_loss = energy_loss;
        }

        public double getEnergyLoss() {
            return energy_loss;
        }

    }
}
