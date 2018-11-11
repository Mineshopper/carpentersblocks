package com.carpentersblocks.block;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.carpentersblocks.api.ICarpentersChisel;
import com.carpentersblocks.api.ICarpentersHammer;
import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.IConstants;
import com.carpentersblocks.util.attribute.AbstractAttribute.Key;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.attribute.EnumAttributeType;
import com.carpentersblocks.util.block.BlockUtil;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DesignHandler.DesignType;
import com.carpentersblocks.util.handler.EventHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.protection.PlayerPermissions;
import com.carpentersblocks.util.protection.ProtectedObject;
import com.carpentersblocks.util.registry.ConfigRegistry;
import com.carpentersblocks.util.states.factory.StateFactory;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockCoverable extends Block {
	
    /** Block drop event for dropping attribute. */
    public static int EVENT_ID_DROP_ATTR = 0x40000000;

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
    public BlockCoverable(Material material) {
        super(material);
    }

    /**
     * For South-sided blocks, rotates and sets the block bounds using
     * the provided ForgeDirection.
     *
     * @param  dir the rotated {@link ForgeDirection}
     */
    protected void setBlockBounds(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, EnumFacing facing)
    {
/*        switch (dir) {
            case DOWN:
                setBlockBounds(minX, 1.0F - maxZ, minY, maxX, 1.0F - minZ, maxY);
                break;
            case UP:
                setBlockBounds(minX, minZ, minY, maxX, maxZ, maxY);
                break;
            case NORTH:
                setBlockBounds(1.0F - maxX, minY, 1.0F - maxZ, 1.0F - minX, maxY, 1.0F - minZ);
                break;
            case EAST:
                setBlockBounds(minZ, minY, 1.0F - maxX, maxZ, maxY, 1.0F - minX);
                break;
            case WEST:
                setBlockBounds(1.0F - maxZ, minY, minX, 1.0F - minZ, maxY, maxX);
                break;
            default:
                setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
                break;
        }*/
    }
    
    /**
     * Gets global bounding box for block.
     * 
     * @param blockState the block state
     * @param blockAccess the IBlockAccess implementor
     * @param blockPos the block position
     */
    protected AxisAlignedBB getGlobalBoundingBox(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
        return super.getBoundingBox(blockState, blockAccess, blockPos).offset(blockPos);
    }
    
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     */
    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World world, BlockPos blockPos, Vec3d start, Vec3d end) {
        if (this instanceof IStateImplementor) {
	    	List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();
	        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
	        if (cbTileEntity != null) {
	        	for (AxisAlignedBB aabb : StateFactory.getState(cbTileEntity).getAxisAlignedBBs()) {
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
        } else {
        	return super.collisionRayTrace(blockState, world, blockPos, start, end);
        }
    }

    /**
     * Called when block event is received.
     *<p>
     * For the context of this mod, this is used for dropping block attributes
     * like covers, overlays, dyes, or any other ItemStack.
     *<p>
     * In order for external classes to call the protected method
     * {@link Block#dropBlockAsItem(World,int,int,int,ItemStack) dropBlockAsItem},
     * they create a block event with parameters itemId and metadata, allowing
     * the {@link ItemStack} to be recreated and dropped.
     *
     * @param world the world
     * @param blockPos the block position
     * @param id the Item Id
     * @param param the ItemStack damage value
     * @return <code>true</code> if event was handled
     */
    @Override
    public boolean eventReceived(IBlockState blockState, World world, BlockPos blockPos, int id, int param) {
    	if (id == EVENT_ID_DROP_ATTR) {
            CbTileEntity cbTileEntity = getSimpleTileEntity(world, blockPos);
            if (cbTileEntity != null) {
            	Key key = new Key(param);
            	if (cbTileEntity.getAttributeHelper().hasAttribute(key)) {
	                ItemStack itemStack = cbTileEntity.getDroppedItemStack(key);
	                if (cbTileEntity.onAttrDropped(key)) {
	                	spawnAsEntity(world, blockPos, itemStack);
	                	return true;
	                } else {
	                	return false;
	                }
            	}
            }
        }
        return super.eventReceived(blockState, world, blockPos, id, param);
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack getItemDrop(World world, int metadata) {
        return new ItemStack(getItemDropped(getDefaultState(), world.rand, 0), 1, metadata);
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
            getSimpleTileEntity(world, blockPos.add( 0, -1,  0)),
            getSimpleTileEntity(world, blockPos.add( 0,  1,  0)),
            getSimpleTileEntity(world, blockPos.add( 0,  0, -1)),
            getSimpleTileEntity(world, blockPos.add( 0,  0,  1)),
            getSimpleTileEntity(world, blockPos.add(-1,  0,  0)),
            getSimpleTileEntity(world, blockPos.add( 1,  0,  0))
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
    protected CbTileEntity getSimpleTileEntity(IBlockAccess blockAccess, BlockPos blockPos)
    {
        TileEntity tileEntity = blockAccess.getTileEntity(blockPos);
        return (tileEntity instanceof CbTileEntity) ? (CbTileEntity) tileEntity : null;
    }

    /**
     * Returns tile entity if block tile entity is instanceof CbTileEntity and
     * also belongs to this block type.
     */
    protected CbTileEntity getTileEntity(IBlockAccess blockAccess, BlockPos blockPos)
    {
        CbTileEntity cbTileEntity = getSimpleTileEntity(blockAccess, blockPos);
        return cbTileEntity != null && blockAccess.getBlockState(blockPos).getBlock().equals(this) ? cbTileEntity : null;
    }

    /**
     * Returns whether player is allowed to activate this block.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param entityPlayer the player
     * @return <code>true</code> if player can activate block
     */
    protected boolean canPlayerActivate(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
        return true;
    }
    
    /**
     * Called when block is clicked by a player.
     * 
     * @param world the world
     * @param blockPos the block position
     * @param entityPlayer the player
     */
    @Override
    public void onBlockClicked(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        if (world.isRemote) {
            return;
        }

        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity == null || !PlayerPermissions.hasElevatedPermission(cbTileEntity, entityPlayer, false)) {
            return;
        }

        ItemStack itemStack = entityPlayer.getHeldItem(EventHandler.eventHand);
        if (itemStack.isEmpty()) {
            return;
        }

        ActionResult actionResult = new ActionResult();
        EnumAttributeLocation location = BlockUtil.getAttributeLocationForFacing(cbTileEntity, EventHandler.eventFace);
        Item item = itemStack.getItem();
        if (item instanceof ICarpentersHammer && ((ICarpentersHammer)item).canUseHammer(world, entityPlayer, EnumHand.MAIN_HAND)) {
            preOnBlockClicked(cbTileEntity, entityPlayer, actionResult);
            if (!actionResult.altered) {
                if (entityPlayer.isSneaking()) {
                    dropLastAddedAttribute(cbTileEntity, location);
                    actionResult.setAltered();
                } else {
                    if (onHammerLeftClick(cbTileEntity, entityPlayer)) {
                    	actionResult.setAltered();
                    }
                }
                actionResult.setAltered();
            } else {
                //onNeighborChange(world, blockPos, blockPos);
                //world.notifyNeighborsOfStateChange(blockPos, this);
            }
        } else if (item instanceof ICarpentersChisel && ((ICarpentersChisel)item).canUseChisel(world, entityPlayer, EnumHand.MAIN_HAND)) {
            if (entityPlayer.isSneaking() && cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.DESIGN_CHISEL)) {
                cbTileEntity.removeAttribute(location, EnumAttributeType.DESIGN_CHISEL);
            } else if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                onChiselClick(cbTileEntity, location, EventHandler.eventHand, true);
            }            
        }
        
        if (actionResult.altered) {
            cbTileEntity.markDirty();
            IBlockState blockState = world.getBlockState(blockPos);
            world.notifyBlockUpdate(blockPos, blockState, blockState, 3);
        }
        
    }

    /**
     * Pops attribute from attribute location.
     *
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param location the location to pop attribute from
     */
    private void dropLastAddedAttribute(CbTileEntity cbTileEntity, EnumAttributeLocation location) {
    	cbTileEntity.removeLastAddedDroppableAttribute(location);
    	
/*        if (cbTileEntity.hasAttribute(cbTileEntity.ATTR_ILLUMINATOR)) {
            cbTileEntity.createBlockDropEvent(cbTileEntity.ATTR_ILLUMINATOR);
        } else if (cbTileEntity.hasAttribute(cbTileEntity.ATTR_OVERLAY[side])) {
            cbTileEntity.createBlockDropEvent(cbTileEntity.ATTR_OVERLAY[side]);
        } else if (cbTileEntity.hasAttribute(cbTileEntity.ATTR_DYE[side])) {
            cbTileEntity.createBlockDropEvent(cbTileEntity.ATTR_DYE[side]);
        } else if (cbTileEntity.hasAttribute(cbTileEntity.ATTR_COVER[side])) {
            cbTileEntity.removeChiselDesign(side);
            cbTileEntity.createBlockDropEvent(cbTileEntity.ATTR_COVER[side]);
        }*/
    }

    /**
     * Called when block is activated.
     * 
     * @param world the world
     * @param blockPos the block position
     * @param blockState the block state
     * @param entityPlayer the player
     * @param hand the active player hand
     * @param itemStack the active player ItemStack
     * @param facing the facing
     * @param hitX the x hit coordinate
     * @param hitY the y hit coordinate
     * @param hitZ the z hit coordinate
     * return <code>true</code> if block property changed
     */
    @Override
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState blockState, EntityPlayer entityPlayer, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);

        if (cbTileEntity == null) {
            return false;
        }

        if (!canPlayerActivate(cbTileEntity, entityPlayer)) {
            return false;
        }

        EnumAttributeLocation location = BlockUtil.getAttributeLocationForFacing(cbTileEntity, facing);
        
        // Allow block to change cbTileEntity if needed before altering attributes
        cbTileEntity = getTileEntityForBlockActivation(cbTileEntity);
        ActionResult actionResult = new ActionResult();

        preOnBlockActivated(cbTileEntity, entityPlayer, facing, hitX, hitY, hitZ, actionResult);

        // If no prior event occurred, try regular activation
        if (!actionResult.altered) {
            if (PlayerPermissions.hasElevatedPermission(cbTileEntity, entityPlayer, false)) {
                if (!entityPlayer.getHeldItem(hand).isEmpty()) {
                	ItemStack itemStack = entityPlayer.getHeldItem(hand);
                	if (itemStack.getItem() instanceof ICarpentersHammer && ((ICarpentersHammer)itemStack.getItem()).canUseHammer(world, entityPlayer, hand)) {
                        if (onHammerRightClick(cbTileEntity, entityPlayer)) {
                            actionResult.setAltered();
                        }
                	} else if (ConfigRegistry.enableChisel && itemStack.getItem() instanceof ICarpentersChisel && ((ICarpentersChisel)itemStack.getItem()).canUseChisel(world, entityPlayer, hand)) {
                        if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                            if (onChiselClick(cbTileEntity, location, hand, false)) {
                                actionResult.setAltered();
                            }
                        }
                    } else if (ConfigRegistry.enableCovers && BlockUtil.isCover(itemStack)) {

                        Block block = BlockUtil.toBlock(itemStack);

                        /* Will handle blocks that save directions using only y axis (pumpkin) */
                        //int metadata = block instanceof BlockDirectional ? MathHelper.floor_double(entityPlayer.rotationYaw * 4.0F / 360.0F + 2.5D) & 3 : itemStack.getItemDamage();

                        /* Will handle blocks that save directions using all axes (logs, quartz) */
                        // TODO: Handle later
                        /*if (BlockProperties.blockRotates(itemStack)) {
                            int rot = Direction.rotateOpposite[EntityLivingUtil.getRotationValue(entityPlayer)];
                            int side_interpolated = entityPlayer.rotationPitch < -45.0F ? 0 : entityPlayer.rotationPitch > 45 ? 1 : rot == 0 ? 3 : rot == 1 ? 4 : rot == 2 ? 2 : 5;
                            metadata = block.onBlockPlaced(world, cbTileEntity.xCoord, cbTileEntity.yCoord, cbTileEntity.zCoord, side_interpolated, hitX, hitY, hitZ, metadata);
                        }*/

                        ItemStack tempStack = itemStack.copy();
                        //tempStack.setItemDamage(metadata);

                        if (EnumAttributeLocation.HOST.equals(location) && (!canSupportCover(cbTileEntity, location) || cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER))) {
                            location = EnumAttributeLocation.valueOf(facing.ordinal());
                        }
                        
                        if (canSupportCover(cbTileEntity, location) && !cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                            cbTileEntity.addAttribute(location, EnumAttributeType.COVER, tempStack);
                            actionResult.setAltered().decInventory().setSoundSource(itemStack);
                        }

                    } else if (entityPlayer.isSneaking()) {
                        if (ConfigRegistry.enableIllumination && BlockUtil.isIlluminator(itemStack)) {
                            if (!cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.ILLUMINATOR)) {
                                cbTileEntity.addAttribute(location, EnumAttributeType.ILLUMINATOR, itemStack);
                                actionResult.setAltered().decInventory().setSoundSource(itemStack);
                            }
                        } else if (ConfigRegistry.enableOverlays && BlockUtil.isOverlay(itemStack)) {
                            if (!cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.OVERLAY)) {
                                cbTileEntity.addAttribute(location, EnumAttributeType.OVERLAY, itemStack);
                                actionResult.setAltered().decInventory().setSoundSource(itemStack);
                            }
                        } else if (ConfigRegistry.enableDyeColors && BlockUtil.isDye(itemStack, false)) {
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
            postOnBlockActivated(cbTileEntity, entityPlayer, facing, hitX, hitY, hitZ, actionResult);
        } else {
            if (actionResult.itemStack == null) {
                //actionResult.setSoundSource(BlockProperties.getCover(cbTileEntity, location));
            }
            damageItemWithChance(world, entityPlayer, hand);
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
            EntityLivingUtil.decrementCurrentSlot(entityPlayer);
        }

        return actionResult.altered;
    }

    /**
     * Cycles through chisel patterns.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param facing the side being clicked
     * @param hand the player hand holding tool
     */
    public boolean onChiselClick(CbTileEntity cbTileEntity, EnumAttributeLocation location, EnumHand hand, boolean leftClick) {
    	String design = "";
    	if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.DESIGN_CHISEL)) {
    		design = (String) cbTileEntity.getAttributeHelper().getAttribute(location, EnumAttributeType.DESIGN_CHISEL).getModel();
    	}
        String designAdj = IConstants.EMPTY_STRING;

        // Attempt to set first design to adjacent
        if (StringUtils.isNullOrEmpty(design)) {
        	
            World world = cbTileEntity.getWorld();
            CbTileEntity[] cbTileEntity_list = getAdjacentTileEntities(world, cbTileEntity.getPos());
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
        if (StringUtils.isNullOrEmpty(designAdj) && EnumHand.MAIN_HAND.equals(hand)) {
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
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @param neighborBlockPos the neighbor block position
     */
    @Override
    public void onNeighborChange(IBlockAccess blockAccess, BlockPos blockPos, BlockPos neighborBlockPos) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
        	for (EnumFacing facing : EnumFacing.values()) {
        		EnumAttributeLocation location = EnumAttributeLocation.valueOf(facing.ordinal());
        		if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
                    if (!canSupportCover(cbTileEntity, location)) {
                        cbTileEntity.removeAttributes(location);
                        continue;
                    }
                    if (blockAccess.isSideSolid(blockPos.offset(facing), facing.getOpposite(), false) && isSideSolid(getDefaultState(), blockAccess, blockPos.offset(facing), facing.getOpposite())) {
                        cbTileEntity.removeAttributes(location);
                    }
                }
        	}
        }
    }

    /**
     * Gets weak redstone power being emitted by block on specified side.
     * <p>
     * If {@link #isBlockNormalCube(IBlockState) isBlockNormalCube} returns <code>true</code>,
     * standard redstone propagation rules will apply instead and this will not be called.
     * 
     * @param blockState the block state
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @param facing the opposite of side being checked
     * @return redstone power from 0 to 15
     */
    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        int power = 0;

        Set<EnumFacing> sides = new HashSet<EnumFacing>();
        for (EnumAttributeLocation location : EnumAttributeLocation.values()) {
    		IBlockState tempBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), location, EnumAttributeType.COVER);
    		if (tempBlockState != null) {
	    		if (!EnumAttributeLocation.HOST.equals(location)) {
	    			for (EnumFacing tempFacing : EnumFacing.values()) {
	    				int tempPower = tempBlockState.getBlock().getWeakPower(tempBlockState, blockAccess, blockPos, tempFacing);
	    	            power = Math.max(power, tempPower);
	    			}
	    		} else {
	    			int tempPower = tempBlockState.getBlock().getWeakPower(tempBlockState, blockAccess, blockPos, facing);
		            power = Math.max(power, tempPower);
	    		}
    		}
        }
        
        return power;
    }
    
    /**
     * Gets redstone power being emitted on the specified side.
     * <p>
     * Note that the side is reversed.
     * 
     * @param blockState the block state
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @param facing the opposite of side being checked
     * @return redstone power from 0 to 15
     */
    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        int power = 0;
        
        Set<IBlockState> blockStates = new HashSet<IBlockState>();
        IBlockState sideBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), EnumAttributeLocation.valueOf(facing.getOpposite().ordinal()), EnumAttributeType.COVER);
        if (sideBlockState != null) {
        	blockStates.add(sideBlockState);
        }
        IBlockState hostBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), EnumAttributeLocation.HOST, EnumAttributeType.COVER);
        if (hostBlockState != null) {
        	blockStates.add(hostBlockState);
        }
        
        for (IBlockState setBlockState : blockStates) {
            int tempPower = hostBlockState.getBlock().getStrongPower(setBlockState, blockAccess, blockPos, facing);
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
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState blockState, World world, RayTraceResult rayTraceResult, ParticleManager particleManager) {
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
     * @param world the world
     * @param blockPos the block position
     * @param particleManager the particle manager
     * @return <code>false</code> if effects are displayed
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, BlockPos blockPos, ParticleManager particleManager) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
            EntityPlayer entityPlayer = world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 6.5F, false);
            if (entityPlayer != null) {
                if (!suppressDestroyBlock(entityPlayer)) {
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
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @return the light value as integer from 0 to 15
     */
    @Override
    public int getLightValue(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getSimpleTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
            return cbTileEntity.getLightValue();
        }
        return 0;
    }

/*    @Override
    *//**
     * Chance that fire will spread and consume this block.
     *//*
    public int getFlammability(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
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
    public int getFireSpreadSpeed(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side)
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
     * Called when player destroys a block with an item that can harvest it.
     * 
     * @param world the world
     * @param entityPlayer the player
     * @param blockPos the block position
     * @param blockState the block state
     * @param tileEntity the tile entity
     * @param itemStack player held ItemStack
     */
    @Override
    public void harvestBlock(World world, EntityPlayer entityPlayer, BlockPos blockPos, IBlockState blockState, TileEntity tileEntity, ItemStack itemStack) {};

    /**
     * Indicates whether block destruction should be suppressed when block is clicked.
     * 
     * @param entityPlayer the player
     * @return <code>true</code> if block should not be destroyed
     */
    protected boolean suppressDestroyBlock(EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            return false;
        }
        ItemStack itemStack = entityPlayer.getHeldItemMainhand();
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            return entityPlayer.capabilities.isCreativeMode && item != null && (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel);
        }
        return false;
    }

    /**
     * Drops block as ItemStack and notifies relevant systems of
     * block removal.  Block attributes will drop later in destruction.
     * <p>
     * This is usually called when a {@link #onNeighborChange(IBlockAccess, BlockPos, BlockPos) neighbor changes}.
     *
     * @param world the world
     * @param blockPos the block position
     * @param dropBlock whether block ItemStack is dropped
     */
    protected void destroyBlock(World world, BlockPos blockPos, boolean dropBlock) {
        //int metadata = dropBlock ? 0 : METADATA_DROP_ATTR_ONLY;
        List<ItemStack> items = getDrops(world, blockPos, getDefaultState(), 1);
        for (ItemStack item : items) {
        	// TODO: Work on block drops
            //dropBlockAsItem(world, x, y, z, item);
        }
        world.setBlockToAir(blockPos);
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
     * @param entityPlayer the player
     * @param willHarvest <code>true</code> if {@link Block#harvestBlock(World, EntityPlayer, BlockPos, IBlockState, TileEntity, ItemStack) Block.harvestBlock}
     * 		  will be called after this.  Can be useful to delay the destruction of tile entities until after harvestBlock
     * @return <code>true</code> if the block is actually destroyed
     */
    @Override
    public boolean removedByPlayer(IBlockState blockState, World world, BlockPos blockPos, EntityPlayer entityPlayer, boolean willHarvest) {
        if (world.isRemote) {
            return super.removedByPlayer(blockState, world, blockPos, entityPlayer, willHarvest);
        }

        // Grab drops while tile entity exists (before calling super)
        //int metadata = entityPlayer != null && entityPlayer.capabilities.isCreativeMode ? METADATA_DROP_ATTR_ONLY : 0;
        List<ItemStack> itemStacks = getDrops(world, blockPos, blockState, 1);

        // Drop attributes if block destroyed, and no Carpenter's Tool is held by entity
        if (!suppressDestroyBlock(entityPlayer) && super.removedByPlayer(blockState, world, blockPos, entityPlayer, willHarvest)) {
            for (ItemStack itemStack : itemStacks) {
            	this.spawnAsEntity(world, blockPos, itemStack);
            }
            return true;
        }

        return false;
    }

    /**
     * Gets a complete list of items dropped from this block.
     *
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @param blockState the block state
     * @param fortune breakers fortune level
     * @return a list of ItemStacks to drop
     */
    @Override
    public List<ItemStack> getDrops(IBlockAccess blockAccess, BlockPos blockPos, IBlockState blockState, int fortune) {
        List<ItemStack> list = super.getDrops(blockAccess, blockPos, blockState, fortune); // Add block item drop

        // TODO: Adapt to blockstate propery
/*        if (metadata == METADATA_DROP_ATTR_ONLY) {
        	list.clear(); // Remove block instance from drop list 
        }*/

        CbTileEntity cbTileEntity = getSimpleTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
        	list.addAll(cbTileEntity.getAllDroppableAttributes());
        }

        return list;
    }


    /**
     * A randomly called display update to be able to add particles or other items for display.
     * 
     * @param world the world
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random rand) {
    	// TODO: this may have issues with cover block states, look into calling all but HOST for this
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
/*            if (cbTileEntity.hasAttribute(cbTileEntity.ATTR_COVER[6])) {
                BlockProperties.toBlock(BlockProperties.getCover(cbTileEntity, 6)).randomDisplayTick(world, x, y, z, random);
            }
            if (cbTileEntity.hasAttribute(cbTileEntity.ATTR_OVERLAY[6])) {
                if (OverlayHandler.getOverlayType(cbTileEntity.getAttribute(cbTileEntity.ATTR_OVERLAY[6])).equals(Overlay.MYCELIUM)) {
                    Blocks.mycelium.randomDisplayTick(world, x, y, z, random);
                }
            }*/
        }
    }

    /**
     * Determines if this block can support the passed in plant on facing side, allowing it to be planted and grow.
     *
     * @param blockState the block state
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @param facing the side to check
     * @param plantable the plant to support
     * @return <code>true</code> if block can sustain plant
     */
    @Override
    public boolean canSustainPlant(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing, IPlantable plantable) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
            if (!isSideSolid(blockState, blockAccess, blockPos, facing)) {
                return false;
            }

            // Add representative attribute blocks
            Set<Block> blocks = new HashSet<Block>();
            EnumAttributeLocation location = BlockUtil.getAttributeLocationForFacing(cbTileEntity, facing);
            if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.COVER)) {
            	IBlockState sideBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), location, EnumAttributeType.COVER);
            	blocks.add(sideBlockState.getBlock());
            	Material material = sideBlockState.getBlock().getMaterial(sideBlockState);
            	if (material.equals(Material.GRASS)) {
           	 		blocks.add(Blocks.GRASS);
           	 	} else if (material.equals(Material.GROUND)) {
                	blocks.add(Blocks.DIRT);
                } else if (material.equals(Material.SAND)) {
                	blocks.add(Blocks.SAND);
                }
            }
            if (cbTileEntity.getAttributeHelper().hasAttribute(location, EnumAttributeType.OVERLAY)) {
            	blocks.add(BlockUtil.toBlock(OverlayHandler.getOverlayType((ItemStack)cbTileEntity.getAttributeHelper().getAttribute(location, EnumAttributeType.OVERLAY).getModel()).getItemStack()));
            }
            
            switch (plantable.getPlantType(blockAccess, blockPos.add(0, 1, 0))) {
                case Desert: return blocks.contains(Blocks.SAND);
                case Nether: return blocks.contains(Blocks.SOUL_SAND);
                case Plains: return blocks.contains(Blocks.GRASS) || blocks.contains(Blocks.DIRT);
                case Beach:
                    boolean isBeach = blocks.contains(Blocks.GRASS) || blocks.contains(Blocks.DIRT) || blocks.contains(Blocks.SAND);
                    boolean hasWater = blockAccess.getBlockState(blockPos.add(-1, 0,  0)).getMaterial() == Material.WATER ||
                                       blockAccess.getBlockState(blockPos.add( 1, 0,  0)).getMaterial() == Material.WATER ||
                                       blockAccess.getBlockState(blockPos.add( 0, 0, -1)).getMaterial() == Material.WATER ||
                                       blockAccess.getBlockState(blockPos.add( 0, 0,  1)).getMaterial() == Material.WATER;
                    return isBeach && hasWater;
                default:
                    break;
            }
        }
        return super.canSustainPlant(blockState, blockAccess, blockPos, facing, plantable);
    }

    /**
     * Gets whether this block is considered solid.
     * 
     * @param blockAccess the block accessor
     * @param blockPos the block position
     * @return <code>true</code> if block is considered solid
     */
    protected boolean isBlockSolid(IBlockAccess blockAccess, BlockPos blockPos) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
            return !cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER) || BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), EnumAttributeLocation.HOST, EnumAttributeType.COVER).isOpaqueCube();
        } else {
            return false;
        }
    }

    /**
     * Called when the block is placed in the world.
     * 
     * @param world the world
     * @param blockPos the block position
     * @param blockState the block state
     * @param entityLivingBase the entity
     * @param itemStack the player held ItemStack
     */
    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        if (!world.isRemote) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                cbTileEntity.setOwner(new ProtectedObject((EntityPlayer)entityLivingBase));
            }
        }
    }

    /**
     * Get the hardness of this Block relative to the ability of the given player
     */
    @Deprecated
    @Override
    public float getPlayerRelativeBlockHardness(IBlockState blockState, EntityPlayer entityPlayer, World world, BlockPos blockPos) {
    	ItemStack itemStack = entityPlayer.getHeldItem(EventHandler.eventHand);
    	
    	// Carpenter's tools should not damage block
        if (!itemStack.isEmpty()) {
            Item item = itemStack.getItem();
            if (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel) {
                return -1;
            }
        }
        
        // Return cover block hardness if one is present
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
        	IBlockState outBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), EnumAttributeLocation.HOST, EnumAttributeType.COVER);
        	if (outBlockState == null) {
        		outBlockState = blockState;
        	}
        	return ForgeHooks.blockStrength(outBlockState, entityPlayer, world, blockPos);
        } else {
            return super.getPlayerRelativeBlockHardness(blockState, entityPlayer, world, blockPos);
        }
    }
    
    // Below method is likely inherited from base block state
/*    @Override
    *//**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     *//*
    public float getPlayerRelativeBlockHardness(EntityPlayer entityPlayer, World world, int x, int y, int z) {
         Don't damage block if holding Carpenter's tool. 

        ItemStack itemStack = entityPlayer.getHeldItem();

        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (item instanceof ICarpentersHammer || item instanceof ICarpentersChisel) {
                return -1;
            }
        }

         Return block hardness of cover. 

        CbTileEntity cbTileEntity = getTileEntity(world, x, y, z);

        if (cbTileEntity != null) {
            return ForgeHooks.blockStrength(BlockProperties.toBlock(BlockProperties.getCover(cbTileEntity, 6)), entityPlayer, world, x, y, z);
        } else {
            return super.getPlayerRelativeBlockHardness(entityPlayer, world, x, y, z);
        }
    }*/

/*    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z)
    {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, x, y, z);
        if (cbTileEntity != null)
        {
            ItemStack itemStack = BlockProperties.getCover(cbTileEntity, 6);
            Block block = BlockProperties.toBlock(itemStack);
            if (!(block instanceof BlockCoverable)) {
                return block instanceof IWrappableBlock ? ((IWrappableBlock)block).getColorMultiplier(blockAccess, x, y, z, block, itemStack.getItemDamage()) : block.colorMultiplier(blockAccess, x, y, z);
            }
        }

        return super.colorMultiplier(blockAccess, x, y, z);
    }*/

/*    *//**
     * Gets whether side should be rendered.
     * 
     * @param blockState the block state
     * @param blockAccess the
     * @param blockPos
     * @param facing
     * @return
     *//*
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing)
    {
        // Side checks in out-of-range areas will crash
        if (y > 0 && y < blockAccess.getHeight())
        {
            CbTileEntity cbTileEntity = getTileEntity(blockAccess, x, y, z);
            if (cbTileEntity != null) {
                ForgeDirection side_src = ForgeDirection.getOrientation(side);
                ForgeDirection side_adj = side_src.getOpposite();

                CbTileEntity cbTileEntity_adj = (CbTileEntity) blockAccess.getTileEntity(x, y, z);
                CbTileEntity cbTileEntity_src = (CbTileEntity) blockAccess.getTileEntity(x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ);

                if (cbTileEntity_adj.getBlockType().isSideSolid(blockAccess, x, y, z, side_adj) == cbTileEntity_src.getBlockType().isSideSolid(blockAccess, x + side_adj.offsetX, y + side_adj.offsetY, z + side_adj.offsetZ, ForgeDirection.getOrientation(side))) {

                    if (shareFaces(cbTileEntity_adj, cbTileEntity_src, side_adj, side_src)) {

                        Block block_adj = BlockProperties.toBlock(BlockProperties.getCover(cbTileEntity_adj, 6));
                        Block block_src = BlockProperties.toBlock(BlockProperties.getCover(cbTileEntity_src, 6));

                        if (!cbTileEntity_adj.hasAttribute(cbTileEntity.ATTR_COVER[6])) {
                            return cbTileEntity_src.hasAttribute(cbTileEntity.ATTR_COVER[6]);
                        } else {
                            if (!cbTileEntity_src.hasAttribute(cbTileEntity.ATTR_COVER[6]) && block_adj.getRenderBlockPass() == 0) {
                                return !block_adj.isOpaqueCube();
                            } else if (cbTileEntity_src.hasAttribute(cbTileEntity.ATTR_COVER[6]) && block_src.isOpaqueCube() == block_adj.isOpaqueCube() && block_src.getRenderBlockPass() == block_adj.getRenderBlockPass()) {
                                return false;
                            } else {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return super.shouldSideBeRendered(blockAccess, x, y, z, side);
    }*/

    /**
     * Gets whether block should render in given layer.
     * 
     * @param blockState the block state
     * @param blockRenderlayer the block render layer
     * @return <code>true</code> if block can render in layer
     */
	public boolean canRenderInLayer(IBlockState blockState, BlockRenderLayer blockRenderLayer) {
		return true;
	}

    /**
     * Returns whether two blocks share faces.
     * <p>
     * Used for rendering purposes to cull faces.
     * TODO: Determine if has any effect
     */
    protected boolean shareFaces(CbTileEntity cbTileEntity_adj, CbTileEntity cbTileEntity_src, EnumFacing facing_adj, EnumFacing facing_src) {
        return cbTileEntity_adj.getBlockType().isSideSolid(getDefaultState(), cbTileEntity_adj.getWorld(), cbTileEntity_adj.getPos(), facing_adj) &&
               cbTileEntity_src.getBlockType().isSideSolid(getDefaultState(), cbTileEntity_src.getWorld(), cbTileEntity_src.getPos(), facing_src);
    }

/*    @Override
    *//**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     *//*
    public boolean isOpaqueCube()
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
            return false;
        }

        if (FeatureRegistry.enableRoutableFluids) {
            // Server condition may fail, so don't throw error if performing server-side
            try {
                Class<?> clazz = RoutableFluidsHelper.getCallerClass();
                if (clazz != null) {
                    for (Class clazz1 : RoutableFluidsHelper.liquidClasses) {
                        if (clazz.isAssignableFrom(clazz1)) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {}
        }

        return false;
    }*/

    
/*    *//**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     *//*
    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }*/
    
    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
    	// TODO Auto-generated method stub
    	super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    /**
     * Whether block should be neighbor block brightness
     * when rendered.
     * 
     * @param blockState the block state
     * @return <code>true</code> if should use neighbor brightness
     */
    @Override
    public boolean getUseNeighborBrightness(IBlockState blockState) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
    	return new CbTileEntity();
    }

    /**
     * Gets whether block has tile entity for given block state.
     * 
     * @param blockState the block state
     * @return <code>true</code> if has a tile entity
     */
    @Override
    public boolean hasTileEntity(IBlockState blockState) {
        return true;
    }
    
    /**
     * Damages item with chance to deal damage.
     * <p>
     * Used to allow Carpenter's Tools to take damage
     * at a specified rate and chance.
     * 
     * @param world the world
     * @param entityPlayer the player
     */
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer, EnumHand hand) {
        Item item = entityPlayer.getHeldItem(hand).getItem();
        if (item instanceof ICarpentersHammer) {
            ((ICarpentersHammer)item).onHammerUse(world, entityPlayer, hand);
        } else if (item instanceof ICarpentersChisel) {
            ((ICarpentersChisel)item).onChiselUse(world, entityPlayer, hand);
        }
    }

    /**
     * Called before click event alters block features such
     * as cover, overlay, or dye color.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param entityPlayer the player
     * @param actionResult stores action parameters
     */
    protected void preOnBlockClicked(CbTileEntity cbTileEntity, EntityPlayer entityPlayer, ActionResult actionResult) {}

    /**
     * Called before activation event alters block features
     * such as cover, overlay, or dye color.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param entityPlayer the player
     * @param facing the activated face
     * @param hitX the x hit coordinate
     * @param hitY the y hit coordinate
     * @param hitZ the z hit coordinate
     * @param actionResult stores action parameters
     */
    protected void preOnBlockActivated(CbTileEntity cbTileEntity, EntityPlayer entityPlayer, EnumFacing facing, float hitX, float hitY, float hitZ, ActionResult actionResult) {}

    /**
     * Called after attribute changes processed as long as
     * no changes occurred.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param entityPlayer the player
     * @param facing the activated face
     * @param hitX the x hit coordinate
     * @param hitY the y hit coordinate
     * @param hitZ the z hit coordinate
     * @param actionResult stores action parameters
     */
    protected void postOnBlockActivated(CbTileEntity cbTileEntity, EntityPlayer entityPlayer, EnumFacing facing, float hitX, float hitY, float hitZ, ActionResult actionResult) {}

    /**
     * Handles hammer left click interactions.
     * <p>
     * Override this for block-specific functionality.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param entityPlayer the player
     * @return <code>true</code> if tile entity changed
     */
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
        return false;
    }

    /**
     * Handles hammer right click interactions.
     * <p>
     * Override this for block-specific functionality.
     * 
     * @param cbTileEntity the Carpenter's Block tile entity
     * @param entityPlayer the player
     * @return <code>true</code> if tile entity changed
     */
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
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
    
    @Override
    /**
     * Creates extended blockstate container.
     * 
     * Note: Listed IProperty properties are cached in chunk and are
     * necessary for communicating block facing when first placed in world.
     * Unlisted IUnlistedProperty properties can carry custom data
     * to render classes, but are not cached in chunk.
     */
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(
        	this,
        	Property._listedProperties,
        	Property._unlistedProperties.toArray(new IUnlistedProperty[Property._unlistedProperties.size()]));
    }

    @Override
    /**
     * Handles passing block properties for rendering purposes mainly.
     * This data will not persist in chunk cache, unlike IProperty.
     */
    public IBlockState getExtendedState(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos) {
    	if (blockState instanceof IExtendedBlockState) {
            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
            return ((IExtendedBlockState)blockState)
            	.withProperty(Property.BLOCK_TYP, this)
    			.withProperty(Property.BLOCK_POS, blockPos)
    			.withProperty(Property.CB_METADATA, cbTileEntity.getCbMetadata())
    			.withProperty(Property.RENDER_FACE, new Boolean[] {
    				this.shouldSideBeRendered(blockState, blockAccess, blockPos, EnumFacing.DOWN),
    				this.shouldSideBeRendered(blockState, blockAccess, blockPos, EnumFacing.UP),
    				this.shouldSideBeRendered(blockState, blockAccess, blockPos, EnumFacing.NORTH),
    				this.shouldSideBeRendered(blockState, blockAccess, blockPos, EnumFacing.SOUTH),
    				this.shouldSideBeRendered(blockState, blockAccess, blockPos, EnumFacing.WEST),
    				this.shouldSideBeRendered(blockState, blockAccess, blockPos, EnumFacing.EAST)
    			})
    			.withProperty(Property.ATTR_MAP, cbTileEntity.getAttributeHelper().copyMap());
        }
        return blockState;
    }
    
    /**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public EnumBlockRenderType getRenderType(IBlockState blockState) {
        return EnumBlockRenderType.MODEL;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return 0;
    }
    
}
