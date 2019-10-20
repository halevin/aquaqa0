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

import java.util.Objects;

public class SchedBlockSimpleEntity {

	private String obsProjectId;
	private String domainEntityState;
	private String flag;
	private String schedBlockUid;
	private String sbName;
	private int qa0PassCount = 0;

	public SchedBlockSimpleEntity(String obsProjectId, String domainEntityState, String flag, String schedBlockUid, String sbName, int qa0PassCount) {
		this.obsProjectId = obsProjectId;
		this.domainEntityState = domainEntityState;
		this.flag = flag;
		this.schedBlockUid = schedBlockUid;
		this.sbName = sbName;
		this.qa0PassCount = qa0PassCount;
	}

	public String getObsProjectId() {
		return obsProjectId;
	}

	public void setObsProjectId(String obsProjectId) {
		this.obsProjectId = obsProjectId;
	}

	public String getDomainEntityState() {
		return domainEntityState;
	}

	public void setDomainEntityState(String domainEntityState) {
		this.domainEntityState = domainEntityState;
	}

	public String getSchedBlockUid() {
		return schedBlockUid;
	}

	public void setSchedBlockUid(String schedBlockUid) {
		this.schedBlockUid = schedBlockUid;
	}

	public String getSbName() {
		return sbName;
	}

	public void setSbName(String sbName) {
		this.sbName = sbName;
	}

	public int getQa0PassCount() {
		return qa0PassCount;
	}

	public void setQa0PassCount(int qa0PassCount) {
		this.qa0PassCount = qa0PassCount;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public int hashCode() {
		return Objects.hash(domainEntityState, flag, obsProjectId, sbName, schedBlockUid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchedBlockSimpleEntity other = (SchedBlockSimpleEntity) obj;
		return Objects.equals(domainEntityState, other.domainEntityState) && Objects.equals(flag, other.flag)
				&& Objects.equals(obsProjectId, other.obsProjectId)
				&& Objects.equals(sbName, other.sbName) && Objects.equals(schedBlockUid, other.schedBlockUid);
	}

	@Override
	public String toString() {
		return "SchedBlockSimpleEntity{" +
				"obsProjectId='" + obsProjectId + '\'' +
				", domainEntityState='" + domainEntityState + '\'' +
				", schedBlockUid='" + schedBlockUid + '\'' +
				", sbName='" + sbName + '\'' +
				", qa0PassCount=" + qa0PassCount +
				'}';
	}
}
