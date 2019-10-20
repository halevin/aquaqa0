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

@Entity
@Table(name="ACCOUNT_DELEGATION")
public class Delegation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1928054798976613729L;

	@Id
	@Column(name="id")
	private Long id;

	@ManyToOne
	@JoinColumn(name="pi_rh_id",  referencedColumnName = "request_handler_id")
	private Account delegate;

	@ManyToOne
	@JoinColumn(name="delegee_rh_id", referencedColumnName = "request_handler_id")
	private Account delegee;

	@Column(name = "project_code")
	private String projectCode;

	@Column(name = "delegation_type")
	private String delegationType;


	public Long getId() {
		return id;
	}

	public Account getDelegate() {
		return delegate;
	}

	public Account getDelegee() {
		return delegee;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public String getDelegationType() {
		return delegationType;
	}

	@Override
	public String toString() {
		return "Delegation{" +
				"id=" + id +
				", delegate=" + delegate +
				", delegee=" + delegee +
				", projectCode='" + projectCode + '\'' +
				", delegationType='" + delegationType + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Delegation that = (Delegation) o;

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;
		if (delegee != null ? !delegee.equals(that.delegee) : that.delegee != null) return false;
		if (projectCode != null ? !projectCode.equals(that.projectCode) : that.projectCode != null) return false;
		return delegationType != null ? delegationType.equals(that.delegationType) : that.delegationType == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (delegate != null ? delegate.hashCode() : 0);
		result = 31 * result + (delegee != null ? delegee.hashCode() : 0);
		result = 31 * result + (projectCode != null ? projectCode.hashCode() : 0);
		result = 31 * result + (delegationType != null ? delegationType.hashCode() : 0);
		return result;
	}
}