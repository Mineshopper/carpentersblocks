package com.carpentersblocks.renderer.helper;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;
import org.apache.logging.log4j.Level;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.registry.FeatureRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FancyFluidsHelper {

    public final static Class[] liquidClasses = { BlockLiquid.class, IFluidBlock.class};
    private final static int CALLER_SUN = 0;
    private final static int CALLER_SEC = 1;
    private static int callMethod = -1;

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
     * Renders fancy fluid.
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
        ItemStack itemStack = getFluidBlock(renderBlocks.blockAccess, x, y, z);

        if (itemStack != null) {

            Block block = BlockProperties.toBlock(itemStack);
            int metadata = itemStack.getItemDamage();

            if (block.getRenderBlockPass() == MinecraftForgeClient.getRenderPass())
            {
                if (!block.hasTileEntity(metadata))
                {
                    LightingHelper lightingHelper = new LightingHelper(renderBlocks);

                    // Set to liquid metadata for accurate brightness and color
                    TE.setMetadata(metadata);
                    lightingHelper.setupLightingYPos(itemStack, x, y, z);
                    int color = block.colorMultiplier(TE.getWorldObj(), x, y, z);
                    lightingHelper.setupColor(x, y, z, 1, color, null);
                    TE.restoreMetadata();

                    double fluidHeight = (block instanceof BlockLiquid ? 1.0D - 1.0F / 9.0F : 0.875F) - 0.0010000000474974513D;
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, fluidHeight, 1.0D);
                    RenderHelper.renderFaceYPos(renderBlocks, x, y, z, block.getIcon(1, metadata));

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
            Class clazz = block.getClass();

            boolean isLiquid = false;
            for (int idx1 = 0; idx1 < liquidClasses.length; ++idx1) {
                if (liquidClasses[idx1].isAssignableFrom(clazz)) {
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

}
