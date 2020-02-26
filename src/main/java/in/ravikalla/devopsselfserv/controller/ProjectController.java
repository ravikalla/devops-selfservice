package in.ravikalla.devopsselfserv.controller;

import org.eclipse.egit.github.core.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.devopsselfserv.service.SourceCodeService;

@RestController
@RequestMapping(value = "/project")
public class ProjectController {
	Logger L = LoggerFactory.getLogger(ProjectController.class);

	private SourceCodeService sourceCodeService;
	@Autowired
	public void setProjectService(SourceCodeService sourceCodeService) {
		this.sourceCodeService = sourceCodeService;
	}

	@RequestMapping(value = "/fork", method = RequestMethod.POST)
	public String fork(
			@RequestParam(value="token", required=true) String strToken
			, @RequestParam(value="orgName", required=true) String strOrg // jonashackt
			, @RequestParam(value="repoName", required=true) String strRepoName // spring-rabbitmq-messaging-microservices
			, @RequestParam(value="newOrgName", required=true) String newOrgName // ravikalla
			) throws Exception {
		L.info("Start : ProjectController.create(...) : strOrg = {}, strRepoName = {}, newOrgName = {}", strOrg, strRepoName, newOrgName);
		Repository repository = null;
		try {
			repository = sourceCodeService.gitFork(strOrg, strToken, strRepoName, newOrgName);
		} catch (Exception e) {
			L.error("39 : ProjectController.create(...) : Exception e = {}", e);
		}
		L.info("End : ProjectController.create(...) : strOrg = {}, strRepoName = {}, newOrgName = {}", strOrg, strRepoName, newOrgName);
		return (null == repository)?"https://github.com/" + newOrgName + "/" + strRepoName:repository.getName();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String create(
			@RequestParam(value="orgName", required=true) String strOrg
			, @RequestParam(value="token", required=true) String strToken
			, @RequestParam(value="repoName", required=true) String strRepoName
			, @RequestParam(value="isPrivate", required=true) Boolean blnIsPrivate
			, @RequestParam(value="cloneURL", required=true) String strCloneURL
			) throws Exception {
		L.info("Start : ProjectController.create(...) : strOrg = {}, strRepoName = {}, blnIsPrivate = {}, strCloneURL = {}", strOrg, strRepoName, blnIsPrivate, strCloneURL);
		Repository repository = null;
		try {
			repository = sourceCodeService.create(strOrg, strToken, strRepoName, blnIsPrivate, strCloneURL);
		} catch (Exception e) {
			L.error("39 : ProjectController.create(...) : Exception e = {}", e);
		}
		L.info("End : ProjectController.create(...) : strOrg = {}, strRepoName = {}, blnIsPrivate = {}, strCloneURL = {}", strRepoName, blnIsPrivate, strCloneURL);
		return (null == repository)?"NULL":repository.getName();
	}
}