package com.carpentersblocks.block.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.property.IUnlistedProperty;

public class Property {

	public static final IProperty[] listedProperties = new IProperty[] { BlockDirectional.FACING };
	public static final List<IUnlistedProperty> unlistedProperties;
	public static final IUnlistedProperty ADDL_INT;
	public static final IUnlistedProperty ATTR_BLOCKSTATE;
	public static final IUnlistedProperty ATTR_MAP;
	public static final IUnlistedProperty<BlockPos> BLOCK_POS;
	public static final IUnlistedProperty<Integer> CB_METADATA;
	public static final IUnlistedProperty<Boolean[]> RENDER_FACE;

	static {
		unlistedProperties = new ArrayList<IUnlistedProperty>();
		unlistedProperties.add(ADDL_INT = new UnlistedProperty(Integer.class, "cbAddlInt"));
		unlistedProperties.add(ATTR_BLOCKSTATE = new UnlistedProperty(IBlockState.class, "cbAttrBlockState"));
		unlistedProperties.add(ATTR_MAP = new UnlistedProperty(Map.class, "cbAttrMap"));
		unlistedProperties.add(BLOCK_POS = new UnlistedProperty(BlockPos.class, "cbBlockPos"));
		unlistedProperties.add(CB_METADATA = new UnlistedProperty(Integer.class, "cbMetadata"));
		unlistedProperties.add(RENDER_FACE = new UnlistedProperty(Boolean[].class, "cbRenderFace"));
	}
	
	private static class UnlistedProperty implements IUnlistedProperty {

		private Class _class;
		private String _name;

		public UnlistedProperty(Class clazz, String name) {
			_class = clazz;
			_name = name;
		}
		
		@Override
		public String getName() {
			return _name;
		}

		@Override
		public boolean isValid(Object value) {
			return true;
		}

		@Override
		public Class getType() {
			return _class;
		}

		@Override
		public String valueToString(Object value) {
			return value.toString();
		}
		
	}

}