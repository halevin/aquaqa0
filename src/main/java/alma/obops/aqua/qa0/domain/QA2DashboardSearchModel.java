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

import alma.obops.common.utils.TextUtils;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

public class QA2DashboardSearchModel {

    private List<String> state;
    private List<String> substate;
    private List<String> preferredArc;
    private List<String> projectCycle;
    private List<String> projectType;
    private List<String> projectGrade;
    private List<String> requestedArray;
    private List<String> dataReducerArc;
    private List<String> plProcessingExec;
    private String drm;
    private boolean delivered24hours;

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

    public List<String> getSubstate() {
        return substate;
    }

    public void setSubstate(List<String> substate) {
        this.substate = substate;
    }

    public List<String> getPreferredArc() {
        return preferredArc;
    }

    public void setPreferredArc(List<String> preferredArc) {
        this.preferredArc = preferredArc;
    }

    public List<String> getProjectCycle() {
        return projectCycle;
    }

    public void setProjectCycle(List<String> projectCycle) {
        this.projectCycle = projectCycle;
    }

    public List<String> getProjectType() {
        return projectType;
    }

    public void setProjectType(List<String> projectType) {
        this.projectType = projectType;
    }

    public List<String> getProjectGrade() {
        return projectGrade;
    }

    public void setProjectGrade(List<String> projectGrade) {
        this.projectGrade = projectGrade;
    }

    public List<String> getRequestedArray() {
        return requestedArray;
    }

    public void setRequestedArray(List<String> requestedArray) {
        this.requestedArray = requestedArray;
    }

    public List<String> getDataReducerArc() {
        return dataReducerArc;
    }

    public void setDataReducerArc(List<String> dataReducerArc) {
        this.dataReducerArc = dataReducerArc;
    }

    public List<String> getPlProcessingExec() {
        return plProcessingExec;
    }

    public void setPlProcessingExec(List<String> plProcessingExec) {
        this.plProcessingExec = plProcessingExec;
    }

    public String getDrm() {
        return drm;
    }

    public void setDrm(String drm) {
        this.drm = drm;
    }

    public boolean isDelivered24hours() {
        return delivered24hours;
    }

    public void setDelivered24hours(boolean delivered24hours) {
        this.delivered24hours = delivered24hours;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(state) &&
                CollectionUtils.isEmpty(substate) &&
                CollectionUtils.isEmpty(preferredArc) &&
                CollectionUtils.isEmpty(projectCycle) &&
                CollectionUtils.isEmpty(projectType) &&
                CollectionUtils.isEmpty(projectGrade) &&
                CollectionUtils.isEmpty(requestedArray) &&
                CollectionUtils.isEmpty(dataReducerArc) &&
                CollectionUtils.isEmpty(plProcessingExec) &&
                TextUtils.isEmpty(drm) && !delivered24hours;
    }


    @Override
    public String toString() {
        return "QA2DashboardSearchModel{" +
                "state=" + state +
                ", substate=" + substate +
                ", preferredArc=" + preferredArc +
                ", projectCycle=" + projectCycle +
                ", projectType=" + projectType +
                ", projectGrade=" + projectGrade +
                ", requestedArray=" + requestedArray +
                ", dataReducerArc=" + dataReducerArc +
                ", plProcessingExec=" + plProcessingExec +
                ", drm='" + drm + '\'' +
                '}';
    }
}
