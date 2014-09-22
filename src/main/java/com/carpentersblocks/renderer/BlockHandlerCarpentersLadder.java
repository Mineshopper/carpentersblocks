package com.carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Ladder;
import com.carpentersblocks.tileentity.TEBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersLadder extends BlockHandlerBase {

    private Ladder data = new Ladder();
    private ItemStack iron = new ItemStack(Blocks.iron_block);
    private ForgeDirection dir;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        /* Sides */

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.375D, 0.125D, 1.0D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        /* Steps */

        renderBlocks.setRenderBounds(0.125D, 0.125D, 0.4375D, 0.875D, 0.1875D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.125D, 0.375D, 0.4375D, 0.875D, 0.4375D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.125D, 0.625D, 0.4375D, 0.875D, 0.6875D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.125D, 0.875D, 0.4375D, 0.875D, 0.9375D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders ladder.
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        ItemStack itemStack = getCoverForRendering();
        dir = data.getDirection(TE);

        switch (data.getType(TE)) {
            case Ladder.TYPE_DEFAULT:
                renderTypeDefaultClassic(itemStack, x, y, z);
                //renderTypeDefault(itemStack, x, y, z);
                break;
            case Ladder.TYPE_RAIL:
                renderTypeRail(itemStack, x, y, z);
                break;
            case Ladder.TYPE_POLE:
                renderTypePole(itemStack, x, y, z);
                break;
        }

        renderBlocks.renderAllFaces = false;
    }

    /**
     * Renders a classic default ladder at coordinates.
     * <p>
     * The classic type connects to adjacent ladders, and is
     * retained for the look.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypeDefaultClassic(ItemStack itemStack, int x, int y, int z)
    {
        double xLow = 0.0D;
        double xHigh = 1.0D;
        double zLow = 0.0D;
        double zHigh = 1.0D;

        /* Gather adjacent ladder metadata. */

        World world = TE.getWorldObj();

        boolean connects_XN = world.getBlock(x - 1, y, z).equals(srcBlock) && data.getDirection((TEBase) world.getTileEntity(x - 1, y, z)).equals(dir);
        boolean connects_XP = world.getBlock(x + 1, y, z).equals(srcBlock) && data.getDirection((TEBase) world.getTileEntity(x + 1, y, z)).equals(dir);
        boolean connects_ZN = world.getBlock(x, y, z - 1).equals(srcBlock) && data.getDirection((TEBase) world.getTileEntity(x, y, z - 1)).equals(dir);
        boolean connects_ZP = world.getBlock(x, y, z + 1).equals(srcBlock) && data.getDirection((TEBase) world.getTileEntity(x, y, z + 1)).equals(dir);

        switch (dir) {
            case DOWN: // Ladder.DIR_ON_X

                // Side supports
                if (!connects_XN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.375D, 0.125D, 1.0D, 0.625D);
                    renderBlock(itemStack, x, y, z);
                }
                if (!connects_XP) {
                    renderBlocks.setRenderBounds(0.875D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
                    renderBlock(itemStack, x, y, z);
                }

                xLow  = connects_XN ? 0.0D : 0.125D;
                xHigh = connects_XP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(xLow, 0.125D, 0.4375D, xHigh, 0.1875D, 0.5625D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.375D, 0.4375D, xHigh, 0.4375D, 0.5625D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.625D, 0.4375D, xHigh, 0.6875D, 0.5625D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.875D, 0.4375D, xHigh, 0.9375D, 0.5625D);
                renderBlock(itemStack, x, y, z);

                break;
            case UP: // Ladder.DIR_ON_Z

                // Side supports
                if (!connects_ZN) {
                    renderBlocks.setRenderBounds(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                }
                if (!connects_ZP) {
                    renderBlocks.setRenderBounds(0.375D, 0.0D, 0.875D, 0.625D, 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                }

                zLow  = connects_ZN ? 0.0D : 0.125D;
                zHigh = connects_ZP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(0.4375D, 0.125D, zLow, 0.5625D, 0.1875D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.4375D, 0.375D, zLow, 0.5625D, 0.4375D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.4375D, 0.625D, zLow, 0.5625D, 0.6875D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.4375D, 0.875D, zLow, 0.5625D, 0.9375D, zHigh);
                renderBlock(itemStack, x, y, z);

                break;
            case NORTH:

                // Side supports
                if (!connects_XN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.8125D, 0.125D, 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                }
                if (!connects_XP) {
                    renderBlocks.setRenderBounds(0.875D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                }

                xLow  = connects_XN ? 0.0D : 0.125D;
                xHigh = connects_XP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(xLow, 0.125D, 0.875D, xHigh, 0.1875D, 1.0D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.375D, 0.875D, xHigh, 0.4375D, 1.0D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.625D, 0.875D, xHigh, 0.6875D, 1.0D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.875D, 0.875D, xHigh, 0.9375D, 1.0D);
                renderBlock(itemStack, x, y, z);

                break;
            case SOUTH:

                // Side supports
                if (!connects_XN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 0.1875D);
                    renderBlock(itemStack, x, y, z);
                }
                if (!connects_XP) {
                    renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
                    renderBlock(itemStack, x, y, z);
                }

                xLow  = connects_XN ? 0.0D : 0.125D;
                xHigh = connects_XP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(xLow, 0.125D, 0.0D, xHigh, 0.1875D, 0.1875D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.375D, 0.0D, xHigh, 0.4375D, 0.1875D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.625D, 0.0D, xHigh, 0.6875D, 0.1875D);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.875D, 0.0D, xHigh, 0.9375D, 0.1875D);
                renderBlock(itemStack, x, y, z);

                break;
            case WEST:

                // Side supports
                if (!connects_ZN) {
                    renderBlocks.setRenderBounds(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                }
                if (!connects_ZP) {
                    renderBlocks.setRenderBounds(0.8125D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                }

                zLow  = connects_ZN ? 0.0D : 0.125D;
                zHigh = connects_ZP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(0.875D, 0.125D, zLow, 1.0D, 0.1875D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.875D, 0.375D, zLow, 1.0D, 0.4375D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.875D, 0.625D, zLow, 1.0D, 0.6875D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.875D, 0.875D, zLow, 1.0D, 0.9375D, zHigh);
                renderBlock(itemStack, x, y, z);

                break;
            case EAST:

                // Side supports
                if (!connects_ZN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                }
                if (!connects_ZP) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.1875D, 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                }

                zLow  = connects_ZN ? 0.0D : 0.125D;
                zHigh = connects_ZP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(0.0D, 0.125D, zLow, 0.1875D, 0.1875D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.0D, 0.375D, zLow, 0.1875D, 0.4375D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.0D, 0.625D, zLow, 0.1875D, 0.6875D, zHigh);
                renderBlock(itemStack, x, y, z);
                renderBlocks.setRenderBounds(0.0D, 0.875D, zLow, 0.1875D, 0.9375D, zHigh);
                renderBlock(itemStack, x, y, z);

                break;
            default: {}
        }
    }

    /**
     * Renders a default ladder at coordinates.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypeDefault(ItemStack itemStack, int x, int y, int z)
    {
        ForgeDirection axisDir = ForgeDirection.SOUTH;

        switch (dir) {
            case DOWN: // DIR_ON_X
                axisDir = ForgeDirection.WEST;
            case UP: // DIR_ON_Z
                renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.125D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0D, 0.875D, 0.625D, 1.0D, 1.0D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.125D, 0.125D, 0.5625D, 0.1875D, 0.875D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.375D, 0.125D, 0.5625D, 0.4375D, 0.875D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.625D, 0.125D, 0.5625D, 0.6875D, 0.875D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.875D, 0.125D, 0.5625D, 0.9375D, 0.875D, axisDir);
                break;
            default:
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.125D, 0.0D, 0.875D, 0.1875D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.375D, 0.0D, 0.875D, 0.4375D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.625D, 0.0D, 0.875D, 0.6875D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.875D, 0.0D, 0.875D, 0.9375D, 0.1875D, dir);
                break;
        }
    }

    /**
     * Renders a rail ladder at coordinates.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypeRail(ItemStack itemStack, int x, int y, int z)
    {
        ForgeDirection axisDir = ForgeDirection.SOUTH;

        switch (dir) {
            case DOWN: // DIR_ON_X
                axisDir = ForgeDirection.WEST;
            case UP: // DIR_ON_Z
                renderBlockWithRotation(iron, x, y, z, 0.3125D, 0.0D, 0.1875D, 0.375D, 1.0D, 0.25D, axisDir);
                renderBlockWithRotation(iron, x, y, z, 0.3125D, 0.0D, 0.1875D, 0.375D, 1.0D, 0.25D, axisDir.getOpposite());
                renderBlockWithRotation(iron, x, y, z, 0.375D, 0.0D, 0.125D, 0.4375D, 1.0D, 0.25D, axisDir);
                renderBlockWithRotation(iron, x, y, z, 0.375D, 0.0D, 0.125D, 0.4375D, 1.0D, 0.25D, axisDir.getOpposite());
                renderBlockWithRotation(iron, x, y, z, 0.625D, 0.0D, 0.1875D, 0.6875D, 1.0D, 0.25D, axisDir);
                renderBlockWithRotation(iron, x, y, z, 0.625D, 0.0D, 0.1875D, 0.6875D, 1.0D, 0.25D, axisDir.getOpposite());
                renderBlockWithRotation(iron, x, y, z, 0.5625D, 0.0D, 0.125D, 0.625D, 1.0D, 0.25D, axisDir);
                renderBlockWithRotation(iron, x, y, z, 0.5625D, 0.0D, 0.125D, 0.625D, 1.0D, 0.25D, axisDir.getOpposite());
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.125D, 0.0D, 0.5625D, 0.1875D, 1.0D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.375D, 0.0D, 0.5625D, 0.4375D, 1.0D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.625D, 0.0D, 0.5625D, 0.6875D, 1.0D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.875D, 0.0D, 0.5625D, 0.9375D, 1.0D, axisDir);
                break;
            default:
                renderBlockWithRotation(iron, x, y, z, 0.125D, 0.0D, 0.0625D, 0.25D, 1.0D, 0.125D, dir);
                renderBlockWithRotation(iron, x, y, z, 0.1875D, 0.0D, 0.125D, 0.25D, 1.0D, 0.1875D, dir);
                renderBlockWithRotation(iron, x, y, z, 0.75D, 0.0D, 0.0625D, 0.875D, 1.0D, 0.125D, dir);
                renderBlockWithRotation(iron, x, y, z, 0.75D, 0.0D, 0.125D, 0.8125D, 1.0D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0625D, 0.0D, 1.0D, 0.1875D, 0.0625D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.3125D, 0.0D, 1.0D, 0.4375D, 0.0625D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.5625D, 0.0D, 1.0D, 0.6875D, 0.0625D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.8125D, 0.0D, 1.0D, 0.9375D, 0.0625D, dir);
                break;
        }
    }

    /**
     * Renders a pole ladder at coordinates.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypePole(ItemStack itemStack, int x, int y, int z)
    {
        ForgeDirection axisDir = ForgeDirection.SOUTH;

        switch (dir) {
            case DOWN: // DIR_ON_X
                axisDir = ForgeDirection.WEST;
            case UP: // DIR_ON_Z
                renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D); // Pole
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.125D, 0.625D, 0.5625D, 0.1875D, 0.9375D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.1875D, 0.875D, 0.5625D, 0.25D, 0.9375D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.75D, 0.0625D, 0.5625D, 0.8125D, 0.375D, axisDir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.8125D, 0.0625D, 0.5625D, 0.875D, 0.125D, axisDir);
                break;
            default:
                for (double yLow = 0.125D; yLow < 1.0D; yLow += 0.25D) {
                    double yHigh = yLow + 0.0625D;
                    renderBlockWithRotation(itemStack, x, y, z, 0.125D, yLow, 0.0D, 0.1875D, yHigh, 0.125D, dir); // xLow nub
                    renderBlockWithRotation(itemStack, x, y, z, 0.8125D, yLow, 0.0D, 0.875D, yHigh, 0.125D, dir); // xHigh nub
                    renderBlockWithRotation(itemStack, x, y, z, 0.125D, yLow, 0.125D, 0.875D, yHigh, 0.1875D, dir); // Full width
                }
                break;
        }
    }

}
