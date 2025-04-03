package me.villagerunknown.innsandinnkeepers.item;

import me.shedaniel.clothconfig2.api.Tooltip;
import me.villagerunknown.innsandinnkeepers.Innsandinnkeepers;
import me.villagerunknown.innsandinnkeepers.entity.block.FireplaceBlockEntity;
import me.villagerunknown.innsandinnkeepers.feature.fireplaceBlockFeature;
import me.villagerunknown.innsandinnkeepers.feature.hearthstoneItemFeature;
import me.villagerunknown.platform.util.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.BundleTooltipData;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

import static me.villagerunknown.innsandinnkeepers.Innsandinnkeepers.MOD_ID;

public class HearthstoneItem extends Item {
	
	private static final int MAX_USE_TIME = Innsandinnkeepers.CONFIG.hearthstoneUseTime;
	private static final int COOLDOWN_TIME = Innsandinnkeepers.CONFIG.hearthstoneCooldownTime;
	
	public HearthstoneItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if( !world.isClient ) {
			if( remainingUseTicks > 0 ) {
				EntityUtil.spawnParticles( user, 1, ParticleTypes.REVERSE_PORTAL, 1, 0.05, 0.05, 0.05, 0.05);
			} else {
				user.stopUsingItem();
			} // if, else
		} // if
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		
		if( !world.isClient() ) {
			BlockPos blockPos = context.getBlockPos();
			
			if(
					world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "cobblestone_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "cobbled_deepslate_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "mossy_cobblestone_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "brick_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "stone_brick_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "mossy_stone_brick_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "deepslate_brick_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "tuff_brick_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "mud_brick_fireplace" ) )
					|| world.getBlockState( blockPos ).isOf( fireplaceBlockFeature.BLOCKS.get( "polished_blackstone_brick_fireplace" ) )
			) {
				world.playSound((PlayerEntity)null, blockPos, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.PLAYERS, 1.0F, 1.0F);
				
				ItemStack itemStack = context.getStack();
				
				LodestoneTrackerComponent lodestoneTrackerComponent = new LodestoneTrackerComponent(Optional.of(GlobalPos.create(world.getRegistryKey(), blockPos)), false);
				itemStack.set(DataComponentTypes.LODESTONE_TRACKER, lodestoneTrackerComponent);
				
				if( null != context.getPlayer() ) {
					MessageUtil.sendChatMessage(context.getPlayer(), Text.translatable( "item.villagerunknown-innsandinnkeepers.hearthstone.bound" ).getString());
				} // if
				
				return ActionResult.SUCCESS;
			} // if
		} // if
		
		return super.useOnBlock(context);
	}
	
	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if( !world.isClient() ) {
			world.playSound((PlayerEntity)null, user.getBlockPos(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
			
			return ItemUsage.consumeHeldItem(world, user, hand);
		} // if
		
		return ActionResult.PASS;
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		ItemStack itemStack = super.finishUsing(stack, world, user);
		
		boolean destroyed = false;
		
		if( !world.isClient() ) {
			LodestoneTrackerComponent lodestoneTrackerComponent = (LodestoneTrackerComponent)stack.get(DataComponentTypes.LODESTONE_TRACKER);
			if (lodestoneTrackerComponent != null && lodestoneTrackerComponent.target().isPresent()) {
				MinecraftServer server = world.getServer();
				RegistryKey<World> dimension = lodestoneTrackerComponent.target().get().dimension();
				BlockPos trackedPos = lodestoneTrackerComponent.target().get().pos();
				
				if( null != server && null != dimension && null != trackedPos ) {
					ServerWorld dimWorld = server.getWorld( dimension );
					
					if( null != dimWorld ) {
						BlockEntity trackedBlockEntity = dimWorld.getBlockEntity(trackedPos);
						
						if( null != trackedBlockEntity ) {
							BlockEntityType<?> trackedBlockType = trackedBlockEntity.getType();
							
							if( null != trackedBlockType && trackedBlockType == fireplaceBlockFeature.FIREPLACE_BLOCK_ENTITY ) {
								BlockPos pos = PositionUtil.findSafeSpawnPosition( dimWorld, lodestoneTrackerComponent.target().get().pos(), 2);
								
								world.playSound((PlayerEntity)null, user.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
								user.teleport( dimWorld, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, PositionFlag.DELTA, user.getYaw(), user.getPitch(), false );
								world.playSound((PlayerEntity)null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
								EntityUtil.spawnParticles( user, 1, ParticleTypes.REVERSE_PORTAL, 20, 0.05, -0.05, 0.05, 0.05);
								
								if( user instanceof PlayerEntity player ) {
									player.getItemCooldownManager().set(Identifier.of(MOD_ID,hearthstoneItemFeature.HEARTHSTONE_STRING), COOLDOWN_TIME);
									player.incrementStat(Stats.USED.getOrCreateStat(this));
								} // if
							} else {
								destroyed = true;
							} // if, else
						} else {
							destroyed = true;
						} // if, else
					} else {
						destroyed = true;
					} // if, else
				} else {
					destroyed = true;
				} // if, else
			} else {
				MessageUtil.sendChatMessage((PlayerEntity) user, Text.translatable( "item.villagerunknown-innsandinnkeepers.hearthstone.notbound" ).getString());
			} // if, else
		} // if
		
		if( destroyed ) {
			MessageUtil.sendChatMessage((PlayerEntity) user, Text.translatable( "item.villagerunknown-innsandinnkeepers.hearthstone.destroyed" ).getString());
			
			itemStack.remove(DataComponentTypes.LODESTONE_TRACKER);
		} // if
		
		return itemStack;
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.DRINK;
	}
	
	public int getMaxUseTime(ItemStack stack, LivingEntity user) {
		return MAX_USE_TIME;
	}
	
	public SoundEvent getDrinkSound() {
		return SoundEvents.BLOCK_BELL_RESONATE;
	}
	
}
