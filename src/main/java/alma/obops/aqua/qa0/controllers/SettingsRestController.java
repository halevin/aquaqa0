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

import alma.obops.aqua.domain.QA0StatusReason;
import alma.obops.boot.filters.RequestLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static alma.obops.aqua.domain.QA0StatusReason.*;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class SettingsRestController {

	private static final String ENCODING = "UTF-8";
	private static final String RETURN_URL_COOKIE = "return-url";

	@Autowired
	Environment env;

	protected Logger logger = LogManager.getLogger( RequestLoggingFilter.class );

	public SettingsRestController() {
	}

	@PostConstruct
	private void init() {
		// no-op
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
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

		String propWebShiftLogURL = "obops.webslt.url";
		final String webShiftLogURL = env.getProperty( propWebShiftLogURL );
		if ( webShiftLogURL == null ) throw new IOException(propWebShiftLogURL + " property is not defined");

		String propJiraURL = "obops.webaqua.jira.url";
		final String jiraURL = env.getProperty( propJiraURL );
		if ( jiraURL == null ) throw new IOException(propJiraURL + " property is not defined");

		List<Map<String,String>> qa0SemiPassReasons = Arrays.asList(
				QA0_ARRAY.getJSON(),
				QA0_CORR.getJSON(),
				QA0_AOD.getJSON(),
				QA0_P2G.getJSON(),
				QA0_AMPPHASE.getJSON(),
				QA0_CALIBRATOR.getJSON(),
				QA0_FLUX.getJSON(),
				QA0_SCHED.getJSON(),
				QA0_WEATHER.getJSON(),
				QA2_CORR.getJSON(),
				QA2_ARRAY.getJSON(),
				QA2_AMPPHASE.getJSON(),
				QA2_TPOFF.getJSON(),
				QA2_CALIBRATOR.getJSON(),
				QA2_SCHED.getJSON(),
				QA2_DATA.getJSON());

		List<Map<String,String>> qa0PendingReasons = Arrays.asList(
				FLUX_CAL.getJSON(),
				CORRECT_ANT_POS.getJSON(),
				OTHER.getJSON());

		Map<String,Object> settings = new HashMap<>();
		settings.put("helpURL", helpURL);
		settings.put("email", email);
		settings.put("aquaQA2URL", aquaURL + (aquaURL.endsWith("/")?"":"/"));
		settings.put("protrackURL", protrackURL + (protrackURL.endsWith("/")?"":"/"));
		settings.put("webShiftLogURL", webShiftLogURL + (webShiftLogURL.endsWith("/")?"":"/"));
		settings.put("jiraURL", jiraURL + (jiraURL.endsWith("/")?"":"/"));
		settings.put("qa0SemiPassReasons", qa0SemiPassReasons);
		settings.put("qa0PendingReasons", qa0PendingReasons);

		return settings;
	}

}
