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

package alma.obops.aqua.qa0.utils;

import alma.obops.aqua.ExecBlockSearchCriteria;
import alma.obops.aqua.domain.QA0Status;
import alma.obops.aqua.qa0.domain.ExecBlockSearchParameters;

import java.util.Date;

public class ToolUtils {

	public static void checkIfDefined( final String propName, final String url ) {
		// Make sure the property is defined
		if( url == null || url.length() == 0 ) {
			String msg = "ObOps configuration property " + propName +
					" is not defined";
			throw new RuntimeException( msg );
		}
	}

	public static ExecBlockSearchCriteria convertToExecBlockSearchCriteria(ExecBlockSearchParameters searchParameters){
		ExecBlockSearchCriteria searchCriteria = new ExecBlockSearchCriteria();
		if ( searchParameters != null ) {
			if ( searchParameters.isUnset()) {
				searchCriteria.getQA0Statuses().add(QA0Status.UNSET);
			}
			if ( searchParameters.isPending()) {
				searchCriteria.getQA0Statuses().add(QA0Status.PENDING);
			}
			if ( searchParameters.isFail()) {
				searchCriteria.getQA0Statuses().add(QA0Status.FAIL);
			}
			if ( searchParameters.isSemipass()) {
				searchCriteria.getQA0Statuses().add(QA0Status.SEMIPASS);
			}
			if ( searchParameters.isPass()) {
				searchCriteria.getQA0Statuses().add(QA0Status.PASS);
			}
			searchCriteria.setIntervalBegin(searchParameters.getDateStart());
			searchCriteria.setIntervalEnd(searchParameters.getDateEnd());
			searchCriteria.setAcaCorrelator(searchParameters.isAca());
			searchCriteria.setBlCorrelator(searchParameters.isBl());
			searchCriteria.setCalProjects(searchParameters.isCalibrator());
			searchCriteria.setScienceProjects(searchParameters.isScience());
			searchCriteria.setExecBlockUID(searchParameters.getExecBlockUID());
			searchCriteria.setSchedBlockName(searchParameters.getSchedBlockName());
			searchCriteria.setObsProjectCode(searchParameters.getProjectCode());
		} else {
			if ( searchParameters.isUnset() ) {
				searchCriteria.getQA0Statuses().add(QA0Status.UNSET);
				searchCriteria.setIntervalBegin(new Date(new Date().getTime() - 100 * 365 * 24 * 60 * 60 * 1000));
				searchCriteria.setIntervalEnd(new Date());
			}
			// searchCriteria
		}
		return searchCriteria;
	} 


}
