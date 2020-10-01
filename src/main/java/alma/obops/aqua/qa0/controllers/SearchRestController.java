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

import alma.obops.aqua.ExecBlockSearchCriteria;
import alma.obops.aqua.domain.ExecBlockView;
import alma.obops.aqua.qa0.domain.ExecBlockSearchParameters;
import alma.obops.aqua.qa0.utils.ToolUtils;
import alma.obops.aqua.service.ExecBlockService;
import alma.obops.boot.filters.RequestLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class SearchRestController {

	private static final String ENCODING = "UTF-8";
	private static final String RETURN_URL_COOKIE = "return-url";

	@Autowired
	private ExecBlockService execBlockService;

	protected Logger logger = LogManager.getLogger( RequestLoggingFilter.class );
	private static int MAX_RESULTS = 10000;

	public SearchRestController() {
	}

	@PostConstruct
	private void init() {
		// no-op
	}

    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<ExecBlockView> getExecBlocks(HttpServletRequest request,
                                                  @RequestBody(required = false) ExecBlockSearchParameters execBlockSearchParameters) {

		logger.trace("execBlockSearchParameters " + execBlockSearchParameters);
		System.out.println("execBlockSearchParameters " + execBlockSearchParameters);

		Principal p = request.getUserPrincipal();
        if (p == null) {
            return null;
        }

		long startSearch = System.currentTimeMillis();
		ExecBlockSearchCriteria searchCriteria = ToolUtils.convertToExecBlockSearchCriteria(execBlockSearchParameters);
		System.out.println("query with search criteria " + searchCriteria);
		List<ExecBlockView> execBlockResults = execBlockService.findWithCriteria(searchCriteria, MAX_RESULTS);
        logger.info("==>> getExecBlocks() duration : " + (System.currentTimeMillis() - startSearch) + " ms. " + (execBlockResults!=null?execBlockResults.size():"0") + " results");

        return execBlockResults;
    }

}
