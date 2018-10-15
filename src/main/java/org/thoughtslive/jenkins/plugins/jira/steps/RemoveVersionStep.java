package org.thoughtslive.jenkins.plugins.jira.steps;

import hudson.Extension;
import lombok.Getter;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;
import org.thoughtslive.jenkins.plugins.jira.api.ResponseData;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepDescriptorImpl;
import org.thoughtslive.jenkins.plugins.jira.util.JiraStepExecution;

import java.io.IOException;

public class RemoveVersionStep extends BasicJiraStep {

    private static final long serialVersionUID = -286745260046233494L;

    @Getter
    private final String id;

    @Getter
    private final Object params;

    @DataBoundConstructor
    public RemoveVersionStep(final String id, final Object params) {
        this.id = id;
        this.params = params;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new RemoveVersionStep.Execution(this, context);
    }

    @Extension
    public static class DescriptorImpl extends JiraStepDescriptorImpl {

        @Override
        public String getFunctionName() {
            return "jiraRemoveVersion";
        }

        @Override
        public String getDisplayName() {
            return getPrefix() + "Remove Version";
        }
    }

    public static class Execution extends JiraStepExecution<ResponseData<Void>> {

        private static final long serialVersionUID = -8962526152287490979L;

        private final RemoveVersionStep step;

        protected Execution(final RemoveVersionStep step, final StepContext context)
                throws IOException, InterruptedException {
            super(context);
            this.step = step;
        }

        @Override
        protected ResponseData<Void> run() throws Exception {

            ResponseData<Void> response = verifyInput();

            if (response == null) {
                logger.println("JIRA: Site - " + siteName + " - Removing version: " + step.getId());

                response = jiraService.removeVersion(step.getId(), step.getParams());
            }

            return logResponse(response);
        }

        @Override
        protected <T> ResponseData<T> verifyInput() throws Exception {
            String errorMessage = null;
            ResponseData<T> response = verifyCommon(step);

            return response;
        }
    }
}
