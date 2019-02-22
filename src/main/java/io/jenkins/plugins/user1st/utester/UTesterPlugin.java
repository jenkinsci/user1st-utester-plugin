package io.jenkins.plugins.user1st.utester;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;

@Extension
public class UTesterPlugin extends GlobalConfiguration {

	private String baseUrl = "https://utesterapiqa.user1st.info";

	private String jwtToken = "";

	public UTesterPlugin() {
		load();
	}

	@Override
	public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
		req.bindJSON(this, json);

		this.baseUrl = json.getString("baseUrl");
		this.jwtToken = json.getString("jwtToken");

		save();
		return super.configure(req, json);
	}

	@DataBoundConstructor
	public UTesterPlugin(String baseUrl, String jwtToken) {
		this.baseUrl = baseUrl;
		this.jwtToken = jwtToken;
	}

	@Override
	public String getDisplayName() {
		return "uTester Plugin Configuration";
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

}
