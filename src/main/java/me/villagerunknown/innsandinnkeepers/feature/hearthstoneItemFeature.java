package me.villagerunknown.innsandinnkeepers.feature;

import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.item.HearthstoneItem;
import me.villagerunknown.innsandinnkeepers.item.HearthstoneItems;
import me.villagerunknown.platform.util.RegistryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class hearthstoneItemFeature {
	
	public static String HEARTHSTONE_STRING = "hearthstone";
	
	public static Set<Item> HEARTHSTONE_ITEMS = new HashSet<>(Arrays.asList(
			HearthstoneItems.HEARTHSTONE_ITEM,
			HearthstoneItems.RED_HEARTHSTONE_ITEM,
			HearthstoneItems.GREEN_HEARTHSTONE_ITEM,
			HearthstoneItems.YELLOW_HEARTHSTONE_ITEM,
			HearthstoneItems.ORANGE_HEARTHSTONE_ITEM,
			HearthstoneItems.PURPLE_HEARTHSTONE_ITEM
	));
	
	public static void execute() {
		new HearthstoneItems();
	}
	
	public static Item registerHearthstoneItem( String id, Item item ) {
		Item registeredItem = RegistryUtil.registerItem( id, item, Innsandinnkeepers.MOD_ID );
		
		RegistryUtil.addItemToGroup( Innsandinnkeepers.CUSTOM_ITEM_GROUP_KEY, registeredItem );
		
		return registeredItem;
	}
	
}
