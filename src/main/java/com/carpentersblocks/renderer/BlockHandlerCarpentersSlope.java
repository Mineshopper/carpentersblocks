package com.carpentersblocks.renderer;

import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.BOTTOM_RIGHT;
import static com.carpentersblocks.renderer.helper.VertexHelper.NORTHEAST;
import static com.carpentersblocks.renderer.helper.VertexHelper.NORTHWEST;
import static com.carpentersblocks.renderer.helper.VertexHelper.SOUTHEAST;
import static com.carpentersblocks.renderer.helper.VertexHelper.SOUTHWEST;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_LEFT;
import static com.carpentersblocks.renderer.helper.VertexHelper.TOP_RIGHT;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import com.carpentersblocks.block.BlockCarpentersSlope;
import com.carpentersblocks.block.BlockCoverable;
import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.Slope.Type;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.renderer.helper.VertexHelper;
import com.carpentersblocks.renderer.helper.slope.oblique.HelperCorner;
import com.carpentersblocks.renderer.helper.slope.oblique.HelperOblWedge;
import com.carpentersblocks.renderer.helper.slope.oblique.HelperOblique;
import com.carpentersblocks.renderer.helper.slope.oblique.HelperPrism;
import com.carpentersblocks.renderer.helper.slope.orthogonal.HelperOrthoWedge;
import com.carpentersblocks.renderer.helper.slope.orthogonal.HelperTriangle;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerCarpentersSlope extends BlockHandlerSloped {

    /* Render IDs */

    private static final int NORMAL_YN              = 0;
    private static final int NORMAL_YP              = 1;
    private static final int NORMAL_ZN              = 2;
    private static final int NORMAL_ZP              = 3;
    private static final int NORMAL_XN              = 4;
    private static final int NORMAL_XP              = 5;
    private static final int TRIANGLE_XZNN          = 6;
    private static final int TRIANGLE_XZNP          = 7;
    private static final int TRIANGLE_XZPP          = 8;
    private static final int TRIANGLE_XZPN          = 9;
    private static final int TRIANGLE_ZXNP          = 10;
    private static final int TRIANGLE_ZXNN          = 11;
    private static final int TRIANGLE_ZXPN          = 12;
    private static final int TRIANGLE_ZXPP          = 13;
    private static final int WEDGE_YN               = 14;
    private static final int WEDGE_YP               = 15;
    private static final int WEDGE_SLOPED_ZN        = 16;
    private static final int WEDGE_ZN               = 17;
    private static final int WEDGE_SLOPED_ZP        = 18;
    private static final int WEDGE_ZP               = 19;
    private static final int WEDGE_SLOPED_XN        = 20;
    private static final int WEDGE_XN               = 21;
    private static final int WEDGE_SLOPED_XP        = 22;
    private static final int WEDGE_XP               = 23;
    private static final int WEDGE_CORNER_SLOPED_ZN = 24;
    private static final int WEDGE_CORNER_SLOPED_ZP = 25;
    private static final int WEDGE_CORNER_SLOPED_XN = 26;
    private static final int WEDGE_CORNER_SLOPED_XP = 27;
    private static final int OBL_INT_YN             = 28;
    private static final int OBL_INT_YP             = 29;
    private static final int OBL_EXT_LEFT_YP        = 30;
    private static final int OBL_EXT_RIGHT_YP       = 31;
    private static final int OBL_EXT_LEFT_YN        = 32;
    private static final int OBL_EXT_RIGHT_YN       = 33;
    private static final int PRISM_YZNN             = 34;
    private static final int PRISM_YZNP             = 35;
    private static final int PRISM_YXNN             = 36;
    private static final int PRISM_YXNP             = 37;
    private static final int PRISM_YZPN             = 38;
    private static final int PRISM_YZPP             = 39;
    private static final int PRISM_YXPN             = 40;
    private static final int PRISM_YXPP             = 41;
    private static final int PRISM_NORTH_XN         = 42;
    private static final int PRISM_NORTH_XP         = 43;
    private static final int PRISM_SOUTH_XN         = 44;
    private static final int PRISM_SOUTH_XP         = 45;
    private static final int PRISM_WEST_ZN          = 46;
    private static final int PRISM_WEST_ZP          = 47;
    private static final int PRISM_EAST_ZN          = 48;
    private static final int PRISM_EAST_ZP          = 49;
    private static final int PRISM_WEDGE_ZN         = 50;
    private static final int PRISM_WEDGE_ZP         = 51;
    private static final int PRISM_WEDGE_XN         = 52;
    private static final int PRISM_WEDGE_XP         = 53;

    /* Lightness values. */

    private final float LIGHTNESS_XYNN              = 0.6F;
    private final float LIGHTNESS_XYPN              = 0.55F;
    private final float LIGHTNESS_ZYNN              = 0.7F;
    private final float LIGHTNESS_ZYPN              = 0.65F;
    private final float LIGHTNESS_XYNP              = 0.85F;
    private final float LIGHTNESS_XYPP              = 0.8F;
    private final float LIGHTNESS_ZYNP              = 0.95F;
    private final float LIGHTNESS_ZYPP              = 0.9F;
    private final float LIGHTNESS_SIDE_WEDGE        = 0.7F;
    private final float LIGHTNESS_POS_OBL           = 0.9F;
    private final float LIGHTNESS_NEG_OBL           = 0.65F;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        IIcon icon = renderBlocks.getIconSafe(((BlockCoverable)block).getIcon());

        renderBlocks.setRenderBounds(0, 0, 0, 1.0D, 1.0D, 1.0D);

        switch (metadata) {
            case BlockCarpentersSlope.META_PRISM_SLOPE:

                tessellator.startDrawing(GL11.GL_TRIANGLES);
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                HelperTriangle.renderFaceXNegZNeg(renderBlocks, 0.0D, 0.0D, 0.0D, icon);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                HelperTriangle.renderFaceXNegZPos(renderBlocks, 0.0D, 0.0D, 0.0D, icon);

                tessellator.setNormal(0.0F, 0.5F, -1.0F);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
                HelperPrism.renderWestPointSlopeZNeg(renderBlocks, 0.0D, 0.0D, 0.0D, IconRegistry.icon_uncovered_quartered);
                tessellator.setNormal(0.0F, 0.5F, 1.0F);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
                HelperPrism.renderWestPointSlopeZPos(renderBlocks, 0.0D, 0.0D, 0.0D, IconRegistry.icon_uncovered_quartered);

                tessellator.draw();
                renderBlocks.setRenderBounds(0, 0, 0, 1.0D, 1.0D, 1.0D);
                // Do not break here, render the wedge below as well.

            case BlockCarpentersSlope.META_WEDGE:

                tessellator.startDrawing(GL11.GL_TRIANGLES);
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                HelperOrthoWedge.renderFaceZNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                HelperOrthoWedge.renderFaceZPos(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, icon);

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                RenderHelper.renderFaceYNeg(renderBlocks, 0.0D, 0.0D, 0.0D, block.getIcon(0, 16));
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                RenderHelper.renderFaceXPos(renderBlocks, 0.0D, 0.0D, 0.0D, block.getIcon(2, 16));
                tessellator.setNormal(-1.0F, 0.5F, 0.0F);
                HelperOblWedge.renderSlopeXNeg(renderBlocks, Slope.ID_WEDGE_POS_W, 0.0D, 0.0D, 0.0D, icon);

                break;
            case BlockCarpentersSlope.META_OBLIQUE_INT:

                tessellator.startDrawing(GL11.GL_TRIANGLES);
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                HelperOrthoWedge.renderFaceYPos(renderBlocks, Slope.ID_OBL_INT_POS_SW, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                HelperOrthoWedge.renderFaceZPos(renderBlocks, Slope.ID_OBL_INT_POS_SW, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(-1.0F, 0.0F, 0.0F);
                HelperOrthoWedge.renderFaceXNeg(renderBlocks, Slope.ID_OBL_INT_POS_SW, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(-1.0F, 0.5F, 1.0F);
                HelperOblique.renderIntObliqueYPos(renderBlocks, Slope.ID_OBL_INT_POS_SW, 0.0D, 0.0D, 0.0D, IconRegistry.icon_uncovered_oblique_pos);

                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1.0F);
                RenderHelper.renderFaceZNeg(renderBlocks, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                RenderHelper.renderFaceXPos(renderBlocks, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                RenderHelper.renderFaceYNeg(renderBlocks, 0.0D, 0.0D, 0.0D, icon);

                break;
            case BlockCarpentersSlope.META_OBLIQUE_EXT:

                tessellator.startDrawing(GL11.GL_TRIANGLES);
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                HelperOrthoWedge.renderFaceYNeg(renderBlocks, Slope.ID_OBL_EXT_POS_SW, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                HelperOrthoWedge.renderFaceZNeg(renderBlocks, Slope.ID_OBL_EXT_POS_SW, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                HelperOrthoWedge.renderFaceXPos(renderBlocks, Slope.ID_OBL_EXT_POS_SW, 0.0D, 0.0D, 0.0D, icon);
                tessellator.setNormal(-1.0F, 0.5F, 1.0F);
                HelperOblique.renderExtObliqueYPosLeft(renderBlocks, Slope.ID_OBL_EXT_POS_SW, 0.0D, 0.0D, 0.0D, IconRegistry.icon_uncovered_oblique_pos);
                HelperOblique.renderExtObliqueYPosRight(renderBlocks, Slope.ID_OBL_EXT_POS_SW, 0.0D, 0.0D, 0.0D, IconRegistry.icon_uncovered_oblique_pos);

                break;
            case BlockCarpentersSlope.META_PRISM:

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

                tessellator.startDrawing(GL11.GL_TRIANGLES);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                tessellator.setNormal(-1.0F, 0.5F, 0.0F);
                HelperPrism.renderSlopeYPosZNeg(renderBlocks, 0.0D, 0.0D, 0.0D, icon);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                tessellator.setNormal(1.0F, 0.5F, 0.0F);
                HelperPrism.renderSlopeYPosZPos(renderBlocks, 0.0D, 0.0D, 0.0D, icon);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
                tessellator.setNormal(0.0F, 0.5F, -1.0F);
                HelperPrism.renderSlopeYPosXNeg(renderBlocks, 0.0D, 0.0D, 0.0D, icon);
                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
                tessellator.setNormal(0.0F, 0.5F, 1.0F);
                HelperPrism.renderSlopeYPosXPos(renderBlocks, 0.0D, 0.0D, 0.0D, icon);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1.0F, 0.0F);
                RenderHelper.renderFaceYNeg(renderBlocks, 0.0D, 0.0D, 0.0D, icon);

                break;
            default:
                return;
        }

        tessellator.draw();

        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    private boolean forceFullFrame = false;

    @Override
    /**
     * Override to provide custom icons.
     */
    protected IIcon getUniqueIcon(ItemStack itemStack, int side, IIcon icon)
    {
        if (isSideSloped)
        {
            Block block = BlockProperties.toBlock(itemStack);
            Slope slope = Slope.slopesList[BlockProperties.getMetadata(TE)];
            int metadata = itemStack.getItemDamage();

            if (!BlockProperties.hasCover(TE, 6)) {

                switch (slope.type) {
                    case OBLIQUE_EXT:
                    case OBLIQUE_INT:
                        icon = slope.isPositive ? IconRegistry.icon_uncovered_oblique_pos : IconRegistry.icon_uncovered_oblique_neg;
                        break;
                    case PRISM:
                    case PRISM_1P:
                    case PRISM_2P:
                    case PRISM_3P:
                    case PRISM_4P:
                    case PRISM_WEDGE:

                        if (forceFullFrame) {
                            icon = IconRegistry.icon_uncovered_full;
                        } else {
                            icon = IconRegistry.icon_uncovered_quartered;
                        }

                        break;
                    default:
                        break;
                }

            } else {

                /* For directional blocks, make sure sloped icons match regardless of side. */

                if (BlockProperties.blockRotates(itemStack)) {
                    if (metadata % 8 == 0) {
                        icon = block.getIcon(slope.isPositive ? 1 : 0, metadata);
                    } else {
                        icon = block.getIcon(2, metadata);
                    }
                } else if (block instanceof BlockDirectional && !slope.type.equals(Type.WEDGE)) {
                    icon = block.getBlockTextureFromSide(1);
                }

                /* Grass-type blocks have unique top faces that we must force on positive sloped sides. */

                if (slope.isPositive) {
                    if (block.getMaterial().equals(Material.grass)) {
                        icon = block.getIcon(1, metadata);
                    }
                }

            }
        }

        return icon;
    }

    @Override
    /**
     * Renders side.
     */
    protected void renderBaseSide(int x, int y, int z, int side, IIcon icon)
    {
        int slopeID = BlockProperties.getMetadata(TE);

        switch (renderID)
        {
            case NORMAL_YN:
                RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
                break;
            case NORMAL_YP:
                RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
                break;
            case NORMAL_ZN:
                RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
                break;
            case NORMAL_ZP:
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
                break;
            case NORMAL_XN:
                RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
                break;
            case NORMAL_XP:
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_XZNN:
                HelperTriangle.renderFaceXNegZNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_XZNP:
                HelperTriangle.renderFaceXNegZPos(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_XZPP:
                HelperTriangle.renderFaceXPosZPos(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_XZPN:
                HelperTriangle.renderFaceXPosZNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXNP:
                HelperTriangle.renderFaceZNegXPos(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXNN:
                HelperTriangle.renderFaceZNegXNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXPN:
                HelperTriangle.renderFaceZPosXNeg(renderBlocks, x, y, z, icon);
                break;
            case TRIANGLE_ZXPP:
                HelperTriangle.renderFaceZPosXPos(renderBlocks, x, y, z, icon);
                break;
            case WEDGE_YN:
                HelperOrthoWedge.renderFaceYNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_YP:
                HelperOrthoWedge.renderFaceYPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_SLOPED_ZN:
                HelperOblWedge.renderSlopeZNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_ZN:
                HelperOrthoWedge.renderFaceZNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_SLOPED_ZP:
                HelperOblWedge.renderSlopeZPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_ZP:
                HelperOrthoWedge.renderFaceZPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_SLOPED_XN:
                HelperOblWedge.renderSlopeXNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_XN:
                HelperOrthoWedge.renderFaceXNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_SLOPED_XP:
                HelperOblWedge.renderSlopeXPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_XP:
                HelperOrthoWedge.renderFaceXPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_CORNER_SLOPED_ZN:
                HelperCorner.renderSlopeZNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_CORNER_SLOPED_ZP:
                HelperCorner.renderSlopeZPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_CORNER_SLOPED_XN:
                HelperCorner.renderSlopeXNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case WEDGE_CORNER_SLOPED_XP:
                HelperCorner.renderSlopeXPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_INT_YN:
                HelperOblique.renderIntObliqueYNeg(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_INT_YP:
                HelperOblique.renderIntObliqueYPos(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_LEFT_YP:
                HelperOblique.renderExtObliqueYPosLeft(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_RIGHT_YP:
                HelperOblique.renderExtObliqueYPosRight(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_LEFT_YN:
                HelperOblique.renderExtObliqueYNegLeft(renderBlocks, slopeID, x, y, z, icon);
                break;
            case OBL_EXT_RIGHT_YN:
                HelperOblique.renderExtObliqueYNegRight(renderBlocks, slopeID, x, y, z, icon);
                break;
            case PRISM_YZNN:
                HelperPrism.renderSlopeYNegZNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_YZNP:
                HelperPrism.renderSlopeYNegZPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_YXNN:
                HelperPrism.renderSlopeYNegXNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_YXNP:
                HelperPrism.renderSlopeYNegXPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_YZPN:
                HelperPrism.renderSlopeYPosZNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_YZPP:
                HelperPrism.renderSlopeYPosZPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_YXPN:
                HelperPrism.renderSlopeYPosXNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_YXPP:
                HelperPrism.renderSlopeYPosXPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_NORTH_XN:
                HelperPrism.renderNorthPointSlopeXNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_NORTH_XP:
                HelperPrism.renderNorthPointSlopeXPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_SOUTH_XN:
                HelperPrism.renderSouthPointSlopeXNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_SOUTH_XP:
                HelperPrism.renderSouthPointSlopeXPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_WEST_ZN:
                HelperPrism.renderWestPointSlopeZNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_WEST_ZP:
                HelperPrism.renderWestPointSlopeZPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_EAST_ZN:
                HelperPrism.renderEastPointSlopeZNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_EAST_ZP:
                HelperPrism.renderEastPointSlopeZPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_WEDGE_ZN:
                HelperPrism.renderWedgeSlopeZNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_WEDGE_ZP:
                HelperPrism.renderWedgeSlopeZPos(renderBlocks, x, y, z, icon);
                break;
            case PRISM_WEDGE_XN:
                HelperPrism.renderWedgeSlopeXNeg(renderBlocks, x, y, z, icon);
                break;
            case PRISM_WEDGE_XP:
                HelperPrism.renderWedgeSlopeXPos(renderBlocks, x, y, z, icon);
                break;
        }
    }

    @Override
    /**
     * Renders slope.
     */
    public void renderBaseBlock(ItemStack itemStack, int x, int y, int z)
    {
        Slope slope = Slope.slopesList[BlockProperties.getMetadata(TE)];

        renderBlocks.enableAO = getEnableAO(itemStack);

        /* Render sloped faces. */

        isSideSloped = true;

        switch (slope.getPrimaryType()) {
            case WEDGE_SIDE:
                prepareHorizontalWedge(itemStack, slope, x, y, z);
                break;
            case WEDGE:
                prepareVerticalWedge(itemStack, slope, x, y, z);
                break;
            case WEDGE_INT:
                prepareWedgeIntCorner(itemStack, slope, x, y, z);
                break;
            case WEDGE_EXT:
                prepareWedgeExtCorner(itemStack, slope, x, y, z);
                break;
            case OBLIQUE_INT:
                prepareObliqueIntCorner(itemStack, slope, x, y, z);
                break;
            case OBLIQUE_EXT:
                prepareObliqueExtCorner(itemStack, slope, x, y, z);
                break;
            case PRISM:
                preparePrism(itemStack, slope, x, y, z);
                break;
            case PRISM_WEDGE:
                preparePrismWedge(itemStack, slope, x, y, z);
                break;
            default:
                break;
        }

        isSideSloped = false;

        lightingHelper.clearLightnessOverride();

        /* Render non-sloped faces. */

        if (slope.hasSide(ForgeDirection.DOWN) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN)) {
            prepareFaceYNeg(itemStack, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.UP) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP)) {
            prepareFaceYPos(itemStack, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.NORTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH)) {
            prepareFaceZNeg(itemStack, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.SOUTH) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH)) {
            prepareFaceZPos(itemStack, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.WEST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST)) {
            prepareFaceXNeg(itemStack, slope, x, y, z);
        }
        if (slope.hasSide(ForgeDirection.EAST) && srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST)) {
            prepareFaceXPos(itemStack, slope, x, y, z);
        }

        VertexHelper.startDrawing(GL11.GL_QUADS);

        renderBlocks.enableAO = false;
    }

    @Override
    protected boolean isPositiveFace(int side)
    {
        return super.isPositiveFace(side) || isSideSloped && Slope.slopesList[BlockProperties.getMetadata(TE)].isPositive;
    }

    /**
     * Will set lighting and render prism sloped faces.
     */
    private void preparePrism(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        int POINT_N = 0;
        int POINT_S = 1;
        int POINT_W = 2;
        int POINT_E = 3;

        List<Integer> pieceList = new ArrayList<Integer>();

        /* Add prism pieces. */

        if (slope.facings.contains(ForgeDirection.NORTH)) {
            pieceList.add(POINT_N);
        }
        if (slope.facings.contains(ForgeDirection.SOUTH)) {
            pieceList.add(POINT_S);
        }
        if (slope.facings.contains(ForgeDirection.WEST)) {
            pieceList.add(POINT_W);
        }
        if (slope.facings.contains(ForgeDirection.EAST)) {
            pieceList.add(POINT_E);
        }

        /* Begin rendering sloped pieces. */

        VertexHelper.startDrawing(GL11.GL_TRIANGLES);

        if (pieceList.contains(POINT_N)) {

            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_W);
            setIDAndRender(itemStack, PRISM_NORTH_XN, x, y, z, WEST);
            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_E);
            setIDAndRender(itemStack, PRISM_NORTH_XP, x, y, z, EAST);

        } else {

            if (slope.isPositive) {
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_N);
                setIDAndRender(itemStack, PRISM_YZPN, x, y, z, NORTH);
            } else {
                renderBlocks.setRenderBounds(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 0.5D);
                setWedgeLighting(itemStack, Slope.WEDGE_NEG_N);
                setIDAndRender(itemStack, PRISM_YZNN, x, y, z, NORTH);
            }

        }

        if (pieceList.contains(POINT_S)) {

            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_W);
            setIDAndRender(itemStack, PRISM_SOUTH_XN, x, y, z, WEST);
            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_E);
            setIDAndRender(itemStack, PRISM_SOUTH_XP, x, y, z, EAST);

        } else {

            if (slope.isPositive) {
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_S);
                setIDAndRender(itemStack, PRISM_YZPP, x, y, z, SOUTH);
            } else {
                renderBlocks.setRenderBounds(0.0D, 0.5D, 0.5D, 1.0D, 1.0D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_NEG_S);
                setIDAndRender(itemStack, PRISM_YZNP, x, y, z, SOUTH);
            }

        }

        if (pieceList.contains(POINT_W)) {

            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_N);
            setIDAndRender(itemStack, PRISM_WEST_ZN, x, y, z, NORTH);
            renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_S);
            setIDAndRender(itemStack, PRISM_WEST_ZP, x, y, z, SOUTH);

        } else {

            if (slope.isPositive) {
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_W);
                setIDAndRender(itemStack, PRISM_YXPN, x, y, z, WEST);
            } else {
                renderBlocks.setRenderBounds(0.0D, 0.5D, 0.0D, 0.5D, 1.0D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_NEG_W);
                setIDAndRender(itemStack, PRISM_YXNN, x, y, z, WEST);
            }

        }

        if (pieceList.contains(POINT_E)) {

            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_N);
            setIDAndRender(itemStack, PRISM_EAST_ZN, x, y, z, NORTH);
            renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
            setWedgeLighting(itemStack, Slope.WEDGE_POS_S);
            setIDAndRender(itemStack, PRISM_EAST_ZP, x, y, z, SOUTH);

        } else {

            if (slope.isPositive) {
                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_E);
                setIDAndRender(itemStack, PRISM_YXPP, x, y, z, EAST);
            } else {
                renderBlocks.setRenderBounds(0.5D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_NEG_E);
                setIDAndRender(itemStack, PRISM_YXNP, x, y, z, EAST);
            }

        }
    }

    /**
     * Will set lighting and render prism wedge sloped faces.
     */
    private void preparePrismWedge(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        switch (slope.slopeID) {
            case Slope.ID_PRISM_WEDGE_POS_N:

                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_W);
                setIDAndRender(itemStack, PRISM_NORTH_XN, x, y, z, WEST);
                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_E);
                setIDAndRender(itemStack, PRISM_NORTH_XP, x, y, z, EAST);

                VertexHelper.startDrawing(GL11.GL_QUADS);

                forceFullFrame = true;
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_N);
                setIDAndRender(itemStack, PRISM_WEDGE_ZN, x, y, z, NORTH);
                forceFullFrame = false;

                break;
            case Slope.ID_PRISM_WEDGE_POS_S:

                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_W);
                setIDAndRender(itemStack, PRISM_SOUTH_XN, x, y, z, WEST);
                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_E);
                setIDAndRender(itemStack, PRISM_SOUTH_XP, x, y, z, EAST);

                VertexHelper.startDrawing(GL11.GL_QUADS);

                forceFullFrame = true;
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_S);
                setIDAndRender(itemStack, PRISM_WEDGE_ZP, x, y, z, SOUTH);
                forceFullFrame = false;

                break;
            case Slope.ID_PRISM_WEDGE_POS_W:

                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 0.5D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_N);
                setIDAndRender(itemStack, PRISM_WEST_ZN, x, y, z, NORTH);
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 0.5D, 0.5D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_S);
                setIDAndRender(itemStack, PRISM_WEST_ZP, x, y, z, SOUTH);

                VertexHelper.startDrawing(GL11.GL_QUADS);

                forceFullFrame = true;
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_W);
                setIDAndRender(itemStack, PRISM_WEDGE_XN, x, y, z, WEST);
                forceFullFrame = false;

                break;
            case Slope.ID_PRISM_WEDGE_POS_E:

                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_N);
                setIDAndRender(itemStack, PRISM_EAST_ZN, x, y, z, NORTH);
                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_S);
                setIDAndRender(itemStack, PRISM_EAST_ZP, x, y, z, SOUTH);

                VertexHelper.startDrawing(GL11.GL_QUADS);

                forceFullFrame = true;
                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                setWedgeLighting(itemStack, Slope.WEDGE_POS_E);
                setIDAndRender(itemStack, PRISM_WEDGE_XP, x, y, z, EAST);
                forceFullFrame = false;

                break;
        }
    }

    /**
     * Will set lighting for wedge sloped faces.  Many slope types
     * make use of these lighting parameters in addition to wedges.
     */
    private void setWedgeLighting(ItemStack itemStack, Slope slope)
    {
        prepareLighting(itemStack);

        World world = TE.getWorldObj();

        boolean solid_YP = world.isSideSolid(TE.xCoord, TE.yCoord + 1, TE.zCoord, ForgeDirection.DOWN);
        boolean solid_YN = world.isSideSolid(TE.xCoord, TE.yCoord - 1, TE.zCoord, ForgeDirection.UP);

        switch (slope.slopeID) {
            case Slope.ID_WEDGE_NW:

                lightingHelper.setLightnessOverride(LIGHTNESS_SIDE_WEDGE);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[WEST][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[WEST][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[NORTH][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[NORTH][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[WEST][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[WEST][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[NORTH][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[NORTH][TOP_RIGHT];

                }

                break;
            case Slope.ID_WEDGE_NE:

                lightingHelper.setLightnessOverride(LIGHTNESS_SIDE_WEDGE);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[NORTH][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[NORTH][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[EAST][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[EAST][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[NORTH][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[NORTH][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[EAST][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[EAST][TOP_RIGHT];

                }

                break;
            case Slope.ID_WEDGE_SW:

                lightingHelper.setLightnessOverride(LIGHTNESS_SIDE_WEDGE);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[SOUTH][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[SOUTH][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[WEST][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[WEST][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[SOUTH][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[SOUTH][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[WEST][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[WEST][TOP_RIGHT];

                }

                break;
            case Slope.ID_WEDGE_SE:

                lightingHelper.setLightnessOverride(LIGHTNESS_SIDE_WEDGE);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[TOP_LEFT]     = offset_ao[EAST][TOP_LEFT];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[EAST][BOTTOM_LEFT];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[SOUTH][BOTTOM_RIGHT];
                    lightingHelper.ao[TOP_RIGHT]    = offset_ao[SOUTH][TOP_RIGHT];

                    renderBlocks.brightnessTopLeft     = offset_brightness[EAST][TOP_LEFT];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[EAST][BOTTOM_LEFT];
                    renderBlocks.brightnessBottomRight = offset_brightness[SOUTH][BOTTOM_RIGHT];
                    renderBlocks.brightnessTopRight    = offset_brightness[SOUTH][TOP_RIGHT];

                }

                break;
            case Slope.ID_WEDGE_NEG_N:

                lightingHelper.setLightnessOverride(LIGHTNESS_ZYNN);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = solid_YN ? offset_ao[UP][SOUTHEAST] : ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = solid_YN ? offset_ao[UP][SOUTHWEST] : ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YN ? offset_brightness[UP][SOUTHEAST] : brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = solid_YN ? offset_brightness[UP][SOUTHWEST] : brightness[DOWN][SOUTHWEST];

                }

                break;
            case Slope.ID_WEDGE_POS_N:

                lightingHelper.setLightnessOverride(LIGHTNESS_ZYNP);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = solid_YP ? offset_ao[DOWN][SOUTHEAST] : ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = solid_YP ? offset_ao[DOWN][SOUTHWEST] : ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][SOUTHEAST] : brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = solid_YP ? offset_brightness[DOWN][SOUTHWEST] : brightness[UP][SOUTHWEST];

                }

                break;
            case Slope.ID_WEDGE_NEG_S:

                lightingHelper.setLightnessOverride(LIGHTNESS_ZYPN);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = solid_YN ? offset_ao[UP][NORTHEAST] : ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YN ? offset_ao[UP][NORTHWEST] : ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = solid_YN ? offset_brightness[UP][NORTHEAST] : brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YN ? offset_brightness[UP][NORTHWEST] : brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[DOWN][SOUTHWEST];

                }

                break;
            case Slope.ID_WEDGE_POS_S:

                lightingHelper.setLightnessOverride(LIGHTNESS_ZYPP);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = solid_YP ? offset_ao[DOWN][NORTHEAST] : ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YP ? offset_ao[DOWN][NORTHWEST] : ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = solid_YP ? offset_brightness[DOWN][NORTHEAST] : brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YP ? offset_brightness[DOWN][NORTHWEST] : brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[UP][SOUTHWEST];

                }

                break;
            case Slope.ID_WEDGE_NEG_W:

                lightingHelper.setLightnessOverride(LIGHTNESS_XYNN);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = solid_YN ? offset_ao[UP][SOUTHEAST] : ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = solid_YN ? offset_ao[UP][NORTHEAST] : ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YN ? offset_brightness[UP][SOUTHEAST] : brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = solid_YN ? offset_brightness[UP][NORTHEAST] : brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[DOWN][SOUTHWEST];

                }

                break;
            case Slope.ID_WEDGE_POS_W:

                lightingHelper.setLightnessOverride(LIGHTNESS_XYNP);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = solid_YP ? offset_ao[DOWN][SOUTHEAST] : ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = solid_YP ? offset_ao[DOWN][NORTHEAST] : ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][SOUTHEAST] : brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = solid_YP ? offset_brightness[DOWN][NORTHEAST] : brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[UP][SOUTHWEST];

                }

                break;
            case Slope.ID_WEDGE_NEG_E:

                lightingHelper.setLightnessOverride(LIGHTNESS_XYPN);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YN ? offset_ao[UP][NORTHWEST] : ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = solid_YN ? offset_ao[UP][SOUTHWEST] : ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YN ? offset_brightness[UP][NORTHWEST] : brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = solid_YN ? offset_brightness[UP][SOUTHWEST] : brightness[DOWN][SOUTHWEST];

                }

                break;
            case Slope.ID_WEDGE_POS_E:

                lightingHelper.setLightnessOverride(LIGHTNESS_XYPP);

                if (renderBlocks.enableAO) {

                    lightingHelper.ao[SOUTHEAST] = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YP ? offset_ao[DOWN][NORTHWEST] : ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = solid_YP ? offset_ao[DOWN][SOUTHWEST] : ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YP ? offset_brightness[DOWN][NORTHWEST] : brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = solid_YP ? offset_brightness[DOWN][SOUTHWEST] : brightness[UP][SOUTHWEST];

                }

                break;
        }
    }

    private void prepareHorizontalWedge(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        VertexHelper.startDrawing(GL11.GL_QUADS);

        setWedgeLighting(itemStack, slope);

        if (slope.facings.contains(ForgeDirection.NORTH)) {
            setIDAndRender(itemStack, WEDGE_SLOPED_ZN, x, y, z, EAST);
        } else {
            setIDAndRender(itemStack, WEDGE_SLOPED_ZP, x, y, z, WEST);
        }
    }

    private void prepareVerticalWedge(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        VertexHelper.startDrawing(GL11.GL_QUADS);

        setWedgeLighting(itemStack, slope);

        if (slope.facings.contains(ForgeDirection.NORTH)) {
            setIDAndRender(itemStack, WEDGE_SLOPED_ZN, x, y, z, NORTH);
        } else if (slope.facings.contains(ForgeDirection.SOUTH)) {
            setIDAndRender(itemStack, WEDGE_SLOPED_ZP, x, y, z, SOUTH);
        } else if (slope.facings.contains(ForgeDirection.WEST)) {
            setIDAndRender(itemStack, WEDGE_SLOPED_XN, x, y, z, WEST);
        } else { // ForgeDirection.EAST
            setIDAndRender(itemStack, WEDGE_SLOPED_XP, x, y, z, EAST);
        }
    }

    private void prepareWedgeIntCorner(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        VertexHelper.startDrawing(GL11.GL_TRIANGLES);

        Slope slopeX = slope.facings.contains(ForgeDirection.WEST) ? slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W : slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E;

        setWedgeLighting(itemStack, slopeX);

        if (slopeX.facings.contains(ForgeDirection.WEST)) {
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
        } else { // ForgeDirection.EAST
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
        }

        Slope slopeZ = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

        setWedgeLighting(itemStack, slopeZ);

        if (slope.facings.contains(ForgeDirection.NORTH)) {
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_ZN, x, y, z, NORTH);
        } else {
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_ZP, x, y, z, SOUTH);
        }
    }

    private void prepareWedgeExtCorner(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        VertexHelper.startDrawing(GL11.GL_TRIANGLES);

        Slope slopeX = slope.facings.contains(ForgeDirection.WEST) ? slope.isPositive ? Slope.WEDGE_POS_W : Slope.WEDGE_NEG_W : slope.isPositive ? Slope.WEDGE_POS_E : Slope.WEDGE_NEG_E;

        setWedgeLighting(itemStack, slopeX);

        if (slopeX.facings.contains(ForgeDirection.WEST)) {
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_XN, x, y, z, WEST);
        } else { // ForgeDirection.EAST
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_XP, x, y, z, EAST);
        }

        Slope slopeZ = slope.facings.contains(ForgeDirection.NORTH) ? slope.isPositive ? Slope.WEDGE_POS_N : Slope.WEDGE_NEG_N : slope.isPositive ? Slope.WEDGE_POS_S : Slope.WEDGE_NEG_S;

        setWedgeLighting(itemStack, slopeZ);

        if (slope.facings.contains(ForgeDirection.NORTH)) {
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_ZN, x, y, z, NORTH);
        } else {
            setIDAndRender(itemStack, WEDGE_CORNER_SLOPED_ZP, x, y, z, SOUTH);
        }
    }

    private void prepareObliqueIntCorner(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        VertexHelper.startDrawing(GL11.GL_TRIANGLES);

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        prepareLighting(itemStack);

        World world = TE.getWorldObj();

        boolean solid_YP = world.isSideSolid(TE.xCoord, TE.yCoord + 1, TE.zCoord, ForgeDirection.DOWN);
        boolean solid_YN = world.isSideSolid(TE.xCoord, TE.yCoord - 1, TE.zCoord, ForgeDirection.UP);

        if (renderBlocks.enableAO) {

            switch (slope.slopeID) {
                case Slope.ID_OBL_INT_NEG_NW:

                    lightingHelper.ao[NORTHEAST] = solid_YN ? offset_ao[UP][NORTHEAST] : ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = solid_YN ? offset_ao[UP][SOUTHWEST] : ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessBottomLeft  = solid_YN ? offset_brightness[UP][NORTHEAST] : brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = solid_YN ? offset_brightness[UP][SOUTHWEST] : brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_NEG_NE:

                    lightingHelper.ao[SOUTHEAST] = solid_YN ? offset_ao[UP][SOUTHEAST] : ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YN ? offset_ao[UP][NORTHWEST] : ao[DOWN][NORTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YN ? offset_brightness[UP][SOUTHEAST] : brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YN ? offset_brightness[UP][NORTHWEST] : brightness[DOWN][NORTHWEST];

                    break;
                case Slope.ID_OBL_INT_NEG_SW:

                    lightingHelper.ao[SOUTHEAST] = solid_YN ? offset_ao[UP][SOUTHEAST] : ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YN ? offset_ao[UP][NORTHWEST] : ao[DOWN][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YN ? offset_brightness[UP][SOUTHEAST] : brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YN ? offset_brightness[UP][NORTHWEST] : brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_NEG_SE:

                    lightingHelper.ao[SOUTHEAST] = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = solid_YN ? offset_ao[UP][NORTHEAST] : ao[DOWN][NORTHEAST];
                    lightingHelper.ao[SOUTHWEST] = solid_YN ? offset_ao[UP][SOUTHWEST] : ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft = solid_YN ? offset_brightness[UP][NORTHEAST] : brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessTopRight   = solid_YN ? offset_brightness[UP][SOUTHWEST] : brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_NW:

                    lightingHelper.ao[NORTHEAST] = solid_YP ? offset_ao[DOWN][NORTHEAST] : ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = solid_YP ? offset_ao[DOWN][SOUTHWEST] : ao[UP][SOUTHWEST];

                    renderBlocks.brightnessBottomLeft  = solid_YP ? offset_brightness[DOWN][NORTHEAST] : brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = solid_YP ? offset_brightness[DOWN][SOUTHWEST] : brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_NE:

                    lightingHelper.ao[SOUTHEAST] = solid_YP ? offset_ao[DOWN][SOUTHEAST] : ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YP ? offset_ao[DOWN][NORTHWEST] : ao[UP][NORTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][SOUTHEAST] : brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YP ? offset_brightness[DOWN][NORTHWEST] : brightness[UP][NORTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_SW:

                    lightingHelper.ao[SOUTHEAST] = solid_YP ? offset_ao[DOWN][SOUTHEAST] : ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHWEST] = solid_YP ? offset_ao[DOWN][NORTHWEST] : ao[UP][NORTHWEST];
                    lightingHelper.ao[SOUTHWEST] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][SOUTHEAST] : brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomRight = solid_YP ? offset_brightness[DOWN][NORTHWEST] : brightness[UP][NORTHWEST];
                    renderBlocks.brightnessTopRight    = offset_brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_OBL_INT_POS_SE:

                    lightingHelper.ao[SOUTHEAST] = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[NORTHEAST] = solid_YP ? offset_ao[DOWN][NORTHEAST] : ao[UP][NORTHEAST];
                    lightingHelper.ao[SOUTHWEST] = solid_YP ? offset_ao[DOWN][SOUTHWEST] : ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft = solid_YP ? offset_brightness[DOWN][NORTHEAST] : brightness[UP][NORTHEAST];
                    renderBlocks.brightnessTopRight   = solid_YP ? offset_brightness[DOWN][SOUTHWEST] : brightness[UP][SOUTHWEST];

                    break;
            }

        }

        if (slope.isPositive) {
            lightingHelper.setLightnessOverride(LIGHTNESS_POS_OBL);
            setIDAndRender(itemStack, OBL_INT_YP, x, y, z, NORTH);
        } else {
            lightingHelper.setLightnessOverride(LIGHTNESS_NEG_OBL);
            setIDAndRender(itemStack, OBL_INT_YN, x, y, z, NORTH);
        }
    }

    private void prepareObliqueExtCorner(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        VertexHelper.startDrawing(GL11.GL_TRIANGLES);

        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        prepareLighting(itemStack);

        World world = TE.getWorldObj();

        boolean solid_YP = world.isSideSolid(TE.xCoord, TE.yCoord + 1, TE.zCoord, ForgeDirection.DOWN);
        boolean solid_YN = world.isSideSolid(TE.xCoord, TE.yCoord - 1, TE.zCoord, ForgeDirection.UP);

        if (renderBlocks.enableAO) {

            switch (slope.slopeID) {
                case Slope.ID_OBL_EXT_NEG_NW:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][NORTHEAST];
                    lightingHelper.ao[BOTTOM_LEFT] = solid_YN ? offset_ao[UP][SOUTHEAST] : ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][SOUTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessBottomLeft = solid_YN ? offset_brightness[UP][SOUTHEAST] : brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][SOUTHWEST];

                    break;
                case Slope.ID_OBL_EXT_NEG_NE:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][SOUTHEAST];
                    lightingHelper.ao[BOTTOM_LEFT] = solid_YN ? offset_ao[UP][SOUTHWEST] : ao[DOWN][SOUTHWEST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][NORTHWEST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft = solid_YN ? offset_brightness[UP][SOUTHWEST] : brightness[DOWN][SOUTHWEST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][NORTHWEST];

                    break;
                case Slope.ID_OBL_EXT_NEG_SW:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][NORTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT] = solid_YN ? offset_ao[UP][NORTHEAST] : ao[DOWN][NORTHEAST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][SOUTHEAST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessBottomLeft = solid_YN ? offset_brightness[UP][NORTHEAST] : brightness[DOWN][NORTHEAST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][SOUTHEAST];

                    break;
                case Slope.ID_OBL_EXT_NEG_SE:

                    lightingHelper.ao[TOP_LEFT]    = offset_ao[DOWN][SOUTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT] = solid_YN ? offset_ao[UP][NORTHWEST] : ao[DOWN][NORTHWEST];
                    lightingHelper.ao[TOP_RIGHT]   = offset_ao[DOWN][NORTHEAST];

                    renderBlocks.brightnessTopLeft    = offset_brightness[DOWN][SOUTHWEST];
                    renderBlocks.brightnessBottomLeft = solid_YN ? offset_brightness[UP][NORTHWEST] : brightness[DOWN][NORTHWEST];
                    renderBlocks.brightnessTopRight   = offset_brightness[DOWN][NORTHEAST];

                    break;
                case Slope.ID_OBL_EXT_POS_NW:

                    lightingHelper.ao[TOP_LEFT]     = solid_YP ? offset_ao[DOWN][SOUTHEAST] : ao[UP][SOUTHEAST];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][NORTHEAST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][SOUTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][SOUTHEAST] : brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][SOUTHWEST];

                    break;
                case Slope.ID_OBL_EXT_POS_NE:

                    lightingHelper.ao[TOP_LEFT]     = solid_YP ? offset_ao[DOWN][SOUTHWEST] : ao[UP][SOUTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][SOUTHEAST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][NORTHWEST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][SOUTHWEST] : brightness[UP][SOUTHWEST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][SOUTHEAST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHWEST];

                    break;
                case Slope.ID_OBL_EXT_POS_SW:

                    lightingHelper.ao[TOP_LEFT]     = solid_YP ? offset_ao[DOWN][NORTHEAST] : ao[UP][NORTHEAST];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][NORTHWEST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][SOUTHEAST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][NORTHEAST] : brightness[UP][NORTHEAST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][NORTHWEST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][SOUTHEAST];

                    break;
                case Slope.ID_OBL_EXT_POS_SE:

                    lightingHelper.ao[TOP_LEFT]     = solid_YP ? offset_ao[DOWN][NORTHWEST] : ao[UP][NORTHWEST];
                    lightingHelper.ao[BOTTOM_LEFT]  = offset_ao[UP][SOUTHWEST];
                    lightingHelper.ao[BOTTOM_RIGHT] = offset_ao[UP][NORTHEAST];

                    renderBlocks.brightnessTopLeft     = solid_YP ? offset_brightness[DOWN][NORTHWEST] : brightness[UP][NORTHWEST];
                    renderBlocks.brightnessBottomLeft  = offset_brightness[UP][SOUTHWEST];
                    renderBlocks.brightnessBottomRight = offset_brightness[UP][NORTHEAST];

                    break;
            }

        }

        if (slope.isPositive) {
            lightingHelper.setLightnessOverride(LIGHTNESS_POS_OBL);
            setIDAndRender(itemStack, OBL_EXT_LEFT_YP, x, y, z, NORTH);
            setIDAndRender(itemStack, OBL_EXT_RIGHT_YP, x, y, z, NORTH);
        } else {
            lightingHelper.setLightnessOverride(LIGHTNESS_NEG_OBL);
            setIDAndRender(itemStack, OBL_EXT_LEFT_YN, x, y, z, NORTH);
            setIDAndRender(itemStack, OBL_EXT_RIGHT_YN, x, y, z, NORTH);
        }
    }

    /**
     * Prepare bottom face.
     */
    private void prepareFaceYNeg(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setupLightingYNeg(itemStack, x, y, z);

        switch (slope.getFace(ForgeDirection.DOWN)) {
            case WEDGE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);
                setIDAndRender(itemStack, WEDGE_YN, x, y, z, DOWN);
                break;
            default:
                VertexHelper.startDrawing(GL11.GL_QUADS);
                setIDAndRender(itemStack, NORMAL_YN, x, y, z, DOWN);
                break;
        }
    }

    /**
     * Prepare top face.
     */
    private void prepareFaceYPos(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setupLightingYPos(itemStack, x, y, z);

        switch (slope.getFace(ForgeDirection.UP)) {
            case WEDGE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);
                setIDAndRender(itemStack, WEDGE_YP, x, y, z, UP);
                break;
            default:
                VertexHelper.startDrawing(GL11.GL_QUADS);
                setIDAndRender(itemStack, NORMAL_YP, x, y, z, UP);
                break;
        }
    }

    /**
     * Prepare North face.
     */
    private void prepareFaceZNeg(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setupLightingZNeg(itemStack, x, y, z);

        switch (slope.getFace(ForgeDirection.NORTH)) {
            case WEDGE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);
                setIDAndRender(itemStack, WEDGE_ZN, x, y, z, NORTH);
                break;
            case TRIANGLE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setupLightingZNeg(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_ZXNP, x, y, z, NORTH);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
                lightingHelper.setupLightingZNeg(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_ZXNN, x, y, z, NORTH);

                break;
            default:
                VertexHelper.startDrawing(GL11.GL_QUADS);
                setIDAndRender(itemStack, NORMAL_ZN, x, y, z, NORTH);
                break;
        }
    }

    /**
     * Prepare South face.
     */
    private void prepareFaceZPos(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setupLightingZPos(itemStack, x, y, z);

        switch (slope.getFace(ForgeDirection.SOUTH)) {
            case WEDGE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);
                setIDAndRender(itemStack, WEDGE_ZP, x, y, z, SOUTH);
                break;
            case TRIANGLE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 0.5D, 0.5D, 1.0D);
                lightingHelper.setupLightingZPos(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_ZXPN, x, y, z, SOUTH);

                renderBlocks.setRenderBounds(0.5D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setupLightingZPos(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_ZXPP, x, y, z, SOUTH);

                break;
            default:
                VertexHelper.startDrawing(GL11.GL_QUADS);
                setIDAndRender(itemStack, NORMAL_ZP, x, y, z, SOUTH);
                break;
        }
    }

    /**
     * Prepare West face.
     */
    private void prepareFaceXNeg(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setupLightingXNeg(itemStack, x, y, z);

        switch (slope.getFace(ForgeDirection.WEST)) {
            case WEDGE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);
                setIDAndRender(itemStack, WEDGE_XN, x, y, z, WEST);
                break;
            case TRIANGLE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                lightingHelper.setupLightingXNeg(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_XZNN, x, y, z, WEST);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setupLightingXNeg(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_XZNP, x, y, z, WEST);

                break;
            default:
                VertexHelper.startDrawing(GL11.GL_QUADS);
                setIDAndRender(itemStack, NORMAL_XN, x, y, z, WEST);
                break;
        }
    }

    /**
     * Prepare East face.
     */
    private void prepareFaceXPos(ItemStack itemStack, Slope slope, int x, int y, int z)
    {
        renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        lightingHelper.setupLightingXPos(itemStack, x, y, z);

        switch (slope.getFace(ForgeDirection.EAST)) {
            case WEDGE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);
                setIDAndRender(itemStack, WEDGE_XP, x, y, z, EAST);
                break;
            case TRIANGLE:
                VertexHelper.startDrawing(GL11.GL_TRIANGLES);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                lightingHelper.setupLightingXPos(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_XZPP, x, y, z, EAST);

                renderBlocks.setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 0.5D);
                lightingHelper.setupLightingXPos(itemStack, x, y, z);
                setIDAndRender(itemStack, TRIANGLE_XZPN, x, y, z, EAST);

                break;
            default:
                VertexHelper.startDrawing(GL11.GL_QUADS);
                setIDAndRender(itemStack, NORMAL_XP, x, y, z, EAST);
                break;
        }
    }

}
