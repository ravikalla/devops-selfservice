package in.ravikalla.devopsselfserv.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.devopsselfserv.service.MappingService;
import in.ravikalla.devopsselfserv.service.ProjectService;
import in.ravikalla.devopsselfserv.util.OrgName;
import in.ravikalla.devopsselfserv.util.ProjectType;

@RestController
@RequestMapping(value = "/selfServiceProject")
public class CreateProjectController {
	Logger L = LoggerFactory.getLogger(CreateProjectController.class);

	private ProjectService projectService;
	@Autowired
	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	private MappingService mappingService;
	@Autowired
	public void setMappingService(MappingService mappingService) {
		this.mappingService = mappingService;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void create(
			@RequestParam(value="gitToken", required=true) String strGitToken
			, @RequestParam(value="technology", required=true) ProjectType projectType // Java
			, @RequestParam(value="newOrgName", required=true) OrgName newOrgName // ravikalla
			) throws Exception {
		L.info("Start : CreateProjectController.create(...) : projectType = {}, newOrgName = {}", projectType, newOrgName);
		try {
			String strTemplateOrg = mappingService.getTemplateOrgName(projectType);
			String strTemplateRepoName = mappingService.getTemplateRepoName(projectType);

			L.info("45 : CreateProjectController.create(...) : strTemplateOrg = {}, strTemplateRepoName = {}", strTemplateOrg, strTemplateRepoName);

			projectService.gitFork(strTemplateOrg, strGitToken, strTemplateRepoName, "lob-" + newOrgName.toString()); // TODO: Generalize it. "lob-" is prepended as GIT repository name is like that - https://github.com/lob-wholesale
		} catch (Exception e) {
			L.error("50 : CreateProjectController.create(...) : Exception e = {}", e);
		}
		L.info("End : CreateProjectController.create(...) : projectType = {}, newOrgName = {}", projectType, newOrgName);
	}
}