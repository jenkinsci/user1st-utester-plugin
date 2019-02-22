package io.jenkins.plugins.user1st.utester.util;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ApiClient {
	
	private String apiUrl;
	
	private String apiToken;
	
	private final String statusEndpoint = "/api/TaskStatus";
	private final String resultsEndpoint = "/api/TaskResults";
	
	
	public ApiClient(String apiUrl,String apiToken) {
		this.apiUrl = apiUrl;
		this.apiToken = apiToken;
		
		Unirest.setObjectMapper(new ObjectMapper() {
		    private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
		                = new com.fasterxml.jackson.databind.ObjectMapper();
		    
		    public <T> T readValue(String value, Class<T> valueType) {
		        try {
		            return jacksonObjectMapper.readValue(value, valueType);
		        } catch (IOException e) {
		            throw new RuntimeException(e);
		        }
		    }

		    public String writeValue(Object value) {
		        try {
		            return jacksonObjectMapper.writeValueAsString(value);
		        } catch (JsonProcessingException e) {
		            throw new RuntimeException(e);
		        }
		    }
		});
	}
	
	public HttpResponse<JsonNode> post(String apiEndpoint, Map<String, Object> requestBody){
		
		HttpResponse<JsonNode> result = null;
		
		try {
			result = Unirest.post(this.apiUrl + apiEndpoint)
					.header("accept", "application/json")
					.header("Authorization", "Bearer " + apiToken)
					.header("Content-Type", "application/json")
					.body(requestBody)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
				
		return result;
	}
	
	public HttpResponse<JsonNode> getTaskStatus(String taskID){
		
		HttpResponse<JsonNode> result = null;
		
		try {
			result = Unirest.get(this.apiUrl + this.statusEndpoint)
					.header("accept", "application/json")
					.header("Authorization", "Bearer " + apiToken)
					.queryString("taskID", taskID)
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public HttpResponse<String> getTaskStatusString(String taskID){
		
		HttpResponse<String> result = null;
		
		try {
			result = Unirest.get(this.apiUrl + this.statusEndpoint)
					.header("accept", "application/json")
					.header("Authorization", "Bearer " + apiToken)
					.queryString("taskID", taskID)
					.asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public HttpResponse<JsonNode> getTaskResults(String taskID){
		
		HttpResponse<JsonNode> result = null;
		
		try {
			result = Unirest.get(this.apiUrl + this.resultsEndpoint)
					.header("accept", "application/json")
					.header("Authorization", "Bearer " + apiToken)
					.queryString("taskID", taskID)
					.queryString("batchSize", "")
					.queryString("pageIndex", "")
					.asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public HttpResponse<String> getTaskResultsString(String taskID){
		
		HttpResponse<String> result = null;
		
		try {
			result = Unirest.get(this.apiUrl + this.resultsEndpoint)
					.header("accept", "application/json")
					.header("Authorization", "Bearer " + apiToken)
					.queryString("taskID", taskID)
					.queryString("batchSize", "")
					.queryString("pageIndex", "")
					.asString();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
