package me.villagerunknown.innsandinnkeepers.feature;

import com.google.common.collect.ImmutableList;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.platform.util.ItemStackUtil;
import me.villagerunknown.platform.util.PotionsUtil;
import me.villagerunknown.platform.util.VillagerUtil;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradedItem;

import java.util.HashSet;
import java.util.Set;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class innkeeperVillagerFeature {
	
	public static final String INNKEEPER_STRING = "innkeeper";
	public static final Identifier INNKEEPER_IDENTIFIER = Identifier.of( MOD_ID, INNKEEPER_STRING );
	
	public static VillagerUtil.CustomVillager INNKEEPER = new VillagerUtil.CustomVillager( INNKEEPER_IDENTIFIER, getFireplaceBlockStates(), INNKEEPER_STRING, SoundEvents.ITEM_FLINTANDSTEEL_USE );

	public static void execute() {
		registerVillagerTrades();
	}
	
	public static ImmutableList<BlockState> getFireplaceBlockStates() {
		Set<BlockState> blockStates = new HashSet<>();
		
		fireplaceBlockFeature.BLOCKS.forEach( ( id, block ) -> {
			blockStates.addAll( block.getStateManager().getStates() );
		} );
		
		return ImmutableList.copyOf( blockStates );
	}
	
	private static void registerVillagerTrades() {
		// # Level 1
		TradeOfferHelper.registerVillagerOffers( INNKEEPER.PROFESSION, 1, f -> {
			if( Innsandinnkeepers.CONFIG.enableHearthstoneTrade ) {
				f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 1, new TradedItem( Items.EMERALD, 6 ), new ItemStack(hearthstoneItemFeature.HEARTHSTONE_ITEM, 1) ) );
			} else {
				f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 1, new TradedItem( Items.EMERALD, 3 ), ItemStackUtil.createWaterBottleStack() ) );
				f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 1, new TradedItem( Items.EMERALD, 3 ), new ItemStack( Items.COOKIE, 3 ) ) );
			} // if, else
			
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 1, new TradedItem( Items.EMERALD, 3 ), new ItemStack( Items.APPLE, 3 ) ) );
		} );
		
		// # Level 2
		TradeOfferHelper.registerVillagerOffers( INNKEEPER.PROFESSION, 2, f -> {
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 2, new TradedItem( Items.EMERALD, 6 ), new ItemStack( Items.BAKED_POTATO, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 2, new TradedItem( Items.EMERALD, 6 ), new ItemStack( Items.BREAD, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 2, new TradedItem( Items.EMERALD, 6 ), new ItemStack( Items.BEETROOT_SOUP, 6 ) ) );
		} );
		
		// # Level 3
		TradeOfferHelper.registerVillagerOffers( INNKEEPER.PROFESSION, 3, f -> {
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 3, new TradedItem( Items.EMERALD, 8 ), new ItemStack( Items.COOKED_COD, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 3, new TradedItem( Items.EMERALD, 8 ), new ItemStack( Items.COOKED_SALMON, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 3, new TradedItem( Items.EMERALD, 8 ), new ItemStack( Items.COOKED_RABBIT, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 3, new TradedItem( Items.EMERALD, 8 ), new ItemStack( Items.COOKED_CHICKEN, 6 ) ) );
			
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 3, new TradedItem( Items.EMERALD, 6 ), PotionsUtil.HEALING_POTION ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 3, new TradedItem( Items.EMERALD, 6 ), PotionsUtil.REGENERATION_POTION ) );
		} );
		
		// # Level 4
		TradeOfferHelper.registerVillagerOffers( INNKEEPER.PROFESSION, 4, f -> {
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 4, new TradedItem( Items.EMERALD, 12 ), new ItemStack( Items.COOKED_BEEF, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 4, new TradedItem( Items.EMERALD, 12 ), new ItemStack( Items.COOKED_MUTTON, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 4, new TradedItem( Items.EMERALD, 12 ), new ItemStack( Items.COOKED_PORKCHOP, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 4, new TradedItem( Items.EMERALD, 12 ), PotionsUtil.STRONG_HEALING_POTION ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 4, new TradedItem( Items.EMERALD, 12 ), PotionsUtil.STRONG_REGENERATION_POTION ) );
		} );
		
		// # Level 5
		TradeOfferHelper.registerVillagerOffers( INNKEEPER.PROFESSION, 5, f -> {
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 5, new TradedItem( Items.EMERALD, 16 ), new ItemStack( Items.GOLDEN_CARROT, 6 ) ) );
			f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 5, new TradedItem( Items.EMERALD, 16 ), PotionsUtil.LONG_REGENERATION_POTION ) );
			
			if( Innsandinnkeepers.CONFIG.enableGoldenAppleTrade ) {
				f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 5, new TradedItem( Items.EMERALD, 32 ), new ItemStack( Items.GOLDEN_APPLE, 1 ) ) );
			} // if
			
			if( Innsandinnkeepers.CONFIG.enableEnchantedGoldenAppleTrade ) {
				f.add( (entity, random) -> VillagerUtil.sellTradeOffer( 5, new TradedItem( Items.EMERALD, 64 ), new ItemStack( Items.ENCHANTED_GOLDEN_APPLE, 1 ) ) );
			} // if
		} );
	}
	
}
