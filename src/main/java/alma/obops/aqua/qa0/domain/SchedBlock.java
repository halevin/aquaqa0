/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2014
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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table( name="piv_sched_block" )
public class SchedBlock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7538535899736575344L;
	@Id
	private String statusEntityId;
	private String obsProjectId;
	private String parentObsUnitSetStatusId;
	private String domainEntityState;
	private String flags;
	private Integer executionCount;
	private String schedBlockUid;
	private String sbName;
	private Double refRa;
	private Double refDec;
	private String totalestimatedtime;
	private String totalestimatedtimeunits;
	private String qa2status;
	private String nominalconfiguration;
	
	transient private String projName;  
	transient private boolean isActive;
	transient private int qa0PassCount = 0;
	transient private String authorType;
	
	public String getDomainEntityState() {
		return domainEntityState;
	}

	public String getObsProjectId() {
		return obsProjectId;
	}

	public void setObsProjectId(String obsProjectId) {
		this.obsProjectId = obsProjectId;
	}

	public String getParentObsUnitSetStatusId() {
		return parentObsUnitSetStatusId;
	}

	public void setParentObsUnitSetStatusId(String parentObsUnitSetStatusId) {
		this.parentObsUnitSetStatusId = parentObsUnitSetStatusId;
	}

	public int getQa0PassCount() {
		return qa0PassCount;
	}
	
	public String getSbName() {
		return sbName;
	}
	
	public String getSchedBlockUid() {
		return schedBlockUid;
	}
	
	/*
	 * amchavan, 21-Aug-2015: commented out
	private String getStateForPi(String sbState, String statusFlags) {

		// ICT-2922 - 'happy flow' for PI
		// ICT-3001 Waiting ForP2G
		if ("Ready".equalsIgnoreCase(sbState) 
			|| "Running".equalsIgnoreCase(sbState) 
			|| "Suspended".equalsIgnoreCase(sbState)
			|| ("Waiting".equalsIgnoreCase(sbState) 
					&& "ForP2G".equalsIgnoreCase(statusFlags))){ 				
			if (executionCount > 0) {
				return "PartiallyObserved";
			} else {
				return "Ready";
			}
		}

		return sbState.toString();

	}
	*/

	public String getStatusEntityId() {
		return statusEntityId;
	}

	public String getTotalestimatedtime() {
		return totalestimatedtime;
	}

	public String getTotalestimatedtimeunits() {
		return totalestimatedtimeunits;
	}
	
	public void setQa0PassCount( int qa0PassCount ) {
		this.qa0PassCount = qa0PassCount;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public Integer getExecutionCount() {
		return executionCount;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getQa2status() {
		return qa2status;
	}

	public Double getRefRa() {
		return refRa;
	}

	public Double getRefDec() {
		return refDec;
	}

	public String getAuthorType() {
		return authorType;
	}

	public String getNominalconfiguration() {
		return nominalconfiguration;
	}


	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}
}
