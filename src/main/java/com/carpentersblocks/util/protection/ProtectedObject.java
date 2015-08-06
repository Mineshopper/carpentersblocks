package com.carpentersblocks.util.protection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ProtectedObject {

    public EntityPlayer entityPlayer;

    public ProtectedObject(EntityPlayer entityPlayer)
    {
        this.entityPlayer = entityPlayer;
    }

    @Override
    public String toString()
    {
        if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
            boolean onlineMode = ((EntityPlayerMP)entityPlayer).mcServer.isServerInOnlineMode();
            if (onlineMode) {
                return entityPlayer.getUniqueID().toString();
            } else {
                return entityPlayer.getDisplayName();
            }
        } else {
            return entityPlayer.getDisplayName();
        }
    }

}
