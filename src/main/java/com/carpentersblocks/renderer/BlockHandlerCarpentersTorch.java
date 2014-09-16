package com.carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.data.Torch;
import com.carpentersblocks.renderer.helper.VertexHelper;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersTorch extends BlockHandlerBase {

    private Vec3[] vec3 = new Vec3[8];
    private static Torch data = new Torch();

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    /**
     * Override to provide custom icons.
     */
    protected IIcon getUniqueIcon(ItemStack itemStack, int side, IIcon icon)
    {
        Block block = BlockProperties.toBlock(itemStack);

        if (BlockProperties.hasCover(TE, 6)) {
            return block.getIcon(2, renderBlocks.blockAccess.getBlockMetadata(TE.xCoord, TE.yCoord, TE.zCoord));
        } else {
            return IconRegistry.icon_uncovered_solid;
        }
    }

    @Override
    /**
     * Renders block
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;
        disableAO = true;

        renderBlocks.setRenderBounds(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.625D, 0.5625D);
        renderTorch(getCoverForRendering(), x, y, z);

        disableAO = false;
        renderBlocks.renderAllFaces = false;
    }

    @Override
    /**
     * Renders side.
     */
    protected void render(int x, int y, int z, int side, IIcon icon)
    {
        renderFace(side, icon, true);
    }

    /**
     * Renders a torch at the given coordinates
     */
    private void renderTorch(ItemStack itemStack, int x, int y, int z)
    {
        renderTorchHead(x, y, z);
        renderTorchHandle(itemStack, x, y, z);
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
        switch (data.getState(TE)) {
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

        vec3 = new Vec3[] {
            Vec3.createVectorHelper(-0.0625F,   0.5D, -0.0625F),
            Vec3.createVectorHelper( 0.0625F,   0.5D, -0.0625F),
            Vec3.createVectorHelper( 0.0625F,   0.5D,  0.0625F),
            Vec3.createVectorHelper(-0.0625F,   0.5D,  0.0625F),
            Vec3.createVectorHelper(-0.0625F, 0.625F, -0.0625F),
            Vec3.createVectorHelper( 0.0625F, 0.625F, -0.0625F),
            Vec3.createVectorHelper( 0.0625F, 0.625F,  0.0625F),
            Vec3.createVectorHelper(-0.0625F, 0.625F,  0.0625F)
        };

        setRotations(data.getDirection(TE), vec3, x, y, z);

        for (int side = 0; side < 6; ++side) {
            renderFace(side, icon, false);
        }
    }

    /**
     * Renders a torch handle at the given coordinates.
     */
    private void renderTorchHandle(ItemStack itemStack, int x, int y, int z)
    {
        vec3 = new Vec3[] {
            Vec3.createVectorHelper(-0.0625F, 0.0D, -0.0625F),
            Vec3.createVectorHelper( 0.0625F, 0.0D, -0.0625F),
            Vec3.createVectorHelper( 0.0625F, 0.0D,  0.0625F),
            Vec3.createVectorHelper(-0.0625F, 0.0D,  0.0625F),
            Vec3.createVectorHelper(-0.0625F, 0.5F, -0.0625F),
            Vec3.createVectorHelper( 0.0625F, 0.5F, -0.0625F),
            Vec3.createVectorHelper( 0.0625F, 0.5F,  0.0625F),
            Vec3.createVectorHelper(-0.0625F, 0.5F,  0.0625F)
        };

        setRotations(data.getDirection(TE), vec3, x, y, z);

        lightingHelper.setupLightingYNeg(itemStack, x, y, z);
        delegateSideRender(itemStack, x, y, z, DOWN);

        lightingHelper.setupLightingYPos(itemStack, x, y, z);
        delegateSideRender(itemStack, x, y, z, UP);

        lightingHelper.setupLightingZNeg(itemStack, x, y, z);
        delegateSideRender(itemStack, x, y, z, NORTH);

        lightingHelper.setupLightingZPos(itemStack, x, y, z);
        delegateSideRender(itemStack, x, y, z, SOUTH);

        lightingHelper.setupLightingXNeg(itemStack, x, y, z);
        delegateSideRender(itemStack, x, y, z, WEST);

        lightingHelper.setupLightingXPos(itemStack, x, y, z);
        delegateSideRender(itemStack, x, y, z, EAST);
    }

    private void setRotations(ForgeDirection dir, Vec3[] vec3, int x, int y, int z)
    {
        for (int vecCount = 0; vecCount < 8; ++vecCount)
        {
            switch (dir) {
                case UP:
                    vec3[vecCount].xCoord += x + 0.5D;
                    vec3[vecCount].yCoord += y;
                    vec3[vecCount].zCoord += z + 0.5D;
                    break;
                default:

                    vec3[vecCount].zCoord += 0.0625D;
                    vec3[vecCount].rotateAroundX(-((float)Math.PI * 3.4F / 9F));

                    vec3[vecCount].yCoord -= 0.4375D;
                    vec3[vecCount].rotateAroundX((float)Math.PI / 2F);

                    switch (dir) {
                        case NORTH:
                            vec3[vecCount].rotateAroundY(0.0F);
                            break;
                        case SOUTH:
                            vec3[vecCount].rotateAroundY((float)Math.PI);
                            break;
                        case WEST:
                            vec3[vecCount].rotateAroundY((float)Math.PI / 2F);
                            break;
                        case EAST:
                            vec3[vecCount].rotateAroundY(-((float)Math.PI / 2F));
                            break;
                        default: {}
                    }

                    vec3[vecCount].xCoord += x + 0.5D;
                    vec3[vecCount].yCoord += y + 0.1875F;
                    vec3[vecCount].zCoord += z + 0.5D;

                    break;
            }
        }
    }

    /**
     * Performs final rendering of face.
     */
    private void renderFace(int side, IIcon icon, boolean isHandle)
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
                vertex1 = vec3[0];
                vertex2 = vec3[1];
                vertex3 = vec3[2];
                vertex4 = vec3[3];
                break;
            case UP:
                vertex1 = vec3[7];
                vertex2 = vec3[6];
                vertex3 = vec3[5];
                vertex4 = vec3[4];
                break;
            case NORTH:
                vertex1 = vec3[1];
                vertex2 = vec3[0];
                vertex3 = vec3[4];
                vertex4 = vec3[5];
                break;
            case SOUTH:
                vertex1 = vec3[3];
                vertex2 = vec3[2];
                vertex3 = vec3[6];
                vertex4 = vec3[7];
                break;
            case WEST:
                vertex1 = vec3[0];
                vertex2 = vec3[3];
                vertex3 = vec3[7];
                vertex4 = vec3[4];
                break;
            case EAST:
                vertex1 = vec3[2];
                vertex2 = vec3[1];
                vertex3 = vec3[5];
                vertex4 = vec3[6];
                break;
        }

        VertexHelper.drawVertex(renderBlocks, vertex1.xCoord, vertex1.yCoord, vertex1.zCoord, uMin, vMax);
        VertexHelper.drawVertex(renderBlocks, vertex2.xCoord, vertex2.yCoord, vertex2.zCoord, uMax, vMax);
        VertexHelper.drawVertex(renderBlocks, vertex3.xCoord, vertex3.yCoord, vertex3.zCoord, uMax, vMin);
        VertexHelper.drawVertex(renderBlocks, vertex4.xCoord, vertex4.yCoord, vertex4.zCoord, uMin, vMin);
    }

}
