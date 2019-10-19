/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2019
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
package alma.obops.template.security.httpbasic;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * From http://ryanjbaxter.com/2015/01/06/securing-rest-apis-with-spring-boot
 * 
 */
@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * Java clients for HTTP Basic security should be coded as follows:
	 * 
	 * <pre>
	 * URL url = new URL( ... );
	 * URLConnection uc = url.openConnection();
	 *
	 * String userpass = username + ":" + password;
	 * String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
	 * 
	 * uc.setRequestProperty( "Authorization", basicAuth );
	 * InputStream in = uc.getInputStream();
	 * </pre>
	 * 
	 * From:
	 * http://stackoverflow.com/questions/496651/connecting-to-remote-url-which-requires-authentication-using-java
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/restapi/**").hasAnyAuthority( "OBOPS/QAA" )
																// allowed roles here
				.antMatchers("/admin/**").authenticated() // .hasAnyAuthority( "OBOPS/QAA" )
															// roles here

				// Uncomment the next two lines only if you want to *disable*
				// CSRF protection (which you may want to do if you are serving
				// non-browser clients). See also:
				// https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html
				// ----------------------------------------------------------------
				.and().csrf().disable();
	}
}
