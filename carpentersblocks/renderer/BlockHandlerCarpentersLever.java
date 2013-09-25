package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.IconHandler;

public class BlockHandlerCarpentersLever extends BlockHandlerBase
{
	
    @Override
	public boolean shouldRender3DInInventory() {
        return false;
    }
    
	@Override
	/**
	 * Renders block
	 */
	public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
    {
		Block coverBlock = isSideCover ? BlockProperties.getCoverBlock(TE, coverRendering) : BlockProperties.getCoverBlock(TE, 6);
		
		renderLever(TE, renderBlocks, coverBlock, srcBlock, x, y, z);

    	return true;
    }
	
    /**
     * Renders slab at given coordinates
     */
	public boolean renderLever(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
    {
		int data = BlockProperties.getData(TE);
        int facing = data & 7;
        boolean flag = (data & 8) > 0;
        Tessellator tessellator = Tessellator.instance;
        boolean hasOverride = renderBlocks.hasOverrideBlockTexture();

        float f = 0.25F;
        float f1 = 0.1875F;
        float f2 = 0.1875F;

        if (facing == 5)
        {
            renderBlocks.setRenderBounds(0.5F - f1, 0.0D, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        }
        else if (facing == 6)
        {
            renderBlocks.setRenderBounds(0.5F - f, 0.0D, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
        }
        else if (facing == 4)
        {
            renderBlocks.setRenderBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0D);
        }
        else if (facing == 3)
        {
            renderBlocks.setRenderBounds(0.5F - f1, 0.5F - f, 0.0D, 0.5F + f1, 0.5F + f, f2);
        }
        else if (facing == 2)
        {
            renderBlocks.setRenderBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0D, 0.5F + f, 0.5F + f1);
        }
        else if (facing == 1)
        {
            renderBlocks.setRenderBounds(0.0D, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
        }
        else if (facing == 0)
        {
            renderBlocks.setRenderBounds(0.5F - f, 1.0F - f2, 0.5F - f1, 0.5F + f, 1.0D, 0.5F + f1);
        }
        else if (facing == 7)
        {
            renderBlocks.setRenderBounds(0.5F - f1, 1.0F - f2, 0.5F - f, 0.5F + f1, 1.0D, 0.5F + f);
        }

        renderBlocks.renderAllFaces = true;
    	renderStandardBlock(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
        renderBlocks.renderAllFaces = false;

        if (!hasOverride)
        {
            renderBlocks.clearOverrideBlockTexture();
        }

        tessellator.setBrightness(coverBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, x, y, z));
        float f3 = 1.0F;

        if (Block.lightValue[coverBlock.blockID] > 0)
        {
            f3 = 1.0F;
        }

        tessellator.setColorOpaque_F(f3, f3, f3);
        Icon icon = IconHandler.icon_lever;

        if (renderBlocks.hasOverrideBlockTexture())
        {
            icon = renderBlocks.overrideBlockTexture;
        }

        double d0 = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        Vec3[] avec3 = new Vec3[8];
        float f4 = 0.0625F;
        float f5 = 0.0625F;
        float f6 = 0.625F;
        avec3[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-f4), 0.0D, (-f5));
        avec3[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(f4, 0.0D, (-f5));
        avec3[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(f4, 0.0D, f5);
        avec3[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-f4), 0.0D, f5);
        avec3[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-f4), f6, (-f5));
        avec3[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(f4, f6, (-f5));
        avec3[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(f4, f6, f5);
        avec3[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-f4), f6, f5);

        for (int j1 = 0; j1 < 8; ++j1)
        {
            if (flag)
            {
                avec3[j1].zCoord -= 0.0625D;
                avec3[j1].rotateAroundX(((float)Math.PI * 2F / 9F));
            }
            else
            {
                avec3[j1].zCoord += 0.0625D;
                avec3[j1].rotateAroundX(-((float)Math.PI * 2F / 9F));
            }

            if (facing == 0 || facing == 7)
            {
                avec3[j1].rotateAroundZ((float)Math.PI);
            }

            if (facing == 6 || facing == 0)
            {
                avec3[j1].rotateAroundY(((float)Math.PI / 2F));
            }

            if (facing > 0 && facing < 5)
            {
                avec3[j1].yCoord -= 0.375D;
                avec3[j1].rotateAroundX(((float)Math.PI / 2F));

                if (facing == 4)
                {
                    avec3[j1].rotateAroundY(0.0F);
                }

                if (facing == 3)
                {
                    avec3[j1].rotateAroundY((float)Math.PI);
                }

                if (facing == 2)
                {
                    avec3[j1].rotateAroundY(((float)Math.PI / 2F));
                }

                if (facing == 1)
                {
                    avec3[j1].rotateAroundY(-((float)Math.PI / 2F));
                }

                avec3[j1].xCoord += x + 0.5D;
                avec3[j1].yCoord += y + 0.5F;
                avec3[j1].zCoord += z + 0.5D;
            }
            else if (facing != 0 && facing != 7)
            {
                avec3[j1].xCoord += x + 0.5D;
                avec3[j1].yCoord += y + 0.125F;
                avec3[j1].zCoord += z + 0.5D;
            }
            else
            {
                avec3[j1].xCoord += x + 0.5D;
                avec3[j1].yCoord += y + 0.875F;
                avec3[j1].zCoord += z + 0.5D;
            }
        }

        Vec3 vec3 = null;
        Vec3 vec31 = null;
        Vec3 vec32 = null;
        Vec3 vec33 = null;

        for (int k1 = 0; k1 < 6; ++k1)
        {
            if (k1 == 0)
            {
                d0 = icon.getInterpolatedU(7.0D);
                d1 = icon.getInterpolatedV(6.0D);
                d2 = icon.getInterpolatedU(9.0D);
                d3 = icon.getInterpolatedV(8.0D);
            }
            else if (k1 == 2)
            {
                d0 = icon.getInterpolatedU(7.0D);
                d1 = icon.getInterpolatedV(6.0D);
                d2 = icon.getInterpolatedU(9.0D);
                d3 = icon.getMaxV();
            }

            if (k1 == 0)
            {
                vec3 = avec3[0];
                vec31 = avec3[1];
                vec32 = avec3[2];
                vec33 = avec3[3];
            }
            else if (k1 == 1)
            {
                vec3 = avec3[7];
                vec31 = avec3[6];
                vec32 = avec3[5];
                vec33 = avec3[4];
            }
            else if (k1 == 2)
            {
                vec3 = avec3[1];
                vec31 = avec3[0];
                vec32 = avec3[4];
                vec33 = avec3[5];
            }
            else if (k1 == 3)
            {
                vec3 = avec3[2];
                vec31 = avec3[1];
                vec32 = avec3[5];
                vec33 = avec3[6];
            }
            else if (k1 == 4)
            {
                vec3 = avec3[3];
                vec31 = avec3[2];
                vec32 = avec3[6];
                vec33 = avec3[7];
            }
            else if (k1 == 5)
            {
                vec3 = avec3[0];
                vec31 = avec3[3];
                vec32 = avec3[7];
                vec33 = avec3[4];
            }

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d0, d3);
            tessellator.addVertexWithUV(vec31.xCoord, vec31.yCoord, vec31.zCoord, d2, d3);
            tessellator.addVertexWithUV(vec32.xCoord, vec32.yCoord, vec32.zCoord, d2, d1);
            tessellator.addVertexWithUV(vec33.xCoord, vec33.yCoord, vec33.zCoord, d0, d1);
        }

        return true;
    }
	
}