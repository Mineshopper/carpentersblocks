package com.carpentersblocks.util.states.factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.carpentersblocks.block.IStateImplementor;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.states.State;
import com.carpentersblocks.util.states.StatePart;
import com.carpentersblocks.util.states.StateUtil;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public abstract class AbstractState {
	
	protected CbTileEntity _cbTileEntity;
	protected State _state;
	
	public AbstractState(CbTileEntity cbTileEntity) {
		_cbTileEntity = cbTileEntity;
		IStateImplementor impl = (IStateImplementor) cbTileEntity.getBlockType();
		_state = new State(impl.getStateMap().getState(impl.getStateDescriptor(cbTileEntity)));
		rotate();
		StateUtil.calcCanSeeSky(this);
	}
	
	public List<AxisAlignedBB> getAxisAlignedBBs() {
		List<AxisAlignedBB> list = new ArrayList<AxisAlignedBB>();
		Iterator<Entry<String, StatePart>> iter = _state.getStateParts().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, StatePart> entry = iter.next();
			Vec3d min = entry.getValue().getVertexMin();
			Vec3d max = entry.getValue().getVertexMax();
			list.add(new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z));
		}
		return list;
	}
	
	public AxisAlignedBB getAxisAlignedBB() {
		double minX = 0;
		double minY = 0;
		double minZ = 0;
		double maxX = 0;
		double maxY = 0;
		double maxZ = 0;
		List<AxisAlignedBB> aabbs = getAxisAlignedBBs();
		for (int i = 0; i < aabbs.size(); ++i) {
			AxisAlignedBB aabb = aabbs.get(i);
			if (i == 0) {
				minX = aabb.minX;
				minY = aabb.minY;
				minZ = aabb.minZ;
				maxX = aabb.maxX;
				maxY = aabb.maxY;
				maxZ = aabb.maxZ;
			} else {
				minX = Math.min(minX, aabb.minX);
				minY = Math.min(minY, aabb.minY);
				minZ = Math.min(minZ, aabb.minZ);
				maxX = Math.max(maxX, aabb.maxX);
				maxY = Math.max(maxY, aabb.maxY);
				maxZ = Math.max(maxZ, aabb.maxZ);
			}
		}
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public List<StatePart> getStateParts() {
		List<StatePart> list = new ArrayList<StatePart>();
		Iterator<Entry<String, StatePart>> iter = _state.getStateParts().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, StatePart> entry = iter.next();
			list.add(entry.getValue());
		}
		return list;
	}
	
	protected abstract void rotate();
	
}
