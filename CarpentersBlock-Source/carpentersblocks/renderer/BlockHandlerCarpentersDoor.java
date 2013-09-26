package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import carpentersblocks.block.BlockCarpentersDoor;
import carpentersblocks.data.Door;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.IconHandler;

public class BlockHandlerCarpentersDoor extends HingedBase
{

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	/**
	 * Renders block
	 */
	public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = isSideCover ? BlockProperties.getCoverBlock(TE, coverRendering) : BlockProperties.getCoverBlock(TE, 6);

		renderBlocks.renderAllFaces = true;

		int data = BlockProperties.getData(TE);
		int type = Door.getType(data);

		switch (type) {
		case Door.TYPE_GLASS_TOP:
			renderGlassTopDoor(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Door.TYPE_GLASS_TALL:
			renderTallDoor(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Door.TYPE_PANELS:
			renderPanelsDoor(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Door.TYPE_SCREEN_TALL:
			renderTallDoor(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Door.TYPE_FRENCH_GLASS:
			renderFrenchGlassDoor(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		case Door.TYPE_HIDDEN:
			renderHiddenDoor(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);
			break;
		}

		renderBlocks.renderAllFaces = false;

		return true;
	}

	/**
	 * Renders a French glass door at the given coordinates
	 */
	public boolean renderFrenchGlassDoor(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		int hinge = Door.getHinge(data);
		int facing = Door.getFacing(data);
		boolean isOpen = Door.getState(data) == Door.STATE_OPEN;
		boolean isBottom = Door.getPiece(data) == Door.PIECE_BOTTOM;

		boolean path_on_x = false;

		float path_offset = 0.0F;

		float x_low = 0.0F;
		float y_low = 0.0F;
		float z_low = 0.0F;
		float x_high = 1.0F;
		float y_high = 1.0F;
		float z_high = 1.0F;

		float x_low_offset = 0.0F;
		float z_low_offset = 0.0F;
		float x_high_offset = 1.0F;
		float z_high_offset = 1.0F;

		switch (facing)
		{
		case Door.FACING_XN:

			if (!isOpen) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			} else {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			}

			break;
		case Door.FACING_XP:

			if (!isOpen) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			} else {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			}

			break;
		case Door.FACING_ZN:

			if (!isOpen) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			} else {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			}

			break;
		case Door.FACING_ZP:

			if (!isOpen) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			} else {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			}

			break;
		}

		if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		{
			/*
			 * Draw vertical pieces.
			 */
			 renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			float temp_x_low = x_low;
			float temp_x_high = x_high;
			float temp_z_low = z_low;
			float temp_z_high = z_high;

			if (path_on_x) {
				temp_x_low += 0.0625F;
				temp_x_high -= 0.0625F;
				temp_z_low = 0.4375F;
				temp_z_high = 0.5625F;
			} else {
				temp_z_low += 0.0625F;
				temp_z_high -= 0.0625F;
				temp_x_low = 0.4375F;
				temp_x_high = 0.5625F;
			}

			setLightnessOffset(-0.05F);

			// Two center pieces
			if (isBottom) {
				renderBlocks.setRenderBounds(temp_x_low, 0.1875F, temp_z_low, temp_x_high, 0.5F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(temp_x_low, 0.625F, temp_z_low, temp_x_high, 0.9375F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			} else {
				renderBlocks.setRenderBounds(temp_x_low, 0.0625F, temp_z_low, temp_x_high, 0.375F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(temp_x_low, 0.5F, temp_z_low, temp_x_high, 0.8125F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			clearLightnessOffset();

			temp_x_low = x_low;
			temp_x_high = x_high;
			temp_z_low = z_low;
			temp_z_high = z_high;

			/*
			 * Draw horizontal pieces.
			 */
			if (path_on_x) {
				temp_z_low = 0.1875F;
				temp_z_high = 0.8125F;
			} else {
				temp_x_low = 0.1875F;
				temp_x_high = 0.8125F;
			}

			if (isBottom) {

				renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				if (path_on_x) {
					temp_x_low += 0.0625F;
					temp_x_high -= 0.0625F;
				} else {
					temp_z_low += 0.0625F;
					temp_z_high -= 0.0625F;
				}

				setLightnessOffset(-0.05F);

				renderBlocks.setRenderBounds(temp_x_low, 0.5F, temp_z_low, temp_x_high, 0.625F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(temp_x_low, 0.9375F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearLightnessOffset();

			} else {

				renderBlocks.setRenderBounds(temp_x_low, 0.8125F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				if (path_on_x) {
					temp_x_low += 0.0625F;
					temp_x_high -= 0.0625F;
				} else {
					temp_z_low += 0.0625F;
					temp_z_high -= 0.0625F;
				}

				setLightnessOffset(-0.05F);

				renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.0625F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(temp_x_low, 0.375F, temp_z_low, temp_x_high, 0.5F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				clearLightnessOffset();

			}
		}

		if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
		{
			Icon icon;

			if (isBottom)
				icon = IconHandler.icon_door_french_glass_bottom;
			else
				icon = IconHandler.icon_door_french_glass_top;

			VertexHelper.setOffset(-(1 - path_offset));
			Tessellator.instance.setBrightness(Block.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

			if (path_on_x) {
				Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);
				renderBlocks.setRenderBounds(0.0F, isBottom ? 0.1875F : 0.0F, 0.1875F, 1.0F, isBottom ? 1.0F : 0.8125F, 0.8125F);
				RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
				VertexHelper.setOffset(-path_offset);
				RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
			} else {
				Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);
				renderBlocks.setRenderBounds(0.1875F, isBottom ? 0.1875F : 0.0F, 0.0F, 0.8125F, isBottom ? 1.0F : 0.8125F, 1.0F);
				RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
				VertexHelper.setOffset(-path_offset);
				RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
			}

			VertexHelper.clearOffset();

			renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, true);
		}

		return true;
	}

	/**
	 * Renders a glass top door at the given coordinates
	 */
	public boolean renderGlassTopDoor(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		int hinge = Door.getHinge(data);
		boolean isOpen = Door.getState(data) == Door.STATE_OPEN;
		boolean isBottom = Door.getPiece(data) == Door.PIECE_BOTTOM;

		boolean path_on_x = false;

		float path_offset = 0.0F;

		float x_low = 0.0F;
		float y_low = 0.0F;
		float z_low = 0.0F;
		float x_high = 1.0F;
		float y_high = 1.0F;
		float z_high = 1.0F;

		float x_low_offset = 0.0F;
		float z_low_offset = 0.0F;
		float x_high_offset = 1.0F;
		float z_high_offset = 1.0F;

		switch (Door.getFacing(data))
		{
		case Door.FACING_XN:

			if (!isOpen) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			} else {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			}

			break;
		case Door.FACING_XP:

			if (!isOpen) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			} else {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			}

			break;
		case Door.FACING_ZN:

			if (!isOpen) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			} else {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			}

			break;
		case Door.FACING_ZP:

			if (!isOpen) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			} else {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			}

			break;
		}

		if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		{
			renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			float temp_x_low = x_low;
			float temp_x_high = x_high;
			float temp_z_low = z_low;
			float temp_z_high = z_high;

			/*
			 * Render interior sheet on bottom
			 */

			 if (isBottom)
			 {
				 if (path_on_x) {
					 temp_x_low += 0.0625F;
					 temp_x_high -= 0.0625F;
					 temp_z_low = 0.1875F;
					 temp_z_high = 0.8125F;
				 } else {
					 temp_z_low += 0.0625F;
					 temp_z_high -= 0.0625F;
					 temp_x_low = 0.1875F;
					 temp_x_high = 0.8125F;
				 }

				 setLightnessOffset(-0.05F);

				 renderBlocks.setRenderBounds(temp_x_low, 0.1875F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

				 clearLightnessOffset();

				 temp_x_low = x_low;
				 temp_x_high = x_high;
				 temp_z_low = z_low;
				 temp_z_high = z_high;
			 }

			 /*
			  * Render horizontal pieces
			  */
			 if (path_on_x) {
				 temp_z_low = 0.1875F;
				 temp_z_high = 0.8125F;
			 } else {
				 temp_x_low = 0.1875F;
				 temp_x_high = 0.8125F;
			 }

			 if (isBottom) {
				 renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			 } else {
				 renderBlocks.setRenderBounds(temp_x_low, 0.8125F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				 renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			 }

			 temp_x_low = x_low;
			 temp_x_high = x_high;
			 temp_z_low = z_low;
			 temp_z_high = z_high;

			 /*
			  * Render interior panel on bottom
			  */
			 if (isBottom)
			 {
				 if (path_on_x) {
					 temp_z_low = 0.3125F;
					 temp_z_high = 0.6875F;
				 } else {
					 temp_x_low = 0.3125F;
					 temp_x_high = 0.6875F;
				 }

				 renderBlocks.setRenderBounds(temp_x_low, 0.3125F, temp_z_low, temp_x_high, 0.875F, temp_z_high);
				 renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			 }
		}

		if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
		{
			if (!isBottom)
			{
				VertexHelper.setOffset(-(1 - path_offset));
				Tessellator.instance.setBrightness(Block.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

				if (path_on_x) {
					Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);
					renderBlocks.setRenderBounds(0.0F, 0.1875F, 0.1875F, 1.0F, 0.8125F, 0.8125F);
					RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, IconHandler.icon_door_glass_top);
					VertexHelper.setOffset(-path_offset);
					RenderHelper.renderFaceXPos(renderBlocks, x, y, z, IconHandler.icon_door_glass_top);
				} else {
					Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);
					renderBlocks.setRenderBounds(0.1875F, 0.1875F, 0.0F, 0.8125F, 0.8125F, 1.0F);
					RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, IconHandler.icon_door_glass_top);
					VertexHelper.setOffset(-path_offset);
					RenderHelper.renderFaceZPos(renderBlocks, x, y, z, IconHandler.icon_door_glass_top);
				}

				VertexHelper.clearOffset();
			}

			renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, true);
		}

		return true;
	}

	/**
	 * Renders a panels door at the given coordinates
	 */
	public boolean renderPanelsDoor(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		int hinge = Door.getHinge(data);
		boolean isOpen = Door.getState(data) == Door.STATE_OPEN;
		boolean isBottom = Door.getPiece(data) == Door.PIECE_BOTTOM;

		boolean path_on_x = false;

		float x_low = 0.0F;
		float y_low = 0.0F;
		float z_low = 0.0F;
		float x_high = 1.0F;
		float y_high = 1.0F;
		float z_high = 1.0F;

		float x_low_offset = 0.0F;
		float z_low_offset = 0.0F;
		float x_high_offset = 1.0F;
		float z_high_offset = 1.0F;

		switch (Door.getFacing(data))
		{
		case Door.FACING_XN:

			if (!isOpen) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;

			} else {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;

			}

			break;
		case Door.FACING_XP:

			if (!isOpen) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;

			} else {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;

			}

			break;
		case Door.FACING_ZN:

			if (!isOpen) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_on_x = true;

			} else {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_on_x = true;

			}

			break;
		case Door.FACING_ZP:

			if (!isOpen) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_on_x = true;

			} else {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_on_x = true;

			}

			break;
		}

		if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		{
			renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			float temp_x_low = x_low;
			float temp_x_high = x_high;
			float temp_z_low = z_low;
			float temp_z_high = z_high;

			/*
			 * Render interior sheet
			 */
			 if (path_on_x) {
				 temp_x_low += 0.0625F;
				 temp_x_high -= 0.0625F;
				 temp_z_low = 0.1875F;
				 temp_z_high = 0.8125F;
			 } else {
				 temp_z_low += 0.0625F;
				 temp_z_high -= 0.0625F;
				 temp_x_low = 0.1875F;
				 temp_x_high = 0.8125F;
			 }

			setLightnessOffset(-0.05F);

			if (isBottom) {
				renderBlocks.setRenderBounds(temp_x_low, 0.1875F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			} else {
				renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.8125F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			clearLightnessOffset();

			temp_x_low = x_low;
			temp_x_high = x_high;
			temp_z_low = z_low;
			temp_z_high = z_high;

			/*
			 * Render horizontal pieces
			 */
			if (path_on_x) {
				temp_z_low = 0.1875F;
				temp_z_high = 0.8125F;
			} else {
				temp_x_low = 0.1875F;
				temp_x_high = 0.8125F;
			}

			if (isBottom) {
				renderBlocks.setRenderBounds(temp_x_low, y_low, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			} else {
				renderBlocks.setRenderBounds(temp_x_low, 0.8125F, temp_z_low, temp_x_high, y_high, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
				renderBlocks.setRenderBounds(temp_x_low, 0.0625F, temp_z_low, temp_x_high, 0.25F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}

			temp_x_low = x_low;
			temp_x_high = x_high;
			temp_z_low = z_low;
			temp_z_high = z_high;

			/*
			 * Render interior panel
			 */
			if (path_on_x) {
				temp_z_low = 0.3125F;
				temp_z_high = 0.6875F;
			} else {
				temp_x_low = 0.3125F;
				temp_x_high = 0.6875F;
			}

			if (isBottom) {
				renderBlocks.setRenderBounds(temp_x_low, 0.3125F, temp_z_low, temp_x_high, 0.9375F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			} else {
				renderBlocks.setRenderBounds(temp_x_low, 0.375F, temp_z_low, temp_x_high, 0.6875F, temp_z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
		}

		if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
			renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, true);

		return true;
	}

	/**
	 * Renders a tall glass or screen door at the given coordinates
	 */
	public boolean renderTallDoor(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		int hinge = Door.getHinge(data);
		boolean isOpen = Door.getState(data) == Door.STATE_OPEN;
		boolean isBottom = Door.getPiece(data) == Door.PIECE_BOTTOM;

		boolean path_on_x = false;

		float path_offset = 0.0F;

		float x_low = 0.0F;
		float y_low = 0.0F;
		float z_low = 0.0F;
		float x_high = 1.0F;
		float y_high = 1.0F;
		float z_high = 1.0F;

		float x_low_offset = 0.0F;
		float z_low_offset = 0.0F;
		float x_high_offset = 1.0F;
		float z_high_offset = 1.0F;

		switch (Door.getFacing(data))
		{
		case Door.FACING_XN:

			if (!isOpen) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			} else {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			}

			break;
		case Door.FACING_XP:

			if (!isOpen) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			} else if (hinge == Door.HINGE_RIGHT) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			} else {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			}

			break;
		case Door.FACING_ZN:

			if (!isOpen) {

				z_low = 0.8125F;
				z_low_offset = z_low;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.09375F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			} else {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			}

			break;
		case Door.FACING_ZP:

			if (!isOpen) {

				z_high = 0.1875F;
				z_high_offset = z_high;
				x_high_offset = 0.1875F;
				x_low_offset = 0.8125F;
				path_offset = 0.90625F;

			} else if (hinge == Door.HINGE_RIGHT) {

				x_high = 0.1875F;
				x_high_offset = x_high;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.90625F;
				path_on_x = true;

			} else {

				x_low = 0.8125F;
				x_low_offset = x_low;
				z_high_offset = 0.1875F;
				z_low_offset = 0.8125F;
				path_offset = 0.09375F;
				path_on_x = true;

			}

			break;
		}

		if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		{
			renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

			if (path_on_x) {
				z_low = 0.1875F;
				z_high = 0.8125F;
			} else {
				x_low = 0.1875F;
				x_high = 0.8125F;
			}

			if (isBottom) {
				renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high, 0.1875F, z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			} else {
				renderBlocks.setRenderBounds(x_low, 0.8125F, z_low, x_high, y_high, z_high);
				renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
			}
		}

		if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
		{
			int type = Door.getType(data);
			Icon icon;

			if (isBottom)
				icon = type == Door.TYPE_SCREEN_TALL ? IconHandler.icon_door_screen_tall : IconHandler.icon_door_glass_tall_bottom;
			else
				icon = type == Door.TYPE_SCREEN_TALL ? IconHandler.icon_door_screen_tall : IconHandler.icon_door_glass_tall_top;

			VertexHelper.setOffset(-(1 - path_offset));
			Tessellator.instance.setBrightness(Block.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

			if (path_on_x) {
				Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);
				renderBlocks.setRenderBounds(0.0F, isBottom ? 0.1875F : 0.0F, 0.1875F, 1.0F, isBottom ? 1.0F : 0.8125F, 0.8125F);
				RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
				VertexHelper.setOffset(-path_offset);
				RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
			} else {
				Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);
				renderBlocks.setRenderBounds(0.1875F, isBottom ? 0.1875F : 0.0F, 0.0F, 0.8125F, isBottom ? 1.0F : 0.8125F, 1.0F);
				RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
				VertexHelper.setOffset(-path_offset);
				RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
			}

			VertexHelper.clearOffset();

			renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, true);
		}

		return true;
	}

	/**
	 * Renders a hidden door at the given coordinates
	 */
	public boolean renderHiddenDoor(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
	{
		if (shouldRenderFrame(TE, renderBlocks, coverBlock, renderPass))
		{
			BlockCarpentersDoor blockRef = (BlockCarpentersDoor) BlockHandler.blockCarpentersDoor;
			blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
			renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		}

		if (shouldRenderPieces(TE, renderBlocks, coverBlock, renderPass))
			renderHandle(TE, renderBlocks, Block.blockIron, x, y, z, true, false);

		return true;
	}

	/**
	 * Renders a door handle for the given coordinates
	 */
	public boolean renderHandle(TECarpentersBlock TE, RenderBlocks renderBlocks, Block block, int x, int y, int z, boolean render_inside_handle, boolean render_outside_handle)
	{
		if (!render_inside_handle && !render_outside_handle)
			return false;

		int data = BlockProperties.getData(TE);
		int hinge = Door.getHinge(data);
		boolean isOpen = Door.getState(data) == Door.STATE_OPEN;
		boolean isBottom = Door.getPiece(data) == Door.PIECE_BOTTOM;

		float x_low = 0.0F;
		float y_low = isBottom ? 0.875F : 0.0625F;
		float z_low = 0.0F;
		float y_high = isBottom ? 0.9375F : 0.125F;
		float y_low_offset = isBottom ? 0.875F : 0.0F;
		float y_high_offset = isBottom ? 1.0F : 0.125F;
		switch (Door.getFacing(data))
		{
		case Door.FACING_XN:

			if (!isOpen) {

				z_low = hinge == Door.HINGE_RIGHT ? 0.875F : 0.0625F;

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.75F, y_low, z_low, 0.8125F, y_high, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.6875F, y_low_offset, z_low, 0.75F, y_high_offset, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.0625F, y_high, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x + 1, y, z);
					renderBlocks.setRenderBounds(0.0625F, y_low_offset, z_low, 0.125F, y_high_offset, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x + 1, y, z);
				}

			} else if (hinge == Door.HINGE_RIGHT) {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.0625F, y_low, 0.1875F, 0.0625F + 0.0625F, y_high, 0.25F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.25F, 0.0625F + 0.0625F, y_high_offset, 0.3125F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.0625F, y_low, 0.9375F, 0.0625F + 0.0625F, y_high, 1.0F);
					renderBlocks.renderStandardBlock(block, x, y, z - 1);
					renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.875F, 0.0625F + 0.0625F, y_high_offset, 0.9375F);
					renderBlocks.renderStandardBlock(block, x, y, z - 1);
				}

			} else {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.0625F, y_low, 0.75F, 0.0625F + 0.0625F, y_high, 0.8125F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.6875F, 0.0625F + 0.0625F, y_high_offset, 0.75F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.0625F, y_low, 0.0F, 0.0625F + 0.0625F, y_high, 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z + 1);
					renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.0625F, 0.0625F + 0.0625F, y_high_offset, 0.125F);
					renderBlocks.renderStandardBlock(block, x, y, z + 1);
				}

			}

			break;
		case Door.FACING_XP:

			if (!isOpen) {

				z_low = hinge == Door.HINGE_RIGHT ? 0.0625F : 0.875F;

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.1875F, y_low, z_low, 0.25F, y_high, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.25F, y_low_offset, z_low, 0.3125F, y_high_offset, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.9375F, y_low, z_low, 1.0F, y_high, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x - 1, y, z);
					renderBlocks.setRenderBounds(0.875F, y_low_offset, z_low, 0.9375F, y_high_offset, z_low + 0.0625F);
					renderBlocks.renderStandardBlock(block, x - 1, y, z);
				}

			} else if (hinge == Door.HINGE_RIGHT) {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.875F, y_low, 0.75F, 0.875F + 0.0625F, y_high, 0.8125F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.6875F, 0.875F + 0.0625F, y_high_offset, 0.75F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.875F, y_low, 0.0F, 0.875F + 0.0625F, y_high, 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z + 1);
					renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.0625F, 0.875F + 0.0625F, y_high_offset, 0.125F);
					renderBlocks.renderStandardBlock(block, x, y, z + 1);
				}

			} else {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.875F, y_low, 0.1875F, 0.875F + 0.0625F, y_high, 0.25F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.25F, 0.875F + 0.0625F, y_high_offset, 0.3125F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.875F, y_low, 0.9375F, 0.875F + 0.0625F, y_high, 1.0F);
					renderBlocks.renderStandardBlock(block, x, y, z - 1);
					renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.875F, 0.875F + 0.0625F, y_high_offset, 0.9375F);
					renderBlocks.renderStandardBlock(block, x, y, z - 1);
				}

			}

			break;
		case Door.FACING_ZN:

			if (!isOpen) {

				x_low = hinge == Door.HINGE_RIGHT ? 0.0625F : 0.875F;

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(x_low, y_low, 0.75F, x_low + 0.0625F, y_high, 0.8125F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(x_low, y_low_offset, 0.6875F, x_low + 0.0625F, y_high_offset, 0.75F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_low + 0.0625F, y_high, 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z + 1);
					renderBlocks.setRenderBounds(x_low, y_low_offset, 0.0625F, x_low + 0.0625F, y_high_offset, 0.125F);
					renderBlocks.renderStandardBlock(block, x, y, z + 1);
				}

			} else if (hinge == Door.HINGE_RIGHT) {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.75F, y_low, 0.0625F, 0.8125F, y_high, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.6875F, y_low_offset, 0.0625F, 0.75F, y_high_offset, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.0F, y_low, 0.0625F, 0.0625F, y_high, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x + 1, y, z);
					renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.0625F, 0.125F, y_high_offset, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x + 1, y, z);
				}

			} else {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.1875F, y_low, 0.0625F, 0.25F, y_high, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.25F, y_low_offset, 0.0625F, 0.3125F, y_high_offset, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.9375F, y_low, 0.0625F, 1.0F, y_high, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x - 1, y, z);
					renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.0625F, 0.9375F, y_high_offset, 0.0625F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x - 1, y, z);
				}

			}

			break;
		case Door.FACING_ZP:

			if (!isOpen) {

				x_low = hinge == Door.HINGE_RIGHT ? 0.875F : 0.0625F;

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(x_low, y_low, 0.1875F, x_low + 0.0625F, y_high, 0.25F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(x_low, y_low_offset, 0.25F, x_low + 0.0625F, y_high_offset, 0.3125F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(x_low, y_low, 0.9375F, x_low + 0.0625F, y_high, 1.0F);
					renderBlocks.renderStandardBlock(block, x, y, z - 1);
					renderBlocks.setRenderBounds(x_low, y_low_offset, 0.875F, x_low + 0.0625F, y_high_offset, 0.9375F);
					renderBlocks.renderStandardBlock(block, x, y, z - 1);
				}

			} else if (hinge == Door.HINGE_RIGHT) {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.1875F, y_low, 0.875F, 0.25F, y_high, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.25F, y_low_offset, 0.875F, 0.3125F, y_high_offset, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.9375F, y_low, 0.875F, 1.0F, y_high, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x - 1, y, z);
					renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.875F, 0.9375F, y_high_offset, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x - 1, y, z);
				}

			} else {

				if (render_outside_handle)
				{
					renderBlocks.setRenderBounds(0.75F, y_low, 0.875F, 0.8125F, y_high, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
					renderBlocks.setRenderBounds(0.6875F, y_low_offset, 0.875F, 0.75F, y_high_offset, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x, y, z);
				}

				if (render_inside_handle)
				{
					renderBlocks.setRenderBounds(0.0F, y_low, 0.875F, 0.0625F, y_high, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x + 1, y, z);
					renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.875F, 0.125F, y_high_offset, 0.875F + 0.0625F);
					renderBlocks.renderStandardBlock(block, x + 1, y, z);
				}

			}

			break;
		}

		return true;
	}

}