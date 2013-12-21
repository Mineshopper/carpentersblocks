package carpentersblocks.util.stairs;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.data.Slope;
import carpentersblocks.data.Stairs;
import carpentersblocks.data.Stairs.StairsType;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;

public class StairsTransform {

	private final Block block = BlockRegistry.blockCarpentersStairs;
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
	
	private final TEBase[] 	TE 			= new TEBase[20];
	private Stairs[]		stairs 		= new Stairs[20];
	private final boolean[] areStairs	= new boolean[20];
	
	public StairsTransform(TEBase TE)
	{
		this.TE[SRC] = TE;
		this.world = TE.worldObj;
		this.x = TE.xCoord;
		this.y = TE.yCoord;
		this.z = TE.zCoord;

		buildStairsMap();
	}
	
	private void buildStairsMap()
	{
		areStairs[XN] = !world.isAirBlock(x - 1, y, z) && (Block.blocksList[world.getBlockId(x - 1, y, z)].equals(block));
		areStairs[XP] = !world.isAirBlock(x + 1, y, z) && (Block.blocksList[world.getBlockId(x + 1, y, z)].equals(block));
		areStairs[YN] = !world.isAirBlock(x, y - 1, z) && (Block.blocksList[world.getBlockId(x, y - 1, z)].equals(block));
		areStairs[YP] = !world.isAirBlock(x, y + 1, z) && (Block.blocksList[world.getBlockId(x, y + 1, z)].equals(block));
		areStairs[ZN] = !world.isAirBlock(x, y, z - 1) && (Block.blocksList[world.getBlockId(x, y, z - 1)].equals(block));
		areStairs[ZP] = !world.isAirBlock(x, y, z + 1) && (Block.blocksList[world.getBlockId(x, y, z + 1)].equals(block));
		
		if (areStairs[YN]) {
			TE[YN] = (TEBase) world.getBlockTileEntity(x, y - 1, z);
			stairs[YN] = Stairs.stairsList[BlockProperties.getData(TE[YN])];
		}
		if (areStairs[YP]) {
			TE[YP] = (TEBase) world.getBlockTileEntity(x, y + 1, z);
			stairs[YP] = Stairs.stairsList[BlockProperties.getData(TE[YP])];
		}
		if (areStairs[XN]) {
			TE[XN] = (TEBase) world.getBlockTileEntity(x - 1, y, z);
			stairs[XN] = Stairs.stairsList[BlockProperties.getData(TE[XN])];
		}
		if (areStairs[XP]) {
			TE[XP] = (TEBase) world.getBlockTileEntity(x + 1, y, z);
			stairs[XP] = Stairs.stairsList[BlockProperties.getData(TE[XP])];
		}
		if (areStairs[ZN]) {
			TE[ZN] = (TEBase) world.getBlockTileEntity(x, y, z - 1);
			stairs[ZN] = Stairs.stairsList[BlockProperties.getData(TE[ZN])];
		}
		if (areStairs[ZP]) {
			TE[ZP] = (TEBase) world.getBlockTileEntity(x, y, z + 1);
			stairs[ZP] = Stairs.stairsList[BlockProperties.getData(TE[ZP])];
		}
	}

	/**
	 * Will begin transforming source stairs and any nearby stairs.
	 */
	public void begin()
	{
		int stairsID = BlockProperties.getData(TE[SRC]);

		if (genCorners(stairsID)) {
			return;
		} else {
			genAdjCorners(stairsID);
		}
		
		if (genHorizStairs(stairsID)) {
			return;
		}
	}

	/**
	 * Converts source block to corner if necessary, and also converts adjacent
	 * blocks to match.
	 */
	private boolean genCorners(final int stairsID)
	{
		int temp_stairsID = stairsID;
		
		Stairs stairs = Stairs.stairsList[stairsID];

		/* Check if stairs should transform into corner to match stairs behind it. */

		if (stairs.stairsType.equals(StairsType.NORMAL_Y))
		{
			if (areStairs[XN]) {
				if (stairs.facings.contains(ForgeDirection.WEST)) {
					if (this.stairs[XN].facings.contains(ForgeDirection.SOUTH) && !this.stairs[XN].facings.contains(ForgeDirection.EAST)) {
						temp_stairsID = this.stairs[XN].arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
					}
					if (this.stairs[XN].facings.contains(ForgeDirection.NORTH) && !this.stairs[XN].facings.contains(ForgeDirection.EAST)) {
						temp_stairsID = this.stairs[XN].arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
					}
				}
				if (stairs.facings.contains(ForgeDirection.EAST)) {
					if (this.stairs[XN].facings.contains(ForgeDirection.SOUTH) && !this.stairs[XN].facings.contains(ForgeDirection.EAST)) {
						temp_stairsID = this.stairs[XN].arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
					}
					if (this.stairs[XN].facings.contains(ForgeDirection.NORTH) && !this.stairs[XN].facings.contains(ForgeDirection.EAST)) {
						temp_stairsID = this.stairs[XN].arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
					}
				}
			}
			if (areStairs[XP]) {
				if (stairs.facings.contains(ForgeDirection.WEST)) {
					if (this.stairs[XP].facings.contains(ForgeDirection.SOUTH) && !this.stairs[XP].facings.contains(ForgeDirection.WEST)) {
						temp_stairsID = this.stairs[XP].arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
					}
					if (this.stairs[XP].facings.contains(ForgeDirection.NORTH) && !this.stairs[XP].facings.contains(ForgeDirection.WEST)) {
						temp_stairsID = this.stairs[XP].arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
					}
				}
				if (stairs.facings.contains(ForgeDirection.EAST)) {
					if (this.stairs[XP].facings.contains(ForgeDirection.SOUTH) && !this.stairs[XP].facings.contains(ForgeDirection.WEST)) {
						temp_stairsID = this.stairs[XP].arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
					}
					if (this.stairs[XP].facings.contains(ForgeDirection.NORTH) && !this.stairs[XP].facings.contains(ForgeDirection.WEST)) {
						temp_stairsID = this.stairs[XP].arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
					}
				}
			}
			if (areStairs[ZN]) {
				if (stairs.facings.contains(ForgeDirection.NORTH)) {
					if (this.stairs[ZN].facings.contains(ForgeDirection.EAST) && !this.stairs[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_stairsID = this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
					}
					if (this.stairs[ZN].facings.contains(ForgeDirection.WEST) && !this.stairs[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_stairsID = this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
					}
				}
				if (stairs.facings.contains(ForgeDirection.SOUTH)) {
					if (this.stairs[ZN].facings.contains(ForgeDirection.EAST) && !this.stairs[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_stairsID = this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
					}
					if (this.stairs[ZN].facings.contains(ForgeDirection.WEST) && !this.stairs[ZN].facings.contains(ForgeDirection.SOUTH)) {
						temp_stairsID = this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
					}
				}
			}
			if (areStairs[ZP]) {
				if (stairs.facings.contains(ForgeDirection.NORTH)) {
					if (this.stairs[ZP].facings.contains(ForgeDirection.EAST) && !this.stairs[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_stairsID = this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
					}
					if (this.stairs[ZP].facings.contains(ForgeDirection.WEST) && !this.stairs[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_stairsID = this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
					}
				}
				if (stairs.facings.contains(ForgeDirection.SOUTH)) {
					if (this.stairs[ZP].facings.contains(ForgeDirection.EAST) && !this.stairs[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_stairsID = this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
					}
					if (this.stairs[ZP].facings.contains(ForgeDirection.WEST) && !this.stairs[ZP].facings.contains(ForgeDirection.NORTH)) {
						temp_stairsID = this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
					}
				}
			}
		}

		/* Check if stairs should transform into corner. */

		if (areStairs[ZN]) {
			if (areStairs[XP]) {
				if (this.stairs[ZN].facings.contains(ForgeDirection.EAST) && this.stairs[XP].facings.contains(ForgeDirection.NORTH)) {
					temp_stairsID = this.stairs[XP].arePositive && this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
				}
				if (this.stairs[ZN].facings.contains(ForgeDirection.WEST) && this.stairs[XP].facings.contains(ForgeDirection.SOUTH)) {
					temp_stairsID = this.stairs[XP].arePositive && this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
				}
			}
			if (areStairs[XN]) {
				if (this.stairs[ZN].facings.contains(ForgeDirection.WEST) && this.stairs[XN].facings.contains(ForgeDirection.NORTH)) {
					temp_stairsID = this.stairs[XN].arePositive && this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
				}
				if (this.stairs[ZN].facings.contains(ForgeDirection.EAST) && this.stairs[XN].facings.contains(ForgeDirection.SOUTH)) {
					temp_stairsID = this.stairs[XN].arePositive && this.stairs[ZN].arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
				}
			}
		}
		if (areStairs[ZP]) {
			if (areStairs[XN]) {
				if (this.stairs[ZP].facings.contains(ForgeDirection.WEST) && this.stairs[XN].facings.contains(ForgeDirection.SOUTH)) {
					temp_stairsID = this.stairs[XN].arePositive && this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
				}
				if (this.stairs[ZP].facings.contains(ForgeDirection.EAST) && this.stairs[XN].facings.contains(ForgeDirection.NORTH)) {
					temp_stairsID = this.stairs[XN].arePositive && this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
				}
			}
			if (areStairs[XP]) {
				if (this.stairs[ZP].facings.contains(ForgeDirection.EAST) && this.stairs[XP].facings.contains(ForgeDirection.SOUTH)) {
					temp_stairsID = this.stairs[XP].arePositive && this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
				}
				if (this.stairs[ZP].facings.contains(ForgeDirection.WEST) && this.stairs[XP].facings.contains(ForgeDirection.NORTH)) {
					temp_stairsID = this.stairs[XP].arePositive && this.stairs[ZP].arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
				}
			}
		}

		BlockProperties.setData(TE[SRC], temp_stairsID);
		
		return stairsID != temp_stairsID;
	}
	
	/**
	 * Converts adjacent blocks to match source block.
	 */
	private void genAdjCorners(int stairsID)
	{
		Stairs stairs = Stairs.stairsList[stairsID];

		if (stairs.facings.contains(ForgeDirection.WEST)) {
			if (areStairs[ZN] && stairs.arePositive == this.stairs[ZN].arePositive) {
				if (this.stairs[ZN].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZN], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW);
				}
				if (this.stairs[ZN].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZN], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW);
				}
			}
			if (areStairs[ZP] && stairs.arePositive == this.stairs[ZP].arePositive) {
				if (this.stairs[ZP].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZP], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW);
				}
				if (this.stairs[ZP].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZP], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW);
				}
			}
		}
		if (stairs.facings.contains(ForgeDirection.EAST)) {
			if (areStairs[ZN] && stairs.arePositive == this.stairs[ZN].arePositive) {
				if (this.stairs[ZN].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZN], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE);
				}
				if (this.stairs[ZN].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZN], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE);
				}
			}
			if (areStairs[ZP] && stairs.arePositive == this.stairs[ZP].arePositive) {
				if (this.stairs[ZP].facings.contains(ForgeDirection.SOUTH)) {
					BlockProperties.setData(TE[ZP], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE);
				}
				if (this.stairs[ZP].facings.contains(ForgeDirection.NORTH)) {
					BlockProperties.setData(TE[ZP], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE);
				}
			}
		}
		if (stairs.facings.contains(ForgeDirection.NORTH)) {
			if (areStairs[XN] && stairs.arePositive == this.stairs[XN].arePositive) {
				if (this.stairs[XN].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XN], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW);
				}
				if (this.stairs[XN].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XN], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE);
				}
			}
			if (areStairs[XP] && stairs.arePositive == this.stairs[XP].arePositive) {
				if (this.stairs[XP].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XP], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE);
				}
				if (this.stairs[XP].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XP], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW);
				}
			}
		}
		if (stairs.facings.contains(ForgeDirection.SOUTH)) {
			if (areStairs[XN] && stairs.arePositive == this.stairs[XN].arePositive) {
				if (this.stairs[XN].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XN], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW);
				}
				if (this.stairs[XN].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XN], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE);
				}
			}
			if (areStairs[XP] && stairs.arePositive == this.stairs[XP].arePositive) {
				if (this.stairs[XP].facings.contains(ForgeDirection.EAST)) {
					BlockProperties.setData(TE[XP], stairs.arePositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE);
				}
				if (this.stairs[XP].facings.contains(ForgeDirection.WEST)) {
					BlockProperties.setData(TE[XP], stairs.arePositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW);
				}
			}
		}
	}
	
	/**
	 * Check if stairs above or below are horizontal and make this a continuation.
	 */
	private boolean genHorizStairs(final int stairsID)
	{
		int temp_stairsID = stairsID;
		
		if (areStairs[YP]) {
			if (stairs[YP].stairsType.equals(StairsType.NORMAL_XZ)) {
				temp_stairsID = stairs[YP].stairsID;
			}
		}
		if (areStairs[YN]) {
			if (stairs[YN].stairsType.equals(StairsType.NORMAL_XZ)) {
				temp_stairsID = stairs[YN].stairsID;
			}
		}
		
		BlockProperties.setData(TE[SRC], temp_stairsID);
		
		return stairsID != temp_stairsID;
	}

}
