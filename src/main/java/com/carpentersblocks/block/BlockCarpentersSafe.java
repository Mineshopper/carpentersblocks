package com.carpentersblocks.block;

import java.util.ArrayList;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.CarpentersBlocks;
import com.carpentersblocks.data.Safe;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.tileentity.TECarpentersSafe;
import com.carpentersblocks.util.BlockProperties;
import com.carpentersblocks.util.EntityLivingUtil;
import com.carpentersblocks.util.handler.ChatHandler;
import com.carpentersblocks.util.protection.PlayerPermissions;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersSafe extends BlockCoverable {

    /** OreDictionary names for safe upgrade materials. */
    public final static String[] upgradeOres = { "ingotGold", "gemDiamond", "gemEmerald" };

    /** ItemStacks that represent panel material for {@link upgradeOres}. */
    public final static ItemStack[] upgradeStack = { new ItemStack(Blocks.gold_block), new ItemStack(Blocks.diamond_block), new ItemStack(Blocks.emerald_block) };

    public BlockCarpentersSafe(Material material)
    {
        super(material);
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        IconRegistry.icon_safe_light = iconRegister.registerIcon(CarpentersBlocks.MODID + ":" + "safe/safe_light");
    }

    /**
     * Returns whether player is allowed to activate this block.
     */
    @Override
    protected boolean canPlayerActivate(TEBase TE, EntityPlayer entityPlayer)
    {
        return PlayerPermissions.canPlayerEdit(TE, TE.xCoord, TE.yCoord, TE.zCoord, entityPlayer) || !Safe.isLocked(TE);
    }

    @Override
    /**
     * Rotates safe so that it faces player.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        int rot = EntityLivingUtil.getRotationValue(entityPlayer);
        ForgeDirection dir = EntityLivingUtil.getRotationFacing(rot).getOpposite();

        if (dir != Safe.getFacing(TE)) {
            Safe.setFacing(TE, rot);
            return true;
        } else {
            return false;
        }
    }

    @Override
    /**
     * Cycles locked state.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        if (entityPlayer.isSneaking()) {

            boolean locked = !Safe.isLocked(TE);

            Safe.setLocked(TE, locked);

            if (locked) {
                Safe.setAutoPerm(TE, Safe.AUTOMATION_DISABLED);
            } else {
                Safe.setAutoPerm(TE, Safe.AUTOMATION_ALL);
            }

            if (locked) {
                ChatHandler.sendMessageToPlayer("message.safe_lock.name", entityPlayer);
            } else {
                ChatHandler.sendMessageToPlayer("message.safe_unlock.name", entityPlayer);
            }

            return true;

        } else {

            int autoPerm = Safe.getAutoPerm(TE);

            if (++autoPerm > 3) {
                autoPerm = 0;
            }

            Safe.setAutoPerm(TE, autoPerm);

            switch (autoPerm) {
                case Safe.AUTOMATION_ALL:
                    ChatHandler.sendMessageToPlayer("message.automation_all.name", entityPlayer);
                    break;
                case Safe.AUTOMATION_DISABLED:
                    ChatHandler.sendMessageToPlayer("message.automation_disabled.name", entityPlayer);
                    break;
                case Safe.AUTOMATION_RECEIVE:
                    ChatHandler.sendMessageToPlayer("message.automation_insert.name", entityPlayer);
                    break;
                case Safe.AUTOMATION_SEND:
                    ChatHandler.sendMessageToPlayer("message.automation_extract.name", entityPlayer);
                    break;
            }

        }

        return true;
    }

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            Safe.setFacing(TE, EntityLivingUtil.getRotationValue(entityLiving));
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    /**
     * Checks if {@link ItemStack} contains safe upgrade.
     *
     * @return <code>true</code> if {@link ItemStack} contains safe upgrade
     */
    public static boolean isUpgrade(ItemStack itemStack)
    {
        return !BlockProperties.getOreDictMatch(itemStack, upgradeOres).equals("");
    }

    @Override
    /**
     * Called upon block activation (right click on the block.)
     */
    protected void postOnBlockActivated(TEBase TE, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ, ActionResult actionResult)
    {
        actionResult.setAltered();

        if (!Safe.isOpen(TE) && canPlayerActivate(TE, entityPlayer)) {

            if (entityPlayer.isSneaking()) {
                ItemStack itemStack = entityPlayer.getHeldItem();
                if (itemStack != null && isUpgrade(itemStack) && !TE.hasAttribute(TE.ATTR_UPGRADE)) {
                    TE.addAttribute(TE.ATTR_UPGRADE, itemStack);
                    actionResult.decInventory().setSoundSource(itemStack);
                    return;
                }
            }

            if (!actionResult.decInv) {
                actionResult.setNoSound();
                entityPlayer.displayGUIChest((TECarpentersSafe)TE);
            }

        } else {

            ChatHandler.sendMessageToPlayer("message.block_lock.name", entityPlayer);

        }
    }

    /**
     * Location sensitive version of getExplosionRestance
     *
     * @param par1Entity The entity that caused the explosion
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param explosionX Explosion source X Position
     * @param explosionY Explosion source X Position
     * @param explosionZ Explosion source X Position
     * @return The amount of the explosion absorbed.
     */
    @Override
    public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
    {
        return Blocks.bedrock.getExplosionResistance(entity);
    }

    @Override
    /**
     * Checks if the block is a solid face on the given side, used by placement logic.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @param side The side to check
     * @return True if the block is solid on the specified side.
     */
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (isBlockSolid(world, x, y, z)) {
                return side != Safe.getFacing(TE);
            }
        }

        return false;
    }

    /**
     * This returns a complete list of items dropped from this block.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param metadata Current metadata
     * @param fortune Breakers fortune level
     * @return A ArrayList containing all items this block drops
     */
    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = super.getDrops(world, x, y, z, metadata, fortune);
        TEBase TE = getSimpleTileEntity(world, x, y, z);

        if (TE != null && TE instanceof TECarpentersSafe) {
            if (TE.hasAttribute(TE.ATTR_UPGRADE)) {
                ret.add(TE.getAttribute(TE.ATTR_UPGRADE));
            }
            for (int slot = 0; slot < ((TECarpentersSafe)TE).getSizeInventory(); ++slot) {
                ItemStack itemStack = ((TECarpentersSafe)TE).getStackInSlot(slot);
                if (itemStack != null) {
                    ret.add(itemStack);
                }
            }
        }

        return ret;
    }

    @Override
    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    public float getPlayerRelativeBlockHardness(EntityPlayer entityPlayer, World world, int x, int y, int z)
    {
        TEBase TE = getTileEntity(world, x, y, z);

        if (TE != null) {
            if (Safe.isOpen(TE) || !PlayerPermissions.canPlayerEdit(TE, TE.xCoord, TE.yCoord, TE.zCoord, entityPlayer)) {
                return -1; // Unbreakable
            } else {
                return super.getPlayerRelativeBlockHardness(entityPlayer, world, x, y, z);
            }
        }

        return super.getPlayerRelativeBlockHardness(entityPlayer, world, x, y, z);
    }

    @Override
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        return new TECarpentersSafe();
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersSafeRenderID;
    }

	@Override
	public ForgeDirection[] getValidRotations(World worldObj, int x, int y,int z)
	{
		ForgeDirection[] axises = {ForgeDirection.UP, ForgeDirection.DOWN};
		return axises;
	}

	@Override
	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis)
	{
		// to correctly support archimedes' ships mod:
		// if Axis is DOWN, block rotates to the left, north -> west -> south -> east
		// if Axis is UP, block rotates to the right:  north -> east -> south -> west

		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TEBase)
		{
			TEBase cbTile = (TEBase)tile;
			ForgeDirection direction = Safe.getFacing(cbTile);
			switch (axis)
			{
				case UP:
				{
					switch (direction)
					{
						case NORTH:{Safe.setFacing(cbTile, 1); break;}
						case EAST:{Safe.setFacing(cbTile, 2); break;}
						case WEST:{Safe.setFacing(cbTile, 3); break;}
						case SOUTH:{Safe.setFacing(cbTile, 0); break;}
						default: break;
					}
					break;
				}
				case DOWN:
				{
					switch (direction)
					{
						case NORTH:{Safe.setFacing(cbTile, 3); break;}
						case EAST:{Safe.setFacing(cbTile, 0); break;}
						case SOUTH:{Safe.setFacing(cbTile, 1); break;}
						case WEST:{Safe.setFacing(cbTile, 2); break;}
						default: break;
					}
					break;
				}
				default: return false;
			}
			return true;
		}
		return false;
	}

}
