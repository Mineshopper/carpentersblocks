package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Bed;
import carpentersblocks.renderer.helper.VertexHelper;
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

    private ForgeDirection dir;
    private boolean isHead;
    private boolean hasDesign;
    private boolean bedParallelNeg;
    private boolean bedParallelPos;

    private TEBase TE_head;
    private TEBase TE_foot;

    @Override
    public boolean shouldRender3DInInventory()
    {
        return false;
    }

    private boolean getIsParallelPos()
    {
        Block blockXP = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord + 1, TE.yCoord, TE.zCoord)];
        Block blockZP = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord + 1)];

        if (dir.ordinal() < 4 && blockXP != null && blockXP.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else if (blockZP != null && blockZP.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else {
            return false;
        }
    }

    private boolean getIsParallelNeg()
    {
        Block blockXN = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord - 1, TE.yCoord, TE.zCoord)];
        Block blockZN = Block.blocksList[renderBlocks.blockAccess.getBlockId(TE.xCoord, TE.yCoord, TE.zCoord - 1)];

        if (dir.ordinal() < 4 && blockXN != null && blockXN.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord);
            return Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
        } else if (blockZN != null && blockZN.equals(srcBlock)) {
            TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getBlockTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1);
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

        /* Reset common fields */

        dir = Bed.getDirection(TE);
        isHead = Bed.isHeadOfBed(TE);
        hasDesign = BlockProperties.hasDesign(TE);
        bedParallelPos = getIsParallelPos();
        bedParallelNeg = getIsParallelNeg();
        TE_head = isHead ? TE : Bed.getOppositeTE(TE);

        if (TE_head != null) {
            TE_foot = Bed.getOppositeTE(TE_head);
        }

        if (hasDesign) {
            icon_design = IconRegistry.icon_design_bed.get(DesignHandler.listBed.indexOf(BlockProperties.getDesign(TE)));
        }

        if (renderPass == PASS_OPAQUE) {
            renderFabricComponents(new ItemStack(Block.cloth), x, y, z);
        }

        /* Apply frame dye override */

        if (TE_head != null) {
            if (BlockProperties.hasDye(TE_head, 6)) {
                setDyeOverride(DyeHandler.getColor(BlockProperties.getDye(TE_head, 6)));
            } else {
                suppressDyeColor = true;
            }
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
            Icon icon_pillow = hasDesign ? icon_design[0] : IconRegistry.icon_bed_pillow;
            setIconOverride(6, icon_pillow);
            setupRender(itemStack, x, y, z, 0.125D, 0.5625D, 0.1875D, 0.875D, 0.6875D, 0.5625D);
            clearIconOverride(6);
        }
    }

    /**
     * Renders blanket.
     */
    private void renderBlanket(ItemStack itemStack, int x, int y, int z)
    {
        VertexHelper.lockFloatingIcon();
        int blanketDyeMetadata = 0;
        boolean isOccupied = false;

        if (TE_head != null) {
            isOccupied |= Bed.isOccupied(TE_head);
        }
        if (TE_foot != null) {
            isOccupied |= Bed.isOccupied(TE_foot);
        }

        if (hasDesign) {

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

        } else {

            if (TE_foot != null) {
                blanketDyeMetadata = BlockProperties.hasDye(TE_foot, coverRendering) ? DyeHandler.getVanillaDmgValue(BlockProperties.getDye(TE_foot, coverRendering)) : 0;
            }

        }

        double yTop = isOccupied ? 0.875D : 0.625D;
        double depth = 0.3125F;

        itemStack.setItemDamage(blanketDyeMetadata);

        if (isHead) {
            setupRender(itemStack, x, y, z, 0.0625D, yTop - 0.0625D, 0.5D, 0.9375D, yTop, 1.0D);
            setupRender(itemStack, x, y, z, 0.0D, yTop - depth, 0.5D, 0.0625D, yTop, 1.0D);
            setupRender(itemStack, x, y, z, 0.9375D, yTop - depth, 0.5D, 1.0D, yTop, 1.0D);
        } else {
            setupRender(itemStack, x, y, z, 0.0625D, yTop - 0.0625D, 0.0D, 0.9375D, yTop, 0.9375D);
            setupRender(itemStack, x, y, z, 0.0D, yTop - depth, 0.0D, 0.0625D, yTop, 1.0D);
            setupRender(itemStack, x, y, z, 0.9375D, yTop - depth, 0.0D, 1.0D, yTop, 1.0D);
            setupRender(itemStack, x, y, z, 0.0625D, yTop - depth, 0.9375D, 0.9375D, yTop, 1.0D);
        }

        itemStack.setItemDamage(15);
        clearIconOverride(6);
        VertexHelper.unlockFloatingIcon();
    }

    /**
     * Renders mattress.
     */
    private void renderMattress(ItemStack itemStack, int x, int y, int z)
    {
        itemStack.setItemDamage(0);
        setupRender(itemStack, x, y, z, bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, isHead ? 0.125D : 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, isHead ? 1.0D : 0.9375D);
    }

    /**
     * Renders normal bed frame.
     */
    private void renderNormalFrame(ItemStack itemStack, int x, int y, int z)
    {
        if (isHead) {

            setupRender(itemStack, x, y, z, 0.125D, 0.1875D, 0.0D, 0.875D, 0.875D, 0.125D); // Render headboard
            setupRender(itemStack, x, y, z, 0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);// Render leg
            setupRender(itemStack, x, y, z, 0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 0.125D);// Render leg
            setupRender(itemStack, x, y, z, 0.0D, 0.1875D, 0.125D, 1.0D, 0.3125D, 1.0D); // Render support board

        } else {

            if (!bedParallelNeg) {
                setupRender(itemStack, x, y, z, 0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D); // Render leg
            }
            if (!bedParallelPos) {
                setupRender(itemStack, x, y, z, 0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D); // Render leg
            }

            setupRender(itemStack, x, y, z, 0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D); // Render support board

        }
    }

    /**
     * Set render bounds, rotate them and render.
     */
    private void setupRender(ItemStack itemStack, int x, int y, int z, double xMin, double yMin, double zMin, double xMax, double yMax, double zMax)
    {
        renderBlocks.setRenderBounds(xMin, yMin, zMin, xMax, yMax, zMax);
        rotateBounds(renderBlocks, dir);
        renderBlock(itemStack, x, y, z);
    }

}
