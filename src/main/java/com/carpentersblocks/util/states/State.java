package com.carpentersblocks.util.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.util.RotationUtil.CbRotation;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class State {
	
	private Map<String, StatePart> _stateParts;
	
	State(Map<String, StatePart> components) {
		this._stateParts = components;
	}
	
	/**
	 * Copy constructor.
	 * <p>
	 * Package-level access only.
	 */
	State(State state) {
		_stateParts = new HashMap<String, StatePart>();
		Iterator<Entry<String, StatePart>> iter = state._stateParts.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry<String, StatePart> pair = (Map.Entry<String, StatePart>) iter.next();
	        _stateParts.put(pair.getKey(), new StatePart(pair.getValue()));
	    }
	}
	
	/**
	 * Gets bounding boxes for constituent state parts.
	 * 
	 * @return a list of {@link AxisAlignedBB AxisAlignedBBs}
	 */
	public List<AxisAlignedBB> getBoundingBoxes() {
		List<AxisAlignedBB> aabbs = new ArrayList<>();
		Iterator<Map.Entry<String, StatePart>> iter = _stateParts.entrySet().iterator();
	    while (iter.hasNext()) {
	    	aabbs.addAll(iter.next().getValue().getBoundingBoxes());
	    }
	    return aabbs;
	}
	
	/**
	 * Gathers quads for constituent state parts.
	 * <p>
	 * Client-side only.
	 * 
	 * @return a list of {@link Quad Quads}
	 */
	@OnlyIn(Dist.CLIENT)
	public List<Quad> getQuads() {
		List<Quad> quads = new ArrayList<>();
		Iterator<Map.Entry<String, StatePart>> iter = _stateParts.entrySet().iterator();
	    while (iter.hasNext()) {
	    	quads.addAll(iter.next().getValue().getQuads());
	    }
	    return quads;
	}
	
	/**
	 * Rotates state.
	 * 
	 * @param rotation the rotation
	 */
	public void rotate(CbRotation rotation) {
		Iterator<Entry<String, StatePart>> iter = _stateParts.entrySet().iterator();
		while (iter.hasNext()) {
			iter.next().getValue().rotate(rotation);
		}
	}
	
}
