package com.carpentersblocks.renderer.bakedblock;

import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.WEST;

import java.util.function.Function;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.block.data.SlopeData.Type;
import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.renderer.QuadContainer;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.util.RotationUtil.Rotation;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedSlope extends AbstractBakedModel {

    public BakedSlope(IModelState modelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(modelState, vertexFormat, bakedTextureGetter);
	}
    
/*    *//**
     * Will set lighting and render prism sloped faces.
     *//*
    private void addPrism(List<Quad> quads, Slope slope) {
        int POINT_N = 0;
        int POINT_S = 1;
        int POINT_W = 2;
        int POINT_E = 3;

        List<Integer> pieceList = new ArrayList<Integer>();

        // Add prism pieces

        if (slope.facings.contains(NORTH)) {
            pieceList.add(POINT_N);
        }
        if (slope.facings.contains(SOUTH)) {
            pieceList.add(POINT_S);
        }
        if (slope.facings.contains(WEST)) {
            pieceList.add(POINT_W);
        }
        if (slope.facings.contains(EAST)) {
            pieceList.add(POINT_E);
        }

        // Add sloped pieces

        if (pieceList.contains(POINT_N)) {
            quads.add(getQuad(slope.slopeID, PRISM_NORTH_XN));
            quads.add(getQuad(slope.slopeID, PRISM_NORTH_XP));
        } else {
            if (slope.isPositive) {
                quads.add(getQuad(slope.slopeID, PRISM_YZPN));
            } else {
                quads.add(getQuad(slope.slopeID, PRISM_YZNN));
            }
        }
        if (pieceList.contains(POINT_S)) {
            quads.add(getQuad(slope.slopeID, PRISM_SOUTH_XN));
            quads.add(getQuad(slope.slopeID, PRISM_SOUTH_XP));
        } else {
            if (slope.isPositive) {
                quads.add(getQuad(slope.slopeID, PRISM_YZPP));
            } else {
                quads.add(getQuad(slope.slopeID, PRISM_YZNP));
            }
        }
        if (pieceList.contains(POINT_W)) {
            quads.add(getQuad(slope.slopeID, PRISM_WEST_ZN));
            quads.add(getQuad(slope.slopeID, PRISM_WEST_ZP));
        } else {
            if (slope.isPositive) {
                quads.add(getQuad(slope.slopeID, PRISM_YXPN));
            } else {
                quads.add(getQuad(slope.slopeID, PRISM_YXNN));
            }
        }
        if (pieceList.contains(POINT_E)) {
            quads.add(getQuad(slope.slopeID, PRISM_EAST_ZN));
            quads.add(getQuad(slope.slopeID, PRISM_EAST_ZP));
        } else {
            if (slope.isPositive) {
                quads.add(getQuad(slope.slopeID, PRISM_YXPP));
            } else {
                quads.add(getQuad(slope.slopeID, PRISM_YXNP));
            }
        }
    }*/

/*    *//**
     * Will set lighting and render prism wedge sloped faces.
     *//*
    private void addPrismWedge(List<Quad> quads, Slope slope) {
        switch (slope.slopeID) {
            case Slope.ID_PRISM_WEDGE_POS_N:
                quads.add(getQuad(slope.slopeID, PRISM_NORTH_XN));
                quads.add(getQuad(slope.slopeID, PRISM_NORTH_XP));
                quads.add(getQuad(slope.slopeID, PRISM_WEDGE_ZN));
                break;
            case Slope.ID_PRISM_WEDGE_POS_S:
                quads.add(getQuad(slope.slopeID, PRISM_SOUTH_XN));
                quads.add(getQuad(slope.slopeID, PRISM_SOUTH_XP));
                quads.add(getQuad(slope.slopeID, PRISM_WEDGE_ZP));
                break;
            case Slope.ID_PRISM_WEDGE_POS_W:
                quads.add(getQuad(slope.slopeID, PRISM_WEST_ZN));
                quads.add(getQuad(slope.slopeID, PRISM_WEST_ZP));
                quads.add(getQuad(slope.slopeID, PRISM_WEDGE_XN));
                break;
            case Slope.ID_PRISM_WEDGE_POS_E:
                quads.add(getQuad(slope.slopeID, PRISM_EAST_ZN));
                quads.add(getQuad(slope.slopeID, PRISM_EAST_ZP));
                quads.add(getQuad(slope.slopeID, PRISM_WEDGE_XP));
                break;
        }
    }*/
    
	@Override
	protected void fillQuads(QuadContainer quadContainer) {
		Type type = SlopeData.getType(_cbMetadata);
		switch (type) {
			case WEDGE:
				fillWedgeQuads(quadContainer);
				break;
			case OBLIQUE_INTERIOR:
				fillObliqueInteriorQuads(quadContainer);
				break;
		}
    	Rotation rotation = Rotation.get(_cbMetadata);
    	quadContainer.rotate(rotation);
	}
	
	private void fillWedgeQuads(QuadContainer quadContainer) {
    	quadContainer.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
    	quadContainer.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
    	quadContainer.add(
    		Quad.getQuad(
    			WEST,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(0.0D, 1.0D, 0.0D),
    			new Vec3d(0.0D, 0.0D, 0.0D),
    			new Vec3d(0.0D, 0.0D, 1.0D)));
    	quadContainer.add(
    		Quad.getQuad(
    			EAST,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(1.0D, 0.0D, 1.0D),
    			new Vec3d(1.0D, 0.0D, 0.0D),
    			new Vec3d(1.0D, 1.0D, 0.0D)));
    	quadContainer.add(
    		Quad.getQuad(
                EnumFacing.UP,
                SpriteRegistry.sprite_uncovered_full,
                new Vec3d(1.0D, 0.0D, 1.0D),
                new Vec3d(1.0D, 1.0D, 0.0D),
                new Vec3d(0.0D, 1.0D, 0.0D),
                new Vec3d(0.0D, 0.0D, 1.0D)));
	}
	
	private void fillObliqueInteriorQuads(QuadContainer quadContainer) {
    	quadContainer.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
    	quadContainer.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
    	quadContainer.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
    	quadContainer.add(
    		Quad.getQuad(
    			SOUTH,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(0.0D, 1.0D, 1.0D),
    			new Vec3d(0.0D, 0.0D, 1.0D),
    			new Vec3d(1.0D, 0.0D, 1.0D)));
    	quadContainer.add(
    		Quad.getQuad(
    			EAST,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(1.0D, 0.0D, 1.0D),
    			new Vec3d(1.0D, 0.0D, 0.0D),
    			new Vec3d(1.0D, 1.0D, 0.0D)));
    	quadContainer.add(
    		Quad.getQuad(
                EnumFacing.UP,
                SpriteRegistry.sprite_uncovered_full,
                new Vec3d(0.0D, 1.0D, 0.0D),
                new Vec3d(0.0D, 1.0D, 1.0D),
                new Vec3d(1.0D, 1.0D, 0.0D)));
    	quadContainer.add(
    		Quad.getQuad(
                EnumFacing.UP,
                SpriteRegistry.sprite_uncovered_oblique_pos,
                new Vec3d(0.0D, 1.0D, 1.0D),
                new Vec3d(1.0D, 0.0D, 1.0D),
                new Vec3d(1.0D, 1.0D, 0.0D)));
	}

}
