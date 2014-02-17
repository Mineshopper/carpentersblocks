package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.Type;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import carpentersblocks.util.registry.ItemRegistry;
import carpentersblocks.util.slope.SlopeTransform;
import carpentersblocks.util.slope.SlopeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersSlope extends BlockCoverable {
    
    public final static String slopeType[] = { "wedge", "obliqueInterior", "obliqueExterior", "prism", "prismWedge" };
    
    public final static int META_WEDGE       = 0;
    public final static int META_OBLIQUE_INT = 1;
    public final static int META_OBLIQUE_EXT = 2;
    public final static int META_PRISM       = 3;
    public final static int META_PRISM_SLOPE = 4;
    
    private boolean rayTracing;
    
    public BlockCarpentersSlope(Material material)
    {
        super(material);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_oblique_int_pos = iconRegister.registerIcon("carpentersblocks:slope/oblique_int_pos");
        IconRegistry.icon_oblique_int_neg = iconRegister.registerIcon("carpentersblocks:slope/oblique_int_neg");
        IconRegistry.icon_oblique_ext_pos = iconRegister.registerIcon("carpentersblocks:slope/oblique_ext_pos");
        IconRegistry.icon_oblique_ext_neg = iconRegister.registerIcon("carpentersblocks:slope/oblique_ext_neg");
        
        super.registerBlockIcons(iconRegister);
    }
    
    @Override
    /**
     * Alters block direction.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int slopeID = BlockProperties.getData(TE);
        Slope slope = Slope.slopesList[slopeID];
        
        /* Cycle between slope types based on current slope. */
        
        switch (slope.type)
        {
            case WEDGE_SIDE:
                if (++slopeID > Slope.ID_WEDGE_SW) {
                    slopeID = Slope.ID_WEDGE_SE;
                }
                break;
            case WEDGE:
                if (slope.isPositive) {
                    if (++slopeID > Slope.ID_WEDGE_POS_E) {
                        slopeID = Slope.ID_WEDGE_POS_N;
                    }
                } else {
                    if (++slopeID > Slope.ID_WEDGE_NEG_E) {
                        slopeID = Slope.ID_WEDGE_NEG_N;
                    }
                }
                break;
            case WEDGE_INT:
                if (slope.isPositive) {
                    if ((slopeID += 2) > Slope.ID_WEDGE_INT_POS_SE) {
                        slopeID = Slope.ID_WEDGE_INT_POS_NE;
                    }
                } else {
                    if ((slopeID += 2) > Slope.ID_WEDGE_INT_NEG_SE) {
                        slopeID = Slope.ID_WEDGE_INT_NEG_NE;
                    }
                }
                break;
            case WEDGE_EXT:
                if (slope.isPositive) {
                    if ((slopeID += 2) > Slope.ID_WEDGE_EXT_POS_NW) {
                        slopeID = Slope.ID_WEDGE_EXT_POS_SW;
                    }
                } else {
                    if ((slopeID += 2) > Slope.ID_WEDGE_EXT_NEG_NW) {
                        slopeID = Slope.ID_WEDGE_EXT_NEG_SW;
                    }
                }
                break;
            case OBLIQUE_INT:
                if (slope.isPositive) {
                    if ((slopeID += 2) > Slope.ID_OBL_INT_POS_SE) {
                        slopeID = Slope.ID_OBL_INT_POS_NE;
                    }
                } else {
                    if ((slopeID += 2) > Slope.ID_OBL_INT_NEG_SE) {
                        slopeID = Slope.ID_OBL_INT_NEG_NE;
                    }
                }
                break;
            case OBLIQUE_EXT:
                if (slope.isPositive) {
                    if ((slopeID += 2) > Slope.ID_OBL_EXT_POS_NW) {
                        slopeID = Slope.ID_OBL_EXT_POS_SW;
                    }
                } else {
                    if ((slopeID += 2) > Slope.ID_OBL_EXT_NEG_NW) {
                        slopeID = Slope.ID_OBL_EXT_NEG_SW;
                    }
                }
                break;
            case PRISM_1P:
                if ((slopeID += 1) > Slope.ID_PRISM_1P_POS_E) {
                    slopeID = Slope.ID_PRISM_1P_POS_N;
                }
                break;
            case PRISM_2P:
                if (slope.equals(Slope.PRISM_2P_POS_NS)) {
                    slopeID = Slope.ID_PRISM_2P_POS_WE;
                } else if (slope.equals(Slope.PRISM_2P_POS_WE)) {
                    slopeID = Slope.ID_PRISM_2P_POS_NS;
                } else if ((slopeID += 1) > Slope.ID_PRISM_2P_POS_SW) {
                    slopeID = Slope.ID_PRISM_2P_POS_SE;
                }
                break;
            case PRISM_3P:
                if ((slopeID += 1) > Slope.ID_PRISM_3P_POS_NSE) {
                    slopeID = Slope.ID_PRISM_3P_POS_NWE;
                }
                break;
            case PRISM_4P:
                break;
            case PRISM_WEDGE:
                if ((slopeID += 1) > Slope.ID_PRISM_WEDGE_POS_E) {
                    slopeID = Slope.ID_PRISM_WEDGE_POS_N;
                }
                break;
            default: {}
        }
        
        BlockProperties.setData(TE, slopeID);
        
        return true;
    }
    
    @Override
    /**
     * Alters block type.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int slopeID = BlockProperties.getData(TE);
        Slope slope = Slope.slopesList[slopeID];
        
        /* Transform slope to next type. */
        
        switch (slope.type)
        {
            case WEDGE_SIDE:
                slopeID = Slope.ID_WEDGE_POS_N;
                break;
            case WEDGE:
                if (slope.isPositive) {
                    slopeID -= 4;
                } else {
                    slopeID = Slope.ID_WEDGE_INT_POS_NE;
                }
                break;
            case WEDGE_INT:
                if (slope.isPositive) {
                    slopeID += 1;
                } else {
                    if (slopeID == Slope.ID_WEDGE_INT_NEG_NE || slopeID == Slope.ID_WEDGE_INT_NEG_NW) {
                        slopeID += 11;
                    } else {
                        slopeID +=3;
                    }
                }
                break;
            case WEDGE_EXT:
                if (slope.isPositive) {
                    slopeID += 1;
                } else {
                    if (slopeID == Slope.ID_WEDGE_EXT_NEG_SW || slopeID == Slope.ID_WEDGE_EXT_NEG_SE) {
                        slopeID += 11;
                    } else {
                        slopeID += 3;
                    }
                }
                break;
            case OBLIQUE_INT:
                if (slope.isPositive) {
                    slopeID += 1;
                } else {
                    if (slopeID == Slope.ID_OBL_INT_NEG_NE || slopeID == Slope.ID_OBL_INT_NEG_NW) {
                        slopeID += 11;
                    } else {
                        slopeID += 3;
                    }
                }
                break;
            case OBLIQUE_EXT:
                if (slope.isPositive) {
                    slopeID += 1;
                } else {
                    slopeID = Slope.ID_PRISM_POS;
                }
                break;
            case PRISM:
                if (slope.isPositive) {
                    slopeID = Slope.ID_PRISM_NEG;
                } else {
                    slopeID = Slope.ID_PRISM_1P_POS_N;
                }
                break;
            case PRISM_1P:
                slopeID = Slope.ID_PRISM_2P_POS_NS;
                break;
            case PRISM_2P:
                if (slope.equals(Slope.PRISM_2P_POS_NS) || slope.equals(Slope.PRISM_2P_POS_WE)) {
                    slopeID = Slope.ID_PRISM_2P_POS_SE;
                } else {
                    slopeID = Slope.ID_PRISM_3P_POS_NWE;
                }
                break;
            case PRISM_3P:
                slopeID = Slope.ID_PRISM_POS_4P;
                break;
            case PRISM_4P:
                slopeID = Slope.ID_PRISM_WEDGE_POS_N;
                break;
            case PRISM_WEDGE:
                slopeID = Slope.ID_WEDGE_SE;
                break;
            default: {}
        }
        
        BlockProperties.setData(TE, slopeID);
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * Returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
    {
        list.add(new ItemStack(item, 1, META_WEDGE      ));
        list.add(new ItemStack(item, 1, META_OBLIQUE_INT));
        list.add(new ItemStack(item, 1, META_OBLIQUE_EXT));
        list.add(new ItemStack(item, 1, META_PRISM      ));
        list.add(new ItemStack(item, 1, META_PRISM_SLOPE));
    }
    
    @Override
    /**
     * Damages hammer with a chance to not damage.
     */
    protected void damageItemWithChance(World world, EntityPlayer entityPlayer)
    {
        if (world.rand.nextFloat() <= ItemRegistry.itemHammerDamageChanceFromSlopes) {
            super.damageItemWithChance(world, entityPlayer);
        }
    }
    
    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        if (!rayTracing) {
            
            TEBase TE = (TEBase) world.getTileEntity(x, y, z);
            
            int slopeID = BlockProperties.getData(TE);
            Slope slope = Slope.slopesList[slopeID];
            
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
    
    @Override
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        
        MovingObjectPosition finalTrace = null;
        
        Slope slope = Slope.slopesList[BlockProperties.getData(TE)];
        SlopeUtil slopeUtil = new SlopeUtil();
        
        int numPasses = slopeUtil.getNumPasses(slope);
        int precision = slopeUtil.getNumBoxesPerPass(slope);
        
        rayTracing = true;
        
        /* Determine if ray trace is a hit on slope. */
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
        
        /* Determine true face hit since sloped faces are two or more shared faces. */
        
        if (finalTrace != null) {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            finalTrace = super.collisionRayTrace(world, x, y, z, startVec, endVec);
        }
        
        return finalTrace;
    }
    
    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        
        AxisAlignedBB box = null;
        
        Slope slope = Slope.slopesList[BlockProperties.getData(TE)];
        SlopeUtil slopeUtil = new SlopeUtil();
        
        int precision = slopeUtil.getNumBoxesPerPass(slope);
        int numPasses = slopeUtil.getNumPasses(slope);
        
        for (int pass = 0; pass < numPasses; ++pass)
        {
            for (int slice = 0; slice < precision; ++slice)
            {
                float[] dim = slopeUtil.genBounds(slope, slice, precision, pass);
                
                if (dim != null) {
                    box = AxisAlignedBB.getAABBPool().getAABB(x + dim[0], y + dim[1], z + dim[2], x + dim[3], y + dim[4], z + dim[5]);
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
    
    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     */
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(world, x, y, z);
        
        if (TE != null) {
            if (isBlockSolid(world, x, y, z)) {
                return Slope.slopesList[BlockProperties.getData(TE)].isFaceFull(side);
            }
        }
        
        return false;
    }
    
    @Override
    /**
     * Returns whether sides share faces based on sloping property and face shape.
     */
    protected boolean shareFaces(TEBase TE_adj, TEBase TE_src, ForgeDirection side_adj, ForgeDirection side_src)
    {
        if (TE_adj.getBlockType() == this)
        {
            Slope slope_src = Slope.slopesList[BlockProperties.getData(TE_src)];
            Slope slope_adj = Slope.slopesList[BlockProperties.getData(TE_adj)];
            
            if (!slope_adj.hasSide(side_adj)) {
                return false;
            } else if (slope_src.getFaceBias(side_src) == slope_adj.getFaceBias(side_adj)) {
                return true;
            } else {
                return false;
            }
        }
        
        return super.shareFaces(TE_adj, TE_src, side_adj, side_src);
    }
    
    @Override
    /**
     * Called when block is placed in world.
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        EventHandler.eventFace = side;
        EventHandler.hitX = hitX;
        EventHandler.hitY = hitY;
        EventHandler.hitZ = hitZ;
        
        return metadata;
    }
    
    /**
     * Returns wedge slope orientation based on side clicked and hit coordinates.
     */
    private int getWedgeOrientation(ForgeDirection dir, int side, double hitX, double hitY, double hitZ)
    {
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
        
    }
    
    private final int CORNER_SE = 0;
    private final int CORNER_NE = 1;
    private final int CORNER_NW = 2;
    private final int CORNER_SW = 3;
    
    /**
     * Returns general slope orientation based on side clicked and hit coordinates.
     */
    private int getCorner(float rotationYaw, int side, double hitX, double hitY, double hitZ)
    {
        int corner;
        
        if (90 <= rotationYaw && rotationYaw < 180) {
            corner = CORNER_SE;
        } else if (180 <= rotationYaw && rotationYaw < 270) {
            corner = CORNER_SW;
        } else if (270 <= rotationYaw && rotationYaw < 360) {
            corner = CORNER_NW;
        } else {
            corner = CORNER_NE;
        }
        
        return corner;
    }
    
    @Override
    /**
     * Called when the block is placed in the world.
     * Uses cardinal direction to adjust metadata if player clicks top or bottom face of block.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getTileEntity(x, y, z);
        
        int slopeID = 0;
        int metadata = world.getBlockMetadata(x, y, z);
        
        boolean isPositive = EventHandler.eventFace > 1 && EventHandler.hitY < 0.5F || EventHandler.eventFace == 1;
        int corner = getCorner(entityLiving.rotationYaw, EventHandler.eventFace, EventHandler.hitX, EventHandler.hitY, EventHandler.hitZ);
        
        ForgeDirection dir = ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[BlockProperties.getDirectionFromFacing(BlockProperties.getOppositeFacing(entityLiving)).ordinal()]);
        
        switch (metadata) {
            case META_WEDGE:
                
                slopeID = getWedgeOrientation(dir, EventHandler.eventFace, EventHandler.hitX, EventHandler.hitY, EventHandler.hitZ);
                
                if (!entityLiving.isSneaking()) {
                    slopeID = SlopeTransform.transformWedge(world, slopeID, x, y, z);
                    BlockProperties.setData(TE, slopeID);
                    SlopeTransform.transformAdjacentWedges(world, slopeID, x, y, z);
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
                        slopeID = SlopeTransform.transformPrism(world, slopeID, x, y, z);
                        BlockProperties.setData(TE, slopeID);
                        SlopeTransform.transformAdjacentPrisms(world, x, y, z);
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
        
        BlockProperties.setData(TE, slopeID);
        
        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }
    
    @Override
    public boolean canCoverSide(TEBase TE, World world, int x, int y, int z, int side)
    {
        return isSideSolid(world, x, y, z, ForgeDirection.getOrientation(side));
    }
    
    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersSlopeRenderID;
    }
    
}
