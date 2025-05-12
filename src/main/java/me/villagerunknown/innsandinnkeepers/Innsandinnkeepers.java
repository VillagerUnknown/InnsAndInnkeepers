package me.villagerunknown.innsandinnkeepers;

import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.innsandinnkeepers.feature.hearthstoneItemFeature;
import me.villagerunknown.innsandinnkeepers.feature.innkeeperVillagerFeature;
import me.villagerunknown.innsandinnkeepers.item.HearthstoneItems;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.PlatformMod;
import me.villagerunknown.platform.manager.featureManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public class Innsandinnkeepers implements ModInitializer {
	
	public static PlatformMod<InnsandinnkeepersConfigData> MOD = Platform.register( "innsandinnkeepers", Innsandinnkeepers.class, InnsandinnkeepersConfigData.class );
	public static String MOD_ID = MOD.getModId();
	public static Logger LOGGER = MOD.getLogger();
	public static InnsandinnkeepersConfigData CONFIG = MOD.getConfig();
	
	public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "item_group"));
	public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(HearthstoneItems.HEARTHSTONE_ITEM))
			.displayName(Text.translatable("itemGroup." + MOD_ID))
			.build();
	
	@Override
	public void onInitialize() {
		// # Initialize Mod with Platform
		Platform.init_mod( MOD );
		
		// Register Item Group for Hearthstones and Fireplaces
		Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
		
		// # Activate Features
		featureManager.addFeature( "fireplace-block", fireplaceBlockFeature::execute );
		featureManager.addFeature( "hearthstone-item", hearthstoneItemFeature::execute );
		featureManager.addFeature( "innkeeper-villager", innkeeperVillagerFeature::execute );
		
		// # Load Features
		featureManager.loadFeatures();
	}
	
}
