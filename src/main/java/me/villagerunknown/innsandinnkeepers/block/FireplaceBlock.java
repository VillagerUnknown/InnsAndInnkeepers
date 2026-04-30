package me.villagerunknown.innsandinnkeepers.block;

import com.mojang.serialization.MapCodec;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.entity.block.FireplaceBlockEntity;
import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.platform.util.MathUtil;
import me.villagerunknown.platform.util.TimeUtil;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
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
	
	public static final EnumProperty<Direction> FACING;
	public static final BooleanProperty LIT;
	
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
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(LIT, true));
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
	
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
	}
	
	protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
		if( Innsandinnkeepers.CONFIG.enableFireplaceCooking ) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof FireplaceBlockEntity) {
				player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
//				player.incrementStat( Stats.USED.getOrCreateStat(this) );
			}
		} // if
	}
	
	@Override
	protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
		ItemStack stackInHand = player.getMainHandStack();
		
		if( null != stackInHand && !stackInHand.isEmpty() ) {
			boolean changed = false;
			
			if( !state.get(LIT) && stackInHand.isIn( TagKey.of( RegistryKeys.ITEM, Identifier.of(MOD_ID, "igniters")) ) ) {
				state = ignite( world, state, pos );
				changed = true;
			} else if( state.get(LIT) && stackInHand.isIn( TagKey.of( RegistryKeys.ITEM, Identifier.of(MOD_ID, "extinguishers")) ) ) {
				state = extinguish( world, state, pos );
				changed = true;
			} // if, else if
			
			if( changed ) {
				if( stackInHand.isDamageable() ) {
					stackInHand.damage(1, player, player.getActiveHand().getEquipmentSlot());
				} else {
					stackInHand.decrementUnlessCreative(1, player);
				} // if, else
				
				return ActionResult.SUCCESS;
			} // if
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
	
	protected BlockState rotate(BlockState state, BlockRotation rotation) {
		return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
	}
	
	protected BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{FACING, LIT});
	}
	
	static {
		FACING = HorizontalFacingBlock.FACING;
		LIT = Properties.LIT;
	}
	
}
