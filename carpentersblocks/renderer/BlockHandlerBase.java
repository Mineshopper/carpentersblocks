package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockFluid;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import carpentersblocks.block.BlockBase;
import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.SlopeType;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import carpentersblocks.util.handler.DyeColorHandler;
import carpentersblocks.util.handler.FeatureHandler;
import carpentersblocks.util.handler.IconHandler;
import carpentersblocks.util.handler.OptifineHandler;
import carpentersblocks.util.handler.OverlayHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockHandlerBase implements ISimpleBlockRenderingHandler
{

	protected boolean isSideCover = false;
	protected boolean renderAlphaOverride = FeatureHandler.enableZFightingFix;
	protected boolean hasMetadataOverride = false;
	protected boolean hasLightnessOffset = false;
	protected boolean hasDyeColorOverride = false;
	protected boolean suppressDyeColor = false;
	protected boolean suppressOverlay = false;
	protected boolean suppressPattern = false;
	protected boolean disableAO = false;
	protected boolean[]	hasIconOverride = new boolean[6];

	protected int coverRendering = 6;
	protected int metadataOverride = 0;
	protected int dyeColorOverride = 0;

	protected Icon[] iconOverride = new Icon[6];

	protected float lightnessOffset = 0.0F;

	/** Identifies which render helper to use. */
	protected int slopeRenderID = 0;
	
	/** Returns whether side is sloped face. */
	protected boolean isSideSloped = false;
	
	/**
	 * Sets directional block side rotation in RenderBlocks.
	 */
	protected void setDirectionalRotation(TECarpentersBlock TE, RenderBlocks renderBlocks, int side)
	{
        int metadata = BlockProperties.getCoverMetadata(TE, coverRendering);
        int dir = metadata & 12;

		switch (side)
		{
			case 0:
		        if (metadata == 3 || dir == 4) {
		        	renderBlocks.uvRotateBottom = 1;
		        }
				break;
			case 1:
		        if (metadata == 3 || dir == 4) {
		        	renderBlocks.uvRotateTop = 1;
		        }
				break;
			case 2:
		        if (metadata == 3 || dir == 4) {
		        	renderBlocks.uvRotateEast = 1; // NORTH
		        }
				break;
			case 3:
		        if (metadata == 3 || dir == 4) {
		        	renderBlocks.uvRotateWest = 1; // SOUTH
		        }
				break;
			case 4:
		        if (metadata == 4 || dir == 8) {
		        	renderBlocks.uvRotateNorth = 1; // WEST
		        }
				break;
			case 5:
		        if (metadata == 4 || dir == 8) {
		        	renderBlocks.uvRotateSouth = 1; // EAST
		        }
				break;
		}
	}
	
	/**
	 * Resets side rotation in RenderBlocks.
	 */
	protected void clearRotation(RenderBlocks renderBlocks, int side)
	{
		switch (side)
		{
		case 0:
			renderBlocks.uvRotateBottom = 0;
			break;
		case 1:
			renderBlocks.uvRotateTop = 0;
			break;
		case 2:
			renderBlocks.uvRotateEast = 0;
			break;
		case 3:
			renderBlocks.uvRotateWest = 0;
			break;
		case 4:
			renderBlocks.uvRotateNorth = 0;
			break;
		case 5:
			renderBlocks.uvRotateSouth = 0;
			break;
		}
	}
	
	/**
	 * Sets metadata override.
	 */
	protected void setMetadataOverride(int metadata)
	{
		hasMetadataOverride = true;
		metadataOverride = metadata;
	}

	/**
	 * Clears metadata override.
	 */
	protected void clearMetadataOverride()
	{
		hasMetadataOverride = false;
	}

	/**
	 * Sets icon override.
	 * Using side 6 overrides all sides.
	 * RenderBlocks' icon override will override this one
	 * when breaking animation is played.
	 */
	protected void setIconOverride(int side, Icon icon)
	{
		if (side == 6) {
			for (int count = 0; count < 6; ++count) {
				hasIconOverride[count] = true;
				iconOverride[count] = icon;
			}
		} else {
			hasIconOverride[side] = true;
			iconOverride[side] = icon;
		}
	}

	/**
	 * Clears icon override.
	 */
	protected void clearIconOverride(int side)
	{
		if (side == 6) {
			for (int count = 0; count < 6; ++count) {
				hasIconOverride[count] = false;
			}
		} else {
			hasIconOverride[side] = false;
		}
	}

	/**
	 * Sets dye color override.
	 */
	protected void setDyeColorOverride(int dyeColor)
	{
		hasDyeColorOverride = true;
		dyeColorOverride = dyeColor;
	}

	/**
	 * Clears dye color override.
	 */
	protected void clearDyeColorOverride()
	{
		hasDyeColorOverride = false;
	}

	/**
	 * Sets lightness offset.
	 */
	protected void setLightnessOffset(float lightness)
	{
		hasLightnessOffset = true;
		lightnessOffset = lightness;
	}

	/**
	 * Clears lightness override.
	 */
	protected void clearLightnessOffset()
	{
		hasLightnessOffset = false;
	}
	
	/**
	 * Stores uncolored, ambient occlusion values for each corner of face.
	 */
	protected float[] base_ao = { 0.0F, 0.0F, 0.0F, 0.0F };

	/**
	 * Stores uncolored light value for face.
	 */
	protected float[] base_RGB = { 0.0F, 0.0F, 0.0F };

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
	{

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block srcBlock, int modelID, RenderBlocks renderBlocks)
	{
		TECarpentersBlock TE = (TECarpentersBlock) blockAccess.getBlockTileEntity(x, y, z);

		int renderPass = MinecraftForgeClient.getRenderPass();

		/*
		 * Render block and any side blocks
		 */
		renderCarpentersBlock(TE, renderBlocks, srcBlock, renderPass, x, y, z);
		renderSideCovers(TE, renderBlocks, srcBlock, renderPass, x, y, z);

		/*
		 * Render fancy fluids
		 */
		if (renderPass >= 0 && FeatureHandler.enableFancyFluids && Minecraft.isFancyGraphicsEnabled() && BlockProperties.hasCover(TE, 6)) {
			renderFancyFluids(TE, renderBlocks, x, y, z, renderPass);
		}

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return 0;
	}

	/**
	 * Returns whether block should render this pass.
	 */
	protected boolean shouldRenderBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass)
	{
		if (renderAlphaOverride) {
			return renderPass == 1;
		} else {
			return	renderBlocks.hasOverrideBlockTexture() ||
					coverBlock.getRenderBlockPass() == renderPass ||
					coverBlock instanceof BlockBase && renderPass == 0 ||
					hasAccessories(srcBlock);
		}
	}

	/**
	 * Returns whether srcBlock requires block rendering pass
	 * requirements independently to handle accessories.
	 * 
	 * This is required for doors and hatches for example because
	 * the handles and inset screen or glass render differently.
	 */
	private boolean hasAccessories(Block srcBlock)
	{
		return	srcBlock == BlockHandler.blockCarpentersDoor ||
				srcBlock == BlockHandler.blockCarpentersHatch;
	}

	/**
	 * Returns whether overlay should render.
	 */
	protected boolean shouldRenderOverlay(TECarpentersBlock TE, int renderPass)
	{
		if (!suppressOverlay)
		{
			Block coverBlock = BlockProperties.getCoverBlock(TE, coverRendering);

			if (BlockProperties.hasOverlay(TE, coverRendering) || coverBlock == Block.grass) {
				int coverPass = coverBlock.getRenderBlockPass();

				if (renderAlphaOverride) {
					return renderPass == 1;
				} else {
					if (coverBlock instanceof BlockBase) {
						return true;
					} else if (coverPass == 1) {
						return true;
					} else if (shouldRenderPattern(TE, renderPass)) {
						return renderPass == 1;
					} else {
						return coverPass == renderPass;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Returns whether pattern should render.
	 */
	protected boolean shouldRenderPattern(TECarpentersBlock TE, int renderPass)
	{
		if (!suppressPattern)
		{
			if (renderPass == 1) {
				if (BlockProperties.hasPattern(TE, coverRendering)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Renders fancy fluids in block space.
	 */
	private void renderFancyFluids(TECarpentersBlock TE, RenderBlocks renderBlocks, int x, int y, int z, int renderPass)
	{
		/*
		 * Render fluid in same block space if near fluid
		 * and fluid has a valid fill path to block.
		 */
		if (FeatureHandler.enableFancyFluids)
		{
			Block block_XN = !renderBlocks.blockAccess.isAirBlock(x - 1, y, z) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z)] : null;
			Block block_XP = !renderBlocks.blockAccess.isAirBlock(x + 1, y, z) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z)] : null;
			Block block_ZN = !renderBlocks.blockAccess.isAirBlock(x, y, z - 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z - 1)] : null;
			Block block_ZP = !renderBlocks.blockAccess.isAirBlock(x, y, z + 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z + 1)] : null;
			Block block_XZNP = !renderBlocks.blockAccess.isAirBlock(x - 1, y, z + 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)] : null;
			Block block_XZPP = !renderBlocks.blockAccess.isAirBlock(x + 1, y, z + 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)] : null;
			Block block_XZNN = !renderBlocks.blockAccess.isAirBlock(x - 1, y, z - 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)] : null;
			Block block_XZPN = !renderBlocks.blockAccess.isAirBlock(x + 1, y, z - 1) ? Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)] : null;

			boolean isFluid_XN = block_XN != null ? (block_XN instanceof IFluidBlock || block_XN instanceof BlockFluid) : false;
			boolean	isFluid_XP = block_XP != null ? (block_XP instanceof IFluidBlock || block_XP instanceof BlockFluid) : false;
			boolean	isFluid_ZN = block_ZN != null ? (block_ZN instanceof IFluidBlock || block_ZN instanceof BlockFluid) : false;
			boolean	isFluid_ZP = block_ZP != null ? (block_ZP instanceof IFluidBlock || block_ZP instanceof BlockFluid) : false;
			boolean	isFluid_XZNP = block_XZNP != null ? (block_XZNP instanceof IFluidBlock || block_XZNP instanceof BlockFluid) : false;
			boolean	isFluid_XZPP = block_XZPP != null ? (block_XZPP instanceof IFluidBlock || block_XZPP instanceof BlockFluid) : false;
			boolean	isFluid_XZNN = block_XZNN != null ? (block_XZNN instanceof IFluidBlock || block_XZNN instanceof BlockFluid) : false;
			boolean	isFluid_XZPN = block_XZPN != null ? (block_XZPN instanceof IFluidBlock || block_XZPN instanceof BlockFluid) : false;

			boolean isSolid_XN = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.WEST, true);
			boolean	isSolid_XP = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.EAST, true);
			boolean	isSolid_ZN = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.NORTH, true);
			boolean	isSolid_ZP = renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z, ForgeDirection.SOUTH, true);

			int metadata = 0;
			int diagMetadata = 0;

			Block fluidBlock = null;
			Block diagFluidBlock = null;
			for (int count = 2; count < 10 && fluidBlock == null; ++count)
			{
				switch (count) {
				case 2:
					if (isFluid_ZN && !isSolid_ZN) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z - 1)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z - 1);
						}
					}
					break;
				case 3:
					if (isFluid_ZP && !isSolid_ZP) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x, y, z + 1)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z + 1);
						}
					}
					break;
				case 4:
					if (isFluid_XN && !isSolid_XN) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z);
						}
					}
					break;
				case 5:
					if (isFluid_XP && !isSolid_XP) {
						fluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z)];
						if (metadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z)) {
							metadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z);
						}
					}
					break;
				case 6:
					if (isFluid_XZPN)  {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z - 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z - 1);
						}
					}
					break;
				case 7:
					if (isFluid_XZPP) {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z + 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x + 1, y, z + 1);
						}
					}
					break;
				case 8:
					if (isFluid_XZNN) {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z - 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z - 1);
						}
					}
					break;
				case 9:
					if (isFluid_XZNP) {
						diagFluidBlock = Block.blocksList[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];
						if (diagMetadata < renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z + 1)) {
							diagMetadata = renderBlocks.blockAccess.getBlockMetadata(x - 1, y, z + 1);
						}
					}
					break;
				}
			}

			if ((fluidBlock != null && renderPass == fluidBlock.getRenderBlockPass()) || (diagFluidBlock != null && renderPass == diagFluidBlock.getRenderBlockPass()))
			{
				boolean renderFluid = false;

				float minX = 0;
				float minZ = 0;
				float maxX = 1.0F;
				float maxZ = 1.0F;
				float offset = 0.01F;

				for (int side = 2; side < 6; ++side)
				{
					switch (side)
					{
					case 2:
						if (	!isSolid_ZP &&
								(
										isFluid_ZP ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.NORTH, true) &&
										(isFluid_XZNP && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.WEST, true) || isFluid_XZPP && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.EAST, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					case 3:
						if (	!isSolid_ZN &&
								(
										isFluid_ZN ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.SOUTH, true) &&
										(isFluid_XZNN && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.WEST, true) || isFluid_XZPN && !renderBlocks.blockAccess.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.EAST, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					case 4:
						if (	!isSolid_XP &&
								(
										isFluid_XP ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.WEST, true) &&
										(isFluid_XZPP && !renderBlocks.blockAccess.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.SOUTH, true) || isFluid_XZPN && !renderBlocks.blockAccess.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.NORTH, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					case 5:
						if (	!isSolid_XN &&
								(
										isFluid_XN ||
										!renderBlocks.blockAccess.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.EAST, true) &&
										(isFluid_XZNP && !renderBlocks.blockAccess.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.SOUTH, true) || isFluid_XZNN && !renderBlocks.blockAccess.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.NORTH, true))
										)
								)
						{
							renderFluid = true;
						}
						break;
					}
				}

				if (renderFluid)
				{
					if (isSolid_XN) {
						minX += offset;
					}
					if (isSolid_XP) {
						maxX -= offset;
					}
					if (isSolid_ZP) {
						maxZ -= offset;
					}
					if (isSolid_ZN) {
						minZ += offset;
					}

					if (fluidBlock == null)
					{
						fluidBlock = diagFluidBlock;
						metadata = diagMetadata;
					}

					if (!fluidBlock.hasTileEntity(metadata) && metadata == 0)
					{
						double fluidHeight = (fluidBlock instanceof BlockFluid ? (1.0D - 1.0F / 9.0F) : 0.875F) - 0.0010000000474974513D;
						renderBlocks.setRenderBounds(minX, offset, minZ, maxX, fluidHeight, maxZ);
						float rgb[] = getRGB(renderBlocks.blockAccess, fluidBlock, x, y, z);
						renderBlocks.renderStandardBlockWithColorMultiplier(fluidBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
					}
				}
			}
		}
	}

	/**
	 * Override for shape specific render methods.
	 */
	public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = BlockProperties.getCoverBlock(TE, coverRendering);
		renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
		return true;
	}

	/**
	 * Sets up side cover rendering bounds.
	 * Will return block location where side cover should be rendered.
	 */
	protected int[] setSideCoverRenderBounds(TECarpentersBlock TE, RenderBlocks renderBlocks, int x, int y, int z, int side)
	{
		double offset = 1.0D / 16.0D;

		/*
		 * Make snow match vanilla snow depth
		 */
		if (side == 1)
		{
			int blockID = BlockProperties.getCoverID(TE, side);
			if (blockID == Block.blockSnow.blockID || blockID == Block.snow.blockID) {
				offset = 1.0D / 8.0D;
			}
		}

		/*
		 * Adjust bounds of side cover to accommodate
		 * partial rendered blocks (slabs, stair steps)
		 */
		switch (side) {
		case 0:
			if (renderBlocks.renderMinY > 0.0D) {
				renderBlocks.renderMaxY = renderBlocks.renderMinY;
				renderBlocks.renderMinY -= offset;
			} else {
				renderBlocks.renderMaxY = 1.0D;
				renderBlocks.renderMinY = renderBlocks.renderMaxY - offset;
				y -= 1;
			}
			break;
		case 1:
			if (renderBlocks.renderMaxY < 1.0D) {
				renderBlocks.renderMinY = renderBlocks.renderMaxY;
				renderBlocks.renderMaxY += offset;
			} else {
				renderBlocks.renderMaxY = offset;
				renderBlocks.renderMinY = 0.0D;
				y += 1;
			}
			break;
		case 2:
			if (renderBlocks.renderMinZ > 0.0D) {
				renderBlocks.renderMaxZ = renderBlocks.renderMinZ;
				renderBlocks.renderMinZ -= offset;
			} else {
				renderBlocks.renderMaxZ = 1.0D;
				renderBlocks.renderMinZ = renderBlocks.renderMaxZ - offset;
				z -= 1;
			}
			break;
		case 3:
			if (renderBlocks.renderMaxZ < 1.0D) {
				renderBlocks.renderMinZ = renderBlocks.renderMaxZ;
				renderBlocks.renderMaxZ += offset;
			} else {
				renderBlocks.renderMaxZ = offset;
				renderBlocks.renderMinZ = 0.0D;
				z += 1;
			}
			break;
		case 4:
			if (renderBlocks.renderMinX > 0.0D) {
				renderBlocks.renderMaxX = renderBlocks.renderMinX;
				renderBlocks.renderMinX -= offset;
			} else {
				renderBlocks.renderMaxX = 1.0D;
				renderBlocks.renderMinX = renderBlocks.renderMaxX - offset;
				x -= 1;
			}
			break;
		case 5:
			if (renderBlocks.renderMaxX < 1.0D) {
				renderBlocks.renderMinX = renderBlocks.renderMaxX;
				renderBlocks.renderMaxX += offset;
			} else {
				renderBlocks.renderMaxX = offset;
				renderBlocks.renderMinX = 0.0D;
				x += 1;
			}
			break;
		}

		return new int[] { x, y, z };
	}

	/**
	 * Renders side covers.
	 */
	protected void renderSideCovers(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		isSideCover = true;
		renderBlocks.renderAllFaces = true;

		srcBlock.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);

		for (int side = 0; side < 6; ++side)
		{
			if (BlockProperties.hasCover(TE, side))
			{
				Block coverBlock = BlockProperties.getCoverBlock(TE, side);
				coverRendering = side;

				if (shouldRenderBlock(TE, renderBlocks, coverBlock, srcBlock, renderPass) || shouldRenderPattern(TE, renderPass))
				{
					int[] renderOffset = setSideCoverRenderBounds(TE, renderBlocks, x, y, z, side);
					renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, renderOffset[0], renderOffset[1], renderOffset[2]);
					renderBlocks.setRenderBoundsFromBlock(srcBlock);
				}
			}
		}

		renderBlocks.renderAllFaces = false;
		coverRendering = 6;
		isSideCover = false;
	}

	/**
	 * Multiplies AO by a color.
	 */
	protected void aoMultiplyByColor(RenderBlocks renderBlocks, float red, float green, float blue)
	{
		renderBlocks.colorRedTopLeft *= red;
		renderBlocks.colorGreenTopLeft *= green;
		renderBlocks.colorBlueTopLeft *= blue;
		renderBlocks.colorRedTopRight *= red;
		renderBlocks.colorGreenTopRight *= green;
		renderBlocks.colorBlueTopRight *= blue;
		renderBlocks.colorRedBottomRight *= red;
		renderBlocks.colorGreenBottomRight *= green;
		renderBlocks.colorBlueBottomRight *= blue;
		renderBlocks.colorRedBottomLeft *= red;
		renderBlocks.colorGreenBottomLeft *= green;
		renderBlocks.colorBlueBottomLeft *= blue;
	}

	/**
	 * Sets AO color.
	 */
	protected void aoSetColor(TECarpentersBlock TE, RenderBlocks renderBlocks, Block block, int side, float red, float green, float blue, float lightness)
	{
		/*
		 * Set base light values against color multiplier
		 */
		renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = red * lightness;
		renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = green * lightness;
		renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = blue * lightness;

		/*
		 * Shade AO values.
		 */
		renderBlocks.colorRedTopLeft *= base_ao[0];
		renderBlocks.colorGreenTopLeft *= base_ao[0];
		renderBlocks.colorBlueTopLeft *= base_ao[0];
		renderBlocks.colorRedTopRight *= base_ao[1];
		renderBlocks.colorGreenTopRight *= base_ao[1];
		renderBlocks.colorBlueTopRight *= base_ao[1];
		renderBlocks.colorRedBottomRight *= base_ao[2];
		renderBlocks.colorGreenBottomRight *= base_ao[2];
		renderBlocks.colorBlueBottomRight *= base_ao[2];
		renderBlocks.colorRedBottomLeft *= base_ao[3];
		renderBlocks.colorGreenBottomLeft *= base_ao[3];
		renderBlocks.colorBlueBottomLeft *= base_ao[3];
	}

	/**
	 * Resets AO color.
	 */
	protected void aoResetColor(RenderBlocks renderBlocks)
	{
		/*
		 * Shade AO values.
		 */
		renderBlocks.colorRedTopLeft = base_ao[0];
		renderBlocks.colorGreenTopLeft = base_ao[0];
		renderBlocks.colorBlueTopLeft = base_ao[0];
		renderBlocks.colorRedTopRight = base_ao[1];
		renderBlocks.colorGreenTopRight = base_ao[1];
		renderBlocks.colorBlueTopRight = base_ao[1];
		renderBlocks.colorRedBottomRight = base_ao[2];
		renderBlocks.colorGreenBottomRight = base_ao[2];
		renderBlocks.colorBlueBottomRight = base_ao[2];
		renderBlocks.colorRedBottomLeft = base_ao[3];
		renderBlocks.colorGreenBottomLeft = base_ao[3];
		renderBlocks.colorBlueBottomLeft = base_ao[3];
	}

	/**
	 * Returns icon for face.
	 */
	protected Icon getIcon(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int side, int x, int y, int z)
	{
		int data = BlockProperties.getData(TE);
		int metadata = hasMetadataOverride ? metadataOverride : BlockProperties.getCoverMetadata(TE, coverRendering);

		Icon icon = coverBlock.getIcon(side, metadata);
		
		/* The daylight sensor grabs textures directly. */
		if (srcBlock == BlockHandler.blockCarpentersDaylightSensor) {
			icon = srcBlock.getBlockTexture(renderBlocks.blockAccess, x, y, z, side);
		}

		/* The lever icon defaults to the handle.  We set the box icon here. */
		if (coverBlock == BlockHandler.blockCarpentersLever) {
			icon = IconHandler.icon_generic;
		}
		
		/* If the face is sloped, set icons accordingly. */
		if (isSideSloped)
		{
			Slope slope = Slope.slopesList[data];
			
			/* Uncovered sloped oblique faces use triangular frame icons. */
			if (!BlockProperties.hasCover(TE, 6))
			{
				if (slope.slopeType.equals(SlopeType.OBLIQUE_INT)) {
					icon = slope.isPositive ? IconHandler.icon_slope_oblique_pt_low : IconHandler.icon_slope_oblique_pt_high;
				} else if (slope.slopeType.equals(SlopeType.OBLIQUE_EXT)) {
					icon = slope.isPositive ? IconHandler.icon_slope_oblique_pt_high : IconHandler.icon_slope_oblique_pt_low;
				}
			}
			
			/* For directional blocks, make sure sloped icons match regardless of side. */
			if (BlockProperties.blockRotates(TE.worldObj, coverBlock, x, y, z)) {
				if (metadata % 8 == 0) {
					icon = coverBlock.getIcon(slope.isPositive ? 1 : 0, metadata);
				} else {
					icon = coverBlock.getIcon(2, metadata);
				}
			} else if (coverBlock instanceof BlockDirectional && !slope.slopeType.equals(SlopeType.WEDGE_Y)) {
				icon = coverBlock.getBlockTextureFromSide(1);
			}
		}

		/* If icon has global override, apply it. */
		if (hasIconOverride[side] && iconOverride[side] != null) {
			icon = iconOverride[side];
		}

		return icon;
	}

	/**
	 * Renders multiple textures to side.
	 */
	protected void renderMultiTexturedSide(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int side, int x, int y, int z, float lightness, Icon icon)
	{
		int renderPass = MinecraftForgeClient.getRenderPass();

		/*
		 * Render side
		 */
		if (shouldRenderBlock(TE, renderBlocks, coverBlock, srcBlock, renderPass))
		{
			if (BlockProperties.blockRotates(TE.worldObj, coverBlock, x, y, z)) {
				setDirectionalRotation(TE, renderBlocks, side);
			}
			
			colorSide(TE, renderBlocks, coverBlock, srcBlock, side, x, y, z, icon, lightness);
			renderSide(TE, renderBlocks, side, 0, x, y, z, icon);
			
			clearRotation(renderBlocks, side);
		}

		/* Do not render decorations on top if this is the daylight sensor block. */
		if (srcBlock == BlockHandler.blockCarpentersDaylightSensor && side == 1) {
			return;
		}

		suppressDyeColor = true;
		
		/* Render pattern on side. */
		if (shouldRenderPattern(TE, renderPass)) {
			renderPattern(TE, renderBlocks, srcBlock, side, x, y, z, lightness);
		}

		/* Render overlay on side. */
		if (shouldRenderOverlay(TE, renderPass)) {
			renderOverlay(TE, renderBlocks, coverBlock, srcBlock, side, x, y, z, lightness, icon);
		}
		
		suppressDyeColor = false;
	}

	/**
	 * Sets icon, applies color and renders given side.
	 */
	protected void prepareRender(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int side, int x, int y, int z, float lightness)
	{
		Icon icon = getIcon(TE, renderBlocks, coverBlock, srcBlock, side, x, y, z);
		
		if (!renderBlocks.enableAO) {
			base_RGB = new float[] { lightness, lightness, lightness };
		}
		
		/*
		 * A texture override indicates the breaking animation is being
		 * drawn.  If this is the case, only draw this for current pass.
		 */
		if (renderBlocks.hasOverrideBlockTexture()) {
			renderSide(TE, renderBlocks, side, 0, x, y, z, renderBlocks.overrideBlockTexture);
		} else {
			renderMultiTexturedSide(TE, renderBlocks, coverBlock, srcBlock, side, x, y, z, lightness, icon);
		}
	}

	/**
	 * Apply color to AO or tessellator
	 */
	protected void colorSide(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int side, int x, int y, int z, Icon icon, float lightness)
	{
		Tessellator tessellator = Tessellator.instance;
		float baseRGB[] = getRGB(renderBlocks.blockAccess, coverBlock, x, y, z);
		float dyeRGB[];

		if (hasDyeColorOverride) {
			dyeRGB = DyeColorHandler.getDyeColorRGB(suppressDyeColor ? DyeColorHandler.NO_COLOR : dyeColorOverride);
		} else {
			dyeRGB = DyeColorHandler.getDyeColorRGB(suppressDyeColor ? DyeColorHandler.NO_COLOR : BlockProperties.getDyeColor(TE, coverRendering));
		}

		if (hasLightnessOffset)
		{
			if (renderBlocks.enableAO) {
				lightness += lightnessOffset;
			} else {
				base_RGB[0] += lightnessOffset;
				base_RGB[1] += lightnessOffset;
				base_RGB[2] += lightnessOffset;
			}
		}

		if (renderBlocks.enableAO) {

			aoResetColor(renderBlocks);
			if (useColorComponent(TE, coverBlock, srcBlock, side, icon)) {
				aoSetColor(TE, renderBlocks, coverBlock, side, baseRGB[0] * dyeRGB[0], baseRGB[1] * dyeRGB[1], baseRGB[2] * dyeRGB[2], lightness);
			} else {
				aoSetColor(TE, renderBlocks, coverBlock, side, dyeRGB[0], dyeRGB[1], dyeRGB[2], lightness);
			}

		} else {

			if (useColorComponent(TE, coverBlock, srcBlock, side, icon)) {
				tessellator.setColorOpaque_F(base_RGB[0] * baseRGB[0] * dyeRGB[0], base_RGB[1] * baseRGB[1] * dyeRGB[1], base_RGB[2] * baseRGB[2] * dyeRGB[2]);
			} else {
				tessellator.setColorOpaque_F(base_RGB[0] * dyeRGB[0], base_RGB[1] * dyeRGB[1], base_RGB[2] * dyeRGB[2]);
			}

		}
	}

	/**
	 * Returns whether block side color component should be used.
	 */
	protected boolean useColorComponent(TECarpentersBlock TE, Block coverBlock, Block srcBlock, int side, Icon icon)
	{
		if (coverBlock == Block.grass || BlockProperties.getOverlay(TE, side) == OverlayHandler.OVERLAY_GRASS)
		{
			Slope slope = Slope.slopesList[BlockProperties.getData(TE)];
			
			if (side == 1 || icon == BlockGrass.getIconSideOverlay() || slope.isPositive && isSideSloped) {
				return srcBlock == BlockHandler.blockCarpentersDaylightSensor ? side != 1 : true;
			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * Renders overlay on side.
	 */
	protected void renderOverlay(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int side, int x, int y, int z, float lightness, Icon icon)
	{
		int data = BlockProperties.getData(TE);
		int overlay = BlockProperties.getOverlay(TE, coverRendering);

		if (isSideSloped)
		{
			Slope slope = Slope.slopesList[BlockProperties.getData(TE)];			
			if (slope.isPositive) {
				side = 1;
			} else if (slope.slopeType.equals(SlopeType.OBLIQUE_INT) || slope.slopeType.equals(SlopeType.OBLIQUE_EXT) || slope.slopeType.equals(SlopeType.PYRAMID)) {
				side = 2;
			}
		}		
		
		/*
		 * If coverBlock is grass, we need to prerender the grass
		 * sides before drawing any overlays.
		 */
		if (coverBlock == Block.grass)
		{
			icon = getGrassOverlayIcon(TE, side);
			
			if (icon == null) {
				return;
			}
			
			colorSide(TE, renderBlocks, Block.grass, srcBlock, side, x, y, z, icon, lightness);

			if (overlay != OverlayHandler.NO_OVERLAY) {
				renderSide(TE, renderBlocks, side, 0, x, y, z, icon);
			}
		}

		switch (overlay)
		{
		case OverlayHandler.OVERLAY_SNOW:
		{
			switch (side)
			{
			case 0:
				return;
			case 1:
				icon = Block.snow.getBlockTextureFromSide(1);
				break;
			default:
				icon = IconHandler.icon_overlay_snow_side;
				break;
			}
			colorSide(TE, renderBlocks, Block.blockSnow, srcBlock, side, x, y, z, icon, lightness);
			break;
		}
		case OverlayHandler.OVERLAY_HAY:
		{
			switch (side)
			{
				case 0:
					return;
				case 1:
					icon = Block.field_111038_cB.getBlockTextureFromSide(1);
					break;
				default:
					icon = IconHandler.icon_overlay_hay_side;
					break;
			}
			colorSide(TE, renderBlocks, Block.field_111038_cB, srcBlock, side, x, y, z, icon, lightness);
			break;
		}
		case OverlayHandler.OVERLAY_WEB:
		{
			icon = Block.web.getBlockTextureFromSide(side);
			colorSide(TE, renderBlocks, Block.web, srcBlock, side, x, y, z, icon, lightness);
			break;
		}
		case OverlayHandler.OVERLAY_VINE:
		{
			icon = Block.vine.getBlockTextureFromSide(side);
			colorSide(TE, renderBlocks, Block.vine, srcBlock, side, x, y, z, icon, lightness);
			break;
		}
		case OverlayHandler.OVERLAY_MYCELIUM:
		{
			switch (side)
			{
				case 0:
					return;
				case 1:
					icon = Block.mycelium.getBlockTextureFromSide(1);
					break;
				default:
					icon = IconHandler.icon_overlay_mycelium_side;
					break;
			}
			colorSide(TE, renderBlocks, Block.mycelium, srcBlock, side, x, y, z, icon, lightness);
			break;
		}
		case OverlayHandler.OVERLAY_GRASS:
			/*
			 * Grass blocks will prerender the grass overlay by
			 * default, so we'll skip this part if it's not necessary.
			 */
			if (coverBlock != Block.grass)
			{
				icon = getGrassOverlayIcon(TE, side);
				
				if (icon == null) {
					return;
				}
				
				colorSide(TE, renderBlocks, Block.grass, srcBlock, side, x, y, z, icon, lightness);
			}
			break;
		}

		renderSide(TE, renderBlocks, side, 0, x, y, z, icon);
	}

	/**
	 * Returns grass overlay icon.
	 * Needed to reduce redundant code, and also because Block.grass
	 * is the only real exception in the game due to biome-specific
	 * coloring and side specificity.
	 */
	protected Icon getGrassOverlayIcon(TECarpentersBlock TE, int side)
	{
		boolean isPositiveSlope = isSideSloped ? Slope.slopesList[BlockProperties.getData(TE)].isPositive : false;
		
		if (side == 1 || isPositiveSlope) {
			return Block.grass.getBlockTextureFromSide(1);
		} else if (side > 1) {

			/*
			 * When FAST graphics are used, grass blocks use a single
			 * texture to draw each side.
			 * 
			 * Because our implementation of grass sides requires
			 * two render passes, and it must be kept separate to drape
			 * off of our custom blocks, we must draw icon_fast_grass to
			 * mimic the look of vanilla grass blocks.
			 */
			if (RenderBlocks.fancyGrass) {
				return BlockGrass.getIconSideOverlay();
			} else {
				return IconHandler.icon_overlay_fast_grass_side;
			}
		}

		return null;
	}

	/**
	 * Renders pattern on side.
	 */
	protected void renderPattern(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int side, int x, int y, int z, float lightness)
	{
		int pattern = BlockProperties.getPattern(TE, coverRendering);
		Icon icon = IconHandler.icon_pattern[pattern];

		colorSide(TE, renderBlocks, Block.glass, srcBlock, side, x, y, z, icon, lightness);
		renderSide(TE, renderBlocks, side, 0, x, y, z, icon);
	}

	/**
	 * Renders side.
	 */
	protected void renderSide(TECarpentersBlock TE, RenderBlocks renderBlocks, int side, double offset, int x, int y, int z, Icon icon)
	{
		if (offset != 0) {
			VertexHelper.setOffset(offset);
		}

		switch (side) {
		case 0:
			RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
			break;
		case 1:
			RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
			break;
		case 2:
			RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
			break;
		case 3:
			RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
			break;
		case 4:
			RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
			break;
		case 5:
			RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
			break;
		}

		VertexHelper.clearOffset();
	}

	/**
	 * Returns float array with RGB values for block.
	 */
	protected float[] getRGB(IBlockAccess blockAccess, Block block, int x, int y, int z)
	{
		float[] rgb = { 0, 0, 0 };

		int color = FeatureHandler.enableOptifineIntegration ? OptifineHandler.getColorMultiplier(block, blockAccess, x, y, z) : block.colorMultiplier(blockAccess, x, y, z);

		rgb[0] = (color >> 16 & 255) / 255.0F;
		rgb[1] = (color >> 8 & 255) / 255.0F;
		rgb[2] = (color & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable)
		{
			rgb[0] = (rgb[0] * 30.0F + rgb[1] * 59.0F + rgb[2] * 11.0F) / 100.0F;
			rgb[1] = (rgb[0] * 30.0F + rgb[1] * 70.0F) / 100.0F;
			rgb[2] = (rgb[0] * 30.0F + rgb[2] * 70.0F) / 100.0F;
		}

		return rgb;
	}

	/**
	 * Sets up color and renders standard cube based on lighting settings.
	 */
	protected boolean renderStandardBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		float rgb[] = getRGB(renderBlocks.blockAccess, coverBlock, x, y, z);
		
		if (srcBlock == BlockHandler.blockCarpentersSlope && !isSideCover) {

			if (Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[coverBlock.blockID] == 0) {
				renderStandardSlopeWithAmbientOcclusion(TE, renderBlocks, coverBlock, srcBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
			} else {
				renderStandardSlopeWithColorMultiplier(TE, renderBlocks, coverBlock, srcBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
			}

		} else {

			if (Minecraft.isAmbientOcclusionEnabled() && !disableAO && Block.lightValue[coverBlock.blockID] == 0) {
				renderStandardBlockWithAmbientOcclusion(TE, renderBlocks, coverBlock, srcBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
			} else {
				renderStandardBlockWithColorMultiplier(TE, renderBlocks, coverBlock, srcBlock, x, y, z, rgb[0], rgb[1], rgb[2]);
			}

		}

		return true;
	}

	/**
	 * Overridden by slopes.
	 */
	protected boolean renderStandardSlopeWithAmbientOcclusion(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
	{
		return false;
	}

	/**
	 * Overridden by slopes.
	 */
	protected boolean renderStandardSlopeWithColorMultiplier(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
	{
		return false;
	}

	/**
	 * Render block using ambient occlusion.
	 */
	protected boolean renderStandardBlockWithAmbientOcclusion(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
	{
		renderBlocks.enableAO = true;
		boolean side_rendered = false;
		Tessellator.instance.setBrightness(983055);

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0) || renderBlocks.renderMinY > 0.0D)
		{
			setLightnessYNeg(renderBlocks, coverBlock, x, y, z);
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 0, x, y, z, 0.5F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1) || renderBlocks.renderMaxY < 1.0D)
		{
			setLightnessYPos(renderBlocks, coverBlock, x, y, z);
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 1, x, y, z, 1.0F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2) || renderBlocks.renderMinZ > 0.0D)
		{
			setLightnessZNeg(renderBlocks, coverBlock, x, y, z);
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 2, x, y, z, 0.8F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3) || renderBlocks.renderMaxZ < 1.0D)
		{
			setLightnessZPos(renderBlocks, coverBlock, x, y, z);
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 3, x, y, z, 0.8F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4) || renderBlocks.renderMinX > 0.0D)
		{
			setLightnessXNeg(renderBlocks, coverBlock, x, y, z);
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 4, x, y, z, 0.6F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5) || renderBlocks.renderMaxX < 1.0D)
		{
			setLightnessXPos(renderBlocks, coverBlock, x, y, z);
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 5, x, y, z, 0.6F);
			side_rendered = true;
		}
		
		if (base_ao[0] > 0)
			

		renderBlocks.enableAO = false;
		return side_rendered;
	}

	/**
	 * Fills AO variables with lightness for bottom face.
	 */
	protected void setLightnessYNeg(RenderBlocks renderBlocks, Block coverBlock, int x, int y, int z)
	{
		int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		boolean useOffsetLightness = renderBlocks.renderMinY >= 0.0D && renderBlocks.renderMinY <= 0.5D;
		boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

		y -= useOffsetLightness ? 1 : 0;

		renderBlocks.aoBrightnessXYNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoBrightnessYZNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoBrightnessYZNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
		renderBlocks.aoBrightnessXYPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
		renderBlocks.aoLightValueScratchXYNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoLightValueScratchYZNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoLightValueScratchYZNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
		renderBlocks.aoLightValueScratchXYPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
		boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y - 1, z)];
		boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y - 1, z)];
		boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z + 1)];
		boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z - 1)];

		if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
			renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXYNN;
			renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXYNN;
		} else {
			renderBlocks.aoLightValueScratchXYZNNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1);
			renderBlocks.aoBrightnessXYZNNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
		}

		if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
			renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXYNN;
			renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXYNN;
		} else {
			renderBlocks.aoLightValueScratchXYZNNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1);
			renderBlocks.aoBrightnessXYZNNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
		}

		if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
			renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXYPN;
			renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXYPN;
		} else {
			renderBlocks.aoLightValueScratchXYZPNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1);
			renderBlocks.aoBrightnessXYZPNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
		}

		if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
			renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXYPN;
			renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXYPN;
		} else {
			renderBlocks.aoLightValueScratchXYZPNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1);
			renderBlocks.aoBrightnessXYZPNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
		}

		y += useOffsetLightness ? 1 : 0;

		int offsetBrightness = mixedBrightness;

		if (renderBlocks.renderMinY <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y - 1, z))
			offsetBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);

		float aoLightValue = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);

		if (maxSmoothLighting) {
			float aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
			float aoBottomLeftTemp = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
			float aoBottomRightTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
			float aoTopRightTemp = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoLightValue + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
			base_ao[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
			base_ao[1] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
			base_ao[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
			base_ao[3] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
			int brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, offsetBrightness);
			int brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, offsetBrightness);
			int brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, offsetBrightness);
			int brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
			renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
			renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
			renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
		} else {
			base_ao[0] = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
			base_ao[1] = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
			base_ao[2] = (aoLightValue + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
			base_ao[3] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoLightValue + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
			renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, offsetBrightness);
			renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, offsetBrightness);
			renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, offsetBrightness);
			renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
		}
	}

	/**
	 * Fills AO variables with lightness for top face.
	 */
	protected void setLightnessYPos(RenderBlocks renderBlocks, Block coverBlock, int x, int y, int z)
	{
		int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		boolean useOffsetLightness = renderBlocks.renderMaxY <= 1.0D && renderBlocks.renderMaxY > 0.5D;
		boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

		y += useOffsetLightness ? 1 : 0;

		renderBlocks.aoBrightnessXYNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoBrightnessXYPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
		renderBlocks.aoBrightnessYZPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoBrightnessYZPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
		renderBlocks.aoLightValueScratchXYNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoLightValueScratchXYPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
		renderBlocks.aoLightValueScratchYZPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoLightValueScratchYZPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
		boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y + 1, z)];
		boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y + 1, z)];
		boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z + 1)];
		boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z - 1)];

		if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
			renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXYNP;
			renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXYNP;
		} else {
			renderBlocks.aoLightValueScratchXYZNPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1);
			renderBlocks.aoBrightnessXYZNPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
		}

		if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
			renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXYPP;
			renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXYPP;
		} else {
			renderBlocks.aoLightValueScratchXYZPPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1);
			renderBlocks.aoBrightnessXYZPPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
		}

		if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
			renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXYNP;
			renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXYNP;
		} else {
			renderBlocks.aoLightValueScratchXYZNPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1);
			renderBlocks.aoBrightnessXYZNPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
		}

		if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
			renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXYPP;
			renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXYPP;
		} else {
			renderBlocks.aoLightValueScratchXYZPPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1);
			renderBlocks.aoBrightnessXYZPPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
		}

		y -= useOffsetLightness ? 1 : 0;

		int offsetBrightness = mixedBrightness;

		if (renderBlocks.renderMaxY >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y + 1, z)) {
			offsetBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
		}

		float aoLightValue = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);

		if (maxSmoothLighting) {
			float aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F;
			float aoBottomLeftTemp = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
			float aoBottomRightTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
			float aoTopRightTemp = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
			base_ao[1] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
			base_ao[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
			base_ao[3] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
			base_ao[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
			int brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
			int brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, offsetBrightness);
			int brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
			int brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));
			renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
			renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
			renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
		} else {
			base_ao[1] = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F;
			base_ao[0] = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
			base_ao[3] = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
			base_ao[2] = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
			renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, offsetBrightness);
			renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
			renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
		}
	}
	
	/**
	 * Fills AO variables with lightness for North face.
	 */
	protected void setLightnessZNeg(RenderBlocks renderBlocks, Block coverBlock, int x, int y, int z)
	{
		int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		boolean useOffsetLightness = renderBlocks.renderMinZ >= 0.0D && renderBlocks.renderMinZ <= 0.5D;
		boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

		z -= useOffsetLightness ? 1 : 0;

		renderBlocks.aoLightValueScratchXZNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoLightValueScratchYZNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoLightValueScratchYZPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
		renderBlocks.aoLightValueScratchXZPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
		renderBlocks.aoBrightnessXZNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoBrightnessYZNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoBrightnessYZPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
		renderBlocks.aoBrightnessXZPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
		boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];
		boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
		boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z - 1)];
		boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z - 1)];

		if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
			renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
			renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
		} else {
			renderBlocks.aoLightValueScratchXYZNNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z);
			renderBlocks.aoBrightnessXYZNNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
		}

		if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
			renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
			renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
		} else {
			renderBlocks.aoLightValueScratchXYZNPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z);
			renderBlocks.aoBrightnessXYZNPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
		}

		if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
			renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
			renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
		} else {
			renderBlocks.aoLightValueScratchXYZPNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z);
			renderBlocks.aoBrightnessXYZPNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
		}

		if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
			renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
			renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
		} else {
			renderBlocks.aoLightValueScratchXYZPPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z);
			renderBlocks.aoBrightnessXYZPPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
		}

		z += useOffsetLightness ? 1 : 0;

		int offsetBrightness = mixedBrightness;

		if (renderBlocks.renderMinZ <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z - 1)) {
			offsetBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
		}

		float aoLightValue = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);

		if (maxSmoothLighting) {
			float aoTopLeftTemp = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
			float aoBottomLeftTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
			float aoBottomRightTemp = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
			float aoTopRightTemp = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F;
			base_ao[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
			base_ao[3] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
			base_ao[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
			base_ao[1] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
			int brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
			int brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
			int brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
			int brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
			renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
			renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
			renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
		} else {
			base_ao[0] = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
			base_ao[3] = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
			base_ao[2] = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
			base_ao[1] = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F;
			renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness);
			renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, offsetBrightness);
			renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
			renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, offsetBrightness);
		}
	}
	
	/**
	 * Fills AO variables with lightness for South face.
	 */
	protected void setLightnessZPos(RenderBlocks renderBlocks, Block coverBlock, int x, int y, int z)
	{
		int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		boolean useOffsetLightness = renderBlocks.renderMaxZ <= 1.0D && renderBlocks.renderMaxZ > 0.5D;
		boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

		z += useOffsetLightness ? 1 : 0;

		renderBlocks.aoLightValueScratchXZNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoLightValueScratchXZPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
		renderBlocks.aoLightValueScratchYZNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoLightValueScratchYZPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
		renderBlocks.aoBrightnessXZNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
		renderBlocks.aoBrightnessXZPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
		renderBlocks.aoBrightnessYZNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoBrightnessYZPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
		boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
		boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];
		boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z + 1)];
		boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z + 1)];

		if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
			renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
			renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
		} else {
			renderBlocks.aoLightValueScratchXYZNNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z);
			renderBlocks.aoBrightnessXYZNNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
		}

		if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
			renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
			renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
		} else {
			renderBlocks.aoLightValueScratchXYZNPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z);
			renderBlocks.aoBrightnessXYZNPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
		}

		if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
			renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
			renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
		} else {
			renderBlocks.aoLightValueScratchXYZPNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z);
			renderBlocks.aoBrightnessXYZPNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
		}

		if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
			renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
			renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
		} else {
			renderBlocks.aoLightValueScratchXYZPPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z);
			renderBlocks.aoBrightnessXYZPPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
		}

		z -= useOffsetLightness ? 1 : 0;

		int offsetBrightness = mixedBrightness;

		if (renderBlocks.renderMaxZ >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z + 1)) {
			offsetBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
		}

		float aoLightValue = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);

		if (maxSmoothLighting) {
			float aoTopLeftTemp = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
			float aoBottomLeftTemp = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
			float aoBottomRightTemp = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
			float aoTopRightTemp = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
			base_ao[0] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
			base_ao[3] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMinX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
			base_ao[2] = (float)(aoTopLeftTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
			base_ao[1] = (float)(aoTopLeftTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoBottomRightTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
			int brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
			int brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
			int brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
			int brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, renderBlocks.renderMaxY * renderBlocks.renderMinX);
			renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, renderBlocks.renderMinY * renderBlocks.renderMinX);
			renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, renderBlocks.renderMinY * renderBlocks.renderMaxX);
			renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, renderBlocks.renderMaxY * renderBlocks.renderMaxX);
		} else {
			base_ao[0] = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
			base_ao[1] = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
			base_ao[2] = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
			base_ao[3] = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
			renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, offsetBrightness);
			renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
			renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
			renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, offsetBrightness);
		}
	}
	
	/**
	 * Fills AO variables with lightness for West face.
	 */
	protected void setLightnessXNeg(RenderBlocks renderBlocks, Block coverBlock, int x, int y, int z)
	{
		int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		boolean useOffsetLightness = renderBlocks.renderMinX >= 0.0D && renderBlocks.renderMinX <= 0.5D;
		boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

		x -= useOffsetLightness ? 1 : 0;

		renderBlocks.aoLightValueScratchXYNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoLightValueScratchXZNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoLightValueScratchXZNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
		renderBlocks.aoLightValueScratchXYNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
		renderBlocks.aoBrightnessXYNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoBrightnessXZNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoBrightnessXZNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
		renderBlocks.aoBrightnessXYNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
		boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y + 1, z)];
		boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y - 1, z)];
		boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
		boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];

		if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
			renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
			renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
		} else {
			renderBlocks.aoLightValueScratchXYZNNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1);
			renderBlocks.aoBrightnessXYZNNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
		}

		if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
			renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
			renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
		} else {
			renderBlocks.aoLightValueScratchXYZNNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1);
			renderBlocks.aoBrightnessXYZNNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
		}

		if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
			renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
			renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
		} else {
			renderBlocks.aoLightValueScratchXYZNPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1);
			renderBlocks.aoBrightnessXYZNPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
		}

		if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
			renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
			renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
		} else {
			renderBlocks.aoLightValueScratchXYZNPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1);
			renderBlocks.aoBrightnessXYZNPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
		}

		x += useOffsetLightness ? 1 : 0;

		int offsetBrightness = mixedBrightness;

		if (renderBlocks.renderMinX <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x - 1, y, z)) {
			offsetBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
		}

		float aoLightValue = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);

		if (maxSmoothLighting) {
			float aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
			float aoBottomLeftTemp = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;
			float aoBottomRightTemp = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
			float aoTopRightTemp = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F;
			base_ao[0] = (float)(aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxZ + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);
			base_ao[3] = (float)(aoBottomLeftTemp * renderBlocks.renderMaxY * renderBlocks.renderMinZ + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
			base_ao[2] = (float)(aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMinZ + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
			base_ao[1] = (float)(aoBottomLeftTemp * renderBlocks.renderMinY * renderBlocks.renderMaxZ + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);
			int brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, offsetBrightness);
			int brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, offsetBrightness);
			int brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, offsetBrightness);
			int brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMaxY * renderBlocks.renderMaxZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);
			renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMaxY * renderBlocks.renderMinZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
			renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMinY * renderBlocks.renderMinZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
			renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessBottomLeftTemp, brightnessBottomRightTemp, brightnessTopRightTemp, brightnessTopLeftTemp, renderBlocks.renderMinY * renderBlocks.renderMaxZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);
		} else {
			base_ao[1] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
			base_ao[0] = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;
			base_ao[3] = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
			base_ao[2] = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F;
			renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, offsetBrightness);
			renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, offsetBrightness);
			renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, offsetBrightness);
		}
	}

	/**
	 * Fills AO variables with lightness for East face.
	 */
	protected void setLightnessXPos(RenderBlocks renderBlocks, Block coverBlock, int x, int y, int z)
	{
		int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		boolean useOffsetLightness = renderBlocks.renderMaxX <= 1.0D && renderBlocks.renderMaxX > 0.5D;
		boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

		x += useOffsetLightness ? 1 : 0;

		renderBlocks.aoLightValueScratchXYPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoLightValueScratchXZPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoLightValueScratchXZPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
		renderBlocks.aoLightValueScratchXYPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
		renderBlocks.aoBrightnessXYPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
		renderBlocks.aoBrightnessXZPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
		renderBlocks.aoBrightnessXZPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
		renderBlocks.aoBrightnessXYPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
		boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y + 1, z)];
		boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y - 1, z)];
		boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
		boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];

		if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
			renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
			renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
		} else {
			renderBlocks.aoLightValueScratchXYZPNN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1);
			renderBlocks.aoBrightnessXYZPNN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
		}

		if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
			renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
			renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
		} else {
			renderBlocks.aoLightValueScratchXYZPNP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1);
			renderBlocks.aoBrightnessXYZPNP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
		}

		if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
			renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
			renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
		} else {
			renderBlocks.aoLightValueScratchXYZPPN = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1);
			renderBlocks.aoBrightnessXYZPPN = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
		}

		if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
			renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
			renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
		} else {
			renderBlocks.aoLightValueScratchXYZPPP = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1);
			renderBlocks.aoBrightnessXYZPPP = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
		}

		x -= useOffsetLightness ? 1 : 0;

		int offsetBrightness = mixedBrightness;

		if (renderBlocks.renderMaxX >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x + 1, y, z)) {
			offsetBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
		}

		float aoLightValue = coverBlock.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);

		if (maxSmoothLighting) {
			float aoTopLeftTemp = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
			float aoBottomLeftTemp = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F;
			float aoBottomRightTemp = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
			float aoTopRightTemp = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
			base_ao[0] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * renderBlocks.renderMinY * renderBlocks.renderMaxZ);
			base_ao[3] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoBottomRightTemp * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * renderBlocks.renderMinY * renderBlocks.renderMinZ);
			base_ao[2] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoTopRightTemp * renderBlocks.renderMaxY * renderBlocks.renderMinZ);
			base_ao[1] = (float)(aoTopLeftTemp * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ + aoBottomLeftTemp * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoBottomRightTemp * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoTopRightTemp * renderBlocks.renderMaxY * renderBlocks.renderMaxZ);
			int brightnessTopLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
			int brightnessBottomLeftTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
			int brightnessBottomRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, offsetBrightness);
			int brightnessTopRightTemp = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
			renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * renderBlocks.renderMaxZ);
			renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * renderBlocks.renderMinZ);
			renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * renderBlocks.renderMinZ);
			renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessTopLeftTemp, brightnessTopRightTemp, brightnessBottomRightTemp, brightnessBottomLeftTemp, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * renderBlocks.renderMaxZ);
		} else {
			base_ao[0] = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
			base_ao[3] = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F;
			base_ao[2] = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
			base_ao[1] = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
			renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness);
			renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness);
			renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, offsetBrightness);
			renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, offsetBrightness);
		}
	}
	
	/**
	 * Renders a standard cube block at the given coordinates, with a given color ratio.  Args: block, x, y, z, r, g, b
	 */
	protected boolean renderStandardBlockWithColorMultiplier(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z, float red, float green, float blue)
	{
		renderBlocks.enableAO = false;
		Tessellator tessellator = Tessellator.instance;
		boolean side_rendered = false;

		int mixedBrightness = coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, 0) || renderBlocks.renderMinY > 0.0D)
		{
			tessellator.setBrightness(renderBlocks.renderMinY > 0.0D ? mixedBrightness : coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z));
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 0, x, y, z, 0.5F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, 1) || renderBlocks.renderMaxY < 1.0D)
		{
			tessellator.setBrightness(renderBlocks.renderMaxY < 1.0D ? mixedBrightness : coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z));
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 1, x, y, z, 1.0F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, 2) || renderBlocks.renderMinZ > 0.0D)
		{
			tessellator.setBrightness(renderBlocks.renderMinZ > 0.0D ? mixedBrightness : coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1));
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 2, x, y, z, 0.8F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, 3) || renderBlocks.renderMaxZ < 1.0D)
		{
			tessellator.setBrightness(renderBlocks.renderMaxZ < 1.0D ? mixedBrightness : coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1));
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 3, x, y, z, 0.8F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, 4) || renderBlocks.renderMinX > 0.0D)
		{
			tessellator.setBrightness(renderBlocks.renderMinX > 0.0D ? mixedBrightness : coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z));
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 4, x, y, z, 0.6F);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, 5) || renderBlocks.renderMaxX < 1.0D)
		{
			tessellator.setBrightness(renderBlocks.renderMaxX < 1.0D ? mixedBrightness : coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z));
			prepareRender(TE, renderBlocks, coverBlock, srcBlock, 5, x, y, z, 0.6F);
			side_rendered = true;
		}

		return side_rendered;
	}

}