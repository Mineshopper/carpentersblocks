package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Collapsible;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersCollapsibleBlock extends BlockBase
{

	public BlockCarpentersCollapsibleBlock(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersCollapsibleBlock");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setTextureName("carpentersblocks:general/generic");
	}

	@Override
	/**
	 * Lower quadrant of block.
	 */
	protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitZ)
	{
		int quad = Collapsible.getQuad(hitX, hitZ);
		int quadHeight = Collapsible.getQuadHeight(TE, quad);

		Collapsible.setQuadHeight(TE, quad, --quadHeight);
		smoothAdjacentCollapsibles(TE, quad);

		return true;
	}
	
	@Override
	/**
	 * Damages hammer with a chance to not damage.
	 */
	protected void damageItemWithChance(World world, EntityPlayer entityPlayer)
	{
		if (world.rand.nextFloat() <= 0.2F) {
			entityPlayer.getCurrentEquippedItem().damageItem(1, entityPlayer);
		}
	}
	
	/**
	 * Will attempt to smooth transitions to any adjacent collapsible blocks
	 * given a TE and source quadrant.
	 */
	private void smoothAdjacentCollapsibles(TEBase TE, int src_quadrant)
	{
		TEBase TE_XN = TE.worldObj.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord) : null;
		TEBase TE_XP = TE.worldObj.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord) : null;
		TEBase TE_ZN = TE.worldObj.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord - 1) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1) : null;
		TEBase TE_ZP = TE.worldObj.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord + 1) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1) : null;
		TEBase TE_XZNN = TE.worldObj.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord - 1) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord - 1) : null;
		TEBase TE_XZNP = TE.worldObj.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord + 1) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord + 1) : null;
		TEBase TE_XZPN = TE.worldObj.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord - 1) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord - 1) : null;
		TEBase TE_XZPP = TE.worldObj.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord + 1) == blockID ? (TEBase)TE.worldObj.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord + 1) : null;
		
		int height = Collapsible.getQuadHeight(TE, src_quadrant);
		
		switch (src_quadrant)
		{
		case Collapsible.QUAD_XZNN:
			if (TE_ZN != null) {
				Collapsible.setQuadHeight(TE_ZN, Collapsible.QUAD_XZNP, height);
			}
			if (TE_XZNN != null) {
				Collapsible.setQuadHeight(TE_XZNN, Collapsible.QUAD_XZPP, height);
			}
			if (TE_XN != null) {
				Collapsible.setQuadHeight(TE_XN, Collapsible.QUAD_XZPN, height);
			}
			break;
		case Collapsible.QUAD_XZNP:
			if (TE_XN != null) {
				Collapsible.setQuadHeight(TE_XN, Collapsible.QUAD_XZPP, height);
			}
			if (TE_XZNP != null) {
				Collapsible.setQuadHeight(TE_XZNP, Collapsible.QUAD_XZPN, height);
			}
			if (TE_ZP != null) {
				Collapsible.setQuadHeight(TE_ZP, Collapsible.QUAD_XZNN, height);
			}
			break;
		case Collapsible.QUAD_XZPN:
			if (TE_XP != null) {
				Collapsible.setQuadHeight(TE_XP, Collapsible.QUAD_XZNN, height);
			}
			if (TE_XZPN != null) {
				Collapsible.setQuadHeight(TE_XZPN, Collapsible.QUAD_XZNP, height);
			}
			if (TE_ZN != null) {
				Collapsible.setQuadHeight(TE_ZN, Collapsible.QUAD_XZPP, height);
			}
			break;
		case Collapsible.QUAD_XZPP:
			if (TE_ZP != null) {
				Collapsible.setQuadHeight(TE_ZP, Collapsible.QUAD_XZPN, height);
			}
			if (TE_XZPP != null) {
				Collapsible.setQuadHeight(TE_XZPP, Collapsible.QUAD_XZNN, height);
			}
			if (TE_XP != null) {
				Collapsible.setQuadHeight(TE_XP, Collapsible.QUAD_XZNP, height);
			}
			break;
		}
	}
	
	@Override
	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y, z
	 */
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		float maxHeight = getMaxHeight(TE);

		if (maxHeight != 1.0F)
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, maxHeight, 1.0F);
	}
	
	@Override
	/**
	 * Checks if the block is a solid face on the given side, used by placement logic.
	 */
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		TEBase TE = (TEBase)world.getBlockTileEntity(x, y, z);

		if (isBlockSolid(world, x, y, z))
		{
			switch (side) {
			case UP:
				return BlockProperties.getData(TE) == 0xffff;
			case NORTH:
				return (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN)) == 32;
			case SOUTH:
				return (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP)) == 32;
			case WEST:
				return (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN)) == 32;
			case EAST:
				return (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP)) == 32;
			default:
				return true;
			}
		}

		return false;
	}
	
	@Override
	/**
	 * Called when the block is placed in the world.
	 */
	public void auxiliaryOnBlockPlacedBy(TEBase TE, World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		// If shift key is down, skip auto-setting quadrant heights
		if (!entityLiving.isSneaking())
		{
			/* Match adjacent collapsible quadrant heights. */
			
			TEBase TE_XN = world.getBlockId(x - 1, y, z) == blockID ? (TEBase)world.getBlockTileEntity(x - 1, y, z) : null;
			TEBase TE_XP = world.getBlockId(x + 1, y, z) == blockID ? (TEBase)world.getBlockTileEntity(x + 1, y, z) : null;
			TEBase TE_ZN = world.getBlockId(x, y, z - 1) == blockID ? (TEBase)world.getBlockTileEntity(x, y, z - 1) : null;
			TEBase TE_ZP = world.getBlockId(x, y, z + 1) == blockID ? (TEBase)world.getBlockTileEntity(x, y, z + 1) : null;

			if (TE_XN != null) {
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNN, Collapsible.getQuadHeight(TE_XN, Collapsible.QUAD_XZPN));
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNP, Collapsible.getQuadHeight(TE_XN, Collapsible.QUAD_XZPP));
			}
			if (TE_XP != null) {
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPN, Collapsible.getQuadHeight(TE_XP, Collapsible.QUAD_XZNN));
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPP, Collapsible.getQuadHeight(TE_XP, Collapsible.QUAD_XZNP));
			}
			if (TE_ZN != null) {
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNN, Collapsible.getQuadHeight(TE_ZN, Collapsible.QUAD_XZNP));
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPN, Collapsible.getQuadHeight(TE_ZN, Collapsible.QUAD_XZPP));
			}
			if (TE_ZP != null) {
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZNP, Collapsible.getQuadHeight(TE_ZP, Collapsible.QUAD_XZNN));
				Collapsible.setQuadHeight(TE, Collapsible.QUAD_XZPP, Collapsible.getQuadHeight(TE_ZP, Collapsible.QUAD_XZPN));
			}
		}
	}
	
	/**
	 * Returns block height determined by the highest quadrant.
	 */
	private float getMaxHeight(TEBase TE)
	{
		float maxHeight = 1.0F / 16.0F;
		
		for (int quadrant = 0; quadrant < 4; ++quadrant) {
			float quadHeight = Collapsible.getQuadHeight(TE, quadrant) / 16.0F;
			if (quadHeight > maxHeight)
				maxHeight = quadHeight;
		}		

		return maxHeight;
	}
	
	/**
	 * Will generate four boxes with max height represented by quadrant height.
	 */
	private float[] genBounds(TEBase TE, int quad)
	{
		float xMin = 0.0F;
		float zMin = 0.0F;
		float xMax = 1.0F;
		float zMax = 1.0F;		
		
		switch (quad)
		{
		case Collapsible.QUAD_XZNN:
			xMax = 0.5F;
			zMax = 0.5F;
			break;
		case Collapsible.QUAD_XZNP:
			xMax = 0.5F;
			zMin = 0.5F;
			break;
		case Collapsible.QUAD_XZPN:
			xMin = 0.5F;
			zMax = 0.5F;
			break;
		case Collapsible.QUAD_XZPP:
			xMin = 0.5F;
			zMin = 0.5F;
			break;
		}
		
		float maxHeight = getMaxHeight(TE);
		float height = Collapsible.getQuadHeight(TE, quad) / 16.0F;
		
		/* Make quads stagger no more than 0.5F so player can always walk across them. */
		if ((maxHeight - height) > 0.5F)
			height = maxHeight - 0.5F;
		
		return new float[] { xMin, 0.0F, zMin, xMax, height, zMax };		
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

		for (int quad = 0; quad < 4; ++quad)
		{
			float[] bounds = genBounds(TE, quad);
			colBox = AxisAlignedBB.getAABBPool().getAABB(x + bounds[0], y + bounds[1], z + bounds[2], x + bounds[3], y + bounds[4], z + bounds[5]);

			if (axisAlignedBB.intersectsWith(colBox)) {
				list.add(colBox);
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
		TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

		MovingObjectPosition finalTrace = null;

		double currDist = 0.0D;
		double maxDist = 0.0D;

		// Determine if ray trace is a hit on block
		for (int quad = 0; quad < 4; ++quad)
		{
			float[] bounds = genBounds(TE, quad);

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

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		return finalTrace;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
	 * coordinates.  Args: world, x, y, z, side
	 */
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return side == ForgeDirection.UP.ordinal() ? true : super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockRegistry.carpentersCollapsibleBlockRenderID;
	}

}
