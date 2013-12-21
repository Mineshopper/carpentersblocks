package carpentersblocks.renderer.helper;

import static carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import static carpentersblocks.renderer.helper.VertexHelper.NORTHWEST;
import static carpentersblocks.renderer.helper.VertexHelper.SOUTHWEST;
import static carpentersblocks.renderer.helper.VertexHelper.SOUTHEAST;
import static carpentersblocks.renderer.helper.VertexHelper.NORTHEAST;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.BlockHandlerBase;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.DyeColorHandler;
import carpentersblocks.util.handler.OptifineHandler;
import carpentersblocks.util.registry.FeatureRegistry;

public class LightingHelper {

	public static LightingHelper instance = new LightingHelper();
	
	private BlockHandlerBase	blockHandler;
	private RenderBlocks 		renderBlocks;
	private float				lightness;
	public final int			NORMAL_BRIGHTNESS 	= 983055;
	public final int			MAX_BRIGHTNESS 		= 15728880;

	private float 				lightnessOffset;
	private boolean				hasBrightnessOverride;
	private int					brightnessOverride;
	private boolean				hasColorOverride;
	private float[]				colorOverride 		= new float[3];
	
	public LightingHelper bind(BlockHandlerBase blockHandler)
	{
		this.blockHandler = blockHandler;
		this.renderBlocks = blockHandler.renderBlocks;
		return this;
	}
	
	/**
	 * Sets lightness.  Can be applied at any time before actually
	 * rendering the side.  It takes effect when
	 * BlockHandlerBase.delegateSideRender() is called.
	 */
	public LightingHelper setLightness(float lightness)
	{
		this.lightness = lightness;
		return this;
	}

	/**
	 * Sets color override.
	 */
	public void setColorOverride(float[] rgb)
	{
		hasColorOverride = true;
		colorOverride[0] = rgb[0];
		colorOverride[1] = rgb[1];
		colorOverride[2] = rgb[2];
	}

	/**
	 * Clears color override.
	 */
	public void clearColorOverride()
	{
		hasColorOverride = false;
	}

	/**
	 * Sets brightness offset.
	 */
	public void setBrightnessOverride(int brightness)
	{
		hasBrightnessOverride = true;
		brightnessOverride = brightness;
	}

	/**
	 * Clears brightness override.
	 */
	public void clearBrightnessOverride()
	{
		hasBrightnessOverride = false;
	}

	/**
	 * Sets lightness offset.
	 */
	public void setLightnessOffset(float lightness)
	{
		lightnessOffset = lightness;
	}

	/**
	 * Clears lightness override.
	 */
	public void clearLightnessOffset()
	{
		lightnessOffset = 0.0F;
	}

	/**
	 * Stores uncolored, ambient occlusion values for each corner of every face.
	 */
	public float[] ao = { 0.0F, 0.0F, 0.0F, 0.0F };
	
	/**
	 * Sets AO color.
	 */
	private void aoSetColor(float red, float green, float blue, float lightness)
	{
		/* Set base light values against color multiplier. */
		
		renderBlocks.colorRedTopLeft = renderBlocks.colorRedBottomLeft = renderBlocks.colorRedBottomRight = renderBlocks.colorRedTopRight = red * lightness;
		renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = green * lightness;
		renderBlocks.colorBlueTopLeft = renderBlocks.colorBlueBottomLeft = renderBlocks.colorBlueBottomRight = renderBlocks.colorBlueTopRight = blue * lightness;

		/* Shade corner values. */
		
		renderBlocks.colorRedTopLeft *= ao[TOP_LEFT];
		renderBlocks.colorGreenTopLeft *= ao[TOP_LEFT];
		renderBlocks.colorBlueTopLeft *= ao[TOP_LEFT];
		renderBlocks.colorRedBottomLeft *= ao[BOTTOM_LEFT];
		renderBlocks.colorGreenBottomLeft *= ao[BOTTOM_LEFT];
		renderBlocks.colorBlueBottomLeft *= ao[BOTTOM_LEFT];
		renderBlocks.colorRedBottomRight *= ao[BOTTOM_RIGHT];
		renderBlocks.colorGreenBottomRight *= ao[BOTTOM_RIGHT];
		renderBlocks.colorBlueBottomRight *= ao[BOTTOM_RIGHT];
		renderBlocks.colorRedTopRight *= ao[TOP_RIGHT];
		renderBlocks.colorGreenTopRight *= ao[TOP_RIGHT];
		renderBlocks.colorBlueTopRight *= ao[TOP_RIGHT];
	}

	/**
	 * Resets AO color.
	 */
	private void aoResetColor()
	{
		renderBlocks.colorRedTopLeft = ao[TOP_LEFT];
		renderBlocks.colorGreenTopLeft = ao[TOP_LEFT];
		renderBlocks.colorBlueTopLeft = ao[TOP_LEFT];
		renderBlocks.colorRedBottomLeft = ao[BOTTOM_LEFT];
		renderBlocks.colorGreenBottomLeft = ao[BOTTOM_LEFT];
		renderBlocks.colorBlueBottomLeft = ao[BOTTOM_LEFT];
		renderBlocks.colorRedBottomRight = ao[BOTTOM_RIGHT];
		renderBlocks.colorGreenBottomRight = ao[BOTTOM_RIGHT];
		renderBlocks.colorBlueBottomRight = ao[BOTTOM_RIGHT];
		renderBlocks.colorRedTopRight = ao[TOP_RIGHT];
		renderBlocks.colorGreenTopRight = ao[TOP_RIGHT];
		renderBlocks.colorBlueTopRight = ao[TOP_RIGHT];
	}

	/**
	 * Returns float array with RGB values for block.
	 */
	public float[] getRGB(Block block, int x, int y, int z)
	{
		float[] rgb = { 0, 0, 0 };

		int color = FeatureRegistry.enableOptifineIntegration ? OptifineHandler.getColorMultiplier(block, renderBlocks.blockAccess, x, y, z) : block.colorMultiplier(renderBlocks.blockAccess, x, y, z);

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
	 * Apply lightness and color to AO or tessellator.
	 */
	public void colorSide(Block block, int x, int y, int z, int side, Icon icon)
	{
		float[] blockRGB = getRGB(block, x, y, z);
		float[] dyeRGB = blockHandler.suppressDyeColor ? new float[] { 1.0F, 1.0F, 1.0F } : DyeColorHandler.getDyeColorRGB(blockHandler.hasDyeColorOverride ? blockHandler.dyeColorOverride : BlockProperties.getDyeColor(blockHandler.TE, blockHandler.coverRendering));

		/* Calculate color for side. */

		float[] finalRGB = { (blockRGB[0] += lightnessOffset) * dyeRGB[0], (blockRGB[1] += lightnessOffset) * dyeRGB[1], (blockRGB[2] += lightnessOffset) * dyeRGB[2] };

		if (block.equals(Block.grass))
		{
			boolean posSlopedSide = blockHandler.isSideSloped ? Slope.slopesList[BlockProperties.getData(blockHandler.TE)].isPositive : false;

			if (!(side == 1 || icon == BlockGrass.getIconSideOverlay() || posSlopedSide)) {
				finalRGB = dyeRGB;
			}
		}

		/* Apply color to side. */

		if (renderBlocks.enableAO) {

			aoResetColor();
			if (hasColorOverride) {
				aoSetColor(colorOverride[0], colorOverride[1], colorOverride[2], lightness);
			} else {
				aoSetColor(finalRGB[0], finalRGB[1], finalRGB[2], lightness);
			}

		} else {

			if (hasColorOverride) {
				Tessellator.instance.setColorOpaque_F(colorOverride[0] * lightness, colorOverride[1] * lightness, colorOverride[2] * lightness);
			} else {
				Tessellator.instance.setColorOpaque_F(finalRGB[0] * lightness, finalRGB[1] * lightness, finalRGB[2] * lightness);
			}

		}
	}

	/**
	 * Fills AO variables with lightness for bottom face.
	 */
	public void setLightingYNeg(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMinY > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
			Tessellator.instance.setBrightness(brightness);

		} else {

			Tessellator.instance.setBrightness(NORMAL_BRIGHTNESS);
			boolean useOffsetLightness = renderBlocks.renderMinY >= 0.0D && renderBlocks.renderMinY <= 0.5D;
			boolean partialLighting = renderBlocks.partialRenderBounds;

			y -= useOffsetLightness ? 1 : 0;

			renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
			renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
			renderBlocks.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
			renderBlocks.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
			boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y - 1, z)];
			boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y - 1, z)];
			boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z + 1)];
			boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z - 1)];

			if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
				renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXYNN;
				renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXYNN;
			} else {
				renderBlocks.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1);
				renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
			}

			if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
				renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXYNN;
				renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXYNN;
			} else {
				renderBlocks.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1);
				renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
			}

			if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
				renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXYPN;
				renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXYPN;
			} else {
				renderBlocks.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1);
				renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
			}

			if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
				renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXYPN;
				renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXYPN;
			} else {
				renderBlocks.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1);
				renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
			}

			y += useOffsetLightness ? 1 : 0;

			int offsetBrightness = mixedBrightness;

			if (renderBlocks.renderMinY <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y - 1, z)) {
				offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
			}

			float aoBlock = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);

			if (partialLighting) {

				float aoMixed_XYZNNN = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoBlock + renderBlocks.aoLightValueScratchYZNN) / 4.0F; /* NW CORNER */
				float aoMixed_XYZNNP = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoBlock) / 4.0F; /* SW CORNER */
				float aoMixed_XYZPNP = (renderBlocks.aoLightValueScratchYZNP + aoBlock + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F; /* SE CORNER */
				float aoMixed_XYZPNN = (aoBlock + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F; /* NE CORNER */

				ao[NORTHWEST] = (float)(aoMixed_XYZNNP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPNP * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX)); /* NW CORNER */
				ao[SOUTHWEST] = (float)(aoMixed_XYZNNP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPNP * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX)); /* SW CORNER */
				ao[SOUTHEAST] = (float)(aoMixed_XYZNNP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPNP * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX)); /* SE CORNER */
				ao[NORTHEAST] = (float)(aoMixed_XYZNNP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPNP * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX)); /* NE CORNER */
				
				int brightnessMixed_XYZNNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, offsetBrightness); /* NW CORNER */
				int brightnessMixed_XYZNNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, offsetBrightness); /* SW CORNER */
				int brightnessMixed_XYZPNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, offsetBrightness); /* SE CORNER */
				int brightnessMixed_XYZPNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, offsetBrightness); /* NE CORNER */
				
				renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZNNN, brightnessMixed_XYZPNN, brightnessMixed_XYZPNP, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX)); /* NW CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZNNN, brightnessMixed_XYZPNN, brightnessMixed_XYZPNP, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX)); /* SW CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZNNN, brightnessMixed_XYZPNN, brightnessMixed_XYZPNP, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX)); /* SE CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZNNN, brightnessMixed_XYZPNN, brightnessMixed_XYZPNP, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX)); /* NE CORNER */
		
			} else {

				ao[NORTHWEST] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoBlock + renderBlocks.aoLightValueScratchYZNN) / 4.0F; /* NW CORNER */
				ao[SOUTHWEST] = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoBlock) / 4.0F; /* SW CORNER */
				ao[SOUTHEAST] = (renderBlocks.aoLightValueScratchYZNP + aoBlock + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F; /* SE CORNER */
				ao[NORTHEAST] = (aoBlock + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F; /* NE CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, offsetBrightness); /* NW CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, offsetBrightness); /* SW CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, offsetBrightness); /* SE CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, offsetBrightness); /* NE CORNER */
				
			}
		}
	}

	/**
	 * Fills AO variables with lightness for top face.
	 */
	public void setLightingYPos(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMaxY < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
			Tessellator.instance.setBrightness(brightness);

		} else {

			Tessellator.instance.setBrightness(NORMAL_BRIGHTNESS);
			boolean useOffsetLightness = renderBlocks.renderMaxY <= 1.0D && renderBlocks.renderMaxY > 0.5D;
			boolean partialLighting = renderBlocks.partialRenderBounds;

			y += useOffsetLightness ? 1 : 0;

			renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
			renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
			renderBlocks.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
			renderBlocks.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
			boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y + 1, z)];
			boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y + 1, z)];
			boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z + 1)];
			boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z - 1)];

			if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
				renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXYNP;
				renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXYNP;
			} else {
				renderBlocks.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1);
				renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
			}

			if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
				renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXYPP;
				renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXYPP;
			} else {
				renderBlocks.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1);
				renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
			}

			if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
				renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXYNP;
				renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXYNP;
			} else {
				renderBlocks.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1);
				renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
			}

			if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
				renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXYPP;
				renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXYPP;
			} else {
				renderBlocks.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1);
				renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
			}

			y -= useOffsetLightness ? 1 : 0;

			int offsetBrightness = mixedBrightness;

			if (renderBlocks.renderMaxY >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y + 1, z)) {
				offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
			}

			float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);

			if (partialLighting) {

				float aoMixed_XYZNPN = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F; /* NW CORNER */
				float aoMixed_XYZNPP = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F; /* SW CORNER */
				float aoMixed_XYZPPP = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F; /* SE CORNER */
				float aoMixed_XYZPPN = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F; /* NE CORNER */

				ao[NORTHWEST] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX)); /* NW CORNER */
				ao[SOUTHWEST] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX)); /* SW CORNER */
				ao[SOUTHEAST] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX)); /* SE CORNER */
				ao[NORTHEAST] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX)); /* NE CORNER */

				int brightnessMixed_XYZNPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness); /* NW CORNER */
				int brightnessMixed_XYZNPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, offsetBrightness); /* SW CORNER */
				int brightnessMixed_XYZPPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, offsetBrightness); /* SE CORNER */
				int brightnessMixed_XYZPPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, offsetBrightness); /* NE CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, brightnessMixed_XYZNPP, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX)); /* NW CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, brightnessMixed_XYZNPP, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX)); /* SW CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, brightnessMixed_XYZNPP, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX)); /* SE CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, brightnessMixed_XYZNPP, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX)); /* NE CORNER */

			} else {

				ao[NORTHWEST] = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F; /* NW CORNER */
				ao[SOUTHWEST] = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F; /* SW CORNER */
				ao[SOUTHEAST] = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F; /* SE CORNER */
				ao[NORTHEAST] = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F; /* NE CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness); /* NW CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, offsetBrightness); /* SW CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, offsetBrightness); /* SE CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, offsetBrightness); /* NE CORNER */
				
			}
		}
	}

	/**
	 * Fills AO variables with lightness for North face.
	 */
	public void setLightingZNeg(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMinZ > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
			Tessellator.instance.setBrightness(brightness);

		} else {

			Tessellator.instance.setBrightness(NORMAL_BRIGHTNESS);
			boolean useOffsetLightness = renderBlocks.renderMinZ >= 0.0D && renderBlocks.renderMinZ <= 0.5D;
			boolean partialLighting = renderBlocks.partialRenderBounds;

			z -= useOffsetLightness ? 1 : 0;

			renderBlocks.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
			renderBlocks.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
			renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
			renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
			boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];
			boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
			boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z - 1)];
			boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z - 1)];

			if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
				renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
				renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
			} else {
				renderBlocks.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z);
				renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
			}

			if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
				renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
				renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
			} else {
				renderBlocks.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z);
				renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
			}

			if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
				renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
				renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
			} else {
				renderBlocks.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z);
				renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
			}

			if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
				renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
				renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
			} else {
				renderBlocks.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z);
				renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
			}

			z += useOffsetLightness ? 1 : 0;

			int offsetBrightness = mixedBrightness;

			if (renderBlocks.renderMinZ <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z - 1)) {
				offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
			}

			float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);

			if (partialLighting) {

				float aoMixed_XYZPPN = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F; /* TL CORNER */
				float aoMixed_XYZPNN = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F; /* BL CORNER */
				float aoMixed_XYZNNN = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F; /* BR CORNER */
				float aoMixed_XYZNPN = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F; /* TR CORNER */

				ao[TOP_LEFT] = (float)(aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPN * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX)); /* TL CORNER */
				ao[BOTTOM_LEFT] = (float)(aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPN * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX)); /* BL CORNER */
				ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPN * renderBlocks.renderMinY * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX)); /* BR CORNER */
				ao[TOP_RIGHT] = (float)(aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPN * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX)); /* TR CORNER */

				int brightnessMixed_XYZPPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, offsetBrightness); /* TL CORNER */
				int brightnessMixed_XYZPNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, offsetBrightness); /* BL CORNER */
				int brightnessMixed_XYZNNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, offsetBrightness); /* BR CORNER */
				int brightnessMixed_XYZNPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness); /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX)); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX)); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX)); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX)); /* TR CORNER */
								
			} else {

				ao[TOP_LEFT] = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F; /* TL CORNER */
				ao[BOTTOM_LEFT] = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F; /* BL CORNER */
				ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F; /* BR CORNER */
				ao[TOP_RIGHT] = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F; /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, offsetBrightness); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, offsetBrightness); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, offsetBrightness); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, offsetBrightness); /* TR CORNER */
			
			}
		}
	}

	/**
	 * Fills AO variables with lightness for South face.
	 */
	public void setLightingZPos(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMaxZ < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
			Tessellator.instance.setBrightness(brightness);

		} else {

			Tessellator.instance.setBrightness(NORMAL_BRIGHTNESS);
			boolean useOffsetLightness = renderBlocks.renderMaxZ <= 1.0D && renderBlocks.renderMaxZ > 0.5D;
			boolean partialLighting = renderBlocks.partialRenderBounds;

			z += useOffsetLightness ? 1 : 0;

			renderBlocks.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);
			renderBlocks.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
			renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
			renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
			renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
			boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
			boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];
			boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y + 1, z + 1)];
			boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x, y - 1, z + 1)];

			if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
				renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
				renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
			} else {
				renderBlocks.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z);
				renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
			}

			if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
				renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
				renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
			} else {
				renderBlocks.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z);
				renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
			}

			if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
				renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
				renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
			} else {
				renderBlocks.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z);
				renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
			}

			if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
				renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
				renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
			} else {
				renderBlocks.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z);
				renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
			}

			z -= useOffsetLightness ? 1 : 0;

			int offsetBrightness = mixedBrightness;

			if (renderBlocks.renderMaxZ >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z + 1)) {
				offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
			}

			float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);

			if (partialLighting) {

				float aoMixed_XYZNPP = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F; /* TL CORNER */
				float aoMixed_XYZNNP = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F; /* BL CORNER */
				float aoMixed_XYZPNP = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F; /* BR CORNER */
				float aoMixed_XYZPPP = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F; /* TR CORNER */

				ao[TOP_LEFT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX)); /* TL CORNER */
				ao[BOTTOM_LEFT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMinX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX)); /* BL CORNER */
				ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX)); /* BR CORNER */
				ao[TOP_RIGHT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX)); /* TR CORNER */

				int brightnessMixed_XYZNPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, offsetBrightness); /* TL CORNER */
				int brightnessMixed_XYZNNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, offsetBrightness); /* BL CORNER */
				int brightnessMixed_XYZPNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness); /* BR CORNER */
				int brightnessMixed_XYZPPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness); /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, renderBlocks.renderMaxY * renderBlocks.renderMinX); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, renderBlocks.renderMinY * renderBlocks.renderMinX); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, renderBlocks.renderMinY * renderBlocks.renderMaxX); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, renderBlocks.renderMaxY * renderBlocks.renderMaxX); /* TR CORNER */
			
			} else {

				ao[TOP_LEFT] = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F; /* TL CORNER */
				ao[BOTTOM_LEFT] = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F; /* BL CORNER */
				ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F; /* BR CORNER */
				ao[TOP_RIGHT] = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F; /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, offsetBrightness); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, offsetBrightness); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness); /* TR CORNER */

			}
		}
	}

	/**
	 * Fills AO variables with lightness for West face.
	 */
	public void setLightingXNeg(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMinX > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
			Tessellator.instance.setBrightness(brightness);

		} else {

			Tessellator.instance.setBrightness(NORMAL_BRIGHTNESS);
			boolean useOffsetLightness = renderBlocks.renderMinX >= 0.0D && renderBlocks.renderMinX <= 0.5D;
			boolean partialLighting = renderBlocks.partialRenderBounds;

			x -= useOffsetLightness ? 1 : 0;

			renderBlocks.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
			renderBlocks.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
			renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
			renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
			boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y + 1, z)];
			boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y - 1, z)];
			boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z - 1)];
			boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x - 1, y, z + 1)];

			if (!canBlockGrassYZNP && !canBlockGrassXYNN) {
				renderBlocks.aoLightValueScratchXYZNNN = renderBlocks.aoLightValueScratchXZNN;
				renderBlocks.aoBrightnessXYZNNN = renderBlocks.aoBrightnessXZNN;
			} else {
				renderBlocks.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1);
				renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
			}

			if (!canBlockGrassYZNN && !canBlockGrassXYNN) {
				renderBlocks.aoLightValueScratchXYZNNP = renderBlocks.aoLightValueScratchXZNP;
				renderBlocks.aoBrightnessXYZNNP = renderBlocks.aoBrightnessXZNP;
			} else {
				renderBlocks.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1);
				renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
			}

			if (!canBlockGrassYZNP && !canBlockGrassXYPN) {
				renderBlocks.aoLightValueScratchXYZNPN = renderBlocks.aoLightValueScratchXZNN;
				renderBlocks.aoBrightnessXYZNPN = renderBlocks.aoBrightnessXZNN;
			} else {
				renderBlocks.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1);
				renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
			}

			if (!canBlockGrassYZNN && !canBlockGrassXYPN) {
				renderBlocks.aoLightValueScratchXYZNPP = renderBlocks.aoLightValueScratchXZNP;
				renderBlocks.aoBrightnessXYZNPP = renderBlocks.aoBrightnessXZNP;
			} else {
				renderBlocks.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1);
				renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
			}

			x += useOffsetLightness ? 1 : 0;

			int offsetBrightness = mixedBrightness;

			if (renderBlocks.renderMinX <= 0.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x - 1, y, z)) {
				offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
			}

			float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);

			if (partialLighting) {

				float aoMixed_XYZNPN = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F; /* TL CORNER */
				float aoMixed_XYZNNN = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F; /* BL CORNER */
				float aoMixed_XYZNNP = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F; /* BR CORNER */
				float aoMixed_XYZNPP = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F; /* TR CORNER */

				ao[TOP_LEFT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * renderBlocks.renderMinZ + aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ); /* TL CORNER */
				ao[BOTTOM_LEFT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * renderBlocks.renderMinZ + aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ); /* BL CORNER */
				ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * renderBlocks.renderMaxZ + aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ); /* BR CORNER */
				ao[TOP_RIGHT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * renderBlocks.renderMaxZ + aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ); /* TR CORNER */

				int brightnessMixed_XYZNPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, offsetBrightness); /* TL CORNER */
				int brightnessMixed_XYZNNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, offsetBrightness); /* BL CORNER */
				int brightnessMixed_XYZNNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, offsetBrightness); /* BR CORNER */
				int brightnessMixed_XYZNPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, offsetBrightness); /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMaxY * renderBlocks.renderMinZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMinY * renderBlocks.renderMinZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMinY * renderBlocks.renderMaxZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMaxY * renderBlocks.renderMaxZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ); /* TR CORNER */

			} else {

				ao[TOP_LEFT] = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F; /* TL CORNER */
				ao[BOTTOM_LEFT] = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F; /* BL CORNER */
				ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F; /* BR CORNER */
				ao[TOP_RIGHT] = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F; /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, offsetBrightness); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, offsetBrightness); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, offsetBrightness); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, offsetBrightness); /* TR CORNER */

			}
		}
	}

	/**
	 * Fills AO variables with lightness for East face.
	 */
	public void setLightingXPos(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMaxX < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
			Tessellator.instance.setBrightness(brightness);

		} else {

			Tessellator.instance.setBrightness(NORMAL_BRIGHTNESS);
			boolean useOffsetLightness = renderBlocks.renderMaxX <= 1.0D && renderBlocks.renderMaxX > 0.5D;
			boolean partialLighting = renderBlocks.partialRenderBounds;

			x += useOffsetLightness ? 1 : 0;

			renderBlocks.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);
			renderBlocks.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);
			renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
			renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
			renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
			renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
			boolean canBlockGrassXYPN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y + 1, z)];
			boolean canBlockGrassXYNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y - 1, z)];
			boolean canBlockGrassYZNP = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z + 1)];
			boolean canBlockGrassYZNN = Block.canBlockGrass[renderBlocks.blockAccess.getBlockId(x + 1, y, z - 1)];

			if (!canBlockGrassXYNN && !canBlockGrassYZNN) {
				renderBlocks.aoLightValueScratchXYZPNN = renderBlocks.aoLightValueScratchXZPN;
				renderBlocks.aoBrightnessXYZPNN = renderBlocks.aoBrightnessXZPN;
			} else {
				renderBlocks.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1);
				renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
			}

			if (!canBlockGrassXYNN && !canBlockGrassYZNP) {
				renderBlocks.aoLightValueScratchXYZPNP = renderBlocks.aoLightValueScratchXZPP;
				renderBlocks.aoBrightnessXYZPNP = renderBlocks.aoBrightnessXZPP;
			} else {
				renderBlocks.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1);
				renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
			}

			if (!canBlockGrassXYPN && !canBlockGrassYZNN) {
				renderBlocks.aoLightValueScratchXYZPPN = renderBlocks.aoLightValueScratchXZPN;
				renderBlocks.aoBrightnessXYZPPN = renderBlocks.aoBrightnessXZPN;
			} else {
				renderBlocks.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1);
				renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
			}

			if (!canBlockGrassXYPN && !canBlockGrassYZNP) {
				renderBlocks.aoLightValueScratchXYZPPP = renderBlocks.aoLightValueScratchXZPP;
				renderBlocks.aoBrightnessXYZPPP = renderBlocks.aoBrightnessXZPP;
			} else {
				renderBlocks.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1);
				renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
			}

			x -= useOffsetLightness ? 1 : 0;

			int offsetBrightness = mixedBrightness;

			if (renderBlocks.renderMaxX >= 1.0D || !renderBlocks.blockAccess.isBlockOpaqueCube(x + 1, y, z)) {
				offsetBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
			}

			float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);

			if (partialLighting) {

				float aoMixed_XYZPPP = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F; /* TL CORNER */
				float aoMixed_XYZPNP = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F; /* BL CORNER */
				float aoMixed_XYZPNN = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F; /* BR CORNER */
				float aoMixed_XYZPPN = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F; /* TR CORNER */

				ao[TOP_LEFT] = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMaxZ); /* TL CORNER */
				ao[BOTTOM_LEFT] = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMaxZ); /* BL CORNER */
				ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMinZ); /* BR CORNER */
				ao[TOP_RIGHT] = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMinZ); /* TR CORNER */

				int brightnessMixed_XYZPPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness); /* TL CORNER */
				int brightnessMixed_XYZPNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness); /* BL CORNER */
				int brightnessMixed_XYZPNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, offsetBrightness); /* BR CORNER */
				int brightnessMixed_XYZPPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, offsetBrightness); /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * renderBlocks.renderMaxZ); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * renderBlocks.renderMaxZ); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * renderBlocks.renderMinZ); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * renderBlocks.renderMinZ); /* TR CORNER */

			} else {

				ao[TOP_LEFT] = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F; /* TL CORNER */
				ao[BOTTOM_LEFT] = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F; /* BL CORNER */
				ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F; /* BR CORNER */
				ao[TOP_RIGHT] = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F; /* TR CORNER */

				renderBlocks.brightnessTopLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, offsetBrightness); /* TL CORNER */
				renderBlocks.brightnessBottomLeft = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, offsetBrightness); /* BL CORNER */
				renderBlocks.brightnessBottomRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, offsetBrightness); /* BR CORNER */
				renderBlocks.brightnessTopRight = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, offsetBrightness); /* TR CORNER */

			}
		}
	}

}
