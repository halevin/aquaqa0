package alma.obops.aqua.qa0.domain;

/*******************************************************************************
 * ALMA - Atacama Large Millimeter Array
 * Copyright (c) ESO - European Southern Observatory, 2013
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper class to represent a node of the ObsProject tree.
 *
 * @author amchavan, Oct 16, 2013
 */

public class ObsProjectTreeNode {

	private String className;
	private String archiveUID;
	private String description;
	private String parentUID;
	private String state = "";
	private String flag = "";

// KEEP THIS JUST IN CASE -- we don't compute completion percentages right now	
//	private int progressPercent = -1;
	
	private List<ObsProjectTreeNode> children;
	private int qa0PassCount;
	private int expectedExecutions;
	// Bundle for any additional entity properties
	private Map<String,Object> properties;
	
	public ObsProjectTreeNode( String className ) {
		this.className = className;
		this.children = new ArrayList<ObsProjectTreeNode>();
		this.properties = new HashMap<String, Object>();
	}

	public void addChild( ObsProjectTreeNode child ) { 
		children.add(child);
	}

	public void setProperty( String key, String value ) { 
		properties.put(key, value);
	}
	
	public String getArchiveUID() {
		return archiveUID;
	}
	
	public List<ObsProjectTreeNode> getChildren() {
		return children;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}

	public String getClassName() {
		return className;
	}

	public String getDescription() {
		return description;
	}

	public String getParentUID() {
		return parentUID;
	}

// KEEP THIS JUST IN CASE -- we don't compute completion percentages right now
//	public int getProgressPercent() {
//		return progressPercent;
//	}

	public int getQa0PassCount() {
		return this.qa0PassCount;
	}

	public String getState() {
		return state;
	}

	public void setArchiveUID(String archiveUID) {
		this.archiveUID = archiveUID;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setParentUID(String parentUID) {
		this.parentUID = parentUID;
	}

// KEEP THIS JUST IN CASE -- we don't compute completion percentages right now
//	public void setProgressPercent(int percentProgress) {
//		this.progressPercent = percentProgress;
//	}

	public void setQa0PassCount(int qa0PassCount) {
		this.qa0PassCount = qa0PassCount;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public int getExpectedExecutions() {
		return expectedExecutions;
	}

	public void setExpectedExecutions(int expectedExecutions) {
		this.expectedExecutions = expectedExecutions;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "ObsProjectTreeNode [className=" + className + ", archiveUID="
				+ archiveUID + ", description=" + description + ", parentUID="
				+ parentUID + ", state=" + state + ", children=" + children
				+ ", qa0PassCount=" + qa0PassCount + ", expectedExecutions="
				+expectedExecutions+"]";
	}
}
