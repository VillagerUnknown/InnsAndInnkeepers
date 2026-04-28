package me.villagerunknown.innsandinnkeepers.entity.block;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.*;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.block.FireplaceBlock;
import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import me.villagerunknown.platform.util.MathUtil;
import me.villagerunknown.platform.util.WorldUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FireplaceBlockEntity extends AbstractFurnaceBlockEntity {
	
	int burnTime;
	int fuelTime;
	int cookTime;
	int cookTimeTotal;
	
	int litTimeRemaining;
	int litTotalTime;
	int cookingTimeSpent;
	int cookingTotalTime;
	
	protected DefaultedList<ItemStack> inventory;
	protected final PropertyDelegate propertyDelegate;
	private final Reference2IntOpenHashMap<RegistryKey<Recipe<?>>> recipesUsed;
	private final ServerRecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> matchGetter;
	
	private static final Codec<Map<RegistryKey<Recipe<?>>, Integer>> CODEC;
	
	public FireplaceBlockEntity(BlockPos pos, BlockState state) {
		super(fireplaceBlockFeature.FIREPLACE_BLOCK_ENTITY, pos, state, RecipeType.SMOKING);
		
		this.inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
		this.propertyDelegate = new PropertyDelegate() {
			public int get(int index) {
				switch (index) {
					case 0 -> {
						return FireplaceBlockEntity.this.burnTime;
					}
					case 1 -> {
						return FireplaceBlockEntity.this.fuelTime;
					}
					case 2 -> {
						return FireplaceBlockEntity.this.cookTime;
					}
					case 3 -> {
						return FireplaceBlockEntity.this.cookTimeTotal;
					}
					default -> {
						return 0;
					}
				}
			}
			
			public void set(int index, int value) {
				switch (index) {
					case 0 -> FireplaceBlockEntity.this.burnTime = value;
					case 1 -> FireplaceBlockEntity.this.fuelTime = value;
					case 2 -> FireplaceBlockEntity.this.cookTime = value;
					case 3 -> FireplaceBlockEntity.this.cookTimeTotal = value;
				}
				
			}
			
			public int size() {
				return 4;
			}
		};
		this.recipesUsed = new Reference2IntOpenHashMap();
		this.matchGetter = ServerRecipeManager.createCachedMatchGetter(RecipeType.SMOKING);
	}
	
	protected Text getContainerName() {
		return Text.translatable("container.fireplace");
	}
	
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new FireplaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
	
	protected DefaultedList<ItemStack> getHeldStacks() {
		return this.inventory;
	}
	
	protected void setHeldStacks(DefaultedList<ItemStack> inventory) {
		this.inventory = inventory;
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, FireplaceBlockEntity blockEntity) {
		boolean bl = state.get(FireplaceBlock.LIT);
		boolean bl2 = false;
		if (bl) {
			ItemStack itemStack = new ItemStack( fireplaceBlockFeature.DEFAULT_FUEL );
			ItemStack itemStack2 = (ItemStack)blockEntity.inventory.get(0);
			boolean bl3 = !itemStack2.isEmpty();
			boolean bl4 = !itemStack.isEmpty();
			
			if( 0 == blockEntity.burnTime ) {
				blockEntity.burnTime = blockEntity.getFuelTime( world.getFuelRegistry(), itemStack );
				blockEntity.fuelTime = blockEntity.burnTime;
			} // if
			
			--blockEntity.burnTime;
			
			if (bl4 && bl3) {
				SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(itemStack2);
				RecipeEntry recipeEntry = (RecipeEntry)blockEntity.matchGetter.getFirstMatch(singleStackRecipeInput, (ServerWorld) world).orElse(null);
				
				int i = blockEntity.getMaxCountPerStack();
				
				if (bl && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
					++blockEntity.cookTime;
					if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
						blockEntity.cookTime = 0;
						blockEntity.cookTimeTotal = getCookTime(world, blockEntity);
						if (craftRecipe(world.getRegistryManager(), recipeEntry, singleStackRecipeInput, blockEntity.inventory, i)) {
							blockEntity.setLastRecipe(recipeEntry);
						}
						
						bl2 = true;
					}
				} else {
					blockEntity.cookTime = 0;
				}
			}
			
			if (bl2) {
				markDirty(world, pos, state);
			}
			
			emitSmoke( world, pos, MathUtil.hasChance( 0.33F ) );
		} else {
			if( blockEntity.cookTime > 0) {
				blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
			}
			
			blockEntity.fuelTime = 0;
			blockEntity.burnTime = 0;
		}
	}
	
	private static void emitSmoke( World world, BlockPos pos, boolean chance ) {
		if( chance ) {
			double d = (double)pos.getX() + 0.5;
			double e = (double)pos.up().getY();
			double f = (double)pos.getZ() + 0.5;
			
			if( !world.isAir( pos.up() ) ) {
				for (int i = 2; i < FireplaceBlock.MAX_BLOCKS_SMOKE_PASSES_THROUGH + 2; i++) {
					if( world.isAir( pos.up( i ) ) ) {
						e = pos.up( i ).getY();
						break;
					} // if
				} // for
			} // if
			
			SimpleParticleType particleType = ParticleTypes.CAMPFIRE_COSY_SMOKE;
			
			if( MathUtil.hasChance( Innsandinnkeepers.CONFIG.chanceForSmokeVariation ) ) {
				String dimensionId = world.getDimensionEntry().getIdAsString();
				
				switch( dimensionId ) {
					case "minecraft:overworld":
						particleType = fireplaceBlockFeature.EXTRA_OVERWORLD_SMOKE_PARTICLES.get((int) MathUtil.getRandomWithinRange(0, fireplaceBlockFeature.EXTRA_OVERWORLD_SMOKE_PARTICLES.size()));
						break;
					case "minecraft:the_nether":
						particleType = fireplaceBlockFeature.EXTRA_NETHER_SMOKE_PARTICLES.get((int) MathUtil.getRandomWithinRange(0, fireplaceBlockFeature.EXTRA_NETHER_SMOKE_PARTICLES.size()));
						break;
					case "minecraft:the_end":
						particleType = fireplaceBlockFeature.EXTRA_END_SMOKE_PARTICLES.get((int) MathUtil.getRandomWithinRange(0, fireplaceBlockFeature.EXTRA_END_SMOKE_PARTICLES.size()));
						break;
				} // switch
			} // if
			
			Random random = Random.create();
			
			world.addParticleClient( particleType, (double) d + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), e + 0.1 + random.nextDouble(), f + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
			
			if( e > (double) FireplaceBlock.MAX_BLOCKS_SMOKE_PASSES_THROUGH / 2 ) {
				world.addParticleClient( particleType, (double) d + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), e + 0.1 + random.nextDouble(), f + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
			} // if
		} // if
	}
	
	private boolean isBurning() {
		return this.burnTime > 0;
	}
	
	private boolean isCooking() {
		return this.cookTime < this.cookTimeTotal;
	}
	
	public int size() {
		return this.inventory.size();
	}
	
	private static boolean canAcceptRecipeOutput(DynamicRegistryManager dynamicRegistryManager, @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe, SingleStackRecipeInput input, DefaultedList<ItemStack> inventory, int maxCount) {
		if (!((ItemStack)inventory.get(0)).isEmpty() && recipe != null) {
			ItemStack itemStack = ((AbstractCookingRecipe)recipe.value()).craft(input, dynamicRegistryManager);
			if (itemStack.isEmpty()) {
				return false;
			} else {
				ItemStack itemStack2 = (ItemStack)inventory.get(1);
				if (itemStack2.isEmpty()) {
					return true;
				} else if (!ItemStack.areItemsAndComponentsEqual(itemStack2, itemStack)) {
					return false;
				} else if (itemStack2.getCount() < maxCount && itemStack2.getCount() < itemStack2.getMaxCount()) {
					return true;
				} else {
					return itemStack2.getCount() < itemStack.getMaxCount();
				}
			}
		} else {
			return false;
		}
	}
	
	private static boolean craftRecipe(DynamicRegistryManager dynamicRegisryManager, @Nullable RecipeEntry<? extends AbstractCookingRecipe> recipe, SingleStackRecipeInput input, DefaultedList<ItemStack> inventory, int maxCount) {
		if (recipe != null && canAcceptRecipeOutput(dynamicRegisryManager, recipe, input, inventory, maxCount)) {
			ItemStack itemStack = (ItemStack)inventory.get(0);
			ItemStack itemStack2 = ((AbstractCookingRecipe)recipe.value()).craft(input, dynamicRegisryManager);
			ItemStack itemStack3 = (ItemStack)inventory.get(1);
			if (itemStack3.isEmpty()) {
				inventory.set(1, itemStack2.copy());
			} else if (ItemStack.areItemsAndComponentsEqual(itemStack3, itemStack2)) {
				itemStack3.increment(1);
			}
			
			itemStack.decrement(1);
			return true;
		} else {
			return false;
		}
	}
	
	private static int getCookTime(World world, FireplaceBlockEntity furnace) {
		SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(furnace.getStack(0));
		return (Integer)furnace.matchGetter.getFirstMatch(singleStackRecipeInput, (ServerWorld) world).map((recipe) -> recipe.value().getCookingTime()).orElse(200) * 2;
	}
	
	protected void readData(ReadView view) {
		super.readData(view);
		
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		
		Inventories.readData(view, this.inventory);
		this.cookingTimeSpent = view.getShort("cooking_time_spent", (short)0);
		this.cookingTotalTime = view.getShort("cooking_total_time", (short)0);
		this.litTimeRemaining = view.getShort("lit_time_remaining", (short)0);
		this.litTotalTime = view.getShort("lit_total_time", (short)0);
		this.recipesUsed.clear();
		this.recipesUsed.putAll((Map)view.read("RecipesUsed", CODEC).orElse(Map.of()));
	}
	
	protected void writeData(WriteView view) {
		super.writeData(view);
		view.putShort("BurnTime", (short)this.burnTime);
		view.putShort("CookTime", (short)this.cookTime);
		view.putShort("CookTimeTotal", (short)this.cookTimeTotal);
		Inventories.writeData(view, this.inventory);
		NbtCompound nbtCompound = new NbtCompound();
		this.recipesUsed.forEach((identifier, count) -> {
			nbtCompound.putInt(identifier.toString(), count);
		});
		view.put("RecipesUsed", CODEC, this.recipesUsed);
	}
	
	public int[] getAvailableSlots(Direction side) {
		if( !Innsandinnkeepers.CONFIG.enableFireplaceCooking ) {
			return new int[]{};
		} else if(side == Direction.DOWN) {
			return new int[]{1};
		} else {
			return new int[]{0};
		} // if, else
	}
	
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return this.isValid(slot, stack);
	}
	
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return (1 == slot);
	}
	
	public void setStack(int slot, ItemStack stack) {
		ItemStack itemStack = (ItemStack)this.inventory.get(slot);
		boolean bl = !stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack);
		this.inventory.set(slot, stack);
		stack.capCount(this.getMaxCount(stack));
		if (slot == 0 && !bl) {
			this.cookTimeTotal = getCookTime(this.world, this);
			this.cookTime = 0;
		}
	}
	
	public boolean isValid(int slot, ItemStack stack) {
		return (Innsandinnkeepers.CONFIG.enableFireplaceCooking && 1 != slot);
	}
	
	public void setLastRecipe(@Nullable RecipeEntry<?> recipe) {
		super.setLastRecipe(recipe);
	}
	
	public void provideRecipeInputs(RecipeFinder finder) {
		super.provideRecipeInputs(finder);
	}
	
	static{
		CODEC = Codec.unboundedMap(Recipe.KEY_CODEC, Codec.INT);
	}
	
}