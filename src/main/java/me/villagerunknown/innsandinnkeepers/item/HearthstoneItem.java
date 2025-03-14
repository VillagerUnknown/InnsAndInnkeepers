package me.villagerunknown.innsandinnkeepers.item;

import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.platform.util.EntityUtil;
import me.villagerunknown.platform.util.MessageUtil;
import me.villagerunknown.platform.util.PlayerUtil;
import me.villagerunknown.platform.util.PositionUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class HearthstoneItem extends Item {
	
	private static final int MAX_USE_TIME = 80;
	
	public HearthstoneItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if( !world.isClient && remainingUseTicks > 0 ) {
			EntityUtil.spawnParticles( user, 1, ParticleTypes.PORTAL, 1, 0.05, 0.05, 0.05, 0.05);
		} // if
		
		super.usageTick(world, user, stack, remainingUseTicks);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		
		if( world.isClient() ) {
			return super.useOnBlock(context);
		}
		
		BlockPos blockPos = context.getBlockPos();
		
		if(
				world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "brick_fireplace" ) )
				|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "stone_brick_fireplace" ) )
				|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "mossy_stone_brick_fireplace" ) )
				|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "deepslate_brick_fireplace" ) )
				|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "tuff_brick_fireplace" ) )
				|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "mud_brick_fireplace" ) )
				|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "polished_blackstone_brick_fireplace" ) )
		) {
			world.playSound((PlayerEntity)null, blockPos, SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM, SoundCategory.PLAYERS, 1.0F, 1.0F);

			ItemStack itemStack = context.getStack();

			LodestoneTrackerComponent lodestoneTrackerComponent = new LodestoneTrackerComponent(Optional.of(GlobalPos.create(world.getRegistryKey(), blockPos)), false);
			itemStack.set(DataComponentTypes.LODESTONE_TRACKER, lodestoneTrackerComponent);

			if( null != context.getPlayer() ) {
				MessageUtil.sendChatMessage(context.getPlayer(), "Hearthstone bound.");
			} // if
		} // if
		
		return super.useOnBlock(context);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ItemUsage.consumeHeldItem(world, user, hand);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if( world.isClient() || user.isSneaking() ) {
			return super.finishUsing(stack, world, user);
		}
		
		LodestoneTrackerComponent lodestoneTrackerComponent = (LodestoneTrackerComponent)stack.get(DataComponentTypes.LODESTONE_TRACKER);
		if (lodestoneTrackerComponent != null) {
			if( lodestoneTrackerComponent.target().isPresent() ) {
				BlockPos pos = PositionUtil.findSafeSpawnPosition( world, lodestoneTrackerComponent.target().get().pos(), 2);
				
				user.teleport(pos.getX(), pos.getY(), pos.getZ(), true);
			} // if
		} // if
		
		return super.finishUsing(stack, world, user);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}
	
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return MAX_USE_TIME;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		tooltip.add( Text.translatable( "item.villagerunknown-innsandinnkeepers.hearthstone.tooltip.bind" ) );
		tooltip.add( Text.translatable( "item.villagerunknown-innsandinnkeepers.hearthstone.tooltip.teleport" ) );
		
		LodestoneTrackerComponent lodestoneTrackerComponent = (LodestoneTrackerComponent)stack.get(DataComponentTypes.LODESTONE_TRACKER);
		if (lodestoneTrackerComponent != null) {
			if( lodestoneTrackerComponent.target().isPresent() ) {
				BlockPos pos = lodestoneTrackerComponent.target().get().pos();
				
				tooltip.addLast( Text.of( "Bound to: " + pos.getX() + " " + pos.getY() + " " + pos.getZ() ) );
			} // if
		} // if
		
		super.appendTooltip(stack, context, tooltip, type);
	}
	
	public SoundEvent getDrinkSound() {
		return SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM;
	}
	
}
