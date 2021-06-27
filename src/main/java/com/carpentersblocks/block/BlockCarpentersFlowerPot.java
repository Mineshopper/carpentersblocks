package com.carpentersblocks.block;

import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.states.StateMap;

public class BlockCarpentersFlowerPot extends AbstractCoverableBlock implements IStateImplementor {

	private static StateMap _stateMap;
	
    public BlockCarpentersFlowerPot(Properties properties, StateMap stateMap) {
        super(properties);
        _stateMap = stateMap;
    }

	@Override
	public String getStateDescriptor(CbTileEntity cbTileEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StateMap getStateMap() {
		// TODO Auto-generated method stub
		return null;
	}
    
    //@Override
    /**
     * Cycle backward through designs.
     *
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
        ((IDesignable)cbTileEntity).setPrevDesign(EnumAttributeLocation.HOST);
        cbTileEntity.removeAttribute(EnumAttributeType.COVER);
        return true;
    }

    @Override
    /**
     * Cycle forward through designs or set to no design.
     *
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, PlayerEntity playerEntity) {
        if (playerEntity.isSneaking()) {
            ((IDesignable)cbTileEntity).removeDesign(EnumAttributeLocation.HOST);
        } else {
        	((IDesignable)cbTileEntity).setNextDesign(EnumAttributeLocation.HOST);
        }
        cbTileEntity.removeAttribute(EnumAttributeType.COVER);
        return true;
    }

    /**
     * Checks if {@link ItemStack} contains fertilizer.
     *
     * @return <code>true</code> if {@link ItemStack} contains fertilizer
     *
    public static boolean isFertilizer(ItemStack itemStack) {
        return itemStack != null ? itemStack.getItem().equals(Items.BONE_MEAL) : false;
    }

    @Override
    /**
     * Sneak-click removes plant and/or soil.
     *
    protected void preOnBlockClicked(CbTileEntity cbTileEntity, PlayerEntity playerEntity, ActionResult actionResult) {
        if (playerEntity.isSneaking()) {
            if (EventHandler.eventHitVector.y > 0.375F) {
                if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeType.FERTILIZER)) {
                    actionResult.setSoundSource(new ItemStack(Blocks.SAND));
                    actionResult.setAltered();
                    cbTileEntity.removeAttribute(EnumAttributeType.FERTILIZER);
                }
                if (!actionResult.altered && cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeType.PLANT)) {
                    actionResult.setSoundSource(((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)).getModel());
                    actionResult.setAltered();
                    cbTileEntity.removeAttribute(EnumAttributeType.PLANT);
                }
            } else if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeType.SOIL)) {
                if (Direction.UP.equals(EventHandler.eventFace)
                		&& EventHandler.eventHitVector.x > 0.375F
                		&& EventHandler.eventHitVector.x < 0.625F
                		&& EventHandler.eventHitVector.z > 0.375F
                		&& EventHandler.eventHitVector.z < 0.625F) {
                    actionResult.setSoundSource((ItemStack) cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.SOIL).getModel());
                    actionResult.setAltered();
                    cbTileEntity.removeAttribute(EnumAttributeType.SOIL);
                }
            }
        }
    }

    @Override
    /**
     * Everything contained in this will run before default onBlockActivated events take place,
     * but after the player has been verified to have permission to edit block.
     *
    protected void preOnBlockActivated(CbTileEntity cbTileEntity, PlayerEntity playerEntity, Direction facing, float hitX, float hitY, float hitZ, ActionResult actionResult) {
        ItemStack itemStack = playerEntity.getHeldItemMainhand();

        if (itemStack != null) {

            boolean hasCover = cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER);
            boolean hasOverlay = cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.OVERLAY);
            boolean hasSoil = cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.SOIL);
            boolean soilAreaClicked = Direction.UP.equals(facing) && hitX > 0.375F && hitX < 0.625F && hitZ > 0.375F && hitZ < 0.625F;

            if (hasSoil) {

                /*
                 * Leaf blocks can be plants or covers.  We need to differentiate
                 * it based on where the block is clicked, and whether it already
                 * has a cover.
                 *
                if (!soilAreaClicked) {
                    if (!hasCover && BlockUtil.isCover(itemStack) || !hasOverlay && BlockUtil.isOverlay(itemStack)) {
                        return;
                    }
                }

                if (!cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT) && FlowerPotUtil.isPlant(itemStack)) {
                    int angle = MathHelper.floor((playerEntity.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 15;
                    FlowerPotMetadata.setAngle(cbTileEntity, angle);
                    cbTileEntity.addAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT, itemStack);
                    actionResult.setAltered().setSoundSource(itemStack).decInventory();
                }

            } else {

                if (FlowerPotUtil.isSoil(itemStack)) {
                    if (hasCover || soilAreaClicked) {
                        cbTileEntity.addAttribute(EnumAttributeLocation.HOST, EnumAttributeType.SOIL, itemStack);
                        actionResult.setAltered().setSoundSource(itemStack).decInventory();
                    }
                }

            }

        }
    }

    @Override
    /**
     * Called upon block activation (right click on the block.)
     *
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        /*
         * Need to handle plant enrichment here since the properties
         * needing to be compared against are client-side only.
         *
         * Client will send relevant properties to the server using a packet,
         * and from there the server will determine if plant should be affected.
         *

        if (worldIn.isRemote) {
            CbTileEntity cbTileEntity = getTileEntity(worldIn, pos);
            if (cbTileEntity != null && cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)) {
                ItemStack itemStack = player.getHeldItemMainhand();
                if (itemStack != null && Items.BONE_MEAL.equals(itemStack.getItem())) {
                    if (!cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER) && FlowerPotUtil.isPlantColorable(cbTileEntity)) {
                        PacketHandler.sendPacketToServer(new PacketEnrichPlant(pos, FlowerPotUtil.getPlantColor(cbTileEntity)));
                        return ActionResultType.CONSUME;
                    }
                }
            }
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own)
     *
    public void onNeighborChange(IWorld blockAccess, BlockPos blockPos, BlockPos neighborBlockPos) {
    	CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
    	World world = cbTileEntity.getWorld();
    	if (!world.isRemote) {
            if (cbTileEntity != null) {
                if (!canPlaceBlockOnSide(world, blockPos, Direction.UP)) {
                    destroyBlock(world, blockPos, true);
                }
                // Eject double tall plant if obstructed
                if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)) {
                    Profile profile = FlowerPotUtil.getPlantProfile(cbTileEntity);
                    if (profile.equals(Profile.DOUBLEPLANT) || profile.equals(Profile.THIN_DOUBLEPLANT)) {
                    	BlockState blockState = world.getBlockState(blockPos.up());
                        if (world.getBlockState(blockPos.up()).getBlock().isSideSolid(blockState, world, blockPos.up(), Direction.DOWN)) {
                        	cbTileEntity.removeAttribute(EnumAttributeType.PLANT);
                        }
                    }

                }
            }
        }
        super.onNeighborChange(blockAccess, blockPos, neighborBlockPos);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     *
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        BlockState downState = world.getBlockState(pos.down());
        return super.canPlaceBlockAt(world, pos) && (downState.isTopSolid()
        		|| downState.getBlockFaceShape(world, pos.down(), Direction.UP) == BlockFaceShape.SOLID)
        		|| downState.getBlock().canPlaceTorchOnTop(downState, world, pos);
    }

    @Override
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     *
    public void onEntityCollidedWithBlock(World world, BlockPos pos, BlockState state, Entity entity) {
        CbTileEntity cbTileEntity = getTileEntity(world, pos);
        if (cbTileEntity != null && cbTileEntity instanceof CbTileEntityFlowerPot) {
            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)) {
                ItemStack itemStack = ((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)).getModel();
                Block block = FlowerPotUtil.toBlock(itemStack);
                cbTileEntity.setCbMetadata(itemStack.getItemDamage());
                BlockState blockState = block.getStateFromMeta(itemStack.getItemDamage());
                FlowerPotUtil.toBlock(itemStack).onEntityCollidedWithBlock(world, pos, blockState, entity);
                cbTileEntity.restoreBlockState();
            }
        }
        super.onEntityCollidedWithBlock(world, pos, state, entity);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * A randomly called display update to be able to add particles or other items for display
     *
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random rand) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null && cbTileEntity instanceof CbTileEntityFlowerPot) {

            /*
             * Metadata at coordinates are for the base cover only.
             * We need to set it for appropriate attributes in order
             * to get accurate results.
             *
            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)) {
                ItemStack itemStack = ((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)).getModel();
                Block plantBlock = FlowerPotUtil.toBlock(itemStack);
                BlockState plantBlockState = plantBlock.getStateFromMeta(itemStack.getItemDamage());
                cbTileEntity.setBlockState(itemStack.getItemDamage());
                plantBlock.randomDisplayTick(plantBlockState, world, blockPos, rand);
                cbTileEntity.restoreBlockState();
            }

            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.SOIL)) {
                ItemStack itemStack = ((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.SOIL)).getModel();
                Block soilBlock = BlockUtil.toBlock(itemStack);
                BlockState soilBlockState = soilBlock.getStateFromMeta(itemStack.getItemDamage());
                cbTileEntity.setBlockState(itemStack.getItemDamage());
                soilBlock.randomDisplayTick(soilBlockState, world, blockPos, rand);
                cbTileEntity.restoreBlockState();
            }

        }

        super.randomDisplayTick(blockState, world, blockPos, rand);
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     *
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IWorld world, BlockPos pos, BlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        CbTileEntity cbTileEntity = getSimpleTileEntity(world, pos);
        if (cbTileEntity != null) {
            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER)) {
            	drops.add(((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.FERTILIZER)).getModel());
            }
            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)) {
            	drops.add(((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.PLANT)).getModel());
            }
            if (cbTileEntity.getAttributeHelper().hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.SOIL)) {
            	drops.add(((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(EnumAttributeLocation.HOST, EnumAttributeType.SOIL)).getModel());
            }
        }
    }
    
/*    @Override
    protected boolean canCoverSide(CbTileEntity cbTileEntity, World world, BlockPos pos, Direction facing)
    {
        return side == 6 ? !cbTileEntity.hasDesign() : false;
    }*/

}
