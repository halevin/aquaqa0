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

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="PIV_ACCOUNT")
public class Account implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 878772108739253615L;
	@Id
	@Column(name="account_id")
	private String accountId;
	@Column(name = "request_handler_id")
	private Long requestHandlerId;
	private String firstname;
	private String lastname;
	private String initials;
	private String preferredarc;
	private String email;
	private String executive;
	private String instno;

	@Transient
	private boolean contactScientist;

	public String getAccountId() {
		return accountId;
	}
	public String getFirstname() {
		return firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public String getInitials() {
		return initials;
	}
	public String getPreferredarc() {
		return preferredarc;
	}
	public String getEmail() {
		return email;
	}
	public String getExecutive() {
		return executive;
	}
	public String getInstno() {
		return instno;
	}
	public Long getRequestHandlerId() { return requestHandlerId; }
	public boolean isContactScientist() { return contactScientist; }
	public void setContactScientist(boolean contactScientist) {	this.contactScientist = contactScientist; }
	
	public void setInstno(String instno) {
		this.instno = instno;
	}
	
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, email, executive, firstname, initials, instno, lastname, preferredarc,
				requestHandlerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		return Objects.equals(accountId, other.accountId) && Objects.equals(email, other.email)
				&& Objects.equals(executive, other.executive) && Objects.equals(firstname, other.firstname)
				&& Objects.equals(initials, other.initials) && Objects.equals(instno, other.instno)
				&& Objects.equals(lastname, other.lastname) && Objects.equals(preferredarc, other.preferredarc)
				&& Objects.equals(requestHandlerId, other.requestHandlerId);
	}

	@Override
	public String toString() {
		return "Account{" +
				"accountId='" + accountId + '\'' +
				", requestHandleId=" + requestHandlerId +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", initials='" + initials + '\'' +
				", preferredarc='" + preferredarc + '\'' +
				", email='" + email + '\'' +
				", executive='" + executive + '\'' +
				", instno='" + instno + '\'' +
				'}';
	}
}