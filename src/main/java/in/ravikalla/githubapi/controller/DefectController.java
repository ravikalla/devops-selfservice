package in.ravikalla.githubapi.controller;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.Issue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.githubapi.service.DefectService;

@RestController
@RequestMapping(value = "/defect")
public class DefectController {
	Logger L = LoggerFactory.getLogger(DefectController.class);

	private DefectService defectService;
	@Autowired
	public void setDefectService(DefectService defectService) {
		this.defectService = defectService;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String create(
			@RequestParam(value="token", required=true) String strToken
			, @RequestParam(value = "username", required=true) String strUserName
			, @RequestParam(value = "reponame", required=true) String strRepoName
			, @RequestParam(value = "title", required=true) String strTitle
			, @RequestParam(value = "label", required=true) String strLabel
			, @RequestParam(value = "body", required=true) String strBody
			) {
		L.info("Start : DefectController.create(...) : strUserName = {}, strRepoName = {}, strTitle = {}, strLabel = {}, strBody = {}", strUserName, strRepoName, strTitle, strLabel, strBody);
		Issue issue = null;
		try {
			issue = defectService.create(strToken, strUserName, strRepoName, strTitle, strLabel, strBody);
		} catch (Exception e) {
			L.error("39 : DefectController.create(...) : Exception e = {}", e);
		}
		L.info("End : DefectController.create(...) : strUserName = {}, strRepoName = {}, strTitle = {}, strLabel = {}, strBody = {}", strUserName, strRepoName, strTitle, strLabel, strBody);
		return (null == issue)?"NULL":issue.getTitle();
	}

	@RequestMapping(value = "/token/{token}/username/{username}/reponame/{reponame}", method = RequestMethod.GET)
	public List<Issue> get(@PathVariable(value = "token") String strToken
			, @PathVariable(value = "username") String strUserName
			, @PathVariable(value = "reponame") String strRepoName) {
		L.info("Start : DefectController.get(...) : strUserName = {}, strRepoName = {}", strUserName, strRepoName);
		List<Issue> lstdefects = null;
		try {
			lstdefects = defectService.get(strToken, strUserName, strRepoName);
		} catch (IOException e) {
			L.error("58 : DefectController.get(...) : Exception e = {}", e);
		}
		L.info("End : DefectController.get(...) : strUserName = {}, strRepoName = {}, lstdefects.size() = {}", strUserName, strRepoName, (null == lstdefects)?0:lstdefects.size());
		return lstdefects;
	}
}