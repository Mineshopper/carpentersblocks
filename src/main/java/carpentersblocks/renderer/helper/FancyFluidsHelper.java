package carpentersblocks.renderer.helper;

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
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FancyFluidsHelper {

    /**
     * Renders fancy fluid.
     */
    public static boolean render(TEBase TE, RenderBlocks renderBlocks, int x, int y, int z)
    {
        ItemStack itemStack = getFluidBlock(renderBlocks.blockAccess, x, y, z);

        if (itemStack != null) {

            Block block = BlockProperties.toBlock(itemStack);
            int metadata = itemStack.getItemDamage();

            if (block.getRenderBlockPass() == MinecraftForgeClient.getRenderPass())
            {
                if (!block.hasTileEntity(metadata) && metadata == 0)
                {
                    LightingHelper lightingHelper = new LightingHelper(renderBlocks);
                    lightingHelper.setupLightingYPos(itemStack, x, y, z);
                    lightingHelper.setupColor(x, y, z, 1, 16777215, null);
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
     * Returns nearby fluid block if one exists and can be routed to, otherwise null.
     */
    public static ItemStack getFluidBlock(IBlockAccess world, int x, int y, int z)
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

            Block block = world.getBlock(x + offsetXZ[idx][0], y, z + offsetXZ[idx][1]);

            if (block instanceof BlockLiquid || block instanceof IFluidBlock) {
                if (idx < 4) {
                    if (!world.isSideSolid(x, y, z, route[idx][0][0], false)) {
                        return new ItemStack(block, world.getBlockMetadata(x + offsetXZ[idx][0], y, z + offsetXZ[idx][1]));
                    }
                } else {
                    for (int routeIdx = 0; routeIdx < 2; ++routeIdx) {
                        if (!world.isSideSolid(x, y, z, route[idx][routeIdx][0], false)) {
                            int[] bridgeXZ = { x + route[idx][routeIdx][0].offsetX, z + route[idx][routeIdx][0].offsetZ };
                            if (!world.isSideSolid(bridgeXZ[0], y, bridgeXZ[1], route[idx][routeIdx][1], false) && !world.isSideSolid(bridgeXZ[0], y, bridgeXZ[1], route[idx][routeIdx][2], false)) {
                                return new ItemStack(block, world.getBlockMetadata(x + offsetXZ[idx][0], y, z + offsetXZ[idx][1]));
                            }
                        }
                    }
                }
            }

        }

        return null;
    }

}
