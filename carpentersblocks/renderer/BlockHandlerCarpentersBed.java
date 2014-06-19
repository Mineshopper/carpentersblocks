package carpentersblocks.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
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

    private ForgeDirection dir;
    private Tessellator tessellator = Tessellator.instance;
    private boolean isHead;
    private boolean isOccupied;
    private int blanketColor;
    private int frameColor;
    private boolean bedParallelPos;
    private boolean bedParallelNeg;
    private String design = "";

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    /**
     * Renders bed
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        ItemStack itemStack = getCoverForRendering();
        setParams();

        renderBlocks.renderAllFaces = true;

        renderCloth(x, y, z);

        switch (Bed.getType(TE)) {
            case Bed.TYPE_NORMAL:
                renderNormalFrame(itemStack, x, y, z);
                break;
            case Bed.TYPE_PLATFORM:
                renderPlatformFrame(itemStack, x, y, z);
                break;
        }

        renderBlocks.renderAllFaces = false;
    }

    /**
     * Sets common parameters.
     */
    private void setParams()
    {
        tessellator = Tessellator.instance;
        dir = Bed.getDirection(TE);
        isHead = Bed.isHeadOfBed(TE);
        isOccupied = Bed.isOccupied(TE);
        design = BlockProperties.getDesign(TE);

        TEBase TE_opp = Bed.getOppositeTE(TE);

        if (isHead) {
            frameColor = DyeHandler.getColor(BlockProperties.getDye(TE, 6));
            if (TE_opp != null) {
                ItemStack dyeStack = BlockProperties.getDye(TE_opp, 6);
                if (dyeStack != null) {
                    blanketColor = 15 - dyeStack.getItemDamage();
                }
                isOccupied |= Bed.isOccupied(TE_opp);
            }
        } else {
            ItemStack dyeStack = BlockProperties.getDye(TE, 6);
            if (dyeStack != null) {
                blanketColor = 15 - dyeStack.getItemDamage();
            }
            if (TE_opp != null) {
                frameColor = DyeHandler.getColor(BlockProperties.getDye(TE_opp, 6));
                isOccupied |= Bed.isOccupied(TE_opp);
            }
        }

        /* Check for adjacent bed pieces that can connect. */

        if (dir.equals(ForgeDirection.NORTH) || dir.equals(ForgeDirection.SOUTH))
        {

            if (renderBlocks.blockAccess.getBlock(TE.xCoord + 1, TE.yCoord, TE.zCoord).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord + 1, TE.yCoord, TE.zCoord);
                bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }
            if (renderBlocks.blockAccess.getBlock(TE.xCoord - 1, TE.yCoord, TE.zCoord).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord - 1, TE.yCoord, TE.zCoord);
                bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }

        } else {

            if (renderBlocks.blockAccess.getBlock(TE.xCoord, TE.yCoord, TE.zCoord + 1).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord + 1);
                bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }
            if (renderBlocks.blockAccess.getBlock(TE.xCoord, TE.yCoord, TE.zCoord - 1).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(TE.xCoord, TE.yCoord, TE.zCoord - 1);
                bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }

        }
    }

    /**
     * Renders pillow, blanket and mattress.
     *
     * @param x
     * @param y
     * @param z
     */
    private void renderCloth(int x, int y, int z)
    {
        boolean hasDesign = BlockProperties.hasDesign(TE);
        IIcon icon_pillow = hasDesign ? IconRegistry.icon_design_bed.get(DesignHandler.listBed.indexOf(BlockProperties.getDesign(TE)))[0] : IconRegistry.icon_bed_pillow;

        suppressDyeColor = true;
        suppressOverlay = true;
        suppressChiselDesign = true;

        ItemStack cloth = new ItemStack(Blocks.wool);

        switch (dir)
        {
            case NORTH: // -Z
            {
                if (isHead) {

                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.875D);
                    renderBlock(cloth, x, y, z);

                    // Render pillow
                    renderBlocks.uvRotateTop = 2;
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.4375D, 0.875D, 0.6875D, 0.8125D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        setIconOverride(6, IconRegistry.icon_design_bed.get(DesignHandler.listBed.indexOf(design))[7]);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
                        renderBlock(cloth, x, y, z);
                        clearIconOverride(6);

                    }

                } else {

                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0625D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
                    renderBlock(cloth, x, y, z);

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);

                    }

                }
                break;
            }
            case SOUTH: // +Z
            {
                if (isHead) {

                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.125D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
                    renderBlock(cloth, x, y, z);

                    // Render pillow
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.1875D, 0.875D, 0.6875D, 0.5625D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        renderBlocks.uvRotateTop = 2;
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);

                    }

                } else {

                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.9375D);
                    renderBlock(cloth, x, y, z);

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        renderBlocks.uvRotateTop = 2;
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);

                    }

                }
                break;
            }
            case WEST: // -X
            {
                if (isHead) {

                    // Render mattress
                    renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.875D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);

                    // Render pillow
                    renderBlocks.uvRotateTop = 1;
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.4375D, 0.5625D, 0.125D, 0.8125D, 0.6875D, 0.875D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        renderBlocks.uvRotateTop = 3;
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);

                    }

                } else {

                    // Render mattress
                    renderBlocks.setRenderBounds(0.0625D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        renderBlocks.uvRotateTop = 3;
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);

                    }

                }
                break;
            }
            default: // EAST +X
            {
                if (isHead) {

                    // Render mattress
                    renderBlocks.setRenderBounds(0.125D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);

                    // Render pillow
                    renderBlocks.uvRotateTop = 3;
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.1875D, 0.5625D, 0.125D, 0.5625D, 0.6875D, 0.875D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        renderBlocks.uvRotateTop = 1;
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);

                    }

                } else {

                    // Render mattress
                    renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.9375D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);

                    // Render blanket
                    if (!hasDesign) {

                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(15);

                    } else {

                        renderBlocks.uvRotateTop = 1;
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);

                    }

                }
                break;
            }
        }

        renderBlocks.uvRotateTop = 0;
    }

    private void renderPlatformFrame(ItemStack itemStack, int x, int y, int z)
    {

    }

    private void renderNormalFrame(ItemStack itemStack, int x, int y, int z)
    {
        setDyeOverride(frameColor);

        switch (dir)
        {
            case NORTH: // -Z
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
            case SOUTH: // +Z
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
            case WEST: // -X
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
            default: // EAST +X
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
        }

        clearDyeOverride();
    }

}
