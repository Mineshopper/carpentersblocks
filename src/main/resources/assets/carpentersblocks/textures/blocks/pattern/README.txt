
********************************************************
*  			   HOW TO LOAD CUSTOM PATTERNS             *
********************************************************

The mod allows a maximum of 255 patterns to be loaded
into game (pattern_1.png to pattern_255.png).

Patterns are loaded at runtime as long as they follow the
following criteria:

	1. Pattern filenames must start with "pattern_"
	2. Pattern filenames must end with ".png"
	3. Pattern filenames must have a number in the middle
	
		For example:
	
		pattern_15.png		--> SUPPORTED
		pattern_fancy.png 	--> UNSUPPORTED
		
Patterns do not have to be numbered together, but
their number value will determine their selection order.