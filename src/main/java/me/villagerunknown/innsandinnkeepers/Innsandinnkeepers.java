package me.villagerunknown.innsandinnkeepers;

import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.innsandinnkeepers.feature.innkeeperVillagerFeature;
import me.villagerunknown.platform.Platform;
import me.villagerunknown.platform.PlatformMod;
import me.villagerunknown.platform.manager.featureManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class Innsandinnkeepers implements ModInitializer {
	
	public static PlatformMod<InnsandinnkeepersConfigData> MOD = null;
	public static String MOD_ID = null;
	public static Logger LOGGER = null;
	public static InnsandinnkeepersConfigData CONFIG = null;
	
	@Override
	public void onInitialize() {
		// # Register Mod w/ Platform
		MOD = Platform.register( "innsandinnkeepers", Innsandinnkeepers.class, InnsandinnkeepersConfigData.class );
		
		MOD_ID = MOD.getModId();
		LOGGER = MOD.getLogger();
		CONFIG = MOD.getConfig();
		
		// # Initialize Mod
		init();
	}
	
	private static void init() {
		Platform.init_mod( MOD );
		
		// # Activate Features
		featureManager.addFeature( "fireplaceBlock", fireplaceBlockFeature::execute );
		featureManager.addFeature( "innkeeperVillager", innkeeperVillagerFeature::execute );
//		featureManager.addFeature( "hearthstoneItem", hearthstoneItemFeature::execute );
	}
	
}
