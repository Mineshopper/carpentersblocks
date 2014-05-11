package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import carpentersblocks.data.DaylightSensor;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersDaylightSensor extends BlockHandlerBase {

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
            suppressPattern = true;

            /* Render glass inlay */

            ItemStack glass = new ItemStack(Blocks.glass);
            float[] primaryRGB = { 1.0F, 1.0F, 1.0F };

            renderBlocks.enableAO = getEnableAO(glass);

            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
            lightingHelper.setupLightingYPos(glass, x, y, z);
            lightingHelper.setupColor(glass, Blocks.glass, x, y, z, 1, primaryRGB, null);
            RenderHelper.renderFaceYPos(renderBlocks, x, y, z, IconRegistry.icon_daylight_sensor_glass_top);

            renderBlocks.enableAO = false;

            /* Render lapis inlay */

            renderBlocks.setRenderBounds(0.125D, 0.0625D, 0.125D, 0.875D, 0.1875D, 0.875D);
            renderBlock(new ItemStack(Blocks.lapis_block), x, y, z);

            /* Render bordering redstone inlay */

            boolean isActive = DaylightSensor.isActive(TE);

            if (isActive) {
                disableAO = true;
                lightingHelper.setBrightnessOverride(lightingHelper.MAX_BRIGHTNESS);
            } else {
                lightingHelper.setLightnessOverride(0.5F);
            }

            ItemStack redstone = new ItemStack(Blocks.redstone_block);

            renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.0625D, 0.125D, 0.1875D, 0.9375D);
            renderBlock(redstone, x, y, z);
            renderBlocks.setRenderBounds(0.875D, 0.0625D, 0.0625D, 0.9375D, 0.1875D, 0.9375D);
            renderBlock(redstone, x, y, z);
            renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.1875D, 0.125D);
            renderBlock(redstone, x, y, z);
            renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.875D, 0.9375D, 0.1875D, 0.9375D);
            renderBlock(redstone, x, y, z);

            lightingHelper.clearLightnessOverride();
            lightingHelper.clearBrightnessOverride();
            disableAO = false;

            suppressDyeColor = false;
            suppressOverlay = false;
            suppressPattern = false;

        /* Render coverBlock walls and bottom */

        ItemStack itemStack = BlockProperties.getCover(TE, 6);

        renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.0625D, 0.25D, 1.0D);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(0.9375D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.0D, 0.9375D, 0.25D, 0.0625D);
        renderBlock(itemStack, x, y, z);
        renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.9375D, 0.9375D, 0.25D, 1.0D);
        renderBlock(itemStack, x, y, z);

        renderBlocks.renderAllFaces = false;
    }

}
