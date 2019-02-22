package io.jenkins.plugins.user1st.utester.results;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResult extends UTesterResult implements Serializable {
	
	private static final long serialVersionUID = 6028306998320728769L;
	
	private StatusResultData data;

	public StatusResult() {
		super();
	}

	@JsonCreator
	public StatusResult(@JsonProperty("statusCode") int statusCode,@JsonProperty("errorMessage") String errorMessage,@JsonProperty("data") StatusResultData data) {
		super(statusCode, errorMessage);
		this.data = data;
	}

	public StatusResultData getData() {
		return data;
	}

	public void setData(StatusResultData data) {
		this.data = data;
	}

	@Override
	public int getStatusCode() {
		return super.getStatusCode();
	}

	@Override
	public void setStatusCode(int statusCode) {
		super.setStatusCode(statusCode);
	}

	@Override
	public String getErrorMessage() {
		return super.getErrorMessage();
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}

	@Override
	public String toString() {
		return "StatusResult [data=" + data + ", statusCode=" + statusCode + ", errorMessage=" + errorMessage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		StatusResult other = (StatusResult) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		return true;
	}
	
}
