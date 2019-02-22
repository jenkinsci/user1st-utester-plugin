package io.jenkins.plugins.user1st.utester.requests;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusRequestData extends TaskRequestData implements Serializable {

	private static final long serialVersionUID = -6299304742213203952L;
	
	@JsonProperty("taskID")
	private String taskID;
	
	public StatusRequestData() {
		super();
	}

	@JsonCreator
	public StatusRequestData(@JsonProperty("timeStamp") Date timeStamp,@JsonProperty("taskID") String taskID) {
		super(timeStamp);
		this.taskID = taskID;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	@Override
	public String toString() {
		return "StatusRequestData [taskID=" + taskID + ", timeStamp=" + timeStamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((taskID == null) ? 0 : taskID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatusRequestData other = (StatusRequestData) obj;
		if (taskID == null) {
			if (other.taskID != null)
				return false;
		} else if (!taskID.equals(other.taskID))
			return false;
		return true;
	}
	
}
