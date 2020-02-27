package in.ravikalla.devopsselfserv.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.ravikalla.devopsselfserv.util.CustomGlobalContext;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {
	Logger L = LoggerFactory.getLogger(AdminController.class);

	@RequestMapping(value = "/jenkinsCredentials", method = RequestMethod.POST)
	public void setJenkinsCredentials(@RequestParam(value = "jenkinsURL", required = true) String strJenkinsURL
			, @RequestParam(value = "jenkinsToken", required = false) String strJenkinsToken
			, @RequestParam(value = "jenkinsUserName", required = false) String strJenkinsUserName
			, @RequestParam(value = "jenkinsPwd", required = false) String strJenkinsPwd
			, @RequestParam(value = "jenkinsCrumb", required = false) String strJenkinsCrumb)
			throws Exception {
		L.info("Start : AdminController.setJenkinsCredentials(...)");
		CustomGlobalContext.setJenkinsUrl(strJenkinsURL);

		if (null != strJenkinsToken)
			CustomGlobalContext.setJenkinsToken(strJenkinsToken);
		if (null != strJenkinsUserName)
			CustomGlobalContext.setJenkinsUserName(strJenkinsUserName);
		if (null != strJenkinsPwd)
			CustomGlobalContext.setJenkinsPwd(strJenkinsPwd);
		L.info("End : AdminController.setJenkinsCredentials(...)");
	}

	@RequestMapping(value = "/gitToken", method = RequestMethod.POST)
	public void setGitToken(@RequestParam(value = "gitToken", required = true) String strGitToken) throws Exception {
		L.info("Start : AdminController.setGitToken(...)");
		CustomGlobalContext.setJenkinsToken(strGitToken);
		L.info("End : AdminController.setGitToken(...)");
	}
}