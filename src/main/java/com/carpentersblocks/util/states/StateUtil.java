package com.carpentersblocks.util.states;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.carpentersblocks.renderer.Quad;
import com.carpentersblocks.util.states.factory.AbstractState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.Vec3d;

public class StateUtil {

	public static void calcCanSeeSky(AbstractState state) {
		List<StatePart> stateParts = state.getStateParts();
		
		// Sort on y-axis from high to low
		Collections.sort(stateParts, new Comparator<StatePart>() {
			@Override
			public int compare(StatePart part1, StatePart part2) {
				if (part1.getVertexMax().y > part2.getVertexMax().y) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		
		// Iterate parts to see which ones can see sky
		List<StatePart> _canSeeSky = new ArrayList<StatePart>();
		for (StatePart statePart : stateParts) {
			if (_canSeeSky.isEmpty()) {
				_canSeeSky.add(statePart);
				continue;
			} else {
				boolean visible = false;
				for (StatePart highPart : _canSeeSky) {
					if (statePart.getVertexMax().x > highPart.getVertexMax().x
							|| statePart.getVertexMax().z > highPart.getVertexMax().z
							|| statePart.getVertexMin().x < highPart.getVertexMin().x
							|| statePart.getVertexMin().z < highPart.getVertexMin().z) {
						_canSeeSky.add(statePart);
						break;
					}
				}
			}
		}
		
		// Save sky visibility to parts
		for (StatePart statePart : stateParts) {
			statePart.setCanSeeSky(_canSeeSky.contains(statePart));
		}
	}
	
	public static List<Quad> getQuads(StatePart statePart) {
		List<Quad> list = new ArrayList<Quad>();
		Vec3d min = statePart.getVertexMin();
		Vec3d max = statePart.getVertexMax();
		for (EnumFacing enumFacing : statePart.getRenderFaces()) {
			switch (enumFacing) {
				case DOWN:
					//-x,-y,+z
					//-x,-y,-z
					//+x,-y,-z
					//+x,-y,+z
					list.add(Quad.getQuad(
						enumFacing,
						new Vec3d(min.x, min.y, max.z),
						new Vec3d(min.x, min.y, min.z),
						new Vec3d(max.x, min.y, min.z),
						new Vec3d(max.x, min.y, max.z))
					);
					break;
				case UP:
					//-x,+y,-z
					//-x,+y,+z
					//+x,+y,+z
					//+x,+y,-z
					list.add(Quad.getQuad(
						enumFacing,
						new Vec3d(min.x, max.y, min.z),
						new Vec3d(min.x, max.y, max.z),
						new Vec3d(max.x, max.y, max.z),
						new Vec3d(max.x, max.y, min.z))
					);
					break;
				case NORTH:
					//+x,+y,-z
					//+x,-y,-z
					//-x,-y,-z
					//-x,+y,-z
					list.add(Quad.getQuad(
						enumFacing,
						new Vec3d(max.x, max.y, min.z),
						new Vec3d(max.x, min.y, min.z),
						new Vec3d(min.x, min.y, min.z),
						new Vec3d(min.x, max.y, min.z))
					);
					break;
				case SOUTH:
					//-x,+y,+z
					//-x,-y,+z
					//+x,-y,+z
					//+x,+y,+z
					list.add(Quad.getQuad(
						enumFacing,
						new Vec3d(min.x, max.y, max.z),
						new Vec3d(min.x, min.y, max.z),
						new Vec3d(max.x, min.y, max.z),
						new Vec3d(max.x, max.y, max.z))
					);
					break;
				case WEST:
					//-x,+y,-z
					//-x,-y,-z
					//-x,-y,+z
					//-x,+y,+z
					list.add(Quad.getQuad(
						enumFacing,
						new Vec3d(min.x, max.y, min.z),
						new Vec3d(min.x, min.y, min.z),
						new Vec3d(min.x, min.y, max.z),
						new Vec3d(min.x, max.y, max.z))
					);
					break;
				case EAST:
					//+x,+y,+z
					//+x,-y,+z
					//+x,-y,-z
					//+x,+y,-z
					list.add(Quad.getQuad(
						enumFacing,
						new Vec3d(max.x, max.y, max.z),
						new Vec3d(max.x, min.y, max.z),
						new Vec3d(max.x, min.y, min.z),
						new Vec3d(max.x, max.y, min.z))
					);
					break;
				default: {}
			}
		}
		TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
		for (Quad quad : list) {
			quad.setMaxBrightness(statePart.isMaxBrightness());
			quad.setRenderLayer(statePart.getRenderLayer());
			quad.setRgb(statePart.getRgb());
			quad.setSprite(map.getAtlasSprite(statePart.getIconName()));
		}
		return list;
	}
	
	/**
	 * Rotates part about axis. Positive axis is clockwise rotation.
	 * Negative axis is counter-clockwise rotation.
	 * 
	 * @param state the state
	 * @param facing the axis of rotation
	 * @param radians rotation angle in radians
	 * @return the rotated state object
	 */
	public static void rotate(AbstractState state, EnumFacing facing, double radians) {
		if (AxisDirection.POSITIVE.equals(facing.getAxisDirection())) {
			radians = -radians;
		}
		for (StatePart statePart : state.getStateParts()) {
			Vec3d vec1 = null;
			Vec3d vec2 = null;
			switch (facing) {
				case DOWN:
				case UP:
					vec1 = rotateAroundY(statePart.getVertexMin(), radians);
					vec2 = rotateAroundY(statePart.getVertexMax(), radians);
					break;
				case NORTH:
				case SOUTH:
					vec1 = rotateAroundZ(statePart.getVertexMin(), radians);
					vec2 = rotateAroundZ(statePart.getVertexMax(), radians);
					break;
				case WEST:
				case EAST:
					vec1 = rotateAroundX(statePart.getVertexMin(), radians);
					vec2 = rotateAroundX(statePart.getVertexMax(), radians);
					break;
			}
			statePart.setVertexMin(new Vec3d(Math.min(vec1.x, vec2.x), Math.min(vec1.y, vec2.y), Math.min(vec1.z, vec2.z)));
			statePart.setVertexMax(new Vec3d(Math.max(vec1.x, vec2.x), Math.max(vec1.y, vec2.y), Math.max(vec1.z, vec2.z)));
		}
		
		// Need to rotate texture face
	}
	
    private static Vec3d rotateAroundX(Vec3d vec3d, double angle) {
    	double y = 0.5D + (vec3d.z - 0.5D) * Math.sin(angle) + (vec3d.y - 0.5D) * Math.cos(angle);
    	double z = 0.5D + (vec3d.z - 0.5D) * Math.cos(angle) - (vec3d.y - 0.5D) * Math.sin(angle);
    	return new Vec3d(vec3d.x, y, z);
    }
	
    private static Vec3d rotateAroundY(Vec3d vec3d, double angle) {
    	double z = 0.5D + (vec3d.x - 0.5D) * Math.sin(angle) + (vec3d.z - 0.5D) * Math.cos(angle);
    	double x = 0.5D + (vec3d.x - 0.5D) * Math.cos(angle) - (vec3d.z - 0.5D) * Math.sin(angle);
    	return new Vec3d(x, vec3d.y, z);
    }
    
    private static Vec3d rotateAroundZ(Vec3d vec3d, double angle) {
    	double y = 0.5D + (vec3d.x - 0.5D) * Math.sin(angle) + (vec3d.y - 0.5D) * Math.cos(angle);
    	double x = 0.5D + (vec3d.x - 0.5D) * Math.cos(angle) - (vec3d.y - 0.5D) * Math.sin(angle);
    	return new Vec3d(x, y, vec3d.z);
    }
    
}
