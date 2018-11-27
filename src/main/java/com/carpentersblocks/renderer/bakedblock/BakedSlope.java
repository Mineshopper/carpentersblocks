package com.carpentersblocks.renderer.bakedblock;

import static net.minecraft.util.EnumFacing.DOWN;
import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.UP;
import static net.minecraft.util.EnumFacing.WEST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.renderer.RenderPkg;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.renderer.helper.RenderHelperSlope;
import com.carpentersblocks.util.RotationUtil.Rotation;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedSlope extends AbstractBakedModel {

	private String _type;
	
	private static Map<String, List<BakedQuad>> _inventoryBakedQuads = new HashMap<String, List<BakedQuad>>();
	
	public BakedSlope(IModelState modelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, String type) {
		super(modelState, vertexFormat, bakedTextureGetter);
		_type = type;
	}
	
	@Override
	public List<BakedQuad> getInventoryQuads(RenderPkg renderPkg) {
		if (!_inventoryBakedQuads.containsKey(_type)) {
			// Need to rotate some of these
			/*
			 * 		Rotation rotation = Rotation.get(renderPkg.getData());
		renderPkg.rotate(rotation);
			 */
			switch (_type) {
				case "wedge":
					fillWedge(renderPkg);
					break;
				case "wedge_interior":
					fillWedgeInterior(renderPkg);
					break;
				case "wedge_exterior":
					fillWedgeExterior(renderPkg);
					break;
				case "oblique_interior":
					fillObliqueInterior(renderPkg);
					break;
				case "oblique_exterior":
					fillObliqueExterior(renderPkg);
					break;
				case "prism_wedge":
					fillPrismWedge(renderPkg);
					break;
				case "prism":
					fillPrism(renderPkg, 0);
					break;
				case "invert_prism":
					fillInvertPrism(renderPkg, 0);
					break;
			}
			renderPkg.rotate(Rotation.X0_Y90_Z0);
			_inventoryBakedQuads.put(_type, renderPkg.getInventoryQuads());
		}
		return _inventoryBakedQuads.get(_type);
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		switch (SlopeData.getType(renderPkg.getData())) {
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
			case INVERT_PRISM:
				fillInvertPrism(renderPkg, 0);
				break;
			case INVERT_PRISM_1P:
				fillInvertPrism(renderPkg, 1);
				break;
			case INVERT_PRISM_2P:
				fillInvertPrism(renderPkg, 2);
				break;
			case INVERT_PRISM_3P:
				fillInvertPrism(renderPkg, 3);
				break;
			case INVERT_PRISM_4P:
				fillInvertPrism(renderPkg, 4);
				break;
		}
		Rotation rotation = Rotation.get(renderPkg.getData());
		renderPkg.rotate(rotation);
	}
	
	private void fillWedge(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXNeg());
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(1.0D, 1.0D, 0.0D),
				new Vec3d(0.0D, 1.0D, 0.0D),
				new Vec3d(0.0D, 0.0D, 1.0D)));
	}
	
	private void fillWedgeExterior(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXNeg());
		renderPkg.add(RenderHelperSlope.getWedgeExteriorZNeg());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.0D, 1.0D, 0.0D),
				new Vec3d(0.0D, 0.0D, 1.0D),
				new Vec3d(1.0D, 0.0D, 1.0D)));
		renderPkg.add(
			Quad.getQuad(
				EAST,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(1.0D, 0.0D, 0.0D),
				new Vec3d(0.0D, 1.0D, 0.0D)));
	}

	private void fillWedgeInterior(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(RenderHelperSlope.getWedgeInteriorZPos());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.0D, 1.0D, 0.0D),
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(1.0D, 1.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				EAST,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.0D, 1.0D, 1.0D),
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(0.0D, 1.0D, 0.0D)));
	}
	
	private void fillObliqueInterior(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeInteriorZPos());
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(
			Quad.getQuad(
				UP,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.0D, 1.0D, 0.0D),
				new Vec3d(0.0D, 1.0D, 1.0D),
				new Vec3d(1.0D, 1.0D, 0.0D)));
		// Right oblique slope part
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_oblique_pos,
				new Vec3d(0.0D, 1.0D, 1.0D),
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(0.5D, 1.0D, 0.5D)));
		// Left oblique slope part
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_oblique_pos,
				new Vec3d(0.5D, 1.0D, 0.5D),
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(1.0D, 1.0D, 0.0D)));
	}
	
	private void fillObliqueExterior(RenderPkg renderPkg) {
		renderPkg.add(
			Quad.getQuad(
				DOWN,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.0D, 0.0D, 1.0D),
				new Vec3d(0.0D, 0.0D, 0.0D),
				new Vec3d(1.0D, 0.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_oblique_neg,
				new Vec3d(0.0D, 1.0D, 0.0D),
				new Vec3d(0.0D, 0.0D, 1.0D),
				new Vec3d(1.0D, 0.0D, 0.0D)));
		renderPkg.add(RenderHelperSlope.getWedgeExteriorZNeg());
		renderPkg.add(
			Quad.getQuad(
				WEST,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.0D, 1.0D, 0.0D),
				new Vec3d(0.0D, 0.0D, 0.0D),
				new Vec3d(0.0D, 0.0D, 1.0D)));
	}
	
	private void fillPrismWedge(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
		renderPkg.add(RenderHelperSlope.getWedgeXNeg());
		renderPkg.add(RenderHelperSlope.getWedgeXPos());
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.0D, 1.0D, 0.0D),
				new Vec3d(0.0D, 0.0D, 1.0D),
				new Vec3d(0.5D, 0.5D, 0.5D),
				new Vec3d(0.5D, 1.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.5D, 1.0D, 0.0D),
				new Vec3d(0.5D, 0.5D, 0.5D),
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(1.0D, 1.0D, 0.0D)));
		renderPkg.add(
			Quad.getQuad(
				WEST,
				SpriteRegistry.sprite_uncovered_quartered,
				new Vec3d(0.5D, 0.5D, 0.5D),
				new Vec3d(0.0D, 0.0D, 1.0D),
				new Vec3d(0.5D, 0.5D, 1.0D)));
		renderPkg.add(
			Quad.getQuad(
				EAST,
				SpriteRegistry.sprite_uncovered_quartered,
				new Vec3d(0.5D, 0.5D, 1.0D),
				new Vec3d(1.0D, 0.0D, 1.0D),
				new Vec3d(0.5D, 0.5D, 0.5D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.5D, 0.5D, 1.0D),
				new Vec3d(0.0D, 0.0D, 1.0D),
				new Vec3d(0.5D, 0.0D, 1.0D)));
		renderPkg.add(
			Quad.getQuad(
				SOUTH,
				SpriteRegistry.sprite_uncovered_full,
				new Vec3d(0.5D, 0.5D, 1.0D),
				new Vec3d(0.5D, 0.0D, 1.0D),
				new Vec3d(1.0D, 0.0D, 1.0D)));
	}
	
	private void fillPrism(RenderPkg renderPkg, int numPoints) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		switch (numPoints) {
			case 0:
				renderPkg.add(RenderHelperSlope.getPrismSlopeZNeg());
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						SpriteRegistry.sprite_uncovered_full,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.0D, 0.0D, 1.0D),
						new Vec3d(1.0D, 0.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_full,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.0D, 0.0D, 0.0D),
						new Vec3d(0.0D, 0.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_full,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(1.0D, 0.0D, 1.0D),
						new Vec3d(1.0D, 0.0D, 0.0D)));
				break;
			case 1:
				renderPkg.add(RenderHelperSlope.getPrismSlopeZNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.0D, 0.0D, 0.0D),
						new Vec3d(0.0D, 0.0D, 1.0D),
						new Vec3d(0.5D, 0.5D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 1.0D),
						new Vec3d(1.0D, 0.0D, 1.0D),
						new Vec3d(1.0D, 0.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.5D)));
				break;
			case 2:
				renderPkg.add(RenderHelperSlope.getPrismZNegXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZNegXPos());
				renderPkg.add(RenderHelperSlope.getPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismZPosXPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 0.0D),
						new Vec3d(0.0D, 0.0D, 0.0D),
						new Vec3d(0.0D, 0.0D, 1.0D),
						new Vec3d(0.5D, 0.5D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 1.0D),
						new Vec3d(1.0D, 0.0D, 1.0D),
						new Vec3d(1.0D, 0.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.0D)));
				break;
			case 3:
				renderPkg.add(
					Quad.getQuad(
						NORTH,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(1.0D, 0.5D, 0.5D),
						new Vec3d(1.0D, 0.0D, 0.0D),
						new Vec3d(0.0D, 0.0D, 0.0D),
						new Vec3d(0.0D, 0.5D, 0.5D)));
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
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(1.0D, 0.5D, 0.5D),
						new Vec3d(1.0D, 0.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(
					Quad.getQuad(
						NORTH,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.0D, 0.0D, 0.0D),
						new Vec3d(0.0D, 0.5D, 0.5D)));
				renderPkg.add(RenderHelperSlope.getPrismSlopedZPosXNeg());
				renderPkg.add(RenderHelperSlope.getPrismSlopedZPosXPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 0.0D),
						new Vec3d(0.0D, 0.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(RenderHelperSlope.getPrismSlopedXNegZPos());
				renderPkg.add(RenderHelperSlope.getPrismSlopedXPosZPos());
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(1.0D, 0.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.0D)));
				break;
		}
	}
	
	private void fillInvertPrism(RenderPkg renderPkg, int numPoints) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		switch (numPoints) {
			case 0:
				renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadZPos(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXPos(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(
					Quad.getQuad(
						NORTH,
						SpriteRegistry.sprite_uncovered_full,
						new Vec3d(1.0D, 1.0D, 1.0D),
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.0D, 1.0D, 1.0D)));
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopedZPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_full,
						new Vec3d(1.0D, 1.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(1.0D, 1.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_full,
						new Vec3d(0.0D, 1.0D, 1.0D),
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.0D, 1.0D, 0.0D)));
				break;
			case 1:
				renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXPos(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopedZPos());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(1.0D, 1.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.5D, 0.5D, 1.0D),
						new Vec3d(1.0D, 1.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.0D, 1.0D, 1.0D),
						new Vec3d(0.5D, 0.5D, 1.0D),
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.0D, 1.0D, 0.0D)));
				break;
			case 2:
				renderPkg.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelper.getQuadXPos(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismZNegXNeg());
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(1.0D, 1.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.0D),
						new Vec3d(0.5D, 0.5D, 1.0D),
						new Vec3d(1.0D, 1.0D, 1.0D)));
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.0D, 1.0D, 1.0D),
						new Vec3d(0.5D, 0.5D, 1.0D),
						new Vec3d(0.5D, 0.5D, 0.0D),
						new Vec3d(0.0D, 1.0D, 0.0D)));
				break;
			case 3:
				renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismXNegZNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismXPosZPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismXPosZNeg());
				// Sloped faces
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.0D, 1.0D, 0.0D),
						new Vec3d(0.0D, 0.5D, 0.5D),
						new Vec3d(1.0D, 0.5D, 0.5D),
						new Vec3d(1.0D, 1.0D, 0.0D)));
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeZNegXNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeXPosZPos());
				break;
			case 4:
				renderPkg.add(RenderHelperSlope.getInvertPrismZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismZNegXNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismZPosXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismXNegZNeg());
				renderPkg.add(RenderHelperSlope.getInvertPrismXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismXPosZPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismXPosZNeg());
				// Sloped faces
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeZNegXPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeZNegXNeg());
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.0D, 1.0D, 0.0D),
						new Vec3d(0.0D, 0.5D, 0.5D),
						new Vec3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(
					Quad.getQuad(
						SOUTH,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(1.0D, 0.5D, 0.5D),
						new Vec3d(1.0D, 1.0D, 0.0D)));
				renderPkg.add(
					Quad.getQuad(
						WEST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(1.0D, 1.0D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.0D),
						new Vec3d(0.5D, 0.5D, 0.5D)));
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeXNegZPos());
				renderPkg.add(RenderHelperSlope.getInvertPrismSlopeXPosZPos());
				renderPkg.add(
					Quad.getQuad(
						EAST,
						SpriteRegistry.sprite_uncovered_quartered,
						new Vec3d(0.5D, 0.5D, 0.5D),
						new Vec3d(0.5D, 0.5D, 0.0D),
						new Vec3d(0.0D, 1.0D, 0.0D)));
				break;
		}
	}
	
}