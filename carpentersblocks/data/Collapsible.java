package carpentersblocks.data;

import carpentersblocks.tileentity.TECarpentersBlock;
import carpentersblocks.util.BlockProperties;

public class Collapsible
{

	/**
	 * 16-bit data components:
	 *
	 *	[0000]	[0000]	[0000]	[0000]
	 *  XZNN	XZNP	XZPN	XZPP
	 */

	public final static int QUAD_XZNN = 0;
	public final static int QUAD_XZNP = 1;
	public final static int QUAD_XZPN = 2;
	public final static int QUAD_XZPP = 3;
			
	/**
	 * Returns corner number.
	 */
	public final static int getQuad(double hitX, double hitZ)
	{
		int xOffset = (int) Math.round(hitX);
		int zOffset = (int) Math.round(hitZ);
		
		if (xOffset == 0) {
			if (zOffset == 0) {
				return QUAD_XZNN;
			} else {
				return QUAD_XZNP;
			}
		} else {
			if (zOffset == 0) {
				return QUAD_XZPN;
			} else {
				return QUAD_XZPP;
			}
		}
	}

	/**
	 * Sets height of corner as value from 1 to 16.
	 * Will correct out-of-range values automatically, and won't cause block update if height doesn't change.
	 */
	public final static void setQuadHeight(TECarpentersBlock TE, int corner, int height)
	{
		int data = BlockProperties.getData(TE);
		
		/* Correct out-of-range values. */
		if (height < 1) {
			height = 16;
		}
		
		--height;
		
		switch (corner) {
		case QUAD_XZNN:
			data &= 0xfff;
			data |= (15 - height) << 12;
			break;
		case QUAD_XZNP:
			data &= 0xf0ff;
			data |= (15 - height) << 8;
			break;
		case QUAD_XZPN:
			data &= 0xff0f;
			data |= (15 - height) << 4;
			break;
		case QUAD_XZPP:
			data &= 0xfff0;
			data |= (15 - height);
			break;
		}

		if (BlockProperties.getData(TE) != data) {
			BlockProperties.setData(TE, data);
		}
	}

	/**
	 * Returns height of corner as value from 1 to 16.
	 */
	public final static int getQuadHeight(final TECarpentersBlock TE, int corner)
	{
		int data = BlockProperties.getData(TE);
		
		switch (corner) {
		case QUAD_XZNN:
			data &= 0xf000;
			return 16 - (data >> 12);
		case QUAD_XZNP:
			data &= 0x0f00;
			return 16 - (data >> 8);
		case QUAD_XZPN:
			data &= 0x00f0;
			return 16 - (data >> 4);
		case QUAD_XZPP:
			return 16 - (data &= 0xf);
		default:
			return 16;
		}
	}

}
