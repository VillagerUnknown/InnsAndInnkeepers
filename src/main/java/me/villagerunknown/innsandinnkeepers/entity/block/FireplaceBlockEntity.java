package me.villagerunknown.innsandinnkeepers.entity.block;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.block.FireplaceBlock;
import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.innsandinnkeepers.screen.FireplaceScreenHandler;
import me.villagerunknown.platform.util.MathUtil;
import me.villagerunknown.platform.util.TimeUtil;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmokerScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class FireplaceBlockEntity extends AbstractFurnaceBlockEntity {
	
	int burnTime;
	int fuelTime;
	int cookTime;
	int cookTimeTotal;
	
	protected DefaultedList<ItemStack> inventory;
	protected final PropertyDelegate propertyDelegate;
	private final Object2IntOpenHashMap<Identifier> recipesUsed;
	private final RecipeManager.MatchGetter<SingleStackRecipeInput, ? extends AbstractCookingRecipe> matchGetter;
	
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
		this.recipesUsed = new Object2IntOpenHashMap<>();
		this.matchGetter = RecipeManager.createCachedMatchGetter(RecipeType.SMOKING);
	}
	
	protected Text getContainerName() {
		return Text.translatable("container.fireplace");
	}
	
	protected int getFuelTime(ItemStack fuel) {
		return 20000;
	}
	
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new FireplaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, FireplaceBlockEntity blockEntity) {
		boolean bl = blockEntity.isBurning();
		boolean bl2 = false;

		ItemStack itemStack = (ItemStack)blockEntity.inventory.get(1);
		ItemStack itemStack2 = (ItemStack)blockEntity.inventory.get(0);
		boolean bl3 = !itemStack2.isEmpty();
		boolean bl4 = !itemStack.isEmpty();
		if (blockEntity.isBurning()) {
			RecipeEntry<?> recipeEntry = blockEntity.matchGetter.getFirstMatch(new SingleStackRecipeInput(itemStack2), world).orElse(null);
//			Innsandinnkeepers.LOGGER.info( "Recipe: " + recipeEntry );
			int i = blockEntity.getMaxCountPerStack();

			if (blockEntity.isCooking() && canAcceptRecipeOutput(world.getRegistryManager(), recipeEntry, blockEntity.inventory, i)) {
				++blockEntity.cookTime;
				if (blockEntity.cookTime == blockEntity.cookTimeTotal) {
					blockEntity.cookTime = 0;
					blockEntity.cookTimeTotal = getCookTime(world, blockEntity);
					if (craftRecipe(world.getRegistryManager(), recipeEntry, blockEntity.inventory, i)) {
						blockEntity.setLastRecipe(recipeEntry);
					}

					bl2 = true;
				}
			} else {
				blockEntity.cookTime = 0;
			}
		} else if (!blockEntity.isCooking() && blockEntity.cookTime > 0) {
			blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
		}

		if( blockEntity.isBurning() ) {
			bl2 = true;
//			state = (BlockState)state.with(AbstractFurnaceBlock.LIT, true);
			world.setBlockState(pos, state, 3);
		}
		
		if( bl2 ) {
			markDirty(world, pos, state);
		}
		
		if( (Boolean)state.get(FireplaceBlock.LIT) && MathUtil.hasChance( 0.33F ) ) {
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
			
			if( MathUtil.hasChance( 0.01F ) ) {
				particleType = ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;
			}
			
			Random random = Random.create();
			
			world.addParticle( particleType, (double) d + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), e + 0.1 + random.nextDouble(), f + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
			
			if( e > (double) FireplaceBlock.MAX_BLOCKS_SMOKE_PASSES_THROUGH / 2 ) {
				world.addParticle( particleType, (double) d + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), e + 0.1 + random.nextDouble(), f + random.nextDouble() / 3.0 * (double)(random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
			} // if
		} // if
		
	}
	
	private boolean isBurning() {
		return true;
	}
	
	private boolean isCooking() {
		return this.cookTime < this.cookTimeTotal;
	}
	
	public int size() {
		return this.inventory.size();
	}
	
	private static boolean canAcceptRecipeOutput(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count) {
		if (!((ItemStack)slots.get(0)).isEmpty() && recipe != null) {
			ItemStack itemStack = recipe.value().getResult(registryManager);
			if (itemStack.isEmpty()) {
				return false;
			} else {
				ItemStack itemStack2 = (ItemStack)slots.get(1);
				if (itemStack2.isEmpty()) {
					return true;
				} else if (!ItemStack.areItemsAndComponentsEqual(itemStack2, itemStack)) {
					return false;
				} else if (itemStack2.getCount() < count && itemStack2.getCount() < itemStack2.getMaxCount()) {
					return true;
				} else {
					return itemStack2.getCount() < itemStack.getMaxCount();
				}
			}
		} else {
			return false;
		}
	}
	
	private static boolean craftRecipe(DynamicRegistryManager registryManager, @Nullable RecipeEntry<?> recipe, DefaultedList<ItemStack> slots, int count) {
		if (recipe != null && canAcceptRecipeOutput(registryManager, recipe, slots, count)) {
			ItemStack itemStack = (ItemStack)slots.get(0);
			ItemStack itemStack2 = recipe.value().getResult(registryManager);
			ItemStack itemStack3 = (ItemStack)slots.get(1);
			if (itemStack3.isEmpty()) {
				slots.set(1, itemStack2.copy());
			} else if (ItemStack.areItemsAndComponentsEqual(itemStack3, itemStack2)) {
				itemStack3.increment(1);
			}
			
			if (itemStack.isOf(Blocks.WET_SPONGE.asItem()) && !((ItemStack)slots.get(0)).isEmpty() && ((ItemStack)slots.get(0)).isOf(Items.BUCKET)) {
				slots.set(0, new ItemStack(Items.WATER_BUCKET));
			}
			
			itemStack.decrement(1);
			return true;
		} else {
			return false;
		}
	}
	
	private static int getCookTime(World world, FireplaceBlockEntity furnace) {
		SingleStackRecipeInput singleStackRecipeInput = new SingleStackRecipeInput(furnace.getStack(0));
		return (Integer)furnace.matchGetter.getFirstMatch(singleStackRecipeInput, world).map((recipe) -> recipe.value().getCookingTime()).orElse(200);
	}
	
	protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, this.inventory, registryLookup);
		this.burnTime = nbt.getShort("BurnTime");
		this.cookTime = nbt.getShort("CookTime");
		this.cookTimeTotal = nbt.getShort("CookTimeTotal");
		this.fuelTime = this.getFuelTime((ItemStack)this.inventory.get(1));
		NbtCompound nbtCompound = nbt.getCompound("RecipesUsed");
		Iterator var4 = nbtCompound.getKeys().iterator();
		
		while(var4.hasNext()) {
			String string = (String)var4.next();
			this.recipesUsed.put(Identifier.of(string), nbtCompound.getInt(string));
		}
		
	}
	
	protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		nbt.putShort("BurnTime", (short)this.burnTime);
		nbt.putShort("CookTime", (short)this.cookTime);
		nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
		Inventories.writeNbt(nbt, this.inventory, registryLookup);
		NbtCompound nbtCompound = new NbtCompound();
		this.recipesUsed.forEach((identifier, count) -> {
			nbtCompound.putInt(identifier.toString(), count);
		});
		nbt.put("RecipesUsed", nbtCompound);
	}
	
	public void setStack(int slot, ItemStack stack) {
		ItemStack itemStack = (ItemStack)this.inventory.get(slot);
		boolean bl = !stack.isEmpty() && ItemStack.areItemsAndComponentsEqual(itemStack, stack);
		this.inventory.set(slot, stack);
		stack.capCount(this.getMaxCount(stack));
		if (slot == 0 && !bl) {
			this.cookTimeTotal = getCookTime(this.world, this);
			this.cookTime = 0;
			this.markDirty();
		}
		
	}
	
	public boolean isValid(int slot, ItemStack stack) {
		if (slot == 1) {
			return false;
		}
		
		return true;
	}
	
	public void setLastRecipe(@Nullable RecipeEntry<?> recipe) {
		if (recipe != null) {
			Identifier identifier = recipe.id();
			this.recipesUsed.addTo(identifier, 1);
		}
		
	}
	
	public List<RecipeEntry<?>> getRecipesUsedAndDropExperience(ServerWorld world, Vec3d pos) {
		List<RecipeEntry<?>> list = Lists.newArrayList();
		ObjectIterator var4 = this.recipesUsed.object2IntEntrySet().iterator();
		
		while(var4.hasNext()) {
			Object2IntMap.Entry<Identifier> entry = (Object2IntMap.Entry)var4.next();
			world.getRecipeManager().get((Identifier)entry.getKey()).ifPresent((recipe) -> {
				list.add(recipe);
				dropExperience(world, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipe.value()).getExperience());
			});
		}
		
		return list;
	}
	
	private static void dropExperience(ServerWorld world, Vec3d pos, int multiplier, float experience) {
		int i = MathHelper.floor((float)multiplier * experience);
		float f = MathHelper.fractionalPart((float)multiplier * experience);
		if (f != 0.0F && Math.random() < (double)f) {
			++i;
		}
		
		ExperienceOrbEntity.spawn(world, pos, i);
	}
	
	public void provideRecipeInputs(RecipeMatcher finder) {
		Iterator var2 = this.inventory.iterator();
		
		while(var2.hasNext()) {
			ItemStack itemStack = (ItemStack)var2.next();
			finder.addInput(itemStack);
		}
		
	}
	
}
