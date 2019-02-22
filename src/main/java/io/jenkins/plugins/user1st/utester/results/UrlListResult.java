package io.jenkins.plugins.user1st.utester.results;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlListResult extends UTesterResult implements Serializable {

	private static final long serialVersionUID = 5644695393327226677L;
	
	private UrlListResultData[] data;

	public UrlListResult() {
		super();
	}
	
	@JsonCreator
	public UrlListResult(@JsonProperty("statusCode") int statusCode,@JsonProperty("errorMessage") String errorMessage,@JsonProperty("data") UrlListResultData[] data) {
		super(statusCode, errorMessage);
		this.data = data;
	}

	public UrlListResultData[] getData() {
		return data;
	}

	public void setData(UrlListResultData[] data) {
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
		return "UrlListResult [data=" + Arrays.toString(data) + ", statusCode=" + statusCode + ", errorMessage="
				+ errorMessage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(data);
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
		UrlListResult other = (UrlListResult) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

}
