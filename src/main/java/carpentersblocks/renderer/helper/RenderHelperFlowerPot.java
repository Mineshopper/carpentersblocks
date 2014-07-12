package carpentersblocks.renderer.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import carpentersblocks.data.FlowerPot;
import carpentersblocks.renderer.BlockHandlerBase;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.flowerpot.FlowerPotProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelperFlowerPot extends RenderHelper {

    /**
     * Applies plant color to tessellator.
     */
    public static void setPlantColor(BlockHandlerBase blockHandler, ItemStack itemStack, int x, int y, int z)
    {
        Block block = FlowerPotProperties.toBlock(itemStack);
        Tessellator tessellator = Tessellator.instance;

        float[] rgb = LightingHelper.getRGB(blockHandler.getBlockColor(block, itemStack.getItemDamage(), x, y, z, 1, null));
        blockHandler.lightingHelper.applyAnaglyph(rgb);

        tessellator.setColorOpaque_F(rgb[0], rgb[1], rgb[2]);

        if (FlowerPot.isEnriched(blockHandler.TE)) {
            if (FlowerPotProperties.getPlantColor(blockHandler.TE) != 16777215) {
                tessellator.setColorOpaque_F(0.45F, 0.80F, 0.30F);
            }
        }
    }

    /**
     * Renders a vanilla double tall plant.
     */
    public static boolean renderBlockDoublePlant(TEBase TE, RenderBlocks renderBlocks, ItemStack itemStack, int x, int y, int z, boolean thin)
    {
        BlockDoublePlant block = (BlockDoublePlant) FlowerPotProperties.toBlock(itemStack);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

        //setPlantColor(TE, renderBlocks, itemStack, block, x, y, z);

        boolean thinPlant = itemStack.getUnlocalizedName().equals("tile.doublePlant.grass");

        int metadata = itemStack.getItemDamage();

        /* Render bottom stem. */

        IIcon icon_bottom = block.func_149888_a(false, metadata);

        if (thinPlant) {
            renderPlantThinCrossedSquares(renderBlocks, block, icon_bottom, x, y, z, false);
        } else {
            renderPlantCrossedSquares(renderBlocks, block, icon_bottom, x, y, z, 0.75F, false);
        }

        tessellator.addTranslation(0.0F, 0.75F, 0.0F);

        /* Render top stem. */

        IIcon icon_top = block.func_149888_a(true, metadata);

        if (thinPlant) {
            renderPlantThinCrossedSquares(renderBlocks, block, icon_top, x, y, z, false);
        } else {
            renderPlantCrossedSquares(renderBlocks, block, icon_top, x, y, z, 0.75F, false);
        }

        /* Render sunflower top. */

        if (metadata == 0) {

            tessellator.addTranslation(0.0F, -0.15F, 0.0F);

            IIcon icon_sunflower_top_front = block.sunflowerIcons[0];
            double angle = FlowerPot.getAngle(TE) / 16.0D * 2 * Math.PI + Math.PI / 2;

            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double uMin = icon_sunflower_top_front.getMinU();
            double vMin = icon_sunflower_top_front.getMinV();
            double uMax = icon_sunflower_top_front.getMaxU();
            double vMax = icon_sunflower_top_front.getMaxV();
            double d11 = 0.5D + 0.25D * cos - 0.45D * sin;
            double d12 = 0.5D + 0.45D * cos + 0.25D * sin;
            double d13 = 0.5D + 0.25D * cos + 0.45D * sin;
            double d14 = 0.5D + -0.45D * cos + 0.25D * sin;
            double d15 = 0.5D + -0.05D * cos + 0.45D * sin;
            double d16 = 0.5D + -0.45D * cos + -0.05D * sin;
            double d17 = 0.5D + -0.05D * cos - 0.45D * sin;
            double d18 = 0.5D + 0.45D * cos + -0.05D * sin;
            tessellator.addVertexWithUV(x + d15, y + 1.0D, z + d16, uMin, vMax);
            tessellator.addVertexWithUV(x + d17, y + 1.0D, z + d18, uMax, vMax);
            tessellator.addVertexWithUV(x + d11, y + 0.0D, z + d12, uMax, vMin);
            tessellator.addVertexWithUV(x + d13, y + 0.0D, z + d14, uMin, vMin);
            IIcon icon_sunflower_top_back = block.sunflowerIcons[1];
            uMin = icon_sunflower_top_back.getMinU();
            vMin = icon_sunflower_top_back.getMinV();
            uMax = icon_sunflower_top_back.getMaxU();
            vMax = icon_sunflower_top_back.getMaxV();
            tessellator.addVertexWithUV(x + d17, y + 1.0D, z + d18, uMin, vMax);
            tessellator.addVertexWithUV(x + d15, y + 1.0D, z + d16, uMax, vMax);
            tessellator.addVertexWithUV(x + d13, y + 0.0D, z + d14, uMax, vMin);
            tessellator.addVertexWithUV(x + d11, y + 0.0D, z + d12, uMin, vMin);

            tessellator.addTranslation(0.0F, 0.15F, 0.0F);

        }

        tessellator.addTranslation(0.0F, -0.75F, 0.0F);

        return true;
    }

    /**
     * Renders plant using crossed squares.
     */
    public static boolean renderPlantCrossedSquares(RenderBlocks renderBlocks, Block block, IIcon icon, int x, int y, int z, float scale, boolean flip_vertical)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

        double uMin = icon.getMinU();
        double vMin = icon.getMinV();
        double uMax = icon.getMaxU();
        double vMax = icon.getMaxV();
        double rotation = 0.45D * scale;
        double xMin = x + 0.5D - rotation;
        double xMax = x + 0.5D + rotation;
        double zMin = z + 0.5D - rotation;
        double zMax = z + 0.5D + rotation;

        if (flip_vertical)
        {
            double temp = vMin;
            vMin = vMax;
            vMax = temp;
        }

        tessellator.addVertexWithUV(xMin, y + (double) scale, zMin, uMin, vMin);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMin, vMax);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMax, vMax);
        tessellator.addVertexWithUV(xMax, y + (double) scale, zMax, uMax, vMin);
        tessellator.addVertexWithUV(xMax, y + (double) scale, zMax, uMin, vMin);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMin, vMax);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMax, vMax);
        tessellator.addVertexWithUV(xMin, y + (double) scale, zMin, uMax, vMin);
        tessellator.addVertexWithUV(xMin, y + (double) scale, zMax, uMin, vMin);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMin, vMax);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMax, vMax);
        tessellator.addVertexWithUV(xMax, y + (double) scale, zMin, uMax, vMin);
        tessellator.addVertexWithUV(xMax, y + (double) scale, zMin, uMin, vMin);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMin, vMax);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMax, vMax);
        tessellator.addVertexWithUV(xMin, y + (double) scale, zMax, uMax, vMin);

        return true;
    }

    /**
     * Renders thin plant using crossed squares.
     */
    public static void renderPlantThinCrossedSquares(RenderBlocks renderBlocks, Block block, IIcon icon, int x, int y, int z, boolean flip_vertical)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

        double uMin = icon.getInterpolatedU(0.0D);
        double uMax = icon.getInterpolatedU(4.0D);
        double vMin = icon.getInterpolatedV(16.0D);
        double vMax = icon.getInterpolatedV(0.0D);
        double rotatedScaleFactor = 0.45D * 0.375F;
        double xMin = x + 0.5D - rotatedScaleFactor;
        double xMax = x + 0.5D + rotatedScaleFactor;
        double zMin = z + 0.5D - rotatedScaleFactor;
        double zMax = z + 0.5D + rotatedScaleFactor;

        if (flip_vertical)
        {
            double temp = vMin;
            vMin = vMax;
            vMax = temp;
        }

        tessellator.addVertexWithUV(xMin, y + 0.75D, zMin, uMin, vMax);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMin, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

        tessellator.addVertexWithUV(xMax, y + 0.75D, zMin, uMin, vMax);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMin, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

        tessellator.addVertexWithUV(xMax, y + 0.75D, zMax, uMin, vMax);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMin, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

        tessellator.addVertexWithUV(xMin, y + 0.75D, zMax, uMin, vMax);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMin, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMax, vMin);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMax, vMax);

        uMin = icon.getInterpolatedU(12.0D);
        uMax = icon.getInterpolatedU(16.0D);

        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMin, uMax, vMin);
        tessellator.addVertexWithUV(xMin, y + 0.75D, zMin, uMax, vMax);

        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMin, uMax, vMin);
        tessellator.addVertexWithUV(xMax, y + 0.75D, zMin, uMax, vMax);

        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
        tessellator.addVertexWithUV(xMax, y + 0.0D, zMax, uMax, vMin);
        tessellator.addVertexWithUV(xMax, y + 0.75D, zMax, uMax, vMax);

        tessellator.addVertexWithUV(x + 0.5D, y + 0.75D, z + 0.5D, uMin, vMax);
        tessellator.addVertexWithUV(x + 0.5D, y + 0.0D, z + 0.5D, uMin, vMin);
        tessellator.addVertexWithUV(xMin, y + 0.0D, zMax, uMax, vMin);
        tessellator.addVertexWithUV(xMin, y + 0.75D, zMax, uMax, vMax);
    }

    /**
     * Renders vanilla cactus using "prickly" method.
     */
    public static void drawPlantCactus(LightingHelper lightingHelper, RenderBlocks renderBlocks, ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        IIcon icon = block.getBlockTextureFromSide(2);

        double uMinL = icon.getInterpolatedU(0.0D);
        double uMaxL = icon.getInterpolatedU(3.0D);
        double uMinR = icon.getInterpolatedU(13.0D);
        double uMaxR = icon.getInterpolatedU(16.0D);
        double vMin = icon.getInterpolatedV(16.0D);
        double vMax = icon.getInterpolatedV(0.0D);

        renderBlocks.enableAO = true;
        renderBlocks.setRenderBounds(0.375D, 0.25D, 0.375D, 0.6875D, 1.0D, 0.6875D);

        /* NORTH FACE */

        lightingHelper.setupLightingZNeg(itemStack, x, y, z);
        lightingHelper.setupColor(x, y, z, 2, 16777215, icon);

        // LEFT
        setupVertex(renderBlocks, x + 0.6875F, y + 0.75F, z + 0.375F, uMinL, vMax, TOP_LEFT);
        setupVertex(renderBlocks, x + 0.6875F, y, z + 0.375F, uMinL, vMin, BOTTOM_LEFT);
        setupVertex(renderBlocks, x + 0.5F, y, z + 0.375F, uMaxL, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.375F, uMaxL, vMax, TOP_CENTER);

        // RIGHT
        setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.375F, uMinR, vMax, TOP_CENTER);
        setupVertex(renderBlocks, x + 0.5F, y, z + 0.375F, uMinR, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.3125F, y, z + 0.375F, uMaxR, vMin, BOTTOM_RIGHT);
        setupVertex(renderBlocks, x + 0.3125F, y + 0.75F, z + 0.375F, uMaxR, vMax, TOP_RIGHT);

        /* SOUTH FACE */

        lightingHelper.setupLightingZPos(itemStack, x, y, z);
        lightingHelper.setupColor(x, y, z, 3, 16777215, icon);

        // LEFT
        setupVertex(renderBlocks, x + 0.3125F, y + 0.75F, z + 0.625F, uMinL, vMax, TOP_LEFT);
        setupVertex(renderBlocks, x + 0.3125F, y, z + 0.625F, uMinL, vMin, BOTTOM_LEFT);
        setupVertex(renderBlocks, x + 0.5F, y, z + 0.625F, uMaxL, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.625F, uMaxL, vMax, TOP_CENTER);

        // RIGHT
        setupVertex(renderBlocks, x + 0.5F, y + 0.75F, z + 0.625F, uMinR, vMax, TOP_CENTER);
        setupVertex(renderBlocks, x + 0.5F, y, z + 0.625F, uMinR, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.6875F, y, z + 0.625F, uMaxR, vMin, BOTTOM_RIGHT);
        setupVertex(renderBlocks, x + 0.6875F, y + 0.75F, z + 0.625F, uMaxR, vMax, TOP_RIGHT);

        /* WEST FACE */

        lightingHelper.setupLightingXNeg(itemStack, x, y, z);
        lightingHelper.setupColor(x, y, z, 4, 16777215, icon);

        // LEFT
        setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.3125F, uMinL, vMax, TOP_LEFT);
        setupVertex(renderBlocks, x + 0.375F, y, z + 0.3125F, uMinL, vMin, BOTTOM_LEFT);
        setupVertex(renderBlocks, x + 0.375F, y, z + 0.5F, uMaxL, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.5F, uMaxL, vMax, TOP_CENTER);

        // RIGHT
        setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.5F, uMinR, vMax, TOP_CENTER);
        setupVertex(renderBlocks, x + 0.375F, y, z + 0.5F, uMinR, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.375F, y, z + 0.6875F, uMaxR, vMin, BOTTOM_RIGHT);
        setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.6875F, uMaxR, vMax, TOP_RIGHT);

        /* EAST FACE */

        lightingHelper.setupLightingXPos(itemStack, x, y, z);
        lightingHelper.setupColor(x, y, z, 5, 16777215, icon);

        // LEFT
        setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.6875F, uMinL, vMax, TOP_LEFT);
        setupVertex(renderBlocks, x + 0.625F, y, z + 0.6875F, uMinL, vMin, BOTTOM_LEFT);
        setupVertex(renderBlocks, x + 0.625F, y, z + 0.5F, uMaxL, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.5F, uMaxL, vMax, TOP_CENTER);

        // RIGHT
        setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.5F, uMinR, vMax, TOP_CENTER);
        setupVertex(renderBlocks, x + 0.625F, y, z + 0.5F, uMinR, vMin, BOTTOM_CENTER);
        setupVertex(renderBlocks, x + 0.625F, y, z + 0.3125F, uMaxR, vMin, BOTTOM_RIGHT);
        setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.3125F, uMaxR, vMax, TOP_RIGHT);

        /* UP */

        lightingHelper.setupLightingYPos(itemStack, x, y, z);
        lightingHelper.setupColor(x, y, z, 1, 16777215, icon);

        icon = block.getBlockTextureFromSide(1);

        double uMin = icon.getInterpolatedU(6.0D);
        double uMax = icon.getInterpolatedU(10.0D);
        vMin = icon.getInterpolatedV(10.0D);
        vMax = icon.getInterpolatedV(6.0D);

        setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.625F, uMin, vMin, TOP_LEFT);
        setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.625F, uMin, vMax, BOTTOM_LEFT);
        setupVertex(renderBlocks, x + 0.625F, y + 0.75F, z + 0.375F, uMax, vMax, BOTTOM_RIGHT);
        setupVertex(renderBlocks, x + 0.375F, y + 0.75F, z + 0.375F, uMax, vMin, TOP_RIGHT);

        renderBlocks.enableAO = false;
    }

}
