package com.carpentersblocks.renderer.helper;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.RenderBlockFluid;
import org.apache.logging.log4j.Level;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RoutableFluidsHelper {

    public final static Class[] liquidClasses = { BlockLiquid.class, IFluidBlock.class};
    private final static int CALLER_SUN = 0;
    private final static int CALLER_SEC = 1;
    private static int callMethod = -1;

    /**
     * Returns the most optimal available class for use in rendering
     * routable fluids.
     *
     * @return a class
     */
    public static Class getCallerClass()
    {
        if (callMethod < 0)
        {
            try {
                sun.reflect.Reflection.getCallerClass(2);
                callMethod = CALLER_SUN;
            } catch (Exception E) {
                try {
                    new SecurityManager() { Class clazz = getClassContext()[2]; };
                    callMethod = CALLER_SEC;
                } catch (Exception E1) {
                    FeatureRegistry.enableRoutableFluids = false;
                    ModLogger.log(Level.WARN, "Routable fluids failed: %s", E1.getMessage());
                };
            };
        }

        switch (callMethod)
        {
            case CALLER_SUN:
                return sun.reflect.Reflection.getCallerClass(4);
            case CALLER_SEC:
                return new SecurityManager() { Class clazz = getClassContext()[4]; }.clazz;
            default:
                return null;
        }
    }

    /**
     * Renders routable fluid.
     *
     * @param  TE the {@link TEBase}
     * @param  renderBlocks the {@link RenderBlocks}
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return true if fluid rendered in space
     */
    public static boolean render(TEBase TE, RenderBlocks renderBlocks, int x, int y, int z)
    {
        // Do not render if fluid is above block
        Class clazz_YP = renderBlocks.blockAccess.getBlock(x, y + 1, z).getClass();
        for (Class clazz : liquidClasses) {
            if (clazz.isAssignableFrom(clazz_YP)) {
                return false;
            }
        }

        ItemStack itemStack = getFluidBlock(renderBlocks.blockAccess, x, y, z);

        if (itemStack != null) {

            Block block = BlockProperties.toBlock(itemStack);
            int metadata = itemStack.getItemDamage();

            if (block.getRenderBlockPass() == MinecraftForgeClient.getRenderPass())
            {
                if (!block.hasTileEntity(metadata))
                {
                    if (block instanceof BlockLiquid) {
                        renderLiquidSurface(TE, renderBlocks, itemStack, x, y, z);
                    } else {
                        RenderBlockFluid.instance.renderWorldBlock(renderBlocks.blockAccess, x, y, z, block, 0, renderBlocks);
                    }
                    return true;
                }
            }

        }

        return false;
    }

    /**
     * Gets nearby, routable fluid block.
     *
     * @param  blockAccess the {@link IBlockAccess}
     * @param  x the x coordinate
     * @param  y the y coordinate
     * @param  z the z coordinate
     * @return a nearby fluid {@link ItemStack}, or null if no routable fluid exists
     */
    public static ItemStack getFluidBlock(IBlockAccess blockAccess, int x, int y, int z)
    {
        int[][] offsetXZ = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}, {-1, -1}, {-1, 1}, {1, 1}, {1, -1}};

        ForgeDirection[][][] route = {
                { { NORTH } },
                { { SOUTH } },
                { { WEST  } },
                { { EAST  } },
                { { NORTH, SOUTH, WEST }, { WEST, EAST, NORTH } },
                { { SOUTH, NORTH, WEST }, { WEST, EAST, SOUTH } },
                { { SOUTH, NORTH, EAST }, { EAST, WEST, SOUTH } },
                { { NORTH, SOUTH, EAST }, { EAST, WEST, NORTH } },
        };

        for (int idx = 0; idx < offsetXZ.length; ++idx) {

            Block block = blockAccess.getBlock(x + offsetXZ[idx][0], y, z + offsetXZ[idx][1]);
            Class clazz_offset = block.getClass();

            boolean isLiquid = false;
            for (Class clazz : liquidClasses) {
                if (clazz.isAssignableFrom(clazz_offset)) {
                    isLiquid = true;
                    break;
                }
            }

            if (isLiquid) {
                if (idx < 4) {
                    if (!blockAccess.isSideSolid(x, y, z, route[idx][0][0], false)) {
                        return new ItemStack(block, blockAccess.getBlockMetadata(x + offsetXZ[idx][0], y, z + offsetXZ[idx][1]));
                    }
                } else {
                    for (int routeIdx = 0; routeIdx < 2; ++routeIdx) {
                        if (!blockAccess.isSideSolid(x, y, z, route[idx][routeIdx][0], false)) {
                            int[] bridgeXZ = { x + route[idx][routeIdx][0].offsetX, z + route[idx][routeIdx][0].offsetZ };
                            if (!blockAccess.isSideSolid(bridgeXZ[0], y, bridgeXZ[1], route[idx][routeIdx][1], false) && !blockAccess.isSideSolid(bridgeXZ[0], y, bridgeXZ[1], route[idx][routeIdx][2], false)) {
                                return new ItemStack(block, blockAccess.getBlockMetadata(x + offsetXZ[idx][0], y, z + offsetXZ[idx][1]));
                            }
                        }
                    }
                }
            }

        }

        return null;
    }

    /**
     * Performs the rendering of the liquid surface.
     *
     * @param TE
     * @param renderBlocks
     * @param itemStack
     * @param x
     * @param y
     * @param z
     */
    public static void renderLiquidSurface(TEBase TE, RenderBlocks renderBlocks, ItemStack itemStack, int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        Block block = BlockProperties.toBlock(itemStack);
        Material material = block.getMaterial();
        double offset = 0.0010000000474974513D;
        IIcon icon = renderBlocks.getBlockIconFromSideAndMetadata(block, 1, itemStack.getItemDamage());

        float flowDir = -1000;
        if (block instanceof BlockLiquid) {
            flowDir = (float) BlockLiquid.getFlowDirection(renderBlocks.blockAccess, x, y, z, material);
        } else if (block instanceof BlockFluidBase) {
            flowDir = (float) BlockFluidBase.getFlowDirection(renderBlocks.blockAccess, x, y, z);
        }

        if (flowDir > -999.0F) {
            icon = renderBlocks.getBlockIconFromSideAndMetadata(block, 2, itemStack.getItemDamage());
        }

        double u_XZNN;
        double u_XZNP;
        double u_XZPP;
        double u_XZPN;
        double v_XZNN;
        double v_XZNP;
        double v_XZPP;
        double v_XZPN;

        if (flowDir < -999.0F)
        {
            u_XZNN = icon.getInterpolatedU(0.0D);
            v_XZNN = icon.getInterpolatedV(0.0D);
            u_XZNP = u_XZNN;
            v_XZNP = icon.getInterpolatedV(16.0D);
            u_XZPP = icon.getInterpolatedU(16.0D);
            v_XZPP = v_XZNP;
            u_XZPN = u_XZPP;
            v_XZPN = v_XZNN;
        }
        else
        {
            float sinDir = MathHelper.sin(flowDir) * 0.25F;
            float cosDir = MathHelper.cos(flowDir) * 0.25F;
            u_XZNN = icon.getInterpolatedU(8.0F + (-cosDir - sinDir) * 16.0F);
            v_XZNN = icon.getInterpolatedV(8.0F + (-cosDir + sinDir) * 16.0F);
            u_XZNP = icon.getInterpolatedU(8.0F + (-cosDir + sinDir) * 16.0F);
            v_XZNP = icon.getInterpolatedV(8.0F + (cosDir + sinDir) * 16.0F);
            u_XZPP = icon.getInterpolatedU(8.0F + (cosDir + sinDir) * 16.0F);
            v_XZPP = icon.getInterpolatedV(8.0F + (cosDir - sinDir) * 16.0F);
            u_XZPN = icon.getInterpolatedU(8.0F + (cosDir - sinDir) * 16.0F);
            v_XZPN = icon.getInterpolatedV(8.0F + (-cosDir - sinDir) * 16.0F);
        }

        double height_XZNN = renderBlocks.getLiquidHeight(x, y, z, material) - offset;
        double height_XZNP = renderBlocks.getLiquidHeight(x, y, z + 1, material) - offset;
        double height_XZPN = renderBlocks.getLiquidHeight(x + 1, y, z, material) - offset;
        double height_XZPP = renderBlocks.getLiquidHeight(x + 1, y, z + 1, material) - offset;

        int colorMultiplier = block.colorMultiplier(renderBlocks.blockAccess, x, y, z);
        float red = (colorMultiplier >> 16 & 255) / 255.0F;
        float green = (colorMultiplier >> 8 & 255) / 255.0F;
        float blue = (colorMultiplier & 255) / 255.0F;

        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        tessellator.setColorOpaque_F(red, green, blue);
        tessellator.addVertexWithUV(x, y + height_XZNN, z, u_XZNN, v_XZNN);
        tessellator.addVertexWithUV(x, y + height_XZNP, z + 1, u_XZNP, v_XZNP);
        tessellator.addVertexWithUV(x + 1, y + height_XZPP, z + 1, u_XZPP, v_XZPP);
        tessellator.addVertexWithUV(x + 1, y + height_XZPN, z, u_XZPN, v_XZPN);
        tessellator.addVertexWithUV(x, y + height_XZNN, z, u_XZNN, v_XZNN);
        tessellator.addVertexWithUV(x + 1, y + height_XZPN, z, u_XZPN, v_XZPN);
        tessellator.addVertexWithUV(x + 1, y + height_XZPP, z + 1, u_XZPP, v_XZPP);
        tessellator.addVertexWithUV(x, y + height_XZNP, z + 1, u_XZNP, v_XZNP);
    }

}
