package me.villagerunknown.innsandinnkeepers;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "villagerunknown-innsandinnkeepers")
public class InnsandinnkeepersConfigData implements me.shedaniel.autoconfig.ConfigData {
	
	/**
	 * General
	 */
	
	@ConfigEntry.Category("General")
	public int maxFireplaceSmokeThroughBlocks = 16;
	
	/**
	 * Innkeepers
	 */
	
	@ConfigEntry.Category("Innkeepers")
	public boolean enableHearthstoneTrade = false;
	
	@ConfigEntry.Category("Innkeepers")
	public boolean enableGoldenAppleTrade = false;
	
	@ConfigEntry.Category("Innkeepers")
	public boolean enableEnchantedGoldenAppleTrade = false;
	
}
