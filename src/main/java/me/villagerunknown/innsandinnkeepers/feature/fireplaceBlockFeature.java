package me.villagerunknown.innsandinnkeepers.feature;

import me.villagerunknown.innsandinnkeepers.block.FireplaceBlock;
import me.villagerunknown.innsandinnkeepers.entity.block.FireplaceBlockEntity;
import me.villagerunknown.platform.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class fireplaceBlockFeature {
	
	public static final String FIREPLACE_STRING = "fireplace";
	public static Block FIREPLACE_BLOCK = null;
	public static BlockItem FIREPLACE_BLOCK_ITEM = null;
	public static BlockEntityType<FireplaceBlockEntity> FIREPLACE_BLOCK_ENTITY = null;
	
	public static void execute() {
		registerBlock();
	}
	
	private static void registerBlock() {
		FIREPLACE_BLOCK = new FireplaceBlock();
		FIREPLACE_BLOCK_ITEM = new BlockItem( FIREPLACE_BLOCK, new Item.Settings() );
		
		RegistryUtil.registerItem( FIREPLACE_STRING, FIREPLACE_BLOCK_ITEM, MOD_ID );
		RegistryUtil.registerBlock( FIREPLACE_STRING, FIREPLACE_BLOCK, MOD_ID );
		
		BlockEntityType.Builder<FireplaceBlockEntity> builder = BlockEntityType.Builder.create(
				FireplaceBlockEntity::new,
				FIREPLACE_BLOCK
		);
		
		FIREPLACE_BLOCK_ENTITY = Registry.register(
				Registries.BLOCK_ENTITY_TYPE,
				Identifier.of(MOD_ID, FIREPLACE_STRING),
				builder.build()
		);
	}
	
}
