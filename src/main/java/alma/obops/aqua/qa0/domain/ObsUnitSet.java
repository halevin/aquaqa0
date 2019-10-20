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
@Table( name="piv_obs_unit_set" )
public class ObsUnitSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9050163273273361177L;
	@Id
	private String statusEntityId;
	private String obsProjectId;
	private String partid;
	private String parentObsUnitSetStatusId;
	private String domainEntityState;
	private String name;
	private String requestedarray;
	private String qa2status;
	
	public String getObsProjectId() {
		return obsProjectId;
	}

	public void setObsProjectId(String obsProjectId) {
		this.obsProjectId = obsProjectId;
	}

	public String getStatusEntityId() {
		return statusEntityId;
	}
	
	public String getPartid() {
		return partid;
	}

	public String getParentObsUnitSetStatusId() {
		return parentObsUnitSetStatusId;
	}

	public void setParentObsUnitSetStatusId(String parentObsUnitSetStatusId) {
		this.parentObsUnitSetStatusId = parentObsUnitSetStatusId;
	}

	public String getDomainEntityState() {
		return getStateForPi(domainEntityState);
	}
	
	public String getName() {
		return name;
	}
	
	public String getRequestedarray(){
		return requestedarray;
	}
	
	public String getQa2status() {
		return qa2status;
	}

	private String getStateForPi(String ousState) {

		// ICT-2922 - 'happy flow' for PI
		if ("ManualProcessing".equalsIgnoreCase(ousState)
				|| "PipelineError".equalsIgnoreCase(ousState)) {
			return "PipelineProcessing";
		} else if ("ReprocessingRequired".equalsIgnoreCase(ousState)) {
			return "ReadyForProcessing";
		}

		return ousState;
	}

}