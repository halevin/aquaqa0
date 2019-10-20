package alma.obops.aqua.qa0.domain;

import java.util.List;
import java.util.Map;

/**
 * Wrapper class to form json response with newsfeed information
 * 
 * @author achalevi
 *
 */

public class News {
	private final List<StateChanges> stateChanges;
	private final List<Map<String, Object>> executions;
	
	public News() {
		stateChanges = null;
		executions = null;
	}
	
	public News(List<StateChanges> stateChanges, List<Map<String, Object>> executions) {
		this.stateChanges = stateChanges;
		this.executions = executions;
	}
	
	public List<StateChanges> getStateChanges() {
		return stateChanges;
	}

	public List<Map<String, Object>> getExecutions() {
		return executions;
	}
}
