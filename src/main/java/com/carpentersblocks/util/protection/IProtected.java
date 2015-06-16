package com.carpentersblocks.util.protection;


public interface IProtected {

    /**
     * Returns object owner as string (UUID for online-mode, player name for offline-mode).
     */
    public String getOwner();


    /**
     * Sets owner of object.
     */
    public void setOwner(ProtectedObject obj);

}
