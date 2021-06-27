package com.carpentersblocks.util.states;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;

import com.carpentersblocks.ModLogger;
import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.client.renderer.RenderConstants;
import com.carpentersblocks.util.states.loader.domain.StateDto;
import com.carpentersblocks.util.states.loader.domain.StateMapDto;
import com.carpentersblocks.util.states.loader.domain.StatePartCuboidComponentDto;
import com.carpentersblocks.util.states.loader.domain.StatePartDto;
import com.carpentersblocks.util.states.loader.domain.StatePartQuadComponentDto;
import com.carpentersblocks.util.states.loader.domain.Vec3fDto;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

public class StateMapConverter {

	private static final String REGEX_RGB_VALIDATION = "^(#?|0x)([A-Fa-f0-9]{6})$";
	private static final String REGEX_RGB = "0x|#";
	private static final String REGEX_RENDER_LAYER = "( +|_+)+";
	private static final String REGEX_DIRECTION_AXIS = "^[ -+] {0,1}(x|y|z)$";
	private static final String REGEX_DIRECTION = "^down|up|north|south|west|east$";
	private static final Vector3d DEFAULT_MIN_VECTOR = Vector3d.ZERO;
	
	/**
	 * Converts state map dto to state map.
	 * 
	 * @param stateMapDto the state map dto
	 * @return a state map
	 */
	public static StateMap convert(StateMapDto stateMapDto) {
		StateMap stateMap = new StateMap();
		for (Map.Entry<String, StateDto> stateDtoEntry : stateMapDto.entrySet()) {
			String stateDtoKey = stateDtoEntry.getKey();
		    StateDto stateDto = stateDtoEntry.getValue();
		    Map<String, StatePart> statePartMap = new HashMap<String, StatePart>();
		    State state = new State(statePartMap);
		    stateMap.putState(stateDtoKey, state);
		    for (Map.Entry<String, StatePartDto> statePartDtoEntry : stateDto.getParts().entrySet()) {
			    String statePartDtoKey = statePartDtoEntry.getKey();
			    StatePartDto statePartDto = statePartDtoEntry.getValue();
			    StatePart statePart = new StatePart(
			    		StateMapConverter.toStatePartQuads(statePartDto),
			    		StateMapConverter.getValueForBoolean(statePartDto.hasCollision(), true));
				statePartMap.put(statePartDtoKey, statePart);
		    }
		}
		return stateMap;
	}
	
	/**
	 * Converts a state part domain transfer object to a
	 * {@link StatePartQuad} that is used for generating
	 * collision {@link AxisAlignedBB AxisAlignedBBs} and {@link Quad Quads}
	 * for rendering client-side.
	 * 
	 * @param dto the domain transfer object
	 * @return a list of {@link StatePartQuad StatePartQuads)
	 */
	private static List<StatePartQuad> toStatePartQuads(StatePartDto dto) {
		List<StatePartQuad> list = new ArrayList<>();
		// Add quads to list
		dto.getQuads().forEach((k, v) -> list.add(toStatePartQuad(v)));

		// Convert cuboids to quads and add to list
		Iterator<Entry<String, StatePartCuboidComponentDto>> iter = dto.getCuboids().entrySet().iterator();
		while (iter.hasNext()) {
			StatePartCuboidComponentDto cuboidDto = iter.next().getValue();
			for (Direction facing : getRenderFacings(cuboidDto)) {
				list.add(new StatePartQuad(
						getVector3d(cuboidDto.getVertexMin()),
						getVector3d(cuboidDto.getVertexMax()),
						getVector3d(cuboidDto.getRotationDeg()),
						getRenderType(cuboidDto.getRenderLayer(), RenderType.cutoutMipped()),
						getValueForBoolean(cuboidDto.isMaxBrightness(), false),
						getValueForBoolean(cuboidDto.hasDiffuseLighting(), true),
						getIconName(cuboidDto.getIconName(), TextureAtlasSprites.RESOURCE_UNCOVERED_SOLID.toString()),
						getRgb(cuboidDto.getRgb(), RenderConstants.DEFAULT_RGB),
						false,
						facing));
			}
		}
		
		return list;
	}
	
	/**
	 * Converts a state part quad domain transfer object to a {@link StatePartQuad}.
	 * 
	 * @param dto the domain transfer object
	 * @return a {@link StatePartQuad)
	 */
	private static StatePartQuad toStatePartQuad(StatePartQuadComponentDto dto) {
		Vector3d vertexMin = getVector3d(dto.getVertexMin());
		Vector3d vertexMax = getVector3d(dto.getVertexMax());
		Vector3d rotationDeg = getVector3d(dto.getRotationDeg());
		RenderType renderLayer = getRenderType(dto.getRenderLayer(), RenderType.cutoutMipped());
		boolean maxBrightness = getValueForBoolean(dto.isMaxBrightness(), false);
		boolean diffuseLighting = getValueForBoolean(dto.hasDiffuseLighting(), true);
		String iconName = getIconName(dto.getIconName(), TextureAtlasSprites.RESOURCE_UNCOVERED_SOLID.toString());
		int rgb = getRgb(dto.getRgb(), RenderConstants.DEFAULT_RGB);
		boolean drawReverse = getValueForBoolean(dto.getDrawReverse(), false);
		Direction facing = getDirection(dto.getFacing());
		return new StatePartQuad(
				vertexMin,
				vertexMax,
				rotationDeg,
				renderLayer,
				maxBrightness,
				diffuseLighting,
				iconName,
				rgb,
				drawReverse,
				facing);
	}
	
	/**
	 * Gets a set of render facings for cuboid.
	 * 
	 * @param dto the data transfer object
	 * @return a set of {@link Direction facings}
	 */
	private static Set<Direction> getRenderFacings(StatePartCuboidComponentDto dto) {
		Set<Direction> set = new HashSet<Direction>();
		if (dto.getRenderFaces() == null) {
			set.addAll(Arrays.asList(Direction.values()));
		} else {
			for (String value : dto.getRenderFaces()) {
				if ("all".equalsIgnoreCase(value.trim())) {
					set.addAll(Arrays.asList(Direction.values()));
					break;
				} else {
					value = value.trim();
					for (Direction direction : Direction.values()) {
						String modifier = AxisDirection.POSITIVE.equals(direction.getAxisDirection()) ? "+" : "-";
						String axis = direction.getAxis().getName();
						if (value.equalsIgnoreCase(direction.getName()) || value.equalsIgnoreCase(axis) || value.equalsIgnoreCase(modifier + axis)) {
							set.add(direction);
							break;
						}
					}
				}
			}
		}
		return set;
	}
	
	/**
	 * Converts Vec3fDto to {@link Vector3d}
	 * 
	 * @param the vec3f
	 * @return a Vector3d
	 */
	private static Vector3d getVector3d(Vec3fDto vec3f) {
		Vector3d value = DEFAULT_MIN_VECTOR;
		if (vec3f != null) {
			value = new Vector3d(vec3f.getX() / 16.0f, vec3f.getY() / 16.0f, vec3f.getZ() / 16.0f);
		}
		return value;
	}
	
	/**
	 * Converts block render type string to {@link RenderType}.
	 * <p>
	 * If string cannot be parsed, will return <code>defaultRenderType</code>.
	 * 
	 * @param renderType the render type string
	 * @param defaultRenderType the default render type
	 * @return the block render type
	 */
	private static RenderType getRenderType(String renderType, RenderType defaultRenderType) {
		RenderType blockRenderLayer = null;
		if (StringUtils.isNotBlank(renderType) && (blockRenderLayer = parseRenderLayer(renderType)) != null) {
			return blockRenderLayer;
		} else {
			return defaultRenderType;
		}
	}
	
	/**
	 * Gets icon name from string.
	 * <p>
	 * If string is blank, will return <code>defaultIconName</code>.
	 * 
	 * @param iconName the icon name
	 * @param defaultIconName the default icon name
	 * @return a string
	 */
	private static String getIconName(String iconName, String defaultIconName) {
		if (StringUtils.isNotBlank(iconName)) {
			return iconName;
		} else {
			return defaultIconName;
		}
	}
	
	/**
	 * Gets {@link RenderType} from string.
	 * <p>
	 * If string cannot be parsed, will return <code>null</code>.
	 * 
	 * @param renderLayer the block render layer
	 * @return a block render type, or <code>null</code>
	 */
	private static RenderType parseRenderLayer(String renderLayer) {
		String[] renderTypeArr1 = renderLayer.toLowerCase().split(REGEX_RENDER_LAYER);
		for (RenderLayerType renderLayerType : RenderLayerType.values()) {
			String[] renderTypeArr2 = renderLayerType.name().toLowerCase().split(REGEX_RENDER_LAYER);
		    Set<String> set1 = new HashSet<String>(Arrays.asList(renderTypeArr1));
		    Set<String> set2 = new HashSet<String>(Arrays.asList(renderTypeArr2));
		    if (set1.equals(set2)) {
		    	return renderLayerType.getRenderType();
		    }
		}
		return null;
	}
	
	/**
	 * Converts red-green-blue string to {@link Integer}.
	 * <p>
	 * If string cannot be parsed, will return <code>defaultRgb</code>.
	 * 
	 * @param rgb the red-green-blue string
	 * @param defaultRgb the default red-green-blue value
	 * @return an integer value
	 */
	private static Integer getRgb(String rgb, Integer defaultRgb) {
		Integer decodedRgb = null;
		if (StringUtils.isNotBlank(rgb) && (decodedRgb = getRgb(rgb)) != null) {
			return decodedRgb;
		} else {
			return defaultRgb;
		}
	}
	
	/**
	 * Gets {@link Integer} from string.
	 * <p>
	 * If string cannot be parsed, will return <code>null</code>.
	 * 
	 * @param rgb the red-green-blue string
	 * @return a integer, or <code>null</code>
	 */
	private static Integer getRgb(String rgb) {
		Integer value = null;
		if (rgb != null) {
			Pattern pattern = Pattern.compile(REGEX_RGB_VALIDATION);
			Matcher matcher = pattern.matcher(rgb);
			if (matcher.matches()) {
				String temp = rgb.toLowerCase().replaceAll(REGEX_RGB, "");
				value = Integer.decode("0x" + temp);
			} else {
				ModLogger.log(Level.WARN, "Incorrect format used for rgb: " + rgb);
			}
		}
		return value;
	}
	
	/**
	 * Gets {@link Direction} from string.
	 * 
	 * @param direction the direction
	 * @return the {@link Direction}
	 */
	private static Direction getDirection(String direction) {
		Direction value = null;
		if (direction != null && !(direction = direction.toLowerCase().trim()).isEmpty()) {
			Pattern pattern = Pattern.compile(REGEX_DIRECTION);
			Matcher matcher = pattern.matcher(direction);
			Pattern pattern2 = Pattern.compile(REGEX_DIRECTION_AXIS);
			Matcher matcher2 = pattern2.matcher(direction);
			if (matcher.matches()) {
				value = Direction.byName(matcher.group());
			} else if (matcher2.matches()) {
				String axis = (direction.substring(0, 1).equals("-") ? "-" : "+")
						+ (direction.length() == 1 ? direction : direction.substring(1));
				value = "-y".equals(axis) ? Direction.DOWN :
						"+y".equals(axis) ? Direction.UP :
						"-z".equals(axis) ? Direction.NORTH :
						"+z".equals(axis) ? Direction.SOUTH :
						"-x".equals(axis) ? Direction.WEST :
						Direction.EAST;
			} else {
				ModLogger.log(Level.WARN, "Incorrect format used for facing: " + direction);
			}
		}
		return value;
	}
	
	/**
	 * Gets {@link Boolean} value from string.
	 * <p>
	 * If string cannot be parsed, will return <code>defaultBoolean</code>.
	 * 
	 * @param booleanValue the string to parse
	 * @param defaultBoolean the default boolean value
	 * @return
	 */
	public static Boolean getValueForBoolean(String booleanValue, Boolean defaultBoolean) {
		if (StringUtils.isNotBlank(booleanValue)) {
			return Boolean.valueOf(booleanValue);
		} else {
			return defaultBoolean;
		}
	}
	
}
