package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
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

        renderBlocks.setOverrideBlockTexture(renderBlocks.getIconSafe(IconRegistry.icon_daylight_sensor_glass_top));

        renderBlocks.setRenderBounds(0.0625D, 0.1875D + yOffset, 0.0625D, 0.9375D, 0.25D + yOffset, 0.9375D); // Glass top
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        renderBlocks.setOverrideBlockTexture(Block.blockLapis.getBlockTextureFromSide(1));

        renderBlocks.setRenderBounds(0.125D, 0.0625D + yOffset, 0.125D, 0.875D, 0.1875D + yOffset, 0.875D); // Lapis middle
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        renderBlocks.setOverrideBlockTexture(Block.blockRedstone.getBlockTextureFromSide(1));

        renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.0625D, 0.125D, 0.1875D + yOffset, 0.9375D); // Redstone X-low wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.875D, 0.0625D + yOffset, 0.0625D, 0.9375D, 0.1875D + yOffset, 0.9375D); // Redstone X-high wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.0625D, 0.9375D, 0.1875D + yOffset, 0.125D); // Redstone Z-low wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0625D + yOffset, 0.875D, 0.9375D, 0.1875D + yOffset, 0.9375D); // Redstone Z-high wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);

        renderBlocks.clearOverrideBlockTexture();

        renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.0625D, 0.9375D, 0.0625D + yOffset, 0.9375D); // Bottom
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0D, 0.0D + yOffset, 0.0D, 0.0625D, 0.25D + yOffset, 1.0D); // X-low wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.9375D, 0.0D + yOffset, 0.0D, 1.0D, 0.25D + yOffset, 1.0D); // X-high wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.0D, 0.9375D, 0.25D + yOffset, 0.0625D); // Z-low wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.0D + yOffset, 0.9375D, 0.9375D, 0.25D + yOffset, 1.0D); // Z-high wall
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
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

            renderBlocks.enableAO = getEnableAO(Block.glass);

            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
            lightingHelper.setLightingYPos(Block.glass, x, y, z);
            lightingHelper.colorSide(Block.glass, x, y, z, 1, null);
            RenderHelper.renderFaceYPos(renderBlocks, x, y, z, IconRegistry.icon_daylight_sensor_glass_top);

            renderBlocks.enableAO = false;

            /* Render lapis inlay */

            renderBlocks.setRenderBounds(0.125D, 0.0625D, 0.125D, 0.875D, 0.1875D, 0.875D);
            renderBlock(Block.blockLapis, x, y, z);

            /* Render bordering redstone inlay */

            boolean isActive = DaylightSensor.isActive(TE);

            if (isActive) {
                disableAO = true;
                lightingHelper.setBrightnessOverride(lightingHelper.MAX_BRIGHTNESS);
            }
            
            for (int box = 0; box < 4; ++box) {
                
                switch (box) {
                    case 0:
                        renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.0625D, 0.125D, 0.1875D, 0.9375D);
                        break;
                    case 1:
                        renderBlocks.setRenderBounds(0.875D, 0.0625D, 0.0625D, 0.9375D, 0.1875D, 0.9375D);
                        break;
                    case 2:
                        renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.0625D, 0.9375D, 0.1875D, 0.125D);
                        break;
                    case 3:
                        renderBlocks.setRenderBounds(0.0625D, 0.0625D, 0.875D, 0.9375D, 0.1875D, 0.9375D);
                        break;
                }
                
                if (!isActive) {
                    lightingHelper.setTempFaceLightness(0.5F);
                }
                
                renderBlock(Block.blockRedstone, x, y, z);
                
            }

            lightingHelper.clearBrightnessOverride();
            disableAO = false;

            suppressDyeColor = false;
            suppressOverlay = false;
            suppressPattern = false;
        }

        /* Render coverBlock walls and bottom */

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

        renderBlocks.renderAllFaces = false;
        return true;
    }

}
