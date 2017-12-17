package com.carpentersblocks.block;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.handler.EventHandler;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpentersSlope extends BlockCoverable {

    public final static String slopeType[] = { "wedge", "obliqueInterior", "obliqueExterior", "prism", "prismWedge" };

    public final static int META_WEDGE       = 0;
    public final static int META_OBLIQUE_INT = 1;
    public final static int META_OBLIQUE_EXT = 2;
    public final static int META_PRISM       = 3;
    public final static int META_PRISM_SLOPE = 4;

    private boolean rayTracing;

    public BlockCarpentersSlope(Material material) {
        super(material);
    }

    @Override
    /**
     * Alters block direction.
     */
    protected boolean onHammerLeftClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	return rotateBlock(cbTileEntity.getWorld(), cbTileEntity.getPos(), EventHandler.eventFace);
    }

    @Override
    /**
     * Alters block type.
     */
    protected boolean onHammerRightClick(CbTileEntity cbTileEntity, EntityPlayer entityPlayer) {
    	SlopeData.setNextType(cbTileEntity);
        return true;        
    }

/*    @SideOnly(Side.CLIENT)
    @Override
    *//**
     * Returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     *//*
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, META_WEDGE      ));
        list.add(new ItemStack(item, 1, META_OBLIQUE_INT));
        list.add(new ItemStack(item, 1, META_OBLIQUE_EXT));
        list.add(new ItemStack(item, 1, META_PRISM      ));
        list.add(new ItemStack(item, 1, META_PRISM_SLOPE));
    }*/

/*    @Override
    *//**
     * Damages hammer with a chance to not damage.
     *//*
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer) {
        if (world.rand.nextFloat() <= ItemRegistry.itemHammerDamageChanceFromSlopes) {
            super.damageItemWithChance(world, entityPlayer);
        }
    }*/

/*    @Override
    *//**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     *//*
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, BlockPos blockPos) {
        if (!rayTracing) {

            CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);

            if (cbTileEntity != null) {

                Slope slope = Slope.getSlope(cbTileEntity);

                switch (slope.getPrimaryType()) {
                    case PRISM:
                    case PRISM_1P:
                    case PRISM_2P:
                    case PRISM_3P:
                    case PRISM_4P:
                        if (slope.isPositive) {
                            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
                        } else {
                            setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
                        }
                        break;
                    default:
                        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                        break;
                }

            }

        }
    }*/

    /*@Override
    *//**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     *//*
    public MovingObjectPosition collisionRayTrace(World world, BlockPos blockPos, Vec3 startVec, Vec3 endVec) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        MovingObjectPosition finalTrace = null;

        if (cbTileEntity != null) {

            Slope slope = Slope.getSlope(cbTileEntity);
            SlopeUtil slopeUtil = new SlopeUtil();

            int numPasses = slopeUtil.getNumPasses(slope);
            int precision = slopeUtil.getNumBoxesPerPass(slope);

            rayTracing = true;

             Determine if ray trace is a hit on slope. 
            for (int pass = 0; pass < numPasses; ++pass)
            {
                for (int slice = 0; slice < precision && finalTrace == null; ++slice)
                {
                    float[] box = slopeUtil.genBounds(slope, slice, precision, pass);

                    if (box != null) {
                        setBlockBounds(box[0], box[1], box[2], box[3], box[4], box[5]);
                        finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
                    }
                }
                if (slope.type.equals(Type.OBLIQUE_EXT)) {
                    --precision;
                }
            }

            rayTracing = false;

             Determine true face hit since sloped faces are two or more shared faces. 

            if (finalTrace != null) {
                setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
            }

        }

        return finalTrace;
    }*/

    /*@Override
    *//**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     *//*
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity) {
        CbTileEntity cbTileEntity = getTileEntity(world, x, y, z);

        if (cbTileEntity != null) {

            AxisAlignedBB box = null;

            Slope slope = Slope.getSlope(cbTileEntity);
            SlopeUtil slopeUtil = new SlopeUtil();

            int precision = slopeUtil.getNumBoxesPerPass(slope);
            int numPasses = slopeUtil.getNumPasses(slope);

            for (int pass = 0; pass < numPasses; ++pass) {

                for (int slice = 0; slice < precision; ++slice)
                {
                    float[] dim = slopeUtil.genBounds(slope, slice, precision, pass);

                    if (dim != null) {
                        box = AxisAlignedBB.getBoundingBox(x + dim[0], y + dim[1], z + dim[2], x + dim[3], y + dim[4], z + dim[5]);
                    }

                    if (box != null && axisAlignedBB.intersectsWith(box)) {
                        list.add(box);
                    }
                }

                if (slope.type.equals(Type.OBLIQUE_EXT)) {
                    --precision;
                }

            }

        }
    }*/

/*    @Override
    *//**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *//*
    public boolean isSideSolid(IBlockAccess blockAccess, BlockPos blockPos, EnumFacing side) {
        CbTileEntity cbTileEntity = getTileEntity(blockAccess, blockPos);
        if (cbTileEntity != null) {
            if (isBlockSolid(blockAccess, blockPos)) {
                return Slope.getSlope(cbTileEntity).isFaceFull(side);
            }
        }
        return false;
    }*/

    @Override
    /**
     * Returns whether sides share faces based on sloping property and face shape.
     */
    protected boolean shareFaces(CbTileEntity cbTileEntity_adj, CbTileEntity cbTileEntity_src, EnumFacing side_adj, EnumFacing side_src) {
/*        if (cbTileEntity_adj.getBlockType() == this) {
            Slope slope_src = Slope.getSlope(cbTileEntity_src);
            Slope slope_adj = Slope.getSlope(cbTileEntity_adj);
            if (!slope_adj.hasSide(side_adj)) {
                return false;
            } else if (slope_src.getFaceBias(side_src) == slope_adj.getFaceBias(side_adj)) {
                return true;
            } else {
                return false;
            }
        }
        return super.shareFaces(cbTileEntity_adj, cbTileEntity_src, side_adj, side_src);*/
    	return false;
    }

/*    @Override
    *//**
     * Called when block is placed in world.
     *//*
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        EventHandler.eventFace = side;
        EventHandler.hitX = hitX;
        EventHandler.hitY = hitY;
        EventHandler.hitZ = hitZ;
        return metadata;
    }*/

/*    *//**
     * Returns wedge slope orientation based on side clicked and hit coordinates.
     *//*
    private int getWedgeOrientation(EnumFacing dir, int side, double hitX, double hitY, double hitZ) {
        switch (side) {
            case 2:
                hitX = 1.0F - hitX;
                break;
            case 4:
                hitX = hitZ;
                break;
            case 5:
                hitX = 1.0F - hitZ;
                break;
        }

        int slopeID;

        if (side > 1) {
            if (hitY > 0.5F && hitX > 1.0F - hitY && hitX < hitY) {
                slopeID = side + 2;
            } else if (hitY < 0.5F && hitX < 1.0F - hitY && hitX > hitY) {
                slopeID = side + 6;
            } else if (hitX < 0.2F) {
                slopeID = side == 2 ? 1 : side == 3 ? 0 : side == 4 ? 3 : 2;
            } else if (hitX > 0.8F){
                slopeID = side == 2 ? 2 : side == 3 ? 3 : side == 4 ? 1 : 0;
            } else if (hitY > 0.5F) {
                slopeID = side + 2;
            } else { // hitY < 0.5F
                slopeID = side + 6;
            }
        } else {
            slopeID = side + 12;
        }

        if (slopeID > 11) {
            switch (dir) {
                case NORTH:
                    return slopeID == 12 ? Slope.ID_WEDGE_NEG_S : Slope.ID_WEDGE_POS_S;
                case SOUTH:
                    return slopeID == 12 ? Slope.ID_WEDGE_NEG_N : Slope.ID_WEDGE_POS_N;
                case WEST:
                    return slopeID == 12 ? Slope.ID_WEDGE_NEG_E : Slope.ID_WEDGE_POS_E;
                case EAST:
                    return slopeID == 12 ? Slope.ID_WEDGE_NEG_W : Slope.ID_WEDGE_POS_W;
                default:
                    return 0;
            }
        } else {
            return slopeID;
        }

    }*/

    private final int CORNER_SE = 0;
    private final int CORNER_NE = 1;
    private final int CORNER_NW = 2;
    private final int CORNER_SW = 3;

    /**
     * Returns general slope orientation based on side clicked and hit coordinates.
     */
    private int getCorner(float rotationYaw) {
        switch ((MathHelper.floor(rotationYaw * 4.0F / 360.0F) & 3) % 4) {
            case 0:
                return CORNER_NE;
            case 1:
                return CORNER_SE;
            case 2:
                return CORNER_SW;
            default:
                return CORNER_NW;
        }
    }

    /*@Override
    *//**
     * Called when the block is placed in the world.
     * Uses cardinal direction to adjust metadata if player clicks top or bottom face of block.
     *//*
    public void onBlockPlacedBy(World world, BlockPos blockPos, EntityLivingBase entityLiving, ItemStack itemStack) {
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

        CbTileEntity cbTileEntity = getTileEntity(world, x, y, z);

        if (cbTileEntity != null) {

            int slopeID = 0;
            int metadata = world.getBlockMetadata(x, y, z);

            boolean isPositive = EventHandler.eventFace > 1 && EventHandler.hitY < 0.5F || EventHandler.eventFace == 1;
            int corner = getCorner(entityLiving.rotationYaw);

            EnumFacing dir = EntityLivingUtil.getFacing(entityLiving).getOpposite();

            switch (metadata) {
            case META_WEDGE:

                slopeID = getWedgeOrientation(dir, EventHandler.eventFace, EventHandler.hitX, EventHandler.hitY, EventHandler.hitZ);

                if (!entityLiving.isSneaking()) {
                    slopeID = SlopeTransform.transformWedge(world, slopeID, blockPos);
                    cbTileEntity.setData(slopeID);
                    SlopeTransform.transformAdjacentWedges(world, slopeID, blockPos);
                }

                break;
            case META_OBLIQUE_INT:

                switch (corner) {
                case CORNER_SE:
                    slopeID = isPositive ? Slope.ID_OBL_INT_POS_SE : Slope.ID_OBL_INT_NEG_SE;
                    break;
                case CORNER_NE:
                    slopeID = isPositive ? Slope.ID_OBL_INT_POS_NE : Slope.ID_OBL_INT_NEG_NE;
                    break;
                case CORNER_NW:
                    slopeID = isPositive ? Slope.ID_OBL_INT_POS_NW : Slope.ID_OBL_INT_NEG_NW;
                    break;
                case CORNER_SW:
                    slopeID = isPositive ? Slope.ID_OBL_INT_POS_SW : Slope.ID_OBL_INT_NEG_SW;
                    break;
                }

                break;
            case META_OBLIQUE_EXT:

                switch (corner) {
                case CORNER_SE:
                    slopeID = isPositive ? Slope.ID_OBL_EXT_POS_SE : Slope.ID_OBL_EXT_NEG_SE;
                    break;
                case CORNER_NE:
                    slopeID = isPositive ? Slope.ID_OBL_EXT_POS_NE : Slope.ID_OBL_EXT_NEG_NE;
                    break;
                case CORNER_NW:
                    slopeID = isPositive ? Slope.ID_OBL_EXT_POS_NW : Slope.ID_OBL_EXT_NEG_NW;
                    break;
                case CORNER_SW:
                    slopeID = isPositive ? Slope.ID_OBL_EXT_POS_SW : Slope.ID_OBL_EXT_NEG_SW;
                    break;
                }

                break;
            case META_PRISM:

                if (isPositive) {

                    slopeID = Slope.ID_PRISM_POS;

                    if (!entityLiving.isSneaking()) {
                        slopeID = SlopeTransform.transformPrism(world, slopeID, blockPos);
                        cbTileEntity.setData(slopeID);
                        SlopeTransform.transformAdjacentPrisms(world, blockPos);
                    }

                } else {

                    slopeID = Slope.ID_PRISM_NEG;

                }

                break;
            case META_PRISM_SLOPE:

                switch (dir) {
                case NORTH:
                    slopeID = Slope.ID_PRISM_WEDGE_POS_S;
                    break;
                case SOUTH:
                    slopeID = Slope.ID_PRISM_WEDGE_POS_N;
                    break;
                case WEST:
                    slopeID = Slope.ID_PRISM_WEDGE_POS_E;
                    break;
                case EAST:
                    slopeID = Slope.ID_PRISM_WEDGE_POS_W;
                    break;
                default: {}
                }

                break;
            }

            cbTileEntity.setData(slopeID);

        }
    }*/

    @Override
    public EnumFacing[] getValidRotations(World world, BlockPos blockPos) {
        return new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN };
    }

    @Override
    public boolean rotateBlock(World world, BlockPos blockPos, EnumFacing facing) {
        CbTileEntity cbTileEntity = getTileEntity(world, blockPos);
        if (cbTileEntity != null) {
        	SlopeData.rotate(cbTileEntity, facing.getAxis());
        	return true;
        }
        return false;
    }
    
    /**
     * Check if the face of a block should block rendering.
     *
     * Faces which are fully opaque should return true, faces with transparency
     * or faces which do not span the full size of the block should return false.
     *
     * @param state The current block state
     * @param world The current world
     * @param pos Block position in world
     * @param face The side to check
     * @return True if the block is opaque on the specified side.
     */
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

}
