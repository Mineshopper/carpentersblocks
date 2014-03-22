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
        ChatComponentTranslation chat = new ChatComponentTranslation(string, new Object[0]);
        chat.getChatStyle().setColor(EnumChatFormatting.GOLD);
        entityPlayer.addChatMessage(chat);
    }
    
}
