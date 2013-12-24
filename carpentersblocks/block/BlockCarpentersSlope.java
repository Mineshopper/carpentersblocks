package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.Type;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import carpentersblocks.util.slope.SlopeTransform;
import carpentersblocks.util.slope.SlopeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersSlope extends BlockBase {

	private boolean rayTracing;

	public BlockCarpentersSlope(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersSlope");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTextureName("carpentersblocks:general/full_frame");
	}

	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	public void registerIcons(IconRegister iconRegister)
	{
		IconRegistry.icon_slope_oblique_pt_low = iconRegister.registerIcon("carpentersblocks:slope/oblique_pt_low");
		IconRegistry.icon_slope_oblique_pt_high = iconRegister.registerIcon("carpentersblocks:slope/oblique_pt_high");

		super.registerIcons(iconRegister);
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
		case WEDGE_XZ:
			if (++slopeID > Slope.ID_WEDGE_SW) {
				slopeID = Slope.ID_WEDGE_SE;
			}
			break;
		case WEDGE_Y:
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
		case PYRAMID:
			slopeID = slope.equals(Slope.PYR_HALF_POS) ? Slope.PYR_HALF_NEG.slopeID : Slope.PYR_HALF_POS.slopeID;
			break;
		case PRISM_1P:
			if ((slopeID += 1) > Slope.ID_PRISM_1P_E) {
				slopeID = Slope.ID_PRISM_1P_N;
			}
			break;
		case PRISM_2P:
			if (slope.equals(Slope.PRISM_2P_NS)) {
				slopeID = Slope.ID_PRISM_2P_WE;
			} else if (slope.equals(Slope.PRISM_2P_WE)) {
				slopeID = Slope.ID_PRISM_2P_NS;
			} else if ((slopeID += 1) > Slope.ID_PRISM_2P_SW) {
				slopeID = Slope.ID_PRISM_2P_SE;
			}
			break;
		case PRISM_3P:
			if ((slopeID += 1) > Slope.ID_PRISM_3P_NSE) {
				slopeID = Slope.ID_PRISM_3P_WEN;
			}
			break;
		case PRISM_4P:
			break;
		case PRISM_SLOPE:
			if ((slopeID += 1) > Slope.ID_PRISM_SLOPE_E) {
				slopeID = Slope.ID_PRISM_SLOPE_N;
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
	protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitZ)
	{
		int slopeID = BlockProperties.getData(TE);
		Slope slope = Slope.slopesList[slopeID];

		/* Transform slope to next type. */

		switch (slope.type)
		{
		case WEDGE_XZ:
			slopeID = Slope.ID_WEDGE_POS_N;
			break;
		case WEDGE_Y:
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
				slopeID = Slope.ID_PYR_HALF_POS;
			}
			break;
		case PYRAMID:
			slopeID = Slope.ID_PRISM_1P_N;
			break;
		case PRISM_1P:
			slopeID = Slope.ID_PRISM_2P_NS;
			break;
		case PRISM_2P:
			if (slope.equals(Slope.PRISM_2P_NS) || slope.equals(Slope.PRISM_2P_WE)) {
				slopeID = Slope.ID_PRISM_2P_SE;
			} else {
				slopeID = Slope.ID_PRISM_3P_WEN;
			}
			break;
		case PRISM_3P:
			slopeID = Slope.ID_PRISM_4P;
			break;
		case PRISM_4P:
			slopeID = Slope.ID_PRISM_SLOPE_N;
			break;
		case PRISM_SLOPE:
			slopeID = Slope.ID_WEDGE_SE;
			break;
		default: {}
		}

		BlockProperties.setData(TE, slopeID);

		return true;
	}

	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		if (!rayTracing) {

			TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

			int slopeID = BlockProperties.getData(TE);
			Slope slope = Slope.slopesList[slopeID];

			switch (slope.type) {
			case PYRAMID:
				if (slope.isPositive) {
					setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
				} else {
					setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
				}
				break;
			case PRISM_1P:
			case PRISM_2P:
			case PRISM_3P:
			case PRISM_4P:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
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
		TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

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
		AxisAlignedBB box = null;

		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

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
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		if (isBlockSolid(world, x, y, z)) {
			return Slope.slopesList[BlockProperties.getData(TE)].isFaceFull(side);
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
			} else if (slope_src.faceBias(side_src) == slope_adj.faceBias(side_adj)) {
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
	 * Sets slope angle depending on click coordinates on block face.
	 *
	 *	Metadata values:
	 * 	 0 - 11	-	Identifies slope angle in x, y, z space.
	 * 	12 - 13	-	Top or bottom side of block clicked.  onBlockPlacedBy() determines
	 * 				direction and sets interpolated value from 0 - 11.
	 */
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
	{
		// Normalize face coordinates
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

		if (side > 1) {
			if (hitY > 0.5F && hitX > 1.0F - hitY && hitX < hitY) {
				return side + 2;
			} else if (hitY < 0.5F && hitX < 1.0F - hitY && hitX > hitY) {
				return side + 6;
			} else if (hitX < 0.2F) {
				return side == 2 ? 1 : side == 3 ? 0 : side == 4 ? 3 : 2;
			} else if (hitX > 0.8F){
				return side == 2 ? 2 : side == 3 ? 3 : side == 4 ? 1 : 0;
			} else if (hitY > 0.5F) {
				return side + 2;
			} else { // hitY < 0.5F
				return side + 6;
			}
		} else {
			return side + 12;
		}
	}

	@Override
	/**
	 * Called when the block is placed in the world.
	 * Uses cardinal direction to adjust metadata if player clicks top or bottom face of block.
	 */
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		BlockProperties.setData(TE, world.getBlockMetadata(x, y, z));
		int slopeID = BlockProperties.getData(TE);

		if (slopeID > 11)
		{
			switch (facing) {
			case 0:
				slopeID = slopeID == 12 ? Slope.ID_WEDGE_NEG_N : Slope.ID_WEDGE_POS_N;
				break;
			case 1:
				slopeID = slopeID == 12 ? Slope.ID_WEDGE_NEG_E : Slope.ID_WEDGE_POS_E;
				break;
			case 2:
				slopeID = slopeID == 12 ? Slope.ID_WEDGE_NEG_S : Slope.ID_WEDGE_POS_S;
				break;
			case 3:
				slopeID = slopeID == 12 ? Slope.ID_WEDGE_NEG_W : Slope.ID_WEDGE_POS_W;
				break;
			}
		}

		BlockProperties.setData(TE, slopeID);

		/* If shift key is down, skip auto-orientation. */
		if (!entityLiving.isSneaking()) {
			SlopeTransform slopeTransform = new SlopeTransform(TE);
			slopeTransform.begin();
		}

		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
	}

	@Override
	public boolean canCoverSide(TEBase TE, World world, int x, int y, int z, int side)
	{
		return isBlockSolidOnSide(world, x, y, z, ForgeDirection.getOrientation(side));
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