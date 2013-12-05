package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Stairs;
import carpentersblocks.data.Stairs.StairsType;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersStairs extends BlockBase
{

	public BlockCarpentersStairs(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersStairs");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
	}
	
    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
	public void registerIcons(IconRegister iconRegister)
	{
		this.blockIcon = IconRegistry.icon_stairs;
		super.registerIcons(iconRegister);
	}

	@Override
	/**
	 * Alters block direction.
	 */
	protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
	{
		int stairsID = BlockProperties.getData(TE);
		Stairs stairs = Stairs.stairsList[stairsID];

		/*
		 * Cycle between stairs direction based on current type
		 */

		switch (stairs.stairsType)
		{
		case NORMAL_XZ:
			if (++stairsID > 3)
				stairsID = 0;
			break;
		case NORMAL_Y:
			if (stairs.arePositive) {
				if (++stairsID > 11)
					stairsID = 8;
			} else {
				if (++stairsID > 7)
					stairsID = 4;
			}
			break;
		case NORMAL_INT:
			if (stairs.arePositive) {
				if ((stairsID += 2) > 18)
					stairsID = 12;
			} else {
				if ((stairsID += 2) > 19)
					stairsID = 13;
			}
			break;
		case NORMAL_EXT:
			if (stairs.arePositive) {
				if ((stairsID += 2) > 26)
					stairsID = 20;
			}  else {
				if ((stairsID += 2) > 27)
					stairsID = 21;
			}
			break;
		}

		BlockProperties.setData(TE, stairsID);

		return true;
	}

	@Override
	/**
	 * Alters block type.
	 */
	protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitZ)
	{
		int stairsID = BlockProperties.getData(TE);
		Stairs stairs = Stairs.stairsList[stairsID];

		/*
		 * Transform stairs to next type
		 */

		switch (stairs.stairsType)
		{
		case NORMAL_XZ:
			stairsID = 8;
			break;
		case NORMAL_Y:
			if (stairs.arePositive) {
				stairsID -= 4;
			} else {
				stairsID = 12;
			}
			break;
		case NORMAL_INT:
			if (stairs.arePositive) {
				stairsID += 1;
			} else {
				if (stairsID == 13 || stairsID == 15) {
					stairsID += 11;
				} else {
					stairsID += 3;
				}
			}
			break;
		case NORMAL_EXT:
			if (stairs.arePositive) {
				stairsID += 1;
			} else {
				stairsID = 0;
			}
			break;
		}

		BlockProperties.setData(TE, stairsID);

		return true;
	}

	/**
	 * Will return stairs boundaries.
	 */
	public float[] genBounds(int box, Stairs stairs)
	{
		++box;

		switch (stairs.stairsID)
		{
		case Stairs.ID_NORMAL_SW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_NW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_NE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_SE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_POS_N:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_POS_W:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_POS_E:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_POS_S:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_NEG_N:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_NEG_W:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_NEG_E:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_NEG_S:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
			case 2:
				return new float[] { 0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_NW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_SW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
			case 3:
				return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_NE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_SE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
			case 3:
				return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_NW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_SW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
			case 3:
				return new float[] { 0.0F, 0.5F, 0.5F, 0.5F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_NE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_SE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
			case 3:
				return new float[] { 0.5F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_NW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F };
			case 3:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_SW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
			case 2:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F };
			case 3:
				return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_NE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_SE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
			case 2:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_NW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_SW:
			switch (box) {
			case 1:
				return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
			case 2:
				return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.5F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_NE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
			case 2:
				return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 0.5F };
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_SE:
			switch (box) {
			case 1:
				return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
			case 2:
				return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F };
			case 3:
				return new float[] { 0.0F, 0.5F, 0.5F, 0.5F, 1.0F, 1.0F };
			}
			break;
		}

		return null;
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

		int stairsID = BlockProperties.getData(TE);
		Stairs stairs = Stairs.stairsList[stairsID];

		double currDist = 0.0D;
		double maxDist = 0.0D;

		// Determine if ray trace is a hit on stairs
		for (int box = 0; box < 3; ++box)
		{
			float[] bounds = genBounds(box, stairs);

			if (bounds != null)
			{
				setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
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
		AxisAlignedBB colBox = null;

		TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

		int stairsID = BlockProperties.getData(TE);
		Stairs stairs = Stairs.stairsList[stairsID];

		for (int box = 0; box < 3; ++box)
		{
			float[] bounds = genBounds(box, stairs);

			if (bounds != null) {
				colBox = AxisAlignedBB.getAABBPool().getAABB(x + bounds[0], y + bounds[1], z + bounds[2], x + bounds[3], y + bounds[4], z + bounds[5]);
			}
			if (colBox != null && axisAlignedBB.intersectsWith(colBox)) {
				list.add(colBox);
			}
		}
	}

	@Override
	/**
	 * Checks if the block is a solid face on the given side, used by placement logic.
	 */
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

		if (isBlockSolid(world, x, y, z))
			return Stairs.stairsList[BlockProperties.getData(TE)].isFaceFull(side);

		return false;
	}

	@Override
	/**
	 * Called when block is placed in world.
	 * Sets stairs angle depending on click coordinates on block face.
	 *
	 *	Metadata values:
	 * 	 0 - 11	-	Identifies stairs angle in x, y, z space.
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
	public void auxiliaryOnBlockPlacedBy(TEBase TE, World world, int x, int y, int z, EntityLiving entityLiving, ItemStack itemStack)
	{
		int facing = MathHelper.floor_double(entityLiving.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

		BlockProperties.setData(TE, world.getBlockMetadata(x, y, z));
		int stairsID = BlockProperties.getData(TE);

		if (stairsID > 11)
		{
			switch (facing) {
			case 0:
				stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_N : Stairs.ID_NORMAL_POS_N;
				break;
			case 1:
				stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_E : Stairs.ID_NORMAL_POS_E;
				break;
			case 2:
				stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_S : Stairs.ID_NORMAL_POS_S;
				break;
			case 3:
				stairsID = stairsID == 12 ? Stairs.ID_NORMAL_NEG_W : Stairs.ID_NORMAL_POS_W;
				break;
			}
		}

		/* If shift key is down, skip auto-orientation. */
		if (!entityLiving.isSneaking())
		{
			Stairs stairs = Stairs.stairsList[stairsID];

			TEBase TE_XN = world.getBlockId(x - 1, y, z) == blockID ? (TEBase)world.getBlockTileEntity(x - 1, y, z) : null;
			TEBase TE_XP = world.getBlockId(x + 1, y, z) == blockID ? (TEBase)world.getBlockTileEntity(x + 1, y, z) : null;
			TEBase TE_YP = world.getBlockId(x, y + 1, z) == blockID ? (TEBase)world.getBlockTileEntity(x, y + 1, z) : null;
			TEBase TE_YN = world.getBlockId(x, y - 1, z) == blockID ? (TEBase)world.getBlockTileEntity(x, y - 1, z) : null;
			TEBase TE_ZN = world.getBlockId(x, y, z - 1) == blockID ? (TEBase)world.getBlockTileEntity(x, y, z - 1) : null;
			TEBase TE_ZP = world.getBlockId(x, y, z + 1) == blockID ? (TEBase)world.getBlockTileEntity(x, y, z + 1) : null;

			/* Gather neighboring stairs */

			Stairs stairs_XN = TE_XN != null ? Stairs.stairsList[BlockProperties.getData(TE_XN)] : (Stairs)null;
			Stairs stairs_XP = TE_XP != null ? Stairs.stairsList[BlockProperties.getData(TE_XP)] : (Stairs)null;
			Stairs stairs_ZN = TE_ZN != null ? Stairs.stairsList[BlockProperties.getData(TE_ZN)] : (Stairs)null;
			Stairs stairs_ZP = TE_ZP != null ? Stairs.stairsList[BlockProperties.getData(TE_ZP)] : (Stairs)null;
			Stairs stairs_YP = TE_YP != null ? Stairs.stairsList[BlockProperties.getData(TE_YP)] : (Stairs)null;
			Stairs stairs_YN = TE_YN != null ? Stairs.stairsList[BlockProperties.getData(TE_YN)] : (Stairs)null;

			/* Store old stairs (to check against new ones at end of function) */

			Stairs temp_stairs_XN = TE_XN != null ? stairs_XN : (Stairs)null;
			Stairs temp_stairs_XP = TE_XP != null ? stairs_XP : (Stairs)null;
			Stairs temp_stairs_YP = TE_YP != null ? stairs_YP : (Stairs)null;
			Stairs temp_stairs_YN = TE_YN != null ? stairs_YN : (Stairs)null;
			Stairs temp_stairs_ZN = TE_ZN != null ? stairs_ZN : (Stairs)null;
			Stairs temp_stairs_ZP = TE_ZP != null ? stairs_ZP : (Stairs)null;

			/* Check if stairs should transform into corner to match stairs behind it. */

			if (stairs.stairsType.equals(StairsType.NORMAL_Y))
			{
				if (TE_XN != null) {
					if (stairs.facings.contains(ForgeDirection.WEST)) {
						if (stairs_XN.facings.contains(ForgeDirection.SOUTH) && !stairs_XN.facings.contains(ForgeDirection.EAST))
							stairsID = stairs_XN.arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
						if (stairs_XN.facings.contains(ForgeDirection.NORTH) && !stairs_XN.facings.contains(ForgeDirection.EAST))
							stairsID = stairs_XN.arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
					}
					if (stairs.facings.contains(ForgeDirection.EAST)) {
						if (stairs_XN.facings.contains(ForgeDirection.SOUTH) && !stairs_XN.facings.contains(ForgeDirection.EAST))
							stairsID = stairs_XN.arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
						if (stairs_XN.facings.contains(ForgeDirection.NORTH) && !stairs_XN.facings.contains(ForgeDirection.EAST))
							stairsID = stairs_XN.arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
					}
				}
				if (TE_XP != null) {
					if (stairs.facings.contains(ForgeDirection.WEST)) {
						if (stairs_XP.facings.contains(ForgeDirection.SOUTH) && !stairs_XP.facings.contains(ForgeDirection.WEST))
							stairsID = stairs_XP.arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
						if (stairs_XP.facings.contains(ForgeDirection.NORTH) && !stairs_XP.facings.contains(ForgeDirection.WEST))
							stairsID = stairs_XP.arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
					}
					if (stairs.facings.contains(ForgeDirection.EAST)) {
						if (stairs_XP.facings.contains(ForgeDirection.SOUTH) && !stairs_XP.facings.contains(ForgeDirection.WEST))
							stairsID = stairs_XP.arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
						if (stairs_XP.facings.contains(ForgeDirection.NORTH) && !stairs_XP.facings.contains(ForgeDirection.WEST))
							stairsID = stairs_XP.arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
					}
				}
				if (TE_ZN != null) {
					if (stairs.facings.contains(ForgeDirection.NORTH)) {
						if (stairs_ZN.facings.contains(ForgeDirection.EAST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH))
							stairsID = stairs_ZN.arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
						if (stairs_ZN.facings.contains(ForgeDirection.WEST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH))
							stairsID = stairs_ZN.arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
					}
					if (stairs.facings.contains(ForgeDirection.SOUTH)) {
						if (stairs_ZN.facings.contains(ForgeDirection.EAST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH))
							stairsID = stairs_ZN.arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
						if (stairs_ZN.facings.contains(ForgeDirection.WEST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH))
							stairsID = stairs_ZN.arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
					}
				}
				if (TE_ZP != null) {
					if (stairs.facings.contains(ForgeDirection.NORTH)) {
						if (stairs_ZP.facings.contains(ForgeDirection.EAST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH))
							stairsID = stairs_ZP.arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
						if (stairs_ZP.facings.contains(ForgeDirection.WEST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH))
							stairsID = stairs_ZP.arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
					}
					if (stairs.facings.contains(ForgeDirection.SOUTH)) {
						if (stairs_ZP.facings.contains(ForgeDirection.EAST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH))
							stairsID = stairs_ZP.arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
						if (stairs_ZP.facings.contains(ForgeDirection.WEST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH))
							stairsID = stairs_ZP.arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
					}
				}
			}

			/* Check if stairs should transform into corner. */

			if (TE_ZN != null) {
				if (TE_XP != null) {
					if (stairs_ZN.facings.contains(ForgeDirection.EAST) && stairs_XP.facings.contains(ForgeDirection.NORTH))
						stairsID = stairs_XP.arePositive && stairs_ZN.arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
					if (stairs_ZN.facings.contains(ForgeDirection.WEST) && stairs_XP.facings.contains(ForgeDirection.SOUTH))
						stairsID = stairs_XP.arePositive && stairs_ZN.arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
				}
				if (TE_XN != null) {
					if (stairs_ZN.facings.contains(ForgeDirection.WEST) && stairs_XN.facings.contains(ForgeDirection.NORTH))
						stairsID = stairs_XN.arePositive && stairs_ZN.arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
					if (stairs_ZN.facings.contains(ForgeDirection.EAST) && stairs_XN.facings.contains(ForgeDirection.SOUTH))
						stairsID = stairs_XN.arePositive && stairs_ZN.arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
				}
			}
			if (TE_ZP != null) {
				if (TE_XN != null) {
					if (stairs_ZP.facings.contains(ForgeDirection.WEST) && stairs_XN.facings.contains(ForgeDirection.SOUTH))
						stairsID = stairs_XN.arePositive && stairs_ZP.arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
					if (stairs_ZP.facings.contains(ForgeDirection.EAST) && stairs_XN.facings.contains(ForgeDirection.NORTH))
						stairsID = stairs_XN.arePositive && stairs_ZP.arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
				}
				if (TE_XP != null) {
					if (stairs_ZP.facings.contains(ForgeDirection.EAST) && stairs_XP.facings.contains(ForgeDirection.SOUTH))
						stairsID = stairs_XP.arePositive && stairs_ZP.arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
					if (stairs_ZP.facings.contains(ForgeDirection.WEST) && stairs_XP.facings.contains(ForgeDirection.NORTH))
						stairsID = stairs_XP.arePositive && stairs_ZP.arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
				}
			}

			/* Change neighboring stairs to matching corners where applicable. */

			if (stairs.facings.contains(ForgeDirection.WEST)) {
				if (TE_ZN != null && stairs.arePositive == stairs_ZN.arePositive) {
					if (stairs_ZN.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZN, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW);
					if (stairs_ZN.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZN, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW);
				}
				if (TE_ZP != null && stairs.arePositive == stairs_ZP.arePositive) {
					if (stairs_ZP.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZP, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW);
					if (stairs_ZP.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZP, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW);
				}
			}
			if (stairs.facings.contains(ForgeDirection.EAST)) {
				if (TE_ZN != null && stairs.arePositive && stairs_ZN.arePositive) {
					if (stairs_ZN.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZN, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE);
					if (stairs_ZN.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZN, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE);
				}
				if (TE_ZP != null && stairs.arePositive && stairs_ZP.arePositive) {
					if (stairs_ZP.facings.contains(ForgeDirection.SOUTH))
						BlockProperties.setData(TE_ZP, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE);
					if (stairs_ZP.facings.contains(ForgeDirection.NORTH))
						BlockProperties.setData(TE_ZP, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE);
				}
			}
			if (stairs.facings.contains(ForgeDirection.NORTH)) {
				if (TE_XN != null && stairs.arePositive && stairs_XN.arePositive) {
					if (stairs_XN.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XN, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW);
					if (stairs_XN.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XN, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE);
				}
				if (TE_XP != null && stairs.arePositive && stairs_XP.arePositive) {
					if (stairs_XP.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XP, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE);
					if (stairs_XP.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XP, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW);
				}
			}
			if (stairs.facings.contains(ForgeDirection.SOUTH)) {
				if (TE_XN != null && stairs.arePositive && stairs_XN.arePositive) {
					if (stairs_XN.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XN, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW);
					if (stairs_XN.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XN, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE);
				}
				if (TE_XP != null && stairs.arePositive && stairs_XP.arePositive) {
					if (stairs_XP.facings.contains(ForgeDirection.EAST))
						BlockProperties.setData(TE_XP, stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE);
					if (stairs_XP.facings.contains(ForgeDirection.WEST))
						BlockProperties.setData(TE_XP, stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW);
				}
			}

			/* Check if stairs above or below is side stairs or oblique interior stairs, and, if so, make this a continuation. */

			if (TE_YP != null) {
				if (stairs_YP.stairsType.equals(StairsType.NORMAL_XZ)) {
					stairsID = stairs_YP.stairsID;
				}
			}
			if (TE_YN != null) {
				if (stairs_YN.stairsType.equals(StairsType.NORMAL_XZ)) {
					stairsID = stairs_YN.stairsID;
				}
			}

			/* Server should update clients when adjacent stairs are altered. */
			if (!world.isRemote)
			{
				if (TE_XN != null && stairs_XN != temp_stairs_XN)
					BlockProperties.setData(TE_XN, stairs_XN.stairsID);
				if (TE_XP != null && stairs_XP != temp_stairs_XP)
					BlockProperties.setData(TE_XP, stairs_XP.stairsID);
				if (TE_ZN != null && stairs_ZN != temp_stairs_ZN)
					BlockProperties.setData(TE_ZN, stairs_ZN.stairsID);
				if (TE_ZP != null && stairs_ZP != temp_stairs_ZP)
					BlockProperties.setData(TE_ZP, stairs_ZP.stairsID);
				if (TE_YN != null && stairs_YN != temp_stairs_YN)
					BlockProperties.setData(TE_YN, stairs_YN.stairsID);
				if (TE_YP != null && stairs_YP != temp_stairs_YP)
					BlockProperties.setData(TE_YP, stairs_YP.stairsID);
			}
		}

		BlockProperties.setData(TE, stairsID);
	}

	@Override
	/**
	 * Returns whether block can support cover on side.
	 */
	public boolean canCoverSide(TEBase TE, World world, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockRegistry.carpentersStairsRenderID;
	}

}
