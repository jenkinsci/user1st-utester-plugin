package io.jenkins.plugins.user1st.utester.buildtask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.Descriptor.FormException;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import io.jenkins.plugins.user1st.utester.Messages;
import io.jenkins.plugins.user1st.utester.UTesterPlugin;
import io.jenkins.plugins.user1st.utester.parameters.RuleString;
import io.jenkins.plugins.user1st.utester.parameters.Runner;
import io.jenkins.plugins.user1st.utester.util.ApiClient;
import jenkins.model.GlobalConfiguration;
import jenkins.tasks.SimpleBuildStep;
import net.sf.json.JSONObject;

public class ScenarioTaskBuilder extends Builder implements SimpleBuildStep {
	
	private final String scenarioJSON;
	
//	private final Runner runner;
	
	private final ArrayList<RuleString> rulesList;
	
	private final String apiEndpoint = "/api/ExecutePageCountTask";
	 
	 
	@DataBoundConstructor
	public ScenarioTaskBuilder(String scenarioJSON, ArrayList<RuleString> rulesList) {
		super();
		this.scenarioJSON = scenarioJSON;
//		this.runner = runner;
		this.rulesList = rulesList;
	}
		
	public String getScenarioJSON() {
		return scenarioJSON;
	}

//	public Runner getRunner() {
//		return runner;
//	}

	public ArrayList<RuleString> getRulesList() {
		return rulesList;
	}

	@Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }


	@Override
	public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
		
		listener.getLogger().println("---Starting initial request ---");
		
		Map<String, Object> requestBody = new HashMap<>();
		
//		requestBody.put("Runner", this.runner);
		requestBody.put("ScenarioJSON", this.scenarioJSON);
		requestBody.put("Rules", this.rulesList);
		
		ApiClient client = new ApiClient(this.getDescriptor().getBaseUrl(), this.getDescriptor().getJwtToken());
		
		HttpResponse<JsonNode> response = client.post(this.apiEndpoint, requestBody);
		
		if(response.getStatus() == 200 && response.getBody().getObject().getJSONObject("data") != null &&
    			response.getBody().getObject().getJSONObject("data").getString("taskID") != null){

    		listener.getLogger().println("---Initial Request Complete! ---");
    		
    		String taskID = response.getBody().getObject().getJSONObject("data").getString("taskID");
    		
		} else {
    		listener.getLogger().println("Something went wrong with the request");
    		listener.getLogger().println("Response: " + response.getBody().getObject().toString());
    		listener.getLogger().println("---Initial Request Failed...---");
    	}
		
	}
	
	@Symbol("uTesterScenarioTask")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

		@SuppressWarnings("rawtypes")
		@Override
		public boolean isApplicable(Class<? extends AbstractProject> arg0) {
			return true;
		}
		
		public DescriptorImpl() {
			load();
        }
		
		@Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        	req.bindJSON(this,formData);
        	save();
        	return super.configure(req,formData);
        }
		
		@Override
        public String getDisplayName() {
			return Messages.ScenarioTaskBuilder_DescriptorImpl_DisplayName();
		}
		
		public String getBaseUrl() {
			return GlobalConfiguration.all().get(UTesterPlugin.class).getBaseUrl();
		}

		public String getJwtToken() {
			return GlobalConfiguration.all().get(UTesterPlugin.class).getJwtToken();
		}
		
	}

}
