package com.carpentersblocks.util.states.loader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.carpentersblocks.ModLogger;
import com.carpentersblocks.block.CbBlocks;
import com.carpentersblocks.util.states.DummyStateMap;
import com.carpentersblocks.util.states.StateConstants;
import com.carpentersblocks.util.states.StateMap;
import com.carpentersblocks.util.states.StateMapConverter;
import com.carpentersblocks.util.states.loader.domain.StateDto;
import com.carpentersblocks.util.states.loader.domain.StateMapDto;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class StateLoader {

	public static final Map<String,String[]> _stateDescriptors;
	static {
		_stateDescriptors = new HashMap<String,String[]>();
		_stateDescriptors.put(CbBlocks.REGISTRY_NAME_PRESSURE_PLATE, new String[] { StateConstants.PRESSURE_PLATE_DEPRESSED_STATE });
	}
	
	private static final String STATES_PATH = "/data/carpentersblocks/states/";
	private static final String JSON_EXT = ".json";
	
	private String _registryName;
	private StateMap _stateMap;
	private StateMapDto _stateMapDto;
	private boolean _error;
	
	public StateLoader(String registryName) {
		_registryName = registryName;
		try {
			_stateMapDto = parseJson();
			this.applyStateInheritance();
			_stateMap = StateMapConverter.convert(_stateMapDto);
		} catch (Exception ex) {
    		ModLogger.log(Level.WARN, "Exception encountered while loading state data for {}, check JSON formatting.", _registryName);
    		_stateMap = new DummyStateMap();
    		ex.printStackTrace();
		}
	}
	
	private StateMapDto parseJson() throws InstantiationException, IllegalAccessException, JsonSyntaxException, JsonIOException {
		StateMapDto stateContainerDto = null;
    	Gson gson = new Gson();
    	try (InputStream inputStream = this.getClass().getResourceAsStream(STATES_PATH + _registryName + JSON_EXT);
    			InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
    		stateContainerDto = gson.fromJson(inputStreamReader, StateMapDto.class);
			if (!stateContainerDto.containsKey(StateConstants.DEFAULT_STATE)) {
				throw new Exception("Default state is missing");
			}
    	} catch (Exception ex) {
    		ModLogger.log(Level.WARN, "Exception encountered while loading state data for {}: {}", _registryName, ex.getMessage());
    		ex.printStackTrace();
    	}
    	return stateContainerDto;
	}
	
	/**
	 * Fills parent and child state properties using inheritance.
	 */
	private void applyStateInheritance() {
		List<StateDto> stateDtos = new ArrayList<>();
		// set parent states for every state
		for (Map.Entry<String, StateDto> stateDtoEntry : _stateMapDto.entrySet()) {
			StateDto stateDto = stateDtoEntry.getValue();
			String parent = stateDto.getParent();
			if (_stateMapDto.containsKey(parent)) {
				ModLogger.log(Level.FATAL, "Unable to locate parent state '{}' for block '{}'", parent, _registryName);
				continue;
			}
			stateDto.setParent(_stateMapDto.get(parent));
			stateDtos.add(stateDto);
		}
		// apply inheritance
		stateDtos.forEach(StateDto::inheritFromParent);
	}
	
	public StateMap getStateMap() {
		return _stateMap;
	}
	
	public boolean isValid() {
		return !_error;
	}
	
}
