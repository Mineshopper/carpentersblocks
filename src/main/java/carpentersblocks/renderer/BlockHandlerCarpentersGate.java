package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Gate;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersGate extends BlockHandlerBase implements ISimpleBlockRenderingHandler {

    private boolean[] gate;
    private ForgeDirection dir;
    private boolean isOpen;
    private int type;

    private static final int YN = 0;
    private static final int YP = 1;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.0D, 0.3125D, 0.4375D, 0.125D, 1.0D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.875D, 0.3125D, 0.4375D, 1.0D, 1.0D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.125D, 0.5D, 0.4375D, 0.875D, 0.9375D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders gate
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        setParams(x, y, z);
        ItemStack itemStack = getCoverForRendering();

        switch (type) {
            case Gate.TYPE_PICKET:
                renderTypePicket(itemStack, x, y, z);
                break;
            case Gate.TYPE_PLANK_VERTICAL:
                renderTypeVerticalPlank(itemStack, x, y, z);
                break;
            case Gate.TYPE_WALL:
                renderTypeWall(itemStack, x, y, z);
                break;
            default:
                renderTypeVanilla(itemStack, x, y, z);
                break;
        }

        renderBlocks.renderAllFaces = false;
    }

    /**
     * Sets up commonly used fields.
     */
    private void setParams(int x, int y, int z)
    {
        type = Gate.getType(TE);
        isOpen = Gate.getState(TE) == Gate.STATE_OPEN;

        boolean[] tempGate = {
                renderBlocks.blockAccess.getBlock(x, y - 1, z).equals(srcBlock),
                renderBlocks.blockAccess.getBlock(x, y + 1, z).equals(srcBlock)
        };

        gate = tempGate;

        if (Gate.getFacing(TE) == Gate.FACING_ON_Z) {
            dir = Gate.getDirOpen(TE) == Gate.DIR_NEG ? ForgeDirection.NORTH : ForgeDirection.SOUTH;
        } else {
            dir = Gate.getDirOpen(TE) == Gate.DIR_NEG ? ForgeDirection.EAST : ForgeDirection.WEST;
        }
    }

    /**
     * Renders vanilla gate at given coordinates.
     */
    private void renderTypeVanilla(ItemStack itemStack, int x, int y, int z)
    {
        double yMin, yMax;
        double yOffset = type * 0.0625D;
        boolean singlePlank = type == Gate.TYPE_VANILLA_X3;

        yMin = gate[YN] ? 0.0D : 0.3125D - yOffset;

        renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.0D, 0.5625D, 1.0D, 0.125D, dir);
        renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.875D, 0.5625D, 1.0D, 1.0D, dir);

        if (isOpen) {

            if (!singlePlank) {

                yMin = gate[YN] ? 0.0D : 0.375D - yOffset;
                yMax = gate[YP] ? 1.0D : 0.9375D;

                renderBlockWithRotation(itemStack, x, y, z, 0.8125D, yMin, 0.0D, 0.9375D, yMax, 0.125D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.8125D, yMin, 0.875D, 0.9375D, yMax, 1.0D, dir);

                yMin = 0.375D - yOffset;

                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.0D, 0.8125D, 0.5625D, 0.125D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.875D, 0.8125D, 0.5625D, 1.0D, dir);

                yMin = 0.75D - yOffset;

                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.0D, 0.8125D, 0.9375D, 0.125D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.875D, 0.8125D, 0.9375D, 1.0D, dir);

            } else {

                yMin = gate[YN] ? 0.0D : 0.1875D;
                yMax = gate[YP] ? 1.0D : 0.9375D;

                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.0D, 0.9375D, yMax, 0.125D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.875D, 0.9375D, yMax, 1.0D, dir);

            }

        } else {

            if (!singlePlank) {

                yMin = gate[YN] ? 0.0D : 0.375D - yOffset;
                yMax = gate[YP] ? 1.0D : 0.9375D;

                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.375D, 0.5625D, yMax, 0.5D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.5D, 0.5625D, yMax, 0.625D, dir);

                yMin = 0.375D - yOffset;

                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.625D, 0.5625D, 0.5625D, 0.875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.125D, 0.5625D, 0.5625D, 0.375D, dir);

                yMin = 0.75D - yOffset;

                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.625D, 0.5625D, 0.9375D, 0.875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.125D, 0.5625D, 0.9375D, 0.375D, dir);

            } else {

                yMin = gate[YN] ? 0.0D : 0.375D - yOffset;
                yMax = gate[YP] ? 1.0D : 0.9375D;

                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, yMin, 0.125D, 0.5625D, yMax, 0.875D, dir);

            }

        }
    }

    /**
     * Renders picket gate at given coordinates.
     */
    private void renderTypePicket(ItemStack itemStack, int x, int y, int z)
    {
        double yMin, yMax;

        if (isOpen) {

            if (!gate[YP]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.625D, 0.0625D, 1.0D, 0.6875D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.625D, 0.8125D, 1.0D, 0.6875D, 0.9375D, dir);
            }
            if (!gate[YN]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.1875D, 0.0625D, 1.0D, 0.25D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.1875D, 0.8125D, 1.0D, 0.25D, 0.9375D, dir);
            }

            yMin = gate[YN] ? 0.0D : 0.0625D;
            yMax = gate[YP] ? 1.0D : 0.8125D;

            renderBlockWithRotation(itemStack, x, y, z, 0.5D, yMin, 0.0D, 0.5625D, yMax, 0.0625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, yMin, 0.1875D, 0.5625D, yMax, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, yMin, 0.75D, 0.5625D, yMax, 0.8125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, yMin, 0.9375D, 0.5625D, yMax, 1.0D, dir);

            yMax = gate[YP] ? 1.0D : 0.875D;

            renderBlockWithRotation(itemStack, x, y, z, 0.6875D, yMin, 0.0D, 0.8125D, yMax, 0.0625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.6875D, yMin, 0.1875D, 0.8125D, yMax, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.6875D, yMin, 0.75D, 0.8125D, yMax, 0.8125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.6875D, yMin, 0.9375D, 0.8125D, yMax, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.9375D, yMin, 0.0D, 1.0D, yMax, 0.0625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.9375D, yMin, 0.1875D, 1.0D, yMax, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.9375D, yMin, 0.75D, 1.0D, yMax, 0.8125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.9375D, yMin, 0.9375D, 1.0D, yMax, 1.0D, dir);

        } else {

            if (!gate[YP]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.625D, 0.0D, 0.5625D, 0.6875D, 1.0D, dir);
            }
            if (!gate[YN]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.1875D, 0.0D, 0.5625D, 0.25D, 1.0D, dir);
            }

            yMin = gate[YN] ? 0.0D : 0.0625D;
            yMax = gate[YP] ? 1.0D : 0.8125D;

            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.0D, 0.625D, yMax, 0.0625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.0D, 0.4375D, yMax, 0.0625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.9375D, 0.625D, yMax, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.9375D, 0.4375D, yMax, 1.0D, dir);

            yMax = gate[YP] ? 1.0D : 0.875D;

            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.1875D, 0.625D, yMax, 0.3125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.1875D, 0.4375D, yMax, 0.3125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.4375D, 0.625D, yMax, 0.5625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.4375D, 0.4375D, yMax, 0.5625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.6875D, 0.625D, yMax, 0.8125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.6875D, 0.4375D, yMax, 0.8125D, dir);

        }
    }

    /**
     * Renders vertical plank gate at given coordinates.
     */
    private void renderTypeVerticalPlank(ItemStack itemStack, int x, int y, int z)
    {
        if (isOpen) {

            if (!gate[YP]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.75D, 0.0625D, 1.0D, 0.875D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.75D, 0.8125D, 1.0D, 0.875D, 0.9375D, dir);
            }
            if (!gate[YN]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.125D, 0.0625D, 1.0D, 0.25D, 0.1875D, dir);
                renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.125D, 0.8125D, 1.0D, 0.25D, 0.9375D, dir);
            }

            renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.0D, 0.0D, 0.6875D, 1.0D, 0.0625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.0D, 0.1875D, 0.6875D, 1.0D, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.0D, 0.75D, 0.6875D, 1.0D, 0.8125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.0D, 0.9375D, 0.6875D, 1.0D, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 0.0625D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.1875D, 1.0D, 1.0D, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.75D, 1.0D, 1.0D, 0.8125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.9375D, 1.0D, 1.0D, 1.0D, dir);

        } else {

            if (!gate[YP]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.75D, 0.0D, 0.5625D, 0.875D, 1.0D, dir);
            }
            if (!gate[YN]) {
                renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.125D, 0.0D, 0.5625D, 0.25D, 1.0D, dir);
            }

            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, 0.0D, 0.0D, 0.625D, 1.0D, 0.1875D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0D, 0.0D, 0.4375D, 1.0D, 0.1875D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, 0.0D, 0.3125D, 0.625D, 1.0D, 0.6875D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0D, 0.3125D, 0.4375D, 1.0D, 0.6875D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5625D, 0.0D, 0.8125D, 0.625D, 1.0D, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.375D, 0.0D, 0.8125D, 0.4375D, 1.0D, 1.0D, dir);

        }
    }

    /**
     * Renders wall gate at given coordinates.
     */
    private void renderTypeWall(ItemStack itemStack, int x, int y, int z)
    {
        double yMax = gate[YP] ? 1.0D : 0.8125D;

        if (isOpen) {
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.0D, 0.0D, 1.0D, yMax, 0.125D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.5D, 0.0D, 0.875D, 1.0D, yMax, 1.0D, dir);
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.0D, 0.0D, 0.5625D, yMax, 1.0D, dir);
        }
    }

}
