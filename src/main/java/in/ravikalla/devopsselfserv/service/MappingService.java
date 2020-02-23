package in.ravikalla.devopsselfserv.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import in.ravikalla.devopsselfserv.util.ProjectType;

@Service
public class MappingService {
	Logger L = LoggerFactory.getLogger(MappingService.class);

	@Value("orgname.java") // TODO : Make this logic generic
	private String strJavaOrgName;
	@Value("projectname.java") // TODO : Make this logic generic
	private String strJavaProjectName;

	@Autowired
	private Environment env;

	public String getTemplateOrgName(ProjectType projectType) throws IOException {
		String templateRepoName = null;
		if (ProjectType.JAVA.equals(projectType))
			templateRepoName = env.getProperty("orgname." + projectType.toString());
		return templateRepoName;
	}

	public String getTemplateRepoName(ProjectType projectType) {
		String templateProjectName = null;
		if (ProjectType.JAVA.equals(projectType))
			templateProjectName = env.getProperty("projectname." + projectType.toString());
		return templateProjectName;
	}
}
