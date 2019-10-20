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
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="bmmv_obsproposal_authors")
public class SnoopiAuthorType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4046591513797504211L;
	private Long id;
	private String userid;
	private String projectArchiveUid;
	private String authorType;
	
	
	@Id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="userid")
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name="project_archive_uid")
	public String getProjectArchiveUid() {
		return projectArchiveUid;
	}
	public void setProjectArchiveUid(String projectArchiveUid) {
		this.projectArchiveUid = projectArchiveUid;
	}

	@Column(name="authtype")
	public String getAuthorType() {
		return authorType;
	}
	public void setAuthorType(String authorType) {
		this.authorType = authorType;
	}

	@Override
	public String toString() {
		return "SnoopiAuthorType [userid=" + userid +
				", projectArchiveUid=" + projectArchiveUid +
				", authorType=" + authorType +"]";
	}
	
}