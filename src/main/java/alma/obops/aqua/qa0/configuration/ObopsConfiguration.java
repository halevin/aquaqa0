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
package alma.obops.aqua.qa0.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import alma.obops.aqua.qa0.utils.ToolUtils;

/**
 * Implements the {@link UrlConfiguration} interface by exposing the properties
 * defined in <em>$ACSDATA/config/obopsConfig.properties</em>
 * 
 * @author amchavan, 22-Aug-2014
 */

@Component
@PropertySource("file:${ACSDATA}/config/obopsConfig.properties")
public class ObopsConfiguration implements UrlConfiguration {

	@Autowired
	private Environment env;
	
	final static int DEFAULT_MAX_THREADS = 10;
	
//	private final static Logger logger = Logger.getLogger(ObopsConfiguration.class.getName());
	

	@Override
	public String getCasUrl() {
		final String propName = "cas.url";
		final String url = env.getProperty( propName );

		ToolUtils.checkIfDefined( propName, url );

		return url.trim();
	}

	@Override
	public String getDRAToolRestServerUrl() {
		
		final String propName = "obops.aqua.qa0.rest.server.url";
		final String url = env.getProperty( propName );

		ToolUtils.checkIfDefined( propName, url );
		
		return url.trim();
	}
}

