package com.carpentersblocks.nbt;

import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DesignHandler.DesignType;

public class IDesignable extends CbTileEntity {

    public boolean hasDesign(EnumAttributeLocation location) {
    	return this.getAttributeHelper().hasAttribute(location, EnumAttributeType.DESIGN_BASIC);
    }

    public String getDesign(EnumAttributeLocation location) {
        return this.getAttributeHelper().getAttribute(location, EnumAttributeType.DESIGN_BASIC).getModel().toString();
    }

    private boolean setDesign(EnumAttributeLocation location, String newDesign) {
    	if (hasDesign(location)) {
    		String existingDesign = this.getAttributeHelper().getAttribute(location, EnumAttributeType.DESIGN_BASIC).toString();
    		if (!existingDesign.equals(newDesign)) {
	    		this.addAttribute(location, EnumAttributeType.DESIGN_BASIC, newDesign);
	    		this.update(true);
	            return true;
    		}
        }
        return false;
    }

    public boolean removeDesign(EnumAttributeLocation location) {
        return this.setDesign(location, "");
    }

    public boolean setNextDesign(EnumAttributeLocation location) {
    	String nextDesign = DesignHandler.getNext(DesignType.FLOWERPOT, getDesign(location));
    	return this.setDesign(location, nextDesign);
    }

    public boolean setPrevDesign(EnumAttributeLocation location) {
    	String prevDesign = DesignHandler.getPrev(DesignType.FLOWERPOT, getDesign(location));
    	return this.setDesign(location, prevDesign);
    }
	
}