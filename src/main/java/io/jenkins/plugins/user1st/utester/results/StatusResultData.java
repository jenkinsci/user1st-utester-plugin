package io.jenkins.plugins.user1st.utester.results;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResultData extends TaskResultData implements Serializable {

	private static final long serialVersionUID = -6299304742213203952L;

	@JsonProperty("taskStatus")
	private int taskStatus;
	
	@JsonProperty("taskID")
	private String taskID;
	
	@JsonProperty("pageCount")
	private int pageCount;
	
	public StatusResultData() {
		super();
	}

	@JsonCreator
	public StatusResultData(@JsonProperty("timeStamp") Date timeStamp,@JsonProperty("taskStatus") int taskStatus,@JsonProperty("taskID") String taskID,@JsonProperty("pageCount") int pageCount) {
		super(timeStamp);
		this.taskStatus = taskStatus;
		this.taskID = taskID;
		this.pageCount = pageCount;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public String toString() {
		return "StatusResultData [taskStatus=" + taskStatus + ", taskID=" + taskID + ", pageCount=" + pageCount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + pageCount;
		result = prime * result + ((taskID == null) ? 0 : taskID.hashCode());
		result = prime * result + taskStatus;
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
		StatusResultData other = (StatusResultData) obj;
		if (pageCount != other.pageCount)
			return false;
		if (taskID == null) {
			if (other.taskID != null)
				return false;
		} else if (!taskID.equals(other.taskID))
			return false;
		if (taskStatus != other.taskStatus)
			return false;
		return true;
	}
	
}
