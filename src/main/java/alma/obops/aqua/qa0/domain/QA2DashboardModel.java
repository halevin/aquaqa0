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
package alma.obops.aqua.qa0.domain;

import java.util.List;

public class QA2DashboardModel {

    public String mousState;
    public String mousSubState;
    public String dataReducerName;
    public String dataReducerArc;
    public String dataReducerNode;
    public String plProcessingExec;
    public String projectsCode;
    public String mousStatusId;
    public List<String> prtsprTickets;
    public String schedBlockName;
    public String receiverBand;
    public int numberQA0PassExecBlocks;
    public String drmName;
    public String preferredArc;
    public String contactScientistName;
    public String latestQa0PassExecBlockDate;
    public String firstDateReadyForProcessing;
    public String firstDateProcessing;
    public String lastDateReadyForReview;
    public String lastDateVerified;
    public String qa2Status;
    public String lastDateDelivered;
    public double stagingTimeDays;
    public double assignmentTimeDays;
    public double analysisTimeDays;
    public double reviewTimeDays;
    public double deliveryTimeDays;
    public double obsToDeliveryTimeDays;
    public String pipeCalDate;
    public String manualCalDate;
    public String pipeImgDate;
    public String manualImgDate;
    public String comment;

    @Override
    public String toString() {
        return "QA2DashboardModel{" +
                "mousState='" + mousState + '\'' +
                ", mousSubState='" + mousSubState + '\'' +
                ", dataReducerName='" + dataReducerName + '\'' +
                ", dataReducerArc='" + dataReducerArc + '\'' +
                ", dataReducerNode='" + dataReducerNode + '\'' +
                ", plProcessingExec='" + plProcessingExec + '\'' +
                ", projectsCode='" + projectsCode + '\'' +
                ", mousStatusId='" + mousStatusId + '\'' +
                ", prtsprTickets=" + prtsprTickets +
                ", schedBlockName='" + schedBlockName + '\'' +
                ", receiverBand='" + receiverBand + '\'' +
                ", numberQA0PassExecBlocks=" + numberQA0PassExecBlocks +
                ", drmName='" + drmName + '\'' +
                ", preferredArc='" + preferredArc + '\'' +
                ", contactScientistName='" + contactScientistName + '\'' +
                ", latestQa0PassExecBlockDate='" + latestQa0PassExecBlockDate + '\'' +
                ", firstDateReadyForProcessing='" + firstDateReadyForProcessing + '\'' +
                ", firstDateProcessing='" + firstDateProcessing + '\'' +
                ", lastDateReadyForReview='" + lastDateReadyForReview + '\'' +
                ", lastDateVerified='" + lastDateVerified + '\'' +
                ", qa2Status='" + qa2Status + '\'' +
                ", lastDateDelivered='" + lastDateDelivered + '\'' +
                ", stagingTimeDays=" + stagingTimeDays +
                ", assignmentTimeDays=" + assignmentTimeDays +
                ", analysisTimeDays=" + analysisTimeDays +
                ", reviewTimeDays=" + reviewTimeDays +
                ", deliveryTimeDays=" + deliveryTimeDays +
                ", obsToDeliveryTimeDays=" + obsToDeliveryTimeDays +
                ", pipeCalDate='" + pipeCalDate + '\'' +
                ", manualCalDate='" + manualCalDate + '\'' +
                ", pipeImgDate='" + pipeImgDate + '\'' +
                ", manualImgDate='" + manualImgDate + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
