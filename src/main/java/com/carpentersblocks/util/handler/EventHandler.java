package com.carpentersblocks.util.handler;

import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.block.BlockCarpentersSlope;
import com.carpentersblocks.item.CbItems;
import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.network.CarpentersBlocksPacketHandler;
import com.carpentersblocks.network.PacketSlopeSelect;
import com.carpentersblocks.util.BlockUtil;
import com.carpentersblocks.util.EntityLivingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent.MouseScrollEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = CarpentersBlocks.MOD_ID)
public class EventHandler {
	
    private static boolean isValidBlockEvent(IWorld world, BlockPos pos) {
    	TileEntity tileEntity = world.getBlockEntity(pos);
    	return tileEntity != null && tileEntity instanceof CbTileEntity && world.isClientSide();
    }
    
    /**
     * Used to prevent block destruction if block is a Carpenter's Block
     * and player is holding a Carpenter's tool.
     */
    @SubscribeEvent
    public static void onBlockBreakEvent(BlockEvent.BreakEvent event) {
    	if (isValidBlockEvent(event.getWorld(), event.getPos())) {
        	ItemStack itemStack = event.getPlayer().getMainHandItem();
        	boolean hasTool = itemStack != null && (itemStack.getItem() instanceof ICarpentersHammer || itemStack.getItem() instanceof ICarpentersChisel);
            if (hasTool && event.getPlayer().isCreative()) {
            	event.setCanceled(true);
            }
    	}
    }
    
    /**
     * Override slope items so only wedge type is dropped.
     * 
     * @param event the event
     */
    @SubscribeEvent
    public static void onItemTossEvent(ItemTossEvent event) {
    	Item item = event.getEntityItem().getItem().getItem();
    	if (item instanceof BlockItem && CbItems.slopeBlockItemSubTypes.contains(item)) {
    		ItemStack itemStack = new ItemStack(CbItems.blockItemSlopeWedge, event.getEntityItem().getItem().getCount());
    		event.getEntityItem().setItem(itemStack);
    	}
    }
    
    /**
     * Grabs mouse scroll events for slope selection.
     */
    @SubscribeEvent
    public static void onMouseScrollEvent(MouseScrollEvent event) {
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if (playerEntity != null && playerEntity.isCrouching()) {
            ItemStack itemStack = playerEntity.getMainHandItem();
            if (itemStack != null && BlockUtil.toBlock(itemStack) instanceof BlockCarpentersSlope) {
                if (event.getScrollDelta() != 0) {
                    CarpentersBlocksPacketHandler.sendToServer(new PacketSlopeSelect(playerEntity.inventory.selected, event.getScrollDelta() > 0));
                }
                event.setCanceled(true);
            }
        }
    }

    /**
     * Handle spawning sprint particles beneath the entity's feet.
     * 
     * @param event the living update event
     */
    @SubscribeEvent
    public static void onLivingUpdateEvent(LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.level;
        if (!EntityLivingUtil.isMovingOnGround(livingEntity)) {
            return;
        }
        CbTileEntity cbTileEntity = EntityLivingUtil.getTileEntityAtFeet(livingEntity);
        if (cbTileEntity != null) {
            ItemStack itemStack = BlockUtil.getFeatureSensitiveSideItemStack(cbTileEntity, Direction.UP);
            // Spawn sprint particles client-side
            // TODO: May not be necessary anymore; check block methods
            if (world.isClientSide() && livingEntity.isSprinting() && !livingEntity.isInWater()) {
            	//livingEntity.func_233569_aL_();
                //ParticleHelper.spawnTileParticleAt(livingEntity, itemStack);
            }
        }
    }

    /**
     * {@link onPlaySoundEvent} is used differently for singleplayer
     * and multiplayer sound events. This will try to locate the {@link CbTileEntity}
     * that best represents the origin of the sound.
     * <p>
     * In singleplayer, this is normally the origin since it's used mainly
     * for placement and destruction sounds.
     * <p>
     * In multiplayer, foreign players produce this event for step sounds,
     * requiring a y offset of -1 to approximate the origin.
     *
     * @param  world the {@link World}
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return an approximate {@link CbTileEntity} used for producing a sound
     */
    private static CbTileEntity getApproximateSoundOrigin(World world, BlockPos blockPos) {
        // Try origin first
        TileEntity tileEntity = world.getBlockEntity(blockPos);
        if (tileEntity != null && tileEntity instanceof CbTileEntity) {
            return (CbTileEntity) tileEntity;
        } else {
            // Try y-offset -1
            TileEntity TE_YN = world.getBlockEntity(blockPos.below());
            if (TE_YN != null && TE_YN instanceof CbTileEntity) {
                return (CbTileEntity) TE_YN;
            }
        }
        return null;
    }

    @SubscribeEvent
    public static void onPlaySoundEvent(PlaySoundEvent event) {
/*        if (event != null && event.name != null && event.name.contains(CarpentersBlocks.MODID))
        {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT)
            {
                World world = FMLClientHandler.instance().getClient().theWorld;
                int x = MathHelper.floor_double(event.sound.getXPosF());
                int y = MathHelper.floor_double(event.sound.getYPosF());
                int z = MathHelper.floor_double(event.sound.getZPosF());

                // We'll set a default block type to be safe
                Block block = Blocks.planks;

                // Grab approximate origin, and gather accurate block type
                CbTileEntity TE = getApproximateSoundOrigin(world, x, y, z);
                if (TE != null && TE.hasAttribute(TE.ATTR_COVER[6])) {
                    block = BlockProperties.toBlock(BlockProperties.getCoverSafe(TE, 6));
                }

                if (event.name.startsWith("step.")) {
                    event.result = new PositionedSoundRecord(new ResourceLocation(block.stepSound.getStepResourcePath()), block.stepSound.getVolume() * 0.15F, block.stepSound.getPitch(), x + 0.5F, y + 0.5F, z + 0.5F);
                } else { // "dig." usually
                    event.result = new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F, x + 0.5F, y + 0.5F, z + 0.5F);
                }
            }
        }*/
    }

    /**
     * Override sounds when walking on block.
     *
     * @param event
     */
    @SubscribeEvent
    public static void onPlaySoundAtEntityEvent(PlaySoundAtEntityEvent event) {
    	// Shouldn't need this if block defaults to wood blockstate
        /*if (event != null && event.getSound() != null && event.name.contains(CarpentersBlocks.MODID))
        {
            Entity entity = event.getEntity();

            // The function to my knowledge is only used for playing walking sounds
            // at entity, so we'll check for the conditions first.
            if (!isMovingOnGround(entity)) {
                return;
            }

            CbTileEntity cbTileEntity = getTileEntityAtFeet(entity);
            if (cbTileEntity != null) {

                // Give SoundType a valid resource by default
            	event.setSound(Blocks.PLANKS.getSoundType().getStepSound())

                // Gather accurate SoundType based on block properties
                Block block = BlockProperties.toBlock(BlockProperties.getFeatureSensitiveSideItemStack(cbTileEntity, Direction.UP));
                if (!(block instanceof BlockCoverable)) {
                	event.setSound(block.getSoundType().getStepSound());
                }
            }
        }*/
    }
    
}
