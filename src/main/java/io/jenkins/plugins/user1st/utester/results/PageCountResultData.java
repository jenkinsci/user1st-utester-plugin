package io.jenkins.plugins.user1st.utester.results;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PageCountResultData extends TaskResultData implements Serializable {
	
	private static final long serialVersionUID = -3694955637114044923L;

	@JsonProperty("status")
	private int status;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("captureName")
	private String captureName;
	
	@JsonProperty("pageTitle")
	private String pageTitle;
	
	@JsonProperty("pageErrors")
	private String[] pageErrors;
	
	@JsonProperty("elements")
	private TaskResultElement[] elements;
	
	@JsonProperty("elementsCount")
	private int elementsCount;
	
	@JsonProperty("elementsHighPriorityCount")
	private int elementsHighPriorityCount;
	
	@JsonProperty("elementsMediumPriorityCount")
	private int elementsMediumPriorityCount;
	
	@JsonProperty("elementsLowPriorityCount")
	private int elementsLowPriorityCount;
	
	@JsonProperty("compliance")
	private float compliance;
	
	@JsonProperty("index")
	private int index;
	
	public PageCountResultData() {
		super();
	}

	@JsonCreator
	public PageCountResultData(@JsonProperty("status") int status,@JsonProperty("timeStamp") Date timeStamp,@JsonProperty("url") String url, 
			@JsonProperty("captureName") String captureName,@JsonProperty("pageTitle") String pageTitle,
			@JsonProperty("pageErrors") String[] pageErrors,@JsonProperty("elements") TaskResultElement[] elements,
			@JsonProperty("elementsCount") int elementsCount,@JsonProperty("elementsHighPriorityCount") int elementsHighPriorityCount,
			@JsonProperty("elementsMediumPriorityCount") int elementsMediumPriorityCount,@JsonProperty("elementsLowPriorityCount") int elementsLowPriorityCount, 
			@JsonProperty("compliance") float compliance,@JsonProperty("index") int index) {
		super(timeStamp);
		this.status = status;
		this.url = url;
		this.captureName = captureName;
		this.pageTitle = pageTitle;
		this.pageErrors = pageErrors;
		this.elements = elements;
		this.elementsCount = elementsCount;
		this.elementsHighPriorityCount = elementsHighPriorityCount;
		this.elementsMediumPriorityCount = elementsMediumPriorityCount;
		this.elementsLowPriorityCount = elementsLowPriorityCount;
		this.compliance = compliance;
		this.index = index;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCaptureName() {
		return captureName;
	}

	public void setCaptureName(String captureName) {
		this.captureName = captureName;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String[] getPageErrors() {
		return pageErrors;
	}

	public void setPageErrors(String[] pageErrors) {
		this.pageErrors = pageErrors;
	}

	public TaskResultElement[] getElements() {
		return elements;
	}

	public void setElements(TaskResultElement[] elements) {
		this.elements = elements;
	}

	public int getElementsCount() {
		return elementsCount;
	}

	public void setElementsCount(int elementsCount) {
		this.elementsCount = elementsCount;
	}

	public int getElementsHighPriorityCount() {
		return elementsHighPriorityCount;
	}

	public void setElementsHighPriorityCount(int elementsHighPriorityCount) {
		this.elementsHighPriorityCount = elementsHighPriorityCount;
	}

	public int getElementsMediumPriorityCount() {
		return elementsMediumPriorityCount;
	}

	public void setElementsMediumPriorityCount(int elementsMediumPriorityCount) {
		this.elementsMediumPriorityCount = elementsMediumPriorityCount;
	}

	public int getElementsLowPriorityCount() {
		return elementsLowPriorityCount;
	}

	public void setElementsLowPriorityCount(int elementsLowPriorityCount) {
		this.elementsLowPriorityCount = elementsLowPriorityCount;
	}

	public float getCompliance() {
		return compliance;
	}

	public void setCompliance(float compliance) {
		this.compliance = compliance;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "PageCountResultData [status=" + status + ", url=" + url + ", captureName=" + captureName
				+ ", pageTitle=" + pageTitle + ", pageErrors=" + Arrays.toString(pageErrors) + ", elements="
				+ Arrays.toString(elements) + ", elementsCount=" + elementsCount + ", elementsHighPriorityCount="
				+ elementsHighPriorityCount + ", elementsMediumPriorityCount=" + elementsMediumPriorityCount
				+ ", elementsLowPriorityCount=" + elementsLowPriorityCount + ", compliance=" + compliance + ", index="
				+ index + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((captureName == null) ? 0 : captureName.hashCode());
		result = prime * result + Float.floatToIntBits(compliance);
		result = prime * result + Arrays.hashCode(elements);
		result = prime * result + elementsCount;
		result = prime * result + elementsHighPriorityCount;
		result = prime * result + elementsLowPriorityCount;
		result = prime * result + elementsMediumPriorityCount;
		result = prime * result + index;
		result = prime * result + Arrays.hashCode(pageErrors);
		result = prime * result + ((pageTitle == null) ? 0 : pageTitle.hashCode());
		result = prime * result + status;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		PageCountResultData other = (PageCountResultData) obj;
		if (captureName == null) {
			if (other.captureName != null)
				return false;
		} else if (!captureName.equals(other.captureName))
			return false;
		if (Float.floatToIntBits(compliance) != Float.floatToIntBits(other.compliance))
			return false;
		if (!Arrays.equals(elements, other.elements))
			return false;
		if (elementsCount != other.elementsCount)
			return false;
		if (elementsHighPriorityCount != other.elementsHighPriorityCount)
			return false;
		if (elementsLowPriorityCount != other.elementsLowPriorityCount)
			return false;
		if (elementsMediumPriorityCount != other.elementsMediumPriorityCount)
			return false;
		if (index != other.index)
			return false;
		if (!Arrays.equals(pageErrors, other.pageErrors))
			return false;
		if (pageTitle == null) {
			if (other.pageTitle != null)
				return false;
		} else if (!pageTitle.equals(other.pageTitle))
			return false;
		if (status != other.status)
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

}
