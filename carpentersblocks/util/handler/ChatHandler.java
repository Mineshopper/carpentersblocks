package carpentersblocks.util.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

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
        ChatComponentTranslation chat = new ChatComponentTranslation(string, new Object[0]);
        
        if (styled) {
            chat.getChatStyle().setColor(EnumChatFormatting.GOLD);
        }
        
        entityPlayer.addChatMessage(chat);
    }
    
}
