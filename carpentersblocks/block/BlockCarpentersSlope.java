package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.SlopeType;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.FeatureHandler;

public class BlockCarpentersSlope extends BlockBase
{

	private final int hitboxPrecision = FeatureHandler.hitboxPrecision;

	public BlockCarpentersSlope(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersSlope");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		func_111022_d("carpentersblocks:slope/slope");
	}

	@Override
	/**
	 * Alters block direction.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int slopeID = BlockProperties.getData(TE);
		Slope slope = Slope.slopesList[slopeID];

		/*
		 * Cycle between slope types based on current slope
		 */
		switch (slope.slopeType)
		{
		case WEDGE_XZ:
			if (++slopeID > 3)
				slopeID = 0;
			break;
		case WEDGE_Y:
			if (slope.isPositive) {
				if (++slopeID > 11)
					slopeID = 8;
			} else {
				if (++slopeID > 7)
					slopeID = 4;
			}
			break;
		case WEDGE_INT:
			if (slope.isPositive) {
				if ((slopeID += 2) > 18)
					slopeID = 12;
			} else {
				if ((slopeID += 2) > 19)
					slopeID = 13;
			}
			break;
		case WEDGE_EXT:
			if (slope.isPositive) {
				if ((slopeID += 2) > 26)
					slopeID = 20;
			} else {
				if ((slopeID += 2) > 27)
					slopeID = 21;
			}
			break;
		case OBLIQUE_INT:
			if (slope.isPositive) {
				if ((slopeID += 2) > 34)
					slopeID = 28;
			} else {
				if ((slopeID += 2) > 35)
					slopeID = 29;
			}
			break;
		case OBLIQUE_EXT:
			if (slope.isPositive) {
				if ((slopeID += 2) > 42)
					slopeID = 36;
			} else {
				if ((slopeID += 2) > 43)
					slopeID = 37;
			}
			break;
		case PYRAMID:
			slopeID = slope.equals(Slope.PYR_HALF_POS) ? Slope.PYR_HALF_NEG.slopeID : Slope.PYR_HALF_POS.slopeID;
			break;
		}

		BlockProperties.setData(TE, slopeID);

		return true;
	}

	@Override
	/**
	 * Alters block type.
	 */
	protected boolean onHammerRightClick(TECarpentersBlock TE, EntityPlayer entityPlayer, int side)
	{
		int slopeID = BlockProperties.getData(TE);
		Slope slope = Slope.slopesList[slopeID];

		/*
		 * Transform slope to next type
		 */
		switch (slope.slopeType)
		{
		case WEDGE_XZ:
			slopeID = 8;
			break;
		case WEDGE_Y:
			if (slope.isPositive) {
				slopeID -= 4;
			} else {
				slopeID = 12;
			}
			break;
		case WEDGE_INT:
			if (slope.isPositive) {
				slopeID += 1;
			} else {
				if (slopeID == 13 || slopeID == 15) {
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
				if (slopeID == 21 || slopeID == 23) {
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
				if (slopeID == 29 || slopeID == 31) {
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
				slopeID = 44;
			}
			break;
		case PYRAMID:
			slopeID = 0;
			break;
		}

		BlockProperties.setData(TE, slopeID);

		return true;
	}

	/**
	 * Will return slope boundaries for all slopes
	 */
	private float[] genBounds(Slope slope, int slice, int precision, int pass)
	{
		++pass;

		// For oblique exterior corners
		float zeroPassOffset = (float) (pass - 1) / getNumPasses(slope);
		float onePassOffset = (float) pass / getNumPasses(slope);

		// Includes 0.0F -> 0.99_F
		float zeroOffset = (float) slice / (float) precision;

		// Includes 0.01_F -> 1.0F
		float oneOffset = (float) (slice + 1) / (float) precision;

		switch (slope.slopeID)
		{
		case Slope.ID_WEDGE_NW:
			return new float[] { zeroOffset, 0.0F, 1.0F - oneOffset, 1.0F, 1.0F, 1.0F };
		case Slope.ID_WEDGE_SW:
			return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, 1.0F, oneOffset };
		case Slope.ID_WEDGE_NE:
			return new float[] { 0.0F, 0.0F, zeroOffset, oneOffset, 1.0F, 1.0F };
		case Slope.ID_WEDGE_SE:
			return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F, 1.0F - zeroOffset };
		case Slope.ID_WEDGE_POS_N:
			return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
		case Slope.ID_WEDGE_POS_W:
			return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
		case Slope.ID_WEDGE_POS_E:
			return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
		case Slope.ID_WEDGE_POS_S:
			return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
		case Slope.ID_WEDGE_NEG_N:
			return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
		case Slope.ID_WEDGE_NEG_W:
			return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
		case Slope.ID_WEDGE_NEG_E:
			return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
		case Slope.ID_WEDGE_NEG_S:
			return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
		case Slope.ID_WEDGE_INT_POS_NW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
			}
		case Slope.ID_WEDGE_INT_POS_SW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
			}
		case Slope.ID_WEDGE_INT_POS_NE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
			}
		case Slope.ID_WEDGE_INT_POS_SE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
			}
		case Slope.ID_WEDGE_INT_NEG_NW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
			}
		case Slope.ID_WEDGE_INT_NEG_SW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
			}
		case Slope.ID_WEDGE_INT_NEG_NE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
			}
		case Slope.ID_WEDGE_INT_NEG_SE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
			}
		case Slope.ID_WEDGE_EXT_POS_NW:
			return new float[] { zeroOffset, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
		case Slope.ID_WEDGE_EXT_POS_SW:
			return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F - zeroOffset };
		case Slope.ID_WEDGE_EXT_POS_NE:
			return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F - zeroOffset, oneOffset, 1.0F };
		case Slope.ID_WEDGE_EXT_POS_SE:
			return new float[] { 0.0F, 0.0F, 0.0F, 1.0F - zeroOffset, oneOffset, 1.0F - zeroOffset };
		case Slope.ID_WEDGE_EXT_NEG_NW:
			return new float[] { zeroOffset, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
		case Slope.ID_WEDGE_EXT_NEG_SW:
			return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F - zeroOffset };
		case Slope.ID_WEDGE_EXT_NEG_NE:
			return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F - zeroOffset, 1.0F, 1.0F };
		case Slope.ID_WEDGE_EXT_NEG_SE:
			return new float[] { 0.0F, 1.0F - oneOffset, 0.0F, 1.0F - zeroOffset, 1.0F, 1.0F - zeroOffset };
		case Slope.ID_OBL_EXT_POS_NW:
			return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 0.0F, 1.0F - oneOffset * (1.0F - zeroPassOffset), 1.0F, onePassOffset, 1.0F };
		case Slope.ID_OBL_EXT_POS_SW:
			return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 0.0F, 0.0F, 1.0F, onePassOffset, oneOffset * (1.0F - zeroPassOffset) };
		case Slope.ID_OBL_EXT_POS_NE:
			return new float[] { 0.0F, 0.0F, zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), oneOffset * (1.0F - zeroPassOffset), onePassOffset, 1.0F };
		case Slope.ID_OBL_EXT_POS_SE:
			return new float[] { 0.0F, 0.0F, 0.0F, oneOffset * (1.0F - zeroPassOffset), onePassOffset, 1.0F - zeroPassOffset - zeroOffset * (1.0F - zeroPassOffset), };
		case Slope.ID_OBL_INT_POS_NW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 0.0F, 1.0F - oneOffset, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
			}
		case Slope.ID_OBL_INT_POS_SW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, 1.0F, oneOffset };
			case 2:
				return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
			}
		case Slope.ID_OBL_INT_POS_NE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, 0.0F, zeroOffset, oneOffset, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
			}
		case Slope.ID_OBL_INT_POS_SE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F, 1.0F - zeroOffset };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
			}
		case Slope.ID_OBL_EXT_NEG_NW:
			return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 1.0F - onePassOffset, 1.0F - oneOffset * (1.0F - zeroPassOffset), 1.0F, 1.0F, 1.0F };
		case Slope.ID_OBL_EXT_NEG_SW:
			return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 1.0F - onePassOffset, 0.0F, 1.0F, 1.0F, oneOffset * (1.0F - zeroPassOffset) };
		case Slope.ID_OBL_EXT_NEG_NE:
			return new float[] { 0.0F, 1.0F - onePassOffset, zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), oneOffset * (1.0F - zeroPassOffset), 1.0F, 1.0F };
		case Slope.ID_OBL_EXT_NEG_SE:
			return new float[] { 0.0F, 1.0F - onePassOffset, 0.0F, oneOffset * (1.0F - zeroPassOffset), 1.0F, 1.0F - zeroPassOffset - zeroOffset * (1.0F - zeroPassOffset) };
		case Slope.ID_OBL_INT_NEG_NW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 0.0F, 1.0F - oneOffset, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
			}
		case Slope.ID_OBL_INT_NEG_SW:
			switch (pass) {
			case 1:
				return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, 1.0F, oneOffset };
			case 2:
				return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
			}
		case Slope.ID_OBL_INT_NEG_NE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, 0.0F, zeroOffset, oneOffset, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
			}
		case Slope.ID_OBL_INT_NEG_SE:
			switch (pass) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F, 1.0F - zeroOffset };
			case 2:
				return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
			}
		case Slope.ID_PYR_HALF_POS:
			return new float[] { (0.5F * zeroOffset), 0.0F, (0.5F * zeroOffset), 1.0F - (0.5F * zeroOffset), oneOffset * 0.5F, 1.0F - (0.5F * zeroOffset) };
		default: //Slope.ID_NEG_PYR:
			return new float[] { (0.5F * zeroOffset), 1.0F - (oneOffset * 0.5F), (0.5F * zeroOffset), 1.0F - (0.5F * zeroOffset), 1.0F, 1.0F - (0.5F * zeroOffset) };
		}
	}

	/**
	 * Return number of boxes that need to be constructed for slope per pass.
	 */
	private int getNumBoxesPerPass(Slope slope)
	{
		switch (slope.slopeType)
		{
		case PYRAMID:
			return hitboxPrecision / 2;
		default:
			return hitboxPrecision;
		}
	}

	/**
	 * Return number of passes required for slope box generation.
	 */
	private int getNumPasses(Slope slope)
	{
		switch (slope.slopeType)
		{
		case OBLIQUE_EXT:
			return getNumBoxesPerPass(slope);
		case OBLIQUE_INT:
			return 3;
		case WEDGE_INT:
			return 2;
		default:
			return 1;
		}
	}

	@Override
	/**
	 * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
	 * x, y, z, startVec, endVec
	 */
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		MovingObjectPosition finalTrace = null;

		int slopeID = BlockProperties.getData(TE);
		Slope slope = Slope.slopesList[slopeID];

		int precision = getNumBoxesPerPass(slope);
		int numPasses = getNumPasses(slope);

		double currDist = 0.0D;
		double maxDist = 0.0D;

		// Determine if ray trace is a hit on slope
		for (int pass = 0; pass < numPasses; ++pass)
		{
			for (int slice = 0; slice < precision; ++slice)
			{
				float[] box = genBounds(slope, slice, precision, pass);
				setBlockBounds(box[0], box[1], box[2], box[3], box[4], box[5]);
				MovingObjectPosition traceResult = super.collisionRayTrace(world, x, y, z, startVec, endVec);

				if (traceResult != null)
				{
					currDist = traceResult.hitVec.squareDistanceTo(endVec);
					if (currDist > maxDist) {
						finalTrace = traceResult;
						maxDist = currDist;
					}
				}
			}
			if (slope.slopeType.equals(SlopeType.OBLIQUE_EXT)) {
				--precision;
			}
		}

		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

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

		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		int slopeID = BlockProperties.getData(TE);
		Slope slope = Slope.slopesList[slopeID];

		int precision = getNumBoxesPerPass(slope);
		int numPasses = getNumPasses(slope);

		// Construct bounding area for entity collision
		for (int pass = 0; pass < numPasses; ++pass) {
			for (int slice = 0; slice < precision; ++slice) {
				float[] dim = genBounds(slope, slice, precision, pass);

				if (dim != null)
					box = AxisAlignedBB.getAABBPool().getAABB(x + dim[0], y + dim[1], z + dim[2], x + dim[3], y + dim[4], z + dim[5]);

				if (box != null && axisAlignedBB.intersectsWith(box)) {
					box.contract(0.11D, 0.11D, 0.11D);
					list.add(box);
				}
			}

			if (slope.slopeType.equals(SlopeType.OBLIQUE_EXT))
				--precision;
		}
	}

	@Override
	/**
	 * Checks if the block is a solid face on the given side, used by placement logic.
	 */
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		TECarpentersBlock TE = (TECarpentersBlock) world.getBlockTileEntity(x, y, z);

		if (isBlockSolid(world, x, y, z))
			return Slope.slopesList[BlockProperties.getData(TE)].isFaceFull(side);

		return false;
	}

	@Override
	/**
	 * Returns whether sides share faces based on sloping property and face shape.
	 */
	protected boolean shareFaces(TECarpentersBlock TE_adj, TECarpentersBlock TE_src, ForgeDirection side_adj, ForgeDirection side_src)
	{
		if (TE_adj.getBlockType() == this)
		{
			Slope slope_src = Slope.slopesList[BlockProperties.getData(TE_src)];
			Slope slope_adj = Slope.slopesList[BlockProperties.getData(TE_adj)];

			if (!slope_adj.hasSide(side_adj))
				return false;
			else if (slope_src.wedgeOrientation(side_src) == slope_adj.wedgeOrientation(side_adj))
				return true;
			else
				return false;
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
			if ((hitY > 0.5F) && (hitX > (1.0F - hitY)) && (hitX < hitY)) {
				return side + 2;
			} else if (hitY < 0.5F && hitX < (1.0F - hitY) && hitX > hitY) {
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
	public void auxiliaryOnBlockPlacedBy(TECarpentersBlock TE, World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
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

		/* If shift key is down, skip auto-orientation. */
		if (!entityLiving.isSneaking())
		{
			Slope slope = Slope.slopesList[slopeID];

			TECarpentersBlock TE_XN = world.getBlockId(x - 1, y, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x - 1, y, z) : null;
			TECarpentersBlock TE_XP = world.getBlockId(x + 1, y, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x + 1, y, z) : null;
			TECarpentersBlock TE_YP = world.getBlockId(x, y + 1, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y + 1, z) : null;
			TECarpentersBlock TE_YN = world.getBlockId(x, y - 1, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y - 1, z) : null;
			TECarpentersBlock TE_ZN = world.getBlockId(x, y, z - 1) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z - 1) : null;
			TECarpentersBlock TE_ZP = world.getBlockId(x, y, z + 1) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y, z + 1) : null;
			TECarpentersBlock TE_XYNN = world.getBlockId(x - 1, y - 1, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x - 1, y - 1, z) : null;
			TECarpentersBlock TE_XYPN = world.getBlockId(x + 1, y - 1, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x + 1, y - 1, z) : null;
			TECarpentersBlock TE_YZNN = world.getBlockId(x, y - 1, z - 1) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y - 1, z - 1) : null;
			TECarpentersBlock TE_YZNP = world.getBlockId(x, y - 1, z + 1) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y - 1, z + 1) : null;
			TECarpentersBlock TE_XYNP = world.getBlockId(x - 1, y + 1, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x - 1, y + 1, z) : null;
			TECarpentersBlock TE_XYPP = world.getBlockId(x + 1, y + 1, z) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x + 1, y + 1, z) : null;
			TECarpentersBlock TE_YZPN = world.getBlockId(x, y + 1, z - 1) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y + 1, z - 1) : null;
			TECarpentersBlock TE_YZPP = world.getBlockId(x, y + 1, z + 1) == blockID ? (TECarpentersBlock)world.getBlockTileEntity(x, y + 1, z + 1) : null;

			/* Gather neighboring slopes */

			Slope slope_XN = TE_XN != null ? Slope.slopesList[BlockProperties.getData(TE_XN)] : (Slope)null;
			Slope slope_XP = TE_XP != null ? Slope.slopesList[BlockProperties.getData(TE_XP)] : (Slope)null;
			Slope slope_ZN = TE_ZN != null ? Slope.slopesList[BlockProperties.getData(TE_ZN)] : (Slope)null;
			Slope slope_ZP = TE_ZP != null ? Slope.slopesList[BlockProperties.getData(TE_ZP)] : (Slope)null;
			Slope slope_YP = TE_YP != null ? Slope.slopesList[BlockProperties.getData(TE_YP)] : (Slope)null;
			Slope slope_YN = TE_YN != null ? Slope.slopesList[BlockProperties.getData(TE_YN)] : (Slope)null;

			/* Store old slopes (to check against new ones at end of function) */

			Slope temp_slope_XN = TE_XN != null ? slope_XN : (Slope)null;
			Slope temp_slope_XP = TE_XP != null ? slope_XP : (Slope)null;
			Slope temp_slope_YP = TE_YP != null ? slope_YP : (Slope)null;
			Slope temp_slope_YN = TE_YN != null ? slope_YN : (Slope)null;
			Slope temp_slope_ZN = TE_ZN != null ? slope_ZN : (Slope)null;
			Slope temp_slope_ZP = TE_ZP != null ? slope_ZP : (Slope)null;

			/* Check if slope should transform to corner to match slope behind it. */

			if (slope.slopeType.equals(SlopeType.WEDGE_Y))
			{
				if (TE_XN != null) {
					if (slope.facings.contains(ForgeDirection.WEST)) {
						if (slope_XN.facings.contains(ForgeDirection.SOUTH) && !slope_XN.facings.contains(ForgeDirection.EAST))
							slopeID = slope_XN.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
						if (slope_XN.facings.contains(ForgeDirection.NORTH) && !slope_XN.facings.contains(ForgeDirection.EAST))
							slopeID = slope_XN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
					}
					if (slope.facings.contains(ForgeDirection.EAST)) {
						if (slope_XN.facings.contains(ForgeDirection.SOUTH) && !slope_XN.facings.contains(ForgeDirection.EAST))
							slopeID = slope_XN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
						if (slope_XN.facings.contains(ForgeDirection.NORTH) && !slope_XN.facings.contains(ForgeDirection.EAST))
							slopeID = slope_XN.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
					}
				}
				if (TE_XP != null) {
					if (slope.facings.contains(ForgeDirection.WEST)) {
						if (slope_XP.facings.contains(ForgeDirection.SOUTH) && !slope_XP.facings.contains(ForgeDirection.WEST))
							slopeID = slope_XP.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
						if (slope_XP.facings.contains(ForgeDirection.NORTH) && !slope_XP.facings.contains(ForgeDirection.WEST))
							slopeID = slope_XP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
					}
					if (slope.facings.contains(ForgeDirection.EAST)) {
						if (slope_XP.facings.contains(ForgeDirection.SOUTH) && !slope_XP.facings.contains(ForgeDirection.WEST))
							slopeID = slope_XP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
						if (slope_XP.facings.contains(ForgeDirection.NORTH) && !slope_XP.facings.contains(ForgeDirection.WEST))
							slopeID = slope_XP.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
					}
				}
				if (TE_ZN != null) {
					if (slope.facings.contains(ForgeDirection.NORTH)) {
						if (slope_ZN.facings.contains(ForgeDirection.EAST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH))
							slopeID = slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
						if (slope_ZN.facings.contains(ForgeDirection.WEST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH))
							slopeID = slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
					}
					if (slope.facings.contains(ForgeDirection.SOUTH)) {
						if (slope_ZN.facings.contains(ForgeDirection.EAST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH))
							slopeID = slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
						if (slope_ZN.facings.contains(ForgeDirection.WEST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH))
							slopeID = slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
					}
				}
				if (TE_ZP != null) {
					if (slope.facings.contains(ForgeDirection.NORTH)) {
						if (slope_ZP.facings.contains(ForgeDirection.EAST) && !slope_ZP.facings.contains(ForgeDirection.NORTH))
							slopeID = slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
						if (slope_ZP.facings.contains(ForgeDirection.WEST) && !slope_ZP.facings.contains(ForgeDirection.NORTH))
							slopeID = slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
					}
					if (slope.facings.contains(ForgeDirection.SOUTH)) {
						if (slope_ZP.facings.contains(ForgeDirection.EAST) && !slope_ZP.facings.contains(ForgeDirection.NORTH))
							slopeID = slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
						if (slope_ZP.facings.contains(ForgeDirection.WEST) && !slope_ZP.facings.contains(ForgeDirection.NORTH))
							slopeID = slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
					}
				}
			}

			/* Check if slope should transform to corner. */

			if (TE_ZN != null) {
				if (TE_XP != null) {
					if (slope_ZN.facings.contains(ForgeDirection.EAST) && slope_XP.facings.contains(ForgeDirection.NORTH))
						slopeID = slope_XP.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
					if (slope_ZN.facings.contains(ForgeDirection.WEST) && slope_XP.facings.contains(ForgeDirection.SOUTH))
						slopeID = slope_XP.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
				}
				if (TE_XN != null) {
					if (slope_ZN.facings.contains(ForgeDirection.WEST) && slope_XN.facings.contains(ForgeDirection.NORTH))
						slopeID = slope_XN.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
					if (slope_ZN.facings.contains(ForgeDirection.EAST) && slope_XN.facings.contains(ForgeDirection.SOUTH))
						slopeID = slope_XN.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
				}
			}
			if (TE_ZP != null) {
				if (TE_XN != null) {
					if (slope_ZP.facings.contains(ForgeDirection.WEST) && slope_XN.facings.contains(ForgeDirection.SOUTH))
						slopeID = slope_XN.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
					if (slope_ZP.facings.contains(ForgeDirection.EAST) && slope_XN.facings.contains(ForgeDirection.NORTH))
						slopeID = slope_XN.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
				}
				if (TE_XP != null) {
					if (slope_ZP.facings.contains(ForgeDirection.EAST) && slope_XP.facings.contains(ForgeDirection.SOUTH))
						slopeID = slope_XP.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
					if (slope_ZP.facings.contains(ForgeDirection.WEST) && slope_XP.facings.contains(ForgeDirection.NORTH))
						slopeID = slope_XP.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
				}
			}

			/* Change neighboring slopes to matching corners where applicable. */

			if (slope.facings.contains(ForgeDirection.WEST)) {
				if (TE_ZN != null && slope.isPositive == slope_ZN.isPositive) {
					if (slope_ZN.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
					if (slope_ZN.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
				}
				if (TE_ZP != null && slope.isPositive == slope_ZP.isPositive) {
					if (slope_ZP.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
					if (slope_ZP.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
				}
			}
			if (slope.facings.contains(ForgeDirection.EAST)) {
				if (TE_ZN != null && slope.isPositive && slope_ZN.isPositive) {
					if (slope_ZN.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
					if (slope_ZN.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
				}
				if (TE_ZP != null && slope.isPositive && slope_ZP.isPositive) {
					if (slope_ZP.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
					if (slope_ZP.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
				}
			}
			if (slope.facings.contains(ForgeDirection.NORTH)) {
				if (TE_XN != null && slope.isPositive && slope_XN.isPositive) {
					if (slope_XN.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
					if (slope_XN.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
				}
				if (TE_XP != null && slope.isPositive && slope_XP.isPositive) {
					if (slope_XP.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
					if (slope_XP.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
				}
			}
			if (slope.facings.contains(ForgeDirection.SOUTH)) {
				if (TE_XN != null && slope.isPositive && slope_XN.isPositive) {
					if (slope_XN.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
					if (slope_XN.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
				}
				if (TE_XP != null && slope.isPositive && slope_XP.isPositive) {
					if (slope_XP.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
					if (slope_XP.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
				}
			}

			/* Check if slope should form into a pyramid. */

			if (TE_XYNN != null && TE_XYPN != null && TE_YZNN != null && TE_YZNP != null) {
				if (
						Slope.slopesList[BlockProperties.getData(TE_XYNN)] == Slope.WEDGE_POS_W &&
						Slope.slopesList[BlockProperties.getData(TE_XYPN)] == Slope.WEDGE_POS_E &&
						Slope.slopesList[BlockProperties.getData(TE_YZNN)] == Slope.WEDGE_POS_N &&
						Slope.slopesList[BlockProperties.getData(TE_YZNP)] == Slope.WEDGE_POS_S
						)
					slopeID = Slope.ID_PYR_HALF_POS;
			}
			if (TE_XYNP != null && TE_XYPP != null && TE_YZPN != null && TE_YZPP != null) {
				if (
						Slope.slopesList[BlockProperties.getData(TE_XYNP)] == Slope.WEDGE_NEG_W &&
						Slope.slopesList[BlockProperties.getData(TE_XYPP)] == Slope.WEDGE_NEG_E &&
						Slope.slopesList[BlockProperties.getData(TE_YZPN)] == Slope.WEDGE_NEG_N &&
						Slope.slopesList[BlockProperties.getData(TE_YZPP)] == Slope.WEDGE_NEG_S
						)
					slopeID = Slope.ID_PYR_HALF_NEG;
			}

			/* Check if slope below is interior corner, change to oblique if it is, and change this to side slope. */

			if (TE_YP != null) {
				if (slope_YP == Slope.WEDGE_INT_NEG_NW) {
					slopeID = Slope.ID_WEDGE_NW;
					BlockProperties.setData(TE_YP, Slope.ID_OBL_INT_NEG_NW);
				} else if (slope_YP == Slope.WEDGE_INT_NEG_SW) {
					slopeID = Slope.ID_WEDGE_SW;
					BlockProperties.setData(TE_YP, Slope.ID_OBL_INT_NEG_SW);
				} else if (slope_YP == Slope.WEDGE_INT_NEG_NE) {
					slopeID = Slope.ID_WEDGE_NE;
					BlockProperties.setData(TE_YP, Slope.ID_OBL_INT_NEG_NE);
				} else if (slope_YP == Slope.WEDGE_INT_NEG_SE) {
					slopeID = Slope.ID_WEDGE_SE;
					BlockProperties.setData(TE_YP, Slope.ID_OBL_INT_NEG_SE);
				}
			}
			if (TE_YN != null) {
				if (slope_YN == Slope.WEDGE_INT_POS_NW) {
					slopeID = Slope.ID_WEDGE_NW;
					BlockProperties.setData(TE_YN, Slope.ID_OBL_INT_POS_NW);
				} else if (slope_YN == Slope.WEDGE_INT_POS_SW) {
					slopeID = Slope.ID_WEDGE_SW;
					BlockProperties.setData(TE_YN, Slope.ID_OBL_INT_POS_SW);
				} else if (slope_YN == Slope.WEDGE_INT_POS_NE) {
					slopeID = Slope.ID_WEDGE_NE;
					BlockProperties.setData(TE_YN, Slope.ID_OBL_INT_POS_NE);
				} else if (slope_YN == Slope.WEDGE_INT_POS_SE) {
					slopeID = Slope.ID_WEDGE_SE;
					BlockProperties.setData(TE_YN, Slope.ID_OBL_INT_POS_SE);
				}
			}

			/* Check if slope above or below is side slope or oblique interior slope, and, if so, make this a continuation. */

			if (TE_YP != null) {
				if (slope_YP.slopeType.equals(SlopeType.WEDGE_XZ)) {
					slopeID = slope_YP.slopeID;
				} else if (slope_YP.slopeType.equals(SlopeType.OBLIQUE_INT)) {
					switch (slope_YP.slopeID) {
					case Slope.ID_OBL_INT_NEG_NW:
						slopeID = Slope.ID_WEDGE_NW;
						break;
					case Slope.ID_OBL_INT_NEG_SW:
						slopeID = Slope.ID_WEDGE_SW;
						break;
					case Slope.ID_OBL_INT_NEG_NE:
						slopeID = Slope.ID_WEDGE_NE;
						break;
					default: // Slope.INT_NEG_OBL_SE
						slopeID = Slope.ID_WEDGE_SE;
						break;
					}
				}
			}
			if (TE_YN != null) {
				if (slope_YN.slopeType.equals(SlopeType.WEDGE_XZ)) {
					slopeID = slope_YN.slopeID;
				} else if (slope_YN.slopeType.equals(SlopeType.OBLIQUE_INT)) {
					switch (slope_YN.slopeID) {
					case Slope.ID_OBL_INT_POS_NW:
						slopeID = Slope.ID_WEDGE_NW;
						break;
					case Slope.ID_OBL_INT_POS_SW:
						slopeID = Slope.ID_WEDGE_SW;
						break;
					case Slope.ID_OBL_INT_POS_NE:
						slopeID = Slope.ID_WEDGE_NE;
						break;
					default: // Slope.INT_POS_OBL_SE
						slopeID = Slope.ID_WEDGE_SE;
						break;
					}
				}
			}

			/* Check if slope should form into exterior oblique corner. */

			if (
					TE_YP != null && (slope_YP.slopeType.equals(SlopeType.OBLIQUE_EXT) || slope_YP.slopeType.equals(SlopeType.WEDGE_XZ)) ||
					TE_YN != null && (slope_YN.slopeType.equals(SlopeType.OBLIQUE_EXT) || slope_YN.slopeType.equals(SlopeType.WEDGE_XZ))
					)
			{
				if (TE_XP != null && TE_ZN != null) {

					if (slope_XP.slopeType.equals(SlopeType.WEDGE_XZ) && slope_ZN.slopeType.equals(SlopeType.WEDGE_XZ) && slope_XP.isPositive == slope_ZN.isPositive)
						if (slope_XP.facings.contains(ForgeDirection.SOUTH) && slope_ZN.facings.contains(ForgeDirection.WEST))
							slopeID = slope_XP.isPositive ? Slope.ID_OBL_EXT_POS_SW : Slope.ID_OBL_EXT_NEG_SW;

				} else if (TE_ZN != null && TE_XN != null) {

					if (slope_ZN.slopeType.equals(SlopeType.WEDGE_XZ) && slope_XN.slopeType.equals(SlopeType.WEDGE_XZ) && slope_ZN.isPositive == slope_XN.isPositive)
						if (slope_ZN.facings.contains(ForgeDirection.EAST) && slope_XN.facings.contains(ForgeDirection.SOUTH))
							slopeID = slope_ZN.isPositive ? Slope.ID_OBL_EXT_POS_SE : Slope.ID_OBL_EXT_NEG_SE;

				} else if (TE_XN != null && TE_ZP != null) {

					if (slope_XN.slopeType.equals(SlopeType.WEDGE_XZ) && slope_ZP.slopeType.equals(SlopeType.WEDGE_XZ) && slope_XN.isPositive == slope_ZP.isPositive)
						if (slope_XN.facings.contains(ForgeDirection.NORTH) && slope_ZP.facings.contains(ForgeDirection.EAST))
							slopeID = slope_XN.isPositive ? Slope.ID_OBL_EXT_POS_NE : Slope.ID_OBL_EXT_NEG_NE;

				} else if (TE_ZP != null && TE_XP != null) {

					if (slope_ZP.slopeType.equals(SlopeType.WEDGE_XZ) && slope_XP.slopeType.equals(SlopeType.WEDGE_XZ) && slope_ZP.isPositive == slope_XP.isPositive)
						if (slope_ZP.facings.contains(ForgeDirection.WEST) && slope_XP.facings.contains(ForgeDirection.NORTH))
							slopeID = slope_ZP.isPositive ? Slope.ID_OBL_EXT_POS_NW : Slope.ID_OBL_EXT_NEG_NW;

				}
			}

			/* Server should update clients when adjacent slopes are altered. */
			if (!world.isRemote)
			{
				if (TE_XN != null && slope_XN != temp_slope_XN)
					BlockProperties.setData(TE_XN, slope_XN.slopeID);
				if (TE_XP != null && slope_XP != temp_slope_XP)
					BlockProperties.setData(TE_XP, slope_XP.slopeID);
				if (TE_ZN != null && slope_ZN != temp_slope_ZN)
					BlockProperties.setData(TE_ZN, slope_ZN.slopeID);
				if (TE_ZP != null && slope_ZP != temp_slope_ZP)
					BlockProperties.setData(TE_ZP, slope_ZP.slopeID);
				if (TE_YN != null && slope_YN != temp_slope_YN)
					BlockProperties.setData(TE_YN, slope_YN.slopeID);
				if (TE_YP != null && slope_YP != temp_slope_YP)
					BlockProperties.setData(TE_YP, slope_YP.slopeID);
			}
		}

		BlockProperties.setData(TE, slopeID);
	}

	@Override
	public boolean canCoverSide(TECarpentersBlock TE, World world, int x, int y, int z, int side)
	{
		return isBlockSolidOnSide(world, x, y, z, ForgeDirection.getOrientation(side));
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersSlopeRenderID;
	}

}
