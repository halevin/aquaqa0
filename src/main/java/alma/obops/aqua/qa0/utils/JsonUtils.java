/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2014
 * (in the framework of the ALMA collaboration).
 * All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *******************************************************************************/

package alma.obops.aqua.qa0.utils;

public class JsonUtils {

	/**
	 * See 7.3 "Line Terminators" of the ECMAScript Language Specification at
	 * http://www.ecma-international.org/publications/files/ECMA-ST/Ecma-262.pdf
	 */
	public static final String[] ECMASCRIPT_LINE_TERMINATORS = {
			"\u2028", 		// Line separator, equivalent to an HTML <br>
			"\u2029",		// Paragraph separator, equivalent to an HTML <p>
			"\n",			// (char) 0x0a, Line Feed
			"\r",			// (char) 0x0d, Carriage Return
	};
	
	// Javascript-friendly encoding of the ECMAScript line terminators
	public static final String[] ENCODED_ECMASCRIPT_LINE_TERMINATORS = {
		"\\u2028", 		// Line separator, equivalent to an HTML <br>
		"\\u2029",		// Paragraph separator, equivalent to an HTML <p>
		"\\n",			// Line Feed
		"\\r" 			// Carriage Return
	};
	
	
	/**
	 * Encode all ECMAScript line terminators in the input string, replacing them with an
	 * equivalent ASCII string.
	 */
	public static String encodeLineTerminators( String in ) {
		String out = in;
		for (int i = 0; i < ECMASCRIPT_LINE_TERMINATORS.length; i++) {
			final String lt = ECMASCRIPT_LINE_TERMINATORS[i];
			final String encodedLt = ENCODED_ECMASCRIPT_LINE_TERMINATORS[i];
			out = out.replace( lt, encodedLt );
		}
		return out;
	}
}
