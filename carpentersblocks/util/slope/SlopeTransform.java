package carpentersblocks.util.slope;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.data.Slope.Type;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;

public class SlopeTransform {

	private final Block block = BlockRegistry.blockCarpentersSlope;
	private final World world;
	private final int x;
	private final int y;
	private final int z;

	private final byte SRC = 0;
	private final byte XN = 1;
	private final byte XP = 2;
	private final byte YN = 3;
	private final byte YP = 4;
	private final byte ZN = 5;
	private final byte ZP = 6;
	private final byte XYNN = 7;
	private final byte XYPN = 8;
	private final byte XYNP = 9;
	private final byte XYPP = 10;
	private final byte YZNN = 11;
	private final byte YZNP = 12;
	private final byte YZPN = 13;
	private final byte YZPP = 14;
	private final byte XYZNNN = 15;
	private final byte XYZNNP = 16;
	private final byte XYZPNN = 17;
	private final byte XYZPNP = 18;
	
	private final TEBase[] 	TE 		= new TEBase[19];
	private Slope[]			slope 	= new Slope[19];
	private final boolean[] isSlope	= new boolean[19];
	private final boolean[] isAir 	= new boolean[19];
	
	public SlopeTransform(TEBase TE)
	{
		this.TE[SRC] = TE;
		this.world = TE.worldObj;
		this.x = TE.xCoord;
		this.y = TE.yCoord;
		this.z = TE.zCoord;

		buildBlockMap();
	}
	
	private void buildBlockMap()
	{
		isAir[XN] = world.isAirBlock(x - 1, y, z);
		isAir[XP] = world.isAirBlock(x + 1, y, z);
		isAir[YN] = world.isAirBlock(x, y - 1, z);
		isAir[YP] = world.isAirBlock(x, y + 1, z);
		isAir[ZN] = world.isAirBlock(x, y, z - 1);
		isAir[ZP] = world.isAirBlock(x, y, z + 1);
		isAir[XYNN] = world.isAirBlock(x - 1, y - 1, z);
		isAir[XYPN] = world.isAirBlock(x + 1, y - 1, z);
		isAir[XYNP] = world.isAirBlock(x - 1, y + 1, z);
		isAir[XYPP] = world.isAirBlock(x + 1, y + 1, z);
		isAir[YZNN] = world.isAirBlock(x, y - 1, z - 1);
		isAir[YZNP] = world.isAirBlock(x, y - 1, z + 1);
		isAir[YZPN] = world.isAirBlock(x, y + 1, z - 1);
		isAir[YZPP] = world.isAirBlock(x, y + 1, z + 1);
		isAir[XYZNNN] = world.isAirBlock(x - 1, y - 1, z - 1);
		isAir[XYZNNP] = world.isAirBlock(x - 1, y - 1, z + 1);
		isAir[XYZPNN] = world.isAirBlock(x + 1, y - 1, z - 1);
		isAir[XYZPNP] = world.isAirBlock(x + 1, y - 1, z + 1);
		
		isSlope[XN] = !isAir[XN] && (Block.blocksList[world.getBlockId(x - 1, y, z)].equals(block));
		isSlope[XP] = !isAir[XP] && (Block.blocksList[world.getBlockId(x + 1, y, z)].equals(block));
		isSlope[YN] = !isAir[YN] && (Block.blocksList[world.getBlockId(x, y - 1, z)].equals(block));
		isSlope[YP] = !isAir[YP] && (Block.blocksList[world.getBlockId(x, y + 1, z)].equals(block));
		isSlope[ZN] = !isAir[ZN] && (Block.blocksList[world.getBlockId(x, y, z - 1)].equals(block));
		isSlope[ZP] = !isAir[ZP] && (Block.blocksList[world.getBlockId(x, y, z + 1)].equals(block));
		isSlope[XYNN] = !isAir[XYNN] && (Block.blocksList[world.getBlockId(x - 1, y - 1, z)].equals(block));
		isSlope[XYPN] = !isAir[XYPN] && (Block.blocksList[world.getBlockId(x + 1, y - 1, z)].equals(block));
		isSlope[XYNP] = !isAir[XYNP] && (Block.blocksList[world.getBlockId(x - 1, y + 1, z)].equals(block));
		isSlope[XYPP] = !isAir[XYPP] && (Block.blocksList[world.getBlockId(x + 1, y + 1, z)].equals(block));
		isSlope[YZNN] = !isAir[YZNN] && (Block.blocksList[world.getBlockId(x, y - 1, z - 1)].equals(block));
		isSlope[YZNP] = !isAir[YZNP] && (Block.blocksList[world.getBlockId(x, y - 1, z + 1)].equals(block));
		isSlope[YZPN] = !isAir[YZPN] && (Block.blocksList[world.getBlockId(x, y + 1, z - 1)].equals(block));
		isSlope[YZPP] = !isAir[YZPP] && (Block.blocksList[world.getBlockId(x, y + 1, z + 1)].equals(block));
		isSlope[XYZNNN] = !isAir[XYZNNN] && (Block.blocksList[world.getBlockId(x - 1, y - 1, z - 1)].equals(block));
		isSlope[XYZNNP] = !isAir[XYZNNP] && (Block.blocksList[world.getBlockId(x - 1, y - 1, z + 1)].equals(block));
		isSlope[XYZPNN] = !isAir[XYZPNN] && (Block.blocksList[world.getBlockId(x + 1, y - 1, z - 1)].equals(block));
		isSlope[XYZPNP] = !isAir[XYZPNP] && (Block.blocksList[world.getBlockId(x + 1, y - 1, z + 1)].equals(block));

		if (isSlope[XN]) {
			TE[XN] = (TEBase) world.getBlockTileEntity(x - 1, y, z);
			slope[XN] = Slope.slopesList[BlockProperties.getData(TE[XN])];
		}
		if (isSlope[XP]) {
			TE[XP] = (TEBase) world.getBlockTileEntity(x + 1, y, z);
			slope[XP] = Slope.slopesList[BlockProperties.getData(TE[XP])];
		}
		if (isSlope[YN]) {
			TE[YN] = (TEBase) world.getBlockTileEntity(x, y - 1, z);
			slope[YN] = Slope.slopesList[BlockProperties.getData(TE[YN])];
		}
		if (isSlope[YP]) {
			TE[YP] = (TEBase) world.getBlockTileEntity(x, y + 1, z);
			slope[YP] = Slope.slopesList[BlockProperties.getData(TE[YP])];
		}
		if (isSlope[ZN]) {
			TE[ZN] = (TEBase) world.getBlockTileEntity(x, y, z - 1);
			slope[ZN] = Slope.slopesList[BlockProperties.getData(TE[ZN])];
		}
		if (isSlope[ZP]) {
			TE[ZP] = (TEBase) world.getBlockTileEntity(x, y, z + 1);
			slope[ZP] = Slope.slopesList[BlockProperties.getData(TE[ZP])];
		}
		if (isSlope[XYNN]) {
			TE[XYNN] = (TEBase) world.getBlockTileEntity(x - 1, y - 1, z);
			slope[XYNN] = Slope.slopesList[BlockProperties.getData(TE[XYNN])];
		}
		if (isSlope[XYPN]) {
			TE[XYPN] = (TEBase) world.getBlockTileEntity(x + 1, y - 1, z);
			slope[XYPN] = Slope.slopesList[BlockProperties.getData(TE[XYPN])];
		}
		if (isSlope[XYNP]) {
			TE[XYNP]= (TEBase) world.getBlockTileEntity(x - 1, y + 1, z);
			slope[XYNP]= Slope.slopesList[BlockProperties.getData(TE[XYNP])];
		}
		if (isSlope[XYPP]) {
			TE[XYPP] = (TEBase) world.getBlockTileEntity(x + 1, y + 1, z);
			slope[XYPP] = Slope.slopesList[BlockProperties.getData(TE[XYPP])];
		}
		if (isSlope[YZNN]) {
			TE[YZNN] = (TEBase) world.getBlockTileEntity(x, y - 1, z - 1);
			slope[YZNN] = Slope.slopesList[BlockProperties.getData(TE[YZNN])];
		}
		if (isSlope[YZNP]) {
			TE[YZNP] = (TEBase) world.getBlockTileEntity(x, y - 1, z + 1);
			slope[YZNP] = Slope.slopesList[BlockProperties.getData(TE[YZNP])];
		}
		if (isSlope[YZPN]) {
			TE[YZPN] = (TEBase) world.getBlockTileEntity(x, y + 1, z - 1);
			slope[YZPN] = Slope.slopesList[BlockProperties.getData(TE[YZPN])];
		}
		if (isSlope[YZPP]) {
			TE[YZPP] = (TEBase) world.getBlockTileEntity(x, y + 1, z + 1);
			slope[YZPP] = Slope.slopesList[BlockProperties.getData(TE[YZPP])];
		}
	}

	/**
	 * Will begin transforming source slope and any nearby slopes.
	 */
	public void begin()
	{
		int slopeID = BlockProperties.getData(TE[SRC]);
		
		if (genWedgeCorner(slopeID)) {
			return;
		}
		genAdjWedgeCorners();
		
		if (genHorizWedge(slopeID)) {
			genVerticalObliques();
			return;
		}
		
		if (genExtOblique(slopeID)) {
			return;
		}
		
		if (genPyramid(slopeID)) {
			return;
		}
		
		if (genPrism(slopeID)) {
			genAdjPrismSlopes();
			return;
		}
	}
	
	/**
	 * Converts adjacent positive wedges to prism slopes if this slope is a connecting half roof piece.
	 */
	private void genAdjPrismSlopes()
	{
		Slope slope = Slope.slopesList[BlockProperties.getData(TE[SRC])];

		if (isSlope[XN] && this.slope[XN].equals(Slope.WEDGE_POS_E)) {
			if (slope.facings.contains(ForgeDirection.WEST)) {
				BlockProperties.setData(TE[XN], Slope.ID_PRISM_SLOPE_E);
			}
		}
		if (isSlope[XP] && this.slope[XP].equals(Slope.WEDGE_POS_W)) {
			if (slope.facings.contains(ForgeDirection.EAST)) {
				BlockProperties.setData(TE[XP], Slope.ID_PRISM_SLOPE_W);
			}
		}
		if (isSlope[ZN] && this.slope[ZN].equals(Slope.WEDGE_POS_S)) {
			if (slope.facings.contains(ForgeDirection.NORTH)) {
				BlockProperties.setData(TE[ZN], Slope.ID_PRISM_SLOPE_S);
			}
		}
		if (isSlope[ZP] && this.slope[ZP].equals(Slope.WEDGE_POS_N)) {
			if (slope.facings.contains(ForgeDirection.SOUTH)) {
				BlockProperties.setData(TE[ZP], Slope.ID_PRISM_SLOPE_N);
			}
		}
	}

	/**
	 * Converts source block to corner if necessary, and also converts adjacent
	 * blocks to match.
	 */
	private boolean genWedgeCorner(final int slopeID)
	{
		int temp_slopeID = slopeID;
		
		Slope slope = Slope.slopesList[slopeID];
		
		if (isSlope[ZN]) {
			if (isSlope[XN]) {
				if (this.slope[ZN].facings.contains(ForgeDirection.WEST) && this.slope[XN].facings.contains(ForgeDirection.NORTH)) {
					temp_slopeID = this.slope[XN].isPositive && this.slope[ZN].isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
				}
				if (this.slope[ZN].facings.contains(ForgeDirection.EAST) && this.slope[XN].facings.contains(ForgeDirection.SOUTH)) {
					temp_slopeID = this.slope[XN].isPositive && this.slope[ZN].isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
				}
			}
			if (isSlope[XP]) {
				if (this.slope[ZN].facings.contains(ForgeDirection.EAST) && this.slope[XP].facings.contains(ForgeDirection.NORTH)) {
					temp_slopeID = this.slope[XP].isPositive && this.slope[ZN].isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
				}
				if (this.slope[ZN].facings.contains(ForgeDirection.WEST) && this.slope[XP].facings.contains(ForgeDirection.SOUTH)) {
					temp_slopeID = this.slope[XP].isPositive && this.slope[ZN].isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
				}
			}
		}
		if (isSlope[ZP]) {
			if (isSlope[XN]) {
				if (this.slope[ZP].facings.contains(ForgeDirection.WEST) && this.slope[XN].facings.contains(ForgeDirection.SOUTH)) {
					temp_slopeID = this.slope[XN].isPositive && this.slope[ZP].isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
				}
				if (this.slope[ZP].facings.contains(ForgeDirection.EAST) && this.slope[XN].facings.contains(ForgeDirection.NORTH)) {
					temp_slopeID = this.slope[XN].isPositive && this.slope[ZP].isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
				}
			}
			if (isSlope[XP]) {
				if (this.slope[ZP].facings.contains(ForgeDirection.EAST) && this.slope[XP].facings.contains(ForgeDirection.SOUTH)) {
					temp_slopeID = this.slope[XP].isPositive && this.slope[ZP].isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
				}
				if (this.slope[ZP].facings.contains(ForgeDirection.WEST) && this.slope[XP].facings.contains(ForgeDirection.NORTH)) {
					temp_slopeID = this.slope[XP].isPositive && this.slope[ZP].isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
				}
			}
		}
		
		if (slope.type.equals(Type.WEDGE_Y))
		{
			if (isSlope[XN]) {
				if (slope.facings.contains(ForgeDirection.WEST)) {
					if (this.slope[XN].facings.contains(ForgeDirection.SOUTH) && !this.slope[XN].facings.contains(ForgeDirection.EAST)) {
						temp_slopeID = this.slope[XN].isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
					}
					if (this.slope[XN].facings.contains(ForgeDirection.NORTH) && !this.slope[XN].facings.contains(ForgeDirection.EAST)) {
						temp_slopeID = this.slope[XN].isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
					}
				}
				if (slope.facings.contains(ForgeDirection.EAST)) {
					if (this.slope[XN].facings.contains(ForgeDirection.SOUTH) && !this.slope[XN].facings.contains(ForgeDirection.EAST)) {
						temp_slopeID = this.slope[XN].isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
					}
					if (this.slope[XN].facings.contains(ForgeDirection.NORTH) && !this.slope[XN].facings.contains(ForgeDirection.EAST)) {
						temp_slopeID = this.slope[XN].isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
					}
				}
			}
			if (isSlope[XP]) {
				if (slope.facings.contains(ForgeDirection.WEST)) {
					if (this.slope[XP].facings.contains(ForgeDirection.SOUTH) && !this.slope[XP].facings.contains(ForgeDirection.WEST)) {
						temp_slopeID = this.slope[XP].isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
					}
					if (this.slope[XP].facings.contains(ForgeDirection.NORTH) && !this.slope[XP].facings.contains(ForgeDirection.WEST)) {
						temp_slopeID = this.slope[XP].isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
					}
				}
				if (slope.facings.contains(ForgeDirection.EAST)) {
					if (this.slope[XP].facings.contains(ForgeDirection.SOUTH) && !this.slope[XP].facings.contains(ForgeDirection.WEST)) {
						temp_slopeID = this.slope[XP].isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
					}
					if (this.slope[XP].facings.contains(ForgeDirection.NORTH) && !this.slope[XP].facings.contains(ForgeDirection.WEST)) {
						temp_slopeID = this.slope[XP].isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
					}
				}
			}
			if (isSlope[ZN]) {
				if (slope.facings.contains(ForgeDirection.NORTH)) {
					if (this.slope[ZN].facings.contains(ForgeDirection.EAST) && !this.slope[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_slopeID = this.slope[ZN].isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
					}
					if (this.slope[ZN].facings.contains(ForgeDirection.WEST) && !this.slope[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_slopeID = this.slope[ZN].isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
					}
				}
				if (slope.facings.contains(ForgeDirection.SOUTH)) {
					if (this.slope[ZN].facings.contains(ForgeDirection.EAST) && !this.slope[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_slopeID = this.slope[ZN].isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
					}
					if (this.slope[ZN].facings.contains(ForgeDirection.WEST) && !this.slope[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_slopeID = this.slope[ZN].isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
					}
				}
			}
			if (isSlope[ZP]) {
				if (slope.facings.contains(ForgeDirection.NORTH)) {
					if (this.slope[ZP].facings.contains(ForgeDirection.EAST) && !this.slope[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_slopeID = this.slope[ZP].isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
					}
					if (this.slope[ZP].facings.contains(ForgeDirection.WEST) && !this.slope[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_slopeID = this.slope[ZP].isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
					}
				}
				if (slope.facings.contains(ForgeDirection.SOUTH)) {
					if (this.slope[ZP].facings.contains(ForgeDirection.EAST) && !this.slope[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_slopeID = this.slope[ZP].isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
					}
					if (this.slope[ZP].facings.contains(ForgeDirection.WEST) && !this.slope[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_slopeID = this.slope[ZP].isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
					}
				}
			}
		}

		BlockProperties.setData(TE[SRC], temp_slopeID);
		
		return slopeID != temp_slopeID;
	}
	
	/**
	 * Converts adjacent blocks to match slopeID.
	 */
	private void genAdjWedgeCorners()
	{
		Slope slope = Slope.slopesList[BlockProperties.getData(TE[SRC])];

		if (slope.facings.contains(ForgeDirection.WEST)) {
			if (isSlope[ZN] && slope.getPrimaryType().equals(this.slope[ZN].getPrimaryType()) && slope.isPositive == this.slope[ZN].isPositive) {
				if (this.slope[ZN].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZN], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
				}
				if (this.slope[ZN].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZN], slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
				}
			}
			if (isSlope[ZP] && slope.getPrimaryType().equals(this.slope[ZP].getPrimaryType()) && slope.isPositive == this.slope[ZP].isPositive) {
				if (this.slope[ZP].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZP], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
				}
				if (this.slope[ZP].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZP], slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
				}
			}
		}
		if (slope.facings.contains(ForgeDirection.EAST)) {
			if (isSlope[ZN] && slope.getPrimaryType().equals(this.slope[ZN].getPrimaryType()) && slope.isPositive == this.slope[ZN].isPositive) {
				if (this.slope[ZN].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZN], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
				}
				if (this.slope[ZN].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZN], slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
				}
			}
			if (isSlope[ZP] && slope.getPrimaryType().equals(this.slope[ZP].getPrimaryType()) && slope.isPositive == this.slope[ZP].isPositive) {
				if (this.slope[ZP].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZP], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
				}
				if (this.slope[ZP].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZP], slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
				}
			}
		}
		if (slope.facings.contains(ForgeDirection.NORTH)) {
			if (isSlope[XN] && slope.getPrimaryType().equals(this.slope[XN].getPrimaryType()) && slope.isPositive == this.slope[XN].isPositive) {
				if (this.slope[XN].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XN], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
				}
				if (this.slope[XN].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XN], slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
				}
			}
			if (isSlope[XP] && slope.getPrimaryType().equals(this.slope[XP].getPrimaryType()) && slope.isPositive == this.slope[XP].isPositive) {
				if (this.slope[XP].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XP], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
				}
				if (this.slope[XP].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XP], slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
				}
			}
		}
		if (slope.facings.contains(ForgeDirection.SOUTH)) {
			if (isSlope[XN] && slope.getPrimaryType().equals(this.slope[XN].getPrimaryType()) && slope.isPositive == this.slope[XN].isPositive) {
				if (this.slope[XN].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XN], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
				}
				if (this.slope[XN].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XN], slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
				}
			}
			if (isSlope[XP] && slope.getPrimaryType().equals(this.slope[XP].getPrimaryType()) && slope.isPositive == this.slope[XP].isPositive) {
				if (this.slope[XP].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XP], slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
				}
				if (this.slope[XP].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XP], slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
				}
			}
		}
	}

	/**
	 * Check if slope above or below is side slope or oblique interior slope, and, if so, make this a continuation.
	 */
	private boolean genHorizWedge(final int slopeID)
	{
		int temp_slopeID = slopeID;
		
		if (isSlope[YP]) {
			if (slope[YP].type.equals(Type.WEDGE_XZ)) {
				temp_slopeID = slope[YP].slopeID;
			} else if (slope[YP].type.equals(Type.OBLIQUE_INT)) {
				switch (slope[YP].slopeID) {
				case Slope.ID_OBL_INT_NEG_NW:
					temp_slopeID = Slope.ID_WEDGE_NW;
					break;
				case Slope.ID_OBL_INT_NEG_SW:
					temp_slopeID = Slope.ID_WEDGE_SW;
					break;
				case Slope.ID_OBL_INT_NEG_NE:
					temp_slopeID = Slope.ID_WEDGE_NE;
					break;
				default: // Slope.INT_NEG_OBL_SE
					temp_slopeID = Slope.ID_WEDGE_SE;
					break;
				}
			}
		}
		if (isSlope[YN]) {
			if (slope[YN].type.equals(Type.WEDGE_XZ)) {
				temp_slopeID = slope[YN].slopeID;
			} else if (slope[YN].type.equals(Type.OBLIQUE_INT)) {
				switch (slope[YN].slopeID) {
				case Slope.ID_OBL_INT_POS_NW:
					temp_slopeID = Slope.ID_WEDGE_NW;
					break;
				case Slope.ID_OBL_INT_POS_SW:
					temp_slopeID = Slope.ID_WEDGE_SW;
					break;
				case Slope.ID_OBL_INT_POS_NE:
					temp_slopeID = Slope.ID_WEDGE_NE;
					break;
				default: // Slope.INT_POS_OBL_SE
					temp_slopeID = Slope.ID_WEDGE_SE;
					break;
				}
			}
		}
		
		/* Check if slope below is interior corner, change to oblique if it is, and change this to side slope. */
		
		if (isSlope[YP]) {
			if (slope[YP] == Slope.WEDGE_INT_NEG_NW) {
				temp_slopeID = Slope.ID_WEDGE_NW;
			} else if (slope[YP] == Slope.WEDGE_INT_NEG_SW) {
				temp_slopeID = Slope.ID_WEDGE_SW;
			} else if (slope[YP] == Slope.WEDGE_INT_NEG_NE) {
				temp_slopeID = Slope.ID_WEDGE_NE;
			} else if (slope[YP] == Slope.WEDGE_INT_NEG_SE) {
				temp_slopeID = Slope.ID_WEDGE_SE;
			}
		}
		if (isSlope[YN]) {
			if (slope[YN] == Slope.WEDGE_INT_POS_NW) {
				temp_slopeID = Slope.ID_WEDGE_NW;
			} else if (slope[YN] == Slope.WEDGE_INT_POS_SW) {
				temp_slopeID = Slope.ID_WEDGE_SW;
			} else if (slope[YN] == Slope.WEDGE_INT_POS_NE) {
				temp_slopeID = Slope.ID_WEDGE_NE;
			} else if (slope[YN] == Slope.WEDGE_INT_POS_SE) {
				temp_slopeID = Slope.ID_WEDGE_SE;
			}
		}
		
		BlockProperties.setData(TE[SRC], temp_slopeID);
		
		return slopeID != temp_slopeID;
	}
	
	/**
	 * Converts blocks above or below to matching oblique slopes.
	 */
	private void genVerticalObliques()
	{
		if (isSlope[YP]) {
			if (slope[YP] == Slope.WEDGE_INT_NEG_NW) {
				BlockProperties.setData(TE[YP], Slope.ID_OBL_INT_NEG_NW);
			} else if (slope[YP] == Slope.WEDGE_INT_NEG_SW) {
				BlockProperties.setData(TE[YP], Slope.ID_OBL_INT_NEG_SW);
			} else if (slope[YP] == Slope.WEDGE_INT_NEG_NE) {
				BlockProperties.setData(TE[YP], Slope.ID_OBL_INT_NEG_NE);
			} else if (slope[YP] == Slope.WEDGE_INT_NEG_SE) {
				BlockProperties.setData(TE[YP], Slope.ID_OBL_INT_NEG_SE);
			}
		}
		if (isSlope[YN]) {
			if (slope[YN] == Slope.WEDGE_INT_POS_NW) {
				BlockProperties.setData(TE[YN], Slope.ID_OBL_INT_POS_NW);
			} else if (slope[YN] == Slope.WEDGE_INT_POS_SW) {
				BlockProperties.setData(TE[YN], Slope.ID_OBL_INT_POS_SW);
			} else if (slope[YN] == Slope.WEDGE_INT_POS_NE) {
				BlockProperties.setData(TE[YN], Slope.ID_OBL_INT_POS_NE);
			} else if (slope[YN] == Slope.WEDGE_INT_POS_SE) {
				BlockProperties.setData(TE[YN], Slope.ID_OBL_INT_POS_SE);
			}
		}
	}
	
	/**
	 * Check if slope should form into exterior oblique corner.
	 */
	private boolean genExtOblique(final int slopeID)
	{
		int temp_slopeID = slopeID;
		
		if (
				isSlope[YP] && (slope[YP].type.equals(Type.OBLIQUE_EXT) || slope[YP].type.equals(Type.WEDGE_XZ)) ||
				isSlope[YN] && (slope[YN].type.equals(Type.OBLIQUE_EXT) || slope[YN].type.equals(Type.WEDGE_XZ))
				)
		{
			if (isSlope[XP] && isSlope[ZN]) {

				if (slope[XP].type.equals(Type.WEDGE_XZ) && slope[ZN].type.equals(Type.WEDGE_XZ) && slope[XP].isPositive == slope[ZN].isPositive) {
					if (slope[XP].facings.contains(ForgeDirection.SOUTH) && slope[ZN].facings.contains(ForgeDirection.WEST)) {
						temp_slopeID = slope[XP].isPositive ? Slope.ID_OBL_EXT_POS_SW : Slope.ID_OBL_EXT_NEG_SW;
					}
				}

			} else if (isSlope[ZN] && isSlope[XN]) {

				if (slope[ZN].type.equals(Type.WEDGE_XZ) && slope[XN].type.equals(Type.WEDGE_XZ) && slope[ZN].isPositive == slope[XN].isPositive) {
					if (slope[ZN].facings.contains(ForgeDirection.EAST) && slope[XN].facings.contains(ForgeDirection.SOUTH)) {
						temp_slopeID = slope[ZN].isPositive ? Slope.ID_OBL_EXT_POS_SE : Slope.ID_OBL_EXT_NEG_SE;
					}
				}

			} else if (isSlope[XN] && isSlope[ZP]) {

				if (slope[XN].type.equals(Type.WEDGE_XZ) && slope[ZP].type.equals(Type.WEDGE_XZ) && slope[XN].isPositive == slope[ZP].isPositive) {
					if (slope[XN].facings.contains(ForgeDirection.NORTH) && slope[ZP].facings.contains(ForgeDirection.EAST)) {
						temp_slopeID = slope[XN].isPositive ? Slope.ID_OBL_EXT_POS_NE : Slope.ID_OBL_EXT_NEG_NE;
					}
				}

			} else if (isSlope[ZP] && isSlope[XP]) {

				if (slope[ZP].type.equals(Type.WEDGE_XZ) && slope[XP].type.equals(Type.WEDGE_XZ) && slope[ZP].isPositive == slope[XP].isPositive) {
					if (slope[ZP].facings.contains(ForgeDirection.WEST) && slope[XP].facings.contains(ForgeDirection.NORTH)) {
						temp_slopeID = slope[ZP].isPositive ? Slope.ID_OBL_EXT_POS_NW : Slope.ID_OBL_EXT_NEG_NW;
					}
				}

			}
		}
		
		BlockProperties.setData(TE[SRC], temp_slopeID);
		
		return slopeID != temp_slopeID;
	}
	
	/**
	 * Check if slope should form into a pyramid.
	 */
	private boolean genPyramid(final int slopeID)
	{
		int temp_slopeID = slopeID;
		
		if (TE[XYNN] != null && TE[XYPN] != null && TE[YZNN] != null && TE[YZNP] != null) {
			if (
					Slope.slopesList[BlockProperties.getData(TE[XYNN])] == Slope.WEDGE_POS_W &&
					Slope.slopesList[BlockProperties.getData(TE[XYPN])] == Slope.WEDGE_POS_E &&
					Slope.slopesList[BlockProperties.getData(TE[YZNN])] == Slope.WEDGE_POS_N &&
					Slope.slopesList[BlockProperties.getData(TE[YZNP])] == Slope.WEDGE_POS_S
					) {
				temp_slopeID = Slope.ID_PYR_HALF_POS;
			}
		}
		if (TE[XYNP] != null && TE[XYPP] != null && TE[YZPN] != null && TE[YZPP] != null) {
			if (
					Slope.slopesList[BlockProperties.getData(TE[XYNP])] == Slope.WEDGE_NEG_W &&
					Slope.slopesList[BlockProperties.getData(TE[XYPP])] == Slope.WEDGE_NEG_E &&
					Slope.slopesList[BlockProperties.getData(TE[YZPN])] == Slope.WEDGE_NEG_N &&
					Slope.slopesList[BlockProperties.getData(TE[YZPP])] == Slope.WEDGE_NEG_S
					) {
				temp_slopeID = Slope.ID_PYR_HALF_NEG;
			}
		}
		
		BlockProperties.setData(TE[SRC], temp_slopeID);
		
		return slopeID != temp_slopeID;
	}
	
	/**
	 * Check if slope should form half-height roof piece based on adjacent low slopes.
	 */
	private boolean genPrism(final int slopeID)
	{
		if (supportsPrism()) {
			
			int temp_slopeID = slopeID;

			List<ForgeDirection> prism = new ArrayList<ForgeDirection>();
			
			prism.add(ForgeDirection.UP);
			
			if (!isSlope[YZNN] && !isAir[YZNN]) {
				prism.add(ForgeDirection.NORTH);
			}
			if (!isSlope[YZNP] && !isAir[YZNP]) {
				prism.add(ForgeDirection.SOUTH);
			}
			if (!isSlope[XYNN] && !isAir[XYNN]) {
				prism.add(ForgeDirection.WEST);
			}
			if (!isSlope[XYPN] && !isAir[XYPN]) {
				prism.add(ForgeDirection.EAST);
			}
			
			if (isAir[XYZNNN] && isAir[XYZNNP] && isAir[XYZPNN] && isAir[XYZPNP]) {
				prism.add(ForgeDirection.NORTH);
				prism.add(ForgeDirection.SOUTH);
				prism.add(ForgeDirection.WEST);
				prism.add(ForgeDirection.EAST);
			}
	
			for (int slope = Slope.ID_PRISM_1P_N; slope <= Slope.ID_PRISM_4P; ++slope) {
				if (prism.equals(Slope.slopesList[slope].facings)) {
					temp_slopeID = slope;
					break;
				}
			}
			
			BlockProperties.setData(TE[SRC], temp_slopeID);
			
			return slopeID != temp_slopeID;
			
		} else {
			
			return false;
			
		}
	}
	
	private boolean supportsPrism()
	{
		return	!world.isAirBlock(x, y - 1, z) &&
				Block.blocksList[world.getBlockId(x, y - 1, z)].isBlockSolidOnSide(world, x, y - 1, z, ForgeDirection.UP) &&
				(isAir[XYNN] || isAir[XYPN] || isAir[YZNN] || isAir[YZNP] || isAir[XYZNNN] || isAir[XYZNNP] || isAir[XYZPNN] || isAir[XYZPNP]) ||
				(isSlope[XYNN] || isSlope[XYPN] || isSlope[YZNN] || isSlope[YZNP] || isSlope[XYZNNN] || isSlope[XYZNNP] || isSlope[XYZPNN] || isSlope[XYZPNP]);
	}

}
