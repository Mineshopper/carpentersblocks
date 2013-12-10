package carpentersblocks.renderer.helper;

import carpentersblocks.data.Slope;
import carpentersblocks.renderer.BlockHandlerBase;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.DyeColorHandler;
import carpentersblocks.util.handler.OptifineHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.FeatureRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class LightingHelper {

	private final BlockHandlerBase	BH;
	private final TEBase 			TE;
	private final RenderBlocks 		renderBlocks;
	private final Block 			srcBlock;
	
	private float					lightness;
	public final int				NORMAL_BRIGHTNESS = 983055;
	public final int				MAX_BRIGHTNESS = 15728880;
	
	private boolean 				hasLightnessOffset;
	private float 					lightnessOffset;
	private boolean					hasBrightnessOverride;
	private int						brightnessOverride;
	private boolean					hasColorOverride;
	private float[]					colorOverride = new float[3];
	
	public LightingHelper(BlockHandlerBase BH, TEBase TE, RenderBlocks renderBlocks)
	{
		this.BH = BH;
		this.TE = TE;
		this.renderBlocks = renderBlocks;
		this.srcBlock = BH.srcBlock;
	}
	
	/**
	 * Sets lightness.
	 */
	public LightingHelper setLightness(float lightness)
	{
		this.lightness = lightness;

		if (!renderBlocks.enableAO)
		{
			base_RGB[0] = lightness;
			base_RGB[1] = lightness;
			base_RGB[2] = lightness;
		}
		
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
		hasLightnessOffset = true;
		lightnessOffset = lightness;
	}

	/**
	 * Clears lightness override.
	 */
	public void clearLightnessOffset()
	{
		hasLightnessOffset = false;
	}

	/**
	 * Stores uncolored, ambient occlusion values for each corner of face.
	 */
	public float[] base_ao = { 0.0F, 0.0F, 0.0F, 0.0F };

	/**
	 * Stores uncolored light value for face.
	 */
	public float[] base_RGB = { 0.0F, 0.0F, 0.0F };
	
	/**
	 * Multiplies AO by a color.
	 */
	private void aoMultiplyByColor(float red, float green, float blue)
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
	private void aoSetColor(float red, float green, float blue, float lightness)
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
	private void aoResetColor()
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
	 * Apply color to AO or tessellator.
	 */
	public void colorSide(Block block, int x, int y, int z, int side, Icon icon)
	{
		Tessellator tessellator = Tessellator.instance;
		float baseRGB[] = getRGB(block, x, y, z);
		float dyeRGB[] = { 1.0F, 1.0F, 1.0F };

		if (hasColorOverride) {
			baseRGB[0] = colorOverride[0];
			baseRGB[1] = colorOverride[1];
			baseRGB[2] = colorOverride[2];
		}
		
		if (!BH.suppressDyeColor) {
			dyeRGB = DyeColorHandler.getDyeColorRGB(BH.hasDyeColorOverride ? BH.dyeColorOverride : BlockProperties.getDyeColor(TE, BH.coverRendering));
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

			aoResetColor();
			if (hasColorOverride) {
				aoSetColor(colorOverride[0], colorOverride[1], colorOverride[2], 1.0F);
			} else if (useColorComponent(block, side, icon)) {
				aoSetColor(baseRGB[0] * dyeRGB[0], baseRGB[1] * dyeRGB[1], baseRGB[2] * dyeRGB[2], lightness);
			} else {
				aoSetColor(dyeRGB[0], dyeRGB[1], dyeRGB[2], lightness);
			}

		} else {

			if (hasColorOverride) {
				tessellator.setColorOpaque_F(colorOverride[0], colorOverride[1], colorOverride[2]);
			} else if (useColorComponent(block, side, icon)) {
				tessellator.setColorOpaque_F(base_RGB[0] * baseRGB[0] * dyeRGB[0], base_RGB[1] * baseRGB[1] * dyeRGB[1], base_RGB[2] * baseRGB[2] * dyeRGB[2]);
			} else {
				tessellator.setColorOpaque_F(base_RGB[0] * dyeRGB[0], base_RGB[1] * dyeRGB[1], base_RGB[2] * dyeRGB[2]);
			}

		}
	}

	/**
	 * Returns whether block side color component should be used.
	 */
	protected boolean useColorComponent(Block block, int side, Icon icon)
	{
		if (block == Block.grass || BlockProperties.getOverlay(TE, side) == OverlayHandler.OVERLAY_GRASS)
		{
			boolean posSlopedSide = BH.isSideSloped ? Slope.slopesList[BlockProperties.getData(TE)].isPositive : false;
			return side == 1 || icon == BlockGrass.getIconSideOverlay() || posSlopedSide;
		}

		return false;
	}
	
	/**
	 * Fills AO variables with lightness for bottom face.
	 */
	public void setLightingYNeg(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {
			
			int brightness = hasBrightnessOverride ? brightnessOverride : (renderBlocks.renderMinY > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z));
			Tessellator.instance.setBrightness(brightness);
			
		} else {
			
			Tessellator.instance.setBrightness(983055);	
			boolean useOffsetLightness = renderBlocks.renderMinY >= 0.0D && renderBlocks.renderMinY <= 0.5D;
			boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

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

			float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);

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
	}

	/**
	 * Fills AO variables with lightness for top face.
	 */
	public void setLightingYPos(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
		
		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : (renderBlocks.renderMaxY < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z));
			Tessellator.instance.setBrightness(brightness);
			
		} else {

			Tessellator.instance.setBrightness(983055);	
			boolean useOffsetLightness = renderBlocks.renderMaxY <= 1.0D && renderBlocks.renderMaxY > 0.5D;
			boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

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
	}

	/**
	 * Fills AO variables with lightness for North face.
	 */
	public void setLightingZNeg(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : (renderBlocks.renderMinZ > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1));
			Tessellator.instance.setBrightness(brightness);
			
		} else {

			Tessellator.instance.setBrightness(983055);	
			boolean useOffsetLightness = renderBlocks.renderMinZ >= 0.0D && renderBlocks.renderMinZ <= 0.5D;
			boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

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
	}

	/**
	 * Fills AO variables with lightness for South face.
	 */
	public void setLightingZPos(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : (renderBlocks.renderMaxZ < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1));
			Tessellator.instance.setBrightness(brightness);
			
		} else {

			Tessellator.instance.setBrightness(983055);	
			boolean useOffsetLightness = renderBlocks.renderMaxZ <= 1.0D && renderBlocks.renderMaxZ > 0.5D;
			boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

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
	}

	/**
	 * Fills AO variables with lightness for West face.
	 */
	public void setLightingXNeg(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : (renderBlocks.renderMinX > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z));
			Tessellator.instance.setBrightness(brightness);
			
		} else {

			Tessellator.instance.setBrightness(983055);	
			boolean useOffsetLightness = renderBlocks.renderMinX >= 0.0D && renderBlocks.renderMinX <= 0.5D;
			boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

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
	}

	/**
	 * Fills AO variables with lightness for East face.
	 */
	public void setLightingXPos(Block block, int x, int y, int z)
	{
		int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);

		if (!renderBlocks.enableAO) {

			int brightness = hasBrightnessOverride ? brightnessOverride : (renderBlocks.renderMaxX < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z));
			Tessellator.instance.setBrightness(brightness);
			
		} else {

			Tessellator.instance.setBrightness(983055);	
			boolean useOffsetLightness = renderBlocks.renderMaxX <= 1.0D && renderBlocks.renderMaxX > 0.5D;
			boolean maxSmoothLighting = renderBlocks.partialRenderBounds;

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
	}
	
}
