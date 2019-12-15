/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2018
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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import alma.obops.boot.security.User;


@Entity
@Table(name="drm_data_reducer")
public class DrmDataReducer implements Serializable{

    private static final long serialVersionUID = -5310788150751934764L;

    private String userId;
    private User user;
    private DrmArcNode arcNode;
    private List<String> qualifications;
    private List<Availability> availabilityList;
    private int load;

    public DrmDataReducer(){
    }

    @Id
    @Column(name="DR_USER_ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="DR_USER_ID")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="NODE_ID")
    public DrmArcNode getArcNode() {
        return arcNode;
    }

    public void setArcNode(DrmArcNode arcNode) {
        this.arcNode = arcNode;
    }

    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
    @CollectionTable(name="drm_dr_qualification", joinColumns=@JoinColumn(name="DR_USER_ID"))
    @Column(name="qualification")
    public List<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name="DR_USER_ID", referencedColumnName = "DR_USER_ID")
    public List<Availability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailabilityList(List<Availability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    @Transient
    public int getLoad() {
        return load;
    }

    public void setLoad(int load) {
        this.load = load;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUser() == null) ? 0 : getUser().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DrmDataReducer other = (DrmDataReducer) obj;
        if (getUser() == null) {
            if (other.getUser() != null)
                return false;
        } else if (!getUser().equals(other.getUser()))
            return false;
        return true;
    }

    public String toString() {

        return getUser().getFirstname() + " " + getUser().getLastname()
                + (getArcNode() != null ? " (" + getArcNode().toString() + ")" : "");
    }
}
