
********************************************************
*  			   HOW TO LOAD CUSTOM DESIGNS              *
********************************************************

The mod allows a maximum of 255 flower pot designs to be
loaded into game (design_1.png to design_255.png).

Designs are loaded at runtime as long as they follow the
following criteria:

	1. Design filenames must start with "design_"
	2. Design filenames must end with ".png"
	3. Design filenames must have a number in the middle
	
		For example:
	
		design_15.png		--> SUPPORTED
		design_fancy.png 	--> UNSUPPORTED
		
Designs do not have to be numbered together, but
their number value will determine their selection order.