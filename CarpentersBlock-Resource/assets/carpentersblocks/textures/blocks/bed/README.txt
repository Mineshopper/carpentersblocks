
******************************************************************
*                 HOW TO ADD CUSTOM BED DESIGNS                  *
******************************************************************

This mod allows a maximum of 255 bed designs to be loaded into the
game (/design_1/.. to /design_255/..).

Designs are loaded at runtime as long as they adhere to the
following criteria:

	1. The folder name must start with "design_"
	2. The folder name must end with a number

		For example:
	
		/design_15/..		--> SUPPORTED
		/design_custom/.. 	--> UNSUPPORTED
		
Designs do not have to be numbered together, but their number
value will determine their selection order in-game.

Designs can include a blanket, pillow or both.  Simply include
"blanket.png" and "pillow.png" in the design folder to have it
selectable in-game.