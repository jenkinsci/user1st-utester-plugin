package io.jenkins.plugins.user1st.utester.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UrlListRequest extends UTesterRequest implements Serializable {

	private static final long serialVersionUID = 5644695393327226677L;

	public UrlListRequest() {
		super();
	}
	
	@JsonCreator
	public UrlListRequest(@JsonProperty("statusCode") int statusCode,@JsonProperty("errorMessage") String errorMessage,@JsonProperty("data") UrlListRequestData data) {
		super(statusCode, errorMessage, data);
	}

}
