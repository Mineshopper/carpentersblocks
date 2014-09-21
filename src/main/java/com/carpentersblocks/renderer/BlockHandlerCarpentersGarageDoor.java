package com.carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.GarageDoor;
import com.carpentersblocks.renderer.helper.LightingHelper;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.util.registry.IconRegistry;

public class BlockHandlerCarpentersGarageDoor extends BlockHandlerBase {

    private GarageDoor data = new GarageDoor();
    ForgeDirection dir;
    boolean isOpen;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.4375D, 0.75D, 0.0D, 0.5625D, 1.0D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.4375D, 0.625D, 0.0D, 0.5625D, 0.75D, 0.3125D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.4375D, 0.625D, 0.6875D, 0.5625D, 0.75D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.4375D, 0.0D, 0.0D, 0.5625D, 0.625D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders ladder.
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        if (data.isVisible(TE)) {

            renderBlocks.renderAllFaces = true;

            isOpen = data.isOpen(TE);
            ItemStack itemStack = getCoverForRendering();
            dir = data.getDirection(TE);

            switch (data.getType(TE)) {
                case GarageDoor.TYPE_DEFAULT:
                    renderTypeDefault(itemStack, x, y, z);
                    break;
                case GarageDoor.TYPE_GLASS_TOP:
                    renderTypeGlassTop(itemStack, x, y, z);
                    break;
                case GarageDoor.TYPE_GLASS:
                    renderTypeGlass(itemStack, x, y, z);
                    break;
                case GarageDoor.TYPE_IRON:
                    renderTypeSiding(itemStack, x, y, z);
                    break;
            }

            renderBlocks.renderAllFaces = false;
        }
    }

    /**
     * Renders pane.
     * <p>
     * TODO: Revisit when alpha pass is properly implemented since alpha renders
     * both dirs during a single quad draw.
     */
    public void renderPartPane(IIcon icon, int x, int y, int z, float offset)
    {
        float LIGHTNESS = LightingHelper.LIGHTNESS[dir.ordinal()];

        Tessellator.instance.setBrightness(Blocks.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        Tessellator.instance.setColorOpaque_F(LIGHTNESS, LIGHTNESS, LIGHTNESS);

        switch (dir) {
            case NORTH:
                renderBlocks.setRenderBounds(0.0F, 0.0F, 1.0F - offset, 1.0F, 1.0F, 1.0F - offset);
                RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
                break;
            case SOUTH:
                renderBlocks.setRenderBounds(0.0F, 0.0F, offset, 1.0F, 1.0F, offset);
                RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
                break;
            case WEST:
                renderBlocks.setRenderBounds(1.0F - offset, 0.0F, 0.0F, 1.0F - offset, 1.0F, 1.0F);
                RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
                break;
            case EAST:
                renderBlocks.setRenderBounds(offset, 0.0F, 0.0F, offset, 1.0F, 1.0F);
                RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
                break;
            default: {}
        }
    }

    /**
     * Renders a default garage door at coordinates.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypeDefault(ItemStack itemStack, int x, int y, int z)
    {
        if (data.isOpen(TE)) {
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.5D, 0.125D, 1.0D, 1.0D, 0.25D, dir);
        } else {
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.75D, 0.125D, 1.0D, 1.0D, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.625D, 0.125D, 0.3125D, 0.75D, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.6875D, 0.625D, 0.125D, 1.0D, 0.75D, 0.25D, dir);
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.125D, 1.0D, 0.625D, 0.25D, dir);
        }
    }

    /**
     * Renders a glass top garage door at coordinates.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypeGlassTop(ItemStack itemStack, int x, int y, int z)
    {
        if (data.isOpen(TE)) {
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.5D, 0.125D, 1.0D, 1.0D, 0.25D, dir);
        } else {
            if (data.isTopmost(TE)) {
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.125D, 0.125D, 1.0D, 0.25D, dir); // Left vert
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.875D, 0.125D, 0.875D, 1.0D, 0.25D, dir); // center top
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.4375D, 0.125D, 0.875D, 0.5625D, 0.25D, dir); // center middle
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.0D, 0.125D, 0.875D, 0.125D, 0.25D, dir); // center bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.125D, 0.1875D, 0.875D, 0.4375D, 0.25D, dir); // center panel bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.875D, 0.0D, 0.125D, 1.0D, 1.0D, 0.25D, dir); // Right vert
                renderPartPane(IconRegistry.icon_garage_glass_top, x, y, z, 0.1875F);
            } else {
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.125D, 0.125D, 1.0D, 0.25D, dir); // Left vert
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.875D, 0.125D, 0.875D, 1.0D, 0.25D, dir); // center top
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.4375D, 0.125D, 0.875D, 0.5625D, 0.25D, dir); // center middle
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.0D, 0.125D, 0.875D, 0.125D, 0.25D, dir); // center bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D, 0.25D, dir); // center panel bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.875D, 0.0D, 0.125D, 1.0D, 1.0D, 0.25D, dir); // Right vert
            }
        }
    }

    /**
     * Renders a glass garage door at coordinates.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypeGlass(ItemStack itemStack, int x, int y, int z)
    {
        if (data.isOpen(TE)) {
            renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.5D, 0.125D, 1.0D, 1.0D, 0.25D, dir);
        } else {
            if (data.isTopmost(TE)) {
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.125D, 0.125D, 1.0D, 0.25D, dir); // Left vert
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.875D, 0.125D, 0.875D, 1.0D, 0.25D, dir); // center top
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.4375D, 0.125D, 0.875D, 0.5625D, 0.25D, dir); // center middle
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.0D, 0.125D, 0.875D, 0.125D, 0.25D, dir); // center bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.125D, 0.1875D, 0.875D, 0.4375D, 0.25D, dir); // center panel bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.875D, 0.0D, 0.125D, 1.0D, 1.0D, 0.25D, dir); // Right vert
                renderPartPane(IconRegistry.icon_garage_glass_top, x, y, z, 0.1875F);
            } else {
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.125D, 0.125D, 1.0D, 0.25D, dir); // Left vert
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.875D, 0.125D, 0.875D, 1.0D, 0.25D, dir); // center top
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.4375D, 0.125D, 0.875D, 0.5625D, 0.25D, dir); // center middle
                renderBlockWithRotation(itemStack, x, y, z, 0.125D, 0.0D, 0.125D, 0.875D, 0.125D, 0.25D, dir); // center bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D, 0.25D, dir); // center panel bottom
                renderBlockWithRotation(itemStack, x, y, z, 0.875D, 0.0D, 0.125D, 1.0D, 1.0D, 0.25D, dir); // Right vert
            }
        }
    }

    /**
     * Renders a siding garage door at coordinates.
     *
     * @param itemStack the {@link ItemStack}
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public void renderTypeSiding(ItemStack itemStack, int x, int y, int z)
    {
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.25D, dir);
    }

}
