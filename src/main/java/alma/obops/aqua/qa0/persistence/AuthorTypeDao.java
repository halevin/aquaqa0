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

package alma.obops.aqua.qa0.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import alma.obops.aqua.qa0.domain.SnoopiAuthorType;

@RepositoryRestResource(exported=false)
public interface AuthorTypeDao extends CrudRepository<SnoopiAuthorType, String> {
	
	List<SnoopiAuthorType> findByUserid(String userid);

	@Query("SELECT e.projectArchiveUid FROM alma.obops.aqua.qa0.domain.SnoopiAuthorType e " +
			"WHERE e.userid = :userid AND " +
			"e.authorType IN (:authorTypes)")
	List<String> findByUseridAndAuthorType(@Param("userid") String userid, @Param("authorTypes") List<String> authorTypes);

	List<SnoopiAuthorType> findByUseridAndProjectArchiveUid(String userid, String projectuid);
}