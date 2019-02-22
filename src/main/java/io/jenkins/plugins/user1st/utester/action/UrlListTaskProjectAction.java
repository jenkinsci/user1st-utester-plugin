package io.jenkins.plugins.user1st.utester.action;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.util.ChartUtil;
import hudson.util.DataSetBuilder;
import hudson.util.Graph;
import io.jenkins.plugins.user1st.utester.results.UrlListResult;
import io.jenkins.plugins.user1st.utester.results.UrlListResultData;

public class UrlListTaskProjectAction implements Action {

	private final AbstractProject<?, ?> project;

	public UrlListTaskProjectAction(AbstractProject<?, ?> project) {
		this.project = project;
	}

	@Override
	public String getDisplayName() {
		return "uTester URL List Task Overview";
	}

	@Override
	public String getIconFileName() {
		return "/plugin/user1st-utester-jenkins/images/user1st-icon.png";
	}

	@Override
	public String getUrlName() {
		return "uTesterUrlListOverview";
	}

	public AbstractProject<?, ?> getProject() {
		return project;
	}

	private List<UrlListTaskAction> getExistingResults() {
		final List<UrlListTaskAction> pcResults = new ArrayList<UrlListTaskAction>();

		if (null == this.project) {
			return pcResults;
		}

		final List<? extends AbstractBuild<?, ?>> builds = project.getBuilds();

		for (AbstractBuild<?, ?> build : builds) {
			final UrlListTaskAction buildAction = build.getAction(UrlListTaskAction.class);

			if (buildAction == null)
				continue;

			final UrlListResult result = buildAction.getResults();

			if (result == null)
				continue;

			pcResults.add(buildAction);

		}

		return pcResults;

	}

	public void doComplianceGraph(final StaplerRequest request, final StaplerResponse response) throws IOException {

		final Graph graph = new GraphImpl("Overall Compliance Average Results", this.getExistingResults()) {
		};

		graph.doPng(request, response);

	}

	private abstract class GraphImpl extends Graph {
		private final String graphTitle;
		private final List<UrlListTaskAction> resultSet;

		protected GraphImpl(String graphTitle, List<UrlListTaskAction> resultSet) {
			super(-1, 400, 300);
			this.graphTitle = graphTitle;
			this.resultSet = resultSet;
		}

		protected DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> createDataSet() {
			DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel> dataSetBuilder = new DataSetBuilder<String, ChartUtil.NumberOnlyBuildLabel>();

			for (UrlListTaskAction pcta : this.resultSet) {

				if (pcta.getResults().getData().length > 0) {

					Number value = null;

					if (pcta.getResults().getData().length < 2) {
						value = new Double(pcta.getResults().getData()[0].getCompliance());
					} else {

						float tempValues = 0F;

						for (UrlListResultData pcrd : pcta.getResults().getData()) {
							tempValues += pcrd.getCompliance();
						}

						value = new Double(tempValues / pcta.getResults().getData().length);
					}
					dataSetBuilder.add(value, "rowKey", new ChartUtil.NumberOnlyBuildLabel(pcta.getBuild()));
				}

			}

			return dataSetBuilder;
		}

		protected JFreeChart createGraph() {
			final CategoryDataset dataset = createDataSet().build();

			final JFreeChart chart = ChartFactory.createLineChart(graphTitle, "Build Number", null, dataset,
					PlotOrientation.VERTICAL, false, true, false);

			chart.setBackgroundPaint(Color.white);

			return chart;
		}

	}

}
