package com.carpentersblocks.util.protection;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

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
            if (((EntityPlayerMP)entityPlayer).mcServer.isServerInOnlineMode()) {
                return entityPlayer.getUniqueID().toString();
            }
        }

        return entityPlayer.getDisplayNameString();
    }

}
