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

package alma.obops.aqua.qa0.controllers;

import java.security.Principal;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import alma.obops.aqua.qa0.service.ExecBlockHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import alma.asdm.domain.SourceCoverage;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class CoverageRestController {

	@Autowired
	Environment env;

	@Autowired
	private ExecBlockHelper execBlockHelper;

	protected Logger logger = Logger.getLogger( this.getClass().getSimpleName() );

	@RequestMapping(value = "/coverages/{eb_uid}")
	@ResponseBody
	public Set<SourceCoverage> coverages(HttpServletRequest request,
			@PathVariable("eb_uid") String execBlockUIDNormalized) throws Exception {


		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}


		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");

			logger.info(" Coverage for EB " + execBlockUID);

			Set<SourceCoverage> coverages = execBlockHelper.getCoverages(execBlockUID);

			logger.info(" Found  " + coverages.size()+" coverages");



			return coverages;

		}

		return null;
	}

}
