package carpentersblocks.util.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

public class ChatHandler {
    
    /**
     * Sends a chat message to a player.
     */
    public static void sendMessageToPlayer(String string, EntityPlayer entityPlayer)
    {
        entityPlayer.addChatMessage(new ChatComponentTranslation(string, new Object[0]));
    }
    
}
