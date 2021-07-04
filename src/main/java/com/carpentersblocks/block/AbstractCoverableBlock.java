package com.carpentersblocks.block;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.client.renderer.RenderConstants;
import com.carpentersblocks.config.Configuration;
import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.nbt.attribute.AbstractAttribute.Key;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.network.PacketUseItemOnBlock;
import com.carpentersblocks.util.BlockUtil;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DesignHandler.DesignType;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;
import com.carpentersblocks.util.protection.PlayerPermissions;
import com.carpentersblocks.util.protection.ProtectedObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public abstract class AbstractCoverableBlock extends Block implements IWaterLoggable {
	
    /** Block drop event for dropping attribute. */
    public static final int EVENT_ID_DROP_ATTR = 0x40000000;

    /** Indicates during getDrops that block instance should not be dropped. */
    protected final int METADATA_DROP_ATTR_ONLY = 16;

    /**
     * Stores actions taken on a block in order to properly play sounds,
     * decrement player inventory, and to determine if a block was altered.
     */
    protected class ActionResult {

        public ItemStack itemStack;
        public boolean playSound = true;
        public boolean altered = false;
        public boolean decInv = false;

        public ActionResult setSoundSource(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }

        public ActionResult setNoSound() {
            playSound = false;
            return this;
        }

        public ActionResult setAltered() {
            altered = true;
            return this;
        }

        public ActionResult decInventory() {
            decInv = true;
            return this;
        }

    }

    /**
     * Class constructor.
     *
     * @param material the block material
     */
    public AbstractCoverableBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    public VoxelShape getCollisionShape(BlockState blockState, IBlockReader world, BlockPos blockPos, ISelectionContext context) {
    	/*super.getCollisionShape(state, worldIn, pos, context)
    	if (this instanceof IStateImplementor) {
	    	List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();
	        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
	        if (cbTileEntity != null) {
	        	for (AxisAlignedBB aabb : StateFactory.getState(cbTileEntity).getBoundingBoxes()) {
	        		list.add(this.rayTrace(blockPos, start, end, aabb));
	        	}
	        } else {
	        	return super.collisionRayTrace(blockState, world, blockPos, start, end);
	        }
	        RayTraceResult finalRayTraceResult = null;
	        double d1 = 0.0D;
	        for (RayTraceResult rayTraceResult : list) {
	            if (rayTraceResult != null) {
	                double d0 = rayTraceResult.hitVec.squareDistanceTo(end);
	                if (d0 > d1) {
	                    finalRayTraceResult = rayTraceResult;
	                    d1 = d0;
	                }
	            }
	        }
	        return finalRayTraceResult;
        } else {*/
        	return super.getCollisionShape(blockState, world, blockPos, context);
        //}
    }

    /**
     * &lt;Step 2&gt; in block drop process
	 * <p>
     * Called when block event is received.
     *<p>
     * For the context of this mod, this is used for dropping block attributes
     * like covers, overlays, dyes, or any other ItemStack. In this case, the
     * attribute will <i>always</i> be droppable, and never <code>null</code>.
     *<p>
     * In order for external classes to call the protected method
     * {@link Block#dropBlockAsItem(World,int,int,int,ItemStack) dropBlockAsItem},
     * they create a block event with parameters itemId and metadata, allowing
     * the {@link ItemStack} to be recreated and dropped.
     *
     * @param world the world
     * @param blockPos the block position
     * @param id the id of the event
     * @param param hashcode for a {@link Key}
     * @return <code>true</code> if event was handled
     */
    @Override
    public boolean triggerEvent(BlockState blockState, World world, BlockPos blockPos, int id, int param) {
    	if (id == EVENT_ID_DROP_ATTR) {
            CbTileEntity cbTileEntity = getSimpleTileEntity(world, blockPos);
            if (cbTileEntity != null) {
            	Key key = new Key(param);
            	if (cbTileEntity.getAttributeHelper().hasAttribute(key)) {
	                ItemStack itemStack = cbTileEntity.getDroppableItemStack(key);
	                if (cbTileEntity.eventReceived(key)) {
	                	// <Step 4> in block drop process
	                	popResource(world, blockPos, itemStack);
	                	return true;
	                } else {
	                	return false;
	                }
            	}
            }
        }
        return super.triggerEvent(blockState, world, blockPos, id, param);
    }
    
    @Override
    public List<ItemStack> getDrops(BlockState state, Builder builder) {
    	TileEntity tileEntity = builder.getParameter(LootParameters.BLOCK_ENTITY);
    	if (tileEntity instanceof CbTileEntity) {
        	builder = builder.withDynamicDrop(new ResourceLocation("contents"), (p_220168_1_, p_220168_2_) -> {
        		//if (metadata == METADATA_DROP_ATTR_ONLY) {
        		//	list.clear(); // Remove block instance from drop list 
        		//}
        		CbTileEntity cbTileEntity = (CbTileEntity) tileEntity;
        		for (ItemStack itemStack : cbTileEntity.getAllDroppableAttributes()) {
        			p_220168_2_.accept(itemStack);
        		}
        	});
    	}
    	return super.getDrops(state, builder);
    }
    
    /**
     * Returns adjacent, similar tile entities that can be used for duplicating
     * block properties like dye color, pattern, style, etc.
     *
     * @param world the world reference
     * @param blockPos the block position
     * @return an array of adjacent, similar tile entities
     */
    protected CbTileEntity[] getAdjacentTileEntities(World world, BlockPos blockPos) {
        return new CbTileEntity[] {
            getSimpleTileEntity(world, blockPos.below()),
            getSimpleTileEntity(world, blockPos.above()),
            getSimpleTileEntity(world, blockPos.north()),
            getSimpleTileEntity(world, blockPos.south()),
            getSimpleTileEntity(world, blockPos.west()),
            getSimpleTileEntity(world, blockPos.east())
        };
    }

    /**
     * Returns tile entity if block tile entity is instanceof CbTileEntity.
     *
     * Used for generic purposes such as getting pattern, dye color, or
     * cover of another Carpenter's block.  Is also used if block
     * no longer exists, such as when breaking a block and ejecting
     * attributes.
     */
    protected CbTileEntity getSimpleTileEntity(IBlockReader blockReader, BlockPos blockPos) {
        TileEntity tileEntity = blockReader.getBlockEntity(blockPos);
        return (tileEntity instanceof CbTileEntity) ? (CbTileEntity) tileEntity : null;
    }

    /**
     * Returns tile entity if block tile entity is instanceof CbTileEntity and
     * also belongs to this block type.
     */
    protected CbTileEntity getTileEntity(IBlockReader blockReader, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getSimpleTileEntity(blockReader, blockPos);
        return cbTileEntity != null && blockReader.getBlockState(blockPos).getBlock().equals(this) ? cbTileEntity : null;
    }

    /**
     * Returns whether player is allowed to activate this block.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param playerEntity the player
     * @return <code>true</code> if player can activate block
     */
    protected boolean canPlayerActivate(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
        return true;
    }
    
    /**
     * Called when block is left clicked by a player.
     * <p>
     * This method will do nothing since we're usually needing to work
     * with a {@link BlockRayTraceResult} and call {@link #attack(BlockState, World, BlockPos, PlayerEntity, Hand, BlockRayTraceResult) here}
     * instead.
     * 
     * @param world the world
     * @param blockPos the block position
     * @param playerEntity the player
     */
    @Override
    public void attack(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity) {
        return;
    }
    
    /**
     * Called when block is left clicked by a player client side and also
     * invoked by server when a {@link PacketUseItemOnBlock.Attack packet}
     * is received.
     * 
     * @param blockState the block state
     * @param world the world
     * @param blockPos the block position
     * @param playerEntity the player entity
     * @param hand the active hand
     * @param blockRayTraceResult the block ray trace result
     */
    public void attack(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
    	if (world.isClientSide) {
            return;
        }
        
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity == null || !PlayerPermissions.hasElevatedPermission(cbTileEntity, playerEntity, false)) {
            return;
        }

        ItemStack itemStack = playerEntity.getMainHandItem();
        if (itemStack.isEmpty()) {
            return;
        }

        ActionResult actionResult = new ActionResult();
        EnumAttributeLocation location = EnumAttributeLocation.UP;
        //EnumAttributeLocation location = BlockUtil.getAttributeLocationForFacing(cbTileEntity, EventHandler.getRayTraceResult().getDirection());
        Item item = itemStack.getItem();
        if (item instanceof ICarpentersHammer && ((ICarpentersHammer)item).canUseHammer(world, playerEntity, Hand.MAIN_HAND)) {
            preOnBlockClicked(cbTileEntity, playerEntity, actionResult);
            if (!actionResult.altered) {
                if (playerEntity.isCrouching()) {
                    if (cbTileEntity.removeLastAddedDroppableAttribute(location)) {
                    	actionResult.setAltered();
                    }
                } else {
                    if (onHammerLeftClick(cbTileEntity, playerEntity)) {
                    	actionResult.setAltered();
                    }
                }
                actionResult.setAltered();
            } else {
            	// TODO: Update this
            	this.onNeighborChange(blockState, world, blockPos, blockPos);
                //world.notifyNeighborsOfStateChange(blockPos, this);
            }
        } else if (item instanceof ICarpentersChisel && ((ICarpentersChisel)item).canUseChisel(world, playerEntity, Hand.MAIN_HAND)) {
            if (playerEntity.isCrouching() && cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.DESIGN_CHISEL)) {
                cbTileEntity.removeAttribute(location, EnumAttributeType.DESIGN_CHISEL);
            } else if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                onChiselClick(cbTileEntity, location, Hand.MAIN_HAND, true);
            }            
        }
        
        if (actionResult.altered) {
            cbTileEntity.setChanged();
            world.sendBlockUpdated(blockPos, blockState, blockState, 3);
        }
    }
    
    /**
     * Called when block is activated.
     * 
     * @param blockState the block state
     * @param world the world
     * @param blockPos the block position
     * @param playerEntity the player
     * @param hand the active player hand
     * @param blockRayTraceResult the block ray trace result
     * return <code>true</code> if block property changed
     */
    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
    	if (world.isClientSide()) {
    		return ActionResultType.sidedSuccess(world.isClientSide());
        }

        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);

        if (cbTileEntity == null || !canPlayerActivate(cbTileEntity, playerEntity)) {
        	return ActionResultType.FAIL;
        }

        EnumAttributeLocation location = BlockUtil.getAttributeLocationForFacing(cbTileEntity, blockRayTraceResult.getDirection());
        
        // Allow block to change cbTileEntity if needed before altering attributes
        cbTileEntity = getTileEntityForBlockActivation(cbTileEntity);
        ActionResult actionResult = new ActionResult();

        preOnBlockActivated(cbTileEntity, playerEntity, blockRayTraceResult, actionResult);

        // If no prior event occurred, try regular activation
        if (!actionResult.altered) {
            if (PlayerPermissions.hasElevatedPermission(cbTileEntity, playerEntity, false)) {
                if (!playerEntity.getItemInHand(hand).isEmpty()) {
                	ItemStack itemStack = playerEntity.getItemInHand(hand);
                	if (itemStack.getItem() instanceof ICarpentersHammer && ((ICarpentersHammer)itemStack.getItem()).canUseHammer(world, playerEntity, hand)) {
                        if (onHammerRightClick(cbTileEntity, playerEntity)) {
                            actionResult.setAltered();
                        }
                	} else if (Configuration.isChiselEnabled() && itemStack.getItem() instanceof ICarpentersChisel && ((ICarpentersChisel)itemStack.getItem()).canUseChisel(world, playerEntity, hand)) {
                        if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                            if (onChiselClick(cbTileEntity, location, hand, false)) {
                                actionResult.setAltered();
                            }
                        }
                    } else if (Configuration.isCoversEnabled() && BlockUtil.isCover(itemStack)) {

                        Block block = BlockUtil.toBlock(itemStack);

                        /* Will handle blocks that save directions using only y axis (pumpkin) */
                        //int metadata = block instanceof BlockDirectional ? MathHelper.floor_double(playerEntity.rotationYaw * 4.0F / 360.0F + 2.5D) & 3 : itemStack.getItemDamage();

                        /* Will handle blocks that save directions using all axes (logs, quartz) */
                        // TODO: Handle later
                        /*if (BlockProperties.blockRotates(itemStack)) {
                            int rot = Direction.rotateOpposite[EntityLivingUtil.getRotationValue(playerEntity)];
                            int side_interpolated = playerEntity.rotationPitch < -45.0F ? 0 : playerEntity.rotationPitch > 45 ? 1 : rot == 0 ? 3 : rot == 1 ? 4 : rot == 2 ? 2 : 5;
                            metadata = block.onBlockPlaced(world, cbTileEntity.xCoord, cbTileEntity.yCoord, cbTileEntity.zCoord, side_interpolated, hitX, hitY, hitZ, metadata);
                        }*/
                        
                        if (EnumAttributeLocation.HOST.equals(location) && (!canSupportCover(cbTileEntity, location) || cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER))) {
                            location = EnumAttributeLocation.valueOf(blockRayTraceResult.getDirection().ordinal());
                        }
                        
                        if (canSupportCover(cbTileEntity, location) && !cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                            cbTileEntity.addAttribute(location, EnumAttributeType.COVER, itemStack);
                            actionResult.setAltered().decInventory().setSoundSource(itemStack);
                        }

                    } else if (playerEntity.isCrouching()) {
                        if (Configuration.isIlluminationEnabled() && BlockUtil.isIlluminator(itemStack)) {
                            if (!cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.ILLUMINATOR)) {
                                cbTileEntity.addAttribute(location, EnumAttributeType.ILLUMINATOR, itemStack);
                                actionResult.setAltered().decInventory().setSoundSource(itemStack);
                            }
                        } else if (Configuration.isOverlaysEnabled() && BlockUtil.isOverlay(itemStack)) {
                            if (!cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.OVERLAY)) {
                                cbTileEntity.addAttribute(location, EnumAttributeType.OVERLAY, itemStack);
                                actionResult.setAltered().decInventory().setSoundSource(itemStack);
                            }
                        } else if (Configuration.isDyeColorsEnabled() && BlockUtil.isDye(itemStack, false)) {
                            if (!cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.DYE)) {
                                cbTileEntity.addAttribute(location, EnumAttributeType.DYE, itemStack);
                                actionResult.setAltered().decInventory().setSoundSource(itemStack);
                            }
                        }
                    }
                }
            }
        }

        if (!actionResult.altered) {
            postOnBlockActivated(cbTileEntity, playerEntity, blockRayTraceResult, actionResult);
        } else {
            if (actionResult.itemStack == null) {
                actionResult.setSoundSource(BlockUtil.getCover(cbTileEntity, location));
            }
            damageItemWithChance(world, playerEntity, hand);
            //onNeighborChange(world, blockPos, blockPos);
            //world.notifyNeighborsOfStateChange(blockPos, this);
        }
        
        if (actionResult.altered) {
            cbTileEntity.update(true);
        	//cbTileEntity.markDirty();
            //world.notifyBlockUpdate(blockPos, blockState, blockState, 3);
        }

/*        if (actionResult.playSound) {
            BlockProperties.playBlockSound(world, actionResult.itemStack, blockPos, false);
        }*/

        if (actionResult.decInv) {
            EntityLivingUtil.decrementCurrentSlot(playerEntity);
        }

        return actionResult.altered ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }

    /**
     * Cycles through chisel patterns.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param facing the side being clicked
     * @param hand the player hand holding tool
     */
    public boolean onChiselClick(CbTileEntity cbTileEntity, EnumAttributeLocation location, Hand hand, boolean leftClick) {
    	String design = "";
    	if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.DESIGN_CHISEL)) {
    		design = (String) cbTileEntity.getAttributeHelper().getAttribute(location, EnumAttributeType.DESIGN_CHISEL).getModel();
    	}
        String designAdj = RenderConstants.EMPTY_STRING;

        // Attempt to set first design to adjacent
        if (StringUtils.isNullOrEmpty(design)) {
        	
            World world = cbTileEntity.getLevel();
            CbTileEntity[] cbTileEntity_list = getAdjacentTileEntities(world, cbTileEntity.getBlockPos());
            for (CbTileEntity cbTileEntity_current : cbTileEntity_list) {
                if (cbTileEntity_current != null) {
                    if (cbTileEntity_current.getAttributeHelper().hasAttribute(location, EnumAttributeType.DESIGN_CHISEL)) {
                        design = (String) cbTileEntity_current.getAttributeHelper().getAttribute(location, EnumAttributeType.DESIGN_CHISEL).getModel();
                        designAdj = design;
                        // TODO: Can refine this later to include side cover designs
                        break;
                    }
                }
            }
        }

        // Set next design
        if (StringUtils.isNullOrEmpty(designAdj) && Hand.MAIN_HAND.equals(hand)) {
            design = leftClick ? DesignHandler.getNext(DesignType.CHISEL, design) : DesignHandler.getPrev(DesignType.CHISEL, design);
        }

        if (!StringUtils.isNullOrEmpty(design)) {
        	cbTileEntity.removeAttribute(location, EnumAttributeType.DESIGN_CHISEL);
        	cbTileEntity.addAttribute(location, EnumAttributeType.DESIGN_CHISEL, design);
        }

        return true;
    }
    
    /**
     * Called when a neighbor block changes.
     * <p>
     * Will drop side covers that are obstructed.
     * 
     * @param blockState the block state
     * @param world the world
     * @param blockPos the block position
     * @param neighborBlockPos the neighbor block position
     */
    @Override
    public void onNeighborChange(BlockState blockState, IWorldReader world, BlockPos blockPos, BlockPos neighborBlockPos) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
        	for (Direction facing : Direction.values()) {
        		EnumAttributeLocation location = EnumAttributeLocation.valueOf(facing.ordinal());
        		if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                    if (!canSupportCover(cbTileEntity, location)) {
                        cbTileEntity.removeAttributes(location);
                        continue;
                    }
                    if (Block.canSupportCenter(world, blockPos.offset(facing.getNormal()), facing.getOpposite())) {
                        cbTileEntity.removeAttributes(location);
                    }
                }
        	}
        }
    }
    
    /**
     * Gets weak redstone power being emitted by block on specified side.
     * 
     * @param blockState the block state
     * @param blockReader the block reader
     * @param blockPos the block position
     * @param direction the side being checked
     * @return redstone power from 0 to 15
     */
    @Override
    public int getSignal(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, Direction direction) {
        CbTileEntity cbTileEntity = getTileEntity(blockReader, blockPos);
        int power = 0;
        Set<Direction> sides = new HashSet<Direction>();
        for (EnumAttributeLocation location : EnumAttributeLocation.values()) {
    		BlockState tempBlockState = BlockUtil.getAttributeBlockState(cbTileEntity, location, EnumAttributeType.COVER);
    		if (tempBlockState != null) {
	    		if (!EnumAttributeLocation.HOST.equals(location)) {
	    			for (Direction tempFacing : Direction.values()) {
	    				int tempPower = tempBlockState.getBlock().getSignal(tempBlockState, blockReader, blockPos, tempFacing);
	    	            power = Math.max(power, tempPower);
	    			}
	    		} else {
	    			int tempPower = tempBlockState.getBlock().getSignal(tempBlockState, blockReader, blockPos, direction);
		            power = Math.max(power, tempPower);
	    		}
    		}
        }
        return power;
    }
    
    /**
     * Gets redstone power being emitted on the specified side.
     * 
     * @param blockState the block state
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @param facing the opposite of side being checked
     * @return redstone power from 0 to 15
     */
    @Override
    public int getDirectSignal(BlockState blockState, IBlockReader blockAccess, BlockPos blockPos, Direction direction) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        int power = 0;
        Set<BlockState> blockStates = new HashSet<BlockState>();
        BlockState sideBlockState = BlockUtil.getAttributeBlockState(cbTileEntity, EnumAttributeLocation.valueOf(direction.getOpposite().ordinal()), EnumAttributeType.COVER);
        if (sideBlockState != null) {
        	blockStates.add(sideBlockState);
        }
        BlockState hostBlockState = BlockUtil.getAttributeBlockState(cbTileEntity, EnumAttributeLocation.HOST, EnumAttributeType.COVER);
        if (hostBlockState != null) {
        	blockStates.add(hostBlockState);
        }
        for (BlockState setBlockState : blockStates) {
            int tempPower = hostBlockState.getBlock().getDirectSignal(setBlockState, blockAccess, blockPos, direction);
            power = Math.max(power, tempPower);
        }
        return power;
    }


    /**
     * Spawn a digging particle effect in the world, this is a wrapper
     * around EffectRenderer.addBlockHitEffects to allow the block more
     * control over the particles. Useful when you have entirely different
     * texture sheets for different sides/locations in the world.
     *
     * @param world The current world
     * @param target The target the player is looking at {x/y/z/side/sub}
     * @param effectRenderer A reference to the current effect renderer.
     * @return True to prevent vanilla digging particles form spawning.
     */
    // TODO: This will have issues with cover blockstate
/*    @Override
    public boolean addHitEffects(BlockState blockState, World world, RayTraceResult rayTraceResult, ParticleManager particleManager) {
        CbTileEntity cbTileEntity = getTileEntity(world, rayTraceResult.getBlockPos());
        if (cbTileEntity != null) {
        	EnumAttributeLocation
            int effectiveSide = cbTileEntity.hasAttribute(cbTileEntity.ATTR_COVER[target.sideHit]) ? target.sideHit : 6;
            ItemStack itemStack = BlockProperties.getCover(cbTileEntity, effectiveSide);

            if (BlockProperties.hasAttribute(cbTileEntity, cbTileEntity.ATTR_OVERLAY[effectiveSide])) {
                Overlay overlay = OverlayHandler.getOverlayType(cbTileEntity.getAttribute(cbTileEntity.ATTR_OVERLAY[effectiveSide]));
                if (OverlayHandler.coversFullSide(overlay, target.sideHit)) {
                    itemStack = overlay.getItemStack();
                }
            }

            Block block = BlockProperties.toBlock(itemStack);

            double xOffset = target.blockX + world.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinX();
            double yOffset = target.blockY + world.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinY();
            double zOffset = target.blockZ + world.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - 0.1F * 2.0F) + 0.1F + block.getBlockBoundsMinZ();

            switch (target.sideHit) {
                case 0:
                    yOffset = target.blockY + block.getBlockBoundsMinY() - 0.1D;
                    break;
                case 1:
                    yOffset = target.blockY + block.getBlockBoundsMaxY() + 0.1D;
                    break;
                case 2:
                    zOffset = target.blockZ + block.getBlockBoundsMinZ() - 0.1D;
                    break;
                case 3:
                    zOffset = target.blockZ + block.getBlockBoundsMaxZ() + 0.1D;
                    break;
                case 4:
                    xOffset = target.blockX + block.getBlockBoundsMinX() - 0.1D;
                    break;
                case 5:
                    xOffset = target.blockX + block.getBlockBoundsMaxX() + 0.1D;
                    break;
            }

            ParticleHelper.addHitEffect(cbTileEntity, target, xOffset, yOffset, zOffset, itemStack, effectRenderer);

            return true;

        }

        return super.addHitEffects(world, target, effectRenderer);
    }*/
    
    /**
     * Renders block destruction effects.  This is controlled to prevent block
     * destroy effects if left-clicked with a Carpenter's Hammer while player
     * is in creative mode.
     * 
     * @param blockState the block state
     * @param world the world
     * @param blockPos the block position
     * @param particleManager the particle manager
     * @return <code>false</code> if effects are displayed
     */
    @Override
    public boolean addDestroyEffects(BlockState blockState, World world, BlockPos blockPos, ParticleManager particleManager) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
            PlayerEntity playerEntity = world.getNearestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 6.5F, false);
            if (playerEntity != null) {
                if (!suppressDestroyBlock(playerEntity)) {
                	// TODO: Work on this
                    //ParticleHelper.addDestroyEffect(world, x, y, z, BlockProperties.getCover(cbTileEntity, 6), effectRenderer);
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Returns block light value.
     * 
     * @param blockState the block state
     * @param blockReader the block reader
     * @param blockPos the block position
     * @return the light value as integer from 0 to 15
     */
    @Override
    public int getLightValue(BlockState blockState, IBlockReader blockReader, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getSimpleTileEntity(blockReader, blockPos);
        if (cbTileEntity != null) {
            return cbTileEntity.getLightValue();
        }
        return 0;
    }

/*    @Override
    *//**
     * Chance that fire will spread and consume this block.
     *//*
    public int getFlammability(IWorld blockAccess, int x, int y, int z, ForgeDirection side)
    {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, x, y, z);
        if (cbTileEntity != null) {
            ItemStack is = BlockProperties.getFeatureSensitiveSideItemStack(cbTileEntity, side);
            Block b = BlockProperties.toBlock(is);
            return b instanceof IWrappableBlock ? ((IWrappableBlock)b).getFlammability(blockAccess, x, y, z, side, b, is.getItemDamage()) : Blocks.fire.getFlammability(b);
        }
        return super.getFlammability(blockAccess, x, y, z, side);
    }

    @Override
    *//**
     * Called when fire is updating on a neighbor block.
     *//*
    public int getFireSpreadSpeed(IWorld blockAccess, int x, int y, int z, ForgeDirection side)
    {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, x, y, z);
        if (cbTileEntity != null) {
            ItemStack is = BlockProperties.getFeatureSensitiveSideItemStack(cbTileEntity, side);
            Block b = BlockProperties.toBlock(is);
            return b instanceof IWrappableBlock ? ((IWrappableBlock)b).getFireSpread(blockAccess, x, y, z, side, b, is.getItemDamage()) : Blocks.fire.getEncouragement(b);
        }

        return super.getFireSpreadSpeed(blockAccess, x, y, z, side);
    }

    @Override
    *//**
     * Currently only called by fire when it is on top of this block.
     * Returning true will prevent the fire from naturally dying during updating.
     * Also prevents fire from dying from rain.
     *//*
    public boolean isFireSource(World world, int x, int y, int z, ForgeDirection side)
    {
        CbTileEntity cbTileEntity = getTileEntity(world, x, y, z);
        if (cbTileEntity != null) {
            ItemStack is = BlockProperties.getFeatureSensitiveSideItemStack(cbTileEntity, side);
            Block b = BlockProperties.toBlock(is);
            if (b instanceof BlockCoverable) {
                return false;
            }
            return b instanceof IWrappableBlock ? ((IWrappableBlock)b).sustainsFire(world, x, y, z, side, b, is.getItemDamage()) : b.isFireSource(world, x, y, z, side);
        }        
        return false;
    }*/
    
    /**
     * Indicates whether block destruction should be suppressed when block is clicked.
     * 
     * @param playerEntity the player
     * @return <code>true</code> if block should not be destroyed
     */
    protected boolean suppressDestroyBlock(PlayerEntity playerEntity) {
        if (playerEntity == null) {
            return false;
        }
        ItemStack itemStack = playerEntity.getMainHandItem();
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            return playerEntity.isCreative() && item != null && (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel);
        }
        return false;
    }
    
    /**
     * Drops block as ItemStack and notifies relevant systems of
     * block removal.  Block attributes will drop later in destruction.
     * <p>
     * This is usually called when a {@link #onNeighborChange(IWorld, BlockPos, BlockPos) neighbor changes}.
     *
     * @param world the world
     * @param blockPos the block position
     * @param dropBlock whether block ItemStack is dropped
     */
    protected void destroyBlock(World world, BlockPos blockPos, boolean dropBlock) {
        //int metadata = dropBlock ? 0 : METADATA_DROP_ATTR_ONLY;
        //List<ItemStack> items = getDrops(world, blockPos, getDefaultState(), 1);
        //for (ItemStack item : items) {
        	// TODO: Work on block drops
            //dropBlockAsItem(world, x, y, z, item);
        //}
        world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 1);
    }
    
    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     * <p>
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param blockState the block state
     * @param world the world
     * @param blockPos the block position
     * @param playerEntity the player
     * @param willHarvest <code>true</code> if {@link Block#harvestBlock(World, PlayerEntity, BlockPos, BlockState, TileEntity, ItemStack) Block.harvestBlock}
     * 		  will be called after this.  Can be useful to delay the destruction of tile entities until after harvestBlock
     * @return <code>true</code> if the block is actually destroyed
     */
    @Override
    public boolean removedByPlayer(BlockState blockState, World world, BlockPos blockPos, PlayerEntity playerEntity, boolean willHarvest, FluidState fluidState) {
        if (world.isClientSide()) {
            return super.removedByPlayer(blockState, world, blockPos, playerEntity, willHarvest, fluidState);
        }

        // Grab drops while tile entity exists (before calling super)
        //int metadata = playerEntity != null && playerEntity.capabilities.isCreativeMode ? METADATA_DROP_ATTR_ONLY : 0;
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
	        if (cbTileEntity != null) {
	        List<ItemStack> itemStacks = cbTileEntity.getAllDroppableAttributes();
	
	        // Drop attributes if block destroyed, and no Carpenter's Tool is held by entity
	        if (!suppressDestroyBlock(playerEntity) && super.removedByPlayer(blockState, world, blockPos, playerEntity, willHarvest, fluidState)) {
	            for (ItemStack itemStack : itemStacks) {
	            	popResource(world, blockPos, itemStack);
	            }
	            return true;
	        }
        }

        return false;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display.
     * 
     * @param world the world
     */
    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState blockState, World world, BlockPos blockPos, Random rand) {
        super.animateTick(blockState, world, blockPos, rand);
    	CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER)) {
            	Block block = BlockUtil.toBlock(BlockUtil.getCover(cbTileEntity, EnumAttributeLocation.HOST));
            	BlockState coverBlockState = BlockUtil.getAttributeBlockState(cbTileEntity, EnumAttributeLocation.HOST, EnumAttributeType.COVER);
                block.animateTick(coverBlockState, world, blockPos, rand);
            }
            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.OVERLAY)) {
            	ItemStack itemStack = (ItemStack) cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.OVERLAY).getModel();
            	if (Overlay.mycelium.equals(OverlayHandler.getOverlayType(itemStack))) {
                	Blocks.MYCELIUM.animateTick(Blocks.MYCELIUM.defaultBlockState(), world, blockPos, rand);
                }
            }
        }
    }
    
    /**
     * Determines if this block can support the passed in plant on facing side, allowing it to be planted and grow.
     *
     * @param blockState the block state
     * @param blockReader the block reader
     * @param blockPos the block position
     * @param direction the side to check
     * @param plantable the plant to support
     * @return <code>true</code> if block can sustain plant
     */
    @Override
    public boolean canSustainPlant(BlockState blockState, IBlockReader blockReader, BlockPos blockPos, Direction direction, IPlantable plantable) {
        CbTileEntity cbTileEntity = getTileEntity(blockReader, blockPos);
        if (cbTileEntity != null) {
            if (!Block.canSupportCenter(cbTileEntity.getLevel(), blockPos, direction)) {
                return false;
            }
            // Add representative attribute blocks
            Set<Block> blocks = new HashSet<Block>();
            EnumAttributeLocation location = BlockUtil.getAttributeLocationForFacing(cbTileEntity, direction);
            if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
            	BlockState sideBlockState = BlockUtil.getAttributeBlockState(cbTileEntity, location, EnumAttributeType.COVER);
            	Block block = sideBlockState.getBlock();
            	blocks.add(sideBlockState.getBlock());
            	Material material = sideBlockState.getMaterial();
            	if (Blocks.GRASS_BLOCK.equals(block)) {
           	 		blocks.add(Blocks.GRASS_BLOCK);
           	 	} else if (Material.DIRT.equals(material)) {
                	blocks.add(Blocks.DIRT);
                } else if (Material.SAND.equals(material)) {
                	blocks.add(Blocks.SAND);
                }
            }
            if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.OVERLAY)) {
            	ItemStack itemStack = (ItemStack) cbTileEntity.getAttributeHelper().getAttribute(location, EnumAttributeType.OVERLAY).getModel();
            	blocks.add(BlockUtil.toBlock(OverlayHandler.getOverlayType(itemStack).getItemStack()));
            }
            PlantType plantType = plantable.getPlantType(blockReader, blockPos.above());
            if (PlantType.BEACH.equals(plantType)) {
            	boolean isBeach = false;
            	boolean hasWater = false;
            	for (Block block : blocks) {
            		BlockState coverBlockState = block.defaultBlockState();
	            	isBeach |= coverBlockState.is(Blocks.GRASS_BLOCK)
	            			|| net.minecraftforge.common.Tags.Blocks.DIRT.contains(this)
	            			|| coverBlockState.is(Blocks.SAND)
	            			|| coverBlockState.is(Blocks.RED_SAND);
	                for (Direction face : Direction.Plane.HORIZONTAL) {
	                    BlockState tempBlockState = blockReader.getBlockState(blockPos.relative(face));
	                    net.minecraft.fluid.FluidState fluidState = blockReader.getFluidState(blockPos.relative(face));
	                    hasWater |= tempBlockState.is(Blocks.FROSTED_ICE);
	                    hasWater |= fluidState.is(net.minecraft.tags.FluidTags.WATER);
	                    if (hasWater) {
	                    	break;
	                    }
	                }
            	}
                return isBeach && hasWater;
            } else if (PlantType.CAVE.equals(plantType)) {
            	return blocks.contains(Blocks.STONE);
            } else if (PlantType.DESERT.equals(plantType)) {
            	return blocks.contains(Blocks.SAND);
            } else if (PlantType.NETHER.equals(plantType)) {
            	return blocks.contains(Blocks.SOUL_SAND);
            } else if (PlantType.PLAINS.equals(plantType)) {
            	return blocks.contains(Blocks.GRASS) || blocks.contains(Blocks.DIRT);
            }
        }
        return super.canSustainPlant(blockState, blockReader, blockPos, direction, plantable);
    }

    /**
     * Gets whether this block is considered solid.
     * 
     * @param world the world
     * @param blockPos the block position
     * @return <code>true</code> if block is considered solid
     */
    protected boolean isBlockSolid(IWorld world, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        //if (cbTileEntity != null) {
        //    return !cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER)
        //    		|| BlockUtil.getAttributeBlockState(cbTileEntity, EnumAttributeLocation.HOST, EnumAttributeType.COVER).isCollisionShapeFullBlock(world, blockPos);
        //} else {
            return false;
        //}
    }
    
    /**
     * Called when the block is placed in the world.
     * 
     * @param world the world
     * @param blockPos the block position
     * @param blockState the block state
     * @param livingEntity the living entity
     * @param itemStack the entity held ItemStack
     */
    @Override
    public void setPlacedBy(World world, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
    	if (!world.isClientSide()) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                cbTileEntity.setOwner(new ProtectedObject((PlayerEntity)livingEntity));
            }
        }
    }
    
    /**
     * Adjust block slipperiness according to cover or side cover.
     * 
     * @param blockState the block state
     * @param worldReader the world reader
     * @param blockPos the block position
     * @param entity the entity
     * @return the slipperiness
     */
    @Override
    public float getSlipperiness(BlockState blockState, IWorldReader worldReader, BlockPos blockPos, Entity entity) {
    	CbTileEntity cbTileEntity = EntityLivingUtil.getTileEntityAtFeet(entity);
        if (cbTileEntity != null) {
            ItemStack itemStack = BlockUtil.getFeatureSensitiveSideItemStack(cbTileEntity, Direction.UP);
            Block block = BlockUtil.toBlock(itemStack);
            if (!(block instanceof AbstractCoverableBlock)) {
            	return BlockUtil.toBlock(itemStack).getSlipperiness(blockState, worldReader, blockPos, entity);
            }
        }
    	return super.getSlipperiness(blockState, worldReader, blockPos, entity);
    }
    
    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * PlayerEntity.
     * 
     * @param blockState the block state
     * @param playerEntity the player entity
     * @param blockReader the block reader
     * @param blockPos the block position
     * @return the relative block hardness
     */
    @Override
    public float getDestroyProgress(BlockState blockState, PlayerEntity playerEntity, IBlockReader blockReader, BlockPos blockPos) {
    	ItemStack itemStack = playerEntity.getMainHandItem();
    	
    	// Carpenter's tools should not damage block
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            if (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel) {
                return -1;
            }
        }
        
        // Return cover block hardness if one is present
        CbTileEntity cbTileEntity = getTileEntity(blockReader, blockPos);
        if (cbTileEntity != null && cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER)) {
        	BlockState outBlockState = BlockUtil.getAttributeBlockState(cbTileEntity, EnumAttributeLocation.HOST, EnumAttributeType.COVER);
        	return outBlockState.getDestroyProgress(playerEntity, blockReader, blockPos);
        } else {
            return super.getDestroyProgress(blockState, playerEntity, blockReader, blockPos);
        }
    }
    
    // Below method is likely inherited from base block state
/*    @Override
    *//**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * PlayerEntity.
     *//*
    public float getPlayerRelativeBlockHardness(PlayerEntity playerEntity, World world, int x, int y, int z) {
         Don't damage block if holding Carpenter's tool. 

        ItemStack itemStack = playerEntity.getHeldItem();

        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel) {
                return -1;
            }
        }

         Return block hardness of cover. 

        CbTileEntity cbTileEntity = getTileEntity(world, x, y, z);

        if (cbTileEntity != null) {
            return ForgeHooks.blockStrength(BlockProperties.toBlock(BlockProperties.getCover(cbTileEntity, 6)), playerEntity, world, x, y, z);
        } else {
            return super.getPlayerRelativeBlockHardness(playerEntity, world, x, y, z);
        }
    }*/
    
    /**
     * Damages item with chance to deal damage.
     * <p>
     * Used to allow Carpenter's Tools to take damage
     * at a specified rate and chance.
     * 
     * @param world the world
     * @param playerEntity the player
     */
    protected void damageItemWithChance(World world, PlayerEntity playerEntity, Hand hand) {
        Item item = playerEntity.getItemInHand(hand).getItem();
        if (item instanceof ICarpentersHammer) {
            ((ICarpentersHammer)item).onHammerUse(world, playerEntity, hand);
        } else if (item instanceof ICarpentersChisel) {
            ((ICarpentersChisel)item).onChiselUse(world, playerEntity, hand);
        }
    }
    
    /**
     * Called before click event alters block features such
     * as cover, overlay, or dye color.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param playerEntity the player
     * @param actionResult stores action parameters
     */
    protected void preOnBlockClicked(CbTileEntity cbTileEntity, PlayerEntity playerEntity, ActionResult actionResult) {}

    /**
     * Called before activation event alters block features
     * such as cover, overlay, or dye color.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param playerEntity the player
     * @param blockRayTraceResult the block ray trace result
     * @param actionResult stores action parameters
     */
    protected void preOnBlockActivated(CbTileEntity cbTileEntity, PlayerEntity playerEntity, BlockRayTraceResult blockRayTraceResult, ActionResult actionResult) {}

    /**
     * Called after attribute changes processed as long as
     * no changes occurred.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param playerEntity the player
     * @param blockRayTraceResult the block ray trace result
     * @param actionResult stores action parameters
     */
    protected void postOnBlockActivated(CbTileEntity cbTileEntity, PlayerEntity playerEntity, BlockRayTraceResult blockRayTraceResult, ActionResult actionResult) {}

    /**
     * Handles hammer left click interactions.
     * <p>
     * Override this for block-specific functionality.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param playerEntity the player
     * @return <code>true</code> if tile entity changed
     */
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
        return false;
    }

    /**
     * Handles hammer right click interactions.
     * <p>
     * Override this for block-specific functionality.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param playerEntity the player
     * @return <code>true</code> if tile entity changed
     */
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
        return false;
    }

    /**
     * Gets whether block location supports a cover.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param location the block attribute location
     */
    protected boolean canSupportCover(CbTileEntity cbTileEntity, EnumAttributeLocation location) {
        return true;
    }

    /**
     * Allows a tile entity called during block activation to be changed before
     * altering attributes like cover, dye, overlay, etc.
     * <p>
     * Primarily offered for the garage door, when open, to swap the top piece
     * with the bottom piece for consistency.
     *
     * @param cbTileEntity the Carpenter's Block tile entity
     * @return a swapped in CbTileEntity, or the passed in CbTileEntity
     */
    protected CbTileEntity getTileEntityForBlockActivation(CbTileEntity cbTileEntity) {
        return cbTileEntity;
    }
    
    /**
     * Gets whether block has tile entity for given block state.
     * 
     * @param blockState the block state
     * @return <code>true</code> if has a tile entity
     */
    @Override
    public boolean hasTileEntity(BlockState blockState) {
        return true;
    }

    /**
     * Creates a tile entity.
     *
     * @param blockState the block state
     * @param blockReader the block reader
     * @return a new tile entity
     */
    @Nullable
    public TileEntity createTileEntity(BlockState state, IBlockReader blockReader) {
    	return new CbTileEntity();
    }
    
}
