package carpentersblocks.renderer.helper;

import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.BlockHandlerBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.OptifineHandler;
import carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightingHelper {

    public static final LightingHelper instance = new LightingHelper();

    private Tessellator      tessellator = Tessellator.instance;
    private BlockHandlerBase blockHandler;
    private RenderBlocks     renderBlocks;
    private float            lightness;
    private float            lightnessOffset;
    private boolean          hasBrightnessOverride;
    private boolean          hasLightnessOverride;
    private int              brightnessOverride;
    private float            lightnessOverride;
    private boolean          hasColorOverride;
    private float[]          colorOverride     = new float[3];

    public final int         NORMAL_BRIGHTNESS = 0xff00ff;
    public final int         MAX_BRIGHTNESS    = 0xf000f0;

    public final float       LIGHTNESS_YN      = 0.5F;
    public final float       LIGHTNESS_YP      = 1.0F;
    public final float       LIGHTNESS_ZN      = 0.8F;
    public final float       LIGHTNESS_ZP      = 0.8F;
    public final float       LIGHTNESS_XN      = 0.6F;
    public final float       LIGHTNESS_XP      = 0.6F;

    private final int        RED               = 0;
    private final int        GREEN             = 1;
    private final int        BLUE              = 2;

    private double           lighting_offset   = 0.5D;

    public LightingHelper bind(BlockHandlerBase blockHandler)
    {
        this.blockHandler = blockHandler;
        renderBlocks = blockHandler.renderBlocks;
        return this;
    }

    /**
     * Sets lightness.  Can be applied at any time before actually
     * rendering the side, but must happen after setting side
     * lighting.  It takes effect when
     * BlockHandlerBase.delegateSideRender() is called.
     */
    public LightingHelper setLightness(float lightness)
    {
        this.lightness = lightness;
        return this;
    }

    /**
     * Sets lightness override.
     */
    public LightingHelper setLightnessOverride(float lightness)
    {
        hasLightnessOverride = true;
        lightnessOverride = lightness;
        return this;
    }

    /**
     * Clears lightness override.
     */
    public void clearLightnessOverride()
    {
        hasLightnessOverride = false;
    }

    /**
     * Returns RenderBlocks instance.
     */
    public RenderBlocks getRenderBlocks()
    {
        return renderBlocks;
    }

    /**
     * Sets color override.
     */
    public void setColorOverride(float[] rgb)
    {
        hasColorOverride = true;
        colorOverride = rgb;
    }

    /**
     * Clears color override.
     */
    public void clearColorOverride()
    {
        hasColorOverride = false;
    }

    /**
     * Sets brightness override.
     */
    public LightingHelper setBrightnessOverride(int brightness)
    {
        hasBrightnessOverride = true;
        brightnessOverride = brightness;
        return this;
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
     * Clears lightness offset.
     */
    public void clearLightnessOffset()
    {
        lightnessOffset = 0.0F;
    }

    /**
     * Stores uncolored, ambient occlusion values for each corner of every face.
     */
    public float[] ao = new float[4];

    /**
     * Returns float array with RGB values for block.
     * If using our custom render helpers, be sure to apply anaglyph filter
     * before rendering.
     */
    public float[] getBlockRGB(Block block, int x, int y, int z)
    {
        int color = FeatureRegistry.enableOptifineIntegration ? OptifineHandler.getColorMultiplier(block, renderBlocks.blockAccess, x, y, z) : block.colorMultiplier(renderBlocks.blockAccess, x, y, z);
        float[] rgb = { (color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F };

        return rgb;
    }

    /**
     * If anaglyph is enabled, will apply a color filter to the RGB before returning it.
     */
    public float[] applyAnaglyphFilter(float[] rgb)
    {
        if (EntityRenderer.anaglyphEnable)
        {
            rgb[RED]   = (rgb[RED] * 30.0F + rgb[GREEN] * 59.0F + rgb[BLUE] * 11.0F) / 100.0F;
            rgb[GREEN] = (rgb[RED] * 30.0F + rgb[GREEN] * 70.0F) / 100.0F;
            rgb[BLUE]  = (rgb[RED] * 30.0F + rgb[BLUE] * 70.0F) / 100.0F;
        }

        return rgb;
    }

    /**
     * Apply lightness and color to AO or tessellator.
     */
    public void colorSide(Block block, int x, int y, int z, int side, Icon icon)
    {
        float[] dyeRGB = { 1.0F, 1.0F, 1.0F };

        if (!blockHandler.suppressDyeColor && !(block.equals(Block.grass) && side == 1)) {
            if (BlockProperties.hasDyeColor(blockHandler.TE, blockHandler.coverRendering) || blockHandler.hasDyeColorOverride) {
                int dyeColor = blockHandler.hasDyeColorOverride ? blockHandler.dyeColorOverride : BlockProperties.getDyeColor(blockHandler.TE, blockHandler.coverRendering);
                dyeRGB = BlockProperties.getDyeRGB(dyeColor);
            }
        }

        float[] blockRGB = getBlockRGB(block, x, y, z);

        /* If block is grass, we have to apply color selectively. */

        if (block.equals(Block.grass)) {

            boolean posSlopedSide = blockHandler.isSideSloped ? Slope.slopesList[BlockProperties.getData(blockHandler.TE)].isPositive : false;
            boolean useGrassColor = block.equals(Block.grass) && (side == 1 || icon.equals(BlockGrass.getIconSideOverlay()) || posSlopedSide);

            if (!useGrassColor) {
                blockRGB[RED] = blockRGB[GREEN] = blockRGB[BLUE] = 1.0F;
            }

        }

        /* Apply color. */

        float[] finalRGB = { (blockRGB[RED] += lightnessOffset) * dyeRGB[RED], (blockRGB[GREEN] += lightnessOffset) * dyeRGB[GREEN], (blockRGB[BLUE] += lightnessOffset) * dyeRGB[BLUE] };
        finalRGB = applyAnaglyphFilter(finalRGB);

        float lightness = hasLightnessOverride ? lightnessOverride : this.lightness;

        if (hasColorOverride) {
            finalRGB = colorOverride;
        }

        if (renderBlocks.enableAO) {

            if (renderBlocks.hasOverrideBlockTexture()) {

                renderBlocks.colorRedTopLeft   = renderBlocks.colorRedBottomLeft   = renderBlocks.colorRedBottomRight   = renderBlocks.colorRedTopRight   = blockRGB[0];
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = blockRGB[1];
                renderBlocks.colorBlueTopLeft  = renderBlocks.colorBlueBottomLeft  = renderBlocks.colorBlueBottomRight  = renderBlocks.colorBlueTopRight  = blockRGB[2];

            } else {

                renderBlocks.colorRedTopLeft   = renderBlocks.colorRedBottomLeft   = renderBlocks.colorRedBottomRight   = renderBlocks.colorRedTopRight   = finalRGB[RED] * lightness;
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = finalRGB[GREEN] * lightness;
                renderBlocks.colorBlueTopLeft  = renderBlocks.colorBlueBottomLeft  = renderBlocks.colorBlueBottomRight  = renderBlocks.colorBlueTopRight  = finalRGB[BLUE] * lightness;

                renderBlocks.colorRedTopLeft       *= ao[TOP_LEFT];
                renderBlocks.colorGreenTopLeft     *= ao[TOP_LEFT];
                renderBlocks.colorBlueTopLeft      *= ao[TOP_LEFT];
                renderBlocks.colorRedBottomLeft    *= ao[BOTTOM_LEFT];
                renderBlocks.colorGreenBottomLeft  *= ao[BOTTOM_LEFT];
                renderBlocks.colorBlueBottomLeft   *= ao[BOTTOM_LEFT];
                renderBlocks.colorRedBottomRight   *= ao[BOTTOM_RIGHT];
                renderBlocks.colorGreenBottomRight *= ao[BOTTOM_RIGHT];
                renderBlocks.colorBlueBottomRight  *= ao[BOTTOM_RIGHT];
                renderBlocks.colorRedTopRight      *= ao[TOP_RIGHT];
                renderBlocks.colorGreenTopRight    *= ao[TOP_RIGHT];
                renderBlocks.colorBlueTopRight     *= ao[TOP_RIGHT];
            }

        } else {

            tessellator.setColorOpaque_F(finalRGB[RED] * lightness, finalRGB[GREEN] * lightness, finalRGB[BLUE] * lightness);

        }
    }

    /**
     * Fills AO variables with lightness for bottom face.
     */
    public LightingHelper setLightingYNeg(Block block, int x, int y, int z)
    {
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
        lightness = LIGHTNESS_YN;

        if (!renderBlocks.enableAO) {

            int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMinY > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            tessellator.setBrightness(brightness);

        } else {

            int brightness = hasBrightnessOverride ? brightnessOverride : NORMAL_BRIGHTNESS;
            tessellator.setBrightness(brightness);

            if (renderBlocks.renderMinY <= lighting_offset) {
                --y;
            }

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

            if (renderBlocks.renderMinY <= lighting_offset) {
                ++y;
            }

            if (renderBlocks.renderMinY <= lighting_offset || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y - 1, z)) {
                mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            }

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z);

            int brightnessMixed_XYZPNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXYPN, mixedBrightness);
            int brightnessMixed_XYZPNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNN, mixedBrightness);
            int brightnessMixed_XYZNNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessYZNN, mixedBrightness);
            int brightnessMixed_XYZNNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessYZNP, mixedBrightness);

            if (renderBlocks.partialRenderBounds) {

                float aoMixed_XYZPNP = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
                float aoMixed_XYZPNN = (aoLightValue + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
                float aoMixed_XYZNNN = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoLightValue + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
                float aoMixed_XYZNNP = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;

                ao[TOP_LEFT     /*SE*/] = (float)(aoMixed_XYZNNP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPNP * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                ao[BOTTOM_LEFT  /*NE*/] = (float)(aoMixed_XYZNNP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPNP * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                ao[BOTTOM_RIGHT /*NW*/] = (float)(aoMixed_XYZNNP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPNP * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
                ao[TOP_RIGHT    /*SW*/] = (float)(aoMixed_XYZNNP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPNP * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));

                renderBlocks.brightnessTopLeft     = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomLeft  = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
                renderBlocks.brightnessTopRight    = renderBlocks.mixAoBrightness(brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));

            } else {

                ao[TOP_LEFT     /*SE*/] = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXYPN) / 4.0F;
                ao[BOTTOM_LEFT  /*NE*/] = (aoLightValue + renderBlocks.aoLightValueScratchYZNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNN) / 4.0F;
                ao[BOTTOM_RIGHT /*NW*/] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNN + aoLightValue + renderBlocks.aoLightValueScratchYZNN) / 4.0F;
                ao[TOP_RIGHT    /*SW*/] = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;

                renderBlocks.brightnessTopLeft     = brightnessMixed_XYZPNP;
                renderBlocks.brightnessBottomLeft  = brightnessMixed_XYZPNN;
                renderBlocks.brightnessBottomRight = brightnessMixed_XYZNNN;
                renderBlocks.brightnessTopRight    = brightnessMixed_XYZNNP;

            }
        }

        return this;
    }

    /**
     * Fills AO variables with lightness for top face.
     */
    public LightingHelper setLightingYPos(Block block, int x, int y, int z)
    {
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
        lightness = LIGHTNESS_YP;

        if (!renderBlocks.enableAO) {

            int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMaxY < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            tessellator.setBrightness(brightness);

        } else {

            int brightness = hasBrightnessOverride ? brightnessOverride : NORMAL_BRIGHTNESS;
            tessellator.setBrightness(brightness);

            if (renderBlocks.renderMaxY >= lighting_offset) {
                ++y;
            }

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

            if (renderBlocks.renderMaxY >= lighting_offset) {
                --y;
            }

            if (renderBlocks.renderMaxY >= lighting_offset || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y + 1, z)) {
                mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            }

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z);

            int brightnessMixed_XYZPPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXYZPPP, renderBlocks.aoBrightnessXYPP, mixedBrightness);
            int brightnessMixed_XYZPPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPN, mixedBrightness);
            int brightnessMixed_XYZNPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, mixedBrightness);
            int brightnessMixed_XYZNPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessYZPP, mixedBrightness);

            if (renderBlocks.partialRenderBounds) {

                float aoMixed_XYZPPP = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
                float aoMixed_XYZPPN = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
                float aoMixed_XYZNPN = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
                float aoMixed_XYZNPP = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F;

                ao[TOP_LEFT     /*SE*/] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMaxZ * renderBlocks.renderMaxX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                ao[BOTTOM_LEFT  /*NE*/] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMinZ * renderBlocks.renderMaxX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                ao[BOTTOM_RIGHT /*NW*/] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMinZ * renderBlocks.renderMinX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
                ao[TOP_RIGHT    /*SW*/] = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMaxZ * renderBlocks.renderMinX + aoMixed_XYZPPN * (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX + aoMixed_XYZNPN * (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));

                renderBlocks.brightnessTopLeft     = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomLeft  = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinZ * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, renderBlocks.renderMinZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinZ) * (1.0D - renderBlocks.renderMinX));
                renderBlocks.brightnessTopRight    = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZPPP, brightnessMixed_XYZPPN, brightnessMixed_XYZNPN, renderBlocks.renderMaxZ * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxZ * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxZ) * (1.0D - renderBlocks.renderMinX));

            } else {

                ao[TOP_LEFT     /*SE*/] = (renderBlocks.aoLightValueScratchYZPP + aoLightValue + renderBlocks.aoLightValueScratchXYZPPP + renderBlocks.aoLightValueScratchXYPP) / 4.0F;
                ao[BOTTOM_LEFT  /*NE*/] = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
                ao[BOTTOM_RIGHT /*NW*/] = (renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;
                ao[TOP_RIGHT    /*SW*/] = (renderBlocks.aoLightValueScratchXYZNPP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchYZPP + aoLightValue) / 4.0F;

                renderBlocks.brightnessTopLeft     = brightnessMixed_XYZPPP;
                renderBlocks.brightnessBottomLeft  = brightnessMixed_XYZPPN;
                renderBlocks.brightnessBottomRight = brightnessMixed_XYZNPN;
                renderBlocks.brightnessTopRight    = brightnessMixed_XYZNPP;

            }
        }

        return this;
    }

    /**
     * Fills AO variables with lightness for North face.
     */
    public LightingHelper setLightingZNeg(Block block, int x, int y, int z)
    {
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
        lightness = LIGHTNESS_ZN;

        if (!renderBlocks.enableAO) {

            int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMinZ > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            tessellator.setBrightness(brightness);

        } else {

            int brightness = hasBrightnessOverride ? brightnessOverride : NORMAL_BRIGHTNESS;
            tessellator.setBrightness(brightness);

            if (renderBlocks.renderMinZ < lighting_offset) {
                --z;
            }

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

            if (renderBlocks.renderMinZ < lighting_offset) {
                ++z;
            }

            if (renderBlocks.renderMinZ < lighting_offset || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z - 1)) {
                mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            }

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1);

            int brightnessMixed_XYZPPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPN, renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, mixedBrightness);
            int brightnessMixed_XYZPNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNN, renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXZPN, mixedBrightness);
            int brightnessMixed_XYZNNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessYZNN, mixedBrightness);
            int brightnessMixed_XYZNPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessYZPN, mixedBrightness);

            if (renderBlocks.partialRenderBounds) {

                float aoMixed_XYZPPN = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
                float aoMixed_XYZPNN = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
                float aoMixed_XYZNNN = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F;
                float aoMixed_XYZNPN = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;

                ao[TOP_LEFT]     = (float)(aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPN * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
                ao[BOTTOM_LEFT]  = (float)(aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPN * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
                ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPN * renderBlocks.renderMinY * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
                ao[TOP_RIGHT]    = (float)(aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPN * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));

                renderBlocks.brightnessTopLeft     = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMaxY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomLeft  = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), renderBlocks.renderMinY * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMinY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
                renderBlocks.brightnessTopRight    = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPN, brightnessMixed_XYZPPN, brightnessMixed_XYZPNN, brightnessMixed_XYZNNN, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), renderBlocks.renderMaxY * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));

            } else {

                ao[TOP_LEFT]     = (aoLightValue + renderBlocks.aoLightValueScratchYZPN + renderBlocks.aoLightValueScratchXZPN + renderBlocks.aoLightValueScratchXYZPPN) / 4.0F;
                ao[BOTTOM_LEFT]  = (renderBlocks.aoLightValueScratchYZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXZPN) / 4.0F;
                ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchYZNN + aoLightValue) / 4.0F;
                ao[TOP_RIGHT]    = (renderBlocks.aoLightValueScratchXZNN + renderBlocks.aoLightValueScratchXYZNPN + aoLightValue + renderBlocks.aoLightValueScratchYZPN) / 4.0F;

                renderBlocks.brightnessTopLeft     = brightnessMixed_XYZPPN;
                renderBlocks.brightnessBottomLeft  = brightnessMixed_XYZPNN;
                renderBlocks.brightnessBottomRight = brightnessMixed_XYZNNN;
                renderBlocks.brightnessTopRight    = brightnessMixed_XYZNPN;

            }
        }

        return this;
    }

    /**
     * Fills AO variables with lightness for South face.
     */
    public LightingHelper setLightingZPos(Block block, int x, int y, int z)
    {
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
        lightness = LIGHTNESS_ZP;

        if (!renderBlocks.enableAO) {

            int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMaxZ < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            tessellator.setBrightness(brightness);

        } else {

            int brightness = hasBrightnessOverride ? brightnessOverride : NORMAL_BRIGHTNESS;
            tessellator.setBrightness(brightness);

            if (renderBlocks.renderMaxZ > lighting_offset) {
                ++z;
            }

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

            if (renderBlocks.renderMaxZ > lighting_offset) {
                --z;
            }

            if (renderBlocks.renderMaxZ > lighting_offset || !renderBlocks.blockAccess.isBlockOpaqueCube(x, y, z + 1)) {
                mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            }

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1);

            int brightnessMixed_XYZNPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYZNPP, renderBlocks.aoBrightnessYZPP, mixedBrightness);
            int brightnessMixed_XYZNNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessYZNP, mixedBrightness);
            int brightnessMixed_XYZPNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZNP, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, mixedBrightness);
            int brightnessMixed_XYZPPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessYZPP, renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYZPPP, mixedBrightness);

            if (renderBlocks.partialRenderBounds) {

                float aoMixed_XYZNPP = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
                float aoMixed_XYZNNP = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
                float aoMixed_XYZPNP = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
                float aoMixed_XYZPPP = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;

                ao[TOP_LEFT]     = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMinX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX));
                ao[BOTTOM_LEFT]  = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMinX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX));
                ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMaxX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX));
                ao[TOP_RIGHT]    = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMaxX + aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX));

                renderBlocks.brightnessTopLeft     = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinX, renderBlocks.renderMaxY * renderBlocks.renderMinX);
                renderBlocks.brightnessBottomLeft  = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinX, renderBlocks.renderMinY * renderBlocks.renderMinX);
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxX, renderBlocks.renderMinY * renderBlocks.renderMaxX);
                renderBlocks.brightnessTopRight    = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNNP, brightnessMixed_XYZPNP, brightnessMixed_XYZPPP, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxX), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxX, renderBlocks.renderMaxY * renderBlocks.renderMaxX);

            } else {

                ao[TOP_LEFT]     = (renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYZNPP + aoLightValue + renderBlocks.aoLightValueScratchYZPP) / 4.0F;
                ao[BOTTOM_LEFT]  = (renderBlocks.aoLightValueScratchXYZNNP + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
                ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchYZNP + aoLightValue + renderBlocks.aoLightValueScratchXYZPNP + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
                ao[TOP_RIGHT]    = (aoLightValue + renderBlocks.aoLightValueScratchYZPP + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;

                renderBlocks.brightnessTopLeft     = brightnessMixed_XYZNPP;
                renderBlocks.brightnessBottomLeft  = brightnessMixed_XYZNNP;
                renderBlocks.brightnessBottomRight = brightnessMixed_XYZPNP;
                renderBlocks.brightnessTopRight    = brightnessMixed_XYZPPP;

            }
        }

        return this;
    }

    /**
     * Fills AO variables with lightness for West face.
     */
    public LightingHelper setLightingXNeg(Block block, int x, int y, int z)
    {
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
        lightness = LIGHTNESS_XN;

        if (!renderBlocks.enableAO) {

            int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMinX > 0.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            tessellator.setBrightness(brightness);

        } else {

            int brightness = hasBrightnessOverride ? brightnessOverride : NORMAL_BRIGHTNESS;
            tessellator.setBrightness(brightness);

            if (renderBlocks.renderMinX < lighting_offset) {
                --x;
            }

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

            if (renderBlocks.renderMinX < lighting_offset) {
                ++x;
            }

            if (renderBlocks.renderMinX < lighting_offset || !renderBlocks.blockAccess.isBlockOpaqueCube(x - 1, y, z)) {
                mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            }

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z);

            int brightnessMixed_XYZNPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNN, renderBlocks.aoBrightnessXYZNPN, renderBlocks.aoBrightnessXYNP, mixedBrightness);
            int brightnessMixed_XYZNNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZNNN, renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXZNN, mixedBrightness);
            int brightnessMixed_XYZNNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYNN, renderBlocks.aoBrightnessXYZNNP, renderBlocks.aoBrightnessXZNP, mixedBrightness);
            int brightnessMixed_XYZNPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZNP, renderBlocks.aoBrightnessXYNP, renderBlocks.aoBrightnessXYZNPP, mixedBrightness);

            if (renderBlocks.partialRenderBounds) {

                float aoMixed_XYZNPN = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
                float aoMixed_XYZNNN = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F;
                float aoMixed_XYZNNP = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
                float aoMixed_XYZNPP = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;

                ao[TOP_LEFT]     = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * renderBlocks.renderMinZ + aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
                ao[BOTTOM_LEFT]  = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * renderBlocks.renderMinZ + aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
                ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZNPP * renderBlocks.renderMinY * renderBlocks.renderMaxZ + aoMixed_XYZNPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);
                ao[TOP_RIGHT]    = (float)(aoMixed_XYZNPP * renderBlocks.renderMaxY * renderBlocks.renderMaxZ + aoMixed_XYZNPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZNNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);

                renderBlocks.brightnessTopLeft     = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMaxY * renderBlocks.renderMinZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ);
                renderBlocks.brightnessBottomLeft  = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMinY * renderBlocks.renderMinZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ);
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMinY * renderBlocks.renderMaxZ, renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ);
                renderBlocks.brightnessTopRight    = renderBlocks.mixAoBrightness(brightnessMixed_XYZNPP, brightnessMixed_XYZNPN, brightnessMixed_XYZNNN, brightnessMixed_XYZNNP, renderBlocks.renderMaxY * renderBlocks.renderMaxZ, renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ);

            } else {

                ao[TOP_LEFT]     = (renderBlocks.aoLightValueScratchXZNN + aoLightValue + renderBlocks.aoLightValueScratchXYZNPN + renderBlocks.aoLightValueScratchXYNP) / 4.0F;
                ao[BOTTOM_LEFT]  = (renderBlocks.aoLightValueScratchXYZNNN + renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXZNN + aoLightValue) / 4.0F;
                ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchXYNN + renderBlocks.aoLightValueScratchXYZNNP + aoLightValue + renderBlocks.aoLightValueScratchXZNP) / 4.0F;
                ao[TOP_RIGHT]    = (aoLightValue + renderBlocks.aoLightValueScratchXZNP + renderBlocks.aoLightValueScratchXYNP + renderBlocks.aoLightValueScratchXYZNPP) / 4.0F;

                renderBlocks.brightnessTopLeft     = brightnessMixed_XYZNPN;
                renderBlocks.brightnessBottomLeft  = brightnessMixed_XYZNNN;
                renderBlocks.brightnessBottomRight = brightnessMixed_XYZNNP;
                renderBlocks.brightnessTopRight    = brightnessMixed_XYZNPP;

            }
        }

        return this;
    }

    /**
     * Fills AO variables with lightness for East face.
     */
    public LightingHelper setLightingXPos(Block block, int x, int y, int z)
    {
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z);
        lightness = LIGHTNESS_XP;

        if (!renderBlocks.enableAO) {

            int brightness = hasBrightnessOverride ? brightnessOverride : renderBlocks.renderMaxX < 1.0D ? mixedBrightness : block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            tessellator.setBrightness(brightness);

        } else {

            int brightness = hasBrightnessOverride ? brightnessOverride : NORMAL_BRIGHTNESS;
            tessellator.setBrightness(brightness);

            if (renderBlocks.renderMaxX > lighting_offset) {
                ++x;
            }

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

            if (renderBlocks.renderMaxX > lighting_offset) {
                --x;
            }

            if (renderBlocks.renderMaxX > lighting_offset || !renderBlocks.blockAccess.isBlockOpaqueCube(x + 1, y, z)) {
                mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            }

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z);

            int brightnessMixed_XYZPPP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPP, renderBlocks.aoBrightnessXYPP, renderBlocks.aoBrightnessXYZPPP, mixedBrightness);
            int brightnessMixed_XYZPNP = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXYZPNP, renderBlocks.aoBrightnessXZPP, mixedBrightness);
            int brightnessMixed_XYZPNN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXYZPNN, renderBlocks.aoBrightnessXYPN, renderBlocks.aoBrightnessXZPN, mixedBrightness);
            int brightnessMixed_XYZPPN = renderBlocks.getAoBrightness(renderBlocks.aoBrightnessXZPN, renderBlocks.aoBrightnessXYZPPN, renderBlocks.aoBrightnessXYPP, mixedBrightness);

            if (renderBlocks.partialRenderBounds) {

                float aoMixed_XYZPPP = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
                float aoMixed_XYZPNP = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
                float aoMixed_XYZPNN = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F;
                float aoMixed_XYZPPN = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F;

                ao[TOP_LEFT]     = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMaxZ);
                ao[BOTTOM_LEFT]  = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMaxZ);
                ao[BOTTOM_RIGHT] = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPN * renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPP * renderBlocks.renderMinY * renderBlocks.renderMinZ);
                ao[TOP_RIGHT]    = (float)(aoMixed_XYZPNP * (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ + aoMixed_XYZPNN * (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPN * renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ) + aoMixed_XYZPPP * renderBlocks.renderMaxY * renderBlocks.renderMinZ);

                renderBlocks.brightnessTopLeft     = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMaxY * renderBlocks.renderMaxZ);
                renderBlocks.brightnessBottomLeft  = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMaxZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMaxZ), renderBlocks.renderMinY * renderBlocks.renderMaxZ);
                renderBlocks.brightnessBottomRight = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMinY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMinY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMinY * renderBlocks.renderMinZ);
                renderBlocks.brightnessTopRight    = renderBlocks.mixAoBrightness(brightnessMixed_XYZPNP, brightnessMixed_XYZPNN, brightnessMixed_XYZPPN, brightnessMixed_XYZPPP, (1.0D - renderBlocks.renderMaxY) * renderBlocks.renderMinZ, (1.0D - renderBlocks.renderMaxY) * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * (1.0D - renderBlocks.renderMinZ), renderBlocks.renderMaxY * renderBlocks.renderMinZ);

            } else {

                ao[TOP_LEFT]     = (aoLightValue + renderBlocks.aoLightValueScratchXZPP + renderBlocks.aoLightValueScratchXYPP + renderBlocks.aoLightValueScratchXYZPPP) / 4.0F;
                ao[BOTTOM_LEFT]  = (renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXYZPNP + aoLightValue + renderBlocks.aoLightValueScratchXZPP) / 4.0F;
                ao[BOTTOM_RIGHT] = (renderBlocks.aoLightValueScratchXYZPNN + renderBlocks.aoLightValueScratchXYPN + renderBlocks.aoLightValueScratchXZPN + aoLightValue) / 4.0F;
                ao[TOP_RIGHT]    = (renderBlocks.aoLightValueScratchXZPN + aoLightValue + renderBlocks.aoLightValueScratchXYZPPN + renderBlocks.aoLightValueScratchXYPP) / 4.0F;

                renderBlocks.brightnessTopLeft     = brightnessMixed_XYZPPP;
                renderBlocks.brightnessBottomLeft  = brightnessMixed_XYZPNP;
                renderBlocks.brightnessBottomRight = brightnessMixed_XYZPNN;
                renderBlocks.brightnessTopRight    = brightnessMixed_XYZPPN;

            }
        }

        return this;
    }

}
