package me.villagerunknown.innsandinnkeepers.client.screen.recipebook;

import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.GhostRecipe;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.display.FurnaceRecipeDisplay;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.context.ContextParameterMap;

import java.util.List;

public class FireplaceRecipeBookWidget extends RecipeBookWidget<FireplaceScreenHandler> {
	
	private static final ButtonTextures TEXTURES = new ButtonTextures(Identifier.ofVanilla("recipe_book/furnace_filter_enabled"), Identifier.ofVanilla("recipe_book/furnace_filter_disabled"), Identifier.ofVanilla("recipe_book/furnace_filter_enabled_highlighted"), Identifier.ofVanilla("recipe_book/furnace_filter_disabled_highlighted"));
	private final Text toggleCraftableButtonText;
	
	public FireplaceRecipeBookWidget(FireplaceScreenHandler screenHandler, Text toggleCraftableButtonText, List<Tab> tabs) {
		super(screenHandler, tabs);
		this.toggleCraftableButtonText = toggleCraftableButtonText;
	}
	
	@Override
	protected ButtonTextures getBookButtonTextures() {
		return TEXTURES;
	}
	
	@Override
	protected boolean isCraftingSlot(Slot slot) {
		boolean isValid;
		switch (slot.id) {
			case 0:
			case 1:
				isValid = true;
				break;
			default:
				isValid = false;
		}
		
		return isValid;
	}
	
	@Override
	protected void populateRecipes(RecipeResultCollection recipeResultCollection, RecipeFinder recipeFinder) {
		recipeResultCollection.populateRecipes(recipeFinder, (display) -> display instanceof FurnaceRecipeDisplay);
	}
	
	@Override
	protected Text getToggleCraftableButtonText() {
		return this.toggleCraftableButtonText;
	}
	
	@Override
	protected void showGhostRecipe(GhostRecipe ghostRecipe, RecipeDisplay display, ContextParameterMap context) {
	
	}
}
