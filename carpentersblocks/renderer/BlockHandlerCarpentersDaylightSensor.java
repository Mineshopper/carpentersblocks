package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import carpentersblocks.data.DaylightSensor;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersDaylightSensor extends BlockDeterminantRender {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        Block temp_block = block;
        double yOffset = 0.375D;

        for (int box = 0; box < 11; ++box)
        {
            switch (box) {
            case 0: // Glass top
                temp_block = Block.glass;
                renderBlocks.setRenderBounds(0.0625D, 0.1875D + yOffset, 0.0625D, 0.9375D, 0.25D + yOffset, 0.9375D);
                renderBlocks.setOverrideBlockTexture(renderBlocks.getIconSafe(IconRegistry.icon_daylight_sensor_glass_top));
                break;
            case 1: // Lapis middle
                temp_block = Block.blockLapis;
                renderBlocks.setRenderBounds(0.125D, 0.0625D + yOffset, 0.125D, 0.875D, 0.1875D + yOffset, 0.875D);
                break;
            case 2: // Redstone X-low wall
                temp_block = Block.blockRedstone;
                renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.0625D, 0.125D, 0.1875D + yOffset, 0.9375D);
                break;
            case 3: // Redstone X-high wall
                temp_block = Block.blockRedstone;
                renderBlocks.setRenderBounds(0.875D, 0.0625D + yOffset, 0.0625D, 0.9375D, 0.1875D + yOffset, 0.9375D);
                break;
            case 4: // Redstone Z-low wall
                temp_block = Block.blockRedstone;
                renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.0625D, 0.9375D, 0.1875D + yOffset, 0.125D);
                break;
            case 5: // Redstone Z-high wall
                temp_block = Block.blockRedstone;
                renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.875D, 0.9375D, 0.1875D + yOffset, 0.9375D);
                break;
            case 6: // Generic bottom
                temp_block = BlockRegistry.blockCarpentersDaylightSensor;
                renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.0625D, 0.9375D, 0.0625D + yOffset, 0.9375D);
                break;
            case 7: // X-low wall
                temp_block = BlockRegistry.blockCarpentersDaylightSensor;
                renderBlocks.setRenderBounds(0.0D, 0.0D + yOffset, 0.0D, 0.0625D, 0.25D + yOffset, 1.0D);
                break;
            case 8: // X-high wall
                temp_block = BlockRegistry.blockCarpentersDaylightSensor;
                renderBlocks.setRenderBounds(0.9375D, 0.0D + yOffset, 0.0D, 1.0D, 0.25D + yOffset, 1.0D);
                break;
            case 9: // Z-low wall
                temp_block = BlockRegistry.blockCarpentersDaylightSensor;
                renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.0D, 0.9375D, 0.25D + yOffset, 0.0625D);
                break;
            case 10: // Z-high wall
                temp_block = BlockRegistry.blockCarpentersDaylightSensor;
                renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.9375D, 0.9375D, 0.25D + yOffset, 1.0D);
                break;
            }

            super.renderInventoryBlock(temp_block, metadata, modelID, renderBlocks);
            renderBlocks.clearOverrideBlockTexture();
        }
    }

    @Override
    /**
     * Renders block
     */
    protected boolean renderCarpentersBlock(int x, int y, int z)
    {
        Block block = BlockProperties.getCoverBlock(TE, 6);

        renderBlocks.renderAllFaces = true;

        if (shouldRenderOpaque())
        {
            suppressDyeColor = true;
            suppressOverlay = true;
            suppressPattern = true;

            /* Render glass inlay */

            VertexHelper.setOffset(-0.75D);
            Tessellator.instance.setBrightness(Block.glass.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
            Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            RenderHelper.renderFaceYPos(renderBlocks, x, y, z, IconRegistry.icon_daylight_sensor_glass_top);
            VertexHelper.clearOffset();

            /* Render lapis inlay */

            renderBlocks.setRenderBounds(0.125D, 0.0625D, 0.125D, 0.875D, 0.1875D, 0.875D);
            renderBlock(Block.blockLapis, x, y, z);

            /* Render bordering redstone inlay */

            boolean isActive = DaylightSensor.isActive(TE);

            if (isActive) {
                disableAO = true;
                lightingHelper.setBrightnessOverride(lightingHelper.MAX_BRIGHTNESS);
            } else {
                lightingHelper.setLightnessOverride(0.5F);
            }

            renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.0625D, 0.125D, 0.1875D, 0.9375D);
            renderBlock(Block.blockRedstone, x, y, z);
            renderBlocks.setRenderBounds(0.875D, 0.0625D, 0.0625D, 0.9375D, 0.1875D, 0.9375D);
            renderBlock(Block.blockRedstone, x, y, z);
            renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.1875D, 0.125D);
            renderBlock(Block.blockRedstone, x, y, z);
            renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.875D, 0.9375D, 0.1875D, 0.9375D);
            renderBlock(Block.blockRedstone, x, y, z);

            lightingHelper.clearLightnessOverride();
            lightingHelper.clearBrightnessOverride();
            disableAO = false;

            suppressDyeColor = false;
            suppressOverlay = false;
            suppressPattern = false;
        }

        /* Render coverBlock walls and bottom */

        if (shouldRenderBlock(block))
        {
            renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.0625D, 0.9375D);
            renderBlock(block, x, y, z);
            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.0625D, 0.25D, 1.0D);
            renderBlock(block, x, y, z);
            renderBlocks.setRenderBounds(0.9375D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
            renderBlock(block, x, y, z);
            renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.0D, 0.9375D, 0.25D, 0.0625D);
            renderBlock(block, x, y, z);
            renderBlocks.setRenderBounds(0.0625D, 0.0D, 0.9375D, 0.9375D, 0.25D, 1.0D);
            renderBlock(block, x, y, z);
        }

        renderBlocks.renderAllFaces = false;
        return true;
    }

}
