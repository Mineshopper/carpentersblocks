package com.carpentersblocks.entity.item;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import com.carpentersblocks.util.protection.IProtected;
import com.carpentersblocks.util.protection.ProtectedUtil;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class EntityBase extends Entity implements IProtected {

    private final static byte   ID_OWNER  = 12;
    private final static String TAG_OWNER = "owner";

    public EntityBase(World world)
    {
        super(world);
    }

    public EntityBase(World world, UUID uuid)
    {
        this(world);
        setOwner(uuid);
    }

    @Override
    public void setOwner(UUID uuid)
    {
        getDataWatcher().updateObject(ID_OWNER, new String(uuid.toString()));
    }

    @Override
    public String getOwner()
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

        /*
         * Attempt to update owner name to new UUID format.
         * TODO: Remove when player name-changing system is switched on
         */
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
