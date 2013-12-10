package carpentersblocks.renderer;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import carpentersblocks.block.BlockBase;
import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.SlopeType;
import carpentersblocks.renderer.helper.FancyFluidsHelper;
import carpentersblocks.renderer.helper.LightingHelper;
import carpentersblocks.renderer.helper.RenderHelper;
import carpentersblocks.renderer.helper.VertexHelper;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.OverlayHandler;
import carpentersblocks.util.registry.FeatureRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockHandlerBase implements ISimpleBlockRenderingHandler {

	protected static final int DOWN 		= 0;
	protected static final int UP 			= 1;
	protected static final int NORTH 		= 2;
	protected static final int SOUTH 		= 3;
	protected static final int WEST 		= 4;
	protected static final int EAST 		= 5;

	protected RenderBlocks		renderBlocks;
	protected LightingHelper	lightingHelper;
	protected int				renderPass;
	public Block				srcBlock;
	protected TEBase			TE;

	protected boolean		renderAlphaOverride = FeatureRegistry.enableZFightingFix;
	protected boolean		hasMetadataOverride = false;
	protected boolean		suppressOverlay 	= false;
	protected boolean		suppressPattern 	= false;
	public boolean			suppressDyeColor 	= false;
	protected boolean		disableAO			= false;
	protected boolean[]		hasIconOverride		= new boolean[6];
	public boolean			hasDyeColorOverride = false;

	/** 0-5 are side covers, with 6 being the block itself. */
	public int			coverRendering		= 6;
	protected int		metadataOverride	= 0;
	protected Icon[]	iconOverride		= new Icon[6];
	public int			dyeColorOverride 	= 0;

	/** Returns whether side is sloped face. */
	public boolean isSideSloped = false;

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderBlocks) { }

	@Override
	public boolean renderWorldBlock(IBlockAccess blockAccess, int x, int y, int z, Block srcBlock, int modelID, RenderBlocks renderBlocks)
	{
		TE = (TEBase) blockAccess.getBlockTileEntity(x, y, z);
		List<Boolean> result = new ArrayList<Boolean>();

		/*
		 * A catch-all for bad render calls.  Could happen if tile entities aren't
		 * properly loaded when chunks are created, or with multi-block entities
		 * like the door or bed when created or destroyed, when TE does not yet exist
		 * or has already been removed.
		 */
		if (TE != null)
		{
			this.renderBlocks = renderBlocks;
			lightingHelper = new LightingHelper(this, TE, renderBlocks);
			renderPass = MinecraftForgeClient.getRenderPass();
			this.srcBlock = srcBlock;

			setRenderBounds();

			result.add(renderCarpentersBlock(x, y, z));
			result.add(renderSideBlocks(x, y, z));

			/*
			 * Render fancy fluids.
			 * Will render a fluid block in this space if valid.
			 */
			if (FeatureRegistry.enableFancyFluids) {
				if (renderPass >= 0 && Minecraft.isFancyGraphicsEnabled() && BlockProperties.hasCover(TE, 6)) {
					FancyFluidsHelper fancyFluids = new FancyFluidsHelper();
					result.add(fancyFluids.render(TE, lightingHelper, renderBlocks, x, y, z, renderPass));
				}
			}
		}

		return result.contains(true);
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
				renderBlocks.uvRotateEast = 1; // NORTH
			}
			break;
		case SOUTH:
			if (metadata == 3 || dir == 4) {
				renderBlocks.uvRotateWest = 1; // SOUTH
			}
			break;
		case WEST:
			if (metadata == 4 || dir == 8) {
				renderBlocks.uvRotateNorth = 1; // WEST
			}
			break;
		case EAST:
			if (metadata == 4 || dir == 8) {
				renderBlocks.uvRotateSouth = 1; // EAST
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
			renderBlocks.uvRotateEast = 0;
			break;
		case SOUTH:
			renderBlocks.uvRotateWest = 0;
			break;
		case WEST:
			renderBlocks.uvRotateNorth = 0;
			break;
		case EAST:
			renderBlocks.uvRotateSouth = 0;
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
	 * Override for specific block rendering applications.
	 */
	protected void setRenderBounds() { }

	/**
	 * Returns whether block should render this pass.
	 */
	protected boolean shouldRenderBlock(Block block)
	{
		if (renderAlphaOverride) {
			return renderPass == 1;
		} else {
			return	renderBlocks.hasOverrideBlockTexture() ||
					block.getRenderBlockPass() == renderPass ||
					block instanceof BlockBase && renderPass == 0 ||
					this instanceof BlockDeterminantRender;
		}
	}

	/**
	 * Returns whether overlay should render.
	 */
	protected boolean shouldRenderOverlay(Block block)
	{
		if (!suppressOverlay)
		{
			if (BlockProperties.hasOverlay(TE, coverRendering) || block == Block.grass)
			{
				int coverPass = block.getRenderBlockPass();

				if (renderAlphaOverride) {
					return renderPass == 1;
				} else {
					if (block instanceof BlockBase) {
						return true;
					} else if (coverPass == 1) {
						return true;
					} else if (shouldRenderPattern()) {
						return renderPass == 1;
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
		if (!suppressPattern)
		{
			if (renderPass == 1) {
				if (BlockProperties.hasPattern(TE, coverRendering)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Sets up side cover rendering bounds.
	 * Will return block location where side cover should be rendered.
	 */
	protected int[] setSideCoverRenderBounds(int x, int y, int z, int side)
	{
		double offset = 1.0D / 16.0D;

		/*
		 * Make snow match vanilla snow depth
		 */
		if (side == 1)
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
		case 0:
			if (renderBlocks.renderMinY > 0.0D) {
				renderBlocks.renderMaxY = renderBlocks.renderMinY;
				renderBlocks.renderMinY -= offset;
			} else {
				renderBlocks.renderMaxY = 1.0D;
				renderBlocks.renderMinY = renderBlocks.renderMaxY - offset;
				y -= 1;
			}
			break;
		case 1:
			if (renderBlocks.renderMaxY < 1.0D) {
				renderBlocks.renderMinY = renderBlocks.renderMaxY;
				renderBlocks.renderMaxY += offset;
			} else {
				renderBlocks.renderMaxY = offset;
				renderBlocks.renderMinY = 0.0D;
				y += 1;
			}
			break;
		case 2:
			if (renderBlocks.renderMinZ > 0.0D) {
				renderBlocks.renderMaxZ = renderBlocks.renderMinZ;
				renderBlocks.renderMinZ -= offset;
			} else {
				renderBlocks.renderMaxZ = 1.0D;
				renderBlocks.renderMinZ = renderBlocks.renderMaxZ - offset;
				z -= 1;
			}
			break;
		case 3:
			if (renderBlocks.renderMaxZ < 1.0D) {
				renderBlocks.renderMinZ = renderBlocks.renderMaxZ;
				renderBlocks.renderMaxZ += offset;
			} else {
				renderBlocks.renderMaxZ = offset;
				renderBlocks.renderMinZ = 0.0D;
				z += 1;
			}
			break;
		case 4:
			if (renderBlocks.renderMinX > 0.0D) {
				renderBlocks.renderMaxX = renderBlocks.renderMinX;
				renderBlocks.renderMinX -= offset;
			} else {
				renderBlocks.renderMaxX = 1.0D;
				renderBlocks.renderMinX = renderBlocks.renderMaxX - offset;
				x -= 1;
			}
			break;
		case 5:
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

		return new int[] { x, y, z };
	}

	/**
	 * Renders side covers.
	 * @return
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
					int[] renderOffset = setSideCoverRenderBounds(x, y, z, side);
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
		BlockProperties.getData(TE);
		int metadata = hasMetadataOverride ? metadataOverride : BlockProperties.getCoverMetadata(TE, coverRendering);

		Icon icon = getUniqueIcon(block, side, block.getIcon(side, metadata));

		/* If icon has global override, apply it. */
		if (hasIconOverride[side] && iconOverride[side] != null) {
			icon = iconOverride[side];
		}

		return icon;
	}

	/**
	 * Renders multiple textures to side.
	 */
	protected void renderMultiTexturedSide(Block block, int x, int y, int z, int side, Icon icon)
	{
		/* Render side */
		if (shouldRenderBlock(block))
		{
			if (BlockProperties.blockRotates(TE.worldObj, block, x, y, z)) {
				setDirectionalRotation(side);
			}

			lightingHelper.colorSide(block, x, y, z, side, icon);
			renderSide(x, y, z, side, 0.0D, icon);
			clearRotation(side);
		}

		boolean temp_state = suppressDyeColor;
		suppressDyeColor = true;

		/* Render pattern on side. */
		if (shouldRenderPattern()) {
			renderPattern(x, y, z, side);
		}

		/* Render overlay on side. */
		if (shouldRenderOverlay(block)) {
			renderOverlay(block, x, y, z, side, icon);
		}

		suppressDyeColor = temp_state;
	}

	/**
	 * Sets side icon and hands to appropriate render method.
	 */
	protected void delegateSideRender(Block block, int x, int y, int z, int side)
	{
		Icon icon = getIcon(block, side);

		/*
		 * A texture override indicates the breaking animation is being
		 * drawn.  If this is the case, only draw this for current pass.
		 */
		if (renderBlocks.hasOverrideBlockTexture()) {
			renderSide(x, y, z, side, 0.0D, renderBlocks.overrideBlockTexture);
		} else {
			renderMultiTexturedSide(block, x, y, z, side, icon);
		}
	}

	/**
	 * Renders overlay on side.
	 */
	protected void renderOverlay(Block block, int x, int y, int z, int side, Icon icon)
	{
		BlockProperties.getData(TE);
		int overlay = BlockProperties.getOverlay(TE, coverRendering);

		if (isSideSloped)
		{
			Slope slope = Slope.slopesList[BlockProperties.getData(TE)];
			if (slope.isPositive) {
				side = 1;
			} else if (slope.slopeType.equals(SlopeType.OBLIQUE_INT) || slope.slopeType.equals(SlopeType.OBLIQUE_EXT) || slope.slopeType.equals(SlopeType.PYRAMID)) {
				side = 2;
			}
		}

		/*
		 * If coverBlock is grass, we need to prerender the grass
		 * sides before drawing any overlays.
		 */
		if (block == Block.grass)
		{
			icon = getGrassOverlayIcon(side);

			if (icon == null) {
				return;
			}

			lightingHelper.colorSide(Block.grass, x, y, z, side, icon);

			if (overlay != OverlayHandler.NO_OVERLAY) {
				renderSide(x, y, z, side, 0.0D, icon);
			}
		}

		switch (overlay)
		{
		case OverlayHandler.OVERLAY_SNOW:
		{
			switch (side)
			{
			case 0:
				return;
			case 1:
				icon = Block.snow.getBlockTextureFromSide(1);
				break;
			default:
				icon = IconRegistry.icon_overlay_snow_side;
				break;
			}
			lightingHelper.colorSide(Block.blockSnow, x, y, z, side, icon);
			break;
		}
		case OverlayHandler.OVERLAY_HAY:
		{
			switch (side)
			{
			case 0:
				return;
			case 1:
				icon = IconRegistry.icon_overlay_hay_top; // MC 1.5+ only, use hay
				break;
			default:
				icon = IconRegistry.icon_overlay_hay_side;
				break;
			}
			lightingHelper.colorSide(Block.dirt, x, y, z, side, icon);
			break;
		}
		case OverlayHandler.OVERLAY_WEB:
		{
			icon = Block.web.getBlockTextureFromSide(side);
			lightingHelper.colorSide(Block.web, x, y, z, side, icon);
			break;
		}
		case OverlayHandler.OVERLAY_VINE:
		{
			icon = Block.vine.getBlockTextureFromSide(side);
			lightingHelper.colorSide(Block.vine, x, y, z, side, icon);
			break;
		}
		case OverlayHandler.OVERLAY_MYCELIUM:
		{
			switch (side)
			{
			case 0:
				return;
			case 1:
				icon = Block.mycelium.getBlockTextureFromSide(1);
				break;
			default:
				icon = IconRegistry.icon_overlay_mycelium_side;
				break;
			}
			lightingHelper.colorSide(Block.mycelium, x, y, z, side, icon);
			break;
		}
		case OverlayHandler.OVERLAY_GRASS:
			/*
			 * Grass blocks will prerender the grass overlay by
			 * default, so we'll skip this part if it's not necessary.
			 */
			if (block != Block.grass)
			{
				icon = getGrassOverlayIcon(side);

				if (icon == null) {
					return;
				}

				lightingHelper.colorSide(Block.grass, x, y, z, side, icon);
			}
			break;
		}

		renderSide(x, y, z, side, 0.0D, icon);
	}

	/**
	 * Returns grass overlay icon.
	 * Needed to reduce redundant code, and also because Block.grass
	 * is the only real exception in the game due to biome-specific
	 * coloring and side specificity.
	 */
	protected Icon getGrassOverlayIcon(int side)
	{
		boolean isPositiveSlope = isSideSloped ? Slope.slopesList[BlockProperties.getData(TE)].isPositive : false;

		if (side == 1 || isPositiveSlope) {
			return Block.grass.getBlockTextureFromSide(1);
		} else if (side > 1) {

			/*
			 * When FAST graphics are used, grass blocks use a single
			 * texture to draw each side.
			 * 
			 * Because our implementation of grass sides requires
			 * two render passes, and it must be kept separate to drape
			 * off of our custom blocks, we must draw icon_fast_grass to
			 * mimic the look of vanilla grass blocks.
			 */
			if (RenderBlocks.fancyGrass) {
				return BlockGrass.getIconSideOverlay();
			} else {
				return IconRegistry.icon_overlay_fast_grass_side;
			}
		}

		return null;
	}

	/**
	 * Renders pattern on side.
	 */
	protected void renderPattern(int x, int y, int z, int side)
	{
		int pattern = BlockProperties.getPattern(TE, coverRendering);
		Icon icon = IconRegistry.icon_pattern[pattern];

		lightingHelper.colorSide(Block.glass, x, y, z, side, icon);
		renderSide(x, y, z, side, 0.0D, icon);
	}

	/**
	 * Renders side.
	 */
	protected void renderSide(int x, int y, int z, int side, double offset, Icon icon)
	{
		if (offset != 0) {
			VertexHelper.setOffset(offset);
		}

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
	 * Renders block.
	 * Coordinates may change since side covers render here.
	 */
	protected boolean renderBlock(Block block, int x, int y, int z)
	{
		boolean side_rendered = false;
		renderBlocks.enableAO = Minecraft.isAmbientOcclusionEnabled() && !disableAO && Block.lightValue[block.blockID] == 0;

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y - 1, z, DOWN) || renderBlocks.renderMinY > 0.0D)
		{
			lightingHelper.setLightness(0.5F).setLightingYNeg(block, x, y, z);
			delegateSideRender(block, x, y, z, DOWN);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y + 1, z, UP) || renderBlocks.renderMaxY < 1.0D)
		{
			lightingHelper.setLightness(1.0F).setLightingYPos(block, x, y, z);
			delegateSideRender(block, x, y, z, UP);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z - 1, NORTH) || renderBlocks.renderMinZ > 0.0D)
		{
			lightingHelper.setLightness(0.8F).setLightingZNeg(block, x, y, z);
			delegateSideRender(block, x, y, z, NORTH);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x, y, z + 1, SOUTH) || renderBlocks.renderMaxZ < 1.0D)
		{
			lightingHelper.setLightness(0.8F).setLightingZPos(block, x, y, z);
			delegateSideRender(block, x, y, z, SOUTH);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x - 1, y, z, WEST) || renderBlocks.renderMinX > 0.0D)
		{
			lightingHelper.setLightness(0.6F).setLightingXNeg(block, x, y, z);
			delegateSideRender(block, x, y, z, WEST);
			side_rendered = true;
		}

		if (renderBlocks.renderAllFaces || srcBlock.shouldSideBeRendered(renderBlocks.blockAccess, x + 1, y, z, EAST) || renderBlocks.renderMaxX < 1.0D)
		{
			lightingHelper.setLightness(0.6F).setLightingXPos(block, x, y, z);
			delegateSideRender(block, x, y, z, EAST);
			side_rendered = true;
		}

		renderBlocks.enableAO = false;
		return side_rendered;
	}

}