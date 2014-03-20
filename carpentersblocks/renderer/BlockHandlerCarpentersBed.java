package carpentersblocks.renderer;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Bed;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.bed.BedDesignHandler;
import carpentersblocks.util.handler.DyeHandler;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersBed extends BlockHandlerBase {
    
    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }
    
    @Override
    /**
     * Renders bed
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderNormalBed(BlockProperties.getCover(TE, 6), x, y, z);
    }
    
    private void renderNormalBed(ItemStack itemStack, int x, int y, int z)
    {
        ForgeDirection dir = Bed.getDirection(TE);
        
        disableAO = true;
        
        boolean isHead = Bed.isHeadOfBed(TE);
        
        TEBase TE_opp = Bed.getOppositeTE(TE);
        
        boolean isOccupied = Bed.isOccupied(TE);
        
        int blanketColor = 0;
        int frameColor = 0;
        
        if (isHead) {
            
            frameColor = DyeHandler.getColor(BlockProperties.getDye(TE, 6));
            
            if (TE_opp != null) {
                blanketColor = DyeHandler.getColor(BlockProperties.getDye(TE_opp, 6));
                isOccupied |= Bed.isOccupied(TE_opp);
            }
            
        } else {
            
            blanketColor = DyeHandler.getColor(BlockProperties.getDye(TE, 6));
            
            if (TE_opp != null) {
                frameColor = DyeHandler.getColor(BlockProperties.getDye(TE_opp, 6));
                isOccupied |= Bed.isOccupied(TE_opp);
            }
            
        }
        
        int design = Bed.getDesign(TE);
        
        boolean hasCustomBlanket = design > 0 && BedDesignHandler.hasBlanket[design];
        
        IIcon icon_pillow = hasCustomBlanket && BedDesignHandler.hasPillow[design] ? IconRegistry.icon_bed_pillow_custom[design] : IconRegistry.icon_bed_pillow;
        
        /* Check for adjacent bed pieces that can connect. */
        
        boolean bedParallelPos = false;
        boolean bedParallelNeg = false;
        
        if (dir.equals(ForgeDirection.NORTH) || dir.equals(ForgeDirection.SOUTH))
        {
            
            if (renderBlocks.blockAccess.getBlock(x + 1, y, z).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(x + 1,  y,  z);
                bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }
            if (renderBlocks.blockAccess.getBlock(x - 1, y, z).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(x - 1,  y,  z);
                bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }
            
        } else {
            
            if (renderBlocks.blockAccess.getBlock(x, y, z + 1).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(x,  y, z + 1);
                bedParallelPos = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }
            if (renderBlocks.blockAccess.getBlock(x, y, z - 1).equals(srcBlock)) {
                TEBase TE_adj = (TEBase) renderBlocks.blockAccess.getTileEntity(x,  y, z - 1);
                bedParallelNeg = Bed.isHeadOfBed(TE) == Bed.isHeadOfBed(TE_adj) && Bed.getDirection(TE) == Bed.getDirection(TE_adj);
            }
            
        }
        
        ItemStack cloth = new ItemStack(Blocks.wool);
        
        switch (dir)
        {
            case NORTH: // -Z
            {
                if (isHead) {
                    
                    // Render headboard
                    renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.875D, 0.875D, 0.875D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 0.875D);
                    renderBlock(itemStack, x, y, z);
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;

                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.875D);
                    renderBlock(cloth, x, y, z);

                    // Render pillow
                    renderBlocks.uvRotateTop = 2;
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.4375D, 0.875D, 0.6875D, 0.8125D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                } else {
                    
                    setDyeOverride(frameColor);
                    
                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    clearDyeOverride();
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;
                    
                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0625D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
                    renderBlock(cloth, x, y, z);
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                }
                break;
            }
            case SOUTH: // +Z
            {
                if (isHead) {
                    
                    // Render headboard
                    renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 0.875D, 0.875D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;
                    
                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.125D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 1.0D);
                    renderBlock(cloth, x, y, z);
                    
                    // Render pillow
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.125D, 0.5625D, 0.1875D, 0.875D, 0.6875D, 0.5625D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                } else {
                    
                    setDyeOverride(frameColor);
                    
                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    clearDyeOverride();
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;
                    
                    // Render mattress
                    renderBlocks.setRenderBounds(bedParallelNeg ? 0.0D : 0.0625D, 0.3125D, 0.0D, bedParallelPos ? 1.0D : 0.9375D, 0.5625D, 0.9375D);
                    renderBlock(cloth, x, y, z);
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0625D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.9375D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                }
                break;
            }
            case WEST: // -X
            {
                if (isHead) {
                    
                    // Render headboard
                    renderBlocks.setRenderBounds(0.875D, 0.1875D, 0.125D, 1.0D, 0.875D, 0.875D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render legs
                    renderBlocks.setRenderBounds(0.875D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 1.0D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.875D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 1.0D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 0.875D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;
                    
                    // Render mattress
                    renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.875D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);
                    
                    // Render pillow
                    renderBlocks.uvRotateTop = 1;
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.4375D, 0.5625D, 0.125D, 0.8125D, 0.6875D, 0.875D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 0.5D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                } else {
                    
                    setDyeOverride(frameColor);
                    
                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.125D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.875D, 0.125D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    clearDyeOverride();
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;
                    
                    // Render mattress
                    renderBlocks.setRenderBounds(0.0625D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 0.0625D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                }
                break;
            }
            default: // EAST +X
            {
                if (isHead) {
                    
                    // Render headboard
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.125D, 0.125D, 0.875D, 0.875D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render legs
                    renderBlocks.setRenderBounds(0.0D, bedParallelNeg ? 0.1875D : 0.0D, 0.0D, 0.125D, bedParallelNeg ? 0.875D : 1.0D, 0.125D);
                    renderBlock(itemStack, x, y, z);
                    renderBlocks.setRenderBounds(0.0D, bedParallelPos ? 0.1875D : 0.0D, 0.875D, 0.125D, bedParallelPos ? 0.875D : 1.0D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.125D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;
                    
                    // Render mattress
                    renderBlocks.setRenderBounds(0.125D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 1.0D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);
                    
                    // Render pillow
                    renderBlocks.uvRotateTop = 3;
                    setIconOverride(6, icon_pillow);
                    renderBlocks.setRenderBounds(0.1875D, 0.5625D, 0.125D, 0.5625D, 0.6875D, 0.875D);
                    renderBlock(cloth, x, y, z);
                    clearIconOverride(6);
                    renderBlocks.uvRotateTop = 0;
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                } else {
                    
                    setDyeOverride(frameColor);
                    
                    // Render legs
                    if (!bedParallelNeg) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.0D, 1.0D, 0.1875D, 0.125D);
                        renderBlock(itemStack, x, y, z);
                    }
                    if (!bedParallelPos) {
                        renderBlocks.setRenderBounds(0.875D, 0.0D, 0.875D, 1.0D, 0.1875D, 1.0D);
                        renderBlock(itemStack, x, y, z);
                    }
                    
                    // Render support board
                    renderBlocks.setRenderBounds(0.0D, 0.1875D, 0.0D, 1.0D, 0.3125D, 1.0D);
                    renderBlock(itemStack, x, y, z);
                    
                    clearDyeOverride();
                    
                    suppressDyeColor = true;
                    suppressOverlay = true;
                    suppressPattern = true;
                    
                    // Render mattress
                    renderBlocks.setRenderBounds(0.0D, 0.3125D, bedParallelNeg ? 0.0D : 0.0625D, 0.9375D, 0.5625D, bedParallelPos ? 1.0D : 0.9375D);
                    renderBlock(cloth, x, y, z);
                    
                    // Render blanket
                    if (!hasCustomBlanket)
                    {
                        disableAO = false;
                        cloth.setItemDamage(blanketColor);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.0625D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.8125D : 0.5625D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.9375D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 1.0D);
                        renderBlock(cloth, x, y, z);
                        renderBlocks.setRenderBounds(0.9375D, isOccupied ? 0.4375D : 0.3125D, 0.0625D, 1.0D, isOccupied ? 0.8125D : 0.5625D, 0.9375D);
                        renderBlock(cloth, x, y, z);
                        cloth.setItemDamage(0);
                        disableAO = true;
                    }
                    
                    suppressDyeColor = false;
                    suppressOverlay = false;
                    suppressPattern = false;
                    
                }
                break;
            }
        }
        
        disableAO = false;
        
        /*
         * If this bed has a blanket design, we'll render part of the blanket
         * here to fill in the gaps (face at head of bed, bottom side).
         */
        if (hasCustomBlanket)
        {
            cloth.setItemDamage(blanketColor);
            suppressDyeColor = true;
            suppressOverlay = true;
            suppressPattern = true;
            
            if (isHead)
            {
                switch (dir)
                {
                    case NORTH: // -Z
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
                        lightingHelper.setLightnessOverride(lightingHelper.LIGHTNESS_Z);
                        delegateSideRender(cloth, x, y, z, SOUTH);
                        break;
                    case SOUTH: // +Z
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        lightingHelper.setLightnessOverride(lightingHelper.LIGHTNESS_Z);
                        delegateSideRender(cloth, x, y, z, NORTH);
                        break;
                    case WEST:     // -X
                        renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        lightingHelper.setLightnessOverride(lightingHelper.LIGHTNESS_X);
                        delegateSideRender(cloth, x, y, z, EAST);
                        break;
                    default:     // EAST +X
                        renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                        lightingHelper.setLightnessOverride(lightingHelper.LIGHTNESS_X);
                        delegateSideRender(cloth, x, y, z, WEST);
                        break;
                }
                
                lightingHelper.setLightnessOverride(lightingHelper.LIGHTNESS_YN);
                delegateSideRender(cloth, x, y, z, DOWN);
                
            } else {
                
                lightingHelper.setLightnessOverride(lightingHelper.LIGHTNESS_YN);
                delegateSideRender(cloth, x, y, z, DOWN);
                
            }
            
            lightingHelper.clearLightnessOverride();
            suppressDyeColor = false;
            suppressOverlay = false;
            suppressPattern = false;
            cloth.setItemDamage(0);
        }
    }
    
}
