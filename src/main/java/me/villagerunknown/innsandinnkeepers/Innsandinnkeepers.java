package me.villagerunknown.innsandinnkeepers;

import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.innsandinnkeepers.feature.hearthstoneItemFeature;
import me.villagerunknown.innsandinnkeepers.feature.innkeeperVillagerFeature;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.PlatformMod;
import me.villagerunknown.platform.manager.featureManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class Innsandinnkeepers implements ModInitializer {
	
	public static PlatformMod<InnsandinnkeepersConfigData> MOD = Platform.register( "innsandinnkeepers", Innsandinnkeepers.class, InnsandinnkeepersConfigData.class );
	public static String MOD_ID = MOD.getModId();
	public static Logger LOGGER = MOD.getLogger();
	public static InnsandinnkeepersConfigData CONFIG = MOD.getConfig();
	
	@Override
	public void onInitialize() {
		// # Initialize Mod
		init();
	}
	
	private static void init() {
		Platform.init_mod( MOD );
		
		// # Activate Features
		featureManager.addFeature( "fireplaceBlock", fireplaceBlockFeature::execute );
		featureManager.addFeature( "innkeeperVillager", innkeeperVillagerFeature::execute );
		featureManager.addFeature( "hearthstoneItem", hearthstoneItemFeature::execute );
		
		// # Load Features
		featureManager.loadFeatures();
	}
	
}
