package carpentersblocks.util.protection;

import java.util.UUID;

public interface IProtected {

    /**
     * Returns owner of object.
     */
    public UUID getOwner();

    /**
     * Sets owner of object.
     */
    public void setOwner(UUID Id);

    /**
     * Returns raw owner object.
     * TODO: Remove when player name-changing system is switched on.
     */
    public Object getOwnerRaw();

}
