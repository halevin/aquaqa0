/*
 * ALMA - Atacama Large Millimiter Array (c) European Southern Observatory, 2014
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package alma.obops.aqua.qa0.domain;

import java.io.Serializable;

/**
 * A class representing the amount of time an ExecutionBlock was executed following
 * a given scan intent.
 *
 * @author rtobar, May 13, 2014
 */
public class IntentExecutionTime implements Comparable<IntentExecutionTime>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2672495184545376980L;
	private String intent;
	private String source;
	private String ra;
	private String dec;
	private long executionTime; // in [ms]

	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRa() {
		return ra;
	}
	public void setRa(String ra) {
		this.ra = ra;
	}
	public String getDec() {
		return dec;
	}
	public void setDec(String dec) {
		this.dec = dec;
	}
	public long getExecutionTime() {
		return executionTime;
	}
	public void setExecutionTime(long executionTime) {
		this.executionTime = executionTime;
	}
	@Override
	public int compareTo(IntentExecutionTime o2) {

		String intent1 = getIntent();
		String intent2 = o2.getIntent();

		// nulls go first
		if( intent1 == null && intent2 == null ) {
			return 0;
		}
		if( intent1 == null && intent2 != null ) {
			return -1;
		}
		if( intent1 != null && intent2 == null ) {
			return 1;
		}

		// OBSERVE_TARGET goes always first
		if( "OBSERVE_TARGET".equals(intent1) ) {
			return -1;
		}
		if( "OBSERVE_TARGET".equals(intent2) ) {
			return 1;
		}

		return intent1.compareTo(intent2);
	}

}