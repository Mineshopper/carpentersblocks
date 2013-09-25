package carpentersblocks.renderer.helper;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;

public class RenderHelper extends VertexHelper {

	/**
	 * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY - offset;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;
		
        switch (renderBlocks.uvRotateBottom)
		{
			case 0:
			{
		        double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		        double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
		        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
	
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2); // BR
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3); // TR
	
				break;
			}
			case 1:
			{
		        double uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
		        double uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
		        double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
		        double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
	
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 2); // BR
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMax, 3); // TR
	
				break;
			}
			case 2:
			{
		        double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
		        double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
		        double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
		        double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
	
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMax, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2); // BR
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 3); // TR
	
				break;
			}
			case 3:
			{
		        double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
		        double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
		        double vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
		        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
	
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 2); // BR
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMax, 3); // TR
	
				break;
			}
		}
	}

	/**
	 * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
	 */
	public static void renderFaceYPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{        
        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMax = y + renderBlocks.renderMaxY + offset;
        double zMin = z + renderBlocks.renderMinZ;
        double zMax = z + renderBlocks.renderMaxZ;
		
        switch (renderBlocks.uvRotateTop)
		{
			case 0:
			{
		        double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		        double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
		        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
	
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1); // BR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3); // TL
	
				break;
			}
			case 1:
			{
		        double uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
		        double uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
		        double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
		        double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
	
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 1); // BR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 3); // TL
	
				break;
			}
			case 2:
			{
		        double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
		        double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
		        double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
		        double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
	
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMin, 1); // BR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 3); // TL
	
				break;
			}
			case 3:
			{
		        double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
		        double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
		        double vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
		        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
	
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 1); // BR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 3); // TL
	
				break;
			}
		}
	}

	/**
	 * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{        
        double xMin = x + renderBlocks.renderMinX;
        double xMax = x + renderBlocks.renderMaxX;
        double yMin = y + renderBlocks.renderMinY;
        double yMax = y + renderBlocks.renderMaxY;
        double zMin = z + renderBlocks.renderMinZ - offset;
		
        switch (renderBlocks.uvRotateEast)
		{
			case 0:
			{
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
		        double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
		        double vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);
		        double vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
	
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1); // TL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 3); // BR
	
				break;
			}
			case 1:
			{
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
				double vMax = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
				
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMin, 1); // TL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, 3); // BR
	
				break;
			}
			case 2:
			{
				double uMin = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
		        double uMax = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
		        double vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
		        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);
	
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMax, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 1); // TL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMin, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 3); // BR
	
				break;
			}
			case 3:
			{
				double uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
				double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
				
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 0); // TR
		        setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMin, 1); // TL
		        setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMax, 3); // BR
	
				break;
			}
		}
	}

	/**
	 * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		double xMin = x + renderBlocks.renderMinX;
		double xMax = x + renderBlocks.renderMaxX;
		double yMin = y + renderBlocks.renderMinY;
		double yMax = y + renderBlocks.renderMaxY;
		double zMax = z + renderBlocks.renderMaxZ + offset;

		switch (renderBlocks.uvRotateWest)
		{
			case 0:
			{				
				double uMin = icon.getInterpolatedU(renderBlocks.renderMinX * 16.0D);
				double uMax = icon.getInterpolatedU(renderBlocks.renderMaxX * 16.0D);
				double vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);
		        double vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);

		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 2); // BR
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3); // TR
	
				break;
			}
			case 1:
			{
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinX * 16.0D);
				double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxX * 16.0D);
				
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 2); // BR
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3); // TR
	
				break;
			}
			case 2:
			{
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinX * 16.0D);
				double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxX * 16.0D);
				double vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
				double vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);

		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMin, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 2); // BR
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMax, vMax, 3); // TR
	
				break;
			}
			case 3:
			{
				double uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(renderBlocks.renderMinX * 16.0D);
				double vMax = icon.getInterpolatedV(renderBlocks.renderMaxX * 16.0D);
				
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMin, 0); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 1); // BL
		        setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMax, 2); // BR
		        setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3); // TR
	
				break;
			}
		}
	}

	/**
	 * Renders the given texture to the West face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		double xMin = x + renderBlocks.renderMinX - offset;
		double yMin = y + renderBlocks.renderMinY;
		double yMax = y + renderBlocks.renderMaxY;
		double zMin = z + renderBlocks.renderMinZ;
		double zMax = z + renderBlocks.renderMaxZ;
		
		switch (renderBlocks.uvRotateNorth)
		{
			case 0:
			{
				double uMin = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
		        double uMax = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
		        double vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
		        double vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);
		        
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0); // TR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3); // BR
	
				break;
			}
			case 1:
			{
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
				double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
				
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0); // TR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 1); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMax, 3); // BR
	
				break;
			}
			case 2:
			{
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
		        double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
		        double vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
		        double vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);
	
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMax, vMax, 0); // TR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMax, 1); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMin, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMin, 3); // BR
	
				break;
			}
			case 3:
			{
				double uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
				double vMax = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
				
		        setupVertex(renderBlocks, xMin, yMax, zMax, uMin, vMax, 0); // TR
		        setupVertex(renderBlocks, xMin, yMax, zMin, uMin, vMin, 1); // TL
		        setupVertex(renderBlocks, xMin, yMin, zMin, uMax, vMin, 2); // BL
		        setupVertex(renderBlocks, xMin, yMin, zMax, uMax, vMax, 3); // BR
	
				break;
			}
		}
	}

	/**
	 * Renders the given texture to the East face of the block.  Args: slope, x, y, z, texture
	 */
	public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, Icon icon)
	{
		double xMax = x + renderBlocks.renderMaxX + offset;
		double yMin = y + renderBlocks.renderMinY;
		double yMax = y + renderBlocks.renderMaxY;
		double zMin = z + renderBlocks.renderMinZ;
		double zMax = z + renderBlocks.renderMaxZ;

		switch (renderBlocks.uvRotateSouth)
		{
			case 0:
			{				
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxZ * 16.0D);
				double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinZ * 16.0D);
			    double vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderBlocks.renderMaxY) * 16.0D);
			    double vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? (1.0D - (renderBlocks.renderMaxY - renderBlocks.renderMinY)) : renderBlocks.renderMinY) * 16.0D);
				
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0); // BL
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 1); // BR
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2); // TR
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3); // TL
	
				break;
			}
			case 1:
			{
				double uMin = icon.getInterpolatedU(16.0D - renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(16.0D - renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(renderBlocks.renderMaxZ * 16.0D);
				double vMax = icon.getInterpolatedV(renderBlocks.renderMinZ * 16.0D);
				
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 0); // BL
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 1); // BR
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 2); // TR
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, 3); // TL
	
				break;
			}
			case 2:
			{
				double uMin = icon.getInterpolatedU(renderBlocks.renderMaxZ * 16.0D);
				double uMax = icon.getInterpolatedU(renderBlocks.renderMinZ * 16.0D);
				double vMin = icon.getInterpolatedV(renderBlocks.renderMinY * 16.0D);
				double vMax = icon.getInterpolatedV(renderBlocks.renderMaxY * 16.0D);
	
				setupVertex(renderBlocks, xMax, yMin, zMax, uMin, vMin, 0); // BL
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMin, 1); // BR
				setupVertex(renderBlocks, xMax, yMax, zMin, uMax, vMax, 2); // TR
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMax, 3); // TL
	
				break;
			}
			case 3:
			{
				double uMin = icon.getInterpolatedU(renderBlocks.renderMaxY * 16.0D);
				double uMax = icon.getInterpolatedU(renderBlocks.renderMinY * 16.0D);
				double vMin = icon.getInterpolatedV(16.0D - renderBlocks.renderMaxZ * 16.0D);
				double vMax = icon.getInterpolatedV(16.0D - renderBlocks.renderMinZ * 16.0D);
				
				setupVertex(renderBlocks, xMax, yMin, zMax, uMax, vMin, 0); // BL
				setupVertex(renderBlocks, xMax, yMin, zMin, uMax, vMax, 1); // BR
				setupVertex(renderBlocks, xMax, yMax, zMin, uMin, vMax, 2); // TR
				setupVertex(renderBlocks, xMax, yMax, zMax, uMin, vMin, 3); // TL
	
				break;
			}
		}
	}
	
}