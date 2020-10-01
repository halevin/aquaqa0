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

package alma.obops.aqua.qa0;

import alma.entity.xmlbinding.valuetypes.types.StatusTStateType;
import alma.lifecycle.stateengine.constants.StateFlag;

import java.util.ArrayList;

public class ToolConstants {

    public static final String QUALIFICATION_MANUAL_CAL = "MANCAL";
    public static final String QUALIFICATION_MANUAL_IMG = "MANIMG";
    public static final String QUALIFICATION_WEBLOG_REVIEW = "WLREVIEW";
    public static final String QUALIFICATION_QA2_APPROVAL = "QA2APPRV";

    public static ArrayList<String> qa2DashboardStates = new ArrayList<>();
    static {
        qa2DashboardStates.add(StatusTStateType.READYFORPROCESSING.toString());
        qa2DashboardStates.add(StatusTStateType.PROCESSING.toString());
        qa2DashboardStates.add(StatusTStateType.PROCESSINGPROBLEM.toString());
        qa2DashboardStates.add(StatusTStateType.READYFORREVIEW.toString());
        qa2DashboardStates.add(StatusTStateType.REVIEWING.toString());
        qa2DashboardStates.add(StatusTStateType.VERIFIED.toString());
        qa2DashboardStates.add(StatusTStateType.DELIVERYINPROGRESS.toString());
        qa2DashboardStates.add("Delivered within the last 24 h");
    }

    public static ArrayList<String> qa2DashboardSubstates = new ArrayList<>();
    static {
        qa2DashboardSubstates.add(StateFlag.PIPELINECALIBRATION.toString());
        qa2DashboardSubstates.add(StateFlag.PIPELINEIMAGING.toString());
        qa2DashboardSubstates.add(StateFlag.PIPELINECALANDIMG.toString());
        qa2DashboardSubstates.add(StateFlag.PIPELINESINGLEDISH.toString());
        qa2DashboardSubstates.add(StateFlag.MANUALCALIBRATION.toString());
        qa2DashboardSubstates.add(StateFlag.MANUALIMAGING.toString());
        qa2DashboardSubstates.add(StateFlag.MANUALCALANDIMG.toString());
        qa2DashboardSubstates.add(StateFlag.MANUALSINGLEDISH.toString());
    }

}
