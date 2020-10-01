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

import alma.obops.aqua.domain.AquaStatusHistory;
import alma.obops.aqua.domain.ExecBlock;
import alma.obops.aqua.domain.ExecBlockComment;
import alma.obops.aqua.domain.ExecutionFraction;
import alma.obops.aqua.domain.aoscheck.AosCheckSummary;
import alma.obops.aqua.qa0.AquaGUIConstants;
import alma.obops.aqua.qa0.domain.AquaStatusHistoryModel;
import alma.obops.aqua.qa0.domain.ExecBlockModel;
import alma.obops.aqua.qa0.service.ExecBlockHelper;
import alma.obops.aqua.reports.AquaReport;
import alma.obops.aqua.service.ExecBlockService;
import alma.obops.aqua.servlet.AquaQA0ReportProducer;
import alma.obops.aqua.servlet.AquaReportProducerRequestHandler;
import alma.obops.boot.filters.RequestLoggingFilter;
import alma.obops.utils.ServletUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class ExecBlockRestController {

	private static final String ENCODING = "UTF-8";
	private static final String RETURN_URL_COOKIE = "return-url";

	protected Logger logger = LogManager.getLogger( RequestLoggingFilter.class );

	@Autowired
	private ExecBlockHelper execBlockHelper;

	public ExecBlockRestController() {
	}

	@PostConstruct
	private void init() {
		// no-op
	}

    @RequestMapping(value = "/execblock/{uid}", method = RequestMethod.GET)
    @ResponseBody
    public ExecBlockModel getExecBlock(HttpServletRequest request,
								  @PathVariable("uid") String execBlockUIDNormalized) {

		Principal p = request.getUserPrincipal();
        if (p == null) {
            return null;
        }

        if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");
			logger.trace("execBlock UID " + execBlockUID);

			try {
				return execBlockHelper.build(execBlockUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
        return null;
    }

	@RequestMapping(value = "/execblock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateExecBlock(HttpServletRequest request,
											 @RequestBody ExecBlockModel execBlockModel) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		logger.info("request update EB " + execBlockModel);

		try {
			execBlockHelper.updateExecBlock(execBlockModel);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity(HttpStatus.OK);
	}


	@RequestMapping(value = "/execfraction/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getExecutionFraction(HttpServletRequest request,
											   @PathVariable("uid") String execBlockUIDNormalized) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");
			logger.trace("execBlock UID " + execBlockUID);

			try {
				Map<String, Object> executionFractionResponse = new HashMap<>();
				ExecutionFraction executionFraction = execBlockHelper.computeExecutionFraction(execBlockUID);
				executionFractionResponse.put("executionFractionReport", executionFraction.toString());
				executionFractionResponse.put("executionFraction", executionFraction.executionFraction);
				return executionFractionResponse;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/aoscheck/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public AosCheckSummary getAOSCheck(HttpServletRequest request,
									   @PathVariable("uid") String execBlockUIDNormalized) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");
			logger.trace("execBlock UID " + execBlockUID);

			try {
				return execBlockHelper.readAosCheck(execBlockUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/qa0history/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public List<AquaStatusHistoryModel> getQA0StatusHistory(HttpServletRequest request,
															@PathVariable("uid") String execBlockUIDNormalized) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");
			logger.trace("execBlock UID " + execBlockUID);

			try {
				return execBlockHelper.readQA0StatusHistory(execBlockUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/report/{uid}", method = RequestMethod.GET)
	public ResponseEntity getQA0ReportPdf(HttpServletRequest request,
								@PathVariable("uid") String execBlockUIDNormalized,
								@RequestParam("format") String format,
							   HttpServletResponse response) throws Exception {

		String loggedInUser = getPrincipal(request);

		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");
			logger.info("getQA0ReportPdf loggedInUser: " + loggedInUser + " execBlockUID: " + execBlockUID);

			try (InputStream propPdfStream = execBlockHelper.getQA0Report(execBlockUID, format);
				 OutputStream outStream = response.getOutputStream();) {

				String headerValue = String.format("inline; filename=\"QA0_report_%s\"", execBlockUID + ".pdf");
				response.setHeader("Content-Disposition", headerValue);

				propPdfStream.transferTo(outStream);

			} catch (Exception ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}


		}
		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/antenna-flags/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public List<List<String>> getAntennaFlags(HttpServletRequest request,
															@PathVariable("uid") String execBlockUIDNormalized) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");
			logger.trace("execBlock UID " + execBlockUID);

			try {
				return execBlockHelper.getAntennaFlags(execBlockUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private String getPrincipal(HttpServletRequest request){
		Principal p = request.getUserPrincipal();
		if (p == null) {
			throw new RuntimeException( "User principal is null (?)" );
		}
		return p.getName();
	}


}
