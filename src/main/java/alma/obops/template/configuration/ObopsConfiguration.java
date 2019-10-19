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
package alma.obops.template.configuration;

import alma.obops.template.Constants;

import java.util.List;
import java.util.Map;

/**
 * Provides the URLs we need to interface with the rest of the system
 * @author amchavan
 */
public interface ObopsConfiguration {

	/** @return The URL of the CAS server securing this application */
    String getCasUrl();

	/**
	 * @return The template Tool rest server url
	 */
    String gettemplateRestServerUrl();

	/**
	 * @return List of PI preferred ARCs acceptable by tool
	 */
	List<String> getPIPreferredArcs();

	/**
	 * @return List of array types acceptable by tool
	 */
	List<String> getArrayTypes();

	/**
	 * @return Current tool ARC
	 */
	String getCurrentARC();

	/**
	 * @return Maximal size of the data package
	 */
	int getMaxSize();

	/**
	 * @return List of non-standard acceptable by tool
	 */
	List<String> getNonStandardModes();


	/**
	 * returns if Torque available
	 *
	 * @return
	 */
	boolean isTorqueAvaiable();

	/**
	 * returns run command for DARED TODO
	 *
	 * @return
	 */
	String getRunCommand();

	/**
	 * returns run command for Torque TODO
	 *
	 * @return
	 */
	String getSourceCommand();

	/**
	 * Returns memory usage options
	 *
	 * @return
	 */
	Map<Constants.MemoryTypes, String> getMemoryOptions();

	/**
	 * returns qsub otions to build torque command
	 *
	 * @return
	 */
	String getQsubOptions();


	/**
	 * Returns list of allowed cycles
	 *
	 * @return
	 */
	List<String> getAllowedCycles();


	/**
	 * Returns OBOPS template configuration values
	 *
	 * @return
	 */
	public String getConfiguration();

}
