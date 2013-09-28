package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockBase;
import carpentersblocks.block.BlockCarpentersStairs;
import carpentersblocks.data.Stairs;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;

public class BlockHandlerCarpentersStairs extends BlockHandlerBase
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{
		Tessellator tessellator = Tessellator.instance;
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		renderBlocks.setRenderBounds(0, 0, 0, 1.0D, 1.0D, 1.0D);

		for (int box = 0; box < 2; ++box)
		{
			switch (box) {
			case 0:
				renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
				break;
			case 1:
				renderBlocks.setRenderBounds(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
				break;
			}

			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(0));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(1));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(2));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(3));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(4));
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, ((BlockBase)block).getDefaultIconFromSide(5));
			tessellator.draw();
		}

		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	/**
	 * Renders stairs at the given coordinates
	 */
	 public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = isSideCover ? BlockProperties.getCoverBlock(TE, coverRendering) : BlockProperties.getCoverBlock(TE, 6);

		int stairsID = BlockProperties.getData(TE);
		Stairs stairs = Stairs.stairsList[stairsID];

		BlockCarpentersStairs blockRef = (BlockCarpentersStairs) BlockHandler.blockCarpentersStairs;

		for (int box = 0; box < 3; ++box)
		{
			float[] bounds = blockRef.genBounds(box, stairs);

			if (bounds != null)
			{
				blockRef.setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
				renderBlocks.setRenderBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
		}

		return true;
	}

	@Override
	/**
	 * Renders side covers (stair specific).
	 */
	protected void renderSideCovers(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		isSideCover = true;
		renderBlocks.renderAllFaces = true;

		int stairsID = BlockProperties.getData(TE);
		Stairs stairs = Stairs.stairsList[stairsID];

		BlockCarpentersStairs blockRef = (BlockCarpentersStairs) BlockHandler.blockCarpentersStairs;

		for (int box = 0; box < 3; ++box)
		{
			float[] bounds = blockRef.genBounds(box, stairs);

			if (bounds != null)
			{
				for (int side = 0; side < 6; ++side)
				{
					Block coverBlock = BlockProperties.getCoverBlock(TE, side);

					coverRendering = side;

					if (
							BlockProperties.hasCover(TE, side) &&
							(shouldRenderBlock(TE, renderBlocks, coverBlock, srcBlock, renderPass)  || shouldRenderPattern(TE, renderPass))
							)
					{
						renderBlocks.setRenderBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
						int[] renderOffset = setSideCoverRenderBounds(TE, renderBlocks, x, y, z, side);

						if (clipSideCoverBoundsBasedOnState(renderBlocks, stairsID, box, side))
							renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, renderOffset[0], renderOffset[1], renderOffset[2]);
					}
				}
			}
		}

		renderBlocks.renderAllFaces = false;
		coverRendering = 6;
		isSideCover = false;
	}

	/**
	 * Alters side cover render bounds to prevent it from intersecting the block mask.
	 */
	private boolean clipSideCoverBoundsBasedOnState(RenderBlocks renderBlocks, int data, int box, int side)
	{
		++box;

		switch (data) {
		case Stairs.ID_NORMAL_POS_N:
			switch (box) {
			case 1:
				if (side == 2)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 3)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_POS_W:
			switch (box) {
			case 1:
				if (side == 4)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_POS_E:
			switch (box) {
			case 1:
				if (side == 5)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_POS_S:
			switch (box) {
			case 1:
				if (side == 3)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 2)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_NEG_N:
			switch (box) {
			case 1:
				if (side == 2)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 3)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_NEG_W:
			switch (box) {
			case 1:
				if (side == 4)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_NEG_E:
			switch (box) {
			case 1:
				if (side == 5)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_NEG_S:
			switch (box) {
			case 1:
				if (side == 3)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 2)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_NW:
			switch (box) {
			case 1:
				if (side == 4) {
					renderBlocks.renderMaxZ -= 0.5D;
					renderBlocks.renderMinY += 0.5D;
				}
				break;
			case 2:
				if (side == 2)
					renderBlocks.renderMinY += 0.5D;
				else if (side == 5)
					return false;
				break;
			case 3:
				if (side == 3 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_SW:
			switch (box) {
			case 1:
				if (side == 4) {
					renderBlocks.renderMinZ += 0.5D;
					renderBlocks.renderMinY += 0.5D;
				}
				break;
			case 2:
				if (side == 3)
					renderBlocks.renderMinY += 0.5D;
				else if (side == 5)
					return false;
				break;
			case 3:
				if (side == 2 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_NE:
			switch (box) {
			case 1:
				if (side == 5) {
					renderBlocks.renderMaxZ -= 0.5D;
					renderBlocks.renderMinY += 0.5D;
				}
				break;
			case 2:
				if (side == 2)
					renderBlocks.renderMinY += 0.5D;
				else if (side == 4)
					return false;
				break;
			case 3:
				if (side == 3 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_POS_SE:
			switch (box) {
			case 1:
				if (side == 5) {
					renderBlocks.renderMinZ += 0.5D;
					renderBlocks.renderMinY += 0.5D;
				}
				break;
			case 2:
				if (side == 3)
					renderBlocks.renderMinY += 0.5D;
				else if (side == 4)
					return false;
				break;
			case 3:
				if (side == 2 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_NW:
			switch (box) {
			case 1:
				if (side == 4) {
					renderBlocks.renderMaxZ -= 0.5D;
					renderBlocks.renderMaxY -= 0.5D;
				}
				break;
			case 2:
				if (side == 2)
					renderBlocks.renderMaxY -= 0.5D;
				else if (side == 5)
					return false;
				break;
			case 3:
				if (side == 3 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_SW:
			switch (box) {
			case 1:
				if (side == 4) {
					renderBlocks.renderMinZ += 0.5D;
					renderBlocks.renderMaxY -= 0.5D;
				}
				break;
			case 2:
				if (side == 3)
					renderBlocks.renderMaxY -= 0.5D;
				else if (side == 5)
					return false;
				break;
			case 3:
				if (side == 2 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_NE:
			switch (box) {
			case 1:
				if (side == 5) {
					renderBlocks.renderMaxZ -= 0.5D;
					renderBlocks.renderMaxY -= 0.5D;
				}
				break;
			case 2:
				if (side == 2)
					renderBlocks.renderMaxY -= 0.5D;
				else if (side == 4)
					return false;
				break;
			case 3:
				if (side == 3 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_INT_NEG_SE:
			switch (box) {
			case 1:
				if (side == 5) {
					renderBlocks.renderMinZ += 0.5D;
					renderBlocks.renderMaxY -= 0.5D;
				}
				break;
			case 2:
				if (side == 3)
					renderBlocks.renderMaxY -= 0.5D;
				else if (side == 4)
					return false;
				break;
			case 3:
				if (side == 2 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_NW:
			switch (box) {
			case 1:
				if (side == 2 || side == 4)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			case 3:
				if (side == 3 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_SW:
			switch (box) {
			case 1:
				if (side == 3 || side == 4)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			case 3:
				if (side == 2 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_NE:
			switch (box) {
			case 1:
				if (side == 2 || side == 5)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			case 3:
				if (side == 3 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_POS_SE:
			switch (box) {
			case 1:
				if (side == 3 || side == 5)
					renderBlocks.renderMinY += 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			case 3:
				if (side == 2 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_NW:
			switch (box) {
			case 1:
				if (side == 2 || side == 4)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			case 3:
				if (side == 3 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_SW:
			switch (box) {
			case 1:
				if (side == 3 || side == 4)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			case 3:
				if (side == 2 || side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_NE:
			switch (box) {
			case 1:
				if (side == 2 || side == 5)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			case 3:
				if (side == 3 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_EXT_NEG_SE:
			switch (box) {
			case 1:
				if (side == 3 || side == 5)
					renderBlocks.renderMaxY -= 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			case 3:
				if (side == 2 || side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_SW:
			switch (box) {
			case 1:
				if (side == 4)
					renderBlocks.renderMinZ += 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_NW:
			switch (box) {
			case 1:
				if (side == 4)
					renderBlocks.renderMaxZ -= 0.5D;
				break;
			case 2:
				if (side == 5)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_NE:
			switch (box) {
			case 1:
				if (side == 5)
					renderBlocks.renderMaxZ -= 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			}
			break;
		case Stairs.ID_NORMAL_SE:
			switch (box) {
			case 1:
				if (side == 5)
					renderBlocks.renderMinZ += 0.5D;
				break;
			case 2:
				if (side == 4)
					return false;
				break;
			}
			break;
		}

		return true;
	}

}