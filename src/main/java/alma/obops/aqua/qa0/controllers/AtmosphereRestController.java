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

import alma.asdm.domain.SourceCoverage;
import alma.obops.aqua.qa0.service.ExecBlockHelper;
import alma.obops.aqua.service.asdm.ASDMAtmosphereSummaryContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Set;
import java.util.logging.Logger;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class AtmosphereRestController {

	@Autowired
	Environment env;

	@Autowired
	private ExecBlockHelper execBlockHelper;

	protected Logger logger = Logger.getLogger( this.getClass().getSimpleName() );

	@RequestMapping(value = "/atmosphere/{eb_uid}")
	@ResponseBody
	public ASDMAtmosphereSummaryContainer coverages(HttpServletRequest request,
													@PathVariable("eb_uid") String execBlockUIDNormalized) throws Exception {


		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}


		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");

			logger.info(" ASDMAtmosphereSummaryContainer for EB " + execBlockUID);

			ASDMAtmosphereSummaryContainer atmosphereSummaryContainer = execBlockHelper.getAtmosphereSummaryContainer(execBlockUID);

			return atmosphereSummaryContainer;

		}

		return null;
	}

}
