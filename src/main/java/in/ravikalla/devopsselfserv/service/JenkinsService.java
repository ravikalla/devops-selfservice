package in.ravikalla.devopsselfserv.service;

import static in.ravikalla.devopsselfserv.util.Constant.JENKINS_URI_CREATEORG;
import static in.ravikalla.devopsselfserv.util.Constant.JENKINS_URI_CREATEPROJECT;
import static in.ravikalla.devopsselfserv.util.Constant.JENKINS_URI_CRUMB;
import static in.ravikalla.devopsselfserv.util.Constant.JENKINS_URI_JAVA_TEMPLATE;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import in.ravikalla.devopsselfserv.util.CustomGlobalContext;
import in.ravikalla.devopsselfserv.util.OrgName;
import in.ravikalla.devopsselfserv.util.ProjectType;

@Service
public class JenkinsService {
	Logger L = LoggerFactory.getLogger(JenkinsService.class);

	@Autowired
	private RestTemplateBuilder restTemplate;

	@Autowired
	private Environment env;

//	CRUMB=$(curl -s 'https://2886795287-9080-frugo04.environments.katacoda.com/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,":",//crumb)' -u admin:admin)
	public String getCrumb() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
		String auth = CustomGlobalContext.getJenkinsUserName() + ":" + CustomGlobalContext.getJenkinsPwd();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.set("Authorization", authHeader);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.build().exchange(CustomGlobalContext.getJenkinsUrl() + JENKINS_URI_CRUMB, HttpMethod.GET, entity, String.class);
		String strCrumb = responseEntity.getBody();

		CustomGlobalContext.setJenkinsCrumb(strCrumb);
		return strCrumb;
	}

//	curl -s -XPOST 'https://2886795287-9080-frugo04.environments.katacoda.com/job/Consumer/createItem?name=ConsumerJavaProject1' -u admin:admin --data-binary @mylocalconfig.xml -H "$CRUMB" -H "Content-Type:text/xml" // CRUMB is not working here
//	curl -s -XPOST 'https://2886795329-9080-elsy04.environments.katacoda.com/job/ravikalla/createItem?name=ConsumerJavaProject1' -u admin:11742235975469edd1c11367cb556483a7 --data-binary @mylocalconfig.xml -H "Content-Type:text/xml"
	public String createJob(OrgName strOrg, ProjectType projectType, String strProjectName) throws Exception {
		L.info("Start : JenkinsService.downloadFile() : strOrg = {}, strProjectName = {}", strOrg, strProjectName);
		String strCreatedJobInfo = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.TEXT_XML));
			String auth = CustomGlobalContext.getJenkinsUserName() + ":" + CustomGlobalContext.getJenkinsToken();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.set("Authorization", authHeader);

			HttpEntity<byte[]> fileEntity = new HttpEntity<byte[]>(downloadFile(projectType).getByteArray(), headers);

			String strURL = CustomGlobalContext.getJenkinsUrl() + JENKINS_URI_CREATEORG.replace("<ORG_NAME>", strOrg.toString()) + JENKINS_URI_CREATEPROJECT.replace("<PROJECT_NAME>", strProjectName);
			L.info("72 : JenkinsService.createJob(...) : strURL = {}", strURL);

			ResponseEntity<String> response = restTemplate.build().exchange(strURL, HttpMethod.POST, fileEntity, String.class);
			strCreatedJobInfo = response.getBody();
		} catch (Exception e) {
			L.error("57 : JenkinsService.downloadFile(...) : Exception e = {}", e);
			throw e;
		}
		L.info("End : JenkinsService.downloadFile() : strOrg = {}, strProjectName = {}, strCreatedJobInfo = {}", strOrg, strProjectName, strCreatedJobInfo);
		return strCreatedJobInfo;
	}

//	curl -X GET https://2886795281-9080-kitek05.environments.katacoda.com/job/JavaTemplate/config.xml -u admin:admin -o mylocalconfig.xml
	public ByteArrayResource downloadFile(ProjectType projectType) {
		L.info("Start : JenkinsService.downloadFile() : strProjectName = {}", projectType);
		ByteArrayResource byteArrayResource = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			String auth = CustomGlobalContext.getJenkinsUserName() + ":" + CustomGlobalContext.getJenkinsPwd();
			byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
			String authHeader = "Basic " + new String(encodedAuth);
			headers.set("Authorization", authHeader);

			HttpEntity<String> entity = new HttpEntity<>(headers);
			String strURL = CustomGlobalContext.getJenkinsUrl() + JENKINS_URI_JAVA_TEMPLATE.replace("<TEMPLATE_NAME>", env.getProperty("git.projectname." + projectType));
			L.info("94 : JenkinsService.downloadFile(...) : strURL = {}", strURL);

			ResponseEntity<byte[]> response = restTemplate.build().exchange(strURL, HttpMethod.GET, entity, byte[].class);
			byteArrayResource = new ByteArrayResource(response.getBody());
//			Files.write(Paths.get("/Users/ravi_kalla/Desktop/Projects/devops-selfservice/src/main/resources/jenkinsjobtemplate.xml"), response.getBody());
		} catch (Exception e) {
			L.error("57 : JenkinsService.downloadFile(...) : Exception e = {}", e);
		}
		L.info("End : JenkinsService.downloadFile() : strProjectName = {}", projectType);
		return byteArrayResource;
	}
}