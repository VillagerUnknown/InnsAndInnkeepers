package me.villagerunknown.innsandinnkeepers.feature;

import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.block.FireplaceBlock;
import me.villagerunknown.innsandinnkeepers.entity.block.FireplaceBlockEntity;
import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import me.villagerunknown.platform.util.RegistryUtil;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.*;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class fireplaceBlockFeature {
	
	private static final List<String> blockTypes = new ArrayList<>(List.of(
			"cobblestone",
			"cobbled_deepslate",
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
	
	public static final Item DEFAULT_FUEL = Items.STICK;
	
	public static final int MINIMUM_SAFE_TELEPORT_RANGE = 2;
	
	public static final List<SimpleParticleType> EXTRA_OVERWORLD_SMOKE_PARTICLES = Arrays.asList(
			ParticleTypes.ASH,
			ParticleTypes.WHITE_ASH,
			ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
			ParticleTypes.WHITE_SMOKE,
			ParticleTypes.SMOKE,
			ParticleTypes.LARGE_SMOKE,
			ParticleTypes.DUST_PLUME,
			ParticleTypes.POOF
	);
	
	public static final List<SimpleParticleType> EXTRA_NETHER_SMOKE_PARTICLES = Arrays.asList(
			ParticleTypes.ASH,
			ParticleTypes.WHITE_ASH,
			ParticleTypes.SMOKE,
			ParticleTypes.LARGE_SMOKE,
			ParticleTypes.FLAME,
			ParticleTypes.SMALL_FLAME,
			ParticleTypes.CRIMSON_SPORE
	);
	
	public static final List<SimpleParticleType> EXTRA_END_SMOKE_PARTICLES = Arrays.asList(
			ParticleTypes.ASH,
			ParticleTypes.WHITE_ASH,
			ParticleTypes.PORTAL,
			ParticleTypes.DRAGON_BREATH,
			ParticleTypes.END_ROD
	);
	
	public static ScreenHandlerType<FireplaceScreenHandler> FIREPLACE_SCREEN_HANDLER = new ScreenHandlerType<>(FireplaceScreenHandler::new, FeatureFlags.VANILLA_FEATURES);
	public static BlockEntityType<FireplaceBlockEntity> FIREPLACE_BLOCK_ENTITY = null;
	
	public static Map<String, BlockEntityType> BLOCK_ENTITY_TYPES = new HashMap<>();
	public static Map<String, Block> BLOCKS = new HashMap<>();
	
	public static void execute() {
		registerScreenHandler();
		registerBlocks();
	}
	
	private static void registerBlocks() {
		Collections.sort( blockTypes );
		
		for (String blockType : blockTypes) {
			registerBlock( blockType );
		} // for
		
		registerBlockEntityType();
	}
	
	private static void registerBlock( String blockType ) {
		String blockName = blockType + "_" + FIREPLACE_STRING;
		
		Block block = new FireplaceBlock( blockName );
		
		Identifier id = Identifier.of(MOD_ID,blockName);
		
		RegistryUtil.addItemToGroup( ItemGroups.FUNCTIONAL, RegistryUtil.registerItem( blockName, new BlockItem( block, new Item.Settings().useBlockPrefixedTranslationKey().registryKey(RegistryKey.of(RegistryKeys.ITEM, id)) ), MOD_ID ) );
		
		BLOCKS.put( blockName, RegistryUtil.registerBlock( blockName, block, MOD_ID ) );
	}
	
	private static void registerBlockEntityType() {
		FIREPLACE_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(
				FireplaceBlockEntity::new,
				BLOCKS.get( "cobblestone_fireplace" ),
				BLOCKS.get( "cobbled_deepslate_fireplace" ),
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
