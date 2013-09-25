package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

public class BlockHandlerCarpentersBlock extends BlockHandlerBase
{
	
    @Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
    	Tessellator tessellator = Tessellator.instance;
    	GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    	GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

    	renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    	
    	tessellator.startDrawingQuads();
    	tessellator.setNormal(0.0F, -1.0F, 0.0F);
    	renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
    	tessellator.draw();
    	tessellator.startDrawingQuads();
    	tessellator.setNormal(0.0F, 1.0F, 0.0F);
    	renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
    	tessellator.draw();
    	tessellator.startDrawingQuads();
    	tessellator.setNormal(0.0F, 0.0F, -1.0F);
    	renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
    	tessellator.draw();
    	tessellator.startDrawingQuads();
    	tessellator.setNormal(0.0F, 0.0F, 1.0F);
    	renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
    	tessellator.draw();
    	tessellator.startDrawingQuads();
    	tessellator.setNormal(-1.0F, 0.0F, 0.0F);
    	renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
    	tessellator.draw();
    	tessellator.startDrawingQuads();
    	tessellator.setNormal(1.0F, 0.0F, 0.0F);
    	renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
    	tessellator.draw();
    	
    	GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }
	
}