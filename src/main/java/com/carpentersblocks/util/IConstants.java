package com.carpentersblocks.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Matrix4f;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;

public interface IConstants {
	
	public static final int DEFAULT_BRIGHTNESS = 0xffffff;
	public static final Set<EnumFacing> DEFAULT_RENDER_FACES = new HashSet<EnumFacing>(Arrays.asList(EnumFacing.VALUES));
	public static final BlockRenderLayer DEFAULT_RENDER_LAYER = BlockRenderLayer.CUTOUT_MIPPED;
	public static final int DEFAULT_RGB = 0xffffff;
	public static final String EMPTY_STRING = "";
	
	/**
	 * NONE
	 * THIRD_PERSON_LEFT_HAND
	 * THIRD_PERSON_RIGHT_HAND
	 * FIRST_PERSON_LEFT_HAND
	 * FIRST_PERSON_RIGHT_HAND
	 * HEAD
	 * GUI
	 * GROUND
	 * FIXED
	 */
    public static final Matrix4f[] perspectiveMatrix = {
    	null,
    	new TRSRTransformation(new ItemTransformVec3f(new Vector3f(75.0f,225.0f,0.0f),new Vector3f(0.0f,0.15625f,0.0f),new Vector3f(0.375f,0.375f,0.375f))).getMatrix(),
    	new TRSRTransformation(new ItemTransformVec3f(new Vector3f(75.0f,225.0f,0.0f),new Vector3f(0.0f,0.15625f,0.0f),new Vector3f(0.375f,0.375f,0.375f))).getMatrix(),
    	new TRSRTransformation(new ItemTransformVec3f(new Vector3f(0.0f,45.0f,0.0f),new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.4f,0.4f,0.4f))).getMatrix(),
    	new TRSRTransformation(new ItemTransformVec3f(new Vector3f(0.0f,225.0f,0.0f),new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.4f,0.4f,0.4f))).getMatrix(),
		new TRSRTransformation(new ItemTransformVec3f(new Vector3f(0.0f,0.0f,0.0f),new Vector3f(1.0f,1.0f,1.0f),new Vector3f(0.0f,0.0f,0.0f))).getMatrix(),
		new TRSRTransformation(new ItemTransformVec3f(new Vector3f(30.0f,45.0f,0.0f),new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.625f,0.625f,0.625f))).getMatrix(),
		new TRSRTransformation(new ItemTransformVec3f(new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.0f,0.1875f,0.0f),new Vector3f(0.25f,0.25f,0.25f))).getMatrix(),
		new TRSRTransformation(new ItemTransformVec3f(new Vector3f(0.0f,0.0f,0.0f),new Vector3f(0.5f,0.5f,0.5f),new Vector3f(0.0f,0.0f,0.0f))).getMatrix()
    };
    
}
