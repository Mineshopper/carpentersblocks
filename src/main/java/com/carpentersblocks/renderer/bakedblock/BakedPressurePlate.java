package com.carpentersblocks.renderer.bakedblock;

import java.util.function.Function;

import com.carpentersblocks.block.IStateImplementor;
import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.RenderPkg;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.states.StatePart;
import com.carpentersblocks.util.states.StateUtil;
import com.carpentersblocks.util.states.factory.AbstractState;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedPressurePlate extends AbstractBakedModel {

    public BakedPressurePlate(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		StateUtil util = new StateUtil();
		AbstractState state = (AbstractState) RenderPkg.getThreadedProperty(Property.CB_STATE);
		if (state == null) {
			renderPkg.addAll(((IStateImplementor)BlockRegistry.blockCarpentersPressurePlate).getStateMap().getInventoryQuads());
		} else {
			for (StatePart part : state.getStateParts()) {
				renderPkg.addAll(util.getQuads(part));
			}
		}
	}
	
}