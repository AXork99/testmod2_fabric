package com.axork99.liminalmod.mixin;

import com.axork99.liminalmod.block.ModBlocks;
import com.axork99.liminalmod.item.ModItems;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(HoeItem.class)
public class HoeHarvest {
	@Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
	private void useOnBlock(@NotNull ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> HARVEST_ACTIONS = Maps.<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>>newHashMap(
				ImmutableMap.of(
						ModBlocks.END_FOLIAGE,
						Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAndDropAction(Blocks.END_STONE.getDefaultState(), ModItems.EXOTIC_MATTER)),
						Blocks.GRASS_BLOCK,
						Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAndDropAction(Blocks.DIRT.getDefaultState(), Items.WHEAT_SEEDS))
				)
		);
		World world2 = context.getWorld();
		BlockPos blockPos2 = context.getBlockPos();
		Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair2 = (Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>) HARVEST_ACTIONS.get(
				world2.getBlockState(blockPos2).getBlock()
		);
		if (pair2 != null) {
			Predicate<ItemUsageContext> predicate = pair2.getFirst();
			Consumer<ItemUsageContext> consumer = pair2.getSecond();
			if (predicate.test(context)) {
				PlayerEntity playerEntity = context.getPlayer();
				world2.playSound(playerEntity, blockPos2, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
				if (!world2.isClient) {
					consumer.accept(context);
					if (playerEntity != null) {
						context.getStack().damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
					}
				}
				cir.setReturnValue(ActionResult.SUCCESS);
			}
		}
	}
}