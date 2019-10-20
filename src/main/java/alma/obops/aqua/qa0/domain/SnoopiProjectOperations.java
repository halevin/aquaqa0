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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table( name="PRJ_OPERATIONS" )
public class SnoopiProjectOperations implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4456021356742926912L;

	@Id
	@Column(name="OBS_PROJECT_ARCHIVE_UID")
	private String obsProjectArchiveId;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "CONTACT_ACCOUNT_ID")
	private Account account;

	public String getObsProjectArchiveId() {
		return obsProjectArchiveId;
	}

	public void setObsProjectArchiveId(String obsProjectArchiveId) {
		this.obsProjectArchiveId = obsProjectArchiveId;
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "SnoopiProjectOperations [obsProjectArchiveId=" + obsProjectArchiveId + "]";
	}
}