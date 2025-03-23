package me.villagerunknown.innsandinnkeepers;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "villagerunknown-innsandinnkeepers")
public class InnsandinnkeepersConfigData implements me.shedaniel.autoconfig.ConfigData {
	
	/**
	 * Fireplaces
	 */
	
	@ConfigEntry.Category("Fireplaces")
	public int maxFireplaceSmokeThroughBlocks = 16;
	
	/**
	 * Innkeepers
	 */
	
	@ConfigEntry.Category("Innkeepers")
	public boolean enableHearthstoneTrade = true;
	
	@ConfigEntry.Category("Innkeepers")
	public boolean enableGoldenAppleTrade = false;
	
	@ConfigEntry.Category("Innkeepers")
	public boolean enableEnchantedGoldenAppleTrade = false;
	
	/**
	 * Hearthstones
	 */
	
	@ConfigEntry.Category("Hearthstones")
	public int hearthstoneUseTime = 80;
	
	@ConfigEntry.Category("Hearthstones")
	public int hearthstoneCooldownTime = 1000;
	
}
