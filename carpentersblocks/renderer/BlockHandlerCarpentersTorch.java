package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.data.Torch;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersTorch extends BlockHandlerBase {
    
    private Vec3[] vector = new Vec3[8];
    
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
        if (BlockProperties.hasCover(TE, 6)) {
            return block.getIcon(2, renderBlocks.blockAccess.getBlockMetadata(TE.xCoord, TE.yCoord, TE.zCoord));
        } else {
            return IconRegistry.icon_solid;
        }
    }
    
    @Override
    /**
     * Renders block
     */
    protected boolean renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;
        disableAO = true;
        
        Block block = BlockProperties.getCover(TE, 6);
        renderTorch(block, x, y, z);
        
        disableAO = false;
        renderBlocks.renderAllFaces = false;
        
        return true;
    }
    
    @Override
    /**
     * Renders side.
     */
    protected void renderSide(int x, int y, int z, int side, double offset, IIcon icon)
    {
        renderFace(Tessellator.instance, side, icon, true);
    }
    
    /**
     * Renders a torch at the given coordinates
     */
    private void renderTorch(Block block, int x, int y, int z)
    {
        if (shouldRenderOpaque()) {
            renderTorchHead(x, y, z);
        }
        
        if (shouldRenderBlock(block)) {
            renderTorchHandle(block, x, y, z);
        }
    }
    
    /**
     * Renders a torch head at the given coordinates.
     */
    private void renderTorchHead(int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(srcBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, TE.xCoord, TE.yCoord, TE.zCoord));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        
        IIcon icon = null;
        switch (Torch.getState(TE)) {
            case LIT:
                icon = IconRegistry.icon_torch_lit;
                break;
            case SMOLDERING:
                icon = IconRegistry.icon_torch_head_smoldering;
                break;
            case UNLIT:
                icon = IconRegistry.icon_torch_head_unlit;
                break;
            default: {}
        }
        
        float vecX = 0.0625F;
        float vecY = 10.0F / 16.0F;
        float vecZ = 0.0625F;
        
        vector[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, 0.5D, -vecZ);
        vector[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.5D, -vecZ);
        vector[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.5D, vecZ);
        vector[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, 0.5D, vecZ);
        vector[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, vecY, -vecZ);
        vector[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, -vecZ);
        vector[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, vecZ);
        vector[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, vecY, vecZ);
        
        setRotations(Torch.getFacing(TE), x, y, z);
        
        for (int side = 0; side < 6; ++side) {
            renderFace(tessellator, side, icon, false);
        }
    }
    
    /**
     * Renders a torch handle at the given coordinates.
     */
    private void renderTorchHandle(Block block, int x, int y, int z)
    {
        float vecX = 0.0625F;
        float vecY = 0.5F;
        float vecZ = 0.0625F;
        
        vector[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, 0.0D, -vecZ);
        vector[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.0D, -vecZ);
        vector[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.0D, vecZ);
        vector[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, 0.0D, vecZ);
        vector[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, vecY, -vecZ);
        vector[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, -vecZ);
        vector[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, vecZ);
        vector[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-vecX, vecY, vecZ);
        
        setRotations(Torch.getFacing(TE), x, y, z);
        
        /* Have to enforce partial render bounds on all sides to get correct lighting. */
        
        renderBlocks.setRenderBounds(0.1D, 0.1D, 0.1D, 0.9D, 0.9D, 0.9D);
        
        lightingHelper.setLightingYNeg(block, x, y, z);
        delegateSideRender(block, x, y, z, DOWN);
        
        lightingHelper.setLightingYPos(block, x, y, z);
        delegateSideRender(block, x, y, z, UP);
        
        lightingHelper.setLightingZNeg(block, x, y, z);
        delegateSideRender(block, x, y, z, NORTH);
        
        lightingHelper.setLightingZPos(block, x, y, z);
        delegateSideRender(block, x, y, z, SOUTH);
        
        lightingHelper.setLightingXNeg(block, x, y, z);
        delegateSideRender(block, x, y, z, WEST);
        
        lightingHelper.setLightingXPos(block, x, y, z);
        delegateSideRender(block, x, y, z, EAST);
    }
    
    private void setRotations(ForgeDirection facing, int x, int y, int z)
    {
        for (int vecCount = 0; vecCount < 8; ++vecCount)
        {
            if (facing.equals(ForgeDirection.UP)) {
                
                vector[vecCount].xCoord += x + 0.5D;
                vector[vecCount].yCoord += y;
                vector[vecCount].zCoord += z + 0.5D;
                
            } else {
                
                vector[vecCount].zCoord += 0.0625D;
                vector[vecCount].rotateAroundX(-((float)Math.PI * 3.4F / 9F));
                
                vector[vecCount].yCoord -= 0.4375D;
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
                vector[vecCount].yCoord += y + 0.1875F;
                vector[vecCount].zCoord += z + 0.5D;
            }
        }
    }
    
    /**
     * Performs final rendering of face.
     */
    private void renderFace(Tessellator tessellator, int side, IIcon icon, boolean isHandle)
    {
        double uMin, uMax, vMin, vMax;
        
        if (isHandle) {
            uMin = icon.getInterpolatedU(7.0D);
            uMax = icon.getInterpolatedU(9.0D);
            vMin = icon.getMinV();
            vMax = icon.getInterpolatedV(8.0D);
        } else {
            uMin = icon.getInterpolatedU(7.0D);
            uMax = icon.getInterpolatedU(9.0D);
            vMin = icon.getInterpolatedV(6.0D);
            vMax = icon.getInterpolatedV(8.0D);
        }
        
        Vec3 vertex1 = null;
        Vec3 vertex2 = null;
        Vec3 vertex3 = null;
        Vec3 vertex4 = null;
        
        switch (side) {
            case DOWN:
                vertex1 = vector[0];
                vertex2 = vector[1];
                vertex3 = vector[2];
                vertex4 = vector[3];
                break;
            case UP:
                vertex1 = vector[7];
                vertex2 = vector[6];
                vertex3 = vector[5];
                vertex4 = vector[4];
                break;
            case NORTH:
                vertex1 = vector[1];
                vertex2 = vector[0];
                vertex3 = vector[4];
                vertex4 = vector[5];
                break;
            case SOUTH:
                vertex1 = vector[3];
                vertex2 = vector[2];
                vertex3 = vector[6];
                vertex4 = vector[7];
                break;
            case WEST:
                vertex1 = vector[0];
                vertex2 = vector[3];
                vertex3 = vector[7];
                vertex4 = vector[4];
                break;
            case EAST:
                vertex1 = vector[2];
                vertex2 = vector[1];
                vertex3 = vector[5];
                vertex4 = vector[6];
                break;
        }
        
        tessellator.addVertexWithUV(vertex1.xCoord, vertex1.yCoord, vertex1.zCoord, uMin, vMax);
        tessellator.addVertexWithUV(vertex2.xCoord, vertex2.yCoord, vertex2.zCoord, uMax, vMax);
        tessellator.addVertexWithUV(vertex3.xCoord, vertex3.yCoord, vertex3.zCoord, uMax, vMin);
        tessellator.addVertexWithUV(vertex4.xCoord, vertex4.yCoord, vertex4.zCoord, uMin, vMin);
    }
    
}
