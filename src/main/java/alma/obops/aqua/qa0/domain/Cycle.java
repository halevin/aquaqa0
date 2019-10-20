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
import java.sql.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="PH1M_CYCLE")
public class Cycle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 245182158387023609L;
	@Id
	@Column(name="ID")
	private String id;
	private String code;
	private String dapr;
	@Column(name="START_DATE")
	private Date startDate;
	@Column(name="END_DATE")
	private Date endDate;

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	@Temporal(TemporalType.DATE)
	public Date getStartDate() {
		return startDate;
	}
	
	@Temporal(TemporalType.DATE)
	public Date getEndDate() {
		return endDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getDapr() {
		return dapr;
	}

	public void setDapr(String dapr) {
		this.dapr = dapr;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cycle cycle = (Cycle) o;
		return Objects.equals(id, cycle.id) &&
				Objects.equals(code, cycle.code) &&
				Objects.equals(dapr, cycle.dapr) &&
				Objects.equals(startDate, cycle.startDate) &&
				Objects.equals(endDate, cycle.endDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, code, dapr, startDate, endDate);
	}

	@Override
	public String toString() {
		return "Cycle{" +
				"id='" + id + '\'' +
				", code='" + code + '\'' +
				", dapr='" + dapr + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				'}';
	}
}
