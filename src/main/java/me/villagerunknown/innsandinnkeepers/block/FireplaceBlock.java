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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FireplaceBlock extends AbstractFurnaceBlock {
	
	public static final int MAX_BLOCKS_SMOKE_PASSES_THROUGH = Innsandinnkeepers.CONFIG.maxFireplaceSmokeThroughBlocks;
	
	public static final MapCodec<FireplaceBlock> CODEC = createCodec(FireplaceBlock::new);
	
	public MapCodec<FireplaceBlock> getCodec() {
		return CODEC;
	}
	
	public FireplaceBlock() {
		super(
				Settings.copy(Blocks.SMOKER)
		);
	}
	
	public FireplaceBlock(AbstractBlock.Settings settings) {
		super(settings);
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
	
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if ((Boolean)state.get(LIT)) {
			double d = (double)pos.getX() + 0.5;
			double e = (double)pos.up().getY();
			double f = (double)pos.getZ() + 0.5;
			if (random.nextDouble() < 0.1) {
				world.playSound(d, e, f, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			} // if
		} // if
	}
	
}
