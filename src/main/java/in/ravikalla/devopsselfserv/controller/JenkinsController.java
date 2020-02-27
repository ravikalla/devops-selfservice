package in.ravikalla.devopsselfserv.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.devopsselfserv.service.JenkinsService;
import in.ravikalla.devopsselfserv.util.OrgName;
import in.ravikalla.devopsselfserv.util.ProjectType;

@RestController
@RequestMapping(value = "/jenkins")
public class JenkinsController {
	Logger L = LoggerFactory.getLogger(JenkinsController.class);

	private JenkinsService jenkinsService;
	@Autowired
	public void setJenkinsService(JenkinsService jenkinsService) {
		this.jenkinsService = jenkinsService;
	}

	@RequestMapping(value = "/downloadTemplate", method = RequestMethod.POST)
	public ResponseEntity<Resource> downloadJenkinsTemplate(
			@RequestParam(value="technology", required=true) ProjectType projectType // JAVA
			) throws Exception {
		L.info("Start : JenkinsController.downloadJenkinsTemplate(...) : projectType = {}", projectType);
		ByteArrayResource byteArrayResource = null;
		try {
//			Create Jenkins Job
			byteArrayResource = jenkinsService.downloadFile(projectType);
		} catch (Exception e) {
			L.error("82 : JenkinsController.downloadJenkinsTemplate(...) : Exception e = {}", e);
		}
		L.info("End : JenkinsController.downloadJenkinsTemplate(...) : projectType = {}", projectType);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"jenkinsJobTemplate.xml\"")
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(byteArrayResource);
	}

	@RequestMapping(value = "/createProject", method = RequestMethod.POST)
	public String createProject(
			@RequestParam(value="projectName", required=true) String strProjectName
			, @RequestParam(value="technology", required=true) ProjectType projectType // JAVA
			, @RequestParam(value="newOrgName", required=true) OrgName newOrgName // ravikalla
			) throws Exception {
		L.info("Start : JenkinsController.downloadJenkinsTemplate(...) : projectType = {}, newOrgName = {}", projectType, newOrgName.toString());
		String strResponse = null;
		try {
//			Create Jenkins Job
			strResponse = jenkinsService.createJob(newOrgName, projectType, strProjectName);
		} catch (Exception e) {
			L.error("82 : JenkinsController.downloadJenkinsTemplate(...) : Exception e = {}", e);
		}
		L.info("End : JenkinsController.downloadJenkinsTemplate(...) : projectType = {}, newOrgName = {}", projectType, newOrgName.toString());
		return strResponse;
	}

	@RequestMapping(value = "/getCrumb", method = RequestMethod.POST)
	public String getCrumb() throws Exception {
		L.info("Start : JenkinsController.getCrumb()");
		String strCrumb = null;
		try {
			strCrumb = jenkinsService.getCrumb();
		} catch (Exception e) {
			L.error("82 : JenkinsController.downloadJenkinsTemplate(...) : Exception e = {}", e);
		}
		L.info("End : JenkinsController.getCrumb()");
		return strCrumb;
	}
}