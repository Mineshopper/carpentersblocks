package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import carpentersblocks.block.BlockCarpentersStairs;
import carpentersblocks.data.Stairs;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.stairs.StairsUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersStairs extends BlockHandlerBase {
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
        
        renderBlocks.setRenderBounds(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
        super.renderInventoryBlock(block, metadata, modelID, renderBlocks);
    }
    
    @Override
    /**
     * Renders stairs at the given coordinates
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        ItemStack itemStack = BlockProperties.getCover(TE, 6);
        
        Stairs stairs = Stairs.stairsList[BlockProperties.getMetadata(TE)];
        StairsUtil stairsUtil = new StairsUtil();
        
        BlockCarpentersStairs blockRef = (BlockCarpentersStairs) BlockRegistry.blockCarpentersStairs;
        
        for (int box = 0; box < 3; ++box)
        {
            float[] bounds = stairsUtil.genBounds(box, stairs);
            
            if (bounds != null)
            {
                blockRef.setBlockBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
                renderBlocks.setRenderBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
                renderBlock(itemStack, x, y, z);
            }
        }
    }
    
    @Override
    /**
     * Renders side covers (stair specific).
     */
    protected void renderSideBlocks(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;
        
        Stairs stairs = Stairs.stairsList[BlockProperties.getMetadata(TE)];
        StairsUtil stairsUtil = new StairsUtil();
        
        for (int box = 0; box < 3; ++box)
        {
            float[] bounds = stairsUtil.genBounds(box, stairs);
            
            if (bounds != null)
            {
                for (int side = 0; side < 6; ++side)
                {
                    ItemStack itemStack = BlockProperties.getCover(TE, side);
                    
                    coverRendering = side;
                    
                    if (BlockProperties.hasCover(TE, side))
                    {
                        renderBlocks.setRenderBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
                        int[] renderOffset = getSideCoverRenderBounds(x, y, z, side);
                        
                        if (clipSideCoverBoundsBasedOnState(stairs.stairsID, box, side)) {
                            renderBlock(itemStack, renderOffset[0], renderOffset[1], renderOffset[2]);
                        }
                    }
                }
            }
        }
        
        renderBlocks.renderAllFaces = false;
        coverRendering = 6;
    }
    
    /**
     * Alters side cover render bounds to prevent it from intersecting the block mask.
     */
    private boolean clipSideCoverBoundsBasedOnState(int data, int box, int side)
    {
        ++box;
        
        switch (data) {
            case Stairs.ID_NORMAL_POS_N:
                switch (box) {
                    case 1:
                        if (side == 2) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 3) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_POS_W:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_POS_E:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_POS_S:
                switch (box) {
                    case 1:
                        if (side == 3) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 2) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_NEG_N:
                switch (box) {
                    case 1:
                        if (side == 2) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 3) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_NEG_W:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_NEG_E:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_NEG_S:
                switch (box) {
                    case 1:
                        if (side == 3) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 2) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_NW:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMaxZ -= 0.5D;
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 2) {
                            renderBlocks.renderMinY += 0.5D;
                        } else if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_SW:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMinZ += 0.5D;
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 3) {
                            renderBlocks.renderMinY += 0.5D;
                        } else if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_NE:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMaxZ -= 0.5D;
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 2) {
                            renderBlocks.renderMinY += 0.5D;
                        } else if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_SE:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMinZ += 0.5D;
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 3) {
                            renderBlocks.renderMinY += 0.5D;
                        } else if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_NW:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMaxZ -= 0.5D;
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 2) {
                            renderBlocks.renderMaxY -= 0.5D;
                        } else if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_SW:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMinZ += 0.5D;
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 3) {
                            renderBlocks.renderMaxY -= 0.5D;
                        } else if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_NE:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMaxZ -= 0.5D;
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 2) {
                            renderBlocks.renderMaxY -= 0.5D;
                        } else if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_SE:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMinZ += 0.5D;
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 3) {
                            renderBlocks.renderMaxY -= 0.5D;
                        } else if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_NW:
                switch (box) {
                    case 1:
                        if (side == 2 || side == 4) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_SW:
                switch (box) {
                    case 1:
                        if (side == 3 || side == 4) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_NE:
                switch (box) {
                    case 1:
                        if (side == 2 || side == 5) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_SE:
                switch (box) {
                    case 1:
                        if (side == 3 || side == 5) {
                            renderBlocks.renderMinY += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_NW:
                switch (box) {
                    case 1:
                        if (side == 2 || side == 4) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_SW:
                switch (box) {
                    case 1:
                        if (side == 3 || side == 4) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_NE:
                switch (box) {
                    case 1:
                        if (side == 2 || side == 5) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 3 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_SE:
                switch (box) {
                    case 1:
                        if (side == 3 || side == 5) {
                            renderBlocks.renderMaxY -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                    case 3:
                        if (side == 2 || side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_SW:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMinZ += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_NW:
                switch (box) {
                    case 1:
                        if (side == 4) {
                            renderBlocks.renderMaxZ -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 5) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_NE:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMaxZ -= 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                }
                break;
            case Stairs.ID_NORMAL_SE:
                switch (box) {
                    case 1:
                        if (side == 5) {
                            renderBlocks.renderMinZ += 0.5D;
                        }
                        break;
                    case 2:
                        if (side == 4) {
                            return false;
                        }
                        break;
                }
                break;
        }
        
        return true;
    }
    
}
