package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import carpentersblocks.data.Ladder;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersLadder extends BlockHandlerBase {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        // Left vertical support
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.375D, 0.125D, 1.0D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        // Right vertical support
        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        // Bottom slat
        renderBlocks.setRenderBounds(0.125D, 0.125D, 0.4375D, 0.875D, 0.1875D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        // Next slat
        renderBlocks.setRenderBounds(0.125D, 0.375D, 0.4375D, 0.875D, 0.4375D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        // Next slat
        renderBlocks.setRenderBounds(0.125D, 0.625D, 0.4375D, 0.875D, 0.6875D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        // Top slat
        renderBlocks.setRenderBounds(0.125D, 0.875D, 0.4375D, 0.875D, 0.9375D, 0.5625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders ladder.
     */
    protected boolean renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;
        disableAO = true;

        Block block = BlockProperties.getCoverBlock(TE, 6);
        int data = BlockProperties.getData(TE);

        double xLow = 0.0D;
        double xHigh = 1.0D;
        double zLow = 0.0D;
        double zHigh = 1.0D;

        /* Gather adjacent ladder metadata. */

        boolean connects_XN = TE.worldObj.getBlockId(x - 1, y, z) == srcBlock.blockID && BlockProperties.getData((TEBase)TE.worldObj.getBlockTileEntity(x - 1, y, z)) == data;
        boolean connects_XP = TE.worldObj.getBlockId(x + 1, y, z) == srcBlock.blockID && BlockProperties.getData((TEBase)TE.worldObj.getBlockTileEntity(x + 1, y, z)) == data;
        boolean connects_ZN = TE.worldObj.getBlockId(x, y, z - 1) == srcBlock.blockID && BlockProperties.getData((TEBase)TE.worldObj.getBlockTileEntity(x, y, z - 1)) == data;
        boolean connects_ZP = TE.worldObj.getBlockId(x, y, z + 1) == srcBlock.blockID && BlockProperties.getData((TEBase)TE.worldObj.getBlockTileEntity(x, y, z + 1)) == data;

        switch (data) {
            case Ladder.FACING_ON_X:

                // Side supports
                if (!connects_XN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.375D, 0.125D, 1.0D, 0.625D);
                    renderBlock(block, x, y, z);
                }
                if (!connects_XP) {
                    renderBlocks.setRenderBounds(0.875D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
                    renderBlock(block, x, y, z);
                }

                xLow  = connects_XN ? 0.0D : 0.125D;
                xHigh = connects_XP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(xLow, 0.125D, 0.4375D, xHigh, 0.1875D, 0.5625D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.375D, 0.4375D, xHigh, 0.4375D, 0.5625D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.625D, 0.4375D, xHigh, 0.6875D, 0.5625D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.875D, 0.4375D, xHigh, 0.9375D, 0.5625D);
                renderBlock(block, x, y, z);

                break;
            case Ladder.FACING_ON_Z:

                // Side supports
                if (!connects_ZN) {
                    renderBlocks.setRenderBounds(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.125D);
                    renderBlock(block, x, y, z);
                }
                if (!connects_ZP) {
                    renderBlocks.setRenderBounds(0.375D, 0.0D, 0.875D, 0.625D, 1.0D, 1.0D);
                    renderBlock(block, x, y, z);
                }

                zLow  = connects_ZN ? 0.0D : 0.125D;
                zHigh = connects_ZP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(0.4375D, 0.125D, zLow, 0.5625D, 0.1875D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.4375D, 0.375D, zLow, 0.5625D, 0.4375D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.4375D, 0.625D, zLow, 0.5625D, 0.6875D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.4375D, 0.875D, zLow, 0.5625D, 0.9375D, zHigh);
                renderBlock(block, x, y, z);

                break;
            case Ladder.FACING_NORTH: // Ladder on +Z

                // Side supports
                if (!connects_XN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.8125D, 0.125D, 1.0D, 1.0D);
                    renderBlock(block, x, y, z);
                }
                if (!connects_XP) {
                    renderBlocks.setRenderBounds(0.875D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
                    renderBlock(block, x, y, z);
                }

                xLow  = connects_XN ? 0.0D : 0.125D;
                xHigh = connects_XP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(xLow, 0.125D, 0.875D, xHigh, 0.1875D, 1.0D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.375D, 0.875D, xHigh, 0.4375D, 1.0D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.625D, 0.875D, xHigh, 0.6875D, 1.0D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.875D, 0.875D, xHigh, 0.9375D, 1.0D);
                renderBlock(block, x, y, z);

                break;
            case Ladder.FACING_SOUTH: // Ladder on -Z

                // Side supports
                if (!connects_XN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 0.1875D);
                    renderBlock(block, x, y, z);
                }
                if (!connects_XP) {
                    renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
                    renderBlock(block, x, y, z);
                }

                xLow  = connects_XN ? 0.0D : 0.125D;
                xHigh = connects_XP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(xLow, 0.125D, 0.0D, xHigh, 0.1875D, 0.1875D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.375D, 0.0D, xHigh, 0.4375D, 0.1875D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.625D, 0.0D, xHigh, 0.6875D, 0.1875D);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(xLow, 0.875D, 0.0D, xHigh, 0.9375D, 0.1875D);
                renderBlock(block, x, y, z);

                break;
            case Ladder.FACING_WEST: // Ladder on +X

                // Side supports
                if (!connects_ZN) {
                    renderBlocks.setRenderBounds(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
                    renderBlock(block, x, y, z);
                }
                if (!connects_ZP) {
                    renderBlocks.setRenderBounds(0.8125D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
                    renderBlock(block, x, y, z);
                }

                zLow  = connects_ZN ? 0.0D : 0.125D;
                zHigh = connects_ZP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(0.875D, 0.125D, zLow, 1.0D, 0.1875D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.875D, 0.375D, zLow, 1.0D, 0.4375D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.875D, 0.625D, zLow, 1.0D, 0.6875D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.875D, 0.875D, zLow, 1.0D, 0.9375D, zHigh);
                renderBlock(block, x, y, z);

                break;
            case Ladder.FACING_EAST: // Ladder on -X

                // Side supports
                if (!connects_ZN) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 0.125D);
                    renderBlock(block, x, y, z);
                }
                if (!connects_ZP) {
                    renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.1875D, 1.0D, 1.0D);
                    renderBlock(block, x, y, z);
                }

                zLow  = connects_ZN ? 0.0D : 0.125D;
                zHigh = connects_ZP ? 1.0D : 0.875D;

                // Slats
                renderBlocks.setRenderBounds(0.0D, 0.125D, zLow, 0.1875D, 0.1875D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.0D, 0.375D, zLow, 0.1875D, 0.4375D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.0D, 0.625D, zLow, 0.1875D, 0.6875D, zHigh);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.0D, 0.875D, zLow, 0.1875D, 0.9375D, zHigh);
                renderBlock(block, x, y, z);

                break;
        }

        disableAO = false;
        renderBlocks.renderAllFaces = false;

        return true;
    }

}
