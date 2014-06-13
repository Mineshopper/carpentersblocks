package carpentersblocks.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import carpentersblocks.block.BlockCarpentersDoor;
import carpentersblocks.data.Hinge;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersDoor extends BlockHandlerBase {

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    /**
     * Renders block
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        ItemStack itemStack = getCoverForRendering();
        int type = Hinge.getType(TE);

        switch (type) {
            case Hinge.TYPE_GLASS_TOP:
                renderGlassTopDoor(itemStack, x, y, z);
                break;
            case Hinge.TYPE_GLASS_TALL:
                renderTallDoor(itemStack, x, y, z);
                break;
            case Hinge.TYPE_PANELS:
                renderPanelsDoor(itemStack, x, y, z);
                break;
            case Hinge.TYPE_SCREEN_TALL:
                renderTallDoor(itemStack, x, y, z);
                break;
            case Hinge.TYPE_FRENCH_GLASS:
                renderFrenchGlassDoor(itemStack, x, y, z);
                break;
            case Hinge.TYPE_HIDDEN:
                renderHiddenDoor(itemStack, x, y, z);
                break;
        }

        renderBlocks.renderAllFaces = false;
    }

    /**
     * Renders a French glass door at the given coordinates
     */
    private void renderFrenchGlassDoor(ItemStack itemStack, int x, int y, int z)
    {
        int hinge = Hinge.getHinge(TE);
        int facing = Hinge.getFacing(TE);
        boolean isOpen = Hinge.getState(TE) == Hinge.STATE_OPEN;
        boolean isBottom = Hinge.getPiece(TE) == Hinge.PIECE_BOTTOM;

        boolean path_on_x = false;

        float path_offset = 0.0F;

        float x_low = 0.0F;
        float y_low = 0.0F;
        float z_low = 0.0F;
        float x_high = 1.0F;
        float y_high = 1.0F;
        float z_high = 1.0F;

        float x_low_offset = 0.0F;
        float z_low_offset = 0.0F;
        float x_high_offset = 1.0F;
        float z_high_offset = 1.0F;

        switch (facing)
        {
            case Hinge.FACING_XN:

                if (!isOpen) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                } else {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                }

                break;
            case Hinge.FACING_XP:

                if (!isOpen) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                } else {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                }

                break;
            case Hinge.FACING_ZN:

                if (!isOpen) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                } else {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                }

                break;
            case Hinge.FACING_ZP:

                if (!isOpen) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                } else {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                }

                break;
        }

        /* Draw vertical pieces. */

        renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
        renderBlock(itemStack, x, y, z);

        float temp_x_low = x_low;
        float temp_x_high = x_high;
        float temp_z_low = z_low;
        float temp_z_high = z_high;

        if (path_on_x) {
            temp_x_low += 0.0625F;
            temp_x_high -= 0.0625F;
            temp_z_low = 0.4375F;
            temp_z_high = 0.5625F;
        } else {
            temp_z_low += 0.0625F;
            temp_z_high -= 0.0625F;
            temp_x_low = 0.4375F;
            temp_x_high = 0.5625F;
        }

        // Two center pieces
        if (isBottom) {
            renderBlocks.setRenderBounds(temp_x_low, 0.1875F, temp_z_low, temp_x_high, 0.5F, temp_z_high);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(temp_x_low, 0.625F, temp_z_low, temp_x_high, 0.9375F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        } else {
            renderBlocks.setRenderBounds(temp_x_low, 0.0625F, temp_z_low, temp_x_high, 0.375F, temp_z_high);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(temp_x_low, 0.5F, temp_z_low, temp_x_high, 0.8125F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        }

        temp_x_low = x_low;
        temp_x_high = x_high;
        temp_z_low = z_low;
        temp_z_high = z_high;

        /* Draw horizontal pieces. */

        if (path_on_x) {
            temp_z_low = 0.1875F;
            temp_z_high = 0.8125F;
        } else {
            temp_x_low = 0.1875F;
            temp_x_high = 0.8125F;
        }

        if (isBottom) {

            renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
            renderBlock(itemStack, x, y, z);

            if (path_on_x) {
                temp_x_low += 0.0625F;
                temp_x_high -= 0.0625F;
            } else {
                temp_z_low += 0.0625F;
                temp_z_high -= 0.0625F;
            }

            renderBlocks.setRenderBounds(temp_x_low, 0.5F, temp_z_low, temp_x_high, 0.625F, temp_z_high);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(temp_x_low, 0.9375F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
            renderBlock(itemStack, x, y, z);

        } else {

            renderBlocks.setRenderBounds(temp_x_low, 0.8125F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
            renderBlock(itemStack, x, y, z);

            if (path_on_x) {
                temp_x_low += 0.0625F;
                temp_x_high -= 0.0625F;
            } else {
                temp_z_low += 0.0625F;
                temp_z_high -= 0.0625F;
            }

            renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.0625F, temp_z_high);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(temp_x_low, 0.375F, temp_z_low, temp_x_high, 0.5F, temp_z_high);
            renderBlock(itemStack, x, y, z);

        }

        IIcon icon;

        if (isBottom) {
            icon = IconRegistry.icon_door_french_glass_bottom;
        } else {
            icon = IconRegistry.icon_door_french_glass_top;
        }

        VertexHelper.setOffset(-(1 - path_offset));
        Tessellator.instance.setBrightness(Blocks.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

        ItemStack glass = new ItemStack(Blocks.glass);

        if (path_on_x) {

            renderBlocks.setRenderBounds(0.0F, isBottom ? 0.1875F : 0.0F, 0.1875F, 1.0F, isBottom ? 1.0F : 0.8125F, 0.8125F);
            Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);

            RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);

            // TODO: Revisit when alpha pass is properly implemented.
            VertexHelper.setOffset(-path_offset);
            RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);

        } else {

            renderBlocks.setRenderBounds(0.1875F, isBottom ? 0.1875F : 0.0F, 0.0F, 0.8125F, isBottom ? 1.0F : 0.8125F, 1.0F);
            Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);

            RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);

            // TODO: Revisit when alpha pass is properly implemented.
            VertexHelper.setOffset(-path_offset);
            RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
        }

        VertexHelper.clearOffset();

        renderHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a glass top door at the given coordinates
     */
    private void renderGlassTopDoor(ItemStack itemStack, int x, int y, int z)
    {
        int hinge = Hinge.getHinge(TE);
        boolean isOpen = Hinge.getState(TE) == Hinge.STATE_OPEN;
        boolean isBottom = Hinge.getPiece(TE) == Hinge.PIECE_BOTTOM;

        boolean path_on_x = false;

        float path_offset = 0.0F;

        float x_low = 0.0F;
        float y_low = 0.0F;
        float z_low = 0.0F;
        float x_high = 1.0F;
        float y_high = 1.0F;
        float z_high = 1.0F;

        float x_low_offset = 0.0F;
        float z_low_offset = 0.0F;
        float x_high_offset = 1.0F;
        float z_high_offset = 1.0F;

        switch (Hinge.getFacing(TE))
        {
            case Hinge.FACING_XN:

                if (!isOpen) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                } else {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                }

                break;
            case Hinge.FACING_XP:

                if (!isOpen) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                } else {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                }

                break;
            case Hinge.FACING_ZN:

                if (!isOpen) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                } else {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                }

                break;
            case Hinge.FACING_ZP:

                if (!isOpen) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                } else {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                }

                break;
        }

        renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
        renderBlock(itemStack, x, y, z);

        float temp_x_low = x_low;
        float temp_x_high = x_high;
        float temp_z_low = z_low;
        float temp_z_high = z_high;

        /* Render interior sheet on bottom */

        if (isBottom)
        {
            if (path_on_x) {
                temp_x_low += 0.0625F;
                temp_x_high -= 0.0625F;
                temp_z_low = 0.1875F;
                temp_z_high = 0.8125F;
            } else {
                temp_z_low += 0.0625F;
                temp_z_high -= 0.0625F;
                temp_x_low = 0.1875F;
                temp_x_high = 0.8125F;
            }

            renderBlocks.setRenderBounds(temp_x_low, 0.1875F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
            renderBlock(itemStack, x, y, z);

            temp_x_low = x_low;
            temp_x_high = x_high;
            temp_z_low = z_low;
            temp_z_high = z_high;
        }

        /* Render horizontal pieces */

        if (path_on_x) {
            temp_z_low = 0.1875F;
            temp_z_high = 0.8125F;
        } else {
            temp_x_low = 0.1875F;
            temp_x_high = 0.8125F;
        }

        if (isBottom) {
            renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        } else {
            renderBlocks.setRenderBounds(temp_x_low, 0.8125F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        }

        temp_x_low = x_low;
        temp_x_high = x_high;
        temp_z_low = z_low;
        temp_z_high = z_high;

        /* Render interior panel on bottom */

        if (isBottom)
        {
            if (path_on_x) {
                temp_z_low = 0.3125F;
                temp_z_high = 0.6875F;
            } else {
                temp_x_low = 0.3125F;
                temp_x_high = 0.6875F;
            }

            renderBlocks.setRenderBounds(temp_x_low, 0.3125F, temp_z_low, temp_x_high, 0.875F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        }

        if (!isBottom) {

            VertexHelper.setOffset(-(1 - path_offset));
            Tessellator.instance.setBrightness(Blocks.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

            ItemStack glass = new ItemStack(Blocks.glass);

            if (path_on_x) {

                renderBlocks.setRenderBounds(0.0F, 0.1875F, 0.1875F, 1.0F, 0.8125F, 0.8125F);
                Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);

                RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, IconRegistry.icon_door_glass_top);

                // TODO: Revisit when alpha pass is properly implemented.
                VertexHelper.setOffset(-path_offset);
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, IconRegistry.icon_door_glass_top);

            } else {

                renderBlocks.setRenderBounds(0.1875F, 0.1875F, 0.0F, 0.8125F, 0.8125F, 1.0F);
                Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);

                RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, IconRegistry.icon_door_glass_top);

                // TODO: Revisit when alpha pass is properly implemented.
                VertexHelper.setOffset(-path_offset);
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, IconRegistry.icon_door_glass_top);

            }

            VertexHelper.clearOffset();
        }

        renderHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a panels door at the given coordinates
     */
    private void renderPanelsDoor(ItemStack itemStack, int x, int y, int z)
    {
        int hinge = Hinge.getHinge(TE);
        boolean isOpen = Hinge.getState(TE) == Hinge.STATE_OPEN;
        boolean isBottom = Hinge.getPiece(TE) == Hinge.PIECE_BOTTOM;

        boolean path_on_x = false;

        float x_low = 0.0F;
        float y_low = 0.0F;
        float z_low = 0.0F;
        float x_high = 1.0F;
        float y_high = 1.0F;
        float z_high = 1.0F;

        float x_low_offset = 0.0F;
        float z_low_offset = 0.0F;
        float x_high_offset = 1.0F;
        float z_high_offset = 1.0F;

        switch (Hinge.getFacing(TE))
        {
            case Hinge.FACING_XN:

                if (!isOpen) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;

                } else {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;

                }

                break;
            case Hinge.FACING_XP:

                if (!isOpen) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;

                } else {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;

                }

                break;
            case Hinge.FACING_ZN:

                if (!isOpen) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_on_x = true;

                } else {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_on_x = true;

                }

                break;
            case Hinge.FACING_ZP:

                if (!isOpen) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_on_x = true;

                } else {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_on_x = true;

                }

                break;
        }

        renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
        renderBlock(itemStack, x, y, z);

        float temp_x_low = x_low;
        float temp_x_high = x_high;
        float temp_z_low = z_low;
        float temp_z_high = z_high;

        /* Render interior sheet */

        if (path_on_x) {
            temp_x_low += 0.0625F;
            temp_x_high -= 0.0625F;
            temp_z_low = 0.1875F;
            temp_z_high = 0.8125F;
        } else {
            temp_z_low += 0.0625F;
            temp_z_high -= 0.0625F;
            temp_x_low = 0.1875F;
            temp_x_high = 0.8125F;
        }

        if (isBottom) {
            renderBlocks.setRenderBounds(temp_x_low, 0.1875F, temp_z_low, temp_x_high, 1.0F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        } else {
            renderBlocks.setRenderBounds(temp_x_low, 0.0F, temp_z_low, temp_x_high, 0.8125F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        }

        temp_x_low = x_low;
        temp_x_high = x_high;
        temp_z_low = z_low;
        temp_z_high = z_high;

        /* Render horizontal pieces */

        if (path_on_x) {
            temp_z_low = 0.1875F;
            temp_z_high = 0.8125F;
        } else {
            temp_x_low = 0.1875F;
            temp_x_high = 0.8125F;
        }

        if (isBottom) {
            renderBlocks.setRenderBounds(temp_x_low, y_low, temp_z_low, temp_x_high, 0.1875F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        } else {
            renderBlocks.setRenderBounds(temp_x_low, 0.8125F, temp_z_low, temp_x_high, y_high, temp_z_high);
            renderBlock(itemStack, x, y, z);
            renderBlocks.setRenderBounds(temp_x_low, 0.0625F, temp_z_low, temp_x_high, 0.25F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        }

        temp_x_low = x_low;
        temp_x_high = x_high;
        temp_z_low = z_low;
        temp_z_high = z_high;

        /* Render interior panel */

        if (path_on_x) {
            temp_z_low = 0.3125F;
            temp_z_high = 0.6875F;
        } else {
            temp_x_low = 0.3125F;
            temp_x_high = 0.6875F;
        }

        if (isBottom) {
            renderBlocks.setRenderBounds(temp_x_low, 0.3125F, temp_z_low, temp_x_high, 0.9375F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        } else {
            renderBlocks.setRenderBounds(temp_x_low, 0.375F, temp_z_low, temp_x_high, 0.6875F, temp_z_high);
            renderBlock(itemStack, x, y, z);
        }

        renderHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a tall glass or screen door at the given coordinates
     */
    private void renderTallDoor(ItemStack itemStack, int x, int y, int z)
    {
        int hinge = Hinge.getHinge(TE);
        boolean isOpen = Hinge.getState(TE) == Hinge.STATE_OPEN;
        boolean isBottom = Hinge.getPiece(TE) == Hinge.PIECE_BOTTOM;

        boolean path_on_x = false;

        float path_offset = 0.0F;

        float x_low = 0.0F;
        float y_low = 0.0F;
        float z_low = 0.0F;
        float x_high = 1.0F;
        float y_high = 1.0F;
        float z_high = 1.0F;

        float x_low_offset = 0.0F;
        float z_low_offset = 0.0F;
        float x_high_offset = 1.0F;
        float z_high_offset = 1.0F;

        switch (Hinge.getFacing(TE))
        {
            case Hinge.FACING_XN:

                if (!isOpen) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                } else {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                }

                break;
            case Hinge.FACING_XP:

                if (!isOpen) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                } else {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                }

                break;
            case Hinge.FACING_ZN:

                if (!isOpen) {

                    z_low = 0.8125F;
                    z_low_offset = z_low;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.09375F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                } else {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                }

                break;
            case Hinge.FACING_ZP:

                if (!isOpen) {

                    z_high = 0.1875F;
                    z_high_offset = z_high;
                    x_high_offset = 0.1875F;
                    x_low_offset = 0.8125F;
                    path_offset = 0.90625F;

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    x_high = 0.1875F;
                    x_high_offset = x_high;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.90625F;
                    path_on_x = true;

                } else {

                    x_low = 0.8125F;
                    x_low_offset = x_low;
                    z_high_offset = 0.1875F;
                    z_low_offset = 0.8125F;
                    path_offset = 0.09375F;
                    path_on_x = true;

                }

                break;
        }

        renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high_offset, y_high, z_high_offset);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(x_low_offset, y_low, z_low_offset, x_high, y_high, z_high);
        renderBlock(itemStack, x, y, z);

        if (path_on_x) {
            z_low = 0.1875F;
            z_high = 0.8125F;
        } else {
            x_low = 0.1875F;
            x_high = 0.8125F;
        }

        if (isBottom) {
            renderBlocks.setRenderBounds(x_low, y_low, z_low, x_high, 0.1875F, z_high);
            renderBlock(itemStack, x, y, z);
        } else {
            renderBlocks.setRenderBounds(x_low, 0.8125F, z_low, x_high, y_high, z_high);
            renderBlock(itemStack, x, y, z);
        }

        int type = Hinge.getType(TE);
        IIcon icon;

        if (isBottom) {
            icon = type == Hinge.TYPE_SCREEN_TALL ? IconRegistry.icon_door_screen_tall : IconRegistry.icon_door_glass_tall_bottom;
        } else {
            icon = type == Hinge.TYPE_SCREEN_TALL ? IconRegistry.icon_door_screen_tall : IconRegistry.icon_door_glass_tall_top;
        }

        VertexHelper.setOffset(-(1 - path_offset));
        Tessellator.instance.setBrightness(Blocks.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));

        if (path_on_x) {

            renderBlocks.setRenderBounds(0.0F, isBottom ? 0.1875F : 0.0F, 0.1875F, 1.0F, isBottom ? 1.0F : 0.8125F, 0.8125F);
            Tessellator.instance.setColorOpaque_F(0.6F, 0.6F, 0.6F);

            RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);

            // TODO: Revisit when alpha pass is properly implemented.
            VertexHelper.setOffset(-path_offset);
            RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);

        } else {

            renderBlocks.setRenderBounds(0.1875F, isBottom ? 0.1875F : 0.0F, 0.0F, 0.8125F, isBottom ? 1.0F : 0.8125F, 1.0F);
            Tessellator.instance.setColorOpaque_F(0.8F, 0.8F, 0.8F);

            RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);

            // TODO: Revisit when alpha pass is properly implemented.
            VertexHelper.setOffset(-path_offset);
            RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);

        }

        VertexHelper.clearOffset();

        renderHandle(new ItemStack(Blocks.iron_block), x, y, z, true, true);
    }

    /**
     * Renders a hidden door at the given coordinates
     */
    private void renderHiddenDoor(ItemStack itemStack, int x, int y, int z)
    {
        BlockCarpentersDoor blockRef = (BlockCarpentersDoor) BlockRegistry.blockCarpentersDoor;
        blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
        renderBlock(itemStack, x, y, z);
        renderHandle(new ItemStack(Blocks.iron_block), x, y, z, true, false);
    }

    /**
     * Renders a door handle for the given coordinates
     */
    private void renderHandle(ItemStack itemStack, int x, int y, int z, boolean render_inside_handle, boolean render_outside_handle)
    {
        if (!render_inside_handle && !render_outside_handle) {
            return;
        }

        suppressDyeColor = true;
        suppressPattern = true;
        suppressOverlay = true;

        int hinge = Hinge.getHinge(TE);
        boolean isOpen = Hinge.getState(TE) == Hinge.STATE_OPEN;
        boolean isBottom = Hinge.getPiece(TE) == Hinge.PIECE_BOTTOM;

        float x_low = 0.0F;
        float y_low = isBottom ? 0.875F : 0.0625F;
        float z_low = 0.0F;
        float y_high = isBottom ? 0.9375F : 0.125F;
        float y_low_offset = isBottom ? 0.875F : 0.0F;
        float y_high_offset = isBottom ? 1.0F : 0.125F;

        switch (Hinge.getFacing(TE))
        {
            case Hinge.FACING_XN:

                if (!isOpen) {

                    z_low = hinge == Hinge.HINGE_RIGHT ? 0.875F : 0.0625F;

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.75F, y_low, z_low, 0.8125F, y_high, z_low + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.6875F, y_low_offset, z_low, 0.75F, y_high_offset, z_low + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.0625F, y_high, z_low + 0.0625F);
                        renderBlock(itemStack, x + 1, y, z);
                        renderBlocks.setRenderBounds(0.0625F, y_low_offset, z_low, 0.125F, y_high_offset, z_low + 0.0625F);
                        renderBlock(itemStack, x + 1, y, z);
                    }

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.0625F, y_low, 0.1875F, 0.0625F + 0.0625F, y_high, 0.25F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.25F, 0.0625F + 0.0625F, y_high_offset, 0.3125F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.0625F, y_low, 0.9375F, 0.0625F + 0.0625F, y_high, 1.0F);
                        renderBlock(itemStack, x, y, z - 1);
                        renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.875F, 0.0625F + 0.0625F, y_high_offset, 0.9375F);
                        renderBlock(itemStack, x, y, z - 1);
                    }

                } else {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.0625F, y_low, 0.75F, 0.0625F + 0.0625F, y_high, 0.8125F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.6875F, 0.0625F + 0.0625F, y_high_offset, 0.75F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.0625F, y_low, 0.0F, 0.0625F + 0.0625F, y_high, 0.0625F);
                        renderBlock(itemStack, x, y, z + 1);
                        renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.0625F, 0.0625F + 0.0625F, y_high_offset, 0.125F);
                        renderBlock(itemStack, x, y, z + 1);
                    }

                }

                break;
            case Hinge.FACING_XP:

                if (!isOpen) {

                    z_low = hinge == Hinge.HINGE_RIGHT ? 0.0625F : 0.875F;

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.1875F, y_low, z_low, 0.25F, y_high, z_low + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.25F, y_low_offset, z_low, 0.3125F, y_high_offset, z_low + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.9375F, y_low, z_low, 1.0F, y_high, z_low + 0.0625F);
                        renderBlock(itemStack, x - 1, y, z);
                        renderBlocks.setRenderBounds(0.875F, y_low_offset, z_low, 0.9375F, y_high_offset, z_low + 0.0625F);
                        renderBlock(itemStack, x - 1, y, z);
                    }

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.875F, y_low, 0.75F, 0.875F + 0.0625F, y_high, 0.8125F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.6875F, 0.875F + 0.0625F, y_high_offset, 0.75F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.875F, y_low, 0.0F, 0.875F + 0.0625F, y_high, 0.0625F);
                        renderBlock(itemStack, x, y, z + 1);
                        renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.0625F, 0.875F + 0.0625F, y_high_offset, 0.125F);
                        renderBlock(itemStack, x, y, z + 1);
                    }

                } else {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.875F, y_low, 0.1875F, 0.875F + 0.0625F, y_high, 0.25F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.25F, 0.875F + 0.0625F, y_high_offset, 0.3125F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.875F, y_low, 0.9375F, 0.875F + 0.0625F, y_high, 1.0F);
                        renderBlock(itemStack, x, y, z - 1);
                        renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.875F, 0.875F + 0.0625F, y_high_offset, 0.9375F);
                        renderBlock(itemStack, x, y, z - 1);
                    }

                }

                break;
            case Hinge.FACING_ZN:

                if (!isOpen) {

                    x_low = hinge == Hinge.HINGE_RIGHT ? 0.0625F : 0.875F;

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(x_low, y_low, 0.75F, x_low + 0.0625F, y_high, 0.8125F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(x_low, y_low_offset, 0.6875F, x_low + 0.0625F, y_high_offset, 0.75F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_low + 0.0625F, y_high, 0.0625F);
                        renderBlock(itemStack, x, y, z + 1);
                        renderBlocks.setRenderBounds(x_low, y_low_offset, 0.0625F, x_low + 0.0625F, y_high_offset, 0.125F);
                        renderBlock(itemStack, x, y, z + 1);
                    }

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.75F, y_low, 0.0625F, 0.8125F, y_high, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.6875F, y_low_offset, 0.0625F, 0.75F, y_high_offset, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.0F, y_low, 0.0625F, 0.0625F, y_high, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x + 1, y, z);
                        renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.0625F, 0.125F, y_high_offset, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x + 1, y, z);
                    }

                } else {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.1875F, y_low, 0.0625F, 0.25F, y_high, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.25F, y_low_offset, 0.0625F, 0.3125F, y_high_offset, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.9375F, y_low, 0.0625F, 1.0F, y_high, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x - 1, y, z);
                        renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.0625F, 0.9375F, y_high_offset, 0.0625F + 0.0625F);
                        renderBlock(itemStack, x - 1, y, z);
                    }

                }

                break;
            case Hinge.FACING_ZP:

                if (!isOpen) {

                    x_low = hinge == Hinge.HINGE_RIGHT ? 0.875F : 0.0625F;

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(x_low, y_low, 0.1875F, x_low + 0.0625F, y_high, 0.25F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(x_low, y_low_offset, 0.25F, x_low + 0.0625F, y_high_offset, 0.3125F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(x_low, y_low, 0.9375F, x_low + 0.0625F, y_high, 1.0F);
                        renderBlock(itemStack, x, y, z - 1);
                        renderBlocks.setRenderBounds(x_low, y_low_offset, 0.875F, x_low + 0.0625F, y_high_offset, 0.9375F);
                        renderBlock(itemStack, x, y, z - 1);
                    }

                } else if (hinge == Hinge.HINGE_RIGHT) {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.1875F, y_low, 0.875F, 0.25F, y_high, 0.875F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.25F, y_low_offset, 0.875F, 0.3125F, y_high_offset, 0.875F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.9375F, y_low, 0.875F, 1.0F, y_high, 0.875F + 0.0625F);
                        renderBlock(itemStack, x - 1, y, z);
                        renderBlocks.setRenderBounds(0.875F, y_low_offset, 0.875F, 0.9375F, y_high_offset, 0.875F + 0.0625F);
                        renderBlock(itemStack, x - 1, y, z);
                    }

                } else {

                    if (render_outside_handle)
                    {
                        renderBlocks.setRenderBounds(0.75F, y_low, 0.875F, 0.8125F, y_high, 0.875F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                        renderBlocks.setRenderBounds(0.6875F, y_low_offset, 0.875F, 0.75F, y_high_offset, 0.875F + 0.0625F);
                        renderBlock(itemStack, x, y, z);
                    }

                    if (render_inside_handle)
                    {
                        renderBlocks.setRenderBounds(0.0F, y_low, 0.875F, 0.0625F, y_high, 0.875F + 0.0625F);
                        renderBlock(itemStack, x + 1, y, z);
                        renderBlocks.setRenderBounds(0.0625F, y_low_offset, 0.875F, 0.125F, y_high_offset, 0.875F + 0.0625F);
                        renderBlock(itemStack, x + 1, y, z);
                    }

                }

                break;
        }

        suppressDyeColor = false;
        suppressPattern = false;
        suppressOverlay = false;
    }

}
