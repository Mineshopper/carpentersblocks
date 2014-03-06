package carpentersblocks.renderer.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.flowerpot.FlowerPotProperties;
import carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelperFlowerPot extends RenderHelper {
    
    /**
     * Applies plant color to tessellator.
     */
    private static void setPlantColor(ItemStack itemStack, Block block, int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        LightingHelper lightingHelper = LightingHelper.instance;
        
        float[] rgb = lightingHelper.applyAnaglyphFilter(lightingHelper.getBlockRGB(itemStack, block, x, y, z));
        
        tessellator.setColorOpaque_F(rgb[0], rgb[1], rgb[2]);
        
        // Adapt this code so it works with new vanilla plants.
        
        if (FeatureRegistry.enablePlantColorOverride) {
            if (block.getBlockColor() != 16777215) {
                tessellator.setColorOpaque_F(0.45F, 0.80F, 0.30F);
            }
        }
    }
        
    /**
     * Renders a double tall plant.
     */
    public boolean renderBlockDoublePlant(RenderBlocks renderBlocks, BlockDoublePlant block, int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        int l = block.colorMultiplier(renderBlocks.blockAccess, x, y, z);
        float f = (l >> 16 & 255) / 255.0F;
        float f1 = (l >> 8 & 255) / 255.0F;
        float f2 = (l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        long j1 = x * 3129871 ^ z * 116129781L;
        j1 = j1 * j1 * 42317861L + j1 * 11L;
        double d19 = x;
        double d0 = y;
        double d1 = z;
        d19 += ((j1 >> 16 & 15L) / 15.0F - 0.5D) * 0.3D;
        d1 += ((j1 >> 24 & 15L) / 15.0F - 0.5D) * 0.3D;
        int i1 = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
        boolean flag = false;
        boolean flag1 = BlockDoublePlant.func_149887_c(i1);
        int k1;

        if (flag1)
        {
            if (renderBlocks.blockAccess.getBlock(x, y - 1, z) != block)
            {
                return false;
            }

            k1 = BlockDoublePlant.func_149890_d(renderBlocks.blockAccess.getBlockMetadata(x, y - 1, z));
        }
        else
        {
            k1 = BlockDoublePlant.func_149890_d(i1);
        }

        IIcon iicon = block.func_149888_a(flag1, k1);
        renderBlocks.drawCrossedSquares(iicon, d19, d0, d1, 1.0F);

        if (flag1 && k1 == 0)
        {
            IIcon iicon1 = block.sunflowerIcons[0];
            double d2 = Math.cos(j1 * 0.8D) * Math.PI * 0.1D;
            double d3 = Math.cos(d2);
            double d4 = Math.sin(d2);
            double d5 = iicon1.getMinU();
            double d6 = iicon1.getMinV();
            double d7 = iicon1.getMaxU();
            double d8 = iicon1.getMaxV();
            double d9 = 0.3D;
            double d10 = -0.05D;
            double d11 = 0.5D + 0.3D * d3 - 0.5D * d4;
            double d12 = 0.5D + 0.5D * d3 + 0.3D * d4;
            double d13 = 0.5D + 0.3D * d3 + 0.5D * d4;
            double d14 = 0.5D + -0.5D * d3 + 0.3D * d4;
            double d15 = 0.5D + -0.05D * d3 + 0.5D * d4;
            double d16 = 0.5D + -0.5D * d3 + -0.05D * d4;
            double d17 = 0.5D + -0.05D * d3 - 0.5D * d4;
            double d18 = 0.5D + 0.5D * d3 + -0.05D * d4;
            tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d5, d8);
            tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d7, d8);
            tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d7, d6);
            tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d5, d6);
            IIcon iicon2 = block.sunflowerIcons[1];
            d5 = iicon2.getMinU();
            d6 = iicon2.getMinV();
            d7 = iicon2.getMaxU();
            d8 = iicon2.getMaxV();
            tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d5, d8);
            tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d7, d8);
            tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d7, d6);
            tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d5, d6);
        }

        return true;
    }
    
    /**
     * Renders plant using crossed squares.
     */
    public static boolean renderPlantCrossedSquares(RenderBlocks renderBlocks, ItemStack itemStack, int x, int y, int z, float scale, boolean flipped)
    {
        Block block = FlowerPotProperties.toBlock(itemStack);
        
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        
        IIcon icon = block.getIcon(2, itemStack.getItemDamage());
        
        setPlantColor(itemStack, block, x, y, z);
        
        double uMin = icon.getMinU();
        double vMin = icon.getMinV();
        double uMax = icon.getMaxU();
        double vMax = icon.getMaxV();
        double rotation = 0.45D * scale;
        double xMin = x + 0.5D - rotation;
        double xMax = x + 0.5D + rotation;
        double zMin = z + 0.5D - rotation;
        double zMax = z + 0.5D + rotation;
        
        if (flipped)
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
    public static void renderPlantThinCrossedSquares(RenderBlocks renderBlocks, ItemStack itemStack, int x, int y, int z, boolean flipped)
    {
        Block block = FlowerPotProperties.toBlock(itemStack);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
                
        if (block.equals(Blocks.wheat)) {
            itemStack.setItemDamage(7);
        }
        
        IIcon icon = renderBlocks.getIconSafe(block.getIcon(2, itemStack.getItemDamage()));

        System.out.println("DEBUG: Icon name = " + icon.getIconName() + ", for damage " + itemStack.getItemDamage());
        
        setPlantColor(itemStack, block, x, y, z);
        
        double uMin = icon.getInterpolatedU(0.0D);
        double uMax = icon.getInterpolatedU(4.0D);
        double vMin = icon.getInterpolatedV(16.0D);
        double vMax = icon.getInterpolatedV(0.0D);
        double rotatedScaleFactor = 0.45D * 0.375F;
        double xMin = x + 0.5D - rotatedScaleFactor;
        double xMax = x + 0.5D + rotatedScaleFactor;
        double zMin = z + 0.5D - rotatedScaleFactor;
        double zMax = z + 0.5D + rotatedScaleFactor;
        
        if (flipped)
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
    public static void drawPlantCactus(RenderBlocks renderBlocks, ItemStack itemStack, int x, int y, int z)
    {
        Block block = BlockProperties.toBlock(itemStack);
        IIcon icon = block.getBlockTextureFromSide(2);
        
        LightingHelper lightingHelper = LightingHelper.instance;
        
        double uMinL = icon.getInterpolatedU(0.0D);
        double uMaxL = icon.getInterpolatedU(3.0D);
        double uMinR = icon.getInterpolatedU(13.0D);
        double uMaxR = icon.getInterpolatedU(16.0D);
        double vMin = icon.getInterpolatedV(16.0D);
        double vMax = icon.getInterpolatedV(0.0D);
        
        renderBlocks.enableAO = true;
        
        renderBlocks.setRenderBounds(0.375D, 0.25D, 0.375D, 0.6875D, 1.0D, 0.6875D);
        
        /* NORTH FACE */
        
        lightingHelper.setLightness(0.8F).setLightingZNeg(itemStack, x, y, z);
        lightingHelper.colorSide(itemStack, block, x, y, z, 2, icon);
        
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
        
        lightingHelper.setLightness(0.8F).setLightingZPos(itemStack, x, y, z);
        lightingHelper.colorSide(itemStack, block, x, y, z, 3, icon);
        
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
        
        lightingHelper.setLightness(0.6F).setLightingXNeg(itemStack, x, y, z);
        lightingHelper.colorSide(itemStack, block, x, y, z, 4, icon);
        
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
        
        lightingHelper.setLightness(0.6F).setLightingXPos(itemStack, x, y, z);
        lightingHelper.colorSide(itemStack, block, x, y, z, 5, icon);
        
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
        
        lightingHelper.setLightness(1.0F).setLightingYPos(itemStack, x, y, z);
        lightingHelper.colorSide(itemStack, block, x, y, z, 1, icon);
        
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
