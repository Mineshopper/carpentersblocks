package com.carpentersblocks.block;

public abstract class BlockFacing extends AbstractCoverableBlock {

    public BlockFacing(Properties properties) {
        super(properties);
    }
    
    //public abstract void setFacing(CbTileEntity cbTileEntity, Direction facing);
    
    //public abstract Direction getFacing(CbTileEntity cbTileEntity);

    /**
     * Checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     *
    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos blockPos, Direction facing) {
        if (canAttachToFacing(facing)) {
        	return world.isSideSolid(blockPos.offset(facing.getOpposite()), facing);
        } else {
            return false;
        }
    }
    
    @Override
    /**
     * Called when a block is placed using its BlockItem. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     *
    public BlockState getStateForPlacement(World world, BlockPos blockPos, Direction facing, float hitX, float hitY, float hitZ, int metadata, EntityLivingBase entityLivingBase) {
    	return this.getDefaultState().withProperty(BlockDirectional.FACING, facing);
    }
    
    @Override
    /**
     * Called when the block is placed in the world.
     *
    public void onBlockPlacedBy(World world, BlockPos blockPos, BlockState blockState, EntityLivingBase entityLivingBase, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, blockState, entityLivingBase, itemStack);
        if (!ignoreSidePlacement()) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                Direction facing = (Direction) blockState.getProperties().get(BlockDirectional.FACING);
                setFacing(cbTileEntity, facing);
            }
        }
        world.notifyNeighborsOfStateChange(blockPos, this, false);
    }

    /**
     * Whether side block placed against influences initial direction of block.
     *
     * @return <code>true</code> if initial placement direction ignored
     *
    protected boolean ignoreSidePlacement() {
        return false;
    }

    @Override
    /**
     * How many world ticks before ticking
     *
    public int tickRate(World world) {
        return 20;
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     *
    public void onNeighborChange(IWorld blockAccess, BlockPos blockPos, BlockPos neighborBlockPos) {
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
     *
    public void notifyBlocksOfPowerChange(World world, BlockState blockState, BlockPos blockPos) {
    	// Strong power change
        world.neighborChanged(blockPos, this, blockPos);

        // Weak power change
        if (canProvidePower(blockState)) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                Direction facing = getFacing(cbTileEntity);
                BlockPos pos = blockPos.offset(facing.getOpposite());
                world.neighborChanged(pos, this, pos);
            } else {
            	world.notifyNeighborsOfStateChange(blockPos, this, false);
            }
        }
    }

    @Override
    public int getWeakPower(BlockState blockState, IWorld blockAccess, BlockPos blockPos, Direction facing) {
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
    public int getStrongPower(BlockState blockState, IWorld blockAccess, BlockPos blockPos, Direction facing) {
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
     *
    public void breakBlock(World world, BlockPos blockPos, BlockState blockState) {
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
     *
    public int getPowerOutput(CbTileEntity cbTileEntity) {
    	return 0;
    }

    /**
     * Whether block can be attached to specified side of another block.
     *
     * @param  side the side
     * @return whether side is supported
     *
    public boolean canAttachToFacing(Direction facing) {
        return true;
    }

    /**
     * Whether block requires an adjacent block with solid side for support.
     *
     * @return whether block can float freely
     *
    public boolean canFloat() {
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos blockPos, Direction axis) {
        if (Arrays.asList(getRotationAxes()).contains(axis)) {
            CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
                Direction facing = getFacing(cbTileEntity);
                //return data.setFacing(cbTileEntity, facing.rotateAround(axis.getAxis()));
            }
        }
        return false;
    }

    /**
     * Get supported axes of rotation.
     *
     * @return an array of axes
     *
    protected Direction.Axis[] getRotationAxes() {
        return new Direction.Axis[] { Direction.Axis.Y };
    }
    
    /**
     * Ticks the block if it's been scheduled
     *
    @Override
    public void updateTick(World world, BlockPos blockPos, BlockState blockState, Random rand) {
    	if (canProvidePower(blockState)) {
    		CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
            if (cbTileEntity != null) {
		        world.notifyNeighborsOfStateChange(blockPos, this, false);
		        world.notifyNeighborsOfStateChange(blockPos.offset(this.getFacing(cbTileEntity).getOpposite()), this, false);
            }
    	}
    }*/

}
