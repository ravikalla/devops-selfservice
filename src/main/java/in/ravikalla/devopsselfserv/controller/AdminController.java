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

	@RequestMapping(value = "/jenkinsToken", method = RequestMethod.POST)
	public void setJenkinsToken(
			@RequestParam(value="jenkinsToken", required=true) String strJenkinsToken
			) throws Exception {
		L.info("Start : AdminController.setJenkinsToken(...)");
		CustomGlobalContext.setJenkinsToken(strJenkinsToken);
		L.info("End : AdminController.setJenkinsToken(...)");
	}
	@RequestMapping(value = "/gitToken", method = RequestMethod.POST)
	public void setGitToken(
			@RequestParam(value="gitToken", required=true) String strGitToken
			) throws Exception {
		L.info("Start : AdminController.setGitToken(...)");
		CustomGlobalContext.setJenkinsToken(strGitToken);
		L.info("End : AdminController.setGitToken(...)");
	}
}