package in.ravikalla.devopsselfserv.controller;

import org.eclipse.egit.github.core.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.devopsselfserv.service.DefectService;
import in.ravikalla.devopsselfserv.service.JenkinsService;
import in.ravikalla.devopsselfserv.service.SourceCodeService;
import static in.ravikalla.devopsselfserv.util.Constant.*;
import in.ravikalla.devopsselfserv.util.Util;
import in.ravikalla.devopsselfserv.util.CustomGlobalContext;
import in.ravikalla.devopsselfserv.util.OrgName;
import in.ravikalla.devopsselfserv.util.ProjectType;

@RestController
@RequestMapping(value = "/selfServiceProject")
public class CreateProjectController {
	Logger L = LoggerFactory.getLogger(CreateProjectController.class);

	private SourceCodeService sourceCodeService;
	@Autowired
	public void setProjectService(SourceCodeService sourceCodeService) {
		this.sourceCodeService = sourceCodeService;
	}

	private DefectService defectService;
	@Autowired
	public void setDefectService(DefectService defectService) {
		this.defectService = defectService;
	}

	private JenkinsService jenkinsService;
	@Autowired
	public void setJenkinsService(JenkinsService jenkinsService) {
		this.jenkinsService = jenkinsService;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void create(
			@RequestParam(value="projectName", required=true) String strProjectName
			, @RequestParam(value="technology", required=true) ProjectType projectType // Java
			, @RequestParam(value="newOrgName", required=true) OrgName newOrgName // ravikalla
			) throws Exception {
		L.info("Start : CreateProjectController.create(...) : projectType = {}, newOrgName = {}", projectType, newOrgName.toString());
		try {
//			Open a Ticket
			Issue issue = defectService.create(CustomGlobalContext.getGitToken(), TICKET_ORG_NAME, TICKET_REPO_NAME, TICKET_JOB_CREATE_TITLE, TICKET_JOB_CREATE_STARTED_LABEL, Util.createDefectBody(TICKET_JOB_CREATE_BODY, projectType, newOrgName));

//			Create Git repository
			sourceCodeService.gitFork(CustomGlobalContext.getGitToken(), projectType.toString(), newOrgName.toString(), strProjectName);

//			Create Jenkins Job
			jenkinsService.createJob(CustomGlobalContext.getJenkinsToken(), newOrgName.toString(), strProjectName);

//			Close Ticket
			defectService.closeTicket(CustomGlobalContext.getGitToken(), TICKET_ORG_NAME, TICKET_REPO_NAME, issue, TICKET_JOB_CREATE_COMPLETED_LABEL);
		} catch (Exception e) {
			L.error("50 : CreateProjectController.create(...) : Exception e = {}", e);
		}
		L.info("End : CreateProjectController.create(...) : projectType = {}, newOrgName = {}", projectType, newOrgName.toString());
	}
}