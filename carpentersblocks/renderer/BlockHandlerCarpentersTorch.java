package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.block.BlockBase;
import carpentersblocks.data.Torch;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.IconHandler;

public class BlockHandlerCarpentersTorch extends BlockHandlerBase
{

	private Vec3[] vector = new Vec3[8];
	
	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	/**
	 * Returns whether torch handle should render.
	 */
	protected boolean shouldRenderTorchHandle(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, int renderPass)
	{
		if (renderAlphaOverride) {
			return renderPass == 1;
		} else {
			return	renderBlocks.hasOverrideBlockTexture() ||
					coverBlock.getRenderBlockPass() == renderPass ||
					coverBlock instanceof BlockBase && renderPass == 0 ||
					shouldRenderPattern(TE, renderPass);
		}
	}
	
	@Override
	/**
	 * Renders block
	 */
	public boolean renderCarpentersBlock(TECarpentersBlock TE, RenderBlocks renderBlocks, Block srcBlock, int renderPass, int x, int y, int z)
	{
		Block coverBlock = isSideCover ? BlockProperties.getCoverBlock(TE, coverRendering) : BlockProperties.getCoverBlock(TE, 6);

		renderTorch(TE, renderBlocks, coverBlock, srcBlock, renderPass, x, y, z);

		return true;
	}

    /**
     * Renders a torch at the given coordinates
     */
    public boolean renderTorch(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int renderPass, int x, int y, int z)
    {
    	if (renderPass == 0) {
    		renderTorchHead(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
    	}
    	
    	if (shouldRenderTorchHandle(TE, renderBlocks, coverBlock, renderPass)) {
      		renderTorchHandle(TE, renderBlocks, coverBlock, srcBlock, x, y, z);
    	}

        return true;
    }

    /**
     * Renders a torch head at the given coordinates.
     */
	private boolean renderTorchHead(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(srcBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, TE.xCoord, TE.yCoord, TE.zCoord));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

		Icon icon = null;
		switch (Torch.getState(TE)) {
			case LIT:
				icon = IconHandler.icon_torch_lit;
				break;
			case SMOLDERING:
				icon = IconHandler.icon_torch_head_smoldering;
				break;
			case UNLIT:
				icon = IconHandler.icon_torch_head_unlit;
				break;
			default: {}
		}

		float vecX = 0.0625F;
		float vecY = 10.0F / 16.0F;
		float vecZ = 0.0625F;
		vector[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), 0.5D, (-vecZ));
		vector[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.5D, (-vecZ));
		vector[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.5D, vecZ);
		vector[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), 0.5D, vecZ);
		vector[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), vecY, (-vecZ));
		vector[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, (-vecZ));
		vector[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, vecZ);
		vector[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), vecY, vecZ);

		setRotations(Torch.getFacing(TE), x, y, z);

		for (int side = 0; side < 6; ++side) {
			renderFace(tessellator, side, icon, false);
		}
		
		return true;
    }
    
    /**
     * Renders a torch handle at the given coordinates.
     */
	private boolean renderTorchHandle(TECarpentersBlock TE, RenderBlocks renderBlocks, Block coverBlock, Block srcBlock, int x, int y, int z)
	{
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(srcBlock.getMixedBrightnessForBlock(renderBlocks.blockAccess, TE.xCoord, TE.yCoord, TE.zCoord));
		tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

		float vecX = 0.0625F;
		float vecY = 0.5F;
		float vecZ = 0.0625F;
		vector[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), 0.0D, (-vecZ));
		vector[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.0D, (-vecZ));
		vector[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, 0.0D, vecZ);
		vector[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), 0.0D, vecZ);
		vector[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), vecY, (-vecZ));
		vector[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, (-vecZ));
		vector[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(vecX, vecY, vecZ);
		vector[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool((-vecX), vecY, vecZ);

		setRotations(Torch.getFacing(TE), x, y, z);

		/*
		 * Lightness and sides won't match here.  Rotations and the ordering
		 * of the vectors may be to blame.
		 */

		setLightnessYNeg(renderBlocks, coverBlock, x, y, z);
		prepareRender(TE, renderBlocks, coverBlock, srcBlock, 0, x, y, z, 0.5F);

		setLightnessYPos(renderBlocks, coverBlock, x, y, z);
		prepareRender(TE, renderBlocks, coverBlock, srcBlock, 1, x, y, z, 1.0F);

		setLightnessZNeg(renderBlocks, coverBlock, x, y, z);
		prepareRender(TE, renderBlocks, coverBlock, srcBlock, 2, x, y, z, 0.6F);

		setLightnessZPos(renderBlocks, coverBlock, x, y, z);
		prepareRender(TE, renderBlocks, coverBlock, srcBlock, 3, x, y, z, 0.8F);

		setLightnessXNeg(renderBlocks, coverBlock, x, y, z);
		prepareRender(TE, renderBlocks, coverBlock, srcBlock, 4, x, y, z, 0.6F);

		setLightnessXPos(renderBlocks, coverBlock, x, y, z);
		prepareRender(TE, renderBlocks, coverBlock, srcBlock, 5, x, y, z, 0.8F);
		
		return true;
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
				vector[vecCount].rotateAroundX(((float)Math.PI / 2F));

				switch (facing) {
					case NORTH:
						vector[vecCount].rotateAroundY(0.0F);
						break;
					case SOUTH:
						vector[vecCount].rotateAroundY((float)Math.PI);
						break;
					case WEST:
						vector[vecCount].rotateAroundY(((float)Math.PI / 2F));
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
	
	@Override
	/**
	 * Renders side.
	 */
	protected void renderSide(TECarpentersBlock TE, RenderBlocks renderBlocks, int side, double offset, int x, int y, int z, Icon icon)
	{
		renderFace(Tessellator.instance, side, icon, true);
	}
	
	/**
	 * Performs final rendering of face.
	 */
	private void renderFace(Tessellator tessellator, int side, Icon icon, boolean isHandle)
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
			vertex1 = vector[1];
			vertex2 = vector[0];
			vertex3 = vector[4];
			vertex4 = vector[5];
			break;
		case 3:
			vertex1 = vector[2];
			vertex2 = vector[1];
			vertex3 = vector[5];
			vertex4 = vector[6];
			break;
		case 4:
			vertex1 = vector[3];
			vertex2 = vector[2];
			vertex3 = vector[6];
			vertex4 = vector[7];
			break;
		case 5:
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

}