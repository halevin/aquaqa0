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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table( name="PIV_EXECBLOCK" )
public class ExecBlock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7879185683470943568L;
	@Id
	private String execblockuid;
	private String schedblockuid;
	private String qa0status;
	@Temporal(TemporalType.TIMESTAMP)	
	private Date aquastarttime;
	@Temporal(TemporalType.TIMESTAMP)	
	private Date aquaendtime;
	@Temporal(TemporalType.TIMESTAMP)	
	private Date starttime;
	@Temporal(TemporalType.TIMESTAMP)	
	private Date endtime;
	private String obsprojectuid;
	private String obsprojectpi;
	private String representativefrequency;
	
	public Date getEndtime() {
		return (aquaendtime!=null)?aquaendtime:endtime;
	}
	
	public String getExecblockuid() {
		return execblockuid;
	}
	
	public String getQa0status() {
		return qa0status;
	}

	public String getSchedblockuid() {
		return schedblockuid;
	}

	public Date getStarttime() {
		return (aquastarttime!=null)?aquastarttime:starttime;
	}

	public String getObsprojectuid() {
		return obsprojectuid;
	}

	public String getObsprojectpi() {
		return obsprojectpi;
	}

	public String getRepresentativefrequency() {
		return representativefrequency;
	}

	public Date getAquastarttime() {
		return aquastarttime;
	}

	public void setAquastarttime(Date aquastarttime) {
		this.aquastarttime = aquastarttime;
	}

	public Date getAquaendtime() {
		return aquaendtime;
	}

	public void setAquaendtime(Date aquaendtime) {
		this.aquaendtime = aquaendtime;
	}

	public void setExecblockuid(String execblockuid) {
		this.execblockuid = execblockuid;
	}

	public void setSchedblockuid(String schedblockuid) {
		this.schedblockuid = schedblockuid;
	}

	public void setQa0status(String qa0status) {
		this.qa0status = qa0status;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}

	public void setObsprojectuid(String obsprojectuid) {
		this.obsprojectuid = obsprojectuid;
	}

	public void setObsprojectpi(String obsprojectpi) {
		this.obsprojectpi = obsprojectpi;
	}

	public void setRepresentativefrequency(String representativefrequency) {
		this.representativefrequency = representativefrequency;
	}

	@Override
	public int hashCode() {
		return Objects.hash(execblockuid, obsprojectpi, obsprojectuid, qa0status,
				representativefrequency, schedblockuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExecBlock other = (ExecBlock) obj;
		// compare dates with SimpleDateFormat because ExecBlock dates will be java.sql.Timestamp
		// instead of java.util.Date after they're retrieved from the DB
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (aquaendtime == null) {
			if (other.aquaendtime != null)
				return false;
		} else if (!format.format(aquaendtime).equals(format.format(other.aquaendtime)))
			return false;
		if (aquastarttime == null) {
			if (other.aquastarttime != null)
				return false;
		} else if (!format.format(aquastarttime).equals(format.format(other.aquastarttime)))
			return false;
		if (endtime == null) {
			if (other.endtime != null)
				return false;
		} else if (!format.format(endtime).equals(format.format(other.endtime)))
			return false;
		if (starttime == null) {
			if (other.starttime != null)
				return false;
		} else if (!format.format(starttime).equals(format.format(other.starttime)))
			return false;
		return Objects.equals(execblockuid, other.execblockuid)
				&& Objects.equals(obsprojectpi, other.obsprojectpi)
				&& Objects.equals(obsprojectuid, other.obsprojectuid) && Objects.equals(qa0status, other.qa0status)
				&& Objects.equals(representativefrequency, other.representativefrequency)
				&& Objects.equals(schedblockuid, other.schedblockuid);
	}

	@Override
	public String toString() {
		return "ExecBlock [execblockuid=" + execblockuid + 
				", schedblockuid=" + schedblockuid +
				", obsprojectuid=" + obsprojectuid + 
				", qa0status=" + qa0status + 
				", starttime="+ starttime + 
				", endtime=" + endtime + 
				", obsprojectpi=" + obsprojectpi +
				", representativefrequency=" + representativefrequency +"]";
	}
}