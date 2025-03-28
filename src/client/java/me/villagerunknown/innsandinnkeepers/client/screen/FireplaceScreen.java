package me.villagerunknown.innsandinnkeepers.client.screen;

import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

//public class FireplaceScreen extends HandledScreen<FireplaceScreenHandler> implements RecipeBookProvider {
//
//	private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.ofVanilla("container/smoker/lit_progress");
//	private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.ofVanilla("container/smoker/burn_progress");
//	private static final Identifier TEXTURE = Identifier.of( MOD_ID,"textures/gui/container/fireplace.png");
//
//	public final AbstractFurnaceRecipeBookScreen recipeBook = new SmokerRecipeBookScreen();
//	private boolean narrow;
//	private final Identifier background = TEXTURE;
//	private final Identifier litProgressTexture = LIT_PROGRESS_TEXTURE;
//	private final Identifier burnProgressTexture = BURN_PROGRESS_TEXTURE;
//
//	public FireplaceScreen(FireplaceScreenHandler handler, PlayerInventory inventory, Text title) {
//		super(handler, inventory, title);
//	}
//
//	public void init() {
//		super.init();
//		this.narrow = this.width < 379;
//		this.recipeBook.initialize(this.width, this.height, this.client, this.narrow, (FireplaceScreenHandler)this.handler);
//		this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
//		this.addDrawableChild(new TexturedButtonWidget(this.x + 20, this.height / 2 - 49, 20, 18, RecipeBookWidget.BUTTON_TEXTURES, (button) -> {
//			this.recipeBook.toggleOpen();
//			this.x = this.recipeBook.findLeftEdge(this.width, this.backgroundWidth);
//			button.setPosition(this.x + 20, this.height / 2 - 49);
//		}));
//		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
//	}
//
//	public void handledScreenTick() {
//		super.handledScreenTick();
//		this.recipeBook.update();
//	}
//
//	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//		if (this.recipeBook.isOpen() && this.narrow) {
//			this.renderBackground(context, mouseX, mouseY, delta);
//			this.recipeBook.render(context, mouseX, mouseY, delta);
//		} else {
//			super.render(context, mouseX, mouseY, delta);
//			this.recipeBook.render(context, mouseX, mouseY, delta);
//			this.recipeBook.drawGhostSlots(context, this.x, this.y, true, delta);
//		}
//
//		this.drawMouseoverTooltip(context, mouseX, mouseY);
//		this.recipeBook.drawTooltip(context, this.x, this.y, mouseX, mouseY);
//	}
//
//	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
//		int i = this.x;
//		int j = this.y;
//		context.drawTexture(this.background, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
//		boolean k;
//		int l;
//		if ((this.handler).isBurning()) {
//			k = true;
//			l = MathHelper.ceil((this.handler).getFuelProgress() * 13.0F) + 1;
//			context.drawGuiTexture(this.litProgressTexture, 14, 14, 0, 14 - l, i + 56, j + 54 + 14 - l, 14, l);
//		}
//
//		k = true;
//		l = MathHelper.ceil((this.handler).getCookProgress() * 24.0F);
//		context.drawGuiTexture(this.burnProgressTexture, 24, 16, 0, 0, i + 79, j + 54, l, 16);
//	}
//
//	public boolean mouseClicked(double mouseX, double mouseY, int button) {
//		if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
//			return true;
//		} else {
//			return this.narrow && this.recipeBook.isOpen() ? true : super.mouseClicked(mouseX, mouseY, button);
//		}
//	}
//
//	protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
//		super.onMouseClick(slot, slotId, button, actionType);
//		this.recipeBook.slotClicked(slot);
//	}
//
//	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//		return this.recipeBook.keyPressed(keyCode, scanCode, modifiers) ? true : super.keyPressed(keyCode, scanCode, modifiers);
//	}
//
//	protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
//		boolean bl = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
//		return this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y, this.backgroundWidth, this.backgroundHeight, button) && bl;
//	}
//
//	public boolean charTyped(char chr, int modifiers) {
//		return this.recipeBook.charTyped(chr, modifiers) ? true : super.charTyped(chr, modifiers);
//	}
//
//	public void refreshRecipeBook() {
//		this.recipeBook.refresh();
//	}
//
//	@Override
//	public void onCraftFailed(RecipeDisplay display) {
//
//	}
//
//	public RecipeBookWidget getRecipeBookWidget() {
//		return this.recipeBook;
//	}
//
//}
