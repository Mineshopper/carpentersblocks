package carpentersblocks.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

import carpentersblocks.data.Bed;
import carpentersblocks.renderer.helper.BedDesignHelper;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.bed.BedDesignHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TERendererCarpentersBed extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity TE, double x, double y, double z, float f)
    {
        /**
         * Bed designs require switching texture sheets in order to
         * simplify texture creation.  They must be rendered in the TE
         * renderer to accomplish this.  I could've made it easy for me
         * and required users to provide 7 properly rotated, cropped
         * bed pieces, but it just seemed a bit extreme.  This method
         * renders every frame.
         */

        int design = Bed.getDesign((TEBase)TE);

        if (design > 0 && BedDesignHandler.hasBlanket[design]) {
            renderBedDesignAt((TEBase)TE, x, y, z, design);
        }
    }

    private void renderBedDesignAt(TEBase TE, double x, double y, double z, int design)
    {
        boolean isHead = Bed.isHeadOfBed(TE);
        boolean isOccupied = Bed.isOccupied(TE);

        /*
         * Setup rendering environment.
         */
        RenderBlocks renderBlocks = new RenderBlocks();
        bindTexture(BedDesignHandler.resource_blanket[design]);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(Block.dirt.getMixedBrightnessForBlock(TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord));
        tessellator.startDrawingQuads();
        GL11.glDisable(GL11.GL_LIGHTING);

        ForgeDirection dir = Bed.getDirection(TE);

        if (isHead) {

            switch (dir)
            {
                case NORTH: // -Z
                    renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 0.5D);
                    BedDesignHelper.renderFaceXNeg(renderBlocks, BedDesignHelper.SIDE5, x, y, z);
                    BedDesignHelper.renderFaceXPos(renderBlocks, BedDesignHelper.SIDE1, x, y, z);
                    break;
                case SOUTH: // +Z
                    renderBlocks.uvRotateTop = 2;
                    renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.5D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                    BedDesignHelper.renderFaceXNeg(renderBlocks, BedDesignHelper.SIDE1, x, y, z);
                    BedDesignHelper.renderFaceXPos(renderBlocks, BedDesignHelper.SIDE5, x, y, z);
                    break;
                case WEST:     // -X
                    renderBlocks.uvRotateTop = 3;
                    renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 0.5D, isOccupied ? 0.875D : 0.625D, 1.0D);
                    BedDesignHelper.renderFaceZNeg(renderBlocks, BedDesignHelper.SIDE1, x, y, z);
                    BedDesignHelper.renderFaceZPos(renderBlocks, BedDesignHelper.SIDE5, x, y, z);
                    break;
                default:     // EAST +X
                    renderBlocks.uvRotateTop = 1;
                    renderBlocks.setRenderBounds(0.5D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                    BedDesignHelper.renderFaceZNeg(renderBlocks, BedDesignHelper.SIDE5, x, y, z);
                    BedDesignHelper.renderFaceZPos(renderBlocks, BedDesignHelper.SIDE1, x, y, z);
                    break;
            }

            BedDesignHelper.renderFaceYPos(renderBlocks, BedDesignHelper.HEAD, x, y, z);
            renderBlocks.uvRotateTop = 0;

        } else {

            switch (dir)
            {
                case NORTH: // -Z
                    renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                    BedDesignHelper.renderFaceXNeg(renderBlocks, BedDesignHelper.SIDE4, x, y, z);
                    BedDesignHelper.renderFaceXPos(renderBlocks, BedDesignHelper.SIDE2, x, y, z);
                    BedDesignHelper.renderFaceZNeg(renderBlocks, BedDesignHelper.END, x, y, z);
                    break;
                case SOUTH: // +Z
                    renderBlocks.uvRotateTop = 2;
                    renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                    BedDesignHelper.renderFaceXNeg(renderBlocks, BedDesignHelper.SIDE2, x, y, z);
                    BedDesignHelper.renderFaceXPos(renderBlocks, BedDesignHelper.SIDE4, x, y, z);
                    BedDesignHelper.renderFaceZPos(renderBlocks, BedDesignHelper.END, x, y, z);
                    break;
                case WEST:     // -X
                    renderBlocks.uvRotateTop = 3;
                    renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                    BedDesignHelper.renderFaceZNeg(renderBlocks, BedDesignHelper.SIDE2, x, y, z);
                    BedDesignHelper.renderFaceZPos(renderBlocks, BedDesignHelper.SIDE4, x, y, z);
                    BedDesignHelper.renderFaceXNeg(renderBlocks, BedDesignHelper.END, x, y, z);
                    break;
                default:     // EAST +X
                    renderBlocks.uvRotateTop = 1;
                    renderBlocks.setRenderBounds(0.0D, isOccupied ? 0.4375D : 0.3125D, 0.0D, 1.0D, isOccupied ? 0.875D : 0.625D, 1.0D);
                    BedDesignHelper.renderFaceZNeg(renderBlocks, BedDesignHelper.SIDE4, x, y, z);
                    BedDesignHelper.renderFaceZPos(renderBlocks, BedDesignHelper.SIDE2, x, y, z);
                    BedDesignHelper.renderFaceXPos(renderBlocks, BedDesignHelper.END, x, y, z);
                    break;
            }

            BedDesignHelper.renderFaceYPos(renderBlocks, BedDesignHelper.FOOT, x, y, z);
            renderBlocks.uvRotateTop = 0;

        }

        tessellator.draw();
        GL11.glEnable(GL11.GL_LIGHTING);
        bindTexture(TextureMap.locationBlocksTexture);
    }

}
