package com.carpentersblocks.util.states.factory;

import java.util.List;

import com.carpentersblocks.block.IStateImplementor;
import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.util.RotationUtil.CbRotation;
import com.carpentersblocks.util.states.State;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractState {
	
	protected CbTileEntity _cbTileEntity;
	protected State _state;
		
	public AbstractState(CbTileEntity cbTileEntity) {
		_cbTileEntity = cbTileEntity;
		IStateImplementor impl = (IStateImplementor) cbTileEntity.getBlockState().getBlock();
		_state = impl.getStateMap().getState(impl.getStateDescriptor(cbTileEntity));
		rotate();
	}
	
	/**
	 * Gets bounding boxes for constituent state parts.
	 * 
	 * @return a list of {@link AxisAlignedBB AxisAlignedBBs}
	 */
	final public List<AxisAlignedBB> getBoundingBoxes() {
		return _state.getBoundingBoxes();
	}
	
	/**
	 * Gets combined bounding box for state.
	 * 
	 * @return an {@link AxisAlignedBB}
	 */
	final public AxisAlignedBB getBoundingBox() {
		double minX = Double.MAX_VALUE;
		double minY = Double.MAX_VALUE;
		double minZ = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double maxY = Double.MIN_VALUE;
		double maxZ = Double.MIN_VALUE;
		List<AxisAlignedBB> aabbs = getBoundingBoxes();
		for (int i = 0; i < aabbs.size(); ++i) {
			AxisAlignedBB aabb = aabbs.get(i);
			minX = Math.min(minX, aabb.minX);
			minY = Math.min(minY, aabb.minY);
			minZ = Math.min(minZ, aabb.minZ);
			maxX = Math.max(maxX, aabb.maxX);
			maxY = Math.max(maxY, aabb.maxY);
			maxZ = Math.max(maxZ, aabb.maxZ);
		}
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * Gets quads for rendering.
	 * 
	 * @return a list of {@link Quad Quads}
	 */
	@OnlyIn(Dist.CLIENT)
	final public List<Quad> getQuads() {
		return _state.getQuads();
	}
	
	/**
	 * Rotates state.
	 * 
	 * @param rotation the rotation
	 */
	final protected void rotate(CbRotation rotation) {
		this._state.rotate(rotation);
	}
	
	protected abstract void rotate();
	
}
