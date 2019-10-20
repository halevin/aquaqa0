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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "piv_obs_proposals")
public class ObsProposal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1792007335756939181L;
	@Id
	private String code;
	private String title;
	private String associatedexec;
	private String scientificCategory;
	private String scientificCategoryString;
	private String abstractText;
	private String cycle;
	private String proposalType;
	private String projectuid;
	private String archiveuid;
	private String creationDate;
	private String priorityFlag;
	private String consensusReport1;
	private String projectState;

	transient private ObsProposalAuthor piAuthor = null;
	transient private List<ObsProposalAuthor> coIAuthors = new ArrayList<ObsProposalAuthor>();
	transient private List<ObsProposalAuthor> coPIAuthors = new ArrayList<ObsProposalAuthor>();
	transient private List<Account> delegeesPhase2 = new ArrayList<Account>();
	transient private List<Account> delegeesData = new ArrayList<Account>();
	transient private List<Account> delegeesTOO = new ArrayList<Account>();
	transient private String descoped = "";
	transient private boolean dapr = false;

	public String getAbstractText() {
		return abstractText;
	}

	public String getAssociatedexec() {
		return associatedexec;
	}

	public String getCode() {
		return code;
	}

	public String getCycle() {
		return cycle;
	}

	public String getProjectuid() {
		return projectuid;
	}

	public void setProjectuid(String projectuid) {
		this.projectuid = projectuid;
	}

	public String getProposalType() {
		return proposalType;
	}

	public String getScientificCategory() {
		return scientificCategory != null && !scientificCategory.isEmpty() ? scientificCategory
				.substring(0, 1) : "";
	}

	public String getScientificCategoryString(){
		return scientificCategoryString;
	}
	
	public String getTitle() {
		return title;
	}

	// Authors handling
	public ObsProposalAuthor getPiAuthor() {
		return piAuthor;
	}

	public void setPiAuthor(ObsProposalAuthor piAuthor) {
		this.piAuthor = piAuthor;
	}

	public List<ObsProposalAuthor> getCoIAuthors() {
		return coIAuthors;
	}

	public void setCoIAuthors(List<ObsProposalAuthor> coIAuthors) {
		this.coIAuthors = coIAuthors;
	}

	public List<ObsProposalAuthor> getCoPIAuthors() {
		return coPIAuthors;
	}

	public void setCoPIAuthors(List<ObsProposalAuthor> coPIAuthors) {
		this.coPIAuthors = coPIAuthors;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public String getPriorityFlag() {
		return priorityFlag;
	}

	public String getArchiveuid() {
		return archiveuid;
	}

	public String getDescoped() {
		return descoped;
	}

	public void setDescoped(String descoped) {
		this.descoped = descoped;
	}

	public List<Account> getDelegeesPhase2() {
		return delegeesPhase2;
	}

	public void setDelegeesPhase2(List<Account> delegeesPhase2) {
		this.delegeesPhase2 = delegeesPhase2;
	}

	public List<Account> getDelegeesData() {
		return delegeesData;
	}

	public void setDelegeesData(List<Account> delegeesData) {
		this.delegeesData = delegeesData;
	}

	public List<Account> getDelegeesTOO() {
		return delegeesTOO;
	}

	public void setDelegeesTOO(List<Account> delegeesTOO) {
		this.delegeesTOO = delegeesTOO;
	}


	public String getConsensusReport() {

		// only return the consensus report if the project has been exported
		// from the ph1m
		// i.e. it is not phase1submitted.
		if (projectState != null
				&& !projectState.equalsIgnoreCase("Phase1Submitted")) {
			return (consensusReport1!=null)?consensusReport1:"";
		} else {
			return "";
		}

	}

	public String getProjectState() {
		return projectState;
	}

	public boolean isDapr() {
		return dapr;
	}

	public void setDapr(boolean dapr) {
		this.dapr = dapr;
	}

	@Override
	public int hashCode() {
		return Objects.hash(abstractText, archiveuid, associatedexec, code, consensusReport1, creationDate, cycle,
				priorityFlag, projectState, projectuid, proposalType, scientificCategory, scientificCategoryString,
				title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObsProposal other = (ObsProposal) obj;
		return Objects.equals(abstractText, other.abstractText) && Objects.equals(archiveuid, other.archiveuid)
				&& Objects.equals(associatedexec, other.associatedexec) && Objects.equals(code, other.code)
				&& Objects.equals(consensusReport1, other.consensusReport1)
				&& Objects.equals(creationDate, other.creationDate) && Objects.equals(cycle, other.cycle)
				&& Objects.equals(priorityFlag, other.priorityFlag) && Objects.equals(projectState, other.projectState)
				&& Objects.equals(projectuid, other.projectuid) && Objects.equals(proposalType, other.proposalType)
				&& Objects.equals(scientificCategory, other.scientificCategory)
				&& Objects.equals(scientificCategoryString, other.scientificCategoryString)
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "ObsProposal [code=" + code + ", title=" + title + ", associatedexec=" + associatedexec
				+ ", scientificCategory=" + scientificCategory + ", scientificCategoryString="
				+ scientificCategoryString + ", abstractText=" + abstractText + ", cycle=" + cycle + ", proposalType="
				+ proposalType + ", projectuid=" + projectuid + ", archiveuid=" + archiveuid + ", creationDate="
				+ creationDate + ", priorityFlag=" + priorityFlag + ", consensusReport1=" + consensusReport1
				+ ", projectState=" + projectState + "]";
	}

}
