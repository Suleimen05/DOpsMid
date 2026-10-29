import hudson.plugins.git.GitSCM
import hudson.plugins.git.BranchSpec
import jenkins.model.Jenkins
import org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition
import org.jenkinsci.plugins.workflow.job.WorkflowJob

def jenkins = Jenkins.get()
def jobName = 'Suleimen_Yeldossuly_Student_API'
def job = jenkins.getItem(jobName)

if (job == null) {
    job = jenkins.createProject(WorkflowJob, jobName)
}

def source = new GitSCM('https://github.com/Suleimen05/DOpsMid.git')
source.branches = [new BranchSpec('*/main')]
def definition = new CpsScmFlowDefinition(source, 'Jenkinsfile')
definition.setLightweight(true)
job.setDefinition(definition)
job.save()
