package me.villagerunknown.innsandinnkeepers.feature;

import me.villagerunknown.innsandinnkeepers.block.FireplaceBlock;
import me.villagerunknown.innsandinnkeepers.entity.block.FireplaceBlockEntity;
import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import me.villagerunknown.platform.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class fireplaceBlockFeature {
	
	public static final String FIREPLACE_STRING = "fireplace";
	public static ScreenHandlerType<FireplaceScreenHandler> FIREPLACE_SCREEN_HANDLER = new ScreenHandlerType<>(FireplaceScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static Block FIREPLACE_BLOCK = new FireplaceBlock();
	public static BlockItem FIREPLACE_BLOCK_ITEM = new BlockItem( FIREPLACE_BLOCK, new Item.Settings() );
	public static BlockEntityType<FireplaceBlockEntity> FIREPLACE_BLOCK_ENTITY = BlockEntityType.Builder.create( FireplaceBlockEntity::new, FIREPLACE_BLOCK ).build();
	
	public static void execute() {
		registerScreenHandler();
		registerBlock();
	}
	
	private static void registerBlock() {
		RegistryUtil.registerItem( FIREPLACE_STRING, FIREPLACE_BLOCK_ITEM, MOD_ID );
		RegistryUtil.registerBlock( FIREPLACE_STRING, FIREPLACE_BLOCK, MOD_ID );
		Registry.register( Registries.BLOCK_ENTITY_TYPE, Identifier.of(MOD_ID, FIREPLACE_STRING), FIREPLACE_BLOCK_ENTITY );
	}
	
	private static void registerScreenHandler() {
		Registry.register( Registries.SCREEN_HANDLER, Identifier.of( MOD_ID, FIREPLACE_STRING ), FIREPLACE_SCREEN_HANDLER );
	}
	
}
