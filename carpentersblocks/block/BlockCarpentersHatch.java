package carpentersblocks.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import carpentersblocks.CarpentersBlocks;
import carpentersblocks.data.Hatch;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;
import carpentersblocks.util.registry.IconRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCarpentersHatch extends BlockBase {

    public BlockCarpentersHatch(int blockID)
    {
        super(blockID, Material.wood);
        setHardness(0.2F);
        setUnlocalizedName("blockCarpentersHatch");
        setCreativeTab(CarpentersBlocks.tabCarpentersBlocks);
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
        IconRegistry.icon_hatch_glass = iconRegister.registerIcon("carpentersblocks:hatch/hatch_glass");
        IconRegistry.icon_hatch_french_glass = iconRegister.registerIcon("carpentersblocks:hatch/hatch_french_glass");
        IconRegistry.icon_hatch_screen = iconRegister.registerIcon("carpentersblocks:hatch/hatch_screen");

        super.registerIcons(iconRegister);
    }

    @Override
    /**
     * Alters direction based on valid sides detected.
     */
    protected boolean onHammerLeftClick(TEBase TE, EntityPlayer entityPlayer)
    {
        BlockProperties.getData(TE);

        if (!TE.worldObj.isRemote) {
            findNextSideSupportBlock(TE, TE.worldObj, TE.xCoord, TE.yCoord, TE.zCoord);
        }

        return true;
    }

    @Override
    /**
     * Alters hatch type and redstone behavior.
     */
    protected boolean onHammerRightClick(TEBase TE, EntityPlayer entityPlayer)
    {
        if (!entityPlayer.isSneaking()) {

            int type = Hatch.getType(TE);

            if (++type > 4) {
                type = 0;
            }

            Hatch.setType(TE, type);

        } else {

            int rigidity = Hatch.getRigidity(TE) == Hatch.HINGED_NONRIGID ? Hatch.HINGED_RIGID : Hatch.HINGED_NONRIGID;

            Hatch.setRigidity(TE, rigidity);

            switch (rigidity) {
                case Hatch.HINGED_NONRIGID:
                    entityPlayer.addChatMessage("message.activation_wood.name");
                    break;
                case Hatch.HINGED_RIGID:
                    entityPlayer.addChatMessage("message.activation_iron.name");
            }

        }

        return true;
    }

    @Override
    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean[] postOnBlockActivated(TEBase TE, World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
        if (!activationRequiresRedstone(TE)) {
            Hatch.setState(TE, Hatch.getState(TE) == Hatch.STATE_CLOSED ? Hatch.STATE_OPEN : Hatch.STATE_CLOSED);
            boolean[] result = { true, false };
            return result;
        }

        return super.postOnBlockActivated(TE, world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
    }

    /**
     * Returns whether hatch requires redstone activation.
     */
    private boolean activationRequiresRedstone(TEBase TE)
    {
        return Hatch.getRigidity(TE) == Hatch.HINGED_RIGID;
    }

    @Override
    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        boolean isHigh = Hatch.getPos(TE) == Hatch.POSITION_HIGH;
        boolean isOpen = Hatch.getState(TE) == Hatch.STATE_OPEN;
        int dir = Hatch.getDir(TE);

        float thickness = 0.1875F;

        /* Hidden type has reduced dimensions to assist in climbing */
        if (Hatch.getType(TE) == Hatch.TYPE_HIDDEN) {
            thickness = 0.125F;
        }

        if (isHigh) {
            setBlockBounds(0.0F, 1.0F - thickness, 0.0F, 1.0F, 1.0F, 1.0F);
        } else {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, thickness, 1.0F);
        }

        if (isOpen)
        {
            switch (dir) {
                case 0:
                    setBlockBounds(0.0F, 0.0F, 1.0F - thickness, 1.0F, 1.0F, 1.0F);
                    break;
                case 1:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, thickness);
                    break;
                case 2:
                    setBlockBounds(1.0F - thickness, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;
                case 3:
                    setBlockBounds(0.0F, 0.0F, 0.0F, thickness, 1.0F, 1.0F);
                    break;
            }
        }
    }

    @Override
    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisAlignedBB, List list, Entity entity)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, axisAlignedBB, list, entity);
    }

    @Override
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockID)
    {
        if (!world.isRemote)
        {
            TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

            if (TE != null)
            {

                int dir = Hatch.getDir(TE);
                int state = Hatch.getState(TE);

                int xOffset = x;
                int zOffset = z;

                switch (dir) {
                    case Hatch.DIR_Z_NEG:
                        zOffset = z + 1;
                        break;
                    case Hatch.DIR_Z_POS:
                        --zOffset;
                        break;
                    case Hatch.DIR_X_NEG:
                        xOffset = x + 1;
                        break;
                    case Hatch.DIR_X_POS:
                        --xOffset;
                        break;
                }

                if (!(isValidSupportBlock(world, x, y, z, world.getBlockId(xOffset, y, zOffset), dir + 2) || world.isBlockSolidOnSide(xOffset, y, zOffset, ForgeDirection.getOrientation(dir + 2)))) {
                    findNextSideSupportBlock(TE, world, x, y, z);
                }

                boolean isPowered = world.isBlockIndirectlyGettingPowered(x, y, z);
                boolean isOpen = state == Hatch.STATE_OPEN;

                if (blockID > 0 && Block.blocksList[blockID].canProvidePower() && isPowered != isOpen) {
                    Hatch.setState(TE, state == Hatch.STATE_OPEN ? Hatch.STATE_CLOSED : Hatch.STATE_OPEN);
                }
            }
        }

        super.onNeighborBlockChange(world, x, y, z, blockID);
    }

    @Override
    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

    @Override
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        int initData = 0;

        if (side > 1) {
            initData = side - 2;
        }

        // Hatch on upper half of block
        if (side != 1 && side != 0 && hitY > 0.5F) {
            initData |= 8;
        }

        return initData;
    }

    @Override
    /**
     * Called when the block is placed in the world.
     * Uses cardinal direction to adjust metadata if player clicks top or bottom face of block.
     */
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        int metadata = world.getBlockMetadata(x, y, z);

        Hatch.setDir(TE, metadata & 0x3);

        if ((metadata & 0x8) > 0) {
            Hatch.setPos(TE, Hatch.POSITION_HIGH);
        }

        super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);
    }

    @Override
    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side)
    {
        switch (side) {
            case 2:
                return isValidSupportBlock(world, x, y, z, world.getBlockId(x, y, z + 1), 3) || world.isBlockSolidOnSide(x, y, z + 1, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[3]));
            case 3:
                return isValidSupportBlock(world, x, y, z, world.getBlockId(x, y, z - 1), 2) || world.isBlockSolidOnSide(x, y, z - 1, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[2]));
            case 4:
                return isValidSupportBlock(world, x, y, z, world.getBlockId(x + 1, y, z), 5) || world.isBlockSolidOnSide(x + 1, y, z, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[5]));
            case 5:
                return isValidSupportBlock(world, x, y, z, world.getBlockId(x - 1, y, z), 4) || world.isBlockSolidOnSide(x - 1, y, z, ForgeDirection.getOrientation(ForgeDirection.OPPOSITES[4]));
        }

        return false;
    }

    /**
     * Will find and set a new direction for hatch if an adjacent block can support it.
     * If nothing is found, block will break.
     */
    private void findNextSideSupportBlock(TEBase TE, World world, int x, int y, int z)
    {
        int dir = Hatch.getDir(TE);

        if (++dir > 3) {
            dir = 0;
        }

        /*
         * This block will rotate until it finds a suitable
         * support block.  It will drop if nothing is found.
         */
        int count = 0;
        while (!this.canPlaceBlockOnSide(world, x, y, z, dir + 2) && count < 4)
        {
            if (++dir > 3) {
                dir = 0;
            }

            ++count;
        }

        if (count == 4) {
            world.setBlockToAir(x, y, z);
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
        } else {
            Hatch.setDir(TE, dir);
        }
    }

    @Override
    public boolean isLadder(World world, int x, int y, int z, EntityLivingBase entityLiving)
    {
        TEBase TE = (TEBase) world.getBlockTileEntity(x, y, z);

        return Hatch.getType(TE) == Hatch.TYPE_HIDDEN &&
                Hatch.getPos(TE) == Hatch.POSITION_HIGH &&
                Hatch.getState(TE) == Hatch.STATE_OPEN;
    }

    /**
     * Checks if the block ID is a valid support block for the hatch to connect with. If it is not the hatch is
     * dropped into the world.
     */
    private boolean isValidSupportBlock(World world, int x, int y, int z, int blockID, int side)
    {
        Block block = Block.blocksList[blockID];

        return block == Block.glowStone ||
                block instanceof BlockCarpentersStairs ||
                block instanceof BlockCarpentersBlock ||
                block instanceof BlockHalfSlab ||
                block instanceof BlockStairs;
    }

    @Override
    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return BlockRegistry.carpentersHatchRenderID;
    }

}
