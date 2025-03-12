package me.villagerunknown.innsandinnkeepers.screen;

import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class FireplaceScreenHandler extends AbstractRecipeScreenHandler<SingleStackRecipeInput, AbstractCookingRecipe> {
	
	private final Inventory inventory;
	private final PropertyDelegate propertyDelegate;
	protected final World world;
	private final RecipeType<? extends AbstractCookingRecipe> recipeType = RecipeType.SMOKING;
	private final RecipeBookCategory category = RecipeBookCategory.SMOKER;
	
	public FireplaceScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(2), new ArrayPropertyDelegate(4));
	}
	
	public FireplaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
		super(fireplaceBlockFeature.FIREPLACE_SCREEN_HANDLER, syncId);
		checkSize(inventory, 2);
		checkDataCount(propertyDelegate, 4);
		this.inventory = inventory;
		this.propertyDelegate = propertyDelegate;
		this.world = playerInventory.player.getWorld();
		this.addSlot(new Slot(inventory, 0, 56, 17));
		this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 1, 116, 35));
		
		int i;
		for(i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		for(i = 0; i < 9; ++i) {
			this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
		
		this.addProperties(propertyDelegate);
	}
	
	public void populateRecipeFinder(RecipeMatcher finder) {
		if (this.inventory instanceof RecipeInputProvider) {
			((RecipeInputProvider)this.inventory).provideRecipeInputs(finder);
		}
		
	}
	
	public void clearCraftingSlots() {
		this.getSlot(0).setStackNoCallbacks(ItemStack.EMPTY);
		this.getSlot(1).setStackNoCallbacks(ItemStack.EMPTY);
	}
	
	public boolean matches(RecipeEntry<AbstractCookingRecipe> recipe) {
		return ((AbstractCookingRecipe)recipe.value()).matches(new SingleStackRecipeInput(this.inventory.getStack(0)), this.world);
	}
	
	public int getCraftingResultSlotIndex() {
		return 1;
	}
	
	public int getCraftingWidth() {
		return 1;
	}
	
	public int getCraftingHeight() {
		return 1;
	}
	
	public int getCraftingSlotCount() {
		return 2;
	}
	
	public boolean canUse(PlayerEntity player) {
		return this.inventory.canPlayerUse(player);
	}
	
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot2 = (Slot)this.slots.get(slot);
		if (slot2 != null && slot2.hasStack()) {
			ItemStack itemStack2 = slot2.getStack();
			itemStack = itemStack2.copy();
			if (slot == 1) {
				if (!this.insertItem(itemStack2, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
				
				slot2.onQuickTransfer(itemStack2, itemStack);
			} else if (slot != 0) {
				if (this.isSmeltable(itemStack2)) {
					if (!this.insertItem(itemStack2, 0, 1, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slot >= 2 && slot < 29) {
					if (!this.insertItem(itemStack2, 29, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (slot >= 29 && slot < 38 && !this.insertItem(itemStack2, 2, 29, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.insertItem(itemStack2, 2, 38, false)) {
				return ItemStack.EMPTY;
			}
			
			if (itemStack2.isEmpty()) {
				slot2.setStack(ItemStack.EMPTY);
			} else {
				slot2.markDirty();
			}
			
			if (itemStack2.getCount() == itemStack.getCount()) {
				return ItemStack.EMPTY;
			}
			
			slot2.onTakeItem(player, itemStack2);
		}
		
		return itemStack;
	}
	
	protected boolean isSmeltable(ItemStack itemStack) {
		return this.world.getRecipeManager().getFirstMatch(this.recipeType, new SingleStackRecipeInput(itemStack), this.world).isPresent();
	}
	
	protected boolean isFuel(ItemStack itemStack) {
		return true;
	}
	
	public float getCookProgress() {
		int i = this.propertyDelegate.get(2);
		int j = this.propertyDelegate.get(3);
		return j != 0 && i != 0 ? MathHelper.clamp((float)i / (float)j, 0.0F, 1.0F) : 0.0F;
	}
	
	public float getFuelProgress() {
		return 1;
	}
	
	public boolean isBurning() {
		return true;
	}
	
	public RecipeBookCategory getCategory() {
		return this.category;
	}
	
	public boolean canInsertIntoSlot(int index) {
		return index != 1;
	}
}
