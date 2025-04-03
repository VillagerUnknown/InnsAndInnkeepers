package me.villagerunknown.innsandinnkeepers.feature;

import me.villagerunknown.innsandinnkeepers.item.HearthstoneItem;
import me.villagerunknown.platform.util.RegistryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class hearthstoneItemFeature {
	
	public static String HEARTHSTONE_STRING = "hearthstone";
	public static Item HEARTHSTONE_ITEM = new HearthstoneItem( new Item.Settings().maxCount(1).registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID,HEARTHSTONE_STRING))) );
	
	public static void execute() {
		registerHearthstoneItem();
	}
	
	private static void registerHearthstoneItem() {
		RegistryUtil.registerItem( HEARTHSTONE_STRING, HEARTHSTONE_ITEM, MOD_ID );
		RegistryUtil.addItemToGroup( ItemGroups.TOOLS, HEARTHSTONE_ITEM );
	}
	
}
