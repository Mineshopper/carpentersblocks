package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockCarpentersHatch;
import carpentersblocks.data.Hatch;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.IconHandler;

public class BlockHandlerCarpentersHatch extends HingedBase
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
				renderBlocks.setRenderBounds(0.0D, 0.2D, 0.0D, 1.0D, 0.3875D, 1.0D);
				break;
			case 1:
				block = Block.stone;
				renderBlocks.setRenderBounds(0.125D, 0.3875D, 0.375D, 0.1875D, 0.45D, 0.4375D);
				break;
			case 2:
				renderBlocks.setRenderBounds(0.5625D, 0.3875D, 0.125D, 0.625D, 0.45D, 0.1875D);
				break;
			case 3:
				renderBlocks.setRenderBounds(0.375D, 0.45D, 0.125D, 0.625D, 0.5125D, 0.1875D);
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
	 * Renders block
	 */
	 public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = isSideCover ? BlockProperties.getCoverBlock(TE, coverRendering) : BlockProperties.getCoverBlock(TE, 6);

		int data = BlockProperties.getData(TE);
		int type = Hatch.getType(data);

		switch (type) {
		case Hatch.TYPE_WINDOW:
			renderHollowHatch(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Hatch.TYPE_SCREEN:
			renderHollowHatch(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Hatch.TYPE_FRENCH_WINDOW:
			renderFrenchWindowHatch(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Hatch.TYPE_PANEL:
			renderPanelHatch(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Hatch.TYPE_HIDDEN:
			renderHiddenHatch(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		}

		return true;
	}

	/**
	 * Renders hidden hatch at given coordinates
	 */
	public boolean renderHiddenHatch(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		{
			BlockCarpentersHatch blockRef = (BlockCarpentersHatch) BlockHandler.blockCarpentersHatch;
			blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
			renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, false);

		return true;
	}

	/**
	 * Renders a window or screen hatch at given coordinates
	 */
	public boolean renderHollowHatch(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		renderBlocks.renderAllFaces = true;

		int data = BlockProperties.getData(TE);
		int dir = Hatch.getDir(data);
		int pos = Hatch.getPos(data);
		int state = Hatch.getState(data);

		boolean	path_on_x = false;
		boolean path_on_y = false;
		float path_offset = 0.0F;

		float x_low = 0.0F;
		float y_low = 0.0F;
		float z_low = 0.0F;
		float x_high = 1.0F;
		float y_high = 1.0F;
		float z_high = 1.0F;

		/*
		 * Set offsets
		 */
		 if (state == Hatch.STATE_CLOSED) {
			 path_on_y = true;
			 if (pos == Hatch.POSITION_LOW) {
				 y_high = 0.1875F;
				 path_offset = 0.09375F;
			 } else {
				 y_low = 0.8125F;
				 path_offset = 0.90625F;
			 }
		 } else {
			 switch (dir)
			 {
			 case Hatch.DIR_X_NEG:
				 x_low = 0.8125F;
				 path_offset = 0.09375F;
				 break;
			 case Hatch.DIR_X_POS:
				 x_high = 0.1875F;
				 path_offset = 0.90625F;
				 break;
			 case Hatch.DIR_Z_NEG:
				 z_low = 0.8125F;
				 path_on_x = true;
				 path_offset = 0.09375F;
				 break;
			 case Hatch.DIR_Z_POS:
				 z_high = 0.1875F;
				 path_on_x = true;
				 path_offset = 0.90625F;
				 break;
			 }
		 }

		 if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		 {
			 /*
			  * Draw sides
			  */
			 if (path_on_x) {

				 renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, 0.0F, z_low, 0.8125F, 0.1875F, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, 0.8125F, z_low, 0.8125F, 1.0F, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else if (path_on_y) {

				 renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, y_low, 0.0F, 0.8125F, y_high, 0.1875F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, y_low, 0.8125F, 0.8125F, y_high, 1.0F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else {

				 renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_high, y_high, 0.1875F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, y_low, 0.8125F, x_high, y_high, 1.0F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, 0.0F, 0.1875F, x_high, 0.1875F, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, 0.8125F, 0.1875F, x_high, 1.0F, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 }
		 }

		 if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
		 {
			 Icon icon;

			 if (Hatch.getType(data) == Hatch.TYPE_SCREEN)
				 icon = IconHandler.icon_hatch_screen;
			 else
				 icon = IconHandler.icon_hatch_glass;

			 Tessellator.instance.setBrightness(Block.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

			 if (path_on_x) {
				 VertexHelper.setOffset(-(1 - path_offset));
				 Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);
				 renderBlocks.setRenderBounds(0.1875F, 0.1875F, 0.0F, 0.8125F, 0.8125F, 1.0F);
				 RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
				 VertexHelper.setOffset(-path_offset);
				 RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
			 } else if (path_on_y) {
				 VertexHelper.setOffset(-path_offset);
				 Tessellator.instance.setColorOpaque_F(0.5F, 0.5F, 0.5F);
				 renderBlocks.setRenderBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
				 RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
				 VertexHelper.setOffset(-(1 - path_offset));
				 Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				 RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
			 } else {
				 VertexHelper.setOffset(-(1 - path_offset));
				 Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);
				 renderBlocks.setRenderBounds(0.0F, 0.1875F, 0.1875F, 1.0F, 0.8125F, 0.8125F);
				 RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
				 VertexHelper.setOffset(-path_offset);
				 RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
			 }

			 VertexHelper.clearOffset();

			 renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, true);
		 }

		 return true;
	}

	/**
	 * Renders a French window hatch at given coordinates
	 */
	public boolean renderFrenchWindowHatch(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		renderBlocks.renderAllFaces = true;

		int data = BlockProperties.getData(TE);
		int dir = Hatch.getDir(data);
		int pos = Hatch.getPos(data);
		int state = Hatch.getState(data);

		boolean	path_on_x = false;
		boolean	path_on_y = false;
		float path_offset = 0.0F;

		float x_low = 0.0F;
		float y_low = 0.0F;
		float z_low = 0.0F;
		float x_high = 1.0F;
		float y_high = 1.0F;
		float z_high = 1.0F;

		/*
		 * Set offsets
		 */
		 if (state == Hatch.STATE_CLOSED) {
			 path_on_y = true;
			 if (pos == Hatch.POSITION_LOW) {
				 y_high = 0.1875F;
				 path_offset = 0.09375F;
			 } else {
				 y_low = 0.8125F;
				 path_offset = 0.90625F;
			 }
		 } else {
			 switch (dir)
			 {
			 case Hatch.DIR_X_NEG:
				 x_low = 0.8125F;
				 path_offset = 0.09375F;
				 break;
			 case Hatch.DIR_X_POS:
				 x_high = 0.1875F;
				 path_offset = 0.90625F;
				 break;
			 case Hatch.DIR_Z_NEG:
				 z_low = 0.8125F;
				 path_on_x = true;
				 path_offset = 0.09375F;
				 break;
			 case Hatch.DIR_Z_POS:
				 z_high = 0.1875F;
				 path_on_x = true;
				 path_offset = 0.90625F;
				 break;
			 }
		 }

		 if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		 {
			 /*
			  * Draw sides
			  */
			 if (path_on_x) {

				 renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, 0.0F, z_low, 0.8125F, 0.1875F, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, 0.8125F, z_low, 0.8125F, 1.0F, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else if (path_on_y) {

				 renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, y_low, 0.0F, 0.8125F, y_high, 0.1875F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, y_low, 0.8125F, 0.8125F, y_high, 1.0F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else {

				 renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_high, y_high, 0.1875F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, y_low, 0.8125F, x_high, y_high, 1.0F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, 0.0F, 0.1875F, x_high, 0.1875F, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, 0.8125F, 0.1875F, x_high, 1.0F, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 }

			 float temp_x_low = x_low;
			 float temp_x_high = x_high;
			 float temp_y_low = y_low;
			 float temp_y_high = y_high;
			 float temp_z_low = z_low;
			 float temp_z_high = z_high;

			 setLightnessOffset(-0.05F);

			 if (path_on_x) {

				 temp_z_low += 0.0625F;
				 temp_z_high -= 0.0625F;
				 renderBlocks.setRenderBounds(0.1875F, 0.4375F, temp_z_low, 0.8125F, 0.5625F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.4375F, 0.1875F, temp_z_low, 0.5625F, 0.4375F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.4375F, 0.5625F, temp_z_low, 0.5625F, 0.8125F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else if (path_on_y) {

				 temp_y_low += 0.0625F;
				 temp_y_high -= 0.0625F;
				 renderBlocks.setRenderBounds(0.1875F, temp_y_low, 0.4375F, 0.8125F, temp_y_high, 0.5625F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.4375F, temp_y_low, 0.1875F, 0.5625F, temp_y_high, 0.4375F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.4375F, temp_y_low, 0.5625F, 0.5625F, temp_y_high, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else {

				 temp_x_low += 0.0625F;
				 temp_x_high -= 0.0625F;
				 renderBlocks.setRenderBounds(temp_x_low, 0.4375F, 0.1875F, temp_x_high, 0.5625F, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(temp_x_low, 0.1875F, 0.4375F, temp_x_high, 0.4375F, 0.5625F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(temp_x_low, 0.5625F, 0.4375F, temp_x_high, 0.8125F, 0.5625F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 }

			 clearLightnessOffset();
		 }

		 if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
		 {
			 Tessellator.instance.setBrightness(Block.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

			 if (path_on_x) {
				 VertexHelper.setOffset(-(1 - path_offset));
				 Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);
				 renderBlocks.setRenderBounds(0.1875F, 0.1875F, 0.0F, 0.8125F, 0.8125F, 1.0F);
				 RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, IconHandler.icon_hatch_french_glass);
				 VertexHelper.setOffset(-path_offset);
				 RenderHelper.renderFaceZPos(renderBlocks, x, y, z, IconHandler.icon_hatch_french_glass);
			 } else if (path_on_y) {
				 VertexHelper.setOffset(-path_offset);
				 Tessellator.instance.setColorOpaque_F(0.5F, 0.5F, 0.5F);
				 renderBlocks.setRenderBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
				 RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, IconHandler.icon_hatch_french_glass);
				 VertexHelper.setOffset(-(1 - path_offset));
				 Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
				 RenderHelper.renderFaceYPos(renderBlocks, x, y, z, IconHandler.icon_hatch_french_glass);
			 } else {
				 VertexHelper.setOffset(-(1 - path_offset));
				 Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);
				 renderBlocks.setRenderBounds(0.0F, 0.1875F, 0.1875F, 1.0F, 0.8125F, 0.8125F);
				 RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, IconHandler.icon_hatch_french_glass);
				 VertexHelper.setOffset(-path_offset);
				 RenderHelper.renderFaceXPos(renderBlocks, x, y, z, IconHandler.icon_hatch_french_glass);
			 }

			 VertexHelper.clearOffset();

			 renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, true);
		 }

		 return true;
	}

	/**
	 * Renders a panel hatch at given coordinates
	 */
	public boolean renderPanelHatch(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		renderBlocks.renderAllFaces = true;

		int data = BlockProperties.getData(TE);
		int dir = Hatch.getDir(data);
		int pos = Hatch.getPos(data);
		int state = Hatch.getState(data);

		boolean	path_on_x = false;
		boolean	path_on_y = false;
		float x_low = 0.0F;
		float y_low = 0.0F;
		float z_low = 0.0F;
		float x_high = 1.0F;
		float y_high = 1.0F;
		float z_high = 1.0F;

		/*
		 * Set offsets
		 */
		 if (state == Hatch.STATE_CLOSED) {
			 path_on_y = true;
			 if (pos == Hatch.POSITION_LOW) {
				 y_high = 0.1875F;
			 } else {
				 y_low = 0.8125F;
			 }
		 } else {
			 switch (dir)
			 {
			 case Hatch.DIR_X_NEG:
				 x_low = 0.8125F;
				 break;
			 case Hatch.DIR_X_POS:
				 x_high = 0.1875F;
				 break;
			 case Hatch.DIR_Z_NEG:
				 z_low = 0.8125F;
				 path_on_x = true;
				 break;
			 case Hatch.DIR_Z_POS:
				 z_high = 0.1875F;
				 path_on_x = true;
				 break;
			 }
		 }

		 if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		 {
			 /*
			  * Draw sides
			  */
			 if (path_on_x) {

				 renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, 0.0F, z_low, 0.8125F, 0.1875F, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, 0.8125F, z_low, 0.8125F, 1.0F, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else if (path_on_y) {

				 renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, y_low, 0.0F, 0.8125F, y_high, 0.1875F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(0.1875F, y_low, 0.8125F, 0.8125F, y_high, 1.0F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 } else {

				 renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_high, y_high, 0.1875F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, y_low, 0.8125F, x_high, y_high, 1.0F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, 0.0F, 0.1875F, x_high, 0.1875F, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(x_low, 0.8125F, 0.1875F, x_high, 1.0F, 0.8125F);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 }

			 float temp_x_low = x_low;
			 float temp_x_high = x_high;
			 float temp_y_low = y_low;
			 float temp_y_high = y_high;
			 float temp_z_low = z_low;
			 float temp_z_high = z_high;

			 /*
			  * Draw thin center panel
			  */
			 if (path_on_x) {
				 temp_x_low = 0.1875F;
				 temp_x_high = 0.8125F;
				 temp_y_low = 0.1875F;
				 temp_y_high = 0.8125F;
				 temp_z_low += 0.0625F;
				 temp_z_high -= 0.0625F;
			 } else if (path_on_y) {
				 temp_x_low = 0.1875F;
				 temp_x_high = 0.8125F;
				 temp_y_low += 0.0625F;
				 temp_y_high -= 0.0625F;
				 temp_z_low = 0.1875F;
				 temp_z_high = 0.8125F;
			 } else {
				 temp_x_low += 0.0625F;
				 temp_x_high -= 0.0625F;
				 temp_y_low = 0.1875F;
				 temp_y_high = 0.8125F;
				 temp_z_low = 0.1875F;
				 temp_z_high = 0.8125F;
			 }

			 setLightnessOffset(-0.05F);

			 renderBlocks.setRenderBounds(temp_x_low, temp_y_low, temp_z_low, temp_x_high, temp_y_high, temp_z_high);
			 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			 clearLightnessOffset();

			 temp_x_low = x_low;
			 temp_x_high = x_high;
			 temp_y_low = y_low;
			 temp_y_high = y_high;
			 temp_z_low = z_low;
			 temp_z_high = z_high;

			 /*
			  * Draw center panel
			  */

			 if (path_on_x) {
				 temp_x_low = 0.3125F;
				 temp_x_high = 0.6875F;
				 temp_y_low = 0.3125F;
				 temp_y_high = 0.6875F;
			 } else if (path_on_y) {
				 temp_x_low = 0.3125F;
				 temp_x_high = 0.6875F;
				 temp_z_low = 0.3125F;
				 temp_z_high = 0.6875F;
			 } else {
				 temp_y_low = 0.3125F;
				 temp_y_high = 0.6875F;
				 temp_z_low = 0.3125F;
				 temp_z_high = 0.6875F;
			 }

			 renderBlocks.setRenderBounds(temp_x_low, temp_y_low, temp_z_low, temp_x_high, temp_y_high, temp_z_high);
			 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		 }

		 if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
			 renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, true);

		 return true;
	}

	/**
	 * Renders a hatch handle for the given coordinates
	 */
	public boolean renderHandle(TECarpentersBlock TE, RenderBlocks renderBlocks, Block block, int x, int y, int z, boolean render_inside_handle, boolean render_outside_handle)
	{
		if (!render_inside_handle && !render_outside_handle)
			return false;

		renderBlocks.renderAllFaces = true;

		int data = BlockProperties.getData(TE);
		int dir = Hatch.getDir(data);
		int pos = Hatch.getPos(data);
		int state = Hatch.getState(data);

		if (pos == Hatch.POSITION_LOW)
		{
			if (state == Hatch.STATE_CLOSED)
			{
				switch (dir) {
				case Hatch.DIR_X_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.0625F, 0.1875F, 0.375F, 0.125F, 0.25F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0625F, 0.1875F, 0.5625F, 0.125F, 0.25F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0625F, 0.25F, 0.375F, 0.125F, 0.3125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.0625F, 0.9375F, 0.375F, 0.125F, 1.0F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.0625F, 0.9375F, 0.5625F, 0.125F, 1.0F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.0625F, 0.875F, 0.375F, 0.125F, 0.9375F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
					}

					break;
				case Hatch.DIR_X_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.875F, 0.1875F, 0.375F, 0.9375F, 0.25F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875F, 0.1875F, 0.5625F, 0.9375F, 0.25F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875F, 0.25F, 0.375F, 0.9375F, 0.3125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.875F, 0.9375F, 0.375F, 0.9375F, 1.0F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.875F, 0.9375F, 0.5625F, 0.9375F, 1.0F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.875F, 0.875F, 0.375F, 0.9375F, 0.9375F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
					}

					break;
				case Hatch.DIR_Z_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.1875F, 0.0625F, 0.4375F, 0.25F, 0.125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.1875F, 0.0625F, 0.625F, 0.25F, 0.125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.25F, 0.0625F, 0.625F, 0.3125F, 0.125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.9375F, 0.0625F, 0.4375F, 1.0F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.5625F, 0.9375F, 0.0625F, 0.625F, 1.0F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.0625F, 0.625F, 0.9375F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
					}

					break;
				case Hatch.DIR_Z_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.1875F, 0.875F, 0.4375F, 0.25F, 0.9375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.1875F, 0.875F, 0.625F, 0.25F, 0.9375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.25F, 0.875F, 0.625F, 0.3125F, 0.9375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.9375F, 0.875F, 0.4375F, 1.0F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.5625F, 0.9375F, 0.875F, 0.625F, 1.0F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.875F, 0.625F, 0.9375F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y - 1, z);
					}

					break;
				}

			} else {

				switch (dir) {
				case Hatch.DIR_X_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.0F, 0.875F, 0.375F, 0.0625F, 0.9375F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x + 1, y, z);
						renderBlocks.setRenderBounds(0.0F, 0.875F, 0.5625F, 0.0625F, 0.9375F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x + 1, y, z);
						renderBlocks.setRenderBounds(0.0625F, 0.875F, 0.375F, 0.125F, 0.9375F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x + 1, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.75F, 0.875F, 0.375F, 0.8125F, 0.9375F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.75F, 0.875F, 0.5625F, 0.8125F, 0.9375F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.6875F, 0.875F, 0.375F, 0.75F, 0.9375F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				case Hatch.DIR_X_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.9375F, 0.875F, 0.375F, 1.0F, 0.9375F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x - 1, y, z);
						renderBlocks.setRenderBounds(0.9375F, 0.875F, 0.5625F, 1.0F, 0.9375F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x - 1, y, z);
						renderBlocks.setRenderBounds(0.875F, 0.875F, 0.375F, 0.9375F, 0.9375F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x - 1, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.1875F, 0.875F, 0.375F, 0.25F, 0.9375F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.1875F, 0.875F, 0.5625F, 0.25F, 0.9375F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.25F, 0.875F, 0.375F, 0.3125F, 0.9375F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				case Hatch.DIR_Z_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.0F, 0.4375F, 0.9375F, 0.0625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z + 1);
						renderBlocks.setRenderBounds(0.5625F, 0.875F, 0.0F, 0.625F, 0.9375F, 0.0625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z + 1);
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.0625F, 0.625F, 0.9375F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z + 1);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.75F, 0.4375F, 0.9375F, 0.8125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.875F, 0.75F, 0.625F, 0.9375F, 0.8125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.6875F, 0.625, 0.9375F, 0.75F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				case Hatch.DIR_Z_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.9375F, 0.4375F, 0.9375F, 1.0F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z - 1);
						renderBlocks.setRenderBounds(0.5625F, 0.875F, 0.9375F, 0.625F, 0.9375F, 1.0F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z - 1);
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.875F, 0.625, 0.9375F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z - 1);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.1875F, 0.4375F, 0.9375F, 0.25F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.875F, 0.1875F, 0.625F, 0.9375F, 0.25F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.875F, 0.25F, 0.625F, 0.9375F, 0.3125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				}
			}

		} else {

			if (state == Hatch.STATE_CLOSED)
			{
				switch (dir) {
				case Hatch.DIR_X_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.0625F, 0.75F, 0.375F, 0.125F, 0.8125F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0625F, 0.75F, 0.5625F, 0.125F, 0.8125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.0625F, 0.6875F, 0.375F, 0.125F, 0.75F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.0625F, 0.0F, 0.375F, 0.125F, 0.0625F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.0625F, 0.0F, 0.5625F, 0.125F, 0.0625F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.0625F, 0.0625F, 0.375F, 0.125F, 0.125F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
					}

					break;
				case Hatch.DIR_X_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.875F, 0.75F, 0.375F, 0.9375F, 0.8125F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875F, 0.75F, 0.5625F, 0.9375F, 0.8125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.875F, 0.6875F, 0.375F, 0.9375F, 0.75F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.875F, 0.0F, 0.375F, 0.9375F, 0.0625F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.875F, 0.0F, 0.5625F, 0.9375F, 0.0625F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.875F, 0.0625F, 0.375F, 0.9375F, 0.125F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
					}

					break;
				case Hatch.DIR_Z_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.75F, 0.0625F, 0.4375F, 0.8125F, 0.125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.75F, 0.0625F, 0.625F, 0.8125F, 0.125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.6875F, 0.0625F, 0.625F, 0.75F, 0.125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.0F, 0.0625F, 0.4375F, 0.0625F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.5625F, 0.0F, 0.0625F, 0.625F, 0.0625F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.0625F, 0.625F, 0.125F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
					}

					break;
				case Hatch.DIR_Z_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.75F, 0.875F, 0.4375F, 0.8125F, 0.9375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.75F, 0.875F, 0.625F, 0.8125F, 0.9375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.6875F, 0.875F, 0.625F, 0.75F, 0.9375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.0F, 0.875F, 0.4375F, 0.0625F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.5625F, 0.0F, 0.875F, 0.625F, 0.0625F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.875F, 0.625F, 0.125F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y + 1, z);
					}

					break;
				}

			} else {

				switch (dir) {
				case Hatch.DIR_X_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.0F, 0.0625F, 0.375F, 0.0625F, 0.125F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x + 1, y, z);
						renderBlocks.setRenderBounds(0.0F, 0.0625F, 0.5625F, 0.0625F, 0.125F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x + 1, y, z);
						renderBlocks.setRenderBounds(0.0625F, 0.0625F, 0.375F, 0.125F, 0.125F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x + 1, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.75F, 0.0625F, 0.375F, 0.8125F, 0.125F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.75F, 0.0625F, 0.5625F, 0.8125F, 0.125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.6875F, 0.0625F, 0.375F, 0.75F, 0.125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				case Hatch.DIR_X_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.9375F, 0.0625F, 0.375F, 1.0F, 0.125F, 0.4375F);
						renderStandardBlock(TE, renderBlocks, block, block, x - 1, y, z);
						renderBlocks.setRenderBounds(0.9375F, 0.0625F, 0.5625F, 1.0F, 0.125F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x - 1, y, z);
						renderBlocks.setRenderBounds(0.875F, 0.0625F, 0.375F, 0.9375F, 0.125F, 0.625F);
						renderStandardBlock(TE, renderBlocks, block, block, x - 1, y, z);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.1875F, 0.0625F, 0.375F, 0.25F, 0.125F, 0.4375F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.1875F, 0.0625F, 0.5625F, 0.25F, 0.125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.25F, 0.0625F, 0.375F, 0.3125F, 0.125F, 0.625F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				case Hatch.DIR_Z_NEG:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.0F, 0.4375F, 0.125F, 0.0625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z + 1);
						renderBlocks.setRenderBounds(0.5625F, 0.0625F, 0.0F, 0.625F, 0.125F, 0.0625F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z + 1);
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.0625F, 0.625F, 0.125F, 0.125F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z + 1);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.75F, 0.4375F, 0.125F, 0.8125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.0625F, 0.75F, 0.625F, 0.125F, 0.8125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.6875F, 0.625F, 0.125F, 0.75F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				case Hatch.DIR_Z_POS:

					if (render_inside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.9375F, 0.4375F, 0.125F, 1.0F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z - 1);
						renderBlocks.setRenderBounds(0.5625F, 0.0625F, 0.9375F, 0.625F, 0.125F, 1.0F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z - 1);
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.875F, 0.625F, 0.125F, 0.9375F);
						renderStandardBlock(TE, renderBlocks, block, block, x, y, z - 1);
					}

					if (render_outside_handle)
					{
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.1875F, 0.4375F, 0.125F, 0.25F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.5625F, 0.0625F, 0.1875F, 0.625F, 0.125F, 0.25F);
						renderBlocks.renderStandardBlock(block, x, y, z);
						renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.25F, 0.625F, 0.125F, 0.3125F);
						renderBlocks.renderStandardBlock(block, x, y, z);
					}

					break;
				}
			}
		}

		return true;
	}

}