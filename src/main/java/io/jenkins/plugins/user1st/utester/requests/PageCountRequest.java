package io.jenkins.plugins.user1st.utester.requests;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PageCountRequest extends UTesterRequest implements Serializable {

	private static final long serialVersionUID = 5644695393327226677L;

	public PageCountRequest() {
		super();
	}
	
	@JsonCreator
	public PageCountRequest(@JsonProperty("statusCode") int statusCode,@JsonProperty("errorMessage") String errorMessage,@JsonProperty("data") PageCountRequestData data) {
		super(statusCode, errorMessage, data);
	}

}
