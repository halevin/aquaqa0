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
package alma.obops.aqua.qa0.controllers;

import alma.obops.aqua.ObsUnitSetSearchCriteria;
import alma.obops.aqua.qa0.domain.QA2DashboardModel;
import alma.obops.aqua.qa0.domain.QA2DashboardSearchModel;
import alma.obops.aqua.qa0.service.QA2DashboardService;
import alma.obops.aqua.qa0.utils.DRAToolUtils;
import alma.obops.aqua.utils.AquaUtils;
import alma.obops.dam.apdm.domain.AccountToolSearch;
import alma.obops.security.SpringSecurityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class QA2DasboardRestController {

    private static Logger logger = Logger.getLogger(QA2DasboardRestController.class.getSimpleName());

    @Autowired
    private QA2DashboardService qa2DashboardService;

    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<QA2DashboardModel> getObsUnitSets(HttpServletRequest request,
                                                  @RequestBody(required = false) QA2DashboardSearchModel qa2DashboardSearchModel) {

        Principal p = request.getUserPrincipal();
        if (p == null) {
            return null;
        }
        ObsUnitSetSearchCriteria obsUnitSetSearchCriteria = DRAToolUtils.convertFromQA2DashboardSearchModelToObsUnitSetSearchCriteria(qa2DashboardSearchModel);
        long startSearch = System.currentTimeMillis();
        List<QA2DashboardModel> obsUnitSets = qa2DashboardService.searchObsUnitSets(obsUnitSetSearchCriteria);
        logger.info("==>> searchObUnitSets() duration : " + (System.currentTimeMillis() - startSearch) + " ms");

        return obsUnitSets;
    }

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveComment(HttpServletRequest request, @RequestBody Map<String, String> parameters) {

        Principal p = request.getUserPrincipal();
        if (p == null) {
            return null;
        }

        if (!SpringSecurityHelper.hasAnyRole("OBOPS/DRM")) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        if ( parameters != null ) {
            String ousId = parameters.get("ousId");
            String commentText = parameters.get("comment");

            qa2DashboardService.saveObsUnitSetComment(ousId, commentText, p.getName());
            return new ResponseEntity(HttpStatus.OK);

        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/filters")
    @ResponseBody
    public List<String> getFilters(HttpServletRequest request){

        Principal p = request.getUserPrincipal();
        if (p == null) {
            return null;
        }

        List<AccountToolSearch> accountToolSearches = qa2DashboardService.getUserFilters(p.getName());

        if ( accountToolSearches != null ) {
            return accountToolSearches.stream().map( a -> AquaUtils.clobToString(a.getSearchCriteria())).collect(Collectors.toList());
        }

        return null;
    }

    @RequestMapping(value = "/savefilter", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity saveFilter(HttpServletRequest request, @RequestBody Map<String, Object> parameters) {

        Principal p = request.getUserPrincipal();
        if (p == null) {
            return null;
        }
        if ( parameters != null ) {
            String name = (String)parameters.get("name");
            qa2DashboardService.saveUserFilter(name, parameters, p.getName());
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @CrossOrigin
    @RequestMapping(value = "/deletefilter/{name}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAvailability(HttpServletRequest request, @PathVariable("name") String name) throws Exception {

        Principal p = request.getUserPrincipal();
        if (p == null) {
            return null;
        }
        qa2DashboardService.deleteUserFilter(name, p.getName());

        return new ResponseEntity(HttpStatus.OK);
    }


}
