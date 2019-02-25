package io.jenkins.plugins.user1st.utester.results;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskResultElement implements Serializable {
	
	private static final long serialVersionUID = 5969789445967507009L;
	
	@JsonProperty("xpath")
	private String xpath;
	
	@JsonProperty("selector")
	private String selector;
	
	@JsonProperty("errors")
	private String[] errors;
	
	private String url;
	
	public TaskResultElement() {}
	
	@JsonCreator
	public TaskResultElement(@JsonProperty("xpath") String xpath,@JsonProperty("selector") String selector,@JsonProperty("errors") String[] errors) {
		this.xpath = xpath;
		this.selector = selector;
		this.errors = errors;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "TaskResultElement [xpath=" + xpath + ", selector=" + selector + ", errors=" + Arrays.toString(errors)
				+ ", url=" + url + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(errors);
		result = prime * result + ((selector == null) ? 0 : selector.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((xpath == null) ? 0 : xpath.hashCode());
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
		TaskResultElement other = (TaskResultElement) obj;
		if (!Arrays.equals(errors, other.errors))
			return false;
		if (selector == null) {
			if (other.selector != null)
				return false;
		} else if (!selector.equals(other.selector))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (xpath == null) {
			if (other.xpath != null)
				return false;
		} else if (!xpath.equals(other.xpath))
			return false;
		return true;
	}

}
