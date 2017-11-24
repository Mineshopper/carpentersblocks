package com.carpentersblocks.util.states.loader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.states.DummyStateMap;
import com.carpentersblocks.util.states.StateConstants;
import com.carpentersblocks.util.states.StateMap;
import com.carpentersblocks.util.states.StateMapConverter;
import com.carpentersblocks.util.states.loader.dto.StateDTO;
import com.carpentersblocks.util.states.loader.dto.StateMapDTO;
import com.carpentersblocks.util.states.loader.dto.StatePartDTO;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class StateLoader {

	public static final Map<String,String[]> _stateDescriptors;
	static {
		_stateDescriptors = new HashMap<String,String[]>();
		_stateDescriptors.put(BlockRegistry.REGISTRY_NAME_PRESSURE_PLATE, new String[] { StateConstants.PRESSURE_PLATE_DEPRESSED_STATE });
	}
	
	private static final String MOD_RESOURCE_PATH = "/assets/carpentersblocks/states/";
	private static final String MC_RESOURCE_PATH = "/assets/minecraft/models/block/";
	private static final String JSON_EXT = ".json";
	
	private String _registryName;
	private StateMap _stateMap;
	private StateMapDTO _stateMapDto;
	private boolean _error;
	
	public StateLoader(String registryName) {
		_registryName = registryName;
		try {
			_stateMapDto = parseJson();
			this.fillChildStates();
			_stateMap = new StateMapConverter(_stateMapDto).convert();
		} catch (Exception ex) {
    		ModLogger.warning("Exception encountered while loading state data for " + _registryName + ", check JSON formatting.");
    		_stateMap = new DummyStateMap();
    		ex.printStackTrace();
		}
	}
	
	private StateMapDTO parseJson() throws InstantiationException, IllegalAccessException, JsonSyntaxException, JsonIOException {
		StateMapDTO stateContainerDto = null;
    	InputStream inputStream = null;
    	Gson gson = new Gson();
    	try {
			inputStream = this.getClass().getResourceAsStream(MOD_RESOURCE_PATH + _registryName + JSON_EXT);
			stateContainerDto = gson.fromJson(new InputStreamReader(inputStream), StateMapDTO.class);
			if (!stateContainerDto.containsKey(StateConstants.DEFAULT_STATE)) {
				throw new Exception("Default state is missing");
			}
    	} catch (Exception ex) {
    		ModLogger.warning("Exception encountered while loading state data for " + _registryName);
    		ex.printStackTrace();
    	}
    	return stateContainerDto;
	}
	
	private void fillChildStates() {
		StateDTO defaultState = _stateMapDto.get(StateConstants.DEFAULT_STATE);
		for (Map.Entry<String, StateDTO> stateDtoEntry : _stateMapDto.entrySet()) {
			String stateDtoKey = stateDtoEntry.getKey();
		    StateDTO stateDto = stateDtoEntry.getValue();
			for (Map.Entry<String, StatePartDTO> statePartDtoEntry : stateDto.getParts().entrySet()) {
				String statePartDtoKey = statePartDtoEntry.getKey();
			    StatePartDTO statePartDto = statePartDtoEntry.getValue();
			    StatePartDTO defaultStatePartDto = defaultState.getParts().get(statePartDtoKey);
			    if (defaultStatePartDto != null) {
				    statePartDto.setMaxBrightness(defaultStatePartDto.isMaxBrightness());
				    if (statePartDto.getRenderFaces() == null || statePartDto.getRenderFaces().isEmpty()) {
				    	statePartDto.setRenderFaces(defaultStatePartDto.getRenderFaces());
				    }
				    if (statePartDto.getRenderLayer() == null) {
				    	statePartDto.setRenderLayer(defaultStatePartDto.getRenderLayer());
				    }
				    if (statePartDto.getRgb() == null) {
				    	statePartDto.setRgb(defaultStatePartDto.getRgb());
				    }
				    if (statePartDto.getIconName() == null) {
				    	statePartDto.setIconName(defaultStatePartDto.getIconName());
				    }
				    if (statePartDto.getVertexMax() == null) {
				    	statePartDto.setVertexMax(defaultStatePartDto.getVertexMax());
				    }
				    if (statePartDto.getVertexMin() == null) {
				    	statePartDto.setVertexMin(defaultStatePartDto.getVertexMin());
				    }
			    }
			}
		}
	}
	
	public StateMap getStateMap() {
		return _stateMap;
	}
	
	public boolean isValid() {
		return !_error;
	}
	
}
