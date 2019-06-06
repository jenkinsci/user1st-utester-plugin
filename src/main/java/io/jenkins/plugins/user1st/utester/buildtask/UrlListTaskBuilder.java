package io.jenkins.plugins.user1st.utester.buildtask;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import io.jenkins.plugins.user1st.utester.Messages;
import io.jenkins.plugins.user1st.utester.UTesterPlugin;
import io.jenkins.plugins.user1st.utester.action.UrlListTaskAction;
import io.jenkins.plugins.user1st.utester.action.UrlListTaskProjectAction;
import io.jenkins.plugins.user1st.utester.parameters.Rule;
import io.jenkins.plugins.user1st.utester.parameters.RuleString;
import io.jenkins.plugins.user1st.utester.parameters.Runner;
import io.jenkins.plugins.user1st.utester.requests.UrlListRequest;
import io.jenkins.plugins.user1st.utester.requests.UrlListRequestData;
import io.jenkins.plugins.user1st.utester.results.UrlListResult;
import io.jenkins.plugins.user1st.utester.results.UrlListResultData;
import io.jenkins.plugins.user1st.utester.results.StatusResult;
import io.jenkins.plugins.user1st.utester.util.ApiClient;
import jenkins.model.GlobalConfiguration;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

public class UrlListTaskBuilder extends Builder implements SimpleBuildStep {

	private final String initialUrl;
	private final String urlsWhiteList;;
	private final float complianceMinimum;
	
	private final int fetchResponseTimeout;
	
	private final boolean allowSimultaneousLogins;
	private final String loginFlowJson;
	private final boolean useRunner;

	private final RuleString[] rulesList;

	private final String apiEndpoint = "/api/ExecuteUrlListTask";

	@DataBoundConstructor
	public UrlListTaskBuilder(String initialUrl, String urlsWhiteList, RuleString[] rulesList,
			float complianceMinimum, boolean allowSimultaneousLogins, String loginFlowJson, boolean useRunner, int fetchResponseTimeout) {
		this.initialUrl = initialUrl;
		this.urlsWhiteList = urlsWhiteList;
		this.rulesList = rulesList;
		this.complianceMinimum = complianceMinimum;
		this.allowSimultaneousLogins = allowSimultaneousLogins;
		this.loginFlowJson = loginFlowJson;
		this.useRunner = useRunner;
		this.fetchResponseTimeout = fetchResponseTimeout;
	}
	
	public boolean isAllowSimultaneousLogins() {
		return this.allowSimultaneousLogins;
	}
	
	public boolean isUseRunner() {	
		return this.useRunner;
	}
	
	public String getLoginFlowJson() {
		return this.loginFlowJson;
	}

	public String getInitialUrl() {
		return initialUrl;
	}

	public String getUrlsWhiteList() {
		return urlsWhiteList;
	}

	public RuleString[] getRulesList() {
		return rulesList;
	}

	public float getComplianceMinimum() {
		return complianceMinimum;
	}
	
	public int getFetchResponseTimeout() {
		return fetchResponseTimeout;
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Override
	public Action getProjectAction(AbstractProject<?, ?> project) {
		return new UrlListTaskProjectAction(project);
	}

	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener)
			throws InterruptedException, IOException {

		listener.getLogger().println("---Starting initial request ---");
		listener.getLogger().println("URLs: " + this.initialUrl);
		listener.getLogger().println("URL Whitelist: " + this.urlsWhiteList);

		Rule[] rules = null;

		if (this.rulesList != null && this.rulesList.length > 0) {
			listener.getLogger().println("Rules List: " + Arrays.toString(this.rulesList));

			rules = new Rule[this.rulesList.length];

			ObjectMapper jacksonObjectMapper = new ObjectMapper();

			int i = 0;

			for (RuleString r : this.rulesList) {
				rules[i] = jacksonObjectMapper.readValue(r.toString(), Rule.class);
				i++;
			}
		}
		
		String[] urlsArray = this.initialUrl.split("\\r?\\n");

		String[] whiteListArray = this.urlsWhiteList.split("\\r?\\n");

		Map<String, Object> requestBody = new HashMap<>();

		Runner runner = null;
		
		if(this.useRunner)
			runner = new Runner(this.allowSimultaneousLogins,this.loginFlowJson);
		
		requestBody.put("Runner", runner);
		requestBody.put("Rules", rules);
		requestBody.put("Urls", urlsArray);
		requestBody.put("UrlsWhiteList", whiteListArray);

		ApiClient client = new ApiClient(this.getDescriptor().getBaseUrl(), this.getDescriptor().getJwtToken());

		HttpResponse<JsonNode> response = client.post(this.apiEndpoint, requestBody);

		ObjectMapper jacksonObjectMapper = new ObjectMapper();

		UrlListRequest requestResponse = jacksonObjectMapper.readValue(response.getBody().toString(),
				UrlListRequest.class);

		if (requestResponse != null && requestResponse.getStatusCode() == 200 && requestResponse.getData() != null
				&& ((UrlListRequestData) requestResponse.getData()).getTaskID() != null) {

			listener.getLogger().println("---Initial Request Complete! ---");

			String taskID = ((UrlListRequestData) requestResponse.getData()).getTaskID();

			int timeoutCount = 0;
			int maxRetry = this.fetchResponseTimeout;
			boolean isDone = false;

			listener.getLogger().println("--- Checking Task Status ---");

			while (!isDone && timeoutCount < maxRetry) {
				isDone = this.isTaskDone(listener, taskID);
				Thread.sleep(1000);
				timeoutCount++;
			}

			listener.getLogger().println("");

			if (timeoutCount >= maxRetry) {
				listener.getLogger().println("--- Max Request attempts exceeded. ---");
				run.setResult(Result.UNSTABLE);
			} else if (isDone) {
				listener.getLogger().println("--- Request Processing Complete! ---");

				try {

					listener.getLogger().println("--- Gathering Results from uTester API ---");

					UrlListResult results = this.getTaskResults(listener, taskID);

					UrlListTaskAction buildAction = new UrlListTaskAction(run, taskID, results);
					run.addAction(buildAction);

					Result r = Result.SUCCESS;

					listener.getLogger().println("--- Gathering Results Complete! ---");

					if (results.getData().length > 0) {

						listener.getLogger().println(
								"--- Checking compliance levels from each data result against minimum level ---");

						for (UrlListResultData pcrd : results.getData()) {
							if (pcrd.getCompliance() < this.complianceMinimum)
								r = Result.FAILURE;
						}
					} else {
						r = Result.UNSTABLE;
						listener.getLogger().println("--- No compliance level(s) found. ---");
					}

					run.setResult(r);

				} catch (Exception e) {
					listener.getLogger().println(e.getMessage());
					run.setResult(Result.UNSTABLE);
				}

			} else {
				listener.getLogger().println("--- Encounter Errors while Processing... ---");
				run.setResult(Result.UNSTABLE);
			}

		} else {
			listener.getLogger().println("Something went wrong with the request");
			listener.getLogger().println("Response: " + requestResponse.toString());
			listener.getLogger().println("---Initial Request Failed...---");
			run.setResult(Result.UNSTABLE);
		}

	}

	private UrlListResult getTaskResults(TaskListener listener, String taskID)
			throws JsonParseException, JsonMappingException, IOException {
		UrlListResult results = null;

		ApiClient client = new ApiClient(GlobalConfiguration.all().get(UTesterPlugin.class).getBaseUrl(),
				GlobalConfiguration.all().get(UTesterPlugin.class).getJwtToken());

		HttpResponse<String> response = client.getTaskResultsString(taskID);

		ObjectMapper jacksonObjectMapper = new ObjectMapper();

		results = jacksonObjectMapper.readValue(response.getBody(), UrlListResult.class);

		return results;
	}

	private boolean isTaskDone(TaskListener listener, String taskID) {

		ApiClient client = new ApiClient(GlobalConfiguration.all().get(UTesterPlugin.class).getBaseUrl(),
				GlobalConfiguration.all().get(UTesterPlugin.class).getJwtToken());

		int taskStatus = -1;

		HttpResponse<String> response = client.getTaskStatusString(taskID);

		try {

			ObjectMapper jacksonObjectMapper = new ObjectMapper();

			StatusResult tempResult = jacksonObjectMapper.readValue(response.getBody(), StatusResult.class);

			taskStatus = tempResult.getData().getTaskStatus();

		} catch (org.json.JSONException e) {
			listener.getLogger().println(e.getMessage());
		} catch (JsonParseException e) {
			listener.getLogger().println(e.getMessage());
		} catch (JsonMappingException e) {
			listener.getLogger().println(e.getMessage());
		} catch (IOException e) {
			listener.getLogger().println(e.getMessage());
		}

		listener.getLogger().print(".");
		listener.getLogger().flush();

		return taskStatus == 2;
	}

	@Symbol("uTesterUrlListTask")
	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		@SuppressWarnings("rawtypes")
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		public DescriptorImpl() {
			load();
		}

		@Override
		public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {

			System.out.println(formData.toString());

			req.bindJSON(this, formData);
			save();
			return super.configure(req, formData);
		}

		@Override
		public String getDisplayName() {
			return Messages.UrlListTaskBuilder_DescriptorImpl_DisplayName();
		}

		@Override
		public String getHelpFile() {
			return "/plugin/user1st-utester-jenkins/helpme.html";
		}

		public String getBaseUrl() {
			return GlobalConfiguration.all().get(UTesterPlugin.class).getBaseUrl();
		}

		public String getJwtToken() {
			return GlobalConfiguration.all().get(UTesterPlugin.class).getJwtToken();
		}

	}

}
