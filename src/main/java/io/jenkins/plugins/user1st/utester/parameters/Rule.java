package io.jenkins.plugins.user1st.utester.parameters;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rule implements Serializable {

	private static final long serialVersionUID = -3993194159425385355L;

	@JsonProperty("type")
	private int type;

	@JsonProperty("selector")
	private String selector;

	@JsonProperty("errorCodes")
	private String[] errorCodes;

	@JsonProperty("priority")
	private int priority;

	@JsonProperty("labels")
	private String[] labels;

	public Rule() {
	}

	@JsonCreator
	public Rule(@JsonProperty("type") int type, @JsonProperty("selector") String selector,
			@JsonProperty("errorCodes") String[] errorCodes, @JsonProperty("priority") int priority,
			@JsonProperty("labels") String[] labels) {
		super();
		this.type = type;
		this.selector = selector;
		this.errorCodes = errorCodes;
		this.priority = priority;
		this.labels = labels;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String[] getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(String[] errorCodes) {
		this.errorCodes = errorCodes;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Rule [type=" + type + ", selector=" + selector + ", errorCodes=" + Arrays.toString(errorCodes)
				+ ", priority=" + priority + ", labels=" + Arrays.toString(labels) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(errorCodes);
		result = prime * result + Arrays.hashCode(labels);
		result = prime * result + priority;
		result = prime * result + ((selector == null) ? 0 : selector.hashCode());
		result = prime * result + type;
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
		Rule other = (Rule) obj;
		if (!Arrays.equals(errorCodes, other.errorCodes))
			return false;
		if (!Arrays.equals(labels, other.labels))
			return false;
		if (priority != other.priority)
			return false;
		if (selector == null) {
			if (other.selector != null)
				return false;
		} else if (!selector.equals(other.selector))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
