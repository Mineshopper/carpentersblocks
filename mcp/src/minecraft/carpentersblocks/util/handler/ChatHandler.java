package carpentersblocks.util.handler;

import net.minecraft.entity.player.EntityPlayer;

public class ChatHandler {

    /**
     * Sends a chat message to a player.
     */
    public static void sendMessageToPlayer(String string, EntityPlayer entityPlayer)
    {
        sendMessageToPlayer(string, entityPlayer, true);
    }

    /**
     * Sends a chat message to a player with style override.
     */
    public static void sendMessageToPlayer(String string, EntityPlayer entityPlayer, boolean styled)
    {
        entityPlayer.addChatMessage(string);
    }

}
