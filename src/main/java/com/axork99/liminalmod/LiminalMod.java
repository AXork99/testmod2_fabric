package com.axork99.liminalmod;

import com.axork99.liminalmod.block.ModBlocks;
import com.axork99.liminalmod.entity.ModEntityTypes;
import com.axork99.liminalmod.item.ModItemGroups;
import com.axork99.liminalmod.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiminalMod implements ModInitializer {
	public static final String MOD_ID = "liminalmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Liminal Mod");
		ModEntityTypes.RegisterEntityTypes();
		ModItems.registerModItems();
		ModItemGroups.registerItemGroups();
		ModBlocks.registerModBlocks();
	}
}