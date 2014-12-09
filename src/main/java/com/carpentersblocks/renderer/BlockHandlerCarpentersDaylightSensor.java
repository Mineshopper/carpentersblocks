package com.carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.block.BlockCarpentersDaylightSensor;
import com.carpentersblocks.data.DaylightSensor;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersDaylightSensor extends BlockHandlerBase {

    private static ItemStack lapis = new ItemStack(Blocks.lapis_block);
    private static ItemStack redstone = new ItemStack(Blocks.redstone_block);

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        double yOffset = 0.375D;

        /* Glass inset */

        renderBlocks.setOverrideBlockTexture(renderBlocks.getIconSafe(IconRegistry.icon_daylight_sensor_glass_top));
        renderBlocks.setRenderBounds(0.0625D, 0.1875D + yOffset, 0.0625D, 0.9375D, 0.25D + yOffset, 0.9375D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.clearOverrideBlockTexture();

        /* Lapis inset */

        renderBlocks.setRenderBounds(0.125D, 0.0625D + yOffset, 0.125D, 0.875D, 0.1875D + yOffset, 0.875D);
        super.renderInventoryBlock(Blocks.lapis_block, metadata, modelID, renderBlocks);

        /* Redstone inset */

        renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.0625D, 0.125D, 0.1875D + yOffset, 0.9375D);
        super.renderInventoryBlock(Blocks.redstone_block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.875D, 0.0625D + yOffset, 0.0625D, 0.9375D, 0.1875D + yOffset, 0.9375D);
        super.renderInventoryBlock(Blocks.redstone_block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.0625D, 0.9375D, 0.1875D + yOffset, 0.125D);
        super.renderInventoryBlock(Blocks.redstone_block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.875D, 0.9375D, 0.1875D + yOffset, 0.9375D);
        super.renderInventoryBlock(Blocks.redstone_block, metadata, modelID, renderBlocks);

        /* Bottom */

        renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.0625D, 0.9375D, 0.0625D + yOffset, 0.9375D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0D, 0.0D + yOffset, 0.0D, 0.0625D, 0.25D + yOffset, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.9375D, 0.0D + yOffset, 0.0D, 1.0D, 0.25D + yOffset, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.0D, 0.9375D, 0.25D + yOffset, 0.0625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.9375D, 0.9375D, 0.25D + yOffset, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }

    @Override
    /**
     * Renders block
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;

        suppressDyeColor = true;
        suppressOverlay = true;
        suppressChiselDesign = true;

        DaylightSensor data = new DaylightSensor();
        ((BlockCarpentersDaylightSensor)srcBlock).setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);

        ForgeDirection dir = data.getDirection(TE).getOpposite();

        /* Render glass pane */

        renderPane(IconRegistry.icon_daylight_sensor_glass_top, x, y, z, dir.getOpposite(), true, false);

        /* Render lapis inlay */

        renderBlockWithRotation(lapis, x, y, z, 0.125D, 0.125D, 0.8125D, 0.875D, 0.875D, 0.9375D, dir);

        /* Render bordering redstone inlay */

        boolean isActive = data.isActive(TE);

        if (isActive) {
            disableAO = true;
            lightingHelper.setBrightnessOverride(lightingHelper.MAX_BRIGHTNESS);
        } else {
            lightingHelper.setLightnessOverride(0.5F);
        }

        renderBlockWithRotation(redstone, x, y, z, 0.0625D, 0.0625D, 0.8125D, 0.125D, 0.9375D, 0.9375D, dir);
        renderBlockWithRotation(redstone, x, y, z, 0.125D, 0.0625D, 0.8125D, 0.875D, 0.125D, 0.9375D, dir);
        renderBlockWithRotation(redstone, x, y, z, 0.125D, 0.875D, 0.8125D, 0.875D, 0.9375D, 0.9375D, dir);
        renderBlockWithRotation(redstone, x, y, z, 0.875D, 0.0625D, 0.8125D, 0.9375D, 0.9375D, 0.9375D, dir);

        lightingHelper.clearLightnessOverride();
        lightingHelper.clearBrightnessOverride();
        disableAO = false;

        suppressDyeColor = false;
        suppressOverlay = false;
        suppressChiselDesign = false;

        /* Render coverBlock walls and bottom */

        ItemStack itemStack = getCoverForRendering();

        renderBlockWithRotation(itemStack, x, y, z, 0.0625D, 0.0625D, 0.9375D, 0.9375D, 0.9375D, 1.0D, dir); // Base
        renderBlockWithRotation(itemStack, x, y, z, 0.0D, 0.0D, 0.75D, 0.0625D, 1.0D, 1.0D, dir);
        renderBlockWithRotation(itemStack, x, y, z, 0.0625D, 0.0D, 0.75D, 0.9375D, 0.0625D, 1.0D, dir);
        renderBlockWithRotation(itemStack, x, y, z, 0.0625D, 0.9375D, 0.75D, 0.9375D, 1.0D, 1.0D, dir);
        renderBlockWithRotation(itemStack, x, y, z, 0.9375D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D, dir);

        renderBlocks.renderAllFaces = false;
    }

}
