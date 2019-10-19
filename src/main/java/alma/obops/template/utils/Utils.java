/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2018
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

package alma.obops.template.utils;

import alma.obops.template.domain.ObsUnitSetModel;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Utils {

	static final Logger logger = Logger.getLogger( Utils.class.getCanonicalName() );

	public static void checkIfDefined(final String propName, final String url) {
		// Make sure the property is defined
		if (url == null || url.length() == 0) {
			String msg = "ObOps configuration property " + propName +
					" is not defined";
			throw new RuntimeException(msg);
		}
	}

	public static String normalizeUID(String uid) {
		return (uid!=null)?uid.replace("://","___").replace("/","_"):"";
	}

	public static String decodeUID(String uid) {
		return (uid!=null)?uid.replace("___","://").replace("_","/"):"";
	}

	public static String createCSVList(int nMous, List<ObsUnitSetModel> ousList) {
		if ( ousList == null ) { return ""; }
		StringBuilder res = new StringBuilder();
		res.append(ObsUnitSetModel.getCSVStringHeader() + "\n");
		for (ObsUnitSetModel ous : ousList.subList(0,Math.min(nMous,ousList.size())) ) {
			res.append(ous.toCSVString() + "\n");
		}
		return res.toString();
	}

//	public static <T extends Object> T readValue(Map<String, Object> params, String paramName, Class<T> type, T defaultValue) {
//		if ( params != null && params.containsKey(paramName)) {
//			try {
//				if ( type == Integer.class ) {
//					return Integer.getInteger((String)params.get(paramName));
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				logger.warning("problem reading from Map of " + paramName+" "+e.getMessage());
//			}
//		}
//		return defaultValue;
//
//	}

}
