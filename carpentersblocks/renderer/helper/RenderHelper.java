package carpentersblocks.renderer.helper;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper extends VertexHelper {
    
    /** Tessellator draw mode for triangles. */
    public final static int TRIANGLES = GL11.GL_TRIANGLES;
    
    /** Tessellator draw mode for quads. */
    public final static int QUADS = GL11.GL_QUADS;
    
    private static boolean rotationOverride = false;
    private static int     rotation;
    
    private static double uMin;
    private static double uMax;
    private static double vMin;
    private static double vMax;
    
    protected static double xMin;
    protected static double xMax;
    protected static double yMin;
    protected static double yMax;
    protected static double zMin;
    protected static double zMax;
    
    protected static double uTL;
    protected static double vTL;
    protected static double uBL;
    protected static double vBL;
    protected static double uBR;
    protected static double vBR;
    protected static double uTR;
    protected static double vTR;
    
    public static void setRotationOverride(int in_rotation)
    {
        rotationOverride = true;
        rotation = in_rotation;
    }
    
    public static void clearRotationOverride()
    {
        rotationOverride = false;
    }
    
    /**
     * Sets block bounds for rendering without a RenderBlocks object.
     */
    public static void setBounds(double in_xMin, double in_yMin, double in_zMin, double in_xMax, double in_yMax, double in_zMax)
    {
        xMin = in_xMin;
        yMin = in_yMin;
        zMin = in_zMin;
        xMax = in_xMax;
        yMax = in_yMax;
        zMax = in_zMax;
    }
    
    /**
     * Sets draw mode in tessellator.
     */
    public static void startDrawing(int drawMode)
    {
        Tessellator.instance.draw();
        Tessellator.instance.startDrawing(drawMode);
    }
    
    /**
     * Sets UV coordinates for each corner based on side rotation.
     */
    private static void setCornerUV(double t_uTL, double t_vTL, double t_uBL, double t_vBL, double t_uBR, double t_vBR, double t_uTR, double t_vTR)
    {
        uTL = t_uTL;
        vTL = t_vTL;
        uBL = t_uBL;
        vBL = t_vBL;
        uBR = t_uBR;
        vBR = t_vBR;
        uTR = t_uTR;
        vTR = t_vTR;
    }
    
    /**
     * Will populate render bounds and icon u, v translations.
     */
    protected static void prepareRender(RenderBlocks renderBlocks, ForgeDirection side, double x, double y, double z, IIcon icon)
    {
        boolean customBounds = renderBlocks == null;
        
        /* Set render bounds with offset. */

        if (!customBounds)
        {
            xMin = x + renderBlocks.renderMinX - (side.equals(ForgeDirection.WEST)  ? offset : 0);
            xMax = x + renderBlocks.renderMaxX + (side.equals(ForgeDirection.EAST)  ? offset : 0);
            yMin = y + renderBlocks.renderMinY - (side.equals(ForgeDirection.DOWN)  ? offset : 0);
            yMax = y + renderBlocks.renderMaxY + (side.equals(ForgeDirection.UP)    ? offset : 0);
            zMin = z + renderBlocks.renderMinZ - (side.equals(ForgeDirection.NORTH) ? offset : 0);
            zMax = z + renderBlocks.renderMaxZ + (side.equals(ForgeDirection.SOUTH) ? offset : 0);
        }
        
        double renderMinX = customBounds ? xMin : renderBlocks.renderMinX;
        double renderMinY = customBounds ? yMin : renderBlocks.renderMinY;
        double renderMinZ = customBounds ? zMin : renderBlocks.renderMinZ;
        double renderMaxX = customBounds ? xMax : renderBlocks.renderMaxX;
        double renderMaxY = customBounds ? yMax : renderBlocks.renderMaxY;
        double renderMaxZ = customBounds ? zMax : renderBlocks.renderMaxZ;
        
        /* Set u, v for icon with rotation. */
        
        int rot = 0;
        
        if (!rotationOverride) {
            switch (side) {
                case DOWN:
                    rot = renderBlocks.uvRotateBottom;
                    break;
                case UP:
                    rot = renderBlocks.uvRotateTop;
                    break;
                case NORTH:
                    rot = renderBlocks.uvRotateNorth;
                    break;
                case SOUTH:
                    rot = renderBlocks.uvRotateSouth;
                    break;
                case WEST:
                    rot = renderBlocks.uvRotateWest;
                    break;
                case EAST:
                    rot = renderBlocks.uvRotateEast;
                    break;
                default: {}
            }
        } else {
            rot = rotation;
        }
        
        switch (side) {
            
            case DOWN:
                
                switch (rot) {
                    
                    case 0:
                        
                        uMin = icon.getInterpolatedU(renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxZ * 16.0D);
                        
                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);
                        
                        break;
                        
                    case 1:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxX * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);
                        
                        break;
                        
                    case 2:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
                        
                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);
                        
                        break;
                        
                    case 3:
                        
                        uMin = icon.getInterpolatedU(renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMaxX * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);
                        
                        break;
                        
                }
                
                break;
                
            case UP:
                
                switch (rot) {
                    
                    case 0:
                        
                        uMin = icon.getInterpolatedU(renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxZ * 16.0D);
                        
                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);
                        
                        break;
                        
                    case 1:
                        
                        uMin = icon.getInterpolatedU(renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMaxX * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);
                        
                        break;
                        
                    case 2:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
                        
                        setCornerUV(uMax, vMax, uMax, vMin, uMin, vMin, uMin, vMax);
                        
                        break;
                        
                    case 3:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxX * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMax, vMax, uMax, vMin, uMin, vMin);
                        
                        break;
                        
                }
                
                break;
                
            case NORTH:
                
                switch (rot) {
                    
                    case 0:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderMaxY - renderMinY) : renderMinY) * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderMaxY) * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 1:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderMaxX * 16.0D);
                        vMax = icon.getInterpolatedV(renderMinX * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                    case 2:
                        
                        uMin = icon.getInterpolatedU(renderMaxX * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinX * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxY * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 3:
                        
                        uMin = icon.getInterpolatedU(renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMaxX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMinX * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                }
                
                break;
                
            case SOUTH:
                
                switch (rot) {
                    
                    case 0:
                        
                        uMin = icon.getInterpolatedU(renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderMaxY - renderMinY) : renderMinY) * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderMaxY) * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 1:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMaxX * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                    case 2:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMinX * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMaxX * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxY * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 3:
                        
                        uMin = icon.getInterpolatedU(renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinX * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxX * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                }
                
                break;
                
            case WEST:
                
                switch (rot) {
                    
                    case 0:
                        
                        uMin = icon.getInterpolatedU(renderMinZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderMaxZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderMaxY) * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderMaxY - renderMinY) : renderMinY) * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 1:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                    case 2:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMinZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxY * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 3:
                        
                        uMin = icon.getInterpolatedU(renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxZ * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                }
                
                break;
                
            case EAST:
                
                switch (rot) {
                    
                    case 0:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D : renderMaxY) * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - (iconHasFloatingHeight(icon) ? 1.0D - (renderMaxY - renderMinY) : renderMinY) * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 1:
                        
                        uMin = icon.getInterpolatedU(16.0D - renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(16.0D - renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(renderMaxZ * 16.0D);
                        vMax = icon.getInterpolatedV(renderMinZ * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                    case 2:
                        
                        uMin = icon.getInterpolatedU(renderMaxZ * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinZ * 16.0D);
                        vMin = icon.getInterpolatedV(renderMinY * 16.0D);
                        vMax = icon.getInterpolatedV(renderMaxY * 16.0D);
                        
                        setCornerUV(uMin, vMax, uMin, vMin, uMax, vMin, uMax, vMax);
                        
                        break;
                        
                    case 3:
                        
                        uMin = icon.getInterpolatedU(renderMaxY * 16.0D);
                        uMax = icon.getInterpolatedU(renderMinY * 16.0D);
                        vMin = icon.getInterpolatedV(16.0D - renderMaxZ * 16.0D);
                        vMax = icon.getInterpolatedV(16.0D - renderMinZ * 16.0D);
                        
                        setCornerUV(uMin, vMin, uMax, vMin, uMax, vMax, uMin, vMax);
                        
                        break;
                        
                }
                
                break;
                
            default: {}
            
        }
    }
    
    /**
     * Renders the given texture to the bottom face of the block. Args: slope, x, y, z, texture
     */
    public static void renderFaceYNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.DOWN, x, y, z, icon);

        setupVertex(renderBlocks, xMin, yMin, zMax, uTR, vTR, SOUTHWEST);
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, NORTHWEST);
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, NORTHEAST);
        setupVertex(renderBlocks, xMax, yMin, zMax, uTL, vTL, SOUTHEAST);
    }
    
    /**
     * Renders the given texture to the top face of the block. Args: slope, x, y, z, texture
     */
    public static void renderFaceYPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.UP, x, y, z, icon);
        
        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, SOUTHEAST);
        setupVertex(renderBlocks, xMax, yMax, zMin, uBL, vBL, NORTHEAST);
        setupVertex(renderBlocks, xMin, yMax, zMin, uBR, vBR, NORTHWEST);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, SOUTHWEST);
    }
    
    /**
     * Renders the given texture to the North face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceZNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.NORTH, x, y, z, icon);

        setupVertex(renderBlocks, xMax, yMax, zMin, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMin, uTR, vTR, TOP_RIGHT   );
    }
    
    /**
     * Renders the given texture to the South face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceZPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.SOUTH, x, y, z, icon);
        
        setupVertex(renderBlocks, xMin, yMax, zMax, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMax, uTR, vTR, TOP_RIGHT   );
    }
    
    /**
     * Renders the given texture to the West face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceXNeg(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.WEST, x, y, z, icon);
        
        setupVertex(renderBlocks, xMin, yMax, zMin, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMin, yMin, zMin, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMin, yMin, zMax, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMin, yMax, zMax, uTR, vTR, TOP_RIGHT   );
    }
    
    /**
     * Renders the given texture to the East face of the block.  Args: slope, x, y, z, texture
     */
    public static void renderFaceXPos(RenderBlocks renderBlocks, double x, double y, double z, IIcon icon)
    {
        prepareRender(renderBlocks, ForgeDirection.EAST, x, y, z, icon);
        
        setupVertex(renderBlocks, xMax, yMax, zMax, uTL, vTL, TOP_LEFT    );
        setupVertex(renderBlocks, xMax, yMin, zMax, uBL, vBL, BOTTOM_LEFT );
        setupVertex(renderBlocks, xMax, yMin, zMin, uBR, vBR, BOTTOM_RIGHT);
        setupVertex(renderBlocks, xMax, yMax, zMin, uTR, vTR, TOP_RIGHT   );
    }
    
}
