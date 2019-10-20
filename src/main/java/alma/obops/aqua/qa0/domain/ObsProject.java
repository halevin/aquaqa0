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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name="piv_obs_project" )
public class ObsProject {

	@Id
	private String code;
	private String pi;
	@Column(name="contact_account_id")
	private String contactScientist;
	private String firstname;
	private String lastname;
	private String contactFirstname;
	private String contactLastname;
	private String title;
	private String prjVersion;
	private String prjLetterGrade;
	private String prjGrade;
	private String rank;
	private String obsProjectId;
	@Column(name="domain_entity_state")
	private String domainEntityState;

	transient private List<Object> children;
	transient private int qa0PassCount = 0;
	transient private boolean isActive;
	transient private String authorType;
	transient private String delegeeType;

	public ObsProject() {
		children = new ArrayList<Object>();
	}
	
 	public void addChild( Object child ) {
		children.add( child );
	}
	
	public List<Object> getChildren() {
		return children;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDomainEntityState() {
		return domainEntityState;
	}

	public String getObsProjectId() {
		return obsProjectId;
	}

	public void setDomainEntityState(String domainEntityState) {
		this.domainEntityState = domainEntityState;
	}

	public String getPi() {
		return pi;
	}

	public void setPi(String pi) {
		this.pi = pi;
	}

	public String getPrjVersion() {
		return prjVersion;
	}

	public String getPrjGrade() {
		return prjGrade;
	}

	public String getRank() {
		return rank;
	}

	public int getQa0PassCount() {
		return this.qa0PassCount;
	}

	public String getTitle() {
		return title;
	}

	public void setQa0PassCount( int count ) {
		this.qa0PassCount = count;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getPrjLetterGrade() {
		return prjLetterGrade;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getContactScientist() {
		return contactScientist;
	}

	public void setContactScientist(String contactScientist) {
		this.contactScientist = contactScientist;
	}

	public String getContactFirstname() {
		return contactFirstname;
	}

	public String getContactLastname() {
		return contactLastname;
	}

	public String getAuthorType() {
		return authorType;
	}

	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	public String getDelegeeType() {
		return delegeeType;
	}

	public void setDelegeeType(String delegeeType) {
		this.delegeeType = delegeeType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, contactFirstname, contactLastname, contactScientist, domainEntityState, firstname,
				lastname, obsProjectId, pi, prjGrade, prjLetterGrade, prjVersion, rank, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObsProject other = (ObsProject) obj;
		return Objects.equals(code, other.code) && Objects.equals(contactFirstname, other.contactFirstname)
				&& Objects.equals(contactLastname, other.contactLastname)
				&& Objects.equals(contactScientist, other.contactScientist)
				&& Objects.equals(domainEntityState, other.domainEntityState)
				&& Objects.equals(firstname, other.firstname) && Objects.equals(lastname, other.lastname)
				&& Objects.equals(obsProjectId, other.obsProjectId) && Objects.equals(pi, other.pi)
				&& Objects.equals(prjGrade, other.prjGrade) && Objects.equals(prjLetterGrade, other.prjLetterGrade)
				&& Objects.equals(prjVersion, other.prjVersion) && Objects.equals(rank, other.rank)
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "ObsProject [code=" + code + ", pi=" + pi + ", contactScientist=" + contactScientist + ", firstname="
				+ firstname + ", lastname=" + lastname + ", contactFirstname=" + contactFirstname + ", contactLastname="
				+ contactLastname + ", title=" + title + ", prjVersion=" + prjVersion + ", prjLetterGrade="
				+ prjLetterGrade + ", prjGrade=" + prjGrade + ", rank=" + rank + ", obsProjectId=" + obsProjectId
				+ ", domainEntityState=" + domainEntityState + ", children=" + children + ", qa0PassCount="
				+ qa0PassCount + ", isActive=" + isActive + ", authorType=" + authorType + ", delegeeType="
				+ delegeeType + "]";
	}
}