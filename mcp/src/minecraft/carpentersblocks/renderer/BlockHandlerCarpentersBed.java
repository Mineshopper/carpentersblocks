package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.DesignHandler;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersBed extends BlockHandlerBase {

    private Icon[] icon_design;

    @Override
    public boolean shouldRender3DInInventory()
    {
        return false;
    }

    @Override
    /**
     * Renders bed
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        ItemStack itemStack = getCoverForRendering();

        if (hasDesign()) {
            icon_design = IconRegistry.icon_design_bed.get(DesignHandler.listBed.indexOf(BlockProperties.getDesign(TE)));
        }

        if (renderPass == PASS_OPAQUE) {
            renderFabricComponents(new ItemStack(Block.cloth), x, y, z);
        }

        applyFrameDyeOverride();

        switch (Bed.getType(TE)) {
            case Bed.TYPE_NORMAL:
                renderNormalFrame(itemStack, x, y, z);
                break;
        }

        clearDyeOverride();
        suppressDyeColor = false;

        renderBlocks.renderAllFaces = false;
    }

    private void applyFrameDyeOverride()
    {
        TEBase TE_temp = isHead() ? TE : Bed.getOppositeTE(TE);

        if (TE_temp != null) {
            if (BlockProperties.hasDye(TE_temp, 6)) {
                setDyeOverride(DyeHandler.getColor(BlockProperties.getDye(TE_temp, 6)));
            } else {
                suppressDyeColor = true;
            }
        }
    }

    private boolean isOccupied()
    {
        TEBase TE_opp = Bed.getOppositeTE(TE);

        if (TE_opp != null && Bed.isOccupied(TE_opp)) {
            return true;
        } else {
            return Bed.isOccupied(TE);
        }
    }

    private boolean isHead()
    {
        return Bed.isHeadOfBed(TE);
    }

    private int getBlanketDyeMetadata()
    {
        TEBase TE_temp = isHead() ? Bed.getOppositeTE(TE) : TE;

        if (TE_temp != null && BlockProperties.hasDye(TE_temp, coverRendering)) {
            return DyeHandler.getVanillaDmgValue(BlockProperties.getDye(TE_temp, coverRendering));
        } else {
            return 0;
        }
    }

    private ForgeDirection getDirection()
    {
        return Bed.getDirection(TE);
    }

    private boolean hasDesign()
    {
        return BlockProperties.hasDesign(TE);
    }

    private boolean isParallelPosSide()
    {
        Block blockXP = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord)];
        Block blockZP = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord + 1)];

        if (getDirection().ordinal() < 4 && blockXP != null && blockXP.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else if (blockZP != null && blockZP.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else {
            return false;
        }
    }

    private boolean isParallelNegSide()
    {
        Block blockXN = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord)];
        Block blockZN = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord - 1)];

        if (getDirection().ordinal() < 4 && blockXN != null && blockXN.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else if (blockZN != null && blockZN.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else {
            return false;
        }
    }

    /**
     * Renders fabric components of bed.
     *
     * @param x
     * @param y
     * @param z
     */
    private void renderFabricComponents(ItemStack itemStack, int x, int y, int z)
    {
        suppressDyeColor = true;
        suppressOverlay = true;
        suppressChiselDesign = true;

        int[] rotateTop = { 2, 0, 1, 3 };
        renderBlocks.uvRotateTop = renderBlocks.uvRotateBottom = rotateTop[getDirection().ordinal() - 2];

        if (isHead()) {
            renderPillow(itemStack, x, y, z);
        }

        renderBlanket(itemStack, x, y, z);
        renderBlocks.uvRotateTop = renderBlocks.uvRotateBottom = 0;
        renderMattress(itemStack, x, y, z);

        suppressDyeColor = false;
        suppressOverlay = false;
        suppressChiselDesign = false;
    }

    private void renderPillow(ItemStack itemStack, int x, int y, int z)
    {
        Icon icon_pillow = hasDesign() ? icon_design[0] : IconRegistry.icon_bed_pillow;
        setIconOverride(6, icon_pillow);
        renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.1875D, 0.875D, 0.6875D, 0.5625D);
        rotateBounds(renderBlocks, getDirection());
        renderBlock(itemStack, x, y, z);
        clearIconOverride(6);
    }

    private void renderBlanket(ItemStack itemStack, int x, int y, int z)
    {
        ForgeDirection dir = getDirection();
        boolean isHead = isHead();
        float yOffset = isOccupied() ? 0.125F : 0.375F;
        tessellator.addTranslation(0.0F, -yOffset, 0.0F);

        if (hasDesign()) {

            int[] idxHead = { 2, 2, 2, 7, 1, 3 };
            int[] idxFoot = { 5, 5, 2, 7, 4, 6 };
            int[][] idxRot = { { 3, 2, 5, 4 }, { 2, 3, 4, 5 }, { 4, 5, 3, 2 }, { 5, 4, 2, 3 } };
            int valDir = dir.ordinal() - 2;

            /** 0 = head, 1 = foot */
            Icon[][] icon = {
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

        double yMin = 0.6875D;
        double yTop = 0.9375D;

        if (isHead) {

            itemStack.setItemDamage(getBlanketDyeMetadata());
            renderBlocks.setRenderBounds(0.0D, yMin, 0.5D, 0.0625D, yTop, 1.0D);
            rotateBounds(renderBlocks, dir);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(0.0D, yTop, 0.5D, 1.0D, 1.0D, 1.0D);
            rotateBounds(renderBlocks, dir);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(0.9375D, yMin, 0.5D, 1.0D, yTop, 1.0D);
            rotateBounds(renderBlocks, dir);
            renderBlock(itemStack, x, y, z);
            itemStack.setItemDamage(15);

        } else {

            itemStack.setItemDamage(getBlanketDyeMetadata());
            renderBlocks.setRenderBounds(0.0D, yMin, 0.0D, 0.0625D, yTop, 1.0D);
            rotateBounds(renderBlocks, dir);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(0.0D, yTop, 0.0D, 1.0D, 1.0D, 1.0D);
            rotateBounds(renderBlocks, dir);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(0.9375D, yMin, 0.0D, 1.0D, yTop, 1.0D);
            rotateBounds(renderBlocks, dir);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(0.0625D, yMin, 0.9375D, 0.9375D, yTop, 1.0D);
            rotateBounds(renderBlocks, dir);
            renderBlock(itemStack, x, y, z);
            itemStack.setItemDamage(15);

        }

        clearIconOverride(6);
        tessellator.addTranslation(0.0F, yOffset, 0.0F);
    }

    private void renderMattress(ItemStack itemStack, int x, int y, int z)
    {
        boolean isHead = isHead();
        boolean bedParallelNeg = isParallelNegSide();
        boolean bedParallelPos = isParallelPosSide();

        itemStack.setItemDamage(0);

        switch (getDirection()) {
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

    private void renderNormalFrame(ItemStack itemStack, int x, int y, int z)
    {
        boolean isHead = isHead();
        boolean bedParallelNeg = isParallelNegSide();
        boolean bedParallelPos = isParallelPosSide();

        switch (getDirection())
        {
            case NORTH:
            {
                if (isHead) {

                    // Render headboard
                    renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.875D, 0.875D, 0.875D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 0.875D);
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

                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                }
                break;
            }
            case SOUTH:
            {
                if (isHead) {

                    // Render headboard
                    renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 0.875D, 0.875D, 0.125D);
                    renderBlock(itemStack, x, y, z);

                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);

                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 1.0D, 0.3125D, 1.0D);
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

                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                }
                break;
            }
            case WEST:
            {
                if (isHead) {

                    // Render headboard
                    renderBlocks.setRenderBounds(0.875D, 0.1875D, 0.125D, 1.0D, 0.875D, 0.875D);
                    renderBlock(itemStack, x, y, z);

                    // Render legs
                    renderBlocks.setRenderBounds(0.875D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 0.875D, 0.3125D, 1.0D);
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

                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                }
                break;
            }
            case EAST:
            {
                if (isHead) {

                    // Render headboard
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 0.125D, 0.875D, 0.875D);
                    renderBlock(itemStack, x, y, z);

                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.0D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                    // Render support board
                    renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
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

                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);

                }
                break;
            }
            default: { }
        }
    }

}
