package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import carpentersblocks.block.BlockCoverable;
import carpentersblocks.data.FlowerPot;
import carpentersblocks.renderer.helper.RenderHelperFlowerPot;
import carpentersblocks.tileentity.TECarpentersFlowerPot;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.flowerpot.FlowerPotHandler;
import carpentersblocks.util.flowerpot.FlowerPotProperties;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersFlowerPot extends BlockHandlerBase {
    
    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }
    
    @Override
    /**
     * Override to provide custom icons.
     */
    protected IIcon getUniqueIcon(Block block, int side, IIcon icon)
    {
        if (block instanceof BlockCoverable) {
            return IconRegistry.icon_solid;
        } else if (block.equals(Blocks.glass)) {
            return IconRegistry.icon_flower_pot_glass;
        } else {
            return icon;
        }
    }
    
    @Override
    /**
     * Renders block
     */
    protected boolean renderCarpentersBlock(int x, int y, int z)
    {
        TECarpentersFlowerPot TE = (TECarpentersFlowerPot) renderBlocks.blockAccess.getTileEntity(x, y, z);
        
        renderBlocks.renderAllFaces = true;
        
        Block block = BlockProperties.getCover(TE, 6);
        
        if (FlowerPot.getDesign(TE) > 0)
        {
            suppressOverlay = true;
            suppressPattern = true;
            suppressDyeColor = true;
        }
        
        if (shouldRenderBlock(block)) {
            renderPot(block, x, y, z);
        }
        
        suppressOverlay = true;
        suppressPattern = true;
        suppressDyeColor = true;
        
        if (FlowerPotProperties.hasSoil(TE))
        {
            block = FlowerPotProperties.getSoil(TE);
            
            if (shouldRenderBlock(block)) {
                renderSoil(block, x, y, z);
            }
        }
        
        if (FlowerPotProperties.hasPlant(TE))
        {
            block = FlowerPotProperties.getPlant(TE);
            
            if (shouldRenderOpaque()) {
                renderPlant(block, x, y, z);
            }
        }
        
        suppressOverlay = false;
        suppressPattern = false;
        suppressDyeColor = false;
        
        renderBlocks.renderAllFaces = false;
        
        return true;
    }
    
    /**
     * Renders flower pot
     */
    public boolean renderPot(Block block, int x, int y, int z)
    {
        if (FlowerPot.getDesign(TE) > 0) {
            IIcon designIcon = IconRegistry.icon_flower_pot_design[FlowerPot.getDesign(TE)];
            setIconOverride(6, designIcon);
            block = srcBlock;
        }
        
        /* BOTTOM BOX */
        
        renderBlocks.setRenderBounds(0.375D, 0.0D, 0.375D, 0.625D, 0.0625D, 0.625D);
        renderBlock(block, x, y, z);
        
        /* NORTH BOX */
        
        renderBlocks.setRenderBounds(0.375D, 0.0D, 0.3125D, 0.625D, 0.375D, 0.375D);
        renderBlock(block, x, y, z);
        
        /* SOUTH BOX */
        
        renderBlocks.setRenderBounds(0.375D, 0.0D, 0.625D, 0.625D, 0.375D, 0.6875D);
        renderBlock(block, x, y, z);
        
        /* WEST BOX */
        
        renderBlocks.setRenderBounds(0.3125D, 0.0D, 0.3125D, 0.375D, 0.375D, 0.6875D);
        renderBlock(block, x, y, z);
        
        /* EAST BOX */
        
        renderBlocks.setRenderBounds(0.625D, 0.0D, 0.3125D, 0.6875D, 0.375D, 0.6875D);
        renderBlock(block, x, y, z);
        
        clearIconOverride(6);
        
        return true;
    }
    
    /**
     * Renders soil
     */
    public boolean renderSoil(Block block, int x, int y, int z)
    {
        setMetadataOverride(FlowerPotProperties.getSoilMetadata((TECarpentersFlowerPot)TE));
        
        renderBlocks.setRenderBounds(0.375D, 0.0625D, 0.375D, 0.625D, 0.25D, 0.625D);
        renderBlock(block, x, y, z);
        
        clearMetadataOverride();
        
        return true;
    }
    
    /**
     * Renders plant
     */
    public boolean renderPlant(Block block, int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.addTranslation(0.0F, 0.25F, 0.0F);
        
        int metadata = FlowerPotProperties.getPlantMetadata((TECarpentersFlowerPot)TE);
        
        setMetadataOverride(metadata);
        
        switch (FlowerPotHandler.getPlantProfile(TE)) {
            case REDUCED_SCALE_YP:
                RenderHelperFlowerPot.renderPlantCrossedSquares(renderBlocks, block, metadata, x, y, z, 0.75F, false);
                break;
            case REDUCED_SCALE_YN:
                RenderHelperFlowerPot.renderPlantCrossedSquares(renderBlocks, block, metadata, x, y, z, 0.75F, true);
                break;
            case TRUE_SCALE:
                RenderHelperFlowerPot.renderPlantCrossedSquares(renderBlocks, block, metadata, x, y, z, 1.0F, false);
                break;
            case THIN_YP:
                RenderHelperFlowerPot.renderPlantThinCrossedSquares(renderBlocks, block, metadata, x, y, z, false);
                break;
            case THIN_YN:
                RenderHelperFlowerPot.renderPlantThinCrossedSquares(renderBlocks, block, metadata, x, y, z, true);
                break;
            case CACTUS:
                RenderHelperFlowerPot.drawPlantCactus(renderBlocks, block, x, y, z);
                break;
            case LEAVES:
                drawStackedBlocks(block, x, y, z);
                break;
        }
        
        clearMetadataOverride();
        
        tessellator.addTranslation(0.0F, -0.25F, 0.0F);
        
        return true;
    }
    
    /**
     * Draws stacked blocks for leaves or mod cacti.
     */
    private void drawStackedBlocks(Block block, int x, int y, int z)
    {
        World world = TE.getWorldObj();
        
        world.setBlockMetadataWithNotify(x, y, z, FlowerPotProperties.getPlantMetadata((TECarpentersFlowerPot) TE), 4);
        renderBlocks.setRenderBounds(0.375F, 0.0D, 0.375F, 0.625F, 0.25D, 0.625F);
        renderBlock(block, x, y, z);
        renderBlocks.setRenderBounds(0.375F, 0.25D, 0.375F, 0.625F, 0.50D, 0.625F);
        renderBlock(block, x, y, z);
        renderBlocks.setRenderBounds(0.375F, 0.50D, 0.375F, 0.625F, 0.75D, 0.625F);
        renderBlock(block, x, y, z);
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        world.setBlockMetadataWithNotify(x, y, z, BlockProperties.getCoverMetadata(TE, 6), 4);
    }
    
}
