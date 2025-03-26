package me.villagerunknown.innsandinnkeepers.feature;

import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.item.HearthstoneItem;
import me.villagerunknown.platform.util.RegistryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

public class hearthstoneItemFeature {
	
	public static String HEARTHSTONE_STRING = "hearthstone";
	public static Item HEARTHSTONE_ITEM = new HearthstoneItem( new Item.Settings().maxCount(1) );
	
	public static void execute() {
		registerHearthstoneItem();
	}
	
	private static void registerHearthstoneItem() {
		RegistryUtil.registerItem( HEARTHSTONE_STRING, HEARTHSTONE_ITEM, Innsandinnkeepers.MOD_ID );
		RegistryUtil.addItemToGroup( ItemGroups.TOOLS, HEARTHSTONE_ITEM );
	}
	
}
