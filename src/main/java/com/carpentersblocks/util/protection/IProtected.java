package com.carpentersblocks.util.protection;

import java.util.UUID;

public interface IProtected {

    /**
     * Returns object owner as UUID string.
     */
    public String getOwner();

    /**
     * Sets owner of object.
     */
    public void setOwner(UUID uuid);

}
