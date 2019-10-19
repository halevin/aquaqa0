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

import alma.obops.boot.filters.RateLimitingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import alma.obops.boot.filters.RequestLoggingFilter;

@Configuration
@PropertySource("file:${ACSDATA}/config/obopsConfig.properties")
public class FilterConfiguration {

	@Autowired
	Environment env;

	protected Logger logger = LogManager.getLogger( FilterConfiguration.class );

	/**
	 * @return A request logging filter
	 */
	@Bean
	public RequestLoggingFilter requestLoggingFilter() {
		return new RequestLoggingFilter();	
	}

	@Bean
	public RateLimitingFilter makeRateLimitingFilter() { return new RateLimitingFilter("obops.template.ratelimit"); }

}
