package carpentersblocks.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
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

    protected int PASS_OPAQUE = 0;
    protected int PASS_ALPHA  = 1;

    protected static final int DOWN  = 0;
    protected static final int UP    = 1;
    protected static final int NORTH = 2;
    protected static final int SOUTH = 3;
    protected static final int WEST  = 4;
    protected static final int EAST  = 5;

    public RenderBlocks      renderBlocks;
    protected LightingHelper lightingHelper = LightingHelper.instance;
    private Minecraft        minecraft = FMLClientHandler.instance().getClient();
    protected int            renderPass;
    public Block             srcBlock;
    public TEBase            TE;

    protected boolean        renderAlphaOverride = FeatureRegistry.enableZFightingFix;
    protected boolean        hasMetadataOverride;
    protected boolean        suppressOverlay;
    protected boolean        suppressPattern;
    public boolean           suppressDyeColor;
    protected boolean        disableAO;
    public boolean           hasDyeColorOverride;
    protected int            metadataOverride;
    public int               dyeColorOverride;

    protected boolean[]      hasIconOverride     = new boolean[6];
    protected Icon[]         iconOverride        = new Icon[6];

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

        Icon icon_XN = renderBlocks.getBlockIconFromSide(block, 0);
        Icon icon_XP = renderBlocks.getBlockIconFromSide(block, 1);
        Icon icon_YN = renderBlocks.getBlockIconFromSide(block, 2);
        Icon icon_YP = renderBlocks.getBlockIconFromSide(block, 3);
        Icon icon_ZN = renderBlocks.getBlockIconFromSide(block, 4);
        Icon icon_ZP = renderBlocks.getBlockIconFromSide(block, 5);

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
    public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block srcBlock, int modelID, RenderBlocks renderBlocks)
    {
        TE = (TEBase) blockAccess.getBlockTileEntity(x, y, z);

        boolean result = false;

        /*
         * A catch-all for bad render calls.  Could happen if tile entities aren't
         * properly loaded when chunks are created, or with multi-block entities
         * like the door or bed when created or destroyed, when TE does not yet exist
         * or has already been removed.
         */
        if (TE != null) {

            this.renderBlocks = renderBlocks;
            renderPass = MinecraftForgeClient.getRenderPass();
            this.srcBlock = srcBlock;

            lightingHelper.bind(this);

            if (renderCarpentersBlock(x, y, z)) {
                result = true;
            }

            if (renderSideBlocks(x, y, z)) {
                result = true;
            }

            /* Will render a fluid block in this space if valid. */

            if (FeatureRegistry.enableFancyFluids) {
                if (renderPass >= 0 && Minecraft.isFancyGraphicsEnabled() && BlockProperties.hasCover(TE, 6)) {
                    if (FancyFluidsHelper.render(TE, lightingHelper, renderBlocks, x, y, z, renderPass)) {
                        result = true;
                    }
                }
            }

        }

        return result;
    }

    @Override
    public boolean shouldRender3DInInventory()
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
     * Sets dye color override.
     */
    protected void setDyeColorOverride(int dyeColor)
    {
        hasDyeColorOverride = true;
        dyeColorOverride = dyeColor;
    }

    /**
     * Clears dye color override.
     */
    protected void clearDyeColorOverride()
    {
        hasDyeColorOverride = false;
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
    protected void setIconOverride(int side, Icon icon)
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
     * Returns whether block should render this pass.
     */
    protected boolean shouldRenderBlock(Block block)
    {
        if (renderBlocks.hasOverrideBlockTexture()) {
            return true;
        } else if (renderAlphaOverride) {
            return renderPass == PASS_ALPHA;
        } else {
            return  block.getRenderBlockPass() == renderPass ||
                    shouldRenderPattern() ||
                    shouldRenderOverlay(block) ||
                    block instanceof BlockCoverable && renderPass == PASS_OPAQUE;
        }
    }

    /**
     * Returns whether opaque objects should render.
     * Objects should not be decorated.
     */
    protected boolean shouldRenderOpaque()
    {
        return renderAlphaOverride && renderPass == PASS_ALPHA || renderPass == PASS_OPAQUE;
    }

    /**
     * Returns whether alpha objects should render.
     */
    protected boolean shouldRenderAlpha()
    {
        return renderPass == PASS_ALPHA;
    }

    /**
     * Returns whether overlay should render.
     */
    protected boolean shouldRenderOverlay(Block block)
    {
        if (!suppressOverlay)
        {
            if (BlockProperties.hasOverlay(TE, coverRendering))
            {
                int coverPass = block.getRenderBlockPass();

                if (renderAlphaOverride) {
                    return renderPass == PASS_ALPHA;
                } else {
                    if (block instanceof BlockCoverable) {
                        return true;
                    } else if (coverPass == PASS_ALPHA) {
                        return true;
                    } else if (shouldRenderPattern()) {
                        return renderPass == PASS_ALPHA;
                    } else {
                        return coverPass == renderPass;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns whether pattern should render.
     */
    protected boolean shouldRenderPattern()
    {
        if (!suppressPattern) {
            if (renderPass == PASS_ALPHA) {
                return BlockProperties.hasPattern(TE, coverRendering);
            }
        }

        return false;
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
            int blockID = BlockProperties.getCoverID(TE, side);
            if (blockID == Block.blockSnow.blockID || blockID == Block.snow.blockID) {
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
    protected boolean renderSideBlocks(int x, int y, int z)
    {
        boolean side_rendered = false;
        renderBlocks.renderAllFaces = true;

        srcBlock.setBlockBoundsBasedOnState(renderBlocks.blockAccess, x, y, z);

        for (int side = 0; side < 6; ++side)
        {
            if (BlockProperties.hasCover(TE, side))
            {
                Block block = BlockProperties.getCoverBlock(TE, side);
                coverRendering = side;

                if (shouldRenderBlock(block) || shouldRenderPattern())
                {
                    int[] renderOffset = getSideCoverRenderBounds(x, y, z, side);
                    renderBlock(block, renderOffset[0], renderOffset[1], renderOffset[2]);
                    renderBlocks.setRenderBoundsFromBlock(srcBlock);
                }
            }
        }

        renderBlocks.renderAllFaces = false;
        coverRendering = 6;
        return side_rendered;
    }

    /**
     * Override to provide custom icons.
     * Will be null checked later.
     */
    protected Icon getUniqueIcon(Block block, int side, Icon icon)
    {
        return icon;
    }

    /**
     * Returns icon for face.
     */
    protected Icon getIcon(Block block, int side)
    {
        int metadata = hasMetadataOverride ? metadataOverride : BlockProperties.hasCover(TE, coverRendering) ? BlockProperties.getCoverMetadata(TE, coverRendering) : EventHandler.BLOCKICON_BASE_ID;

        Icon icon = renderBlocks.getIconSafe(getUniqueIcon(block, side, block.getIcon(side, metadata)));

        if (hasIconOverride[side]) {
            icon = renderBlocks.getIconSafe(iconOverride[side]);
        }

        return icon;
    }

    /**
     * Renders multiple textures to side.
     */
    protected void renderMultiTexturedSide(Block block, int x, int y, int z, int side, Icon icon)
    {
        boolean temp_dye_state = suppressDyeColor;

        /* Render side */
        if (shouldRenderBlock(block))
        {
            if (BlockProperties.blockRotates(block)) {
                setDirectionalRotation(side);
            }

            lightingHelper.colorSide(block, x, y, z, side, icon);
            renderSide(x, y, z, side, 0.0D, icon);
            clearRotation(side);
        }

        suppressDyeColor = true;

        /* Grass sides are a special case. */
        if (block.equals(Block.grass)) {
            renderGrassSideOverlay(x, y, z, side);
        }

        /* Render pattern on side. */

        if (shouldRenderPattern()) {
            renderPattern(x, y, z, side);
        }

        /* Render overlay on side. */
        
        if (shouldRenderOverlay(block)) {
            renderOverlay(block, x, y, z, side, icon);
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
            renderSide(x, y, z, side, 0.0D, renderBlocks.overrideBlockTexture);
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
            if (renderAlphaOverride && renderPass == PASS_ALPHA || !renderAlphaOverride && renderPass == PASS_OPAQUE)
            {
                Icon icon = getGrassOverlayIcon(side);
                lightingHelper.colorSide(Block.grass, x, y, z, side, icon);
                renderSide(x, y, z, side, 0.0D, icon);
            }
        }
    }

    /**
     * Renders overlay on side.
     */
    protected void renderOverlay(Block block, int x, int y, int z, int side, Icon icon)
    {
        int overlay = BlockProperties.getOverlay(TE, coverRendering);

        if (isSideSloped && Slope.slopesList[BlockProperties.getData(TE)].isPositive) {
            side = UP;
        }

        switch (overlay) {
            case OverlayHandler.OVERLAY_SNOW: {
                switch (side) {
                    case DOWN:
                        return;
                    case UP:
                        icon = Block.snow.getBlockTextureFromSide(1);
                        break;
                    default:
                        icon = IconRegistry.icon_overlay_snow_side;
                        break;
                }
                lightingHelper.colorSide(Block.blockSnow, x, y, z, side, icon);
                renderSide(x, y, z, side, 0.0D, icon);
                break;
            }
            case OverlayHandler.OVERLAY_HAY: {
                switch (side) {
                    case DOWN:
                        return;
                    case UP:
                        icon = Block.hay.getBlockTextureFromSide(1);
                        break;
                    default:
                        icon = IconRegistry.icon_overlay_hay_side;
                        break;
                }
                lightingHelper.colorSide(Block.hay, x, y, z, side, icon);
                renderSide(x, y, z, side, 0.0D, icon);
                break;
            }
            case OverlayHandler.OVERLAY_WEB: {
                icon = Block.web.getBlockTextureFromSide(side);
                lightingHelper.colorSide(Block.web, x, y, z, side, icon);
                renderSide(x, y, z, side, 0.0D, icon);
                break;
            }
            case OverlayHandler.OVERLAY_VINE: {
                icon = Block.vine.getBlockTextureFromSide(side);
                lightingHelper.colorSide(Block.vine, x, y, z, side, icon);
                renderSide(x, y, z, side, 0.0D, icon);
                break;
            }
            case OverlayHandler.OVERLAY_MYCELIUM: {
                switch (side) {
                    case DOWN:
                        return;
                    case UP:
                        icon = Block.mycelium.getBlockTextureFromSide(1);
                        break;
                    default:
                        icon = IconRegistry.icon_overlay_mycelium_side;
                        break;
                }
                lightingHelper.colorSide(Block.mycelium, x, y, z, side, icon);
                renderSide(x, y, z, side, 0.0D, icon);
                break;
            }
            case OverlayHandler.OVERLAY_GRASS: {
                if (block != Block.grass && side > DOWN)
                {
                    icon = getGrassOverlayIcon(side);
                    lightingHelper.colorSide(Block.grass, x, y, z, side, icon);
                    renderSide(x, y, z, side, 0.0D, icon);
                }
                break;
            }
        }
    }

    /**
     * Returns grass overlay icon.
     */
    protected Icon getGrassOverlayIcon(int side)
    {
        boolean isPositiveSlope = isSideSloped ? Slope.slopesList[BlockProperties.getData(TE)].isPositive : false;

        Icon icon = BlockGrass.getIconSideOverlay();

        if (side == UP || isPositiveSlope) {
            icon = Block.grass.getBlockTextureFromSide(1);
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
        Icon icon = renderBlocks.getIconSafe(IconRegistry.icon_pattern[pattern]);

        lightingHelper.colorSide(Block.glass, x, y, z, side, icon);
        renderSide(x, y, z, side, 0.0D, icon);
    }

    /**
     * Renders side.
     */
    protected void renderSide(int x, int y, int z, int side, double offset, Icon icon)
    {
        VertexHelper.setOffset(offset);

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

        VertexHelper.clearOffset();
    }

    /**
     * Blocks override this in order to render everything they need.
     */
    protected boolean renderCarpentersBlock(int x, int y, int z)
    {
        return renderBlock(BlockProperties.getCoverBlock(TE, 6), x, y, z);
    }

    /**
     * Sets renderBlocks enableAO state to true depending on
     * rendering environment and block requirements.
     */
    public boolean getEnableAO(Block block)
    {
        return Minecraft.isAmbientOcclusionEnabled() && !disableAO && Block.lightValue[block.blockID] == 0;
    }

    /**
     * Renders block.
     * Coordinates may change since side covers render here.
     */
    protected boolean renderBlock(Block block, int x, int y, int z)
    {
        boolean side_rendered = false;
        renderBlocks.enableAO = getEnableAO(block);

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN) || renderBlocks.renderMinY > 0.0D)
        {
            lightingHelper.setLightingYNeg(block, x, y, z);
            delegateSideRender(block, x, y, z, DOWN);
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP) || renderBlocks.renderMaxY < 1.0D)
        {
            lightingHelper.setLightingYPos(block, x, y, z);
            delegateSideRender(block, x, y, z, UP);
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH) || renderBlocks.renderMinZ > 0.0D)
        {
            lightingHelper.setLightingZNeg(block, x, y, z);
            delegateSideRender(block, x, y, z, NORTH);
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH) || renderBlocks.renderMaxZ < 1.0D)
        {
            lightingHelper.setLightingZPos(block, x, y, z);
            delegateSideRender(block, x, y, z, SOUTH);
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST) || renderBlocks.renderMinX > 0.0D)
        {
            lightingHelper.setLightingXNeg(block, x, y, z);
            delegateSideRender(block, x, y, z, WEST);
            side_rendered = true;
        }

        if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST) || renderBlocks.renderMaxX < 1.0D)
        {
            lightingHelper.setLightingXPos(block, x, y, z);
            delegateSideRender(block, x, y, z, EAST);
            side_rendered = true;
        }

        renderBlocks.enableAO = false;
        return side_rendered;
    }

}
