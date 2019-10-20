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
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table( name="STATE_CHANGES" )
public class StateChangesSimple implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9153446557265671919L;
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
				"]";
	}
}