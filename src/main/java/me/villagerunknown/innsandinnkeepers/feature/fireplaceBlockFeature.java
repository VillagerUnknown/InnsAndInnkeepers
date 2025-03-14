package me.villagerunknown.innsandinnkeepers.feature;

import me.villagerunknown.innsandinnkeepers.block.FireplaceBlock;
import me.villagerunknown.innsandinnkeepers.entity.block.FireplaceBlockEntity;
import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import me.villagerunknown.platform.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.*;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class fireplaceBlockFeature {
	
	private static final List<String> blockTypes = new ArrayList<>(List.of(
			"cobblestone",
			"mossy_cobblestone",
			"brick",
			"stone_brick",
			"mossy_stone_brick",
			"deepslate_brick",
			"tuff_brick",
			"mud_brick",
			"polished_blackstone_brick"
	));
	
	public static final String FIREPLACE_STRING = "fireplace";
	public static ScreenHandlerType<FireplaceScreenHandler> FIREPLACE_SCREEN_HANDLER = new ScreenHandlerType<>(FireplaceScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static BlockEntityType<FireplaceBlockEntity> FIREPLACE_BLOCK_ENTITY = null;
	
	public static Map<String, BlockEntityType> BLOCK_ENTITY_TYPES = new HashMap<>();
	public static Map<String, Block> BLOCKS = new HashMap<>();
	
	public static void execute() {
		registerScreenHandler();
		registerBlocks();
		registerBlockEntityType();
	}
	
	private static void registerBlocks() {
		Collections.sort( blockTypes );
		
		for (String blockType : blockTypes) {
			registerBlock( blockType );
		} // for
	}
	
	private static void registerBlock( String blockType ) {
		Block block = new FireplaceBlock();
		
		RegistryUtil.addItemToGroup( ItemGroups.FUNCTIONAL, RegistryUtil.registerItem( blockType + "_" + FIREPLACE_STRING, new BlockItem( block, new Item.Settings() ), MOD_ID ) );
		
		BLOCKS.put( blockType + "_" + FIREPLACE_STRING, RegistryUtil.registerBlock( blockType + "_" + FIREPLACE_STRING, block, MOD_ID ) );
	}
	
	private static void registerBlockEntityType() {
		FIREPLACE_BLOCK_ENTITY = BlockEntityType.Builder.create(
				FireplaceBlockEntity::new,
				BLOCKS.get( "cobblestone_fireplace" ),
				BLOCKS.get( "mossy_cobblestone_fireplace" ),
				BLOCKS.get( "brick_fireplace" ),
				BLOCKS.get( "stone_brick_fireplace" ),
				BLOCKS.get( "mossy_stone_brick_fireplace" ),
				BLOCKS.get( "deepslate_brick_fireplace" ),
				BLOCKS.get( "tuff_brick_fireplace" ),
				BLOCKS.get( "mud_brick_fireplace" ),
				BLOCKS.get( "polished_blackstone_brick_fireplace" )
		).build();
		
		BLOCK_ENTITY_TYPES.put( FIREPLACE_STRING, Registry.register( Registries.BLOCK_ENTITY_TYPE, Identifier.of(MOD_ID, FIREPLACE_STRING), FIREPLACE_BLOCK_ENTITY ) );
	}
	
	private static void registerScreenHandler() {
		Registry.register( Registries.SCREEN_HANDLER, Identifier.of( MOD_ID, FIREPLACE_STRING ), FIREPLACE_SCREEN_HANDLER );
	}
	
}
