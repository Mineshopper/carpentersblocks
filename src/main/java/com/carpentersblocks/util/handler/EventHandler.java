package com.carpentersblocks.util.handler;

import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.block.BlockUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {

	public static EnumFacing eventFace;
	public static EnumHand eventHand;
	public static Vec3d eventHitVector;
	
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    /**
     * Check render settings on GUI open/close event.
     */
    public void onGuiOpenEvent(GuiOpenEvent event)
    {
/*        if (event.gui == null) {
            if (ShadersHandler.enableShadersModCoreIntegration) {
                ShadersHandler.update();
            }
        }*/
    }

    private boolean isValidBlockEvent(World world, BlockPos pos) {
    	TileEntity tileEntity = world.getTileEntity(pos);
    	return tileEntity != null && tileEntity instanceof CbTileEntity;
    }
    
    @SubscribeEvent
    /**
     * Used to prevent block destruction if block is a Carpenter's Block
     * and player is holding a Carpenter's tool.
     */
    public void onBlockBreakEvent(BlockEvent.BreakEvent event) {
    	if (isValidBlockEvent(event.getWorld(), event.getPos())) {
        	ItemStack itemStack = event.getPlayer().getHeldItemMainhand();
        	boolean hasTool = itemStack != null && (itemStack.getItem() instanceof ICarpentersHammer || itemStack.getItem() instanceof ICarpentersChisel);
            if (hasTool && event.getPlayer().capabilities.isCreativeMode) {
            	event.setCanceled(true);
            }
    	}
    }

    private Vec3d getNormalizedHitVec(Vec3d vec, BlockPos pos) {
    	double x = Math.abs(vec.xCoord - pos.getX());
    	double y = Math.abs(vec.yCoord - pos.getY());
    	double z = Math.abs(vec.zCoord - pos.getZ());
    	return new Vec3d(x, y, z);
    }
    
    /**
     * Handles block left-click events.
     * <p>
     * Used to invoke {@link Block#onBlockClicked(World, BlockPos, EntityPlayer) onBlockClicked}
     * when a Carpenter's tool is held.  This would normally destroy a block in creative, or attempt
     * to damage a block in survival mode.
     * 
     * @param event the event
     */
    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
    	if (isValidBlockEvent(event.getWorld(), event.getPos())) {
    		eventFace = event.getFace();
    		eventHand = event.getHand();
    		eventHitVector = getNormalizedHitVec(event.getHitVec(), event.getPos());
	    	ItemStack itemStack = event.getEntityPlayer().getHeldItem(event.getHand());
	    	boolean hasTool = itemStack != null && (itemStack.getItem() instanceof ICarpentersHammer || itemStack.getItem() instanceof ICarpentersChisel);
	        if (hasTool && event.getEntityPlayer().capabilities.isCreativeMode) {
	        	IBlockState blockState = event.getEntityPlayer().getEntityWorld().getBlockState(event.getPos());
	            blockState.getBlock().onBlockClicked(event.getEntityPlayer().getEntityWorld(), event.getPos(), event.getEntityPlayer());
	            if (event.getWorld().isRemote) {
	            	event.setCanceled(true);
	            }
	        }
    	}
    }
        
    /**
     * Handles block right-click events.
     * <p>
     * Used to invoke {@link Block#onBlockActivated(World, BlockPos, IBlockState, EntityPlayer, EnumHand, ItemStack, EnumFacing, float, float, float) onBlockActivated}
     * when a Carpenter's tool is held and player is sneaking.  This would normally do nothing.
     * 
     * @param event the event
     */
    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
    	if (isValidBlockEvent(event.getWorld(), event.getPos())) {
    		eventFace = event.getFace();
    		eventHand = event.getHand();
    		eventHitVector = getNormalizedHitVec(event.getHitVec(), event.getPos());
    		ItemStack itemStack = event.getEntityPlayer().getHeldItem(event.getHand());
            if (event.getEntityPlayer().isSneaking()) {
                if (!(itemStack != null && itemStack.getItem() instanceof ItemBlock && !BlockUtil.isOverlay(itemStack))) {
                	IBlockState blockState = event.getWorld().getBlockState(event.getPos());
                	blockState.getBlock().onBlockActivated(event.getEntityPlayer().worldObj, event.getPos(), blockState, event.getEntityPlayer(), EnumHand.MAIN_HAND, event.getEntityPlayer().getHeldItemMainhand(), event.getFace(), 1.0F, 1.0F, 1.0F);
                }
            }
    	}
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    /**
     * Grabs mouse scroll events for slope selection.
     */
    public void onMouseEvent(MouseEvent event)
    {
        // We only want to process wheel events
        if (event.getButton() < 0) {
            EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
            if (entityPlayer != null && entityPlayer.isSneaking()) {
                ItemStack itemStack = entityPlayer.getHeldItemMainhand();
/*                if (itemStack != null && itemStack.getItem() instanceof ItemBlock && BlockProperties.toBlock(itemStack).equals(BlockRegistry.blockCarpentersSlope)) {
                    if (event.getDwheel() != 0) {
                        PacketHandler.sendPacketToServer(new PacketSlopeSelect(entityPlayer.inventory.currentItem, event.dwheel > 0));
                    }
                    event.setCanceled(true);
                }*/
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdateEvent(LivingUpdateEvent event)
    {
        EntityLivingBase entityLivingBase = event.getEntityLiving();
        World world = entityLivingBase.worldObj;

        	// BlockState may negate the need for all this
/*
        if (!isMovingOnGround(entity))
        {
            return;
        }

        CbTileEntity TE = getTileEntityAtFeet(entity);
        if (TE != null) {

            ItemStack itemStack = BlockProperties.getFeatureSensitiveSideItemStack(TE, ForgeDirection.UP);

             Spawn sprint particles client-side. 

            if (world.isRemote && entity.isSprinting() && !entity.isInWater()) {
                ParticleHelper.spawnTileParticleAt(entity, itemStack);
            }

             Adjust block slipperiness according to cover. 

            Block block = BlockProperties.toBlock(itemStack);
            if (block instanceof BlockCoverable) {
                TE.getBlockType().slipperiness = Blocks.dirt.slipperiness;
            } else {
                TE.getBlockType().slipperiness = block.slipperiness;
            }

        }*/
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
    private CbTileEntity getApproximateSoundOrigin(World world, BlockPos blockPos)
    {
        // Try origin first
        TileEntity tileEntity = world.getTileEntity(blockPos);
        if (tileEntity != null && tileEntity instanceof CbTileEntity) {
            return (CbTileEntity) tileEntity;
        } else {
            // Try y-offset -1
            TileEntity TE_YN = world.getTileEntity(blockPos.add(0, -1, 0));
            if (TE_YN != null && TE_YN instanceof CbTileEntity) {
                return (CbTileEntity) TE_YN;
            }
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlaySoundEvent(PlaySoundEvent event)
    {
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
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlaySoundAtEntityEvent(PlaySoundAtEntityEvent event)
    {
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
                Block block = BlockProperties.toBlock(BlockProperties.getFeatureSensitiveSideItemStack(cbTileEntity, EnumFacing.UP));
                if (!(block instanceof BlockCoverable)) {
                	event.setSound(block.getSoundType().getStepSound());
                }
            }
        }*/
    }

    /**
     * Gets the {@link CbTileEntity} object at player's feet, if one exists.
     * <p>
     * It is safer to gather the tile entity reference than a block reference.
     *
     * @param entity
     * @return
     */
    private CbTileEntity getTileEntityAtFeet(Entity entity) {
        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.posY - 0.20000000298023224D - entity.getYOffset());
        int z = MathHelper.floor_double(entity.posZ);

        TileEntity tileEntity = entity.worldObj.getTileEntity(new BlockPos(x, y, z));
        if (tileEntity != null && tileEntity instanceof CbTileEntity) {
            return (CbTileEntity) tileEntity;
        } else {
            return null;
        }
    }

    /**
     * Determines if the player is moving in the x, z directions on
     * solid ground.
     *
     * @param entity
     * @return
     */
    private boolean isMovingOnGround(Entity entity) {
        return entity.onGround && (entity.motionX != 0 || entity.motionZ != 0);
    }

}
