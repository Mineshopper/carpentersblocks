package com.carpentersblocks.client.renderer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3f;

public interface RenderConstants {
	
	public static final int DEFAULT_BRIGHTNESS = 0xffffff;
	public static final Set<Direction> DEFAULT_RENDER_FACES = new HashSet<Direction>(Arrays.asList(Direction.values()));
	public static final RenderType DEFAULT_RENDER_LAYER = RenderType.cutoutMipped();
	public static final int DEFAULT_RGB = 0xffffff;
	public static final String EMPTY_STRING = "";
    
	/** Item camera transforms for rendering mod blocks. */
    public static final ItemCameraTransforms itemCameraTransforms = new ItemCameraTransforms(
    	new ItemTransformVec3f(new Vector3f(75.0f, 225.0f, 0.0f), new Vector3f(0.0f, 0.15625f, 0.0f), new Vector3f(0.375f, 0.375f, 0.375f)), // thirdperson_left
    	new ItemTransformVec3f(new Vector3f(75.0f, 225.0f, 0.0f), new Vector3f(0.0f, 0.15625f, 0.0f), new Vector3f(0.375f, 0.375f, 0.375f)), // thirdperson_right
    	new ItemTransformVec3f(new Vector3f( 0.0f,  45.0f, 0.0f), new Vector3f(0.0f,     0.0f, 0.0f), new Vector3f(  0.4f,   0.4f,   0.4f)), // firstperson_left
    	new ItemTransformVec3f(new Vector3f( 0.0f, 225.0f, 0.0f), new Vector3f(0.0f,     0.0f, 0.0f), new Vector3f(  0.4f,   0.4f,   0.4f)), // firstperson_right
		new ItemTransformVec3f(new Vector3f( 0.0f,   0.0f, 0.0f), new Vector3f(1.0f,     1.0f, 1.0f), new Vector3f(  0.0f,   0.0f,   0.0f)), // head
		new ItemTransformVec3f(new Vector3f(30.0f, 315.0f, 0.0f), new Vector3f(0.0f,     0.0f, 0.0f), new Vector3f(0.625f, 0.625f, 0.625f)), // gui
		new ItemTransformVec3f(new Vector3f( 0.0f,   0.0f, 0.0f), new Vector3f(0.0f,  0.1875f, 0.0f), new Vector3f( 0.25f,  0.25f,  0.25f)), // ground
		new ItemTransformVec3f(new Vector3f( 0.0f,   0.0f, 0.0f), new Vector3f(0.5f,     0.5f, 0.5f), new Vector3f(  0.0f,   0.0f,   0.0f))); // fixed
	
}
