package carpentersblocks.renderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import carpentersblocks.block.BlockCoverable;
import carpentersblocks.data.Slope;
import carpentersblocks.renderer.helper.FancyFluidsHelper;
import carpentersblocks.renderer.helper.LightingHelper;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockHandlerBase implements ISimpleBlockRenderingHandler {

    protected static final int DOWN  = 0;
    protected static final int UP    = 1;
    protected static final int NORTH = 2;
    protected static final int SOUTH = 3;
    protected static final int WEST  = 4;
    protected static final int EAST  = 5;
    
    public RenderBlocks      renderBlocks;
    protected LightingHelper lightingHelper = LightingHelper.instance;
    public Block             srcBlock;
    public TEBase            TE;

    protected boolean        hasMetadataOverride;
    protected boolean        suppressOverlay;
    protected boolean        suppressPattern;
    public boolean           suppressDyeColor;
    protected boolean        disableAO;
    public boolean           hasDyeOverride;
    protected int            metadataOverride;
    public int               dyeOverride;
    
    protected boolean[]      hasIconOverride     = new boolean[6];
    protected IIcon[]        iconOverride        = new IIcon[6];
    
    /** 0-5 are side covers, with 6 being the block itself. */
    public int               coverRendering      = 6;
    
    /** Returns whether side is sloped face. */
    public boolean           isSideSloped;
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        IIcon icon_XN = renderBlocks.getBlockIconFromSide(block, 0);
        IIcon icon_XP = renderBlocks.getBlockIconFromSide(block, 1);
        IIcon icon_YN = renderBlocks.getBlockIconFromSide(block, 2);
        IIcon icon_YP = renderBlocks.getBlockIconFromSide(block, 3);
        IIcon icon_ZN = renderBlocks.getBlockIconFromSide(block, 4);
        IIcon icon_ZP = renderBlocks.getBlockIconFromSide(block, 5);
        
        if (block instanceof BlockCoverable) {
            icon_XN = renderBlocks.getBlockIconFromSideAndMetadata(block, 0, EventHandler.BLOCKICON_BASE_ID);
            icon_XP = renderBlocks.getBlockIconFromSideAndMetadata(block, 1, EventHandler.BLOCKICON_BASE_ID);
            icon_YN = renderBlocks.getBlockIconFromSideAndMetadata(block, 2, EventHandler.BLOCKICON_BASE_ID);
            icon_YP = renderBlocks.getBlockIconFromSideAndMetadata(block, 3, EventHandler.BLOCKICON_BASE_ID);
            icon_ZN = renderBlocks.getBlockIconFromSideAndMetadata(block, 4, EventHandler.BLOCKICON_BASE_ID);
            icon_ZP = renderBlocks.getBlockIconFromSideAndMetadata(block, 5, EventHandler.BLOCKICON_BASE_ID);
        }
        
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderBlocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon_XN);
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderBlocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon_XP);
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderBlocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon_YN);
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderBlocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon_YP);
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon_ZN);
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderBlocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon_ZP);
        tessellator.draw();
        
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(90.0F, 0.0F, -1.0F, 0.0F);
    }
    
    @Override
    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block block, int modelID, RenderBlocks renderBlocks)
    {
        VertexHelper.drawCount = 0;
        
        TE = (TEBase) blockAccess.getTileEntity(x, y, z);

        /*
         * A catch-all for bad render calls.  Could happen if tile entities aren't
         * properly loaded when chunks are created, or with multi-block entities
         * like the door or bed when created or destroyed, when TE does not yet exist
         * or has already been removed.
         */
        if (TE != null) {
            
            this.renderBlocks = renderBlocks;
            srcBlock = block;
            lightingHelper.bind(this);

            renderCarpentersBlock(x, y, z);
            renderSideBlocks(x, y, z);
            
            /* Will render a fluid block in this space if valid. */

            if (FeatureRegistry.enableFancyFluids) {
                
                int renderPass = MinecraftForgeClient.getRenderPass();
                
                if (renderPass >= 0 && Minecraft.isFancyGraphicsEnabled() && BlockProperties.hasCover(TE, 6)) {
                    FancyFluidsHelper.render(TE, lightingHelper, renderBlocks, x, y, z, renderPass);
                }
                
            }
            
        }

        return VertexHelper.drawCount > 0;
    }
    
    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }
    
    @Override
    public int getRenderId()
    {
        return 0;
    }
    
    /**
     * Sets directional block side rotation in RenderBlocks.
     */
    protected void setDirectionalRotation(int side)
    {
        int metadata = BlockProperties.getCoverMetadata(TE, coverRendering);
        int dir = metadata & 12;
        
        switch (side)
        {
            case DOWN:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateBottom = 1;
                }
                break;
            case UP:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateTop = 1;
                }
                break;
            case NORTH:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateNorth = 1;
                }
                break;
            case SOUTH:
                if (metadata == 3 || dir == 4) {
                    renderBlocks.uvRotateSouth = 1;
                }
                break;
            case WEST:
                if (metadata == 3 || dir == 8) {
                    renderBlocks.uvRotateWest = 1;
                }
                break;
            case EAST:
                if (metadata == 3 || dir == 8) {
                    renderBlocks.uvRotateEast = 1;
                }
                break;
        }
    }
    
    /**
     * Resets side rotation in RenderBlocks.
     */
    protected void clearRotation(int side)
    {
        switch (side)
        {
            case DOWN:
                renderBlocks.uvRotateBottom = 0;
                break;
            case UP:
                renderBlocks.uvRotateTop = 0;
                break;
            case NORTH:
                renderBlocks.uvRotateNorth = 0;
                break;
            case SOUTH:
                renderBlocks.uvRotateSouth = 0;
                break;
            case WEST:
                renderBlocks.uvRotateWest = 0;
                break;
            case EAST:
                renderBlocks.uvRotateEast = 0;
                break;
        }
    }
    
    /**
     * Sets dye override.
     */
    protected void setDyeOverride(int dye)
    {
        hasDyeOverride = true;
        dyeOverride = dye;
    }
    
    /**
     * Clears dye override.
     */
    protected void clearDyeOverride()
    {
        hasDyeOverride = false;
    }
    
    /**
     * Sets metadata override.
     */
    protected void setMetadataOverride(int metadata)
    {
        hasMetadataOverride = true;
        metadataOverride = metadata;
    }
    
    /**
     * Clears metadata override.
     */
    protected void clearMetadataOverride()
    {
        hasMetadataOverride = false;
    }
    
    /**
     * Sets icon override.
     * Using side 6 overrides all sides.
     * RenderBlocks' icon override will override this one
     * when breaking animation is played.
     */
    protected void setIconOverride(int side, IIcon icon)
    {
        if (side == 6) {
            for (int count = 0; count < 6; ++count) {
                hasIconOverride[count] = true;
                iconOverride[count] = icon;
            }
        } else {
            hasIconOverride[side] = true;
            iconOverride[side] = icon;
        }
    }
    
    /**
     * Clears icon override.
     */
    protected void clearIconOverride(int side)
    {
        if (side == 6) {
            for (int count = 0; count < 6; ++count) {
                hasIconOverride[count] = false;
            }
        } else {
            hasIconOverride[side] = false;
        }
    }

    /**
     * Sets up side cover rendering bounds.
     * Will return block location where side cover should be rendered.
     */
    protected int[] getSideCoverRenderBounds(int x, int y, int z, int side)
    {
        double offset = 1.0D / 16.0D;
        
        /*
         * Make snow match vanilla snow depth for continuity.
         */
        if (side == UP)
        {
            Block block = BlockProperties.getCover(TE, side);
            if (block.equals(Blocks.snow) || block.equals(Blocks.snow_layer)) {
                offset = 1.0D / 8.0D;
            }
        }
        
        /*
         * Adjust bounds of side cover to accommodate
         * partial rendered blocks (slabs, stair steps)
         */
        switch (side) {
            case DOWN:
                if (renderBlocks.renderMinY > 0.0D) {
                    renderBlocks.renderMaxY = renderBlocks.renderMinY;
                    renderBlocks.renderMinY -= offset;
                } else {
                    renderBlocks.renderMaxY = 1.0D;
                    renderBlocks.renderMinY = renderBlocks.renderMaxY - offset;
                    y -= 1;
                }
                break;
            case UP:
                if (renderBlocks.renderMaxY < 1.0D) {
                    renderBlocks.renderMinY = renderBlocks.renderMaxY;
                    renderBlocks.renderMaxY += offset;
                } else {
                    renderBlocks.renderMaxY = offset;
                    renderBlocks.renderMinY = 0.0D;
                    y += 1;
                }
                break;
            case NORTH:
                if (renderBlocks.renderMinZ > 0.0D) {
                    renderBlocks.renderMaxZ = renderBlocks.renderMinZ;
                    renderBlocks.renderMinZ -= offset;
                } else {
                    renderBlocks.renderMaxZ = 1.0D;
                    renderBlocks.renderMinZ = renderBlocks.renderMaxZ - offset;
                    z -= 1;
                }
                break;
            case SOUTH:
                if (renderBlocks.renderMaxZ < 1.0D) {
                    renderBlocks.renderMinZ = renderBlocks.renderMaxZ;
                    renderBlocks.renderMaxZ += offset;
                } else {
                    renderBlocks.renderMaxZ = offset;
                    renderBlocks.renderMinZ = 0.0D;
                    z += 1;
                }
                break;
            case WEST:
                if (renderBlocks.renderMinX > 0.0D) {
                    renderBlocks.renderMaxX = renderBlocks.renderMinX;
                    renderBlocks.renderMinX -= offset;
                } else {
                    renderBlocks.renderMaxX = 1.0D;
                    renderBlocks.renderMinX = renderBlocks.renderMaxX - offset;
                    x -= 1;
                }
                break;
            case EAST:
                if (renderBlocks.renderMaxX < 1.0D) {
                    renderBlocks.renderMinX = renderBlocks.renderMaxX;
                    renderBlocks.renderMaxX += offset;
                } else {
                    renderBlocks.renderMaxX = offset;
                    renderBlocks.renderMinX = 0.0D;
                    x += 1;
                }
                break;
        }
        
        int[] coords = { x, y, z };
        return coords;
    }
    
    /**
     * Renders side covers.
     */
    protected void renderSideBlocks(int x, int y, int z)
    {
        renderBlocks.renderAllFaces = true;
        
        srcBlock.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);
        
        for (int side = 0; side < 6; ++side)
        {
            if (BlockProperties.hasCover(TE, side))
            {
                Block block = BlockProperties.getCover(TE, side);
                coverRendering = side;
                
                int[] renderOffset = getSideCoverRenderBounds(x, y, z, side);
                renderBlock(block, renderOffset[0], renderOffset[1], renderOffset[2]);
                renderBlocks.setRenderBoundsFromBlock(srcBlock);
            }
        }
        
        renderBlocks.renderAllFaces = false;
        coverRendering = 6;
    }
    
    /**
     * Override to provide custom icons.
     * Will be null checked later.
     */
    protected IIcon getUniqueIcon(Block block, int side, IIcon icon)
    {
        return icon;
    }
    
    /**
     * Returns icon for face.
     */
    protected IIcon getIcon(Block block, int side)
    {
        int metadata = hasMetadataOverride ? metadataOverride : BlockProperties.hasCover(TE, coverRendering) ? BlockProperties.getCoverMetadata(TE, coverRendering) : EventHandler.BLOCKICON_BASE_ID;
        
        IIcon icon = renderBlocks.getIconSafe(getUniqueIcon(block, side, block.getIcon(side, metadata)));
        
        if (hasIconOverride[side]) {
            icon = renderBlocks.getIconSafe(iconOverride[side]);
        }
        
        return icon;
    }
    
    /**
     * Renders multiple textures to side.
     */
    protected void renderMultiTexturedSide(Block block, int x, int y, int z, int side, IIcon icon)
    {
        int renderPass = MinecraftForgeClient.getRenderPass();
        int blockRenderPass = block.getRenderBlockPass();
        
        /* Render base block texture. */
        
        if (renderPass == blockRenderPass) {
            
            if (BlockProperties.blockRotates(block)) {
                setDirectionalRotation(side);
            }

            /* Render side */
            
            lightingHelper.colorSide(block, x, y, z, side, icon);
            renderSide(x, y, z, side, icon);
            
            clearRotation(side);
            
            /* Grass sides are a special case. */
            
            if (block.equals(Blocks.grass)) {
                renderGrassSideOverlay(x, y, z, side);
            }
            
        }
        
        boolean temp_dye_state = suppressDyeColor;
        suppressDyeColor = true;
        
        /*
         * Render decorations.
         * 
         * Decorations should always render on the alpha pass.
         * When the base cover renders on alpha as well, we
         * need to adjust rendering depths of decorations to
         * prevent overlapping artifacts.
         */
        
        if (!suppressPattern && BlockProperties.hasPattern(TE, coverRendering)) {
            if (block.getRenderBlockPass() == 1) {
                VertexHelper.setOffset(0.0001D);
            }
            renderPattern(x, y, z, side);
            VertexHelper.clearOffset();
        }
        
        if (!suppressOverlay && BlockProperties.hasOverlay(TE, coverRendering)) {
            if (blockRenderPass == 1) {
                VertexHelper.setOffset(0.0002D);
            }
            renderOverlay(block, x, y, z, side, icon);
            VertexHelper.clearOffset();
        }
        
        suppressDyeColor = temp_dye_state;
    }
    
    /**
     * Sets side icon, draws any attributes needed, and hands to appropriate render method.
     * This will bypass typical draw behavior if breaking animation is being drawn.
     */
    protected void delegateSideRender(Block block, int x, int y, int z, int side)
    {
        /*
         * A texture override in the context of this mod indicates the breaking
         * animation is being drawn. If this is the case, draw side without
         * decorations. Can also check Icon name for beginsWith("destroy_stage_").
         */
        if (renderBlocks.hasOverrideBlockTexture()) {
            lightingHelper.colorSide(block, x, y, z, side, renderBlocks.overrideBlockTexture);
            renderSide(x, y, z, side, renderBlocks.overrideBlockTexture);
        } else {
            renderMultiTexturedSide(block, x, y, z, side, getIcon(block, side));
        }
    }
    
    /**
     * Renders forced grass overlay on side.
     */
    protected void renderGrassSideOverlay(int x, int y, int z, int side)
    {
        if (side > UP)
        {
            IIcon icon = getGrassOverlayIcon(side);
            lightingHelper.colorSide(Blocks.grass, x, y, z, side, icon);
            renderSide(x, y, z, side, icon);
        }
    }
    
    /**
     * Renders overlay on side.
     */
    protected void renderOverlay(Block block, int x, int y, int z, int side, IIcon icon)
    {
        if (isSideSloped && Slope.slopesList[BlockProperties.getMetadata(TE)].isPositive) {
            side = UP;
        }

        switch (OverlayHandler.getOverlay(BlockProperties.getOverlay(TE, coverRendering))) {
            case SNOW: {
                
                switch (side) {
                    case DOWN:
                        return;
                    case UP:
                        icon = Blocks.snow.getBlockTextureFromSide(1);
                        break;
                    default:
                        icon = IconRegistry.icon_overlay_snow_side;
                        break;
                }
                lightingHelper.colorSide(Blocks.snow, x, y, z, side, icon);
                
                break;
            }
            case HAY: {
                
                switch (side) {
                    case DOWN:
                        return;
                    case UP:
                        icon = Blocks.hay_block.getBlockTextureFromSide(1);
                        break;
                    default:
                        icon = IconRegistry.icon_overlay_hay_side;
                        break;
                }
                lightingHelper.colorSide(Blocks.hay_block, x, y, z, side, icon);
                
                break;
            }
            case WEB: {
                
                icon = Blocks.web.getBlockTextureFromSide(side);
                lightingHelper.colorSide(Blocks.web, x, y, z, side, icon);
                
                break;
            }
            case VINE: {
                
                icon = Blocks.vine.getBlockTextureFromSide(side);
                lightingHelper.colorSide(Blocks.vine, x, y, z, side, icon);
                
                break;
            }
            case MYCELIUM: {
                
                switch (side) {
                    case DOWN:
                        return;
                    case UP:
                        icon = Blocks.mycelium.getBlockTextureFromSide(1);
                        break;
                    default:
                        icon = IconRegistry.icon_overlay_mycelium_side;
                        break;
                }
                lightingHelper.colorSide(Blocks.mycelium, x, y, z, side, icon);
                
                break;
            }
            case GRASS: {
                
                if (block != Blocks.grass && side > DOWN) {
                    icon = getGrassOverlayIcon(side);
                    lightingHelper.colorSide(Blocks.grass, x, y, z, side, icon);
                }

                break;
            }
            default:
                break;
        }
        
        renderSide(x, y, z, side, icon);
    }
    
    /**
     * Returns grass overlay icon.
     */
    protected IIcon getGrassOverlayIcon(int side)
    {
        boolean isPositiveSlope = isSideSloped ? Slope.slopesList[BlockProperties.getMetadata(TE)].isPositive : false;
        
        IIcon icon = BlockGrass.getIconSideOverlay();
        
        if (side == UP || isPositiveSlope) {
            icon = Blocks.grass.getBlockTextureFromSide(1);
        } else {
            
            /*
             * When FAST graphics are used, grass blocks use a single
             * texture to draw each side.
             *
             * Because our implementation of grass sides requires
             * two render passes, and it must be kept separate to drape
             * off of our custom blocks, we must draw icon_fast_grass to
             * mimic the look of vanilla grass blocks.
             */
            if (!RenderBlocks.fancyGrass) {
                icon = IconRegistry.icon_overlay_fast_grass_side;
            }
        }
        
        return icon;
    }
    
    /**
     * Renders pattern on side.
     */
    protected void renderPattern(int x, int y, int z, int side)
    {
        int pattern = BlockProperties.getPattern(TE, coverRendering);
        IIcon icon = renderBlocks.getIconSafe(IconRegistry.icon_pattern[pattern]);
        
        lightingHelper.colorSide(Blocks.glass, x, y, z, side, icon);
        renderSide(x, y, z, side, icon);
    }
    
    /**
     * Renders side.
     */
    protected void renderSide(int x, int y, int z, int side, IIcon icon)
    {
        switch (side) {
            case DOWN:
                RenderHelper.renderFaceYNeg(renderBlocks, x, y, z, icon);
                break;
            case UP:
                RenderHelper.renderFaceYPos(renderBlocks, x, y, z, icon);
                break;
            case NORTH:
                RenderHelper.renderFaceZNeg(renderBlocks, x, y, z, icon);
                break;
            case SOUTH:
                RenderHelper.renderFaceZPos(renderBlocks, x, y, z, icon);
                break;
            case WEST:
                RenderHelper.renderFaceXNeg(renderBlocks, x, y, z, icon);
                break;
            case EAST:
                RenderHelper.renderFaceXPos(renderBlocks, x, y, z, icon);
                break;
        }
    }
    
    /**
     * Blocks override this in order to render everything they need.
     */
    protected void renderCarpentersBlock(int x, int y, int z)
    {
        renderBlock(BlockProperties.getCover(TE, 6), x, y, z);
    }
    
    /**
     * Sets renderBlocks enableAO state to true depending on
     * rendering environment and block requirements.
     */
    protected boolean getEnableAO(Block block)
    {
        return Minecraft.isAmbientOcclusionEnabled() && !disableAO && block.getLightValue() == 0;
    }
    
    /**
     * Renders block.
     * Coordinates may change since side covers render here.
     */
    protected void renderBlock(Block block, int x, int y, int z)
    {
        renderBlocks.enableAO = getEnableAO(block);
        
        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN) || renderBlocks.renderMinY > 0.0D)
        {
            lightingHelper.setLightingYNeg(block, x, y, z);
            delegateSideRender(block, x, y, z, DOWN);
        }
        
        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP) || renderBlocks.renderMaxY < 1.0D)
        {
            lightingHelper.setLightingYPos(block, x, y, z);
            delegateSideRender(block, x, y, z, UP);
        }
        
        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH) || renderBlocks.renderMinZ > 0.0D)
        {
            lightingHelper.setLightingZNeg(block, x, y, z);
            delegateSideRender(block, x, y, z, NORTH);
        }
        
        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH) || renderBlocks.renderMaxZ < 1.0D)
        {
            lightingHelper.setLightingZPos(block, x, y, z);
            delegateSideRender(block, x, y, z, SOUTH);
        }
        
        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST) || renderBlocks.renderMinX > 0.0D)
        {
            lightingHelper.setLightingXNeg(block, x, y, z);
            delegateSideRender(block, x, y, z, WEST);
        }
        
        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST) || renderBlocks.renderMaxX < 1.0D)
        {
            lightingHelper.setLightingXPos(block, x, y, z);
            delegateSideRender(block, x, y, z, EAST);
        }
        
        renderBlocks.enableAO = false;
    }
    
}
