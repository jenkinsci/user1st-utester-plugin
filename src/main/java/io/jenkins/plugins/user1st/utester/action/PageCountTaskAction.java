package io.jenkins.plugins.user1st.utester.action;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialPointer;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.general.ValueDataset;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.HttpResponse;

import hudson.model.Action;
import hudson.model.Run;
import hudson.util.Graph;
import io.jenkins.plugins.user1st.utester.UTesterPlugin;
import io.jenkins.plugins.user1st.utester.results.PageCountResult;
import io.jenkins.plugins.user1st.utester.results.PageCountResultData;
import io.jenkins.plugins.user1st.utester.results.StatusResult;
import io.jenkins.plugins.user1st.utester.results.StatusResultData;
import io.jenkins.plugins.user1st.utester.results.TaskResultElement;
import io.jenkins.plugins.user1st.utester.util.ApiClient;
import jenkins.model.GlobalConfiguration;

public class PageCountTaskAction implements Action {

	private Run<?, ?> build;

	private String taskID;

	private PageCountResult results;

	public PageCountTaskAction(Run<?, ?> build) {
		this.build = build;
		this.taskID = "";
		this.results = new PageCountResult();
	}

	public PageCountTaskAction(Run<?, ?> build, String taskID) {
		this.build = build;
		this.taskID = taskID;
		this.results = new PageCountResult();
	}

	public PageCountTaskAction(Run<?, ?> build, String taskID, PageCountResult results) {
		this.build = build;
		this.taskID = taskID;
		this.results = results;
	}

	@Override
	public String getIconFileName() {
		return "/plugin/user1st-utester-jenkins/images/user1st-icon.png";
	}

	@Override
	public String getDisplayName() {
		return "uTester Page Count Task Results";
	}

	@Override
	public String getUrlName() {
		return "uTesterPageCountTask";
	}

	public int getBuildNumber() {
		return this.build.number;
	}

	public Run<?, ?> getBuild() {
		return build;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public PageCountResult getResults() {
		return results;
	}

	public void setResults(PageCountResult results) {
		this.results = results;
	}

	public boolean isTaskDone() {

		System.out.println("***checking for saved values***");

		int taskStatus = -1;

		if (this.results == null || (this.results.getData() != null && this.results.getStatusCode() != 200)) {

			System.out.println("***saved values not found***");

			ApiClient client = new ApiClient(GlobalConfiguration.all().get(UTesterPlugin.class).getBaseUrl(),
					GlobalConfiguration.all().get(UTesterPlugin.class).getJwtToken());

			HttpResponse<String> response = client.getTaskStatusString(taskID);

			try {

				ObjectMapper jacksonObjectMapper = new ObjectMapper();

				StatusResult tempResult = jacksonObjectMapper.readValue(response.getBody().toString(),
						StatusResult.class);

				taskStatus = ((StatusResultData) tempResult.getData()).getTaskStatus();

				return taskStatus == 2;

			} catch (org.json.JSONException e) {
				System.out.println(e.getMessage());
			} catch (JsonParseException e) {
				System.out.println(e.getMessage());
			} catch (JsonMappingException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("***saved values found***");
			return true;
		}

		return false;
	}

	private PageCountResult getTaskResults(String taskID) throws JsonParseException, JsonMappingException, IOException {
		PageCountResult results = null;

		ApiClient client = new ApiClient(GlobalConfiguration.all().get(UTesterPlugin.class).getBaseUrl(),
				GlobalConfiguration.all().get(UTesterPlugin.class).getJwtToken());

		HttpResponse<String> response = client.getTaskResultsString(taskID);

		ObjectMapper jacksonObjectMapper = new ObjectMapper();

		results = jacksonObjectMapper.readValue(response.getBody(), PageCountResult.class);

		return results;
	}

	private String getTaskResultsString() {

		String resultString = "";

		ObjectMapper mapper = new ObjectMapper();

		if (this.results == null || this.results.getData() == null || this.results.getStatusCode() != 200) {

			try {
				this.results = this.getTaskResults(taskID);
				resultString = mapper.writeValueAsString(this.results);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			try {
				resultString = mapper.writeValueAsString(this.results);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}

		return resultString;

	}
	
	public float getAverageElementCount() {
		
		float averageCount = 0F;
		
		if(this.results != null && this.results.getData() != null) {
			for(PageCountResultData pcrd: this.results.getData()) {
				averageCount += pcrd.getElementsCount();
			}
			
			averageCount = averageCount / this.results.getData().length;
		}
		
		return averageCount;
	}
	
	public JsonArray getResultElements() {
		
		JsonArray ele = null;
		
		ObjectMapper mapper = new ObjectMapper();

		JsonParser parser = new JsonParser();
		
		List<TaskResultElement> elements = new ArrayList<TaskResultElement>();
		
		if(this.results != null && this.results.getData() != null) {
			for(PageCountResultData pcrd: this.results.getData()) {
				List<TaskResultElement> tempElements = Arrays.asList(pcrd.getElements());
				for (TaskResultElement tre: tempElements) {
					tre.setUrl(pcrd.getUrl());
					elements.add(tre);
				}
				//elements.addAll(Arrays.asList(pcrd.getElements()));
			}
			
			try {
				ele = parser.parse(mapper.writeValueAsString(Arrays.stream(elements.toArray()).distinct().toArray())).getAsJsonArray();
			} catch (JsonSyntaxException | JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ele;
	}
	
	public JsonArray getPageErrors() {
		JsonArray ele = null;
		
		ObjectMapper mapper = new ObjectMapper();

		JsonParser parser = new JsonParser();
		
		List<String> pageErrors = new ArrayList<String>();
		
		if(this.results != null && this.results.getData() != null) {
			for(PageCountResultData pcrd: this.results.getData()) {
				pageErrors.addAll(Arrays.asList(pcrd.getPageErrors()));
			}
			
			try {
				ele = parser.parse(mapper.writeValueAsString(Arrays.stream(pageErrors.toArray()).distinct().toArray())).getAsJsonArray();
			} catch (JsonSyntaxException | JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ele;
	}
	
	public JsonArray getResultJSONOjbect() {
		
		JsonArray ele = null;
		
		ObjectMapper mapper = new ObjectMapper();

		JsonParser parser = new JsonParser();
		
		if(this.results != null && this.results.getData() != null) {
			try {
				ele = parser.parse(mapper.writeValueAsString(this.results.getData())).getAsJsonArray();
			} catch (JsonSyntaxException | JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ele;
	}

	public JsonObject getResultJSON() {

		JsonObject ele = null;

		JsonParser parser = new JsonParser();

		if (this.results == null || this.results.getData() == null || this.results.getStatusCode() != 200) {
			try {
				this.results = this.getTaskResults(taskID);
				ele = parser.parse(getTaskResultsString()).getAsJsonObject().get("data").getAsJsonArray().get(0)
						.getAsJsonObject();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ele = parser.parse(getTaskResultsString()).getAsJsonObject().get("data").getAsJsonArray().get(0)
					.getAsJsonObject();
		}

		return ele;
	}

	public JsonArray getResultsJSON() {

		JsonArray ele = null;

		JsonParser parser = new JsonParser();

		if (this.results == null || this.results.getData() == null || this.results.getStatusCode() != 200) {
			try {
				this.results = this.getTaskResults(taskID);
				ele = parser.parse(getTaskResultsString()).getAsJsonObject().get("data").getAsJsonArray().get(0)
						.getAsJsonObject().get("elements").getAsJsonArray();
				;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			ele = parser.parse(getTaskResultsString()).getAsJsonObject().get("data").getAsJsonArray().get(0)
					.getAsJsonObject().get("elements").getAsJsonArray();
			;
		}

		return ele;
	}

	public void doComplianceGraph(final StaplerRequest request, final StaplerResponse response) throws IOException {

		if (this.results != null) {

			float tempVal = 0F;

			if (this.getResults().getData().length > 0) {
				for (PageCountResultData pcrd : this.results.getData()) {
					tempVal += pcrd.getCompliance();
				}

				tempVal = tempVal / this.results.getData().length;
			}

			final Graph graph = new GraphImpl("Compliance Rating", tempVal) {
			};

			graph.doPng(request, response);
		}

	}

	private abstract class GraphImpl extends Graph {

		private final String graphTitle;

		private final float compliance;

		protected GraphImpl(final String graphTitle, final float compliance) {
			super(-1, 400, 300);
			this.graphTitle = graphTitle;
			this.compliance = compliance;
		}

		protected JFreeChart createGraph() {

			final JFreeChart chart = createStandardDialChart(this.graphTitle, "Percentage",
					new DefaultValueDataset(this.compliance * 100), 0D, 100D, 10D, 4);

			DialPlot dialplot = (DialPlot) chart.getPlot();
			StandardDialRange standarddialrange = new StandardDialRange(0D, 50D, Color.red);
			standarddialrange.setInnerRadius(0.52D);
			standarddialrange.setOuterRadius(0.55D);
			dialplot.addLayer(standarddialrange);
			StandardDialRange standarddialrange1 = new StandardDialRange(50D, 80D, Color.orange);
			standarddialrange1.setInnerRadius(0.52D);
			standarddialrange1.setOuterRadius(0.55D);
			dialplot.addLayer(standarddialrange1);
			StandardDialRange standarddialrange2 = new StandardDialRange(80D, 100D, Color.green);
			standarddialrange2.setInnerRadius(0.52D);
			standarddialrange2.setOuterRadius(0.55D);
			dialplot.addLayer(standarddialrange2);
			DialBackground dialbackground = new DialBackground(Color.white);
			dialplot.setBackground(dialbackground);
			dialplot.removePointer(0);
			DialPointer.Pointer pointer = new DialPointer.Pointer();
			pointer.setFillPaint(Color.black);
			dialplot.addPointer(pointer);

			return chart;
		}

		private JFreeChart createStandardDialChart(String s, String s1, ValueDataset valuedataset, double d, double d1,
				double d2, int i) {
			DialPlot dialplot = new DialPlot();
			dialplot.setDataset(valuedataset);
			dialplot.setDialFrame(new StandardDialFrame());
			dialplot.setBackground(new DialBackground());
			DialTextAnnotation dialtextannotation = new DialTextAnnotation(s1);
			dialtextannotation.setFont(new Font("Dialog", 1, 14));
			dialtextannotation.setRadius(0.69999999999999996D);
			dialplot.addLayer(dialtextannotation);
			DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
			dialplot.addLayer(dialvalueindicator);
			StandardDialScale standarddialscale = new StandardDialScale(d, d1, -120D, -300D, 10D, 4);
			standarddialscale.setMajorTickIncrement(d2);
			standarddialscale.setMinorTickCount(i);
			standarddialscale.setTickRadius(0.88D);
			standarddialscale.setTickLabelOffset(0.14999999999999999D);
			standarddialscale.setTickLabelFont(new Font("Dialog", 0, 14));
			dialplot.addScale(0, standarddialscale);
			dialplot.addPointer(new org.jfree.chart.plot.dial.DialPointer.Pin());
			DialCap dialcap = new DialCap();
			dialplot.setCap(dialcap);
			return new JFreeChart(s, dialplot);
		}

	}

}
