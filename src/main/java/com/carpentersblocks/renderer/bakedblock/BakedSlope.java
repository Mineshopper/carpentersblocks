package com.carpentersblocks.renderer.bakedblock;

import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.WEST;

import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.block.data.SlopeData.Type;
import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.renderer.RenderPkg;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.util.RotationUtil.Rotation;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedSlope extends AbstractBakedModel {

    public BakedSlope(IModelState modelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(modelState, vertexFormat, bakedTextureGetter);
	}
    
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		Type type = SlopeData.getType(renderPkg.getData());
		switch (type) {
			case WEDGE:
				fillWedgeQuads(renderPkg);
				break;
			case OBLIQUE_INTERIOR:
				fillObliqueInteriorQuads(renderPkg);
				break;
			case OBLIQUE_EXTERIOR:
				fillObliqueExteriorQuads(renderPkg);
				break;
		}
    	Rotation rotation = Rotation.get(renderPkg.getData());
    	renderPkg.rotate(rotation);
	}
	
	private void fillWedgeQuads(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
    	renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
    	renderPkg.add(
    		Quad.getQuad(
    			WEST,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(0.0D, 1.0D, 0.0D),
    			new Vec3d(0.0D, 0.0D, 0.0D),
    			new Vec3d(0.0D, 0.0D, 1.0D)));
    	renderPkg.add(
    		Quad.getQuad(
    			EAST,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(1.0D, 0.0D, 1.0D),
    			new Vec3d(1.0D, 0.0D, 0.0D),
    			new Vec3d(1.0D, 1.0D, 0.0D)));
    	renderPkg.add(
    		Quad.getQuad(
                EnumFacing.UP,
                SpriteRegistry.sprite_uncovered_full,
                new Vec3d(1.0D, 0.0D, 1.0D),
                new Vec3d(1.0D, 1.0D, 0.0D),
                new Vec3d(0.0D, 1.0D, 0.0D),
                new Vec3d(0.0D, 0.0D, 1.0D)));
	}
	
	private void fillObliqueInteriorQuads(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
    	renderPkg.add(RenderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
    	renderPkg.add(RenderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
    	renderPkg.add(
    		Quad.getQuad(
    			SOUTH,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(0.0D, 1.0D, 1.0D),
    			new Vec3d(0.0D, 0.0D, 1.0D),
    			new Vec3d(1.0D, 0.0D, 1.0D)));
    	renderPkg.add(
    		Quad.getQuad(
    			EAST,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(1.0D, 0.0D, 1.0D),
    			new Vec3d(1.0D, 0.0D, 0.0D),
    			new Vec3d(1.0D, 1.0D, 0.0D)));
    	renderPkg.add(
    		Quad.getQuad(
                EnumFacing.UP,
                SpriteRegistry.sprite_uncovered_full,
                new Vec3d(0.0D, 1.0D, 0.0D),
                new Vec3d(0.0D, 1.0D, 1.0D),
                new Vec3d(1.0D, 1.0D, 0.0D)));
    	renderPkg.add(
    		Quad.getQuad(
                EnumFacing.UP,
                SpriteRegistry.sprite_uncovered_oblique_pos,
                new Vec3d(0.0D, 1.0D, 1.0D),
                new Vec3d(1.0D, 0.0D, 1.0D),
                new Vec3d(1.0D, 1.0D, 0.0D)));
	}
	
	private void fillObliqueExteriorQuads(RenderPkg renderPkg) {
		// TODO: Split into two halves for proper UV mapping
		renderPkg.add(
    		Quad.getQuad(
    			EnumFacing.DOWN,
    			SpriteRegistry.sprite_uncovered_full,
    			new Vec3d(0.0D, 0.0D, 1.0D),
    			new Vec3d(0.0D, 0.0D, 0.0D),
    			new Vec3d(1.0D, 0.0D, 0.0D)));
		renderPkg.add(
    		Quad.getQuad(
    			EnumFacing.UP,
    			SpriteRegistry.sprite_uncovered_oblique_neg,
    			new Vec3d(0.0D, 1.0D, 0.0D),
    			new Vec3d(0.0D, 0.0D, 1.0D),
    			new Vec3d(1.0D, 0.0D, 0.0D)));
		renderPkg.add(
    		Quad.getQuad(
                EnumFacing.NORTH,
                SpriteRegistry.sprite_uncovered_full,
                new Vec3d(1.0D, 0.0D, 0.0D),
                new Vec3d(0.0D, 0.0D, 0.0D),
                new Vec3d(0.0D, 1.0D, 0.0D)));
		renderPkg.add(
    		Quad.getQuad(
                EnumFacing.WEST,
                SpriteRegistry.sprite_uncovered_full,
                new Vec3d(0.0D, 1.0D, 0.0D),
                new Vec3d(0.0D, 0.0D, 0.0D),
                new Vec3d(0.0D, 0.0D, 1.0D)));
	}
	
}
