package com.carpentersblocks.util.protection;

import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import com.carpentersblocks.util.registry.FeatureRegistry;

public class PlayerPermissions {

    /**
     * Returns true if player is operator.
     * Can only return true if called server-side.
     */
    public static boolean isOp(EntityPlayer entityPlayer)
    {
        if (!entityPlayer.worldObj.isRemote) {
            return ((EntityPlayerMP)entityPlayer).mcServer.getConfigurationManager().func_152596_g(entityPlayer.getGameProfile());
        } else {
            return false;
        }
    }

    /**
     * If player is an op or the owner of the {@link IProtected} object,
     * they will have the necessary permissions to activate and/or destroy
     * a protected block.
     *
     * @param  object the {@link IProtected} block or entity
     * @param  entityPlayer the {@link EntityPlayer}
     * @return <code>true</code> if player has elevated permission
     */
    public static boolean hasElevatedPermission(IProtected object, EntityPlayer entityPlayer)
    {
        return isOp(entityPlayer) || isOwner(object, entityPlayer);
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

    /**
     * Whether the player is the owner of the object.
     *
     * @param object
     * @param entityPlayer
     * @return <code>true</code> if player is owner
     */
    private static boolean isOwner(IProtected object, EntityPlayer entityPlayer)
    {
        try {
            UUID.fromString(object.getOwner());
            return object.getOwner().equals(entityPlayer.getUniqueID().toString());
        } catch (IllegalArgumentException e) {
            return object.getOwner().equals(entityPlayer.getDisplayName());
        }
    }

}
