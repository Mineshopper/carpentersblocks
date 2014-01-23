package carpentersblocks.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Safe;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.tileentity.TECarpentersSafe;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.handler.EventHandler;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersSafe extends BlockBase {

    public BlockCarpentersSafe(int blockID)
    {
        super(blockID, Material.wood);
        setUnlocalizedName("blockCarpentersSafe");
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
        setHardness(2.5F);
        setTextureName("carpentersblocks:general/solid");
    }

    @SideOnly(Side.CLIENT)
    @Override
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconRegister)
    {
        IconRegistry.icon_safe_light = iconRegister.registerIcon("carpentersblocks:safe/safe_light");

        super.registerIcons(iconRegister);
    }

    /**
     * Returns whether player is allowed to make alterations to this block.
     * This does not include block activation.  For that, use canPlayerActivate().
     */
    @Override
    protected boolean canPlayerEdit(TEBase TE, EntityLivingBase entityLiving)
    {
        if (isOp(entityLiving)) {
            return true;
        } else {
            return ((EntityPlayer)entityLiving).canPlayerEdit(TE.xCoord, TE.yCoord, TE.zCoord, EventHandler.eventFace, entityLiving.getHeldItem()) &&
                    TE.isOwner(entityLiving);
        }
    }

    /**
     * Returns whether player is allowed to activate this block.
     */
    @Override
    protected boolean canPlayerActivate(TEBase TE, EntityLivingBase entityLiving)
    {
        return isOp(entityLiving) || TE.isOwner(entityLiving) || !Safe.isLocked(TE);
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
                entityPlayer.addChatMessage("message.safe_lock.name");
            } else {
                entityPlayer.addChatMessage("message.safe_unlock.name");
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
                entityPlayer.addChatMessage("message.automation_all.name");
                break;
            case Safe.AUTOMATION_DISABLED:
                entityPlayer.addChatMessage("message.automation_disabled.name");
                break;
            case Safe.AUTOMATION_RECEIVE:
                entityPlayer.addChatMessage("message.automation_insert.name");
                break;
            case Safe.AUTOMATION_SEND:
                entityPlayer.addChatMessage("message.automation_extract.name");
                break;
            }

        }

        return true;
    }

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param world The current world
     * @param player The player damaging the block, may be null
     * @param x X Position
     * @param y Y position
     * @param z Z position
     * @return True if the block is actually destroyed.
     */
    /*
    @Override
    public boolean removeBlockByPlayer(World world, EntityPlayer entityPlayer, int x, int y, int z)
    {
        TECarpentersSafe TE = (TECarpentersSafe) world.getBlockTileEntity(x, y, z);

        if (!world.isRemote && !Safe.isOpen(TE))
        {
            if (entityPlayer == null) {
                return !Safe.isLocked(TE);
            } else {
                if (canPlayerEdit(TE, entityPlayer)) {
                    return super.removeBlockByPlayer(world, entityPlayer, x, y, z);
                } else {
                    return false;
                }
            }
        }

        return false;
    }
     */

    @Override
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        Safe.setFacing(TE, BlockProperties.getOppositeFacing(BlockProperties.getEntityFacing(entityLiving)));

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * Called upon block activation (right click on the block.)
     */
    protected boolean[] postOnBlockActivated(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        int DEC_INV = 1;
        boolean[] result = { true, false };

        if (!Safe.isOpen(TE) && canPlayerActivate(TE, entityPlayer)) {

            TECarpentersSafe TE_safe = (TECarpentersSafe) TE;
            ItemStack itemStack = entityPlayer.getHeldItem();

            if (itemStack != null && itemStack.getItem().equals(Item.ingotGold))
            {
                if (!TE_safe.hasUpgrade())
                {
                    if (TE_safe.incSizeInventory()) {
                        world.markBlockForUpdate(x, y, z);
                        result[DEC_INV] = true;
                        return result;
                    }
                }
            }

            if (!result[DEC_INV]) {
                entityPlayer.displayGUIChest((TECarpentersSafe)TE);
            }

        } else {

            entityPlayer.addChatMessage("message.block_lock.name");

        }

        /*
         * Safe should always return true because it either warns the player
         * that it is locked, or it returns the GUI.
         */
        return result;
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
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        if (isBlockSolid(world, x, y, z)) {
            TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);
            return side != Safe.getFacing(TE);
        }

        return false;
    }

    @Override
    /**
     * ejects contained items into the world, and notifies neighbours of an update, as appropriate
     */
    public void breakBlock(World world, int x, int y, int z, int par5, int metadata)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        if (TE != null && !Safe.isOpen(TE)) {

            TECarpentersSafe TE_safe = (TECarpentersSafe) TE;

            if (TE_safe.hasUpgrade()) {
                BlockProperties.ejectEntity(TE, new ItemStack(Item.ingotGold));
            }

            for (int slot = 0; slot < TE_safe.getSizeInventory(); ++slot)
            {
                ItemStack itemStack = TE_safe.getStackInSlot(slot);

                if (itemStack != null) {
                    BlockProperties.ejectEntity(TE, itemStack);
                }
            }

        }

        super.breakBlock(world, x, y, z, par5, metadata);
    }

    @Override
    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    public float getPlayerRelativeBlockHardness(EntityPlayer entityPlayer, World world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        if (Safe.isOpen(TE) || !entityPlayer.equals(BlockProperties.getOwner(TE))) {
            return -1; // Unbreakable
        } else {
            return super.getPlayerRelativeBlockHardness(entityPlayer, world, x, y, z);
        }
    }

    @Override
    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World world)
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

}
