package carpentersblocks.util.protection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;

public class ProtectedUtil {

    private static Map<String, UUID> cachedUUID = new HashMap<String, UUID>();

    /**
     * Returns whether player is owner of object.
     */
    public static boolean isOwner(IProtected object, EntityPlayer entityPlayer)
    {
        UUID uuid = updateOwnerUUID(object);

        if (uuid != null) {
            return object.getOwner().equals(uuid.toString());
        } else {
            // Attempt to update owner format manually, then resort to fallback method
            updateOwnerUUID(object);
            return object.getOwner().equals(entityPlayer.getDisplayName());
        }
    }

    /**
     * Reads owner from protected object and converts it to UUID if necessary.
     */
    public static UUID updateOwnerUUID(IProtected object)
    {
        String owner = object.getOwner();

        UUID uuid = null;
        try {
            uuid = UUID.fromString(owner);
        } catch (IllegalArgumentException e) {
            if (cachedUUID.containsKey(owner)) {
                object.setOwner(cachedUUID.get(owner));
            } else {
                uuid = getOwnerIdentity(owner);
                if (uuid != null) {
                    object.setOwner(uuid);
                    cachedUUID.put(owner, uuid);
                }
            }
        }

        return uuid;
    }

    /**
     * Converts saved owner names to UUID and returns result.
     */
    private static UUID getOwnerIdentity(String name)
    {
        final GameProfile[] gameProfileResult = new GameProfile[1];
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
            @Override
            public void onProfileLookupSucceeded(GameProfile gameProfile) {
                gameProfileResult[0] = gameProfile;
            }
            @Override
            public void onProfileLookupFailed(GameProfile gameProfile, Exception exception) {
                gameProfileResult[0] = null;
            }
        };
        MinecraftServer.getServer().func_152359_aw().findProfilesByNames(new String[] { name }, Agent.MINECRAFT, profilelookupcallback);
        return gameProfileResult[0] != null ? gameProfileResult[0].getId() : null;
    }

}
