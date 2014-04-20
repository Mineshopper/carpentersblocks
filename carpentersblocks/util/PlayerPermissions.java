package carpentersblocks.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import carpentersblocks.util.registry.FeatureRegistry;

public class PlayerPermissions {
    
	/**
	 * Returns true if EntityPlayer is operator or owner of IProtected object.
	 */
	private static boolean isOwner(IProtected object, EntityPlayer entityPlayer)
	{
		String owner = object.getOwner();
		
		return owner.equals(entityPlayer.getDisplayName()) || owner.equals("") || isOp(entityPlayer);
	}
	
    /**
     * Returns true if player is operator.
     * Can only return true if called server-side.
     */
	private static boolean isOp(EntityPlayer entityPlayer)
    {
        if (!entityPlayer.worldObj.isRemote) {
            return ((EntityPlayerMP)entityPlayer).mcServer.getConfigurationManager().isPlayerOpped(entityPlayer.getDisplayName());
        } else {
            return false;
        }
    }

    /**
     * Returns whether player is allowed to make alterations to object.
     */
    public static boolean canPlayerEdit(IProtected object, int x, int y, int z, EntityPlayer entityPlayer)
    {
        if (isOp(entityPlayer)) {
            return true;
        } else if (FeatureRegistry.enableOwnership) {
            return isOwner(object, entityPlayer);
        } else {
            return entityPlayer.canPlayerEdit(x, y, z, 0, entityPlayer.getHeldItem());
        }
    }
        
}
