package me.villagerunknown.innsandinnkeepers.feature;

import com.google.common.collect.ImmutableSet;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.platform.util.ListUtil;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.ai.brain.task.ScheduleActivityTask;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterest;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class innkeeperVillagerFeature {
	
	private static final int DEFAULT_MAX_USES = 12;
	private static final int COMMON_MAX_USES = 16;
	private static final int RARE_MAX_USES = 3;
	private static final int NOVICE_SELL_XP = 1;
	private static final int NOVICE_BUY_XP = 2;
	private static final int APPRENTICE_SELL_XP = 5;
	private static final int APPRENTICE_BUY_XP = 10;
	private static final int JOURNEYMAN_SELL_XP = 10;
	private static final int JOURNEYMAN_BUY_XP = 20;
	private static final int EXPERT_SELL_XP = 15;
	private static final int EXPERT_BUY_XP = 30;
	private static final int MASTER_TRADE_XP = 30;
	private static final float LOW_PRICE_MULTIPLIER = 0.05F;
	private static final float HIGH_PRICE_MULTIPLIER = 0.2F;
	
	public static final String INNKEEPER_STRING = "innkeeper";
	public static final Identifier INNKEEPER_IDENTIFIER = Identifier.of( MOD_ID, INNKEEPER_STRING );
	
	public static PointOfInterestType INNKEEPER_POI_TYPE = null;
	public static RegistryKey<PointOfInterestType> INNKEEPER_POI_TYPE_REGISTRY_KEY = null;
	public static RegistryEntry<PointOfInterestType> INNKEEPER_POI_TYPE_REGISTRY = null;
	public static VillagerProfession INNKEEPER_PROFESSION = null;
	public static RegistryEntry<VillagerProfession> INNKEEPER_PROFESSION_REGISTRY = null;

	public static void execute() {
		registerPointOfInterest();
		registerVillagerProfession();
		registerVillagerTrades();
	}
	
	private static void registerPointOfInterest() {
		Set<BlockState> blockStates = new HashSet<>();
		
		fireplaceBlockFeature.BLOCKS.forEach( ( id, block ) -> {
			blockStates.addAll( block.getStateManager().getStates() );
		} );
		
		INNKEEPER_POI_TYPE = PointOfInterestHelper.register( INNKEEPER_IDENTIFIER, 1, 1, blockStates );
		
		Registry.register( Registries.POINT_OF_INTEREST_TYPE, INNKEEPER_IDENTIFIER, INNKEEPER_POI_TYPE );
		
		INNKEEPER_POI_TYPE_REGISTRY = Registries.POINT_OF_INTEREST_TYPE.getEntry( INNKEEPER_POI_TYPE );
		INNKEEPER_POI_TYPE_REGISTRY_KEY =  RegistryKey.of( RegistryKeys.POINT_OF_INTEREST_TYPE, INNKEEPER_IDENTIFIER );
	}
	
	private static void registerVillagerProfession() {
		Predicate<RegistryEntry<PointOfInterestType>> predicate = (entry) -> entry.matchesKey( INNKEEPER_POI_TYPE_REGISTRY_KEY );
		
		INNKEEPER_PROFESSION = new VillagerProfession( INNKEEPER_STRING, predicate, predicate, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ITEM_FLINTANDSTEEL_USE );
		
		Registry.register( Registries.VILLAGER_PROFESSION, INNKEEPER_IDENTIFIER, INNKEEPER_PROFESSION );
		
		INNKEEPER_PROFESSION_REGISTRY = Registries.VILLAGER_PROFESSION.getEntry( INNKEEPER_PROFESSION );
	}
	
	private static void registerVillagerTrades() {
		// # Level 1
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 1, f -> {
			if( Innsandinnkeepers.CONFIG.enableHearthstoneTrade ) {
				f.add((entity, random) -> new TradeOffer(
						new TradedItem(Items.EMERALD, 6),
						new ItemStack(hearthstoneItemFeature.HEARTHSTONE_ITEM, 1),
						DEFAULT_MAX_USES,
						NOVICE_BUY_XP,
						LOW_PRICE_MULTIPLIER
				));
			} else {
				ItemStack water = new ItemStack( Items.POTION, 1 );
				water.set(DataComponentTypes.ITEM_NAME, Text.translatable( "item.minecraft.potion.effect.water" ) );
				f.add( (entity, random) -> new TradeOffer(
						new TradedItem( Items.EMERALD, 3 ),
						water,
						DEFAULT_MAX_USES,
						NOVICE_BUY_XP,
						LOW_PRICE_MULTIPLIER
				));
				f.add( (entity, random) -> new TradeOffer(
						new TradedItem( Items.EMERALD, 3 ),
						new ItemStack( Items.COOKIE, 3 ),
						DEFAULT_MAX_USES,
						NOVICE_BUY_XP,
						LOW_PRICE_MULTIPLIER
				));
			} // if, else
			
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 3 ),
					new ItemStack( Items.APPLE, 3 ),
					DEFAULT_MAX_USES,
					NOVICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
		} );
		
		// # Level 2
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 2, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 6 ),
					new ItemStack( Items.BAKED_POTATO, 6 ),
					DEFAULT_MAX_USES,
					NOVICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 6 ),
					new ItemStack( Items.BREAD, 6 ),
					DEFAULT_MAX_USES,
					NOVICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 6 ),
					new ItemStack( Items.BEETROOT_SOUP ),
					DEFAULT_MAX_USES,
					NOVICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
		} );
		
		// # Level 3
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 3, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 8 ),
					new ItemStack( Items.COOKED_COD, 6 ),
					COMMON_MAX_USES,
					APPRENTICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 8 ),
					new ItemStack( Items.COOKED_SALMON, 6 ),
					COMMON_MAX_USES,
					APPRENTICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 8 ),
					new ItemStack( Items.COOKED_RABBIT, 6 ),
					COMMON_MAX_USES,
					APPRENTICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 8 ),
					new ItemStack( Items.COOKED_CHICKEN, 6 ),
					COMMON_MAX_USES,
					APPRENTICE_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 8 ),
					createPotionStack( Text.translatable("item.minecraft.potion.effect.healing"), Potions.HEALING ),
					RARE_MAX_USES,
					APPRENTICE_BUY_XP,
					HIGH_PRICE_MULTIPLIER
			));
			
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 8 ),
					createPotionStack( Text.translatable("item.minecraft.potion.effect.regeneration"), Potions.REGENERATION ),
					RARE_MAX_USES,
					APPRENTICE_BUY_XP,
					HIGH_PRICE_MULTIPLIER
			));
		} );
		
		// # Level 4
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 4, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 12 ),
					new ItemStack( Items.COOKED_BEEF, 6 ),
					COMMON_MAX_USES,
					JOURNEYMAN_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 12 ),
					new ItemStack( Items.COOKED_MUTTON, 6 ),
					COMMON_MAX_USES,
					JOURNEYMAN_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 12 ),
					new ItemStack( Items.COOKED_PORKCHOP, 6 ),
					COMMON_MAX_USES,
					JOURNEYMAN_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 12 ),
					createPotionStack( Text.translatable("item.minecraft.potion.effect.healing"), Potions.STRONG_HEALING ),
					RARE_MAX_USES,
					JOURNEYMAN_BUY_XP,
					HIGH_PRICE_MULTIPLIER
			));
			
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 12 ),
					createPotionStack( Text.translatable("item.minecraft.potion.effect.regeneration"), Potions.STRONG_REGENERATION ),
					RARE_MAX_USES,
					JOURNEYMAN_BUY_XP,
					HIGH_PRICE_MULTIPLIER
			));
		} );
		
		// # Level 5
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 5, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 16 ),
					new ItemStack( Items.GOLDEN_CARROT, 6 ),
					RARE_MAX_USES,
					EXPERT_BUY_XP,
					LOW_PRICE_MULTIPLIER
			));
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 16 ),
					createPotionStack( Text.translatable("item.minecraft.potion.effect.regeneration"), Potions.LONG_REGENERATION ),
					RARE_MAX_USES,
					EXPERT_BUY_XP,
					HIGH_PRICE_MULTIPLIER
			));
			
			if( Innsandinnkeepers.CONFIG.enableGoldenAppleTrade ) {
				f.add((entity, random) -> new TradeOffer(
						new TradedItem(Items.EMERALD, 32),
						new ItemStack(Items.GOLDEN_APPLE, 1),
						RARE_MAX_USES,
						MASTER_TRADE_XP,
						HIGH_PRICE_MULTIPLIER
				));
			} // if
			
			if( Innsandinnkeepers.CONFIG.enableEnchantedGoldenAppleTrade ) {
				f.add((entity, random) -> new TradeOffer(
						new TradedItem(Items.EMERALD, 64),
						new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1),
						RARE_MAX_USES,
						MASTER_TRADE_XP,
						HIGH_PRICE_MULTIPLIER
				));
			} // if
		} );
	}
	
	private static ItemStack createPotionStack( Text name, RegistryEntry<Potion> potionEffect ) {
		ItemStack potion = new ItemStack( Items.POTION );
		
		potion.set(DataComponentTypes.ITEM_NAME, name );
		potion.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potionEffect));
		
		return potion;
	}
	
}
