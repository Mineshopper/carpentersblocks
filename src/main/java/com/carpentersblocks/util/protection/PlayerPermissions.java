package com.carpentersblocks.util.protection;

import java.util.UUID;

import com.carpentersblocks.util.registry.FeatureRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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
     * Whether player is an operator, owner or is in a singleplayer server.
     *
     * @param  object the {@link IProtected} block or entity
     * @param  entityPlayer the {@link EntityPlayer}
     * @return <code>true</code> if player has elevated permission
     */
    public static boolean hasElevatedPermission(IProtected object, EntityPlayer entityPlayer)
    {
        if (entityPlayer.worldObj.isRemote && Minecraft.getMinecraft().isSingleplayer()) { // Check if client is playing singleplayer
            return true;
        } else if (!entityPlayer.worldObj.isRemote && MinecraftServer.getServer().isSinglePlayer()) { // Check if server is integrated (singleplayer)
            return true;
        } else if (isOp(entityPlayer)) {
            return true;
        } else {
            return isOwner(object, entityPlayer);
        }
    }

    /**
     * Whether the player is the owner of the object.
     *
     * @param object
     * @param entityPlayer
     * @return <code>true</code> if player is owner or {@link FeatureRegistry#enableOwnership} is <code>false</code>
     */
    private static boolean isOwner(IProtected object, EntityPlayer entityPlayer)
    {
    	if (!FeatureRegistry.enableOwnership) {
    		return true;
    	}
    	
        try {
            UUID.fromString(object.getOwner());
            return object.getOwner().equals(entityPlayer.getUniqueID().toString());
        } catch (IllegalArgumentException e) {
            return object.getOwner().equals(entityPlayer.getDisplayName());
        }
    }

}
