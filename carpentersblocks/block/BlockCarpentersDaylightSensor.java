package carpentersblocks.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.DaylightSensor;
import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.tileentity.TECarpentersBlockExt;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.BlockHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersDaylightSensor extends BlockBase
{

	public BlockCarpentersDaylightSensor(int blockID)
	{
		super(blockID, Material.wood);
		setHardness(0.2F);
		setUnlocalizedName("blockCarpentersDaylightSensor");
		setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
		func_111022_d("carpentersblocks:general/generic");
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
	 */
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
	{
		return side == 1 ? Block.daylightSensor.getBlockTextureFromSide(1) : super.getBlockTexture(world, x, y, z, side);
	}

	@Override
	/**
	 * Alters polarity.
	 */
	protected boolean onHammerLeftClick(TECarpentersBlock TE, EntityPlayer entityPlayer)
	{
		int data = BlockProperties.getData(TE);
		int polarity = DaylightSensor.getPolarity(data) == DaylightSensor.POLARITY_POSITIVE ? DaylightSensor.POLARITY_NEGATIVE : DaylightSensor.POLARITY_POSITIVE;

		if (!TE.worldObj.isRemote) {
			DaylightSensor.setPolarity(TE, polarity);
		} else {
			switch (polarity) {
			case DaylightSensor.POLARITY_POSITIVE:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.activation_day.name"));
				break;
			case DaylightSensor.POLARITY_NEGATIVE:
				entityPlayer.addChatMessage(LanguageRegistry.instance().getStringLocalization("message.activation_night.name"));
			}
		}

		return true;
	}

	@Override
	/**
	 * Returns true if the block is emitting indirect/weak redstone power on the specified side. If isBlockNormalCube
	 * returns true, standard redstone propagation rules will apply instead and this will not be called. Args: World, X,
	 * Y, Z, side. Note that the side is reversed - eg it is 1 (up) when checking the bottom of the block.
	 */
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
	{
		TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

		int data = BlockProperties.getData(TE);
		int type = DaylightSensor.getType(data);

		boolean isDay = type > 2;
		boolean outputDay = DaylightSensor.getPolarity(data) == DaylightSensor.POLARITY_POSITIVE;

		return isDay ? (outputDay ? type : 0) : (!outputDay ? 15 : 0);
	}

	public void updateLightLevel(World world, int x, int y, int z)
	{
		if (!world.provider.hasNoSky)
		{
			TECarpentersBlock TE = (TECarpentersBlock)world.getBlockTileEntity(x, y, z);

			int data = BlockProperties.getData(TE);
			int old_lightValue = DaylightSensor.getType(data);

			int lightValue = world.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) - world.skylightSubtracted;
			float lightAngle = world.getCelestialAngleRadians(1.0F);

			if (lightAngle < (float)Math.PI) {
				lightAngle += (0.0F - lightAngle) * 0.2F;
			} else {
				lightAngle += (((float)Math.PI * 2F) - lightAngle) * 0.2F;
			}

			lightValue = Math.round(lightValue * MathHelper.cos(lightAngle));

			if (lightValue < 0) {
				lightValue = 0;
			} else if (lightValue > 15) {
				lightValue = 15;
			}

			if (old_lightValue != lightValue)
			{
				DaylightSensor.setType(TE, lightValue);
				world.notifyBlocksOfNeighborChange(x, y, z, blockID);
			}
		}
	}

	@Override
	/**
	 * Can this block provide power. Only wire currently seems to have this change based on its state.
	 */
	 public boolean canProvidePower()
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TECarpentersBlockExt();
	}

	@Override
	/**
	 * The type of render function that is called for this block
	 */
	public int getRenderType()
	{
		return BlockHandler.carpentersDaylightSensorRenderID;
	}

}
