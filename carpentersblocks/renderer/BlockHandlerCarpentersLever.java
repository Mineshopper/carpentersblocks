package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.block.BlockCarpentersLever;
import carpentersblocks.block.BlockCoverable;
import carpentersblocks.data.Lever;
import carpentersblocks.data.Lever.Axis;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersLever extends BlockHandlerBase {
    
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
        renderBlocks.renderAllFaces = true;
        
        Block block = BlockProperties.getCover(TE, 6);
        
        renderLever(block, x, y, z);
        
        renderBlocks.renderAllFaces = false;
        
        return true;
    }
    
    /**
     * Renders lever.
     */
    private boolean renderLever(Block block, int x, int y, int z)
    {
        /* Set block bounds and render lever base. */
        
        BlockCarpentersLever blockRef = (BlockCarpentersLever) BlockRegistry.blockCarpentersLever;
        blockRef.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
        
        renderBlocks.setRenderBoundsFromBlock(blockRef);
        renderBlock(block, x, y, z);
        
        /* Render lever handle. */
        
        renderLeverHandle(x, y, z);
        
        return true;
    }
    
    /**
     * Renders the lever handle.
     */
    private boolean renderLeverHandle(int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(Blocks.dirt.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        
        ForgeDirection facing = Lever.getFacing(TE);
        boolean toggleState = Lever.getState(TE) == Lever.STATE_ON;
        boolean rotateLever = Lever.getAxis(TE) == Axis.X;
        
        IIcon icon = renderBlocks.hasOverrideBlockTexture() ? renderBlocks.overrideBlockTexture : IconRegistry.icon_lever;
        
        double uMin = icon.getMinU();
        double uMax = icon.getMaxU();
        double vMin = icon.getMinV();
        double vMax = icon.getMaxV();
        
        Vec3[] vector = new Vec3[8];
        float vecX = 0.0625F;
        float vecY = 0.625F;
        float vecZ = 0.0625F;
        vector[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, 0.0D, -vecZ);
        vector[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.0D, -vecZ);
        vector[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.0D, vecZ);
        vector[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, 0.0D, vecZ);
        vector[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, vecY, -vecZ);
        vector[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, -vecZ);
        vector[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, vecZ);
        vector[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, vecY, vecZ);
        
        /* Set up lever handle rotation. */
        
        for (int vecCount = 0; vecCount < 8; ++vecCount)
        {
            if (toggleState) {
                vector[vecCount].zCoord -= 0.0625D;
                vector[vecCount].rotateAroundX((float)Math.PI * 2F / 9F);
            } else {
                vector[vecCount].zCoord += 0.0625D;
                vector[vecCount].rotateAroundX(-((float)Math.PI * 2F / 9F));
            }
            
            if (facing.ordinal() < 2) {
                
                if (facing.equals(ForgeDirection.DOWN)) {
                    vector[vecCount].rotateAroundZ((float)Math.PI);
                }
                
                if (rotateLever) {
                    vector[vecCount].rotateAroundY((float)Math.PI / 2F);
                }
                
                if (facing.equals(ForgeDirection.UP)) {
                    vector[vecCount].xCoord += x + 0.5D;
                    vector[vecCount].yCoord += y + 0.125F;
                    vector[vecCount].zCoord += z + 0.5D;
                } else {
                    vector[vecCount].xCoord += x + 0.5D;
                    vector[vecCount].yCoord += y + 0.875F;
                    vector[vecCount].zCoord += z + 0.5D;
                }
                
            } else {
                
                vector[vecCount].yCoord -= 0.375D;
                vector[vecCount].rotateAroundX((float)Math.PI / 2F);
                
                switch (facing) {
                    case NORTH:
                        vector[vecCount].rotateAroundY(0.0F);
                        break;
                    case SOUTH:
                        vector[vecCount].rotateAroundY((float)Math.PI);
                        break;
                    case WEST:
                        vector[vecCount].rotateAroundY((float)Math.PI / 2F);
                        break;
                    case EAST:
                        vector[vecCount].rotateAroundY(-((float)Math.PI / 2F));
                        break;
                    default: {}
                }
                
                vector[vecCount].xCoord += x + 0.5D;
                vector[vecCount].yCoord += y + 0.5F;
                vector[vecCount].zCoord += z + 0.5D;
                
            }
        }
        
        Vec3 vertex1 = null;
        Vec3 vertex2 = null;
        Vec3 vertex3 = null;
        Vec3 vertex4 = null;
        
        for (int side = 0; side < 6; ++side)
        {
            if (side == 0) {
                uMin = icon.getInterpolatedU(7.0D);
                vMin = icon.getInterpolatedV(6.0D);
                uMax = icon.getInterpolatedU(9.0D);
                vMax = icon.getInterpolatedV(8.0D);
            } else if (side == 2) {
                uMin = icon.getInterpolatedU(7.0D);
                vMin = icon.getInterpolatedV(6.0D);
                uMax = icon.getInterpolatedU(9.0D);
                vMax = icon.getMaxV();
            }
            
            switch (side) {
                case 0:
                    vertex1 = vector[0];
                    vertex2 = vector[1];
                    vertex3 = vector[2];
                    vertex4 = vector[3];
                    break;
                case 1:
                    vertex1 = vector[7];
                    vertex2 = vector[6];
                    vertex3 = vector[5];
                    vertex4 = vector[4];
                    break;
                case 2:
                    tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                    vertex1 = vector[1];
                    vertex2 = vector[0];
                    vertex3 = vector[4];
                    vertex4 = vector[5];
                    break;
                case 3:
                    tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                    vertex1 = vector[2];
                    vertex2 = vector[1];
                    vertex3 = vector[5];
                    vertex4 = vector[6];
                    break;
                case 4:
                    tessellator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
                    vertex1 = vector[3];
                    vertex2 = vector[2];
                    vertex3 = vector[6];
                    vertex4 = vector[7];
                    break;
                case 5:
                    tessellator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
                    vertex1 = vector[0];
                    vertex2 = vector[3];
                    vertex3 = vector[7];
                    vertex4 = vector[4];
                    break;
            }
            
            tessellator.addVertexWithUV(vertex1.xCoord, vertex1.yCoord, vertex1.zCoord, uMin, vMax);
            tessellator.addVertexWithUV(vertex2.xCoord, vertex2.yCoord, vertex2.zCoord, uMax, vMax);
            tessellator.addVertexWithUV(vertex3.xCoord, vertex3.yCoord, vertex3.zCoord, uMax, vMin);
            tessellator.addVertexWithUV(vertex4.xCoord, vertex4.yCoord, vertex4.zCoord, uMin, vMin);
        }
        
        return true;
    }
    
}
