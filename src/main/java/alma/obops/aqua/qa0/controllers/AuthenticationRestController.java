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

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alma.obops.boot.filters.RequestLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthenticationRestController {

	private static final String ENCODING = "UTF-8";
	private static final String RETURN_URL_COOKIE = "return-url";

	@Autowired
	private UserDao userDao;

	@Autowired
	private UrlConfiguration obopsConfig;

	protected Logger logger = LogManager.getLogger( RequestLoggingFilter.class );

	public AuthenticationRestController() {
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
		logger.warn( msg );
		return "Nothing here";
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
