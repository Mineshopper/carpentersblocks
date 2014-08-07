package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Hatch;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersHatch extends BlockHandlerHinged {

    private ForgeDirection baseDir;
    private boolean isHigh;
    private int type;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.0D, 0.4375D, 0.0D, 1.0D, 0.5625D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.5625D, 0.375D, 0.125D, 0.625D, 0.4375D);
        super.renderInventoryBlock(Block.blockIron, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625, 0.5625D, 0.5625D, 0.125D, 0.625D, 0.625D);
        super.renderInventoryBlock(Block.blockIron, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.625D, 0.375D, 0.125D, 0.6875D, 0.625D);
        super.renderInventoryBlock(Block.blockIron, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders block at coordinates.
     */
    public void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        setParams();
        ItemStack itemStack = getCoverForRendering();

        switch (type) {
            case Hatch.TYPE_WINDOW:
            case Hatch.TYPE_SCREEN:
                renderTypeFrame(itemStack, x, y, z);
                break;
            case Hatch.TYPE_FRENCH_WINDOW:
                renderTypeFrench(itemStack, x, y, z);
                break;
            case Hatch.TYPE_PANEL:
                renderTypePanel(itemStack, x, y, z);
                break;
            case Hatch.TYPE_HIDDEN:
                renderTypeHidden(itemStack, x, y, z);
                break;
        }

        renderBlocks.renderAllFaces = false;
    }

    /**
     * Sets up commonly used fields.
     */
    private void setParams()
    {
        type = Hatch.getType(TE);
        isHigh = Hatch.getPos(TE) == Hatch.POSITION_HIGH;
        isOpen = Hatch.getState(TE) == Hatch.STATE_OPEN;

        switch (Hatch.getDir(TE)) {
            case Hatch.DIR_Z_NEG:
                side = baseDir = ForgeDirection.SOUTH;
                break;
            case Hatch.DIR_Z_POS:
                side = baseDir = ForgeDirection.NORTH;
                break;
            case Hatch.DIR_X_NEG:
                side = baseDir = ForgeDirection.EAST;
                break;
            case Hatch.DIR_X_POS:
                side = baseDir = ForgeDirection.WEST;
                break;
        }

        if (!isOpen) {
            side = isHigh ? ForgeDirection.UP : ForgeDirection.DOWN;
        }
    }

    /**
     * Renders hidden hatch at given coordinates.
     */
    private void renderTypeHidden(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D, side);
        renderPartHandle(new ItemStack(Block.blockIron), x, y, z, true, false);
    }

    /**
     * Renders standard 3x3 outer frame.
     */
    private void renderPartFrame(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.8125D, 0.1875D, 1.0D, 1.0D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D, 1.0D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D, 1.0D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D, side);
    }

    /**
     * Renders a window or screen hatch at given coordinates.
     */
    private void renderTypeFrame(ItemStack itemStack, int x, int y, int z)
    {
        renderPartFrame(itemStack, x, y, z);
        renderPartPane(type == Hatch.TYPE_SCREEN ? IconRegistry.icon_hatch_screen : IconRegistry.icon_hatch_glass, x, y, z);
        renderPartHandle(new ItemStack(Block.blockIron), x, y, z, true, true);
    }

    /**
     * Renders a French window hatch at given coordinates.
     */
    private void renderTypeFrench(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.4375D, 0.875D, 0.8125D, 0.5625D, 0.9375D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.1875D, 0.875D, 0.5625D, 0.4375D, 0.9375D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.5625D, 0.875D, 0.5625D, 0.8125D, 0.9375D, side);
        renderPartFrame(itemStack, x, y, z);
        renderPartPane(IconRegistry.icon_hatch_french_glass, x, y, z);
        renderPartHandle(new ItemStack(Block.blockIron), x, y, z, true, true);
    }

    /**
     * Renders a panel hatch at given coordinates.
     */
    private void renderTypePanel(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.1875D, 0.875D, 0.8215D, 0.8215D, 0.9375D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.3125D, 0.3125D, 0.8125D, 0.6875D, 0.6875D, 1.0D, side);
        renderPartFrame(itemStack, x, y, z);
        renderPartHandle(new ItemStack(Block.blockIron), x, y, z, true, true);
    }

    /**
     * Renders a hatch handle for the given coordinates.
     */
    private void renderPartHandle(ItemStack itemStack, int x, int y, int z, boolean renderInterior, boolean renderExterior)
    {
        int blockRenderPass = Block.blocksList[itemStack.itemID].getRenderBlockPass();

        if (renderPass == blockRenderPass) {

            if (!renderInterior && !renderExterior) {
                return;
            }

            suppressDyeColor = true;
            suppressChiselDesign = true;
            suppressOverlay = true;

            double zOffset = Hatch.getType(TE) == Hatch.TYPE_HIDDEN ? 0.125D : 0.1875D;
            double yMin = isHigh ? 0.0625D : 0.875D;
            double yMax = isHigh ? 0.125D : 0.9375D;

            if (renderInterior) {

                if (isOpen) {

                    ForgeDirection dir = side.getOpposite();
                    int xTemp = x - dir.offsetX;
                    int yTemp = y - dir.offsetY;
                    int zTemp = z - dir.offsetZ;

                    renderBlockWithRotation(itemStack, xTemp, yTemp, zTemp, 0.375D, yMin, 0.9375D, 0.4375D, yMax, 1.0D, dir);
                    renderBlockWithRotation(itemStack, xTemp, yTemp, zTemp, 0.5625D, yMin, 0.9375D, 0.625D, yMax, 1.0D, dir);
                    renderBlockWithRotation(itemStack, xTemp, yTemp, zTemp, 0.375D, yMin, 0.875D, 0.625D, yMax, 0.9375D, dir);

                } else {

                    ForgeDirection dir = isHigh ? baseDir : baseDir.getOpposite();

                    renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.9375D - zOffset, 0.4375D, yMax, 1.0D - zOffset, side, dir);
                    renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.9375D - zOffset, 0.625D, yMax, 1.0D - zOffset, side, dir);
                    renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.875D - zOffset, 0.625D, yMax, 0.9375D - zOffset, side, dir);

                }
            }

            if (renderExterior) {

                if (isOpen) {

                    renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.9375D - zOffset, 0.4375D, yMax, 1.0D - zOffset, baseDir);
                    renderBlockWithRotation(itemStack, x, y, z, 0.5625D, yMin, 0.9375D - zOffset, 0.625D, yMax, 1.0D - zOffset, baseDir);
                    renderBlockWithRotation(itemStack, x, y, z, 0.375D, yMin, 0.875D - zOffset, 0.625D, yMax, 0.9375D - zOffset, baseDir);

                } else {

                    ForgeDirection dir1 = side.getOpposite();
                    ForgeDirection dir2 = !isHigh ? baseDir.getOpposite() : baseDir;
                    int xTemp = x - dir1.offsetX;
                    int yTemp = y - dir1.offsetY;
                    int zTemp = z - dir1.offsetZ;

                    renderBlockWithRotation(itemStack, xTemp, yTemp, zTemp, 0.375D, yMin, 0.9375D, 0.4375D, yMax, 1.0D, dir1, dir2);
                    renderBlockWithRotation(itemStack, xTemp, yTemp, zTemp, 0.5625D, yMin, 0.9375D, 0.625D, yMax, 1.0D, dir1, dir2);
                    renderBlockWithRotation(itemStack, xTemp, yTemp, zTemp, 0.375D, yMin, 0.875D, 0.625D, yMax, 0.9375D, dir1, dir2);

                }

            }

            suppressDyeColor = false;
            suppressChiselDesign = false;
            suppressOverlay = false;

        }
    }

}
