package carpentersblocks.util.protection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.MinecraftServer;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ProtectedUtil {

    private static Map<String, String> cachedUUID = new HashMap<String, String>();

    /**
     * Reads owner from protected object and converts it to UUID if necessary.
     */
    public static void updateOwnerUUID(IProtected object)
    {
        UUID uuid;
        try {
            uuid = object.getOwner();
        } catch (IllegalArgumentException e) {
            String rawName = (String) object.getOwnerRaw();
            if (cachedUUID.containsKey(rawName)) {
                object.setOwner(UUID.fromString(cachedUUID.get(rawName)));
            } else {
                uuid = getOwnerUUID(rawName);
                if (uuid != null) {
                    object.setOwner(uuid);
                    cachedUUID.put(rawName, uuid.toString());
                }
            }
        }
    }

    /**
     * Converts saved owner names to UUID and returns result.
     * If nothing found (unlikely), returns null.
     */
    private static UUID getOwnerUUID(String name)
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
