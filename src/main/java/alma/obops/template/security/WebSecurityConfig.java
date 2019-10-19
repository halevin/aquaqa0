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

package alma.obops.template.security;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import alma.obops.boot.security.AlmaUserDetailsService;


/**
 * Spring Security Configuration, adapted from
 * https://objectpartners.com/2014/05/20/configuring-spring-security-cas-providers-with-java-config
 *
 */

// Comment out the following two lines to turn off CAS Authentication
//@Configuration
//@EnableWebSecurity
@PropertySource("file:${ACSDATA}/config/obopsConfig.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${obops.template.rest.server.url}")
	private String draToolURL;

	@Value("${cas.url}")
	private String casURL;

	@Bean
	public ServiceProperties serviceProperties() {
		ServiceProperties serviceProperties = new ServiceProperties();
		serviceProperties.setService(draToolURL + "/j_spring_cas_security_check");
		serviceProperties.setSendRenew(false);
		return serviceProperties;
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
		CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
		casAuthenticationProvider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
		casAuthenticationProvider.setServiceProperties(serviceProperties());
		casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
//		casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
		casAuthenticationProvider.setKey("0xFFF-1");
		return casAuthenticationProvider;
	}

	@Bean
	public AuthenticationUserDetailsService<Authentication> authenticationUserDetailsService() {
		return new AlmaUserDetailsService();
	}

	@Bean
	public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
		return new Cas20ServiceTicketValidator(casURL);
	}

	@Bean
	public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
		CasAuthenticationFilter filter = new CasAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		return filter;
	}

	@Bean
	public SingleSignOutFilter singleLogoutFilter() throws Exception {
//		SingleSignOutFilter filter = new SingleSignOutFilter();
//		filter.setCasServerUrlPrefix(casURL);
		return new SingleSignOutFilter();
	}

	@Bean
	public LogoutFilter requestSingleLogoutFilter() throws Exception {
		SecurityContextLogoutHandler h = new SecurityContextLogoutHandler();
		LogoutFilter filter = new LogoutFilter(casURL + "/logout", h);
		filter.setFilterProcessesUrl("/j_spring_cas_security_logout");
		return filter;
	}

	@Bean
	public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
		CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
		casAuthenticationEntryPoint.setLoginUrl(casURL + "/login");
		casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
		return casAuthenticationEntryPoint;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

//		http.addFilter(casAuthenticationFilter());
		http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());

		http
				.addFilter(casAuthenticationFilter())
				.authorizeRequests()
				// .antMatchers("/restapi/login-check").authenticated()
				// .antMatchers("/restapi/account/**").permitAll()
				// .antMatchers("/restapi/do-logout/**").authenticated()
				// .antMatchers("/restapi/dr/**").hasAnyAuthority( "OBOPS/DRM" )
				// .antMatchers("/restapi/drinfo/**").hasAnyAuthority( "OBOPS/QAA" )
				// .antMatchers("/restapi/availability/**").hasAnyAuthority( "OBOPS/QAA" )
				// .antMatchers("/restapi/public/**").authenticated()
				.antMatchers("/**").authenticated()
		.and().csrf()
//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
				.disable();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(casAuthenticationProvider());
	}



}
