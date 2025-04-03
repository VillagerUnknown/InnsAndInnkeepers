package me.villagerunknown.innsandinnkeepers.client;

import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.platform.util.StringUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static me.villagerunknown.innsandinnkeepers.feature.hearthstoneItemFeature.HEARTHSTONE_ITEM;

public class InnsandinnkeepersClient implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		registerHearthstoneItemTooltips();
		
//		HandledScreens.register(fireplaceBlockFeature.FIREPLACE_SCREEN_HANDLER, FireplaceScreen::new);
	}
	
	private static void registerHearthstoneItemTooltips() {
		ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, list) -> {
			if (!stack.isOf( HEARTHSTONE_ITEM )) {
				return;
			}
			
			list.add( Text.translatable( "item.villagerunknown-innsandinnkeepers.hearthstone.tooltip.bind" ) );
			list.add( Text.translatable( "item.villagerunknown-innsandinnkeepers.hearthstone.tooltip.teleport" ) );
			
			LodestoneTrackerComponent lodestoneTrackerComponent = (LodestoneTrackerComponent)stack.get(DataComponentTypes.LODESTONE_TRACKER);
			if (lodestoneTrackerComponent != null) {
				if( lodestoneTrackerComponent.target().isPresent() ) {
					BlockPos pos = lodestoneTrackerComponent.target().get().pos();
					String dimensionName = StringUtil.capitalize( lodestoneTrackerComponent.target().get().dimension().getValue().getPath().toLowerCase() );
					
					String boundTo = Text.translatable("item.villagerunknown-innsandinnkeepers.hearthstone.tooltip.boundto").getString();
					list.addLast( Text.of( "(" + boundTo + ": " + dimensionName + " @ "  + pos.getX() + " " + pos.getY() + " " + pos.getZ() + ")" ) );
				} // if
			} else {
				String notBound = Text.translatable("item.villagerunknown-innsandinnkeepers.hearthstone.tooltip.notbound").getString();
				list.addLast( Text.of( "(" + notBound + ")" ) );
			} // if, else
		});
	}
	
}
