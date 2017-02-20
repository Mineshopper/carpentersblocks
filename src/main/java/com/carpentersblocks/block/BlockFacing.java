package com.carpentersblocks.block;

import java.util.Arrays;

import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.block.CollapsibleUtil;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class BlockFacing extends BlockCoverable {

    public BlockFacing(Material material) {
        super(material);
    }
    
    public abstract void setFacing(CbTileEntity cbTileEntity, EnumFacing facing);
    
    public abstract EnumFacing getFacing(CbTileEntity cbTileEntity);

    /**
     * Checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, EnumFacing facing) {
        if (canAttachToFacing(facing)) {
        	return world.isSideSolid(blockPos.add(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ()), facing);
        } else {
            return false;
        }
    }

    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public IBlockState onBlockPlaced(World world, BlockPos blockPos, EnumFacing facing, float hitX, float hitY, float hitZ, int metadata, EntityLivingBase entityLivingBase) {
        return getDefaultState().withProperty(BlockDirectional.FACING, facing);
    }
    
    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState blockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, blockState, entityLivingBase, itemStack);
        if (!ignoreSidePlacement()) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
            	CollapsibleUtil util = new CollapsibleUtil(cbTileEntity.getData());
                EnumFacing facing = getPlacementDirection(blockState);
                setFacing(cbTileEntity, facing);
            }
        }
        world.notifyNeighborsOfStateChange(blockPos, this);
    }

    /**
     * Gets placement direction when first placed in world.
     *
     * @param world the {@link World}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the {@link ForgeDirection}
     */
    protected EnumFacing getPlacementDirection(IBlockState blockState) {
        return blockState.getValue(BlockDirectional.FACING);
    }

    /**
     * Whether side block placed against influences initial direction of block.
     *
     * @return <code>true</code> if initial placement direction ignored
     */
    protected boolean ignoreSidePlacement() {
        return false;
    }

    @Override
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World world) {
        return 20;
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborChange(IBlockAccess blockAccess, BlockPos blockPos, BlockPos neighborBlockPos) {
        super.onNeighborChange(blockAccess, blockPos, neighborBlockPos);
        World world = (World)blockAccess;
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null && !canPlaceBlockOnSide(world, blockPos, getFacing(cbTileEntity)) && !canFloat()) {
            //destroyBlock(world, blockPos, true);
        }
    }

    /**
     * Notifies relevant blocks of a change in power output.
     *
     * @param  world
     * @param  x
     * @param  y
     * @param  z
     * @return nothing
     */
    public void notifyBlocksOfPowerChange(World world, IBlockState blockState, BlockPos blockPos) {
    	// Strong power change
        world.notifyBlockOfStateChange(blockPos, this);

        // Weak power change
        if (canProvidePower(blockState)) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                EnumFacing facing = getFacing(cbTileEntity);
                //world.notifyBlockOfStateChange(blockPos.add(-1, -1, -1), this);
            } else {
            	//world.notifyNeighborsOfStateChange(blockPos, this);
            }
        }
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing) {
        int power = super.getWeakPower(blockState, blockAccess, blockPos, facing);
        if (canProvidePower(blockState)) {
            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
            if (cbTileEntity != null) {
                power = Math.max(power, getPowerOutput(cbTileEntity));
            }
        }
        return power;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos blockPos, EnumFacing facing) {
        int power = super.getStrongPower(blockState, blockAccess, blockPos, facing);
        if (canProvidePower(blockState)) {
            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
            if (cbTileEntity != null) {
                if (facing.equals(getFacing(cbTileEntity))) {
                    power = Math.max(power, getPowerOutput(cbTileEntity));
                }
            }
        }
        return power;
    }

    @Override
    /**
     * Ejects contained items into the world, and notifies neighbors of an update, as appropriate
     */
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        if (canProvidePower(blockState)) {
            notifyBlocksOfPowerChange(world, blockState, blockPos);
        }
        super.breakBlock(world, blockPos, blockState);
    }

    /**
     * Gets block-specific power level from 0 to 15.
     *
     * @param  cbTileEntity  the {@link CbTileEntity}
     * @return the power output
     */
    public int getPowerOutput(CbTileEntity cbTileEntity) {
    	return 0;
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     */
    public boolean canAttachToFacing(EnumFacing facing) {
        return true;
    }

    /**
     * Whether block requires an adjacent block with solid side for support.
     *
     * @return whether block can float freely
     */
    public boolean canFloat() {
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos blockPos, EnumFacing axis) {
        if (Arrays.asList(getRotationAxes()).contains(axis)) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                EnumFacing facing = getFacing(cbTileEntity);
                //return data.setFacing(cbTileEntity, facing.rotateAround(axis.getAxis()));
            }
        }

        return false;
    }

    /**
     * Get supported axes of rotation.
     *
     * @return an array of axes
     */
    protected EnumFacing.Axis[] getRotationAxes()
    {
        return new EnumFacing.Axis[] { EnumFacing.Axis.Y };
    }

}
