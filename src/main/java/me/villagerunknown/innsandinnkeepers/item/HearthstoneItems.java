package me.villagerunknown.innsandinnkeepers.item;

import me.villagerunknown.innsandinnkeepers.feature.hearthstoneItemFeature;
import net.minecraft.item.Item;

public class HearthstoneItems {
	
	public static Item HEARTHSTONE_ITEM;
	public static Item RED_HEARTHSTONE_ITEM;
	public static Item GREEN_HEARTHSTONE_ITEM;
	public static Item YELLOW_HEARTHSTONE_ITEM;
	public static Item ORANGE_HEARTHSTONE_ITEM;
	public static Item PURPLE_HEARTHSTONE_ITEM;
	
	public HearthstoneItems(){}
	
	static{
		HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( hearthstoneItemFeature.HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1) ) );
		RED_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "red_" + hearthstoneItemFeature.HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1) ) );
		GREEN_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "green_" + hearthstoneItemFeature.HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1) ) );
		YELLOW_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "yellow_" + hearthstoneItemFeature.HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1) ) );
		ORANGE_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "orange_" + hearthstoneItemFeature.HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1) ) );
		PURPLE_HEARTHSTONE_ITEM = hearthstoneItemFeature.registerHearthstoneItem( "purple_" + hearthstoneItemFeature.HEARTHSTONE_STRING, new HearthstoneItem( new Item.Settings().maxCount(1) ) );
	}
	
}
