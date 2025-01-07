package me.villagerunknown.innsandinnkeepers.feature;

import com.google.common.collect.ImmutableSet;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
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
//		registerVillagerTrades();
	}
	
	private static void registerPointOfInterest() {
		Set<BlockState> blockStates = ImmutableSet.copyOf(fireplaceBlockFeature.FIREPLACE_BLOCK.getStateManager().getStates());
		
		INNKEEPER_POI_TYPE = PointOfInterestHelper.register( INNKEEPER_IDENTIFIER, 1, 1, blockStates );
//		INNKEEPER_POI_TYPE = new PointOfInterestType( blockStates, 1, 1 );
		
		Innsandinnkeepers.LOGGER.info( "POI Type: " + INNKEEPER_POI_TYPE );
		
		Registry.register( Registries.POINT_OF_INTEREST_TYPE, INNKEEPER_IDENTIFIER, INNKEEPER_POI_TYPE );
		
		INNKEEPER_POI_TYPE_REGISTRY = Registries.POINT_OF_INTEREST_TYPE.getEntry( INNKEEPER_POI_TYPE );
		INNKEEPER_POI_TYPE_REGISTRY_KEY =  RegistryKey.of( RegistryKeys.POINT_OF_INTEREST_TYPE, INNKEEPER_IDENTIFIER );
		
		Innsandinnkeepers.LOGGER.info( "POI Registry: " + INNKEEPER_POI_TYPE_REGISTRY );
		Innsandinnkeepers.LOGGER.info( "POI Key: " + INNKEEPER_POI_TYPE_REGISTRY_KEY );
	}
	
	private static void registerVillagerProfession() {
		Predicate<RegistryEntry<PointOfInterestType>> predicate = (entry) -> entry.matchesKey( INNKEEPER_POI_TYPE_REGISTRY_KEY );
		
		INNKEEPER_PROFESSION = new VillagerProfession( INNKEEPER_STRING, predicate, predicate, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_SHEPHERD );
		
		Innsandinnkeepers.LOGGER.info( "Profession: " + INNKEEPER_PROFESSION );
		
		Registry.register( Registries.VILLAGER_PROFESSION, INNKEEPER_IDENTIFIER, INNKEEPER_PROFESSION );
		
		INNKEEPER_PROFESSION_REGISTRY = Registries.VILLAGER_PROFESSION.getEntry( INNKEEPER_PROFESSION );
		
		Innsandinnkeepers.LOGGER.info( "Profession Registration: " + INNKEEPER_PROFESSION_REGISTRY );
	}
	
	private static void registerVillagerTrades() {
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 1, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD ),
					null,
					new ItemStack( Items.STONE_BUTTON ),
					6,
					2,
					0.20f
			));
		} );
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 2, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 16 ),
					null,
					new ItemStack( Items.BREAD ),
					6,
					2,
					0.20f
			));
		} );
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 3, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 32 ),
					null,
					new ItemStack( Items.COOKED_BEEF ),
					6,
					2,
					0.20f
			));
		} );
		TradeOfferHelper.registerVillagerOffers( INNKEEPER_PROFESSION, 4, f -> {
			f.add( (entity, random) -> new TradeOffer(
					new TradedItem( Items.EMERALD, 64 ),
					null,
					new ItemStack( Items.GOLDEN_CARROT ),
					6,
					2,
					0.20f
			));
		} );
	}
	
}
