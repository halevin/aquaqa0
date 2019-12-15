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

import java.io.IOException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import alma.asdm.domain.SourceCoverage;
import alma.obops.aqua.qa0.DRAToolConstants;
import alma.obops.aqua.qa0.configuration.UrlConfiguration;
import alma.obops.aqua.qa0.domain.Availability;
import alma.obops.aqua.qa0.domain.DTArcNode;
import alma.obops.aqua.qa0.domain.DTDataReducer;
import alma.obops.aqua.qa0.domain.DataReducerJsonModel;
import alma.obops.aqua.qa0.persistence.ArcNodeDao;
import alma.obops.aqua.qa0.persistence.AvailabilityDao;
import alma.obops.aqua.qa0.persistence.CycleDao;
import alma.obops.aqua.qa0.persistence.DataReducerDao;
import alma.obops.aqua.qa0.persistence.DataReducerRepository;
import alma.obops.aqua.qa0.utils.DRAToolUtils;
import alma.obops.boot.security.User;
import alma.obops.boot.security.UserDao;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class CoverageRestController {

	private static final String ENCODING = "UTF-8";
	private static final String RETURN_URL_COOKIE = "return-url";

	@Autowired
	Environment env;

	@Autowired
	private UserDao userDao;

	@Autowired
	private DataReducerDao dataReducerDao;

	@Autowired
	private DataReducerRepository dataReducerRepository;

	@Autowired
	private UrlConfiguration obopsConfig;

	@Autowired
	private ArcNodeDao arcNodeDao;

	@Autowired
	private CycleDao cycleDao;

	@Autowired
	private AvailabilityDao availabilityDao;

	protected Logger logger = Logger.getLogger( this.getClass().getCanonicalName() );

	public CoverageRestController() {
	}

	@PostConstruct
	private void init() {
		// no-op
	}

	@RequestMapping(value = "/covergares/{eb_uid}")
	@ResponseBody
	public ResponseEntity covergares(HttpServletRequest request,
			@PathVariable("eb_uid") String execBlockUID) throws Exception {


		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		logger.info(" Coverage for EB " + execBlockUID);

		return new ResponseEntity(HttpStatus.OK);
	}

}
