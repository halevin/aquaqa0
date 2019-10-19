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

package alma.obops.template;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alma.obops.template.configuration.ObopsConfiguration;
import alma.obops.template.domain.ObsUnitSetModel;
import alma.obops.template.service.templateService;
import alma.obops.template.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import alma.obops.boot.security.User;
import alma.obops.boot.security.UserDao;
import org.springframework.web.bind.annotation.RestController;

@Api(value="Assign MOUS API", description="Operations assigning MemberObsUnitSets to specific ARC")
@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class ToolRestController {

	private static final String ENCODING = "UTF-8";
	private static final String RETURN_URL_COOKIE = "return-url";

	@Autowired
	Environment env;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ObopsConfiguration obopsConfig;

	protected Logger logger = LogManager.getLogger( this.getClass().getCanonicalName() );

	public ToolRestController() {
	}

	@PostConstruct
	private void init() {
		// no-op
	}

	@ApiOperation(value = "Assign specific MOUS", response = ResponseEntity.class)
	@RequestMapping(value = "/assign/{ous_id}", method = RequestMethod.POST)
	public ResponseEntity assign(
			@ApiParam(value = "Normalized MOUS Status ID in the form uid___XXXX_XXXXX_XXXX .", required = true)
				@PathVariable("ous_id") String mousStatusId)
			throws IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated()) {
			logger.warn(" no authority ");
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}

		if ( mousStatusId == null ) {
			return new ResponseEntity("Empty URL parameter", HttpStatus.EXPECTATION_FAILED);
		}

		String decodedMousStatusId = Utils.decodeUID(mousStatusId);
		logger.warn("mousStatusID "+decodedMousStatusId);
		try {
			String response = templateService.processAssign(decodedMousStatusId, true);
            return new ResponseEntity(response, HttpStatus.OK);
		} catch (Exception ex) {
			logger.warn("returning response "+ex.getMessage());
            return new ResponseEntity(ex.getMessage(), HttpStatus.OK);
		}
	}

	@ApiOperation(value = "Returns MOUSs with manual recipes which can be processed in the current ARC", response = ResponseEntity.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK", response = ResponseEntity.class, responseContainer = "ResponseEntity"),
			@ApiResponse(code = 403, message = "OK", response = ResponseEntity.class, responseContainer = "ResponseEntity")
	})
	@RequestMapping(value = "/manual/{n_mous}", method = RequestMethod.GET)
	public ResponseEntity manual(
			@ApiParam(value = "Number of Manual MOUS that you want to see.", required = true)
				@PathVariable("n_mous") Integer numberOfMOUS,
			@ApiParam(value = "Checks the DATA_AT_<ARC> Flag. If set to False can be dismissed.")
				@RequestParam(value = "checkData", required = false, defaultValue = "true") boolean checkData,
			@ApiParam(hidden = true)
				@RequestParam(value = "jsonOutput", required = false, defaultValue = "false") boolean jsonOutput)
			throws IOException {
		Authentication authentication = SecurityContext/Holder.getContext().getAuthentication();
		if (! authentication.isAuthenticated()) {
			logger.warn(" no authority ");
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		logger.warn("numberOfMOUS "+numberOfMOUS);
        List<ObsUnitSetModel> ousList = templateService.listMOUSs(checkData, true);
		if ( jsonOutput ) {
            return new ResponseEntity(ousList.subList(0,Math.min(ousList.size(), numberOfMOUS)), HttpStatus.OK);
        } else {
            return new ResponseEntity(Utils.createCSVList(numberOfMOUS,ousList), HttpStatus.OK);
        }
	}

	@ApiOperation(value = "Assign MOUSs with pipeline recipes to current ARC ", response = ResponseEntity.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "mode", value = "If 'run' sends the command to the PBS(TORQUE if available), if 'try' only prints the list of MOUS ", required = true, dataType = "string", paramType = "path"),
			@ApiImplicitParam(name = "pipe", value = "Number of Pipeline MOUS to be assigned", required = false, dataType = "int", paramType = "query", defaultValue = "5"),
			@ApiImplicitParam(name = "checkData", value = "Checks the DATA_AT_<ARC> Flag. If set to False can be dismissed", required = false, dataType = "boolean", paramType = "query", defaultValue = "true")
	})
	@RequestMapping(value = "/pipeline", method = RequestMethod.POST,
					consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE} )
	public ResponseEntity pipelineAssign(
			@ApiParam(hidden = true)
					@RequestParam Map<String, Object> params)
			throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated()) {
			logger.warn(" no authority ");
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		boolean run = false;
		int numberOfMOUS = 5;
		boolean checkData = true;
		List<String> ousIds = null;

		logger.warn(" >>>>>>>>>>>>>>> params " + params.toString());
		if ( params != null ) {
			try {
				run = params.containsKey("mode") ? "run".equals(params.get("mode")) : true;
				numberOfMOUS = params.containsKey("pipe") ? Integer.parseInt((String) params.get("pipe")) : 5;
				checkData = params.containsKey("checkData") ? Boolean.parseBoolean((String) params.get("checkData")) : true;
				String ousIdsStr = (params.get("ousids") != null) ? (String) params.get("ousids") : null;
				if (ousIdsStr != null) {
					ObjectMapper mapper = new ObjectMapper();
					ousIds = mapper.readValue(ousIdsStr, List.class);
					;
				}
			} catch (Exception e) {
				logger.warn(" Error parsing " + params);
			}
		}
		try {
			return new ResponseEntity(templateService.processMOUSsInPipeline(ousIds, numberOfMOUS, checkData, run), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/pipelinelist/{n_mous}", method = RequestMethod.GET)
	public ResponseEntity pipeline(@PathVariable("n_mous") Integer numberOfMOUS,
								   @RequestParam(value = "checkData", required = false, defaultValue = "true") boolean checkData)
			throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated()) {
			logger.warn(" no authority ");
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		List<ObsUnitSetModel> ousList = templateService.listMOUSs(checkData, false);
		if ( ousList == null || ousList.size() == 0) {
			return new ResponseEntity("No MOUS to Process.", HttpStatus.OK);
		}
		return new ResponseEntity(ousList.subList(0,Math.min(numberOfMOUS,ousList.size())), HttpStatus.OK);
	}




	/**
	 * If we find a return URL cookie we redirect there, otherwise we return a
	 * generic string
	 */
	@RequestMapping("/login-check")
	public Object login(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(RETURN_URL_COOKIE)) {
					String url = URLDecoder.decode(cookie.getValue(), ENCODING);
					response.sendRedirect(url);
					return null;
				}
			}
		}

		// Something went wrong, we shouldn't be here in principle
		String msg = "Found " +
				(cookies == null ? "no" : cookies.length) +
				" cookies; no cookies named " + RETURN_URL_COOKIE;
		logger.warn( msg );
		return "Nothing here";
	}

	@RequestMapping("/settings")
	public Map<String,String> settings(HttpServletResponse response)
			throws IOException {

		String propHelpURL = "obops.template.help.url";
		final String helpURL = env.getProperty( propHelpURL );
		if ( helpURL == null ) throw new IOException("obops.template.help.url property is not defined");

		String propEmail = "obops.template.email";
		final String email = env.getProperty( propEmail );
		if ( email == null ) throw new IOException("obops.template.email property is not defined");

		final String currentARC = obopsConfig.getCurrentARC();


		Map<String,String> settings = new HashMap<>();
		settings.put("helpURL", helpURL);
		settings.put("email", email);
		settings.put("arc", currentARC);

		return settings;
	}

	/**
	 * Performs a local logout, then redirects to the CAS logout page
	 * @throws IOException
	 */
	@RequestMapping( value="/do-logout", method = RequestMethod.GET )
	public void logout( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		// This implementation taken from http://websystique.com/spring-security/spring-security-4-logout-example/
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    response.sendRedirect( obopsConfig.getCasUrl() + "/logout" );
	}

	/**
	 * @return An Account instance describing the currently logged on user, if
	 *         any; <code>null</code> otherwise.<br>
	 *         Example: <code>.../account</code> returns
	 *         <code>{"accountId":"lricci","firstname":"Luca","lastname":"Ricci",...}</code>
	 * @throws IOException
	 */
	@CrossOrigin
	@RequestMapping(value = "/account", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Object getAccount( HttpServletRequest request ) throws IOException {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			// Should never happen
			String msg = "Internal error: no user info in the request";
			throw new RuntimeException( msg );
		}

		String accountID = p.getName();
		User t = userDao.findById( accountID ).orElse(null);
		if (t == null) {
			// Should never happen
			String msg = "Internal error: no account found for account ID:" + accountID;
			throw new RuntimeException( msg );
		}

		return t;
	}

	@ApiOperation(value = "Shows a list of tool's configuration values", response = ResponseEntity.class)
	@CrossOrigin
	@GetMapping("/configuration")
	@ResponseBody
	public String getConfiguration( HttpServletRequest request ) throws IOException {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			// Should never happen
			String msg = "Internal error: no user info in the request";
			throw new RuntimeException( msg );
		}

		return obopsConfig.getConfiguration();
	}

}
