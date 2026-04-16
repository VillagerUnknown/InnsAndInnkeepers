package me.villagerunknown.innsandinnkeepers.item;

import me.villagerunknown.innsandinnkeepers.feature.hearthstoneItemFeature;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;
import static me.villagerunknown.innsandinnkeepers.feature.hearthstoneItemFeature.HEARTHSTONE_STRING;

public class HearthstoneItems {
	
	public static Item HEARTHSTONE_ITEM;
	public static Item RED_HEARTHSTONE_ITEM;
	public static Item GREEN_HEARTHSTONE_ITEM;
	public static Item YELLOW_HEARTHSTONE_ITEM;
	public static Item ORANGE_HEARTHSTONE_ITEM;
	public static Item PURPLE_HEARTHSTONE_ITEM;
	
	public HearthstoneItems(){}
	
	static{
		HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID,HEARTHSTONE_STRING))) ) );
		RED_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "red_" + HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID,HEARTHSTONE_STRING))) ) );
		GREEN_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "green_" + HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID,HEARTHSTONE_STRING))) ) );
		YELLOW_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "yellow_" + HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID,HEARTHSTONE_STRING))) ) );
		ORANGE_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "orange_" + HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID,HEARTHSTONE_STRING))) ) );
		PURPLE_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "purple_" + HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID,HEARTHSTONE_STRING))) ) );
	}
	
}
