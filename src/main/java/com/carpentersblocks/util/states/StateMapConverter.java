package com.carpentersblocks.util.states;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.carpentersblocks.util.IConstants;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.states.loader.Vec3f;
import com.carpentersblocks.util.states.loader.dto.StateDTO;
import com.carpentersblocks.util.states.loader.dto.StateMapDTO;
import com.carpentersblocks.util.states.loader.dto.StatePartDTO;

import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.Vec3d;

public class StateMapConverter {

	private static final String RGB_VALIDATION_REGEX = "^(#?|0x)([A-Fa-f0-9]{6})$";
	private static final String RGB_REGEX = "0x|#";
	private static final String RENDER_LAYER_REGEX = "( +|_+)+";
	private static final String RENDER_FACE_REGEX = "^\\s+([-+]?[xXyYzZ]{1}|(?i)all|down|up|north|south|west|east(?-i))\\s*$";
	private static final Vec3d DEFAULT_MIN_VECTOR = new Vec3d(0.0d, 0.0d, 0.0d);
	private static final Vec3d DEFAULT_MAX_VECTOR = new Vec3d(1.0d, 1.0d, 1.0d);
	
	private StateMapDTO _stateMapDto;
	
	public StateMapConverter(StateMapDTO stateMapDto) {
		_stateMapDto = stateMapDto;
	}
	
	/**
	 * Converts StateMapDTO to StateMap.
	 * 
	 * @param stateMapDto the state map DTO
	 * @return a state map
	 */
	public StateMap convert() {
		StateMap stateMap = new StateMap();
		for (Map.Entry<String, StateDTO> stateDtoEntry : _stateMapDto.entrySet()) {
			String stateDtoKey = stateDtoEntry.getKey();
		    StateDTO stateDto = stateDtoEntry.getValue();
		    State state = new State();
		    stateMap.putState(stateDtoKey, state);
		    Map<String, StatePart> statePartMap = new HashMap<String, StatePart>();
		    state.setStateParts(statePartMap);
		    for (Map.Entry<String, StatePartDTO> statePartDtoEntry : stateDto.getParts().entrySet()) {
			    String statePartDtoKey = statePartDtoEntry.getKey();
			    StatePartDTO statePartDto = statePartDtoEntry.getValue();
				StatePart statePart = new StatePart();
				statePartMap.put(statePartDtoKey, statePart);
				statePart.setMaxBrightness(statePartDto.isMaxBrightness());
				statePart.setRenderFaces(getRenderFaces(statePartDto));
				statePart.setRenderLayer(getRenderLayer(statePartDto));
				statePart.setRgb(getRgb(statePartDto));
				statePart.setIconName(statePartDto.getIconName());
				Vec3d vec1 = getVertexMin(statePartDto);
				Vec3d vec2 = getVertexMax(statePartDto);
				Vec3d minVec = new Vec3d(Math.min(vec1.x, vec2.x), Math.min(vec1.y, vec2.y), Math.min(vec1.z, vec2.z));
				Vec3d maxVec = new Vec3d(Math.max(vec1.x, vec2.x), Math.max(vec1.y, vec2.y), Math.max(vec1.z, vec2.z));
				statePart.setVertexMin(minVec);
				statePart.setVertexMax(maxVec);
		    }
		}
		return stateMap;
	}
		
	private Set<EnumFacing> getRenderFaces(StatePartDTO statePartDto) {
		Set<EnumFacing> set = new HashSet<EnumFacing>();
		if (statePartDto.getRenderFaces() == null) {
			set.addAll(Arrays.asList(EnumFacing.values()));
		} else {
			for (String value : statePartDto.getRenderFaces()) {
				if ("all".equalsIgnoreCase(value.trim())) {
					set.addAll(Arrays.asList(EnumFacing.values()));
					break;
				} else {
					value = value.trim();
					for (EnumFacing enumFacing : EnumFacing.values()) {
						String direction = AxisDirection.POSITIVE.equals(enumFacing.getAxisDirection()) ? "+" : "-";
						String axis = enumFacing.getAxis().getName();
						if (value.equalsIgnoreCase(enumFacing.getName()) || value.equalsIgnoreCase(axis) || value.equalsIgnoreCase(direction + axis)) {
							set.add(enumFacing);
							break;
						}
					}
				}
			}
		}
		return set;
	}
	
	private BlockRenderLayer getRenderLayer(StatePartDTO statePartDto) {
		if (statePartDto.getRenderLayer() != null) {
			String[] renderLayerArr1 = statePartDto.getRenderLayer().toLowerCase().split(RENDER_LAYER_REGEX);
			for (BlockRenderLayer blockRenderLayer : BlockRenderLayer.values()) {
				String[] renderLayerArr2 = blockRenderLayer.toString().toLowerCase().split(RENDER_LAYER_REGEX);
			    Set<String> set1 = new HashSet<String>(Arrays.asList(renderLayerArr1));
			    Set<String> set2 = new HashSet<String>(Arrays.asList(renderLayerArr2));
			    if (set1.equals(set2)) {
			    	return blockRenderLayer;
			    }
			}
		}
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	private int getRgb(StatePartDTO statePartDto) {
		int value = IConstants.DEFAULT_RGB;
		String rgb = statePartDto.getRgb();
		if (rgb != null) {
			Pattern pattern = Pattern.compile(RGB_VALIDATION_REGEX);
			Matcher matcher = pattern.matcher(rgb);
			if (matcher.matches()) {
				String temp = statePartDto.getRgb().toLowerCase().replaceAll(RGB_REGEX, "");
				value = Integer.decode("0x" + temp);
			} else {
				ModLogger.warning("Incorrect format used for rgb: " + rgb);
			}
		}
		return value;
	}
	
	private Vec3d getVertexMin(StatePartDTO statePartDto) {
		Vec3d value = DEFAULT_MIN_VECTOR;
		Vec3f vec3f = statePartDto.getVertexMin();
		if (vec3f != null) {
			value = new Vec3d(vec3f.getX() / 16.0f, vec3f.getY() / 16.0f, vec3f.getZ() / 16.0f);
		}
		return value;
	}
	
	private Vec3d getVertexMax(StatePartDTO statePartDto) {
		Vec3d value = DEFAULT_MAX_VECTOR;
		Vec3f vec3f = statePartDto.getVertexMax();
		if (vec3f != null) {
			value = new Vec3d(vec3f.getX() / 16.0f, vec3f.getY() / 16.0f, vec3f.getZ() / 16.0f);
		}
		return value;
	}
	
}
