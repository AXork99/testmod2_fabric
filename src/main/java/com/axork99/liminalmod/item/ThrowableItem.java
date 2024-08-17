package com.axork99.liminalmod.item;

import com.axork99.liminalmod.entity.projectile.thrown.EggplantEntity;
import com.axork99.liminalmod.utils.ItemSettingsWrapper;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.BiFunction;

public class ThrowableItem extends Item {
    private final SoundEvent throwSound;
    private final SoundCategory soundCategory;
    private final BiFunction<PlayerEntity, World, ? extends ThrownItemEntity> thrownItem;

    public ThrowableItem(Settings settings) {
        super(settings);
        this.throwSound = settings.throwSound;
        this.soundCategory = settings.soundCategory;
        this.thrownItem = settings.thrownItem;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(
                null,
                user.getX(),
                user.getY(),
                user.getZ(),
                this.throwSound,
                this.soundCategory,
                0.5F,
                0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
        );
        if (!world.isClient) {
            ThrownItemEntity throwableEntity = thrownItem.apply(user, world);
            throwableEntity.setItem(itemStack);
            throwableEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
            world.spawnEntity(throwableEntity);
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    public static class Settings<T extends Settings<T>> extends ItemSettingsWrapper<T> {

        private SoundEvent throwSound = SoundEvents.ENTITY_SNOWBALL_THROW;
        private SoundCategory soundCategory = SoundCategory.NEUTRAL;
        private BiFunction<PlayerEntity, World, ? extends ThrownItemEntity> thrownItem = (playerEntity, world) -> new SnowballEntity(world, playerEntity);

        public T throwSound(SoundEvent sound, SoundCategory category) {
            this.throwSound = sound;
            this.soundCategory = category;
            return (T) this;
        }

        public T entityThrown(BiFunction<PlayerEntity, World, ? extends ThrownItemEntity> thrownItem) {
            this.thrownItem = thrownItem;
            return (T) this;
        }
    }

    public static interface Throwable extends Ownable, FlyingItemEntity {
        boolean getLeftStatus();
        void setLeftStatus(boolean leftStatus);
        boolean getShot();
        void setShot(boolean shot);
    }

}
