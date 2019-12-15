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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import alma.archive.projectcode.TypeCode;
import alma.obops.aqua.AquaConstants;
import alma.obops.aqua.ObsUnitSetSearchCriteria;
import alma.obops.aqua.qa0.DRAToolConstants;
import alma.obops.aqua.qa0.domain.DTDataReducer;
import alma.obops.aqua.qa0.domain.QA2DashboardSearchModel;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;

public class DRAToolUtils {

	public static void checkIfDefined( final String propName, final String url ) {
		// Make sure the property is defined
		if( url == null || url.length() == 0 ) {
			String msg = "ObOps configuration property " + propName +
					" is not defined";
			throw new RuntimeException( msg );
		}
	}

	public static boolean isDataReducer(List<DTDataReducer> dataReducerList, String userId) {
		if ( userId != null ) {
			for (DTDataReducer dr : dataReducerList) {
				if (dr.getUserId().trim().equals(userId.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	public static ObsUnitSetSearchCriteria convertFromQA2DashboardSearchModelToObsUnitSetSearchCriteria(QA2DashboardSearchModel model) {

		ObsUnitSetSearchCriteria obsUnitSetSearchCriteria = new ObsUnitSetSearchCriteria();

		if ( model != null ) {

			if ( !CollectionUtils.isEmpty(model.getState())) {
				Set<String> statesSet = new HashSet<>();
				Iterator iterator = model.getState().iterator();
				while  ( iterator.hasNext() ) {
					if (((String)iterator.next()).contains("Delivered")) {
						iterator.remove();
						obsUnitSetSearchCriteria.setDelivered24hours(true);
					}
				}

				statesSet.addAll(model.getState());
				obsUnitSetSearchCriteria.setPossibleStates(statesSet);
			} else if ( !model.isDelivered24hours() ) {
				Set<String> statesSet = new HashSet<>();
				statesSet.addAll(DRAToolConstants.qa2DashboardStates);
				obsUnitSetSearchCriteria.setPossibleStates(statesSet);
			}


			if ( !CollectionUtils.isEmpty(model.getSubstate())) {
				Set<String> substatesSet = new HashSet<>();
				substatesSet.addAll(model.getSubstate());
				obsUnitSetSearchCriteria.setPossibleFlags(substatesSet);
			}
			if ( !CollectionUtils.isEmpty(model.getRequestedArray())) {
				Set<AquaConstants.Array> arraysSet = new HashSet<>();
				if (model.getRequestedArray().contains("12M")) {
					arraysSet.add(AquaConstants.Array.TM);
				}
				if (model.getRequestedArray().contains("7M")) {
					arraysSet.add(AquaConstants.Array.M7);
				}
				if (model.getRequestedArray().contains("TP")) {
					arraysSet.add(AquaConstants.Array.TP);
				}
				obsUnitSetSearchCriteria.setPossibleArrays(arraysSet);
			}
			if ( !CollectionUtils.isEmpty(model.getPreferredArc())) {
				Set<AquaConstants.ARC> arcSet = new HashSet<>();
				for ( String arc : model.getPreferredArc()) {
					arcSet.add(AquaConstants.ARC.getByName(arc));
				}
				obsUnitSetSearchCriteria.setPrefARCs(arcSet);
			}
			if ( !CollectionUtils.isEmpty(model.getDataReducerArc())) {
				Set<AquaConstants.ARC> arcSet = new HashSet<>();
				for ( String arc : model.getDataReducerArc()) {
					arcSet.add(AquaConstants.ARC.getByName(arc));
				}
				obsUnitSetSearchCriteria.setPossibleARCs(arcSet);
			}
			if ( !CollectionUtils.isEmpty(model.getPlProcessingExec())) {
				Set<AquaConstants.ARC> plProcessingExecSet = new HashSet<>();
				for ( String arc : model.getPlProcessingExec()) {
					plProcessingExecSet.add(AquaConstants.ARC.getByName(arc));
				}
				obsUnitSetSearchCriteria.setPlProcExecs(plProcessingExecSet);
			}
			if ( !CollectionUtils.isEmpty(model.getProjectGrade())) {
				Set<String> gradeSet = new HashSet<>();
				gradeSet.addAll(model.getProjectGrade());
				obsUnitSetSearchCriteria.setProjectGrades(gradeSet);
			}
			if ( !CollectionUtils.isEmpty(model.getProjectCycle())) {
				Set<String> cycleSet = new HashSet<>();
				cycleSet.addAll(model.getProjectCycle());
				obsUnitSetSearchCriteria.setProjectCycles(cycleSet);
			}
			if ( !CollectionUtils.isEmpty(model.getProjectType())) {
				Set<TypeCode> typeSet = new HashSet<>();
				for ( String projectType : model.getProjectType()) {
					typeSet.add(TypeCode.typeCodeAbbreviation2enum(projectType));
				}
				obsUnitSetSearchCriteria.setProjectTypes(typeSet);
			}
//			if ( model.isDelivered24hours() ) {
//				obsUnitSetSearchCriteria.setStateForHistory("Delivered");
//				obsUnitSetSearchCriteria.setStateIntervalBegin(new Date((DateUtilsLite.getNowUT()).getTime() - 24 * 3600 * 1000));
//				obsUnitSetSearchCriteria.setStateIntervalEnd(DateUtilsLite.getNowUT());
//			}
			if (StringUtils.hasText(model.getDrm())) {
				obsUnitSetSearchCriteria.setDrm(model.getDrm());
			}
		}

		return obsUnitSetSearchCriteria;
	}

}
