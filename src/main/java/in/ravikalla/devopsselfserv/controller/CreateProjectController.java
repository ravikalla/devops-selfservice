package in.ravikalla.devopsselfserv.controller;

import static in.ravikalla.devopsselfserv.util.Constant.TICKET_JOB_CREATE_BODY;
import static in.ravikalla.devopsselfserv.util.Constant.TICKET_JOB_CREATE_COMPLETED_LABEL;
import static in.ravikalla.devopsselfserv.util.Constant.TICKET_JOB_CREATE_STARTED_LABEL;
import static in.ravikalla.devopsselfserv.util.Constant.TICKET_JOB_CREATE_TITLE;
import static in.ravikalla.devopsselfserv.util.Constant.TICKET_ORG_NAME;
import static in.ravikalla.devopsselfserv.util.Constant.TICKET_REPO_NAME;

import org.eclipse.egit.github.core.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.devopsselfserv.service.DefectService;
import in.ravikalla.devopsselfserv.service.JenkinsService;
import in.ravikalla.devopsselfserv.service.SourceCodeService;
import in.ravikalla.devopsselfserv.util.CustomGlobalContext;
import in.ravikalla.devopsselfserv.util.OrgName;
import in.ravikalla.devopsselfserv.util.ProjectType;
import in.ravikalla.devopsselfserv.util.Util;

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
			, @RequestParam(value="technology", required=true) ProjectType projectType // JAVA
			, @RequestParam(value="newOrgName", required=true) OrgName newOrgName // ravikalla
			) throws Exception {
		L.info("Start : CreateProjectController.create(...) : projectType = {}, newOrgName = {}", projectType, newOrgName.toString());
		try {
//			Open a Ticket
			Issue issue = defectService.create(TICKET_ORG_NAME, TICKET_REPO_NAME,
					Util.createDefectInfo(TICKET_JOB_CREATE_TITLE, projectType, newOrgName),
					TICKET_JOB_CREATE_STARTED_LABEL,
					Util.createDefectInfo(TICKET_JOB_CREATE_BODY, projectType, newOrgName));

//			Create Git repository
			sourceCodeService.gitFork(projectType, newOrgName);

//			Create Jenkins Job
			jenkinsService.createJob(newOrgName, projectType, strProjectName);

//			Close Ticket
			defectService.closeTicket(CustomGlobalContext.getGitToken(), TICKET_ORG_NAME, TICKET_REPO_NAME, issue, TICKET_JOB_CREATE_COMPLETED_LABEL);
		} catch (Exception e) {
			L.error("65 : CreateProjectController.create(...) : Exception e = {}", e);
		}
		L.info("End : CreateProjectController.create(...) : projectType = {}, newOrgName = {}", projectType, newOrgName.toString());
	}
}