package com.carpentersblocks.util.states;

public class StateConstants {

	public static final String DEFAULT_STATE = "default";
	public static final String PRESSURE_PLATE_DEPRESSED_STATE = "depressed";
	public static final String STATE_PART_FLOWERPOT_POT = "pot";
	public static final String STATE_PART_FLOWERPOT_SOIL = "soil";
	public static final String STATE_PART_FLOWERPOT_SMALL_PLANT = "small_plant";
	
	public enum StatePartEnum {
		
		FLOWERPOT_POT("pot"),
		FLOWERPOT_SOIL("soil"),
		FLOWERPOT_SMALL_PLANT("small_plant");
		
		private String _key;
		
		StatePartEnum(String key) {
			this._key = key;
		}

		public String getKey() {
			return this._key;
		}

		public void setKey(String key) {
			this._key = key;
		}
		
	}
	
}
