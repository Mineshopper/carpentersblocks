package com.carpentersblocks.client.renderer.bakedblock.slope;

import static net.minecraft.util.Direction.DOWN;
import static net.minecraft.util.Direction.EAST;
import static net.minecraft.util.Direction.NORTH;
import static net.minecraft.util.Direction.SOUTH;
import static net.minecraft.util.Direction.UP;
import static net.minecraft.util.Direction.WEST;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.client.renderer.RenderPkg;
import com.carpentersblocks.client.renderer.bakedblock.AbstractBakedModel;
import com.carpentersblocks.client.renderer.helper.RenderHelper;
import com.carpentersblocks.client.renderer.helper.RenderHelperSlope;
import com.carpentersblocks.util.RotationUtil.CbRotation;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.vector.Vector3d;

public abstract class AbstractBakedSlope extends AbstractBakedModel {

	public AbstractBakedSlope(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		switch (SlopeData.getType(renderPkg.getCbMetadata())) {
			case WEDGE:
				fillWedge(renderPkg);
				break;
			case WEDGE_INTERIOR:
				fillWedgeInterior(renderPkg);
				break;
			case WEDGE_EXTERIOR:
				fillWedgeExterior(renderPkg);
				break;
			case OBLIQUE_INTERIOR:
				fillObliqueInterior(renderPkg);
				break;
			case OBLIQUE_EXTERIOR:
				fillObliqueExterior(renderPkg);
				break;
			case PRISM_WEDGE:
				fillPrismWedge(renderPkg);
				break;
			case PRISM:
				fillPrism(renderPkg, 0);
				break;
			case PRISM_1P:
				fillPrism(renderPkg, 1);
				break;
			case PRISM_2P:
				fillPrism(renderPkg, 2);
				break;
			case PRISM_3P:
				fillPrism(renderPkg, 3);
				break;
			case PRISM_4P:
				fillPrism(renderPkg, 4);
				break;
			case INVERTED_PRISM:
				fillInvertedPrism(renderPkg, 0);
				break;
			case INVERTED_PRISM_1P:
				fillInvertedPrism(renderPkg, 1);
				break;
			case INVERTED_PRISM_2P:
				fillInvertedPrism(renderPkg, 2);
				break;
			case INVERTED_PRISM_3P:
				fillInvertedPrism(renderPkg, 3);
				break;
			case INVERTED_PRISM_4P:
				fillInvertedPrism(renderPkg, 4);
				break;
		}
		CbRotation rotation = CbRotation.get(renderPkg.getCbMetadata());
		renderPkg.rotate(rotation);
	}
	
	protected void fillWedge(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXNeg());
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D)));
	}
	
	protected void fillWedgeExterior(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXNeg());
		renderPkg.add(RenderHelperSlope.getWedgeExteriorZNeg());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D)));
		renderPkg.add(
			Quad.getQuad(
				EAST,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 0.0D),
				new Vector3d(0.0D, 1.0D, 0.0D)));
	}

	protected void fillWedgeInterior(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(RenderHelperSlope.getWedgeInteriorZPos());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				EAST,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(0.0D, 1.0D, 0.0D)));
	}
	
	protected void fillObliqueInterior(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeInteriorZPos());
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(
			Quad.getQuad(
				UP,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 1.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
		// Left oblique slope part
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_oblique_pos,
				new Vector3d(0.0D, 1.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 1.0D, 0.5D)));
		// Right oblique slope part
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_oblique_pos,
				new Vector3d(0.5D, 1.0D, 0.5D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
	}
	
	protected void fillObliqueExterior(RenderPkg renderPkg) {
		renderPkg.add(
			Quad.getQuad(
				DOWN,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.0D, 0.0D, 0.0D),
				new Vector3d(1.0D, 0.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_oblique_neg,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 0.0D)));
		renderPkg.add(RenderHelperSlope.getWedgeExteriorZNeg());
		renderPkg.add(
			Quad.getQuad(
				WEST,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D)));
	}
	
	protected void fillPrismWedge(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXNeg());
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.5D, 0.5D),
				new Vector3d(0.5D, 1.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.5D, 1.0D, 0.0D),
				new Vector3d(0.5D, 0.5D, 0.5D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				WEST,
				TextureAtlasSprites.sprite_uncovered_quartered,
				new Vector3d(0.5D, 0.5D, 0.5D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.5D, 1.0D)));
		renderPkg.add(
			Quad.getQuad(
				EAST,
				TextureAtlasSprites.sprite_uncovered_quartered,
				new Vector3d(0.5D, 0.5D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.5D, 0.5D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.5D, 0.5D, 1.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.0D, 1.0D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.5D, 0.5D, 1.0D),
				new Vector3d(0.5D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D)));
	}
	
	protected void fillPrism(RenderPkg renderPkg, int numPoints) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		switch (numPoints) {
			case 0:
				renderPkg.add(RenderHelperSlope.getPrismSlopeZNeg());
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 0.0D)));
				break;
			case 1:
				renderPkg.add(RenderHelperSlope.getPrismSlopeZNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				break;
			case 2:
				renderPkg.add(RenderHelperSlope.getPrismZNegXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZNegXPos());
				renderPkg.add(RenderHelperSlope.getPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D)));
				break;
			case 3:
				renderPkg.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D)));
				renderPkg.add(RenderHelperSlope.getPrismSlopedZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismSlopedZPosXPos());
				renderPkg.add(RenderHelperSlope.getPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getPrismXNegZNeg());
				renderPkg.add(RenderHelperSlope.getPrismXNegZPos());
				renderPkg.add(RenderHelperSlope.getPrismSlopedXNegZPos());
				renderPkg.add(RenderHelperSlope.getPrismXPosZPos());
				renderPkg.add(RenderHelperSlope.getPrismXPosZNeg());
				renderPkg.add(RenderHelperSlope.getPrismSlopedXPosZPos());
				break;
			case 4:
				renderPkg.add(RenderHelperSlope.getPrismZNegXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZNegXPos());
				renderPkg.add(RenderHelperSlope.getPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getPrismXNegZNeg());
				renderPkg.add(RenderHelperSlope.getPrismXNegZPos());
				renderPkg.add(RenderHelperSlope.getPrismXPosZPos());
				renderPkg.add(RenderHelperSlope.getPrismXPosZNeg());
				// Sloped faces
				renderPkg.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D)));
				renderPkg.add(RenderHelperSlope.getPrismSlopedZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismSlopedZPosXPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(RenderHelperSlope.getPrismSlopedXNegZPos());
				renderPkg.add(RenderHelperSlope.getPrismSlopedXPosZPos());
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D)));
				break;
		}
	}
	
	protected void fillInvertedPrism(RenderPkg renderPkg, int numPoints) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		switch (numPoints) {
			case 0:
				renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadZPos(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(1.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 1.0D, 1.0D)));
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopedZPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 1.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 1.0D, 0.0D)));
				break;
			case 1:
				renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopedZPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 1.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 1.0D, 0.0D)));
				break;
			case 2:
				renderPkg.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZNegXNeg());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 1.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.0D, 1.0D, 0.0D)));
				break;
			case 3:
				renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXNegZNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXPosZPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXPosZNeg());
				// Sloped faces
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 1.0D, 0.0D)));
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeZNegXNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeXPosZPos());
				break;
			case 4:
				renderPkg.add(RenderHelperSlope.getInvertedPrismZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZNegXNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXNegZNeg());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXPosZPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismXPosZNeg());
				// Sloped faces
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeZNegXNeg());
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 1.0D, 0.0D)));
				renderPkg.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertedPrismSlopeXPosZPos());
				renderPkg.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.0D, 1.0D, 0.0D)));
				break;
		}
	}
	
}
