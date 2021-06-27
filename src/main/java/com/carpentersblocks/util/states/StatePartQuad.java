package com.carpentersblocks.util.states;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.util.QuadUtil;
import com.carpentersblocks.util.RotationUtil;
import com.carpentersblocks.util.RotationUtil.CbRotation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StatePartQuad {
	
	private Vector3d[] _vec3ds;
	private RenderType _renderLayer;
	private boolean _maxBrightness;
	private boolean _diffuseLighting;
	private String _iconName;
	private int _rgb;
	private Direction _facing;
	
	/** Used when calling {@link #toQuad(List)}, otherwise reversed quad not useful */
	private boolean _drawReverse;
	
	/**
	 * Class constructor.
	 * 
	 * @param dto the domain transfer object
	 */
	public StatePartQuad(
			Vector3d vertexMin,
			Vector3d vertexMax,
			Vector3d rotationDeg,
			RenderType renderLayer,
			boolean maxBrightness,
			boolean diffuseLighting,
			String iconName,
			int rgb,
			boolean drawReverse,
			Direction facing) {
		this._vec3ds = this.genVector3dIntermediates(facing, vertexMin, vertexMax);
		this._facing = facing;
		// No rotation should default to Vector3d.ZERO when loading states
		if (!rotationDeg.equals(Vector3d.ZERO)) {
			this._vec3ds = RotationUtil.rotate(this._vec3ds, rotationDeg);
			Vector3d normal = QuadUtil.getNormal(this._vec3ds);
			this._facing = Direction.getNearest(normal.x, normal.y, normal.z);
		}
		this._renderLayer = renderLayer;
		this._maxBrightness = maxBrightness;
		this._diffuseLighting = diffuseLighting;
		this._iconName = iconName;
		this._rgb = rgb;
		this._drawReverse = drawReverse;
		this._facing = facing;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param src the source state part quad
	 */
	StatePartQuad(StatePartQuad src) {
		this._vec3ds = new Vector3d[] {
			src.getVector3ds()[0].add(Vector3d.ZERO),
			src.getVector3ds()[1].add(Vector3d.ZERO),
			src.getVector3ds()[2].add(Vector3d.ZERO),
			src.getVector3ds()[3].add(Vector3d.ZERO)
		};
		this._vec3ds = Arrays.copyOf(src.getVector3ds(), src.getVector3ds().length);
		this._renderLayer = src.getRenderLayer();
		this._maxBrightness = src.isMaxBrightness();
		this._diffuseLighting = src.isDiffuseLighting();
		this._iconName = src.getIconName();
		this._rgb = src.getRgb();
		this._drawReverse = src.isDrawReverse();
		this._facing = src.getFacing();
	}
	
	public Vector3d[] getVector3ds() {
		return _vec3ds;
	}

	public RenderType getRenderLayer() {
		return _renderLayer;
	}

	public boolean isMaxBrightness() {
		return _maxBrightness;
	}

	public boolean isDiffuseLighting() {
		return _diffuseLighting;
	}

	public String getIconName() {
		return _iconName;
	}

	public int getRgb() {
		return _rgb;
	}
	
	public Direction getFacing() {
		return _facing;
	}

	public boolean isDrawReverse() {
		return _drawReverse;
	}
	
	/**
	 * Generates intermediate points from minimum and maximum
	 * vertex points.
	 * 
	 * @param facing the quad facing
	 * @return an array of {@link Vector3d} points representing four points of a {@link Quad}
	 */
	private Vector3d[] genVector3dIntermediates(Direction facing, Vector3d vertexMin, Vector3d vertexMax) {
		switch (facing) {
			case DOWN:
				//-x,-y,+z
				//-x,-y,-z
				//+x,-y,-z
				//+x,-y,+z
				return new Vector3d[] {
					new Vector3d(vertexMin.x, vertexMin.y, vertexMax.z),
					new Vector3d(vertexMin.x, vertexMin.y, vertexMin.z),
					new Vector3d(vertexMax.x, vertexMin.y, vertexMin.z),
					new Vector3d(vertexMax.x, vertexMin.y, vertexMax.z)
				};
			case UP:
				//-x,+y,-z
				//-x,+y,+z
				//+x,+y,+z
				//+x,+y,-z
				return new Vector3d[] {
					new Vector3d(vertexMin.x, vertexMax.y, vertexMin.z),
					new Vector3d(vertexMin.x, vertexMax.y, vertexMax.z),
					new Vector3d(vertexMax.x, vertexMax.y, vertexMax.z),
					new Vector3d(vertexMax.x, vertexMax.y, vertexMin.z)
				};
			case NORTH:
				//+x,+y,-z
				//+x,-y,-z
				//-x,-y,-z
				//-x,+y,-z
				return new Vector3d[] {
					new Vector3d(vertexMax.x, vertexMax.y, vertexMin.z),
					new Vector3d(vertexMax.x, vertexMin.y, vertexMin.z),
					new Vector3d(vertexMin.x, vertexMin.y, vertexMin.z),
					new Vector3d(vertexMin.x, vertexMax.y, vertexMin.z)
				};
			case SOUTH:
				//-x,+y,+z
				//-x,-y,+z
				//+x,-y,+z
				//+x,+y,+z
				return new Vector3d[] {
					new Vector3d(vertexMin.x, vertexMax.y, vertexMax.z),
					new Vector3d(vertexMin.x, vertexMin.y, vertexMax.z),
					new Vector3d(vertexMax.x, vertexMin.y, vertexMax.z),
					new Vector3d(vertexMax.x, vertexMax.y, vertexMax.z)
				};
			case WEST:
				//-x,+y,-z
				//-x,-y,-z
				//-x,-y,+z
				//-x,+y,+z
				return new Vector3d[] {
					new Vector3d(vertexMin.x, vertexMax.y, vertexMin.z),
					new Vector3d(vertexMin.x, vertexMin.y, vertexMin.z),
					new Vector3d(vertexMin.x, vertexMin.y, vertexMax.z),
					new Vector3d(vertexMin.x, vertexMax.y, vertexMax.z)
				};
			default: // case EAST:
				//+x,+y,+z
				//+x,-y,+z
				//+x,-y,-z
				//+x,+y,-z
				return new Vector3d[] {
					new Vector3d(vertexMax.x, vertexMax.y, vertexMax.z),
					new Vector3d(vertexMax.x, vertexMin.y, vertexMax.z),
					new Vector3d(vertexMax.x, vertexMin.y, vertexMin.z),
					new Vector3d(vertexMax.x, vertexMax.y, vertexMin.z)
				};
		}
	}
	
	/**
	 * Adds {@link AxisAlignedBB} to list for collision and ray tracing.
	 */
	public void toAxisAlignedBB(List<AxisAlignedBB> list) {
		list.add(
			new AxisAlignedBB(
				Arrays.stream(_vec3ds).min(Comparator.comparing(v->v.x)).get().x,
				Arrays.stream(_vec3ds).min(Comparator.comparing(v->v.y)).get().y,
				Arrays.stream(_vec3ds).min(Comparator.comparing(v->v.z)).get().z,
				Arrays.stream(_vec3ds).max(Comparator.comparing(v->v.x)).get().x,
				Arrays.stream(_vec3ds).max(Comparator.comparing(v->v.y)).get().y,
				Arrays.stream(_vec3ds).max(Comparator.comparing(v->v.z)).get().z));
	}
	
	/**
	 * Adds {@link Quad} to list for rendering, and the
	 * reverse quad if {@link #_drawReverse} is <code>true</code>.
	 * <p>
	 * Client-side only method.
	 */
	@OnlyIn(Dist.CLIENT)
	public void toQuad(List<Quad> quads) {
		ResourceLocation resourceLocation = new ResourceLocation(this._iconName);
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(resourceLocation);
		quads.add(Quad.getQuad(
			this._facing,
			false,
			sprite,
			this._rgb,
			this._maxBrightness,
			this._diffuseLighting,
			this._renderLayer,
			this._vec3ds));
		if (this._drawReverse) {
			Vector3d[] invertedVector3ds = (Vector3d[])
				IntStream.rangeClosed(1, this._vec3ds.length)
				.mapToObj(i -> this._vec3ds[this._vec3ds.length - i])
				.toArray();
			quads.add(Quad.getQuad(
				this._facing.getOpposite(),
				false,
				sprite,
				this._rgb,
				this._maxBrightness,
				this._diffuseLighting,
				this._renderLayer,
				invertedVector3ds));
		}
	}
	
	/**
	 * Rotates state part quad.
	 * 
	 * @param rotation the rotation
	 */
	public void rotate(CbRotation rotation) {
		this._facing = rotation.getRotatedDirection(this._facing);
		this._vec3ds = RotationUtil.rotate(this._vec3ds, rotation);
	}
	
}