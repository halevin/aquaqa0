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
@Table(name="XML_SCHEDBLOCK_ENTITIES")
public class SchedBlockEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2233576152786428650L;

	@Id
	@Column(name="archive_uid")
	private String archiveUid;

	private String owner;
	
	// Solution taken from http://stackoverflow.com/questions/11578697/using-oracle-xmltype-column-in-hibernate/18282094#18282094
//	@ColumnTransformer(read = "NVL2(xml, (xml).getClobVal(), NULL)", write = "NULLSAFE_XMLTYPE(?)")
//	@Lob
	@Column(name="xml")
	private String xml;

	public String getArchiveUid() {
		return archiveUid;
	}

	public String getXml() {
		return xml;
	}

	public String getOwner() {
		return owner;
	}	
	
	
}