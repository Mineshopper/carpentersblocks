package com.carpentersblocks.util.protection;

public interface IProtected {

    /**
     * Returns owner of object.
     */
    public String getOwner();

    /**
     * Sets owner of object.
     */
    public void setOwner(String name);

}
