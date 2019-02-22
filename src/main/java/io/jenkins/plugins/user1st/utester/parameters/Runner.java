package io.jenkins.plugins.user1st.utester.parameters;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;

public final class Runner extends AbstractDescribableImpl<Runner> {

	private boolean allowSimultaneousLogins;
	
	private String loginFlowJson;
	
	@DataBoundConstructor
	public Runner(boolean allowSimultaneousLogins, String loginFlowJson) {
		this.allowSimultaneousLogins = allowSimultaneousLogins;
		this.loginFlowJson = loginFlowJson;
	}
	
	public boolean getAllowSimultaneousLogins() {
		return allowSimultaneousLogins;
	}
	
	public String getLoginFlowJson() {
		return loginFlowJson;
	}
	
	@Extension
    public static class DescriptorImpl extends Descriptor<Runner>{
		@Override
        public String getDisplayName() {
            return "";
        }
	}
	
}
