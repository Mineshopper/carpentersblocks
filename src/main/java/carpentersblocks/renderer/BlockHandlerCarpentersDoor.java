package carpentersblocks.renderer;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Hinge;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersDoor extends BlockHandlerHinged {

    private boolean hingeLeft;
    private boolean isOpen;
    private int facing;
    private int type;
    private boolean isBottom;

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    /**
     * Renders block at coordinates.
     */
    public void renderBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        setParams();
        ItemStack itemStack = getCoverForRendering();

        switch (type) {
            case Hinge.TYPE_GLASS_TOP:
                renderTypeGlassTop(itemStack, x, y, z);
                break;
            case Hinge.TYPE_GLASS_TALL:
            case Hinge.TYPE_SCREEN_TALL:
                renderTypeTall(itemStack, x, y, z);
                break;
            case Hinge.TYPE_PANELS:
                renderTypePanels(itemStack, x, y, z);
                break;
            case Hinge.TYPE_FRENCH_GLASS:
                renderTypeFrench(itemStack, x, y, z);
                break;
            case Hinge.TYPE_HIDDEN:
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
        type = Hinge.getType(TE);
        hingeLeft = Hinge.getHinge(TE) == Hinge.HINGE_LEFT;
        isOpen = Hinge.getState(TE) == Hinge.STATE_OPEN;
        isBottom = Hinge.getPiece(TE) == Hinge.PIECE_BOTTOM;
        int facing = Hinge.getFacing(TE);
        this.facing = facing == Hinge.FACING_ZN ? 0 : facing == Hinge.FACING_ZP ? 1 : facing == Hinge.FACING_XN ? 2 : 3;

        ForgeDirection[][] extrapolatedSide = {
                { ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST },
                { ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.WEST },
                { ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.NORTH },
                { ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH }
        };

        side = extrapolatedSide[this.facing][!isOpen ? 0 : hingeLeft ? 1 : 2];
    }

    /**
     * Renders a French glass door at the given coordinates.
     */
    private void renderTypeFrench(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.8125D, 0.1875D, 1.0D, 1.0D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D, side);

        if (isBottom) {
            renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.1875D, 0.875D, 0.5625D, 0.5D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.625D, 0.875D, 0.5625D, 0.9375D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D, 1.0D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.5D, 0.875D, 0.8125D, 0.625D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.9375D, 0.875D, 0.8125D, 1.0D, 0.9375D, side);
            renderPartPane(IconRegistry.icon_door_french_glass_bottom, x, y, z);
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.0625D, 0.875D, 0.5625D, 0.375D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.4375D, 0.5D, 0.875D, 0.5625D, 0.8125D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D, 1.0D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.875D, 0.8125D, 0.0625D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.375D, 0.875D, 0.8125D, 0.5D, 0.9375D, side);
            renderPartPane(IconRegistry.icon_door_french_glass_top, x, y, z);
        }

        renderPartHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a glass top door at the given coordinates.
     */
    private void renderTypeGlassTop(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.8125D, 0.1875D, 1.0D, 1.0D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D, side);

        if (isBottom) {
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.1875D, 0.875D, 0.8125D, 1.0D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D, 1.0D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.3125D, 0.3125D, 0.8125D, 0.6875D, 0.875D, 1.0D, side);
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D, 1.0D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D, 1.0D, side);
            renderPartPane(IconRegistry.icon_door_glass_top, x, y, z);
        }

        renderPartHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a panels door at the given coordinates.
     */
    private void renderTypePanels(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.8125D, 0.1875D, 1.0D, 1.0D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D, side);

        if (isBottom) {
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.1875D, 0.875D, 0.8125D, 1.0D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D, 1.0D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.3125D, 0.3125D, 0.8125D, 0.6875D, 0.9375D, 1.0D, side);
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.875D, 0.8125D, 0.8125D, 0.9375D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D, 1.0D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0625D, 0.8125D, 0.8125D, 0.25D, 1.0D, side);
            renderBlockWithRotation(itemStack, x, y, z, 0.3125D, 0.375D, 0.8125D, 0.6875D, 0.6875D, 1.0D, side);
        }

        renderPartHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a tall glass or screen door at the given coordinates.
     */
    private void renderTypeTall(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.8125D, 0.1875D, 1.0D, 1.0D, side);
        renderBlockWithRotation(itemStack, x, y, z, 0.8125D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D, side);

        if (isBottom) {
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.0D, 0.8125D, 0.8125D, 0.1875D, 1.0D, side);
            renderPartPane(type == Hinge.TYPE_SCREEN_TALL ? IconRegistry.icon_door_screen_tall : IconRegistry.icon_door_glass_tall_bottom, x, y, z);
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.1875D, 0.8125D, 0.8125D, 0.8125D, 1.0D, 1.0D, side);
            renderPartPane(type == Hinge.TYPE_SCREEN_TALL ? IconRegistry.icon_door_screen_tall : IconRegistry.icon_door_glass_tall_top, x, y, z);
        }

        renderPartHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a hidden door at the given coordinates
     */
    private void renderTypeHidden(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D, side);
        renderPartHandle(new ItemStack(Blocks.iron_block), x, y, z, true, false);
    }

    /**
     * Renders a door handle for the given coordinates
     */
    private void renderPartHandle(ItemStack itemStack, int x, int y, int z, boolean renderInner, boolean renderOuter)
    {
        if (!renderInner && !renderOuter) {
            return;
        }

        suppressDyeColor = true;
        suppressChiselDesign = true;
        suppressOverlay = true;

        float xMin = isOpen ? (!hingeLeft ? 0.875F : 0.0625F) : (!hingeLeft ? 0.0625F : 0.875F);
        float yMin = isBottom ? 0.875F : 0.0625F;
        float yMax = isBottom ? 0.9375F : 0.125F;
        float yMinOffset = isBottom ? 0.875F : 0.0F;
        float yMaxOffset = isBottom ? 1.0F : 0.125F;

        boolean renderSrc = renderInner && !isOpen || renderOuter && isOpen;
        boolean renderOffset = renderOuter && !isOpen || renderInner && isOpen;

        if (renderSrc) {
            renderBlockWithRotation(itemStack, x, y, z, xMin, yMin, 0.75F, xMin + 0.0625F, yMax, 0.8125F, side);
            renderBlockWithRotation(itemStack, x, y, z, xMin, yMinOffset, 0.6875F, xMin + 0.0625F, yMaxOffset, 0.75F, side);
        }

        if (renderOffset) {
            ForgeDirection opp = side.getOpposite();
            renderBlockWithRotation(itemStack, x - opp.offsetX, y, z - opp.offsetZ, xMin, yMin, 0.0F, xMin + 0.0625F, yMax, 0.0625F, side);
            renderBlockWithRotation(itemStack, x - opp.offsetX, y, z - opp.offsetZ, xMin, yMinOffset, 0.0625F, xMin + 0.0625F, yMaxOffset, 0.125F, side);
        }

        suppressDyeColor = false;
        suppressChiselDesign = false;
        suppressOverlay = false;
    }

}
