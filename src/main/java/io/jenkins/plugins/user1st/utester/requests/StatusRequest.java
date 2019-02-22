package io.jenkins.plugins.user1st.utester.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusRequest extends UTesterRequest implements Serializable {
	
	private static final long serialVersionUID = 6028306998320728769L;

	public StatusRequest() {
		super();
	}

	@JsonCreator
	public StatusRequest(@JsonProperty("statusCode") int statusCode,@JsonProperty("errorMessage") String errorMessage,@JsonProperty("data") StatusRequestData data) {
		super(statusCode, errorMessage, data);
	}
	
}