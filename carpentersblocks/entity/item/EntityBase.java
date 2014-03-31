package carpentersblocks.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import carpentersblocks.util.IProtected;

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
        setOwner(entityPlayer.getDisplayName());
    }

    @Override
    public void setOwner(String name)
    {
        getDataWatcher().updateObject(ID_OWNER, new String(name));
    }
    
    @Override
    public String getOwner()
    {
        return getDataWatcher().getWatchableObjectString(ID_OWNER);
    }
    
    /**
     * Returns true if entityPlayer is owner of tile entity.
     */
    public boolean isOwner(EntityPlayer entityPlayer)
    {
        return getOwner().equals(entityPlayer.getDisplayName()) || getOwner().equals("");
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
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setString(TAG_OWNER, getDataWatcher().getWatchableObjectString(ID_OWNER));        
    }
    
}
