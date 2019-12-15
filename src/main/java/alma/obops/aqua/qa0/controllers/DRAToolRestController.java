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
public class DRAToolRestController {

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

	public DRAToolRestController() {
	}

	@PostConstruct
	private void init() {
		// no-op
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
		logger.warning( msg );
		return "Nothing here";
	}

	@RequestMapping("/settings")
	public Map<String,Object> settings(HttpServletResponse response)
			throws IOException {

		String propHelpURL = "obops.aqua.qa0.help.url";
		final String helpURL = env.getProperty( propHelpURL );
		if ( helpURL == null ) throw new IOException(propHelpURL + " property is not defined");

		String propEmail = "obops.aqua.qa0.email";
		final String email = env.getProperty( propEmail );
		if ( email == null ) throw new IOException(propEmail + " property is not defined");

		String propAquaURL = "obops.webaqua.qa2.url";
		final String aquaURL = env.getProperty( propAquaURL );
		if ( aquaURL == null ) throw new IOException(propAquaURL + " property is not defined");

		String propProtrackURL = "obops.protrack.url";
		final String protrackURL = env.getProperty( propProtrackURL );
		if ( protrackURL == null ) throw new IOException(propProtrackURL + " property is not defined");

		String propJiraURL = "obops.webaqua.jira.url";
		final String jiraURL = env.getProperty( propJiraURL );
		if ( jiraURL == null ) throw new IOException(propJiraURL + " property is not defined");


		Map<String,Object> settings = new HashMap<>();
		settings.put("helpURL", helpURL);
		settings.put("email", email);
		settings.put("aquaURL", aquaURL + (aquaURL.endsWith("/")?"":"/"));
		settings.put("protrackURL", protrackURL + (protrackURL.endsWith("/")?"":"/"));
		settings.put("jiraURL", jiraURL + (jiraURL.endsWith("/")?"":"/"));
		settings.put("states", DRAToolConstants.qa2DashboardStates);
		settings.put("substates", DRAToolConstants.qa2DashboardSubstates);
		settings.put("cycles", cycleDao.findAllCodes());

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

	@RequestMapping(value = "/account/{name}")
	@ResponseBody
	public Object getUsers( HttpServletRequest request, @PathVariable("name") String name) throws Exception {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		List<User> accounts = userDao.findByString(name);
		logger.warning(" Returning accounts " + accounts.size());
		List<DTDataReducer> drList = dataReducerRepository.findDataReducers(null, null, null, null, false);
		logger.warning(" Returning data reducers " + drList.size());
		for ( User user : accounts ) {
			user.setIsDataReducer(DRAToolUtils.isDataReducer(drList, user.getUsername()));
		}

		return accounts;
	}

	@CrossOrigin
	@RequestMapping(value = "/dr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity setDataReducer(HttpServletRequest request, @RequestBody DataReducerJsonModel drModel) throws Exception {
		logger.warning(" setDataReducer " + drModel);


		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated() || ! authentication.getAuthorities().contains(new SimpleGrantedAuthority("OBOPS/DRM"))) {
			logger.warning(" no authority ");
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}


		if ( drModel != null && drModel.getUserId() != null ) {
			String userId = drModel.getUserId();

			User user = userDao.findById(userId).orElse(null);
			logger.warning(" user " + user);
			if ( user != null ) {
				DTDataReducer dr = dataReducerDao.findByUserId(userId);
				if ( dr == null ) { dr = new DTDataReducer();}
				dr.setUserId(userId);
				DTArcNode arcNode = arcNodeDao.findByArcAndNode(drModel.getDrArc(), drModel.getDrNode());
				if (arcNode != null && arcNode.getNode() == null ) {
					arcNode.setNode("");
				}
				dr.setArcNode(arcNode);
				dr.setQualifications(drModel.getQualifications());
				dataReducerDao.save(dr);
			} else {
				return new ResponseEntity(HttpStatus.FORBIDDEN);
			}
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/dr/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity deleteDataReducer(HttpServletRequest request, @PathVariable("userId") String userId) throws Exception {
		logger.warning(" deleteDataReducer " + userId);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated() || ! authentication.getAuthorities().contains(new SimpleGrantedAuthority("OBOPS/DRM"))) {
			logger.warning(" no authority ");
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}

		DTDataReducer dr = dataReducerDao.findById(userId).orElse(null);

		logger.warning(" DTDataReducer exists " + dr);

		if ( userId != null ) {
			List<Availability> availabilities = availabilityDao.findByUserIdOrderByDateStartDesc(dr.getUserId());
			for (Availability av : availabilities ) {
				availabilityDao.delete(av);
			}
			dataReducerDao.deleteById(userId);
		} else {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/dr")
	@ResponseBody
	public List<DTDataReducer> findDataReducers(HttpServletRequest request,
												@RequestParam(value = "userId", required = false) String userId,
												@RequestParam(value = "drArc", required = false) String arc,
												@RequestParam(value = "drNode", required = false) String node,
												@RequestParam(value = "qualifications", required = false) Set<String> qualifications) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated()) {
			return null;
		}

		List<DTDataReducer> drList = dataReducerRepository.findDataReducers(userId, arc, node, qualifications, false);

		logger.info(" Found "+ drList.size() +" data reducers ");

		return drList;
	}

	@RequestMapping(value = "/drinfo")
	@ResponseBody
	public DTDataReducer dataReducerInfo(HttpServletRequest request) throws Exception {


		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		String drID = p.getName();

		DTDataReducer dr = dataReducerDao.findById(drID).orElse(null);

		logger.info(" Found data reducer " + dr);

		return dr;
	}

	@RequestMapping(value = "/avail")
	@ResponseBody
	public List<DTDataReducer> getAvailability(HttpServletRequest request,
											   @RequestParam(value = "drArc", required = false) String arc,
											   @RequestParam(value = "drNode", required = false) String node,
											   @RequestParam(value = "qualifications", required = false) Set<String> qualifications) throws Exception {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		List<DTDataReducer> drList = dataReducerRepository.findDataReducers(null, arc, node, qualifications, true);
		logger.info(" Found "+ drList.size() +" availability records ");
		return drList;
	}

	@CrossOrigin
	@RequestMapping(value = "/avail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity setAvailability(HttpServletRequest request, @RequestBody Availability availability) throws Exception {
		logger.warning(" setAvailability " + availability);


		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated() || ! authentication.getAuthorities().contains(new SimpleGrantedAuthority("OBOPS/QAA"))) {
			logger.warning(" no authority ");
			return null;
		}


		if ( availability != null && availability.getUserId() != null ) {
			availabilityDao.save(availability);
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/avail/{id}", method = RequestMethod.DELETE)
	public ResponseEntity deleteAvailability(HttpServletRequest request, @PathVariable("id") Long id) throws Exception {
		logger.warning(" availability ID " + id);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (! authentication.isAuthenticated() || ! authentication.getAuthorities().contains(new SimpleGrantedAuthority("OBOPS/QAA"))) {
			logger.warning(" no authority ");
			return null;
		}

		availabilityDao.deleteById(id);

		return new ResponseEntity(HttpStatus.OK);
	}

}
