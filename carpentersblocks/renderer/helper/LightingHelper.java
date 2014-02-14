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

    private boolean          hasLightnessOverride;
    private float            lightnessOverride;

    private boolean          hasBrightnessOverride;
    private int              brightnessOverride;

    private boolean          hasColorOverride;
    private float[]          colorOverride      = new float[3];

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

    public LightingHelper bind(BlockHandlerBase blockHandler)
    {
        this.blockHandler = blockHandler;
        renderBlocks = blockHandler.renderBlocks;
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
            rgb[BLUE]  = (rgb[RED] * 30.0F + rgb[BLUE]  * 70.0F) / 100.0F;
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

        float[] finalRGB = { blockRGB[RED] * dyeRGB[RED], blockRGB[GREEN] * dyeRGB[GREEN], blockRGB[BLUE] * dyeRGB[BLUE] };
        finalRGB = applyAnaglyphFilter(finalRGB);

        if (hasLightnessOverride) {
            lightness = lightnessOverride;
        }

        if (hasBrightnessOverride) {
            tessellator.setBrightness(brightnessOverride);
        }

        if (hasColorOverride) {
            finalRGB = colorOverride;
        }

        if (renderBlocks.enableAO) {

            if (renderBlocks.hasOverrideBlockTexture()) {

                renderBlocks.colorRedTopLeft   = renderBlocks.colorRedBottomLeft   = renderBlocks.colorRedBottomRight   = renderBlocks.colorRedTopRight   = blockRGB[0];
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = blockRGB[1];
                renderBlocks.colorBlueTopLeft  = renderBlocks.colorBlueBottomLeft  = renderBlocks.colorBlueBottomRight  = renderBlocks.colorBlueTopRight  = blockRGB[2];

            } else {

                renderBlocks.colorRedTopLeft   = renderBlocks.colorRedBottomLeft   = renderBlocks.colorRedBottomRight   = renderBlocks.colorRedTopRight   = finalRGB[RED]   * lightness;
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = finalRGB[GREEN] * lightness;
                renderBlocks.colorBlueTopLeft  = renderBlocks.colorBlueBottomLeft  = renderBlocks.colorBlueBottomRight  = renderBlocks.colorBlueTopRight  = finalRGB[BLUE]  * lightness;

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
     * Takes two brightness inputs and returns average brightness.
     */
    public static int getAverageBrightness(int brightness1, int brightness2)
    {
        int section_1_1 = brightness1 >> 16 & 255;
        int section_1_3 = brightness1 & 255;

        int section_2_1 = brightness2 >> 16 & 255;
        int section_2_3 = brightness2 & 255;

        int difference1 = (int) ((section_1_1 + section_2_1) / 2.0F);
        int difference3 = (int) ((section_1_3 + section_2_3) / 2.0F);

        return difference1 << 16 | difference3;
    }

    /**
     * Returns mixed ambient occlusion value from two inputs, with ratio
     * applied to first input.
     */
    public static float getMixedAo(float ao1, float ao2, double ratio)
    {
        float diff = (float) (Math.abs(ao1 - ao2) * (1.0F - ratio));

        return ao1 > ao2 ? ao1 - diff : ao1 + diff;
    }

    /**
     * Fills AO variables with lightness for bottom face.
     */
    public LightingHelper setLightingYNeg(Block block, int x, int y, int z)
    {
        int y_offset = renderBlocks.renderMinY > 0.0F ? y : y - 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z);
        tessellator.setBrightness(mixedBrightness);
        lightness = LIGHTNESS_YN;

        if (renderBlocks.enableAO) {

            float ratio = (float) (1.0F - renderBlocks.renderMinY);
            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y_offset, z);

            renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
            renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
            renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
            renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
            renderBlocks.aoLightValueScratchXYNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z), ratio);
            renderBlocks.aoLightValueScratchYZNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1), ratio);
            renderBlocks.aoLightValueScratchYZNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1), ratio);
            renderBlocks.aoLightValueScratchXYPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z), ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1), ratio);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1), ratio);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1), ratio);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1), ratio);

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
        int y_offset = renderBlocks.renderMaxY < 1.0F ? y : y + 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z);
        tessellator.setBrightness(mixedBrightness);
        lightness = LIGHTNESS_YP;

        if (renderBlocks.enableAO) {

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y_offset, z);

            renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z - 1);
            renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z - 1);
            renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z + 1);
            renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z + 1);
            renderBlocks.aoLightValueScratchXYNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchYZPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchYZPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1), renderBlocks.renderMaxY);

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
        int z_offset = renderBlocks.renderMinZ > 0.0F ? z : z - 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z_offset);
        tessellator.setBrightness(mixedBrightness);
        lightness = LIGHTNESS_ZN;

        if (renderBlocks.enableAO) {

            float ratio = (float) (1.0F - renderBlocks.renderMinZ);
            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z_offset);

            renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
            renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
            renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
            renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
            renderBlocks.aoLightValueScratchXZNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z), ratio);
            renderBlocks.aoLightValueScratchYZNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z), ratio);
            renderBlocks.aoLightValueScratchYZPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z), ratio);
            renderBlocks.aoLightValueScratchXZPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z), ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z), ratio);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z), ratio);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z), ratio);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z), ratio);

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
        int z_offset = renderBlocks.renderMaxZ < 1.0F ? z : z + 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z_offset);
        tessellator.setBrightness(mixedBrightness);
        lightness = LIGHTNESS_ZP;

        if (renderBlocks.enableAO) {

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z_offset);

            renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z);
            renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z);
            renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z);
            renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z);
            renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z);
            renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z);
            renderBlocks.aoLightValueScratchXZNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXZPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchYZNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchYZPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z), renderBlocks.renderMaxZ);

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
        int x_offset = renderBlocks.renderMinX > 0.0F ? x : x - 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z);
        tessellator.setBrightness(mixedBrightness);
        lightness = LIGHTNESS_XN;

        if (renderBlocks.enableAO) {

            float ratio = (float) (1.0F - renderBlocks.renderMinX);
            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x_offset, y, z);

            renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
            renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
            renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
            renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
            renderBlocks.aoLightValueScratchXYNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z), ratio);
            renderBlocks.aoLightValueScratchXZNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1), ratio);
            renderBlocks.aoLightValueScratchXZNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1), ratio);
            renderBlocks.aoLightValueScratchXYNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z), ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1), ratio);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1), ratio);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1), ratio);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x - 1, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1), ratio);

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
        int x_offset = renderBlocks.renderMaxX < 1.0F ? x : x + 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z);
        tessellator.setBrightness(mixedBrightness);
        lightness = LIGHTNESS_XP;

        if (renderBlocks.enableAO) {

            float aoLightValue = block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x_offset, y, z);

            renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z);
            renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z - 1);
            renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z + 1);
            renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z);
            renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z - 1);
            renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z + 1);
            renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z - 1);
            renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z + 1);
            renderBlocks.aoLightValueScratchXYPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXZPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z - 1), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXZPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y, z + 1), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z - 1), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y - 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y - 1, z + 1), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z - 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z - 1), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x + 1, y + 1, z + 1), block.getAmbientOcclusionLightValue(renderBlocks.blockAccess, x, y + 1, z + 1), renderBlocks.renderMaxX);

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
