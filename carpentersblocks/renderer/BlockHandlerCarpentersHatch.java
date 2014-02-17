package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import carpentersblocks.block.BlockCarpentersHatch;
import carpentersblocks.data.Hatch;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersHatch extends BlockHandlerBase {
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.0D, 0.4375D, 0.0D, 1.0D, 0.5625D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        
        renderBlocks.setOverrideBlockTexture(Blocks.iron_block.getBlockTextureFromSide(2));
        
        renderBlocks.setRenderBounds(0.0625D, 0.5625D, 0.375D, 0.125D, 0.625D, 0.4375D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625, 0.5625D, 0.5625D, 0.125D, 0.625D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        renderBlocks.setRenderBounds(0.0625D, 0.625D, 0.375D, 0.125D, 0.6875D, 0.625D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        
        renderBlocks.clearOverrideBlockTexture();
    }
    
    @Override
    /**
     * Renders block
     */
    protected boolean renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;
        
        Block block = BlockProperties.getCover(TE, 6);
        int type = Hatch.getType(TE);
        
        switch (type) {
            case Hatch.TYPE_WINDOW:
                renderHollowHatch(block, x, y, z);
                break;
            case Hatch.TYPE_SCREEN:
                renderHollowHatch(block, x, y, z);
                break;
            case Hatch.TYPE_FRENCH_WINDOW:
                renderFrenchWindowHatch(block, x, y, z);
                break;
            case Hatch.TYPE_PANEL:
                renderPanelHatch(block, x, y, z);
                break;
            case Hatch.TYPE_HIDDEN:
                renderHiddenHatch(block, x, y, z);
                break;
        }
        
        renderBlocks.renderAllFaces = false;
        
        return true;
    }
    
    /**
     * Renders hidden hatch at given coordinates
     */
    private void renderHiddenHatch(Block block, int x, int y, int z)
    {
        if (shouldRenderBlock(block)) {
            BlockCarpentersHatch blockRef = (BlockCarpentersHatch) BlockRegistry.blockCarpentersHatch;
            blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
            renderBlock(block, x, y, z);
        }
        
        if (shouldRenderOpaque()) {
            renderHandle(Blocks.iron_block, x, y, z, true, false);
        }
    }
    
    /**
     * Renders a window or screen hatch at given coordinates
     */
    private void renderHollowHatch(Block block, int x, int y, int z)
    {
        int dir = Hatch.getDir(TE);
        int pos = Hatch.getPos(TE);
        int state = Hatch.getState(TE);
        
        boolean path_on_x = false;
        boolean path_on_y = false;
        float path_offset = 0.0F;
        
        float x_low = 0.0F;
        float y_low = 0.0F;
        float z_low = 0.0F;
        float x_high = 1.0F;
        float y_high = 1.0F;
        float z_high = 1.0F;
        
        /*
         * Set offsets
         */
        if (state == Hatch.STATE_CLOSED) {
            path_on_y = true;
            if (pos == Hatch.POSITION_LOW) {
                y_high = 0.1875F;
                path_offset = 0.09375F;
            } else {
                y_low = 0.8125F;
                path_offset = 0.90625F;
            }
        } else {
            switch (dir)
            {
                case Hatch.DIR_X_NEG:
                    x_low = 0.8125F;
                    path_offset = 0.09375F;
                    break;
                case Hatch.DIR_X_POS:
                    x_high = 0.1875F;
                    path_offset = 0.90625F;
                    break;
                case Hatch.DIR_Z_NEG:
                    z_low = 0.8125F;
                    path_on_x = true;
                    path_offset = 0.09375F;
                    break;
                case Hatch.DIR_Z_POS:
                    z_high = 0.1875F;
                    path_on_x = true;
                    path_offset = 0.90625F;
                    break;
            }
        }
        
        if (shouldRenderBlock(block)) {
            
            /* Draw sides */
            
            if (path_on_x) {
                
                renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, 0.0F, z_low, 0.8125F, 0.1875F, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, 0.8125F, z_low, 0.8125F, 1.0F, z_high);
                renderBlock(block, x, y, z);
                
            } else if (path_on_y) {
                
                renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, y_low, 0.0F, 0.8125F, y_high, 0.1875F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, y_low, 0.8125F, 0.8125F, y_high, 1.0F);
                renderBlock(block, x, y, z);
                
            } else {
                
                renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_high, y_high, 0.1875F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, y_low, 0.8125F, x_high, y_high, 1.0F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, 0.0F, 0.1875F, x_high, 0.1875F, 0.8125F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, 0.8125F, 0.1875F, x_high, 1.0F, 0.8125F);
                renderBlock(block, x, y, z);
                
            }
            
        }
        
        if (shouldRenderOpaque()) {
            
            IIcon icon;
            
            if (Hatch.getType(TE) == Hatch.TYPE_SCREEN) {
                icon = IconRegistry.icon_hatch_screen;
            } else {
                icon = IconRegistry.icon_hatch_glass;
            }
            
            suppressDyeColor = true;
            
            if (path_on_x) {
                
                renderBlocks.setRenderBounds(0.1875F, 0.1875F, 0.0F, 0.8125F, 0.8125F, 1.0F);
                
                lightingHelper.setLightingZPos(Blocks.glass, x, y, z);
                lightingHelper.colorSide(Blocks.glass, x, y, z, 3, null);
                VertexHelper.setOffset(-path_offset);
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
                
                if (!renderAlphaOverride) {
                    lightingHelper.setLightingZNeg(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 2, null);
                    VertexHelper.setOffset(-(1 - path_offset));
                    RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
                }
                
            } else if (path_on_y) {
                
                renderBlocks.setRenderBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
                
                if (renderAlphaOverride) {
                    
                    /* On alpha pass, YNeg face is drawn on both sides. */
                    
                    lightingHelper.setLightingYPos(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 0, null);
                    VertexHelper.setOffset(-path_offset);
                    RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
                    
                } else {
                    
                    lightingHelper.setLightingYNeg(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 0, null);
                    VertexHelper.setOffset(-path_offset);
                    RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
                    
                    if (!renderAlphaOverride) {
                        lightingHelper.setLightingYPos(Blocks.glass, x, y, z);
                        lightingHelper.colorSide(Blocks.glass, x, y, z, 1, null);
                        VertexHelper.setOffset(-(1 - path_offset));
                        RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
                    }
                    
                }
                
            } else {
                
                renderBlocks.setRenderBounds(0.0F, 0.1875F, 0.1875F, 1.0F, 0.8125F, 0.8125F);
                
                lightingHelper.setLightingXPos(Blocks.glass, x, y, z);
                lightingHelper.colorSide(Blocks.glass, x, y, z, 5, null);
                VertexHelper.setOffset(-path_offset);
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
                
                if (!renderAlphaOverride) {
                    lightingHelper.setLightingXNeg(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 4, null);
                    VertexHelper.setOffset(-(1 - path_offset));
                    RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
                }
                
            }
            
            VertexHelper.clearOffset();
            suppressDyeColor = false;
            
            renderHandle(Blocks.iron_block, x, y, z, true, true);
        }
    }
    
    /**
     * Renders a French window hatch at given coordinates
     */
    private void renderFrenchWindowHatch(Block block, int x, int y, int z)
    {
        int dir = Hatch.getDir(TE);
        int pos = Hatch.getPos(TE);
        int state = Hatch.getState(TE);
        
        boolean path_on_x = false;
        boolean path_on_y = false;
        float path_offset = 0.0F;
        
        float x_low = 0.0F;
        float y_low = 0.0F;
        float z_low = 0.0F;
        float x_high = 1.0F;
        float y_high = 1.0F;
        float z_high = 1.0F;
        
        /* Set offsets */
        
        if (state == Hatch.STATE_CLOSED) {
            
            path_on_y = true;
            if (pos == Hatch.POSITION_LOW) {
                y_high = 0.1875F;
                path_offset = 0.09375F;
            } else {
                y_low = 0.8125F;
                path_offset = 0.90625F;
            }
            
        } else {
            
            switch (dir)
            {
                case Hatch.DIR_X_NEG:
                    x_low = 0.8125F;
                    path_offset = 0.09375F;
                    break;
                case Hatch.DIR_X_POS:
                    x_high = 0.1875F;
                    path_offset = 0.90625F;
                    break;
                case Hatch.DIR_Z_NEG:
                    z_low = 0.8125F;
                    path_on_x = true;
                    path_offset = 0.09375F;
                    break;
                case Hatch.DIR_Z_POS:
                    z_high = 0.1875F;
                    path_on_x = true;
                    path_offset = 0.90625F;
                    break;
            }
            
        }
        
        if (shouldRenderBlock(block)) {
            
            /* Draw sides */
            
            if (path_on_x) {
                
                renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, 0.0F, z_low, 0.8125F, 0.1875F, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, 0.8125F, z_low, 0.8125F, 1.0F, z_high);
                renderBlock(block, x, y, z);
                
            } else if (path_on_y) {
                
                renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, y_low, 0.0F, 0.8125F, y_high, 0.1875F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, y_low, 0.8125F, 0.8125F, y_high, 1.0F);
                renderBlock(block, x, y, z);
                
            } else {
                
                renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_high, y_high, 0.1875F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, y_low, 0.8125F, x_high, y_high, 1.0F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, 0.0F, 0.1875F, x_high, 0.1875F, 0.8125F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, 0.8125F, 0.1875F, x_high, 1.0F, 0.8125F);
                renderBlock(block, x, y, z);
                
            }
            
            float temp_x_low = x_low;
            float temp_x_high = x_high;
            float temp_y_low = y_low;
            float temp_y_high = y_high;
            float temp_z_low = z_low;
            float temp_z_high = z_high;
            
            if (path_on_x) {
                
                temp_z_low += 0.0625F;
                temp_z_high -= 0.0625F;
                renderBlocks.setRenderBounds(0.1875F, 0.4375F, temp_z_low, 0.8125F, 0.5625F, temp_z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.4375F, 0.1875F, temp_z_low, 0.5625F, 0.4375F, temp_z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.4375F, 0.5625F, temp_z_low, 0.5625F, 0.8125F, temp_z_high);
                renderBlock(block, x, y, z);
                
            } else if (path_on_y) {
                
                temp_y_low += 0.0625F;
                temp_y_high -= 0.0625F;
                renderBlocks.setRenderBounds(0.1875F, temp_y_low, 0.4375F, 0.8125F, temp_y_high, 0.5625F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.4375F, temp_y_low, 0.1875F, 0.5625F, temp_y_high, 0.4375F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.4375F, temp_y_low, 0.5625F, 0.5625F, temp_y_high, 0.8125F);
                renderBlock(block, x, y, z);
                
            } else {
                
                temp_x_low += 0.0625F;
                temp_x_high -= 0.0625F;
                renderBlocks.setRenderBounds(temp_x_low, 0.4375F, 0.1875F, temp_x_high, 0.5625F, 0.8125F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(temp_x_low, 0.1875F, 0.4375F, temp_x_high, 0.4375F, 0.5625F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(temp_x_low, 0.5625F, 0.4375F, temp_x_high, 0.8125F, 0.5625F);
                renderBlock(block, x, y, z);
                
            }
            
        }
        
        if (shouldRenderOpaque()) {
            
            suppressDyeColor = true;
            
            if (path_on_x) {
                
                renderBlocks.setRenderBounds(0.1875F, 0.1875F, 0.0F, 0.8125F, 0.8125F, 1.0F);
                
                lightingHelper.setLightingZPos(Blocks.glass, x, y, z);
                lightingHelper.colorSide(Blocks.glass, x, y, z, 3, null);
                VertexHelper.setOffset(-path_offset);
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, IconRegistry.icon_hatch_french_glass);
                
                if (!renderAlphaOverride) {
                    lightingHelper.setLightingZNeg(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 2, null);
                    VertexHelper.setOffset(-(1 - path_offset));
                    RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, IconRegistry.icon_hatch_french_glass);
                }
                
            } else if (path_on_y) {
                
                renderBlocks.setRenderBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
                
                if (renderAlphaOverride) {
                    
                    /* On alpha pass, YNeg face is drawn on both sides. */
                    
                    lightingHelper.setLightingYPos(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 0, null);
                    VertexHelper.setOffset(-path_offset);
                    RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, IconRegistry.icon_hatch_french_glass);
                    
                } else {
                    
                    lightingHelper.setLightingYNeg(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 0, null);
                    VertexHelper.setOffset(-path_offset);
                    RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, IconRegistry.icon_hatch_french_glass);
                    
                    if (!renderAlphaOverride) {
                        lightingHelper.setLightingYPos(Blocks.glass, x, y, z);
                        lightingHelper.colorSide(Blocks.glass, x, y, z, 1, null);
                        VertexHelper.setOffset(-(1 - path_offset));
                        RenderHelper.renderFaceYPos(renderBlocks, x, y, z, IconRegistry.icon_hatch_french_glass);
                    }
                    
                }
                
            } else {
                
                renderBlocks.setRenderBounds(0.0F, 0.1875F, 0.1875F, 1.0F, 0.8125F, 0.8125F);
                
                lightingHelper.setLightingXPos(Blocks.glass, x, y, z);
                lightingHelper.colorSide(Blocks.glass, x, y, z, 5, null);
                VertexHelper.setOffset(-path_offset);
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, IconRegistry.icon_hatch_french_glass);
                
                if (!renderAlphaOverride) {
                    lightingHelper.setLightingXNeg(Blocks.glass, x, y, z);
                    lightingHelper.colorSide(Blocks.glass, x, y, z, 4, null);
                    VertexHelper.setOffset(-(1 - path_offset));
                    RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, IconRegistry.icon_hatch_french_glass);
                }
                
            }
            
            VertexHelper.clearOffset();
            suppressDyeColor = false;
            
            renderHandle(Blocks.iron_block, x, y, z, true, true);
        }
    }
    
    /**
     * Renders a panel hatch at given coordinates
     */
    private void renderPanelHatch(Block block, int x, int y, int z)
    {
        int dir = Hatch.getDir(TE);
        int pos = Hatch.getPos(TE);
        int state = Hatch.getState(TE);
        
        boolean path_on_x = false;
        boolean path_on_y = false;
        float x_low = 0.0F;
        float y_low = 0.0F;
        float z_low = 0.0F;
        float x_high = 1.0F;
        float y_high = 1.0F;
        float z_high = 1.0F;
        
        /* Set offsets */
        
        if (state == Hatch.STATE_CLOSED) {
            
            path_on_y = true;
            if (pos == Hatch.POSITION_LOW) {
                y_high = 0.1875F;
            } else {
                y_low = 0.8125F;
            }
            
        } else {
            
            switch (dir)
            {
                case Hatch.DIR_X_NEG:
                    x_low = 0.8125F;
                    break;
                case Hatch.DIR_X_POS:
                    x_high = 0.1875F;
                    break;
                case Hatch.DIR_Z_NEG:
                    z_low = 0.8125F;
                    path_on_x = true;
                    break;
                case Hatch.DIR_Z_POS:
                    z_high = 0.1875F;
                    path_on_x = true;
                    break;
            }
            
        }
        
        if (shouldRenderBlock(block)) {
            
            /* Draw sides */
            
            if (path_on_x) {
                
                renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, 0.0F, z_low, 0.8125F, 0.1875F, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, 0.8125F, z_low, 0.8125F, 1.0F, z_high);
                renderBlock(block, x, y, z);
                
            } else if (path_on_y) {
                
                renderBlocks.setRenderBounds(0.0F, y_low, z_low, 0.1875F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.8125F, y_low, z_low, 1.0F, y_high, z_high);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, y_low, 0.0F, 0.8125F, y_high, 0.1875F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(0.1875F, y_low, 0.8125F, 0.8125F, y_high, 1.0F);
                renderBlock(block, x, y, z);
                
            } else {
                
                renderBlocks.setRenderBounds(x_low, y_low, 0.0F, x_high, y_high, 0.1875F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, y_low, 0.8125F, x_high, y_high, 1.0F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, 0.0F, 0.1875F, x_high, 0.1875F, 0.8125F);
                renderBlock(block, x, y, z);
                renderBlocks.setRenderBounds(x_low, 0.8125F, 0.1875F, x_high, 1.0F, 0.8125F);
                renderBlock(block, x, y, z);
                
            }
            
            float temp_x_low = x_low;
            float temp_x_high = x_high;
            float temp_y_low = y_low;
            float temp_y_high = y_high;
            float temp_z_low = z_low;
            float temp_z_high = z_high;
            
            /* Draw thin center panel */
            
            if (path_on_x) {
                
                temp_x_low = 0.1875F;
                temp_x_high = 0.8125F;
                temp_y_low = 0.1875F;
                temp_y_high = 0.8125F;
                temp_z_low += 0.0625F;
                temp_z_high -= 0.0625F;
                
            } else if (path_on_y) {
                
                temp_x_low = 0.1875F;
                temp_x_high = 0.8125F;
                temp_y_low += 0.0625F;
                temp_y_high -= 0.0625F;
                temp_z_low = 0.1875F;
                temp_z_high = 0.8125F;
                
            } else {
                
                temp_x_low += 0.0625F;
                temp_x_high -= 0.0625F;
                temp_y_low = 0.1875F;
                temp_y_high = 0.8125F;
                temp_z_low = 0.1875F;
                temp_z_high = 0.8125F;
                
            }
            
            renderBlocks.setRenderBounds(temp_x_low, temp_y_low, temp_z_low, temp_x_high, temp_y_high, temp_z_high);
            renderBlock(block, x, y, z);
            
            temp_x_low = x_low;
            temp_x_high = x_high;
            temp_y_low = y_low;
            temp_y_high = y_high;
            temp_z_low = z_low;
            temp_z_high = z_high;
            
            /* Draw center panel */
            
            if (path_on_x) {
                
                temp_x_low = 0.3125F;
                temp_x_high = 0.6875F;
                temp_y_low = 0.3125F;
                temp_y_high = 0.6875F;
                
            } else if (path_on_y) {
                
                temp_x_low = 0.3125F;
                temp_x_high = 0.6875F;
                temp_z_low = 0.3125F;
                temp_z_high = 0.6875F;
                
            } else {
                
                temp_y_low = 0.3125F;
                temp_y_high = 0.6875F;
                temp_z_low = 0.3125F;
                temp_z_high = 0.6875F;
                
            }
            
            renderBlocks.setRenderBounds(temp_x_low, temp_y_low, temp_z_low, temp_x_high, temp_y_high, temp_z_high);
            renderBlock(block, x, y, z);
            
        }
        
        if (shouldRenderOpaque()) {
            renderHandle(Blocks.iron_block, x, y, z, true, true);
        }
    }
    
    /**
     * Renders a hatch handle for the given coordinates
     */
    private void renderHandle(Block handleBlock, int x, int y, int z, boolean render_inside_handle, boolean render_outside_handle)
    {
        if (!render_inside_handle && !render_outside_handle) {
            return;
        }
        
        int dir = Hatch.getDir(TE);
        int pos = Hatch.getPos(TE);
        int state = Hatch.getState(TE);
        
        float thickness = 0.1875F;
        
        if (Hatch.getType(TE) == Hatch.TYPE_HIDDEN) {
            thickness = 0.125F;
        }
        
        if (pos == Hatch.POSITION_LOW)
        {
            if (state == Hatch.STATE_CLOSED)
            {
                switch (dir) {
                    case Hatch.DIR_X_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.0625F, thickness, 0.375F, 0.125F, thickness + 0.0625F, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.0625F, thickness, 0.5625F, 0.125F, thickness + 0.0625F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.0625F, thickness + 0.0625F, 0.375F, 0.125F, thickness + 0.125F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.0625F, 0.9375F, 0.375F, 0.125F, 1.0F, 0.4375F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.0625F, 0.9375F, 0.5625F, 0.125F, 1.0F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.0625F, 0.875F, 0.375F, 0.125F, 0.9375F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                        }
                        
                        break;
                    case Hatch.DIR_X_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.875F, thickness, 0.375F, 0.9375F, thickness + 0.0625F, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.875F, thickness, 0.5625F, 0.9375F, thickness + 0.0625F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.875F, thickness + 0.0625F, 0.375F, 0.9375F, thickness + 0.125F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.875F, 0.9375F, 0.375F, 0.9375F, 1.0F, 0.4375F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.875F, 0.9375F, 0.5625F, 0.9375F, 1.0F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.875F, 0.875F, 0.375F, 0.9375F, 0.9375F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, thickness, 0.0625F, 0.4375F, thickness + 0.0625F, 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, thickness, 0.0625F, 0.625F, thickness + 0.0625F, 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, thickness + 0.0625F, 0.0625F, 0.625F, thickness + 0.125F, 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.9375F, 0.0625F, 0.4375F, 1.0F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.9375F, 0.0625F, 0.625F, 1.0F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 0.0625F, 0.625F, 0.9375F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, thickness, 0.875F, 0.4375F, thickness + 0.0625F, 0.9375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, thickness, 0.875F, 0.625F, thickness + 0.0625F, 0.9375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, thickness + 0.0625F, 0.875F, 0.625F, thickness + 0.125F, 0.9375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.9375F, 0.875F, 0.4375F, 1.0F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.9375F, 0.875F, 0.625F, 1.0F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 0.875F, 0.625F, 0.9375F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y - 1, z);
                        }
                        
                        break;
                }
                
            } else {
                
                switch (dir) {
                    case Hatch.DIR_X_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.0F, 0.875F, 0.375F, 0.0625F, 0.9375F, 0.4375F);
                            renderBlock(Blocks.iron_block, x + 1, y, z);
                            renderBlocks.setRenderBounds(0.0F, 0.875F, 0.5625F, 0.0625F, 0.9375F, 0.625F);
                            renderBlock(Blocks.iron_block, x + 1, y, z);
                            renderBlocks.setRenderBounds(0.0625F, 0.875F, 0.375F, 0.125F, 0.9375F, 0.625F);
                            renderBlock(Blocks.iron_block, x + 1, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(1.0F - thickness - 0.0625F, 0.875F, 0.375F, 1.0F - thickness, 0.9375F, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(1.0F - thickness - 0.0625F, 0.875F, 0.5625F, 1.0F - thickness, 0.9375F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(1.0F - thickness - 0.125F, 0.875F, 0.375F, 1.0F - thickness - 0.0625F, 0.9375F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                    case Hatch.DIR_X_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.9375F, 0.875F, 0.375F, 1.0F, 0.9375F, 0.4375F);
                            renderBlock(Blocks.iron_block, x - 1, y, z);
                            renderBlocks.setRenderBounds(0.9375F, 0.875F, 0.5625F, 1.0F, 0.9375F, 0.625F);
                            renderBlock(Blocks.iron_block, x - 1, y, z);
                            renderBlocks.setRenderBounds(0.875F, 0.875F, 0.375F, 0.9375F, 0.9375F, 0.625F);
                            renderBlock(Blocks.iron_block, x - 1, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(thickness, 0.875F, 0.375F, thickness + 0.0625F, 0.9375F, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(thickness, 0.875F, 0.5625F, thickness + 0.0625F, 0.9375F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(thickness + 0.0625F, 0.875F, 0.375F, thickness + 0.125F, 0.9375F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 0.0F, 0.4375F, 0.9375F, 0.0625F);
                            renderBlock(Blocks.iron_block, x, y, z + 1);
                            renderBlocks.setRenderBounds(0.5625F, 0.875F, 0.0F, 0.625F, 0.9375F, 0.0625F);
                            renderBlock(Blocks.iron_block, x, y, z + 1);
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 0.0625F, 0.625F, 0.9375F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y, z + 1);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 1.0F - thickness - 0.0625F, 0.4375F, 0.9375F, 1.0F - thickness);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.875F, 1.0F - thickness - 0.0625F, 0.625F, 0.9375F, 1.0F - thickness);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 1.0F - thickness - 0.125F, 0.625, 0.9375F, 1.0F - thickness - 0.0625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 0.9375F, 0.4375F, 0.9375F, 1.0F);
                            renderBlock(Blocks.iron_block, x, y, z - 1);
                            renderBlocks.setRenderBounds(0.5625F, 0.875F, 0.9375F, 0.625F, 0.9375F, 1.0F);
                            renderBlock(Blocks.iron_block, x, y, z - 1);
                            renderBlocks.setRenderBounds(0.375F, 0.875F, 0.875F, 0.625, 0.9375F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y, z - 1);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.875F, thickness, 0.4375F, 0.9375F, thickness + 0.0625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.875F, thickness, 0.625F, 0.9375F, thickness + 0.0625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, 0.875F, thickness + 0.0625F, 0.625F, 0.9375F, thickness + 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                }
            }
            
        } else {
            
            if (state == Hatch.STATE_CLOSED)
            {
                switch (dir) {
                    case Hatch.DIR_X_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.0625F, 1.0F - thickness - 0.0625F, 0.375F, 0.125F, 1.0F - thickness, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.0625F, 1.0F - thickness - 0.0625F, 0.5625F, 0.125F, 1.0F - thickness, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.0625F, 1.0F - thickness - 0.125F, 0.375F, 0.125F, 1.0F - thickness - 0.0625F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.0625F, 0.0F, 0.375F, 0.125F, 0.0625F, 0.4375F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.0625F, 0.0F, 0.5625F, 0.125F, 0.0625F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.0625F, 0.0625F, 0.375F, 0.125F, 0.125F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                        }
                        
                        break;
                    case Hatch.DIR_X_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.875F, 1.0F - thickness - 0.0625F, 0.375F, 0.9375F, 1.0F - thickness, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.875F, 1.0F - thickness - 0.0625F, 0.5625F, 0.9375F, 1.0F - thickness, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.875F, 1.0F - thickness - 0.125F, 0.375F, 0.9375F, 1.0F - thickness - 0.0625F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.875F, 0.0F, 0.375F, 0.9375F, 0.0625F, 0.4375F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.875F, 0.0F, 0.5625F, 0.9375F, 0.0625F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.875F, 0.0625F, 0.375F, 0.9375F, 0.125F, 0.625F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 1.0F - thickness - 0.0625F, 0.0625F, 0.4375F, 1.0F - thickness, 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, 1.0F - thickness - 0.0625F, 0.0625F, 0.625F, 1.0F - thickness, 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, 1.0F - thickness - 0.125F, 0.0625F, 0.625F, 1.0F - thickness - 0.0625F, 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.0F, 0.0625F, 0.4375F, 0.0625F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.0F, 0.0625F, 0.625F, 0.0625F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.0625F, 0.625F, 0.125F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 1.0F - thickness - 0.0625F, 0.875F, 0.4375F, 1.0F - thickness, 0.9375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, 1.0F - thickness - 0.0625F, 0.875F, 0.625F, 1.0F - thickness, 0.9375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, 1.0F - thickness - 0.125F, 0.875F, 0.625F, 1.0F - thickness - 0.0625F, 0.9375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.0F, 0.875F, 0.4375F, 0.0625F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.0F, 0.875F, 0.625F, 0.0625F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.875F, 0.625F, 0.125F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y + 1, z);
                        }
                        
                        break;
                }
                
            } else {
                
                switch (dir) {
                    case Hatch.DIR_X_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.0F, 0.0625F, 0.375F, 0.0625F, 0.125F, 0.4375F);
                            renderBlock(Blocks.iron_block, x + 1, y, z);
                            renderBlocks.setRenderBounds(0.0F, 0.0625F, 0.5625F, 0.0625F, 0.125F, 0.625F);
                            renderBlock(Blocks.iron_block, x + 1, y, z);
                            renderBlocks.setRenderBounds(0.0625F, 0.0625F, 0.375F, 0.125F, 0.125F, 0.625F);
                            renderBlock(Blocks.iron_block, x + 1, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(1.0F - thickness - 0.0625F, 0.0625F, 0.375F, 1.0F - thickness, 0.125F, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(1.0F - thickness - 0.0625F, 0.0625F, 0.5625F, 1.0F - thickness, 0.125F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(1.0F - thickness - 0.125F, 0.0625F, 0.375F, 1.0F - thickness - 0.0625F, 0.125F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                    case Hatch.DIR_X_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.9375F, 0.0625F, 0.375F, 1.0F, 0.125F, 0.4375F);
                            renderBlock(Blocks.iron_block, x - 1, y, z);
                            renderBlocks.setRenderBounds(0.9375F, 0.0625F, 0.5625F, 1.0F, 0.125F, 0.625F);
                            renderBlock(Blocks.iron_block, x - 1, y, z);
                            renderBlocks.setRenderBounds(0.875F, 0.0625F, 0.375F, 0.9375F, 0.125F, 0.625F);
                            renderBlock(Blocks.iron_block, x - 1, y, z);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(thickness, 0.0625F, 0.375F, thickness + 0.0625F, 0.125F, 0.4375F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(thickness, 0.0625F, 0.5625F, thickness + 0.0625F, 0.125F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(thickness + 0.0625F, 0.0625F, 0.375F, thickness + 0.125F, 0.125F, 0.625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_NEG:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.0F, 0.4375F, 0.125F, 0.0625F);
                            renderBlock(Blocks.iron_block, x, y, z + 1);
                            renderBlocks.setRenderBounds(0.5625F, 0.0625F, 0.0F, 0.625F, 0.125F, 0.0625F);
                            renderBlock(Blocks.iron_block, x, y, z + 1);
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.0625F, 0.625F, 0.125F, 0.125F);
                            renderBlock(Blocks.iron_block, x, y, z + 1);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 1.0F - thickness - 0.0625F, 0.4375F, 0.125F, 1.0F - thickness);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.0625F, 1.0F - thickness - 0.0625F, 0.625F, 0.125F, 1.0F - thickness);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 1.0F - thickness - 0.125F, 0.625F, 0.125F, 1.0F - thickness - 0.0625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                    case Hatch.DIR_Z_POS:
                        
                        if (render_inside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.9375F, 0.4375F, 0.125F, 1.0F);
                            renderBlock(Blocks.iron_block, x, y, z - 1);
                            renderBlocks.setRenderBounds(0.5625F, 0.0625F, 0.9375F, 0.625F, 0.125F, 1.0F);
                            renderBlock(Blocks.iron_block, x, y, z - 1);
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, 0.875F, 0.625F, 0.125F, 0.9375F);
                            renderBlock(Blocks.iron_block, x, y, z - 1);
                        }
                        
                        if (render_outside_handle)
                        {
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, thickness, 0.4375F, 0.125F, thickness + 0.0625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.5625F, 0.0625F, thickness, 0.625F, 0.125F, thickness + 0.0625F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                            renderBlocks.setRenderBounds(0.375F, 0.0625F, thickness + 0.0625F, 0.625F, 0.125F, thickness + 0.125F);
                            renderBlocks.renderStandardBlock(handleBlock, x, y, z);
                        }
                        
                        break;
                }
            }
        }
    }
    
}
