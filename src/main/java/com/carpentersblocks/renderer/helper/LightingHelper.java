package com.carpentersblocks.renderer.helper;

import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import com.carpentersblocks.renderer.BlockHandlerBase;
import com.carpentersblocks.util.BlockProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightingHelper {

    public  RenderBlocks    renderBlocks;
    private boolean         hasLightnessOverride;
    private float           lightnessOverride;
    private boolean         hasBrightnessOverride;
    private int             brightnessOverride;
    private boolean         hasColorOverride;
    private int             colorOverride          = 0xffffff;
    public static final int NORMAL_BRIGHTNESS      = 0xff00ff;
    public static final int MAX_BRIGHTNESS         = 0xf000f0;
    public static float[]   LIGHTNESS              = { 0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F };

    /** Ambient occlusion values for all four corners of side. */
    public float[] ao = new float[4];

    /** Brightness for side. */
    public int brightness;

    /**
     * Class constructor specifying the {@link BlockHandlerBase}.
     *
     * @param blockHandler  the {@link BlockHandlerBase}
     * @return the {@link LightingHelper}
     */
    public LightingHelper(RenderBlocks renderBlocks)
    {
        this.renderBlocks = renderBlocks;
    }

    /**
     * Sets lightness override.
     *
     * @param lightness  the lightness override
     * @return the {@link LightingHelper}
     */
    public LightingHelper setLightnessOverride(float lightness)
    {
        hasLightnessOverride = true;
        lightnessOverride = lightness;
        return this;
    }

    /**
     * Clears lightness override.
     *
     * @return nothing
     */
    public void clearLightnessOverride()
    {
        hasLightnessOverride = false;
    }

    /**
     * Sets brightness override.
     *
     * @param lightness  the brightness override
     * @return the {@link LightingHelper}
     */
    public LightingHelper setBrightnessOverride(int brightness)
    {
        hasBrightnessOverride = true;
        brightnessOverride = brightness;
        return this;
    }

    /**
     * Clears brightness override.
     *
     * @return nothing
     */
    public void clearBrightnessOverride()
    {
        hasBrightnessOverride = false;
    }

    /**
     * Sets color override.
     *
     * @param color  the color override
     * @return nothing
     */
    public void setColorOverride(int color)
    {
        hasColorOverride = true;
        colorOverride = color;
    }

    /**
     * Clears color override.
     *
     * @return nothing
     */
    public void clearColorOverride()
    {
        hasColorOverride = false;
    }

    /**
     * Gets rgb color from integer.
     *
     * @param color  the integer color
     * @return a float array with rgb values
     */
    public static float[] getRGB(int color)
    {
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;

        return new float[] { red, green, blue };
    }

    /**
     * Sets up the color using lightness, brightness, and the primary color
     * value (usually the dye color) for the side.
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param block  the {@link Block} inside the {@link ItemStack}
     * @param x  the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @param side  the side
     * @param hexColor  the primary color
     * @param icon  the icon
     * @param grassMode  affects how color is applied to faces for grass block only
     * @return a float array with rgb values
     */
    public void setupColor(int x, int y, int z, int side, int hexColor, IIcon icon)
    {
        Tessellator tessellator = Tessellator.instance;
        float lightness = hasLightnessOverride ? lightnessOverride : LIGHTNESS[side];
        tessellator.setBrightness(hasBrightnessOverride ? brightnessOverride : brightness);
        float[] rgb = getRGB(hexColor);

        if (hasColorOverride && !renderBlocks.hasOverrideBlockTexture()) {
            rgb = getRGB(colorOverride);
        }

        applyAnaglyph(rgb);

        if (renderBlocks.enableAO) {

            if (renderBlocks.hasOverrideBlockTexture()) {

                renderBlocks.colorRedTopLeft   = renderBlocks.colorRedBottomLeft   = renderBlocks.colorRedBottomRight   = renderBlocks.colorRedTopRight   = rgb[0];
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = rgb[1];
                renderBlocks.colorBlueTopLeft  = renderBlocks.colorBlueBottomLeft  = renderBlocks.colorBlueBottomRight  = renderBlocks.colorBlueTopRight  = rgb[2];

            } else {

                renderBlocks.colorRedTopLeft   = renderBlocks.colorRedBottomLeft   = renderBlocks.colorRedBottomRight   = renderBlocks.colorRedTopRight   = rgb[0] * lightness;
                renderBlocks.colorGreenTopLeft = renderBlocks.colorGreenBottomLeft = renderBlocks.colorGreenBottomRight = renderBlocks.colorGreenTopRight = rgb[1] * lightness;
                renderBlocks.colorBlueTopLeft  = renderBlocks.colorBlueBottomLeft  = renderBlocks.colorBlueBottomRight  = renderBlocks.colorBlueTopRight  = rgb[2] * lightness;

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

            tessellator.setColorOpaque_F(rgb[0] * lightness, rgb[1] * lightness, rgb[2] * lightness);

        }
    }

    /**
     * Will apply anaglyph color multipliers to RGB float array.
     * <p>
     * If {@link EntityRenderer#anaglyphEnable} is false,
     * will do nothing.
     *
     * @param rgb  array containing red, green and blue float values
     * @return nothing
     */
    public void applyAnaglyph(float[] rgb)
    {
        if (EntityRenderer.anaglyphEnable) {
            rgb[0] = (rgb[0] * 30.0F + rgb[1] * 59.0F + rgb[2] * 11.0F) / 100.0F;
            rgb[1] = (rgb[0] * 30.0F + rgb[1] * 70.0F) / 100.0F;
            rgb[2] = (rgb[0] * 30.0F + rgb[2]  * 70.0F) / 100.0F;
        }
    }

    /**
     * Gets average brightness from two brightness values.
     *
     * @param brightness1  the first brightness value
     * @param brightness2  the second brightness value
     * @return the mixed brightness
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
     * Gets mixed ambient occlusion value from two inputs, with a
     * ratio applied to the final result.
     *
     * @param ao1  the first ambient occlusion value
     * @param ao2  the second ambient occlusion value
     * @param ratio  the ratio for mixing
     * @return the mixed red, green, blue float values
     */
    public static float getMixedAo(float ao1, float ao2, double ratio)
    {
        float diff = (float) (Math.abs(ao1 - ao2) * (1.0F - ratio));

        return ao1 > ao2 ? ao1 - diff : ao1 + diff;
    }

    /**
     * Sets up lighting for the bottom face and returns the {@link LightingHelper}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading
     * with respect to the following attributes:
     * <p>
     * <ul>
     *   <li>{@link RenderBlocks#enableAO}</li>
     *   <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param x     the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @return the {@link LightingHelper}
     */
    public LightingHelper setupLightingYNeg(ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        int y_offset = renderBlocks.renderMinY > 0.0F ? y : y - 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z);
        brightness = mixedBrightness;

        if (renderBlocks.enableAO) {

            float ratio = (float) (1.0F - renderBlocks.renderMinY);
            float aoLightValue = renderBlocks.blockAccess.getBlock(x, y_offset, z).getAmbientOcclusionLightValue();

            renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y_offset, z);
            renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z - 1);
            renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z + 1);
            renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y_offset, z);
            renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y_offset, z - 1);
            renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y_offset, z + 1);
            renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y_offset, z - 1);
            renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y_offset, z + 1);
            renderBlocks.aoLightValueScratchXYNN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchYZNN = getMixedAo(renderBlocks.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchYZNP = getMixedAo(renderBlocks.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYPN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue(), ratio);

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
     * Sets up lighting for the top face and returns the {@link LightingHelper}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading
     * with respect to the following attributes:
     * <p>
     * <ul>
     *   <li>{@link RenderBlocks#enableAO}</li>
     *   <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param x     the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @return the {@link LightingHelper}
     */
    public LightingHelper setupLightingYPos(ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        int y_offset = renderBlocks.renderMaxY < 1.0F ? y : y + 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z);
        brightness = mixedBrightness;

        if (renderBlocks.enableAO) {

            float aoLightValue = renderBlocks.blockAccess.getBlock(x, y_offset, z).getAmbientOcclusionLightValue();

            renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y_offset, z);
            renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y_offset, z);
            renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z - 1);
            renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y_offset, z + 1);
            renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y_offset, z - 1);
            renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y_offset, z - 1);
            renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y_offset, z + 1);
            renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y_offset, z + 1);
            renderBlocks.aoLightValueScratchXYNP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYPP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchYZPN = getMixedAo(renderBlocks.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchYZPP = getMixedAo(renderBlocks.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxY);

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
     * Sets up lighting for the North face and returns the {@link LightingHelper}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading
     * with respect to the following attributes:
     * <p>
     * <ul>
     *   <li>{@link RenderBlocks#enableAO}</li>
     *   <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param x     the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @return the {@link LightingHelper}
     */
    public LightingHelper setupLightingZNeg(ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        int z_offset = renderBlocks.renderMinZ > 0.0F ? z : z - 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z_offset);
        brightness = mixedBrightness;

        if (renderBlocks.enableAO) {

            float ratio = (float) (1.0F - renderBlocks.renderMinZ);
            float aoLightValue = renderBlocks.blockAccess.getBlock(x, y, z_offset).getAmbientOcclusionLightValue();

            renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z_offset);
            renderBlocks.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z_offset);
            renderBlocks.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z_offset);
            renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z_offset);
            renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z_offset);
            renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z_offset);
            renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z_offset);
            renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z_offset);
            renderBlocks.aoLightValueScratchXZNN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchYZNN = getMixedAo(renderBlocks.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchYZPN = getMixedAo(renderBlocks.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXZPN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue(), ratio);

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
     * Sets up lighting for the South face and returns the {@link LightingHelper}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading
     * with respect to the following attributes:
     * <p>
     * <ul>
     *   <li>{@link RenderBlocks#enableAO}</li>
     *   <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param x     the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @return the {@link LightingHelper}
     */
    public LightingHelper setupLightingZPos(ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        int z_offset = renderBlocks.renderMaxZ < 1.0F ? z : z + 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z_offset);
        brightness = mixedBrightness;

        if (renderBlocks.enableAO) {

            float aoLightValue = renderBlocks.blockAccess.getBlock(x, y, z_offset).getAmbientOcclusionLightValue();

            renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y, z_offset);
            renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y, z_offset);
            renderBlocks.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y - 1, z_offset);
            renderBlocks.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y + 1, z_offset);
            renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y - 1, z_offset);
            renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x - 1, y + 1, z_offset);
            renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y - 1, z_offset);
            renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x + 1, y + 1, z_offset);
            renderBlocks.aoLightValueScratchXZNP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXZPP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchYZNP = getMixedAo(renderBlocks.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchYZPP = getMixedAo(renderBlocks.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxZ);

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
     * Sets up lighting for the West face and returns the {@link LightingHelper}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading
     * with respect to the following attributes:
     * <p>
     * <ul>
     *   <li>{@link RenderBlocks#enableAO}</li>
     *   <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param x     the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @return the {@link LightingHelper}
     */
    public LightingHelper setupLightingXNeg(ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        int x_offset = renderBlocks.renderMinX > 0.0F ? x : x - 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z);
        brightness = mixedBrightness;

        if (renderBlocks.enableAO) {

            float ratio = (float) (1.0F - renderBlocks.renderMinX);
            float aoLightValue = renderBlocks.blockAccess.getBlock(x_offset, y, z).getAmbientOcclusionLightValue();

            renderBlocks.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y - 1, z);
            renderBlocks.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z - 1);
            renderBlocks.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z + 1);
            renderBlocks.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y + 1, z);
            renderBlocks.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y - 1, z - 1);
            renderBlocks.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y - 1, z + 1);
            renderBlocks.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y + 1, z - 1);
            renderBlocks.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y + 1, z + 1);
            renderBlocks.aoLightValueScratchXYNN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXZNN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXZNP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYNP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNNN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNNP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNPN = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue(), ratio);
            renderBlocks.aoLightValueScratchXYZNPP = getMixedAo(renderBlocks.blockAccess.getBlock(x - 1, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue(), ratio);

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
     * Sets up lighting for the East face and returns the {@link LightingHelper}.
     * <p>
     * This is a consolidated <code>method</code> that sets side shading
     * with respect to the following attributes:
     * <p>
     * <ul>
     *   <li>{@link RenderBlocks#enableAO}</li>
     *   <li>{@link RenderBlocks#partialRenderBounds}</li>
     * </ul>
     *
     * @param itemStack  the cover {@link ItemStack}
     * @param x     the x coordinate
     * @param y  the y coordinate
     * @param z  the z coordinate
     * @return the {@link LightingHelper}
     */
    public LightingHelper setupLightingXPos(ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        int x_offset = renderBlocks.renderMaxX < 1.0F ? x : x + 1;
        int mixedBrightness = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z);
        brightness = mixedBrightness;

        if (renderBlocks.enableAO) {

            float aoLightValue = renderBlocks.blockAccess.getBlock(x_offset, y, z).getAmbientOcclusionLightValue();

            renderBlocks.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y - 1, z);
            renderBlocks.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z - 1);
            renderBlocks.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y, z + 1);
            renderBlocks.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y + 1, z);
            renderBlocks.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y - 1, z - 1);
            renderBlocks.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y - 1, z + 1);
            renderBlocks.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y + 1, z - 1);
            renderBlocks.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x_offset, y + 1, z + 1);
            renderBlocks.aoLightValueScratchXYPN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXZPN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXZPP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYPP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPNN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPNP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPPN = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);
            renderBlocks.aoLightValueScratchXYZPPP = getMixedAo(renderBlocks.blockAccess.getBlock(x + 1, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue(), renderBlocks.renderMaxX);

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
