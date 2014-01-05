package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.block.BlockCarpentersBarrier;
import carpentersblocks.data.Barrier;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersBarrier extends BlockHandlerBase {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		renderBlocks.setRenderBounds(0.125D, 0.0D, 0.375D, 0.375D, 1.0D, 0.625D);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

		renderBlocks.setRenderBounds(0.625D, 0.0D, 0.375D, 0.875D, 1.0D, 0.625D);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

		renderBlocks.setRenderBounds(0.0D, 0.8125D, 0.4375D, 1.0D, 0.9375D, 0.5625D);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

		renderBlocks.setRenderBounds(0.0D, 0.4375D, 0.4375D, 1.0D, 0.5625D, 0.5625D);
		super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
	}

	@Override
	/**
	 * Renders barrier
	 */
	protected boolean renderCarpentersBlock(int x, int y, int z)
	{
		int type = Barrier.getType(TE);
		Block block = BlockProperties.getCoverBlock(TE, 6);

		switch (type) {
		case Barrier.TYPE_PICKET:
			renderPicketFence(block, x, y, z);
			break;
		case Barrier.TYPE_PLANK_VERTICAL:
			renderVerticalPlankFence(block, x, y, z);
			break;
		case Barrier.TYPE_WALL:
			renderWall(block, x, y, z);
			break;
		default:
			renderFence(block, x, y, z);
		}

		return true;
	}

	/**
	 * Returns whether barrier has forced post or naturally forms a post at coordinates.
	 */
	private boolean isPostAt(int x, int y, int z)
	{
		if (renderBlocks.blockAccess.getBlockId(x, y, z) == BlockRegistry.blockCarpentersBarrierID)
		{
			TEBase TE = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(x, y, z);

			BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockRegistry.blockCarpentersBarrier;

			boolean connect_XN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.EAST);
			boolean connect_XP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.WEST);
			boolean connect_YP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y + 1, z, ForgeDirection.UP);
			boolean connect_ZN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.SOUTH);
			boolean connect_ZP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.NORTH);

			boolean pathOnX = connect_XN && connect_XP;
			boolean pathOnZ = connect_ZN && connect_ZP;

			return	Barrier.getPost(TE) == Barrier.HAS_POST ||
					pathOnX == pathOnZ ||
					connect_YP ||
					(connect_XN || connect_XP) && (connect_ZN || connect_ZP);
		}

		return false;
	}

	/**
	 * Renders vanilla fence at given coordinates
	 */
	private void renderFence(Block block, int x, int y, int z)
	{
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockRegistry.blockCarpentersBarrier;

		boolean connect_XN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.EAST);
		boolean connect_XP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.WEST);
		boolean connect_ZN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.SOUTH);
		boolean connect_ZP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.NORTH);

		float x_Low = 0.0F;
		float x_High = 0.0F;
		float y_Low = 0.0F;
		float y_High = 0.0F;
		float z_Low = 0.0F;
		float z_High = 0.0F;

		z_Low = 0.375F;
		z_High = 0.625F;
		y_High = 1.0F;

		float yPlankOffset = Barrier.getType(TE) * 0.0625F;
		boolean joinPlanks = Barrier.getType(TE) == Barrier.TYPE_VANILLA_X3;

		// Render center post
		if (isPostAt(x, y, z) || isPostAt(x, y + 1, z)) {
			renderBlocks.setRenderBounds(z_Low, 0.0D, z_Low, z_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		z_Low = 0.4375F;
		z_High = 0.5625F;
		y_High = 0.9375F;
		y_Low = 0.75F;
		x_Low = connect_XN ? 0.0F : z_Low;
		x_High = connect_XP ? 1.0F : z_High;
		float zLow1 = connect_ZN ? 0.0F : z_Low;
		float zHigh1 = connect_ZP ? 1.0F : z_High;

		/*
		 * Upper horizontal plank
		 */
		if (connect_XN || connect_XP) {
			renderBlocks.setRenderBounds(x_Low, joinPlanks ? 0.1875F : y_Low - yPlankOffset, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_ZN || connect_ZP) {
			renderBlocks.setRenderBounds(z_Low, joinPlanks ? 0.1875F : y_Low - yPlankOffset, zLow1, z_High, y_High, zHigh1);
			renderBlock(block, x, y, z);
		}

		/*
		 * Skip rendering lower plank if planks are joined.
		 */
		if (!joinPlanks)
		{
			/*
			 * Lower horizontal plank
			 */
			y_Low = 0.375F;
			y_High = 0.5625F;

			if (connect_XN || connect_XP) {
				renderBlocks.setRenderBounds(x_Low, y_Low - yPlankOffset, z_Low, x_High, y_High, z_High);
				renderBlock(block, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low - yPlankOffset, zLow1, z_High, y_High, zHigh1);
				renderBlock(block, x, y, z);
			}
		}

		blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
	}

	/**
	 * Renders picket fence block at given coordinates
	 */
	private void renderPicketFence(Block block, int x, int y, int z)
	{
		BlockProperties.getData(TE);
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockRegistry.blockCarpentersBarrier;
		float x_Low = 0.0F;
		float x_High = 0.0F;
		float y_Low = 0.0F;
		float y_High = 0.0F;
		float z_Low = 0.0F;
		float z_High = 0.0F;

		boolean connect_XN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.EAST);
		boolean connect_XP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.WEST);
		boolean connect_YP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y + 1, z, ForgeDirection.DOWN);
		boolean connect_YN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y - 1, z, ForgeDirection.UP);
		boolean connect_ZN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.SOUTH);
		boolean connect_ZP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.NORTH);

		boolean isBarrierAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == BlockRegistry.blockCarpentersBarrierID;
		boolean isBarrierBelow = renderBlocks.blockAccess.getBlockId(x, y - 1, z) == BlockRegistry.blockCarpentersBarrierID;

		z_Low = 0.4375F;
		z_High = 0.5625F;
		y_High = connect_YP ? 1.0F : 0.6875F;

		// Render center post
		if (isPostAt(x, y, z)) {
			renderBlocks.setRenderBounds(z_Low, 0.0D, z_Low, z_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		z_Low = 0.4375F;
		z_High = 0.5625F;
		y_High = 0.6875F;
		y_Low = 0.625F;
		x_Low = connect_XN ? 0.0F : z_Low;
		x_High = connect_XP ? 1.0F : z_High;
		float zLow1 = connect_ZN ? 0.0F : z_Low;
		float zHigh1 = connect_ZP ? 1.0F : z_High;

		/*
		 * Upper horizontal plank
		 */
		if (!isBarrierAbove)
		{
			if (connect_XN || connect_XP) {
				renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
				renderBlock(block, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, zLow1, z_High, y_High, zHigh1);
				renderBlock(block, x, y, z);
			}
		}

		/*
		 * Lower horizontal plank
		 */
		if (!isBarrierBelow)
		{
			y_Low = 0.1875F;
			y_High = 0.25F;

			if (connect_XN || connect_XP) {
				renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
				renderBlock(block, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, zLow1, z_High, y_High, zHigh1);
				renderBlock(block, x, y, z);
			}
		}

		/*
		 * Vertical planks
		 */

		y_Low = connect_YN ? 0.0F : 0.0625F;

		/*
		 * Render center plank
		 */

		y_High = 1.0F;

		x_Low = 0.4375F;
		x_High = 0.5625F;
		z_Low = 0.5625F;
		z_High = 0.625F;
		if (!connect_ZP) {
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}
		z_Low -= 0.1875F;
		z_High -= 0.1875F;
		if (!connect_ZN) {
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		z_Low = 0.4375F;
		z_High = 0.5625F;
		x_Low = 0.5625F;
		x_High = 0.625F;
		if (!connect_XP) {
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}
		x_Low -= 0.1875F;
		x_High -= 0.1875F;
		if (!connect_XN) {
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_XN)
		{
			// Render inside plank
			y_High = connect_YP ? 1.0F : 0.875F;
			x_High = 0.3125F;
			x_Low = 0.1875F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			x_High = 0.0625F;
			x_Low = 0.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_XP)
		{
			// Render inside plank
			y_High = connect_YP ? 1.0F : 0.875F;
			x_Low = 0.6875F;
			x_High = 0.8125F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			x_Low = 0.9375F;
			x_High = 1.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_ZN)
		{
			// Render inside plank
			y_High = connect_YP ? 1.0F : 0.875F;
			z_High = 0.3125F;
			z_Low = 0.1875F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			z_High = 0.0625F;
			z_Low = 0.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_ZP)
		{
			// Render inside plank
			y_High = connect_YP ? 1.0F : 0.875F;
			z_Low = 0.6875F;
			z_High = 0.8125F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			z_Low = 0.9375F;
			z_High = 1.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
	}

	/**
	 * Renders wall block at given coordinates
	 */
	private void renderWall(Block block, int x, int y, int z)
	{
		BlockProperties.getData(TE);
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockRegistry.blockCarpentersBarrier;

		boolean connect_XN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.EAST);
		boolean connect_XP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.WEST);
		boolean connect_YP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y + 1, z, ForgeDirection.DOWN);
		blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y - 1, z, ForgeDirection.UP);
		boolean connect_ZN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.SOUTH);
		boolean connect_ZP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.NORTH);
		boolean connect_YZPN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y + 1, z - 1, ForgeDirection.SOUTH);
		boolean connect_YZPP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y + 1, z + 1, ForgeDirection.NORTH);
		boolean connect_XYNP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x - 1, y + 1, z, ForgeDirection.EAST);
		boolean connect_XYPP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x + 1, y + 1, z, ForgeDirection.WEST);

		/*
		 * Render center post.
		 */
		if (isPostAt(x, y, z) || isPostAt(x, y + 1, z)) {
			renderBlocks.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
			renderBlock(block, x, y, z);
		}

		/*
		 * Render connecting wall pieces
		 */
		if (connect_XN)
		{
			renderBlocks.setRenderBounds(0.0D, 0.0D, 0.3125D, 0.5D, connect_YP && connect_XYNP ? 1.0F : 0.8125D, 0.6875D);
			renderBlock(block, x, y, z);
		}

		if (connect_XP)
		{
			renderBlocks.setRenderBounds(0.5D, 0.0D, 0.3125D, 1.0D, connect_YP && connect_XYPP ? 1.0D : 0.8125D, 0.6875D);
			renderBlock(block, x, y, z);
		}

		if (connect_ZN)
		{
			renderBlocks.setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, connect_YP && connect_YZPN ? 1.0D : 0.8125D, 0.5D);
			renderBlock(block, x, y, z);
		}

		if (connect_ZP)
		{
			renderBlocks.setRenderBounds(0.3125D, 0.0D, 0.5D, 0.6875D, connect_YP && connect_YZPP ? 1.0D : 0.8125D, 1.0D);
			renderBlock(block, x, y, z);
		}
	}

	/**
	 * Renders wall block at given coordinates
	 */
	private void renderVerticalPlankFence(Block block, int x, int y, int z)
	{
		BlockProperties.getData(TE);
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockRegistry.blockCarpentersBarrier;

		float x_Low = 0.0F;
		float x_High = 0.0F;
		float y_Low = 0.0F;
		float y_High = 0.0F;
		float z_Low = 0.0F;
		float z_High = 0.0F;

		boolean connect_XN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.EAST);
		boolean connect_XP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.WEST);
		boolean connect_YP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y + 1, z, ForgeDirection.DOWN);
		blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y - 1, z, ForgeDirection.UP);
		boolean connect_ZN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z - 1, ForgeDirection.SOUTH);
		boolean connect_ZP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y, z + 1, ForgeDirection.NORTH);

		boolean isBarrierAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == BlockRegistry.blockCarpentersBarrierID;
		boolean isBarrierBelow = renderBlocks.blockAccess.getBlockId(x, y - 1, z) == BlockRegistry.blockCarpentersBarrierID;

		/*
		 * Center post
		 */
		y_High = 0.875F;

		if (connect_YP) {
			y_High = 1.0F;
		}

		z_Low = 0.4375F;
		z_High = 0.5625F;

		// Render center post
		if (isPostAt(x, y, z)) {
			renderBlocks.setRenderBounds(z_Low, 0.0D, z_Low, z_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		/*
		 * Horizontal supports
		 */

		float z_Offset = 0.0001F;
		z_Low = 0.4375F + z_Offset;
		z_High = 0.5625F - z_Offset;
		y_High = 0.875F;
		y_Low = 0.75F;
		x_Low = connect_XN ? 0.0F : z_High;
		x_High = connect_XP ? 1.0F : z_Low;
		float z_Min = connect_ZN ? 0.0F : z_Low;
		float z_Max = connect_ZP ? 1.0F : z_High;

		/*
		 * Upper horizontal plank
		 */
		if (!isBarrierAbove) {
			if (connect_XN || connect_XP) {
				renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
				renderBlock(block, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, z_Min, z_High, y_High, z_Max);
				renderBlock(block, x, y, z);
			}
		}

		/*
		 * Lower horizontal plank
		 */
		if (!isBarrierBelow) {
			y_Low = 0.125F;
			y_High = 0.25F;

			if (connect_XN || connect_XP) {
				renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
				renderBlock(block, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, z_Min, z_High, y_High, z_Max);
				renderBlock(block, x, y, z);
			}
		}

		/*
		 * Vertical planks
		 */

		y_Low = 0.0F;
		y_High = 1.0F;

		if (connect_XN)
		{
			// Render inside plank
			x_High = 0.5F;
			x_Low = 0.3125F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			x_High = 0.1875F;
			x_Low = 0.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_XP)
		{
			// Render inside plank
			x_Low = 0.5F;
			x_High = 0.6875F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			x_Low = 0.8125F;
			x_High = 1.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_ZN)
		{
			// Render inside plank
			z_High = 0.5F;
			z_Low = 0.3125F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			z_High = 0.1875F;
			z_Low = 0.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		if (connect_ZP)
		{
			// Render inside plank
			z_Low = 0.5F;
			z_High = 0.6875F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);

			// Render outside plank
			z_Low = 0.8125F;
			z_High = 1.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderBlock(block, x, y, z);
		}

		blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
	}

}