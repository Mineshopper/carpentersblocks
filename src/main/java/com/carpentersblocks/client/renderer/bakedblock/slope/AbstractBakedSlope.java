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
import com.carpentersblocks.client.renderer.ReferenceQuads;
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
	protected void fillQuads(ReferenceQuads referenceQuads) {
		int cbMetadata = RenderPkg.get().getCbMetadata();
		switch (SlopeData.getType(cbMetadata)) {
			case WEDGE:
				fillWedge(referenceQuads);
				break;
			case WEDGE_INTERIOR:
				fillWedgeInterior(referenceQuads);
				break;
			case WEDGE_EXTERIOR:
				fillWedgeExterior(referenceQuads);
				break;
			case OBLIQUE_INTERIOR:
				fillObliqueInterior(referenceQuads);
				break;
			case OBLIQUE_EXTERIOR:
				fillObliqueExterior(referenceQuads);
				break;
			case PRISM_WEDGE:
				fillPrismWedge(referenceQuads);
				break;
			case PRISM:
				fillPrism(referenceQuads, 0);
				break;
			case PRISM_1P:
				fillPrism(referenceQuads, 1);
				break;
			case PRISM_2P:
				fillPrism(referenceQuads, 2);
				break;
			case PRISM_3P:
				fillPrism(referenceQuads, 3);
				break;
			case PRISM_4P:
				fillPrism(referenceQuads, 4);
				break;
			case INVERTED_PRISM:
				fillInvertedPrism(referenceQuads, 0);
				break;
			case INVERTED_PRISM_1P:
				fillInvertedPrism(referenceQuads, 1);
				break;
			case INVERTED_PRISM_2P:
				fillInvertedPrism(referenceQuads, 2);
				break;
			case INVERTED_PRISM_3P:
				fillInvertedPrism(referenceQuads, 3);
				break;
			case INVERTED_PRISM_4P:
				fillInvertedPrism(referenceQuads, 4);
				break;
		}
		CbRotation rotation = CbRotation.get(cbMetadata);
		referenceQuads.rotate(rotation);
	}
	
	protected void fillWedge(ReferenceQuads referenceQuads) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelperSlope.getWedgeXNeg());
		referenceQuads.add(RenderHelperSlope.getWedgeXPos());
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D)));
	}
	
	protected void fillWedgeExterior(ReferenceQuads referenceQuads) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelperSlope.getWedgeXNeg());
		referenceQuads.add(RenderHelperSlope.getWedgeExteriorZNeg());
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D)));
		referenceQuads.add(
			Quad.getQuad(
				EAST,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 0.0D),
				new Vector3d(0.0D, 1.0D, 0.0D)));
	}

	protected void fillWedgeInterior(ReferenceQuads referenceQuads) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelperSlope.getWedgeXPos());
		referenceQuads.add(RenderHelperSlope.getWedgeInteriorZPos());
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
		referenceQuads.add(
			Quad.getQuad(
				EAST,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(0.0D, 1.0D, 0.0D)));
	}
	
	protected void fillObliqueInterior(ReferenceQuads referenceQuads) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelperSlope.getWedgeInteriorZPos());
		referenceQuads.add(RenderHelperSlope.getWedgeXPos());
		referenceQuads.add(
			Quad.getQuad(
				UP,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 1.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
		// Left oblique slope part
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_oblique_pos,
				new Vector3d(0.0D, 1.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 1.0D, 0.5D)));
		// Right oblique slope part
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_oblique_pos,
				new Vector3d(0.5D, 1.0D, 0.5D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
	}
	
	protected void fillObliqueExterior(ReferenceQuads referenceQuads) {
		referenceQuads.add(
			Quad.getQuad(
				DOWN,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.0D, 0.0D, 0.0D),
				new Vector3d(1.0D, 0.0D, 0.0D)));
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_oblique_neg,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 0.0D)));
		referenceQuads.add(RenderHelperSlope.getWedgeExteriorZNeg());
		referenceQuads.add(
			Quad.getQuad(
				WEST,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D)));
	}
	
	protected void fillPrismWedge(ReferenceQuads referenceQuads) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
		referenceQuads.add(RenderHelperSlope.getWedgeXNeg());
		referenceQuads.add(RenderHelperSlope.getWedgeXPos());
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.0D, 1.0D, 0.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.5D, 0.5D),
				new Vector3d(0.5D, 1.0D, 0.0D)));
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.5D, 1.0D, 0.0D),
				new Vector3d(0.5D, 0.5D, 0.5D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(1.0D, 1.0D, 0.0D)));
		referenceQuads.add(
			Quad.getQuad(
				WEST,
				TextureAtlasSprites.sprite_uncovered_quartered,
				new Vector3d(0.5D, 0.5D, 0.5D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.5D, 1.0D)));
		referenceQuads.add(
			Quad.getQuad(
				EAST,
				TextureAtlasSprites.sprite_uncovered_quartered,
				new Vector3d(0.5D, 0.5D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.5D, 0.5D)));
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.5D, 0.5D, 1.0D),
				new Vector3d(0.0D, 0.0D, 1.0D),
				new Vector3d(0.5D, 0.0D, 1.0D)));
		referenceQuads.add(
			Quad.getQuad(
				SOUTH,
				TextureAtlasSprites.sprite_uncovered_full,
				new Vector3d(0.5D, 0.5D, 1.0D),
				new Vector3d(0.5D, 0.0D, 1.0D),
				new Vector3d(1.0D, 0.0D, 1.0D)));
	}
	
	protected void fillPrism(ReferenceQuads referenceQuads, int numPoints) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		switch (numPoints) {
			case 0:
				referenceQuads.add(RenderHelperSlope.getPrismSlopeZNeg());
				referenceQuads.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 1.0D)));
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 1.0D)));
				referenceQuads.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 0.0D)));
				break;
			case 1:
				referenceQuads.add(RenderHelperSlope.getPrismSlopeZNeg());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXPos());
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D)));
				referenceQuads.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				break;
			case 2:
				referenceQuads.add(RenderHelperSlope.getPrismZNegXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismZNegXPos());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXPos());
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D)));
				referenceQuads.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 0.0D, 1.0D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D)));
				break;
			case 3:
				referenceQuads.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D)));
				referenceQuads.add(RenderHelperSlope.getPrismSlopedZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismSlopedZPosXPos());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXPos());
				referenceQuads.add(RenderHelperSlope.getPrismXNegZNeg());
				referenceQuads.add(RenderHelperSlope.getPrismXNegZPos());
				referenceQuads.add(RenderHelperSlope.getPrismSlopedXNegZPos());
				referenceQuads.add(RenderHelperSlope.getPrismXPosZPos());
				referenceQuads.add(RenderHelperSlope.getPrismXPosZNeg());
				referenceQuads.add(RenderHelperSlope.getPrismSlopedXPosZPos());
				break;
			case 4:
				referenceQuads.add(RenderHelperSlope.getPrismZNegXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismZNegXPos());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismZPosXPos());
				referenceQuads.add(RenderHelperSlope.getPrismXNegZNeg());
				referenceQuads.add(RenderHelperSlope.getPrismXNegZPos());
				referenceQuads.add(RenderHelperSlope.getPrismXPosZPos());
				referenceQuads.add(RenderHelperSlope.getPrismXPosZNeg());
				// Sloped faces
				referenceQuads.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				referenceQuads.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D)));
				referenceQuads.add(RenderHelperSlope.getPrismSlopedZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getPrismSlopedZPosXPos());
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				referenceQuads.add(RenderHelperSlope.getPrismSlopedXNegZPos());
				referenceQuads.add(RenderHelperSlope.getPrismSlopedXPosZPos());
				referenceQuads.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D)));
				break;
		}
	}
	
	protected void fillInvertedPrism(ReferenceQuads referenceQuads, int numPoints) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_full));
		switch (numPoints) {
			case 0:
				referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelper.getQuadZPos(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(
					Quad.getQuad(
						NORTH,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(1.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 1.0D, 1.0D)));
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopedZPos());
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 1.0D, 1.0D)));
				referenceQuads.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_full,
						new Vector3d(0.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 1.0D, 0.0D)));
				break;
			case 1:
				referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopedZPos());
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 1.0D, 1.0D)));
				referenceQuads.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(0.0D, 1.0D, 0.0D)));
				break;
			case 2:
				referenceQuads.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZNegXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZNegXNeg());
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(1.0D, 1.0D, 1.0D)));
				referenceQuads.add(
					Quad.getQuad(
						EAST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 1.0D),
						new Vector3d(0.5D, 0.5D, 1.0D),
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.0D, 1.0D, 0.0D)));
				break;
			case 3:
				referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_full));
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXNegZNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXNegZPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXPosZPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXPosZNeg());
				// Sloped faces
				referenceQuads.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 1.0D, 0.0D)));
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeZNegXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeZNegXNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeXNegZPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeXPosZPos());
				break;
			case 4:
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZNegXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZNegXNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismZPosXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXNegZNeg());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXNegZPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXPosZPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismXPosZNeg());
				// Sloped faces
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeZNegXPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeZNegXNeg());
				referenceQuads.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.0D, 1.0D, 0.0D),
						new Vector3d(0.0D, 0.5D, 0.5D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				referenceQuads.add(
					Quad.getQuad(
						SOUTH,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(0.5D, 0.5D, 0.5D),
						new Vector3d(1.0D, 0.5D, 0.5D),
						new Vector3d(1.0D, 1.0D, 0.0D)));
				referenceQuads.add(
					Quad.getQuad(
						WEST,
						TextureAtlasSprites.sprite_uncovered_quartered,
						new Vector3d(1.0D, 1.0D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.0D),
						new Vector3d(0.5D, 0.5D, 0.5D)));
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeXNegZPos());
				referenceQuads.add(RenderHelperSlope.getInvertedPrismSlopeXPosZPos());
				referenceQuads.add(
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
