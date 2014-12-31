package com.carpentersblocks.renderer;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Bed;
import com.carpentersblocks.renderer.helper.VertexHelper;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DyeHandler;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersBed extends BlockHandlerBase {

    private IIcon[] icon_design;

    private ForgeDirection dir;
    private boolean isHead;
    private boolean hasDesign;
    private boolean bedParallelNeg;
    private boolean bedParallelPos;
    private boolean isOccupied;

    private TEBase TE_head;
    private TEBase TE_foot;

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    private boolean getIsParallelPos()
    {
        if (dir.ordinal() < 4 && renderBlocks.blockAccess.getBlock(TE.xCoord + 1, TE.yCoord, TE.zCoord).equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else if (renderBlocks.blockAccess.getBlock(TE.xCoord, TE.yCoord, TE.zCoord + 1).equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else {
            return false;
        }
    }

    private boolean getIsParallelNeg()
    {
        if (dir.ordinal() < 4 && renderBlocks.blockAccess.getBlock(TE.xCoord - 1, TE.yCoord, TE.zCoord).equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else if (renderBlocks.blockAccess.getBlock(TE.xCoord, TE.yCoord, TE.zCoord - 1).equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else {
            return false;
        }
    }

    @Override
    /**
     * Renders bed
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        // Continue only if bed is complete
        if (Bed.getOppositeTE(TE) == null) {
            return;
        }

        setParams(); // Set up common be parameters

        // Render mattress, pillow and blanket

        renderFabricComponents(new ItemStack(Blocks.wool), x, y, z);

        // Render frame with dye override

        if (TE_head.hasAttribute(TE_head.ATTR_DYE[6])) {
            setDyeOverride(DyeHandler.getColor(TE_head.getAttribute(TE_head.ATTR_DYE[6])));
        } else {
            suppressDyeColor = true;
        }

        switch (Bed.getType(TE)) {
            case Bed.TYPE_NORMAL:
                renderNormalFrame(getCoverForRendering(), x, y, z);
                break;
        }

        clearDyeOverride();
        suppressDyeColor = false;
        renderBlocks.renderAllFaces = false;
    }

    /**
     * Sets up commonly used fields.
     */
    private void setParams()
    {
        dir = Bed.getDirection(TE);
        isHead = Bed.isHeadOfBed(TE);
        hasDesign = TE.hasDesign();
        bedParallelPos = getIsParallelPos();
        bedParallelNeg = getIsParallelNeg();

        if (isHead) {
            TE_head = TE;
            TE_foot = Bed.getOppositeTE(TE);
        } else {
            TE_head = Bed.getOppositeTE(TE);
            TE_foot = TE;
        }

        // Occupied state could (maybe) not be synchronized
        isOccupied = Bed.isOccupied(TE_head) || Bed.isOccupied(TE_foot);

        if (hasDesign) {
            icon_design = IconRegistry.icon_design_bed.get(DesignHandler.listBed.indexOf(TE.getDesign()));
        }
    }

    /**
     * Renders fabric components of bed.
     */
    private void renderFabricComponents(ItemStack itemStack, int x, int y, int z)
    {
        suppressDyeColor = true;
        suppressOverlay = true;
        suppressChiselDesign = true;

        int[] rotateTop = { 2, 0, 1, 3 };
        renderBlocks.uvRotateTop = renderBlocks.uvRotateBottom = rotateTop[dir.ordinal() - 2];
        renderPillow(itemStack, x, y, z);
        renderBlanket(itemStack, x, y, z);
        renderBlocks.uvRotateTop = renderBlocks.uvRotateBottom = 0;
        renderMattress(itemStack, x, y, z);

        suppressDyeColor = false;
        suppressOverlay = false;
        suppressChiselDesign = false;
    }

    /**
     * Renders pillow.
     */
    private void renderPillow(ItemStack itemStack, int x, int y, int z)
    {
        if (isHead) {
            IIcon icon_pillow = hasDesign ? icon_design[0] : IconRegistry.icon_bed_pillow;
            setIconOverride(6, icon_pillow);
            renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.5625D, 0.1875D, 0.875D, 0.6875D, 0.5625D, dir);
            clearIconOverride(6);
        }
    }

    /**
     * Renders blanket.
     */
    private void renderBlanket(ItemStack itemStack, int x, int y, int z)
    {
        VertexHelper.setFloatingIconLock();

        if (hasDesign)
        {
            int[] idxHead = { 2, 2, 2, 7, 1, 3 };
            int[] idxFoot = { 5, 5, 2, 7, 4, 6 };
            int[][] idxRot = { { 3, 2, 5, 4 }, { 2, 3, 4, 5 }, { 4, 5, 3, 2 }, { 5, 4, 2, 3 } };
            int valDir = dir.ordinal() - 2;

            /** 0 = head, 1 = foot */
            IIcon[][] icon = {
                {
                    icon_design[idxHead[0]],
                    icon_design[idxHead[1]],
                    icon_design[idxHead[idxRot[valDir][0]]],
                    icon_design[idxHead[idxRot[valDir][1]]],
                    icon_design[idxHead[idxRot[valDir][2]]],
                    icon_design[idxHead[idxRot[valDir][3]]]
                },
                {
                    icon_design[idxFoot[0]],
                    icon_design[idxFoot[1]],
                    icon_design[idxFoot[idxRot[valDir][0]]],
                    icon_design[idxFoot[idxRot[valDir][1]]],
                    icon_design[idxFoot[idxRot[valDir][2]]],
                    icon_design[idxFoot[idxRot[valDir][3]]]
                }
            };

            int idx = isHead ? 0 : 1;
            for (int side = 0; side < 6; ++side) {
                setIconOverride(side, icon[idx][side]);
            }
        }

        double yTop = isOccupied ? 0.875D : 0.625D;
        double depth = 0.3125F;

        // Color the blanket
        int dyeColor = TE_foot.hasAttribute(TE_foot.ATTR_DYE[coverRendering]) ? DyeHandler.getVanillaDmgValue(TE_foot.getAttribute(TE_foot.ATTR_DYE[coverRendering])) : 0;
        itemStack.setItemDamage(dyeColor);

        if (isHead) {
            renderBlockWithRotation(itemStack, x, y, z, 0.0625D, yTop - 0.0625D, 0.5D, 0.9375D, yTop, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, yTop - depth, 0.5D, 0.0625D, yTop, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.9375D, yTop - depth, 0.5D, 1.0D, yTop, 1.0D, dir);
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.0625D, yTop - 0.0625D, 0.0D, 0.9375D, yTop, 0.9375D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, yTop - depth, 0.0D, 0.0625D, yTop, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.9375D, yTop - depth, 0.0D, 1.0D, yTop, 1.0D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.0625D, yTop - depth, 0.9375D, 0.9375D, yTop, 1.0D, dir);
        }

        itemStack.setItemDamage(15);
        clearIconOverride(6);
        VertexHelper.clearFloatingIconLock();
    }

    /**
     * Renders mattress.
     */
    private void renderMattress(ItemStack itemStack, int x, int y, int z)
    {
        itemStack.setItemDamage(0);

        switch (dir) {
            case NORTH:
                renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, isHead ? 0.0D : 0.0625D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, isHead ? 0.875D : 1.0D);
                break;
            case SOUTH:
                renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, isHead ? 0.125D : 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, isHead ? 1.0D : 0.9375D);
                break;
            case WEST:
                renderBlocks.setRenderBounds(isHead ? 0.0D : 0.0625D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, isHead ? 0.875D : 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                break;
            case EAST:
                renderBlocks.setRenderBounds(isHead ? 0.125D : 0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, isHead ? 1.0D : 0.9375D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                break;
            default: {}
        }

        renderBlock(itemStack, x, y, z);
    }

    /**
     * Renders normal bed frame.
     */
    private void renderNormalFrame(ItemStack itemStack, int x, int y, int z)
    {
        /* Render components that cannot easily be rotated */

        switch (dir)
        {
            case NORTH:
            {
                if (isHead) {

                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                } else {

                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }

                }
                break;
            }
            case SOUTH:
            {
                if (isHead) {

                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);

                } else {

                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }

                }
                break;
            }
            case WEST:
            {
                if (isHead) {

                    // Render legs
                    renderBlocks.setRenderBounds(0.875D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                } else {

                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }

                }
                break;
            }
            case EAST:
            {
                if (isHead) {

                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.0D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                } else {

                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }

                }
                break;
            }
            default: { }
        }

        /* Render components that are safe to rotate */

        if (isHead) {
            renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.1875D, 0.0D, 0.875D, 0.875D, 0.125D, dir); // Render headboard
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.1875D, 0.125D, 1.0D, 0.3125D, 1.0D, dir); // Render support board
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D, dir); // Render support board
        }
    }

}
