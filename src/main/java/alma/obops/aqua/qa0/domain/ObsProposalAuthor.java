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
@Table(name="piv_proposal_authors")
public class ObsProposalAuthor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4581659161627365994L;
	@Id
	private String accountId;
	private String firstname;
	private String lastname;
	private String initials;
	private String sequence;
	private String projectUid;
	private String authorType;
	
	public String getAccountId() {
		return accountId;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getInitials() {
		return initials;
	}
	public String getLastname() {
		return lastname;
	}
	public String getProjectUid() {
		return projectUid;
	}
	public String getSequence() {
		return sequence;
	}
	public String getAuthorType() {	return authorType; }
}