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
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import alma.obops.aqua.qa0.configuration.UrlConfiguration;
import alma.obops.boot.security.User;
import alma.obops.boot.security.UserDao;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class AquaQA0RestController {

	private static final String ENCODING = "UTF-8";
	private static final String RETURN_URL_COOKIE = "return-url";

	@Autowired
	Environment env;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UrlConfiguration obopsConfig;

	protected Logger logger = Logger.getLogger( this.getClass().getCanonicalName() );

	public AquaQA0RestController() {
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

}
