package me.villagerunknown.innsandinnkeepers.entity.block;

import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmokerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FireplaceBlockEntity extends AbstractFurnaceBlockEntity {
	
	public FireplaceBlockEntity(BlockPos pos, BlockState state) {
		super(fireplaceBlockFeature.FIREPLACE_BLOCK_ENTITY, pos, state, RecipeType.SMOKING);
	}
	
	protected Text getContainerName() {
		return Text.translatable("container.fireplace");
	}
	
	protected int getFuelTime(ItemStack fuel) {
		return super.getFuelTime(fuel) / 2;
	}
	
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new SmokerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
	
}
