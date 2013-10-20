package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockCarpentersBarrier;
import carpentersblocks.data.Barrier;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;

public class BlockHandlerCarpentersBarrier extends BlockHandlerBase
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		for (int box = 0; box < 4; ++box)
		{
			switch (box) {
			case 0:
				renderBlocks.setRenderBounds(0.125D, 0.0D, 0.375D, 0.375D, 1.0D, 0.625D);
				break;
			case 1:
				renderBlocks.setRenderBounds(0.625D, 0.0D, 0.375D, 0.875D, 1.0D, 0.625D);
				break;
			case 2:
				renderBlocks.setRenderBounds(0.0D, 0.8125D, 0.4375D, 1.0D, 0.9375D, 0.5625D);
				break;
			case 3:
				renderBlocks.setRenderBounds(0.0D, 0.4375D, 0.4375D, 1.0D, 0.5625D, 0.5625D);
				break;
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
			tessellator.draw();
		}

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	/**
	 * Renders barrier
	 */
	 public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = isSideCover ? BlockProperties.getCoverBlock(TE, coverRendering) : BlockProperties.getCoverBlock(TE, 6);

		int data = BlockProperties.getData(TE);
		int type = Barrier.getType(data);

		switch (type) {
		case Barrier.TYPE_PICKET:
			renderPicketFence(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			break;
		case Barrier.TYPE_PLANK_VERTICAL:
			renderVerticalPlankFence(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			break;
		case Barrier.TYPE_WALL:
			renderWall(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			break;
		default:
			renderFence(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		return true;
	}

	/**
	 * Returns whether barrier has forced post or naturally forms a post at coordinates.
	 */
	private boolean isPostAt(IBlockAccess blockAccess, int x, int y, int z)
	{
		if (blockAccess.getBlockId(x, y, z) == BlockHandler.blockCarpentersBarrierID)
		{
			TECarpentersBlock TE = (TECarpentersBlock) blockAccess.getBlockTileEntity(x, y, z);

			BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockHandler.blockCarpentersBarrier;

			boolean connect_XN = blockRef.canConnectBarrierTo(TE, blockAccess, x - 1, y, z, ForgeDirection.EAST);
			boolean connect_XP = blockRef.canConnectBarrierTo(TE, blockAccess, x + 1, y, z, ForgeDirection.WEST);
			boolean connect_YP = blockRef.canConnectBarrierTo(TE, blockAccess, x, y + 1, z, ForgeDirection.UP);
			boolean connect_ZN = blockRef.canConnectBarrierTo(TE, blockAccess, x, y, z - 1, ForgeDirection.SOUTH);
			boolean connect_ZP = blockRef.canConnectBarrierTo(TE, blockAccess, x, y, z + 1, ForgeDirection.NORTH);

			boolean pathOnX = connect_XN && connect_XP;
			boolean pathOnZ = connect_ZN && connect_ZP;

			return	Barrier.getPost(BlockProperties.getData(TE)) == Barrier.HAS_POST ||
					pathOnX == pathOnZ ||
					connect_YP ||
					(connect_XN || connect_XP) && (connect_ZN || connect_ZP);
		}

		return false;
	}

	/**
	 * Renders vanilla fence at given coordinates
	 */
	public boolean renderFence(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockHandler.blockCarpentersBarrier;

		boolean connect_XN = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x - 1, y, z, ForgeDirection.EAST);
		boolean connect_XP = blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x + 1, y, z, ForgeDirection.WEST);
		blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y + 1, z, ForgeDirection.DOWN);
		blockRef.canConnectBarrierTo(TE, renderBlocks.blockAccess, x, y - 1, z, ForgeDirection.UP);
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

		float yPlankOffset = Barrier.getType(data) * 0.0625F;
		boolean joinPlanks = Barrier.getType(data) == Barrier.TYPE_VANILLA_X3;

		// Render center post
		if (isPostAt(renderBlocks.blockAccess, x, y, z) || isPostAt(renderBlocks.blockAccess, x, y + 1, z)) {
			renderBlocks.setRenderBounds(z_Low, 0.0D, z_Low, z_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
			renderBlocks.setRenderBounds(x_Low, joinPlanks ? 0.1875F : (y_Low - yPlankOffset), z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		if (connect_ZN || connect_ZP) {
			renderBlocks.setRenderBounds(z_Low, joinPlanks ? 0.1875F : (y_Low - yPlankOffset), zLow1, z_High, y_High, zHigh1);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low - yPlankOffset, zLow1, z_High, y_High, zHigh1);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
		}

		blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
		return true;
	}

	/**
	 * Renders picket fence block at given coordinates
	 */
	public boolean renderPicketFence(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockHandler.blockCarpentersBarrier;
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

		boolean isBarrierAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == BlockHandler.blockCarpentersBarrierID;
		boolean isBarrierBelow = renderBlocks.blockAccess.getBlockId(x, y - 1, z) == BlockHandler.blockCarpentersBarrierID;

		z_Low = 0.4375F;
		z_High = 0.5625F;
		y_High = connect_YP ? 1.0F : 0.6875F;

		// Render center post
		if (isPostAt(renderBlocks.blockAccess, x, y, z)) {
			renderBlocks.setRenderBounds(z_Low, 0.0D, z_Low, z_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, zLow1, z_High, y_High, zHigh1);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, zLow1, z_High, y_High, zHigh1);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}
		z_Low -= 0.1875F;
		z_High -= 0.1875F;
		if (!connect_ZN) {
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		z_Low = 0.4375F;
		z_High = 0.5625F;
		x_Low = 0.5625F;
		x_High = 0.625F;
		if (!connect_XP) {
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}
		x_Low -= 0.1875F;
		x_High -= 0.1875F;
		if (!connect_XN) {
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			x_High = 0.0625F;
			x_Low = 0.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			x_Low = 0.9375F;
			x_High = 1.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			z_High = 0.0625F;
			z_Low = 0.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			y_High = connect_YP ? 1.0F : 0.8125F;
			z_Low = 0.9375F;
			z_High = 1.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
		return true;
	}

	/**
	 * Renders wall block at given coordinates
	 */
	public boolean renderWall(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		BlockProperties.getData(TE);
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockHandler.blockCarpentersBarrier;

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
		 if (isPostAt(renderBlocks.blockAccess, x, y, z) || isPostAt(renderBlocks.blockAccess, x, y + 1, z)) {
			 renderBlocks.setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
			 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		 }

		 /*
		  * Render connecting wall pieces
		  */
		 if (connect_XN)
		 {
			 renderBlocks.setRenderBounds(0.0D, 0.0D, 0.3125D, 0.5D, connect_YP && connect_XYNP ? 1.0F : 0.8125D, 0.6875D);
			 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		 }

		 if (connect_XP)
		 {
			 renderBlocks.setRenderBounds(0.5D, 0.0D, 0.3125D, 1.0D, connect_YP && connect_XYPP ? 1.0D : 0.8125D, 0.6875D);
			 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		 }

		 if (connect_ZN)
		 {
			 renderBlocks.setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, connect_YP && connect_YZPN ? 1.0D : 0.8125D, 0.5D);
			 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		 }

		 if (connect_ZP)
		 {
			 renderBlocks.setRenderBounds(0.3125D, 0.0D, 0.5D, 0.6875D, connect_YP && connect_YZPP ? 1.0D : 0.8125D, 1.0D);
			 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		 }

		 return true;
	}

	/**
	 * Renders wall block at given coordinates
	 */
	public boolean renderVerticalPlankFence(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		BlockProperties.getData(TE);
		BlockCarpentersBarrier blockRef = (BlockCarpentersBarrier) BlockHandler.blockCarpentersBarrier;

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

		boolean isBarrierAbove = renderBlocks.blockAccess.getBlockId(x, y + 1, z) == BlockHandler.blockCarpentersBarrierID;
		boolean isBarrierBelow = renderBlocks.blockAccess.getBlockId(x, y - 1, z) == BlockHandler.blockCarpentersBarrierID;

		/*
		 * Center post
		 */
		 y_High = 0.875F;

		if (connect_YP)
			y_High = 1.0F;

		z_Low = 0.4375F;
		z_High = 0.5625F;

		// Render center post
		if (isPostAt(renderBlocks.blockAccess, x, y, z)) {
			renderBlocks.setRenderBounds(z_Low, 0.0D, z_Low, z_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, z_Min, z_High, y_High, z_Max);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			if (connect_ZN || connect_ZP) {
				renderBlocks.setRenderBounds(z_Low, y_Low, z_Min, z_High, y_High, z_Max);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
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
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			x_High = 0.1875F;
			x_Low = 0.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		if (connect_XP)
		{
			// Render inside plank
			x_Low = 0.5F;
			x_High = 0.6875F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			x_Low = 0.8125F;
			x_High = 1.0F;
			z_High = 0.625F;
			z_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			z_Low -= 0.1875F;
			z_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		if (connect_ZN)
		{
			// Render inside plank
			z_High = 0.5F;
			z_Low = 0.3125F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			z_High = 0.1875F;
			z_Low = 0.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		if (connect_ZP)
		{
			// Render inside plank
			z_Low = 0.5F;
			z_High = 0.6875F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			// Render outside plank
			z_Low = 0.8125F;
			z_High = 1.0F;
			x_High = 0.625F;
			x_Low = 0.5625F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			x_Low -= 0.1875F;
			x_High -= 0.1875F;
			renderBlocks.setRenderBounds(x_Low, y_Low, z_Low, x_High, y_High, z_High);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
		return true;
	}

}