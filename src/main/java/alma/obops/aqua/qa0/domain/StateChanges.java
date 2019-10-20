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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@NamedQueries({
@NamedQuery(name="StateChanges.getChanges", query=" SELECT e "
		+ "FROM  StateChanges e "
		+ "WHERE (e.domainentityid IN (:domuids) AND (e.entitytype LIKE 'SBK' OR e.entitytype LIKE 'PRJ') "
		+ "OR   e.statusentityid IN (:statuids) AND (e.entitytype LIKE 'OUS' OR e.entitytype LIKE 'OUT')) "
		// Projects filter
		+ "AND (e.entitytype LIKE 'PRJ' AND (e.domainentitystate LIKE 'Approved' OR "
		+       "e.domainentitystate LIKE 'Ready' OR e.domainentitystate LIKE 'Phase2Submitted' OR "
		+       "e.domainentitystate LIKE 'Completed' OR "
		+       "e.domainentitystate LIKE 'ObservingTimedOut' OR e.domainentitystate LIKE 'Broken' OR "
		+       "e.domainentitystate LIKE 'Repaired' OR e.domainentitystate LIKE 'Deleted') OR"
		// OUS filter
		+ 		"(e.entitytype LIKE 'OUS' OR e.entitytype LIKE 'OUT') AND (e.domainentitystate LIKE 'ObservingTimedOut' OR "
		+ 		"e.domainentitystate LIKE 'Broken' OR e.domainentitystate LIKE 'Repaired' OR "
		+ 		"e.domainentitystate LIKE 'ReadyForProcessing' OR e.domainentitystate LIKE 'ReadyToDeliver' OR "
		+ 		"e.domainentitystate LIKE 'Delivered' OR e.domainentitystate LIKE 'QA3InProgress' OR "
		+ 		"e.domainentitystate LIKE 'Deleted') OR "
		// SB filter
		+ 		"e.entitytype LIKE 'SBK' AND (e.domainentitystate LIKE 'ObservingTimedOut' OR "
		+ 		"e.domainentitystate LIKE 'Broken' OR e.domainentitystate LIKE 'Repaired' OR "
		+ 		"e.domainentitystate LIKE 'FullyObserved' OR e.domainentitystate LIKE 'Deleted')) "
		+ "ORDER BY timestamp desc, DOMAIN_ENTITY_ID "),
@NamedQuery(name="StateChanges.getTimedOutOUS", query=" SELECT e "
		+ "FROM  StateChanges e "
		+ "WHERE (e.domainentitystate LIKE 'ObservingTimedOut') "
		+ "AND   e.statusentityid = (:statuid) AND (e.entitytype LIKE 'OUS' OR e.entitytype LIKE 'OUT') "
		+ "ORDER BY timestamp desc ")
})
@Table( name="PIV_STATE_CHANGES" )
public class StateChanges implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8876913371508099531L;
	@Id
	@Column(name="STATE_CHANGES_ID")
	private String statechangesid;
	@Column(name="STATUS_ENTITY_ID")
	private String statusentityid;
	@Column(name="DOMAIN_ENTITY_ID")
	private String domainentityid;
	@Column(name="DOMAIN_ENTITY_STATE")
	private String domainentitystate;
	@Temporal(TemporalType.TIMESTAMP)	
	private Date timestamp;
	@Column(name="ENTITY_TYPE")
	private String entitytype;
	@Column(name="DOMAIN_PART_ID")
	private String domainpartid;
	@Column(name="CHILD_SB_UID")
	private String childSbUid;
	
	transient private String projectuid;
	transient private String projName;
	transient private String name;

	public String getStatechangesid() {
		return statechangesid;
	}

	public String getStatusentityid() {
		return statusentityid;
	}

	public String getDomainentityid() {
		return domainentityid;
	}

	public String getDomainentitystate() {
		return domainentitystate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getEntitytype() {
		return entitytype;
	}

	public String getDomainpartid() {
		return domainpartid;
	}

	public String getProjectuid() {
		return projectuid;
	}

	public void setProjectuid(String projectuid) {
		this.projectuid = projectuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getChildSbUid() {
		return (childSbUid!=null)?childSbUid:"";
	}

	public void setChildSbUid(String childSbUid) {
		this.childSbUid = childSbUid;
	}

	@Transient
	public String getDate(){
		if ( timestamp == null ) return "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(timestamp);
	}

	@Override
	public String toString() {
		return "SnoopiProjectOperations [Id=" + statechangesid +
				", statusentityid=" + statusentityid +
				", domainentityid=" + domainentityid +
				", domainentitystate=" + domainentitystate +
				", timestamp=" + timestamp +
				", entitytype=" + entitytype +
				", domainpartid=" + domainpartid +
//				", startDate=" + startDate.toString() + 
//				", endDate=" + endDate.toString() + 
				"]";
	}
	
	public void assignNameAndPUID(Map<String, String> names, Map<String, String> projectUIDs) {
		if (getEntitytype().equalsIgnoreCase("SBK")) {
			setProjectuid(projectUIDs.get(getDomainentityid()));
			setName(names.get(getDomainentityid()));
			setProjName(names.get(projectUIDs.get(getDomainentityid())));
		}
		if (getEntitytype().equalsIgnoreCase("PRJ")) {
			setProjectuid(projectUIDs.get(getDomainentityid()));
			setName(names.get(getDomainentityid()));
		}
		if (getEntitytype().equalsIgnoreCase("OUS") || getEntitytype().equalsIgnoreCase("OUT")) {
			setProjectuid(getDomainentityid());
			setName(names.get(getStatusentityid()));
			setProjName(names.get(getDomainentityid()));
		}
	}
}
