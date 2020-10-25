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

import alma.obops.aqua.qa0.domain.ExecBlockCommentModel;
import alma.obops.aqua.qa0.service.ExecBlockHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/restapi")
public class CommentsRestController {

	@Autowired
	private ExecBlockHelper execBlockHelper;

	protected Logger logger = LogManager.getLogger( CommentsRestController.class );

	@RequestMapping(value = "/comment/{uid}", method = RequestMethod.GET)
	@ResponseBody
	public List<ExecBlockCommentModel> getComments(HttpServletRequest request,
												   @PathVariable("uid") String execBlockUIDNormalized) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		if ( execBlockUIDNormalized != null ) {
			String execBlockUID = execBlockUIDNormalized.replace("___", "://").replaceAll("_", "/");
			logger.trace("execBlock UID " + execBlockUID);

			try {
				return execBlockHelper.getExecBlockComments(execBlockUID).stream().map( c -> {
					ExecBlockCommentModel execBlockCommentModel = new ExecBlockCommentModel();
					execBlockCommentModel.id = c.getCommentId();
					execBlockCommentModel.author = c.getAuthor();
					execBlockCommentModel.timestamp = c.getTimestamp();
					execBlockCommentModel.comment = c.getComment();
					execBlockCommentModel.execBlockUid = execBlockUID;
					return  execBlockCommentModel;
				}).collect(Collectors.toList());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Collections.emptyList();
	}

	@RequestMapping(value = "/comment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateSaveComment(HttpServletRequest request,
										  @RequestBody ExecBlockCommentModel execBlockCommentModel) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		try {
			execBlockHelper.updateComment(execBlockCommentModel, p.getName());
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
	public ResponseEntity deleteSaveComment(HttpServletRequest request,
											@PathVariable("id") Long commentId) {

		Principal p = request.getUserPrincipal();
		if (p == null) {
			return null;
		}

		try {
			execBlockHelper.deleteComment(commentId, p.getName());
		} catch (Exception e) {
			logger.warn(e.getMessage());
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity(HttpStatus.OK);
	}



}
