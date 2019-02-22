package io.jenkins.plugins.user1st.utester.results;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
	@JsonSubTypes.Type(value = StatusResultData.class, name = "StatusResultData"),
	@JsonSubTypes.Type(value = PageCountResultData.class, name = "PageCountResultData")
})
public abstract class TaskResultData implements Serializable {
	
	private static final long serialVersionUID = -4227314570563262317L;
	
	@JsonProperty("timeStamp")
	protected Date timeStamp;
	
	protected TaskResultData() {}

	@JsonCreator
	protected TaskResultData(@JsonProperty("timeStamp") Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "TaskResultData [timeStamp=" + timeStamp + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
		TaskResultData other = (TaskResultData) obj;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	
	
}
