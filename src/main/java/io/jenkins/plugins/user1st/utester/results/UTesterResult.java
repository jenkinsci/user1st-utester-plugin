package io.jenkins.plugins.user1st.utester.results;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
	@JsonSubTypes.Type(value = PageCountResult.class, name = "PageCountResult"),
	@JsonSubTypes.Type(value = StatusResult.class, name = "StatusResult"),
	@JsonSubTypes.Type(value = UrlListResult.class, name = "UrlListResult")
})
public abstract class UTesterResult implements Serializable {
	
	private static final long serialVersionUID = 6181189394634258164L;
	
	@JsonProperty("statusCode")
	protected int statusCode;
	
	@JsonProperty("errorMessage")
	protected String errorMessage;
	
	protected UTesterResult() {}
	
	@JsonCreator
	protected UTesterResult(@JsonProperty("statusCode") int statusCode, @JsonProperty("errorMessage") String errorMessage) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "UTesterResult [statusCode=" + statusCode + ", errorMessage=" + errorMessage + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
		result = prime * result + statusCode;
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
		UTesterResult other = (UTesterResult) obj;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		if (statusCode != other.statusCode)
			return false;
		return true;
	}	
	
}
