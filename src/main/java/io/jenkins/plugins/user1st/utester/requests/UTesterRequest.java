package io.jenkins.plugins.user1st.utester.requests;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
	@JsonSubTypes.Type(value = PageCountRequest.class, name = "PageCountRequest"),
	@JsonSubTypes.Type(value = StatusRequest.class, name = "StatusRequest"),
	@JsonSubTypes.Type(value = UrlListRequest.class, name = "UrlListRequest")
})
public abstract class UTesterRequest implements Serializable {
	
	private static final long serialVersionUID = 6181189394634258164L;
	
	@JsonProperty("statusCode")
	protected int statusCode;
	
	@JsonProperty("errorMessage")
	protected String errorMessage;
	
	@JsonProperty("data")
	@JsonSubTypes({
		@JsonSubTypes.Type(value = PageCountRequestData.class, name = "PageCountRequestData"),
		@JsonSubTypes.Type(value = StatusRequestData.class, name = "StatusRequestData")
	})
	protected TaskRequestData data;
	
	protected UTesterRequest() {}
	
	@JsonCreator
	protected UTesterRequest(@JsonProperty("statusCode") int statusCode, @JsonProperty("errorMessage") String errorMessage, @JsonProperty("data") TaskRequestData data) {
		this.statusCode = statusCode;
		this.errorMessage = errorMessage;
		this.data = data;
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

	public TaskRequestData getData() {
		return data;
	}

	public void setData(TaskRequestData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "UTesterRequest [statusCode=" + statusCode + ", errorMessage=" + errorMessage + ", data=" + data + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
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
		UTesterRequest other = (UTesterRequest) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
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
