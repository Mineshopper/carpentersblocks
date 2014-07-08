package carpentersblocks.entity.item;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import carpentersblocks.util.protection.IProtected;
import carpentersblocks.util.protection.ProtectedUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class EntityBase extends Entity implements IProtected {

    private final static byte ID_OWNER  = 12;

    private final static String TAG_OWNER = "owner";

    public EntityBase(World world)
    {
        super(world);
    }

    public EntityBase(World world, EntityPlayer entityPlayer)
    {
        this(world);
        setOwner(entityPlayer.getUniqueID());
    }

    @Override
    public void setOwner(UUID Id)
    {
        getDataWatcher().updateObject(ID_OWNER, new String(Id.toString()));
    }

    @Override
    public UUID getOwner() throws IllegalArgumentException
    {
        return UUID.fromString(getDataWatcher().getWatchableObjectString(ID_OWNER));
    }

    @Override
    public Object getOwnerRaw()
    {
        return getDataWatcher().getWatchableObjectString(ID_OWNER);
    }

    @Override
    protected void entityInit()
    {
        getDataWatcher().addObject(ID_OWNER, new String(""));
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        getDataWatcher().updateObject(ID_OWNER, String.valueOf(nbtTagCompound.getString(TAG_OWNER)));

        // TODO: Remove when player name-changing system is switched on
        if (FMLCommonHandler.instance().getSide().equals(Side.SERVER)) {
            ProtectedUtil.updateOwnerUUID(this);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setString(TAG_OWNER, getDataWatcher().getWatchableObjectString(ID_OWNER));
    }

}
