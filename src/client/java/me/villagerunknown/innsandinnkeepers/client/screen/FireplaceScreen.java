package me.villagerunknown.innsandinnkeepers.client.screen;

import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenPos;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.display.RecipeDisplay;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class FireplaceScreen extends HandledScreen<FireplaceScreenHandler> {

	private static final Identifier LIT_PROGRESS_TEXTURE = Identifier.ofVanilla("container/smoker/lit_progress");
	private static final Identifier BURN_PROGRESS_TEXTURE = Identifier.ofVanilla("container/smoker/burn_progress");
	private static final Identifier TEXTURE = Identifier.of( MOD_ID,"textures/gui/container/fireplace.png");
	
	private final Identifier background = TEXTURE;
	private final Identifier litProgressTexture = LIT_PROGRESS_TEXTURE;
	private final Identifier burnProgressTexture = BURN_PROGRESS_TEXTURE;

	public FireplaceScreen(FireplaceScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	public void init() {
		super.init();
		this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
	}

	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
		int i = this.x;
		int j = this.y;
		context.drawTexture(RenderLayer::getGuiTextured, this.background, i, j, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
		boolean k;
		int l;
		if ((this.handler).isBurning()) {
			k = true;
			l = MathHelper.ceil((this.handler).getFuelProgress() * 13.0F) + 1;
			context.drawGuiTexture(RenderLayer::getGuiTextured, this.litProgressTexture, 14, 14, 0, 14 - l, i + 56, j + 36 + 24 - l, 14, l);
		}

		k = true;
		l = MathHelper.ceil((this.handler).getCookProgress() * 24.0F);
		context.drawGuiTexture(RenderLayer::getGuiTextured, this.burnProgressTexture, 24, 16, 0, 0, i + 79, j + 34, l, 16);
	}

}
