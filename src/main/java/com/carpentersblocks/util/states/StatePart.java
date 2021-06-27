package com.carpentersblocks.util.states;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.util.RotationUtil.CbRotation;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Class holds {@link StatePartQuad StatePartQuads} and is responsible
 * for providing {@link AxisAlignedBB} helper methods and generating
 * {@link Quad Quads} for client-side rendering.
 */
public class StatePart {
	
	private static final StatePart EMPTY_STATE_PART = new StatePart();
	private List<StatePartQuad> _statePartQuads;
	private boolean _collision;
	
	private StatePart() { }
	
	StatePart(List<StatePartQuad> statePartQuads, boolean collision) {
		this._collision = collision;
		this._statePartQuads = statePartQuads;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param statePart the source state part
	 */
	StatePart(StatePart statePart) {
		this._statePartQuads = new ArrayList<>();
		if (statePart._statePartQuads != null) {
			for (StatePartQuad quad : statePart._statePartQuads) {
				this._statePartQuads.add(new StatePartQuad(quad));
			}
		}
		this._collision = statePart._collision;
	}
	
	/**
	 * Gets bounding boxes for constituent state quads.
	 * <p>
	 * Package-level access only.
	 * 
	 * @return a list of {@link AxisAlignedBB AxisAlignedBBs}
	 */
	List<AxisAlignedBB> getBoundingBoxes() {
		if (!_collision) {
			return Collections.emptyList();
		}
		List<AxisAlignedBB> aabbs = new ArrayList<>();
		Iterator<StatePartQuad> iterQuads = _statePartQuads.iterator();
		while (iterQuads.hasNext()) {
			iterQuads.next().toAxisAlignedBB(aabbs);
		}
		return aabbs;
	}
	
	/**
	 * Gets quads for constituent state parts.
	 * <p>
	 * Package-level access only.
	 * 
	 * @return a list of {@link Quad quads}
	 */
	@OnlyIn(Dist.CLIENT)
	public List<Quad> getQuads() {
		List<Quad> quads = new ArrayList<>();
		Iterator<StatePartQuad> iterQuads = _statePartQuads.iterator();
		while (iterQuads.hasNext()) {
			iterQuads.next().toQuad(quads);
		}
		return quads;
	}
	
	/**
	 * Gets empty state part.
	 * <p>
	 * Used by {@link DummyStateMap}.
	 * <p>
	 * Package-level access only.
	 * 
	 * @return an empty state part
	 */
	public static StatePart getEmptyStatePart() {
		return EMPTY_STATE_PART;
	}
	
	/**
	 * Rotates state part.
	 * 
	 * @param rotation the rotation
	 */
	public void rotate(CbRotation rotation) {
		_statePartQuads.forEach(q -> q.rotate(rotation));
	}
	
}