package me.villagerunknown.innsandinnkeepers.block;

import com.mojang.serialization.MapCodec;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.entity.block.FireplaceBlockEntity;
import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.platform.util.MathUtil;
import me.villagerunknown.platform.util.TimeUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class FireplaceBlock extends AbstractFurnaceBlock {
	
	private static final List<Item> IGNITERS = new ArrayList<>(List.of(
			Items.FLINT_AND_STEEL,
			Items.FIRE_CHARGE
	));
	
	private static final List<Item> EXTINGUISHERS = new ArrayList<>(List.of(
			Items.WOODEN_SHOVEL,
			Items.STONE_SHOVEL,
			Items.IRON_SHOVEL,
			Items.GOLDEN_SHOVEL,
			Items.DIAMOND_SHOVEL,
			Items.NETHERITE_SHOVEL
	));
	
	public static final int MAX_BLOCKS_SMOKE_PASSES_THROUGH = Innsandinnkeepers.CONFIG.maxFireplaceSmokeThroughBlocks;
	
	public static final MapCodec<FireplaceBlock> CODEC = createCodec(FireplaceBlock::new);
	
	public MapCodec<FireplaceBlock> getCodec() {
		return CODEC;
	}
	
	public FireplaceBlock( String path ) {
		super(
				Settings.copy(Blocks.SMOKER)
						.registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID,path)))
		);
		this.setDefaultState((BlockState)(this.stateManager.getDefaultState()).with(LIT, true));
	}
	
	public FireplaceBlock(Settings settings) {
		super(settings);
		this.setDefaultState((BlockState)(this.stateManager.getDefaultState()).with(LIT, true));
	}
	
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FireplaceBlockEntity(pos, state);
	}
	
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return validateTicker( type, fireplaceBlockFeature.FIREPLACE_BLOCK_ENTITY, FireplaceBlockEntity::tick );
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
//		BlockEntity blockEntity = world.getBlockEntity(pos);
//		if (blockEntity instanceof FireplaceBlockEntity) {
//			player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
//			player.incrementStat(Stats.INTERACT_WITH_SMOKER);
//		}
	}
	
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		ItemStack stackInHand = player.getMainHandStack();
		
		if( null != stackInHand && !stackInHand.isEmpty() ) {
			Item itemInHand = stackInHand.getItem();
			
			if( !state.get(LIT) && IGNITERS.contains( itemInHand ) ) {
				state = ignite( world, state, pos );
				
				if( Items.FIRE_CHARGE == itemInHand ) {
					stackInHand.decrementUnlessCreative(1, player);
				} else if( Items.FLINT_AND_STEEL == itemInHand  ) {
					stackInHand.damage(1, player, LivingEntity.getSlotForHand(player.getActiveHand()));
				} // if, else
			} else if( state.get(LIT) && EXTINGUISHERS.contains( itemInHand ) ) {
				state = extinguish( world, state, pos );
				
				stackInHand.damage(1, player, LivingEntity.getSlotForHand(player.getActiveHand()));
			} // if, else if
		} // if
		
		return super.onUse(state, world, pos, player, hit);
	}
	
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if ((Boolean)state.get(LIT)) {
			double d = (double)pos.getX() + 0.5;
			double e = (double)pos.up().getY();
			double f = (double)pos.getZ() + 0.5;
			if (random.nextDouble() < 0.1) {
				world.playSoundClient(d, e, f, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			} // if
		} // if
	}
	
//	@Override
//	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
//		world.setBlockState( pos, state.with( LIT, true ) );
//
//		super.onPlaced(world, pos, state, placer, itemStack);
//	}
	
	public BlockState ignite( World world, BlockState state, BlockPos pos ) {
		world.playSoundAtBlockCenterClient( pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 0.5F, 1, true );
		state = state.with(LIT,true);
		world.setBlockState( pos, state );
		return state;
	}
	
	public BlockState extinguish( World world, BlockState state, BlockPos pos ) {
		world.playSoundAtBlockCenterClient( pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 1, 1, true );
		state = state.with(LIT,false);
		world.setBlockState( pos, state );
		return state;
	}
	
}
