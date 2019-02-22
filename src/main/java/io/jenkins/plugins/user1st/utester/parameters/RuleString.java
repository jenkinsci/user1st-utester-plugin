package io.jenkins.plugins.user1st.utester.parameters;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;

public final class RuleString extends AbstractDescribableImpl<RuleString> {
	
	private String rule;
	
	@DataBoundConstructor
	public RuleString(String rule) {
		this.rule = rule;
	}
	
	public String getRule() {
		return rule;
	}

	@Override
	public String toString() {
		return rule;
	}


	@Extension
    public static class DescriptorImpl extends Descriptor<RuleString>{
		@Override
        public String getDisplayName() {
            return "";
        }
	}

}
