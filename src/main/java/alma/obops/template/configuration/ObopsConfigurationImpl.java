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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import alma.obops.template.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements the {@link ObopsConfiguration} interface by exposing the properties
 * defined in <em>$ACSDATA/config/obopsConfig.properties</em>
 * 
 * @author amchavan, 22-Aug-2014
 */

@Component
@PropertySource("file:${ACSDATA}/config/obopsConfig.properties")
public class ObopsConfigurationImpl implements ObopsConfiguration {

	@Autowired
	private Environment env;

	private final static String CAS_SERVER_URL = "cas.url";
	private final static String REST_SERVER_URL = "obops.template.rest.server.url";
	private final static String PI_PREFERED_ARC = "obops.template.pi.preferred.arc";
	private final static String ARRAY_TYPES = "obops.template.array.types";
	private final static String CURRENT_ARC = "obops.template.current.arc";
	private final static String MAX_SIZE = "obops.template.max.size";
	private final static String IS_TORQUE_AVAILABLE = "obops.template.torque";
	private final static String TORQUE_NODES = "obops.template.torque.nodes";
	private final static String TORQUE_WALLTIME = "obops.template.torque.walltime";
	private final static String RUN_COMMAND = "obops.template.run.cmd";
	private final static String SOURCE_COMMAND = "obops.template.source.cmd";
	private final static String MEMORY_12M_CAL = "obops.template.memory.12mcal";
	private final static String MEMORY_7M = "obops.template.memory.7m";
	private final static String MEMORY_SD = "obops.template.memory.sd";
	private final static String MEMORY_12M = "obops.template.memory.12m";
	private final static String ALLOWED_CYCLES = "obops.template.allowed.cycles";
//	private final static Logger logger = Logger.getLogger(ObopsConfiguration.class.getName());


	@Override
	public String getCasUrl() {
		return getPropertyValue(CAS_SERVER_URL);
	}

	@Override
	public String gettemplateRestServerUrl() {
		return getPropertyValue(REST_SERVER_URL);
	}

	@Override
	public List<String> getPIPreferredArcs() {
		String properyValue = getPropertyValue(PI_PREFERED_ARC);
		return getValuesFromConfig(properyValue);
	}

	@Override
	public List<String> getArrayTypes() {
		String properyValue = getPropertyValue(ARRAY_TYPES);
		return getValuesFromConfig(properyValue);
	}

	@Override
	public String getCurrentARC() {
		return getPropertyValue(CURRENT_ARC);
	}

	@Override
	public int getMaxSize() {
		return Integer.getInteger(env.getProperty(MAX_SIZE));
	}

	@Override
	public List<String> getNonStandardModes() {
		String properyValue = getPropertyValue(PI_PREFERED_ARC);
		return getValuesFromConfig(properyValue);
	}

	@Override
	public boolean isTorqueAvaiable() {
		String properyValue = getPropertyValue(IS_TORQUE_AVAILABLE);
		Utils.checkIfDefined(IS_TORQUE_AVAILABLE, properyValue);
		return properyValue.contains("true");
	}

	@Override
	public String getRunCommand() {
		return getPropertyValue(RUN_COMMAND);
	}

	@Override
	public String getSourceCommand() {
		return getPropertyValue(SOURCE_COMMAND);
	}

	@Override
	public Map<Constants.MemoryTypes, String> getMemoryOptions() {
		Map<Constants.MemoryTypes,String> memoryOptions = new HashMap<>();

		String propery12MCal = getPropertyValue(MEMORY_12M_CAL);
		String propery7M = getPropertyValue(MEMORY_7M);
		String properySD = getPropertyValue(MEMORY_SD);
		String propery12M = getPropertyValue(MEMORY_12M);
		memoryOptions.put(Constants.MemoryTypes._12M_cal, propery12MCal);
		memoryOptions.put(Constants.MemoryTypes._7M, propery7M);
		memoryOptions.put(Constants.MemoryTypes._SD, properySD);
		memoryOptions.put(Constants.MemoryTypes._12M, propery12M);
		return memoryOptions;
	}

	@Override
	public String getQsubOptions() {
		String properyNodes = getPropertyValue(TORQUE_NODES);
		String properyWalltime = getPropertyValue(TORQUE_WALLTIME);
		return " -m abe -M `whoami` -l nodes="+properyNodes+" -l walltime=" + properyWalltime;
	}

	@Override
	public List<String> getAllowedCycles() {
		String properyValue = getPropertyValue(ALLOWED_CYCLES);
		return getValuesFromConfig(properyValue);
	}


	private List<String> getValuesFromConfig(String property) {
		List<String> values = new ArrayList<>();
		if(property !=null)
		{
			for (String value : (property).split(",")) {
				values.add(value.trim());
			}
		}
		return values;
	}

	private String getPropertyValue(String properyName){
		final String value = env.getProperty(properyName);
		Utils.checkIfDefined(properyName, value);
		return value.trim();
	}

	@Override
	public String getConfiguration() {
		return
				"<pre> \n" +
				"template obopsConfig.properties values \n \n" +
				PI_PREFERED_ARC + " : " + getPropertyValue(PI_PREFERED_ARC) + " \n" +
				ARRAY_TYPES + " : " + getPropertyValue(ARRAY_TYPES) + " \n" +
				CURRENT_ARC + " : " + getCurrentARC() + " \n" +
				TORQUE_NODES + " : " + getPropertyValue(TORQUE_NODES) + " \n" +
				TORQUE_WALLTIME + " : " + getPropertyValue(TORQUE_WALLTIME) + " \n" +
				RUN_COMMAND + " : " + getRunCommand() + " \n" +
				SOURCE_COMMAND + " : " + getSourceCommand() + " \n" +
				ALLOWED_CYCLES + " : " + getPropertyValue(ALLOWED_CYCLES) + " \n" +
				"</pre>";
	}

}

