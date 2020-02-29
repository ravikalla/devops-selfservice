package in.ravikalla.devopsselfserv.service;

import static in.ravikalla.devopsselfserv.util.Constant.JENKINS_URI_CREATEORG;
import static in.ravikalla.devopsselfserv.util.Constant.JENKINS_URI_CREATEPROJECT;
import static in.ravikalla.devopsselfserv.util.Constant.JENKINS_URI_JAVA_TEMPLATE;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import in.ravikalla.devopsselfserv.util.CustomGlobalContext;
import in.ravikalla.devopsselfserv.util.OrgName;
import in.ravikalla.devopsselfserv.util.ProjectType;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class JenkinsService {
	Logger L = LoggerFactory.getLogger(JenkinsService.class);

	@Autowired
	private Environment env;

	@Autowired
    ResourceLoader resourceLoader;

//	curl -s -XPOST 'https://2886795287-9080-frugo04.environments.katacoda.com/job/Consumer/createItem?name=ConsumerJavaProject1' -u admin:admin --data-binary @mylocalconfig.xml -H "$CRUMB" -H "Content-Type:text/xml" // CRUMB is not working here
//	curl -s -XPOST 'https://2886795329-9080-elsy04.environments.katacoda.com/job/ravikalla/createItem?name=ConsumerJavaProject1' -u admin:11742235975469edd1c11367cb556483a7 --data-binary @mylocalconfig.xml -H "Content-Type:text/xml"
	public String createJob(OrgName strOrg, ProjectType projectType, String strProjectName) throws Exception {
		OkHttpClient client = new OkHttpClient().newBuilder().build();

//		RequestBody body = RequestBody.create(MediaType.parse("application/xml"), getJobFileContent(env.getProperty("jenkins.templatepath." + projectType)));
		RequestBody body = RequestBody.create(MediaType.parse("application/xml"), getJobFileContent(env.getProperty("jenkins.templatepath." + projectType)));

		String strURL = CustomGlobalContext.getJenkinsUrl() + JENKINS_URI_CREATEORG.replace("<ORG_NAME>", strOrg.toString()) + JENKINS_URI_CREATEPROJECT.replace("<PROJECT_NAME>", strProjectName);
		L.info("45 : JenkinsService.createJob(...) : strURL = {}", strURL);
		String auth = CustomGlobalContext.getJenkinsUserName() + ":" + CustomGlobalContext.getJenkinsToken();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);

		Request request = new Request.Builder().url(
				strURL)
				.method("POST", body).addHeader("Content-Type", "application/xml")
				.addHeader("Authorization", authHeader).build();
		Response response = client.newCall(request).execute();
		return response.body().toString();
	}

	public String getJobFileContent(String strJobFile) throws IOException {
//		File file = new File(strJobFile);
//		return Files.asCharSource(file, Charsets.UTF_8).read();
        String strData = null;
        try {
            Resource resource = resourceLoader.getResource("classpath:" + strJobFile);
        	InputStream input = resource.getInputStream();
            byte[] bdata = FileCopyUtils.copyToByteArray(input);
            strData = new String(bdata, StandardCharsets.UTF_8);
        } catch (IOException e) {
            L.error("JenkinsService.getJobFileContent(...) : IOException = {}", e);
        }

        return strData;
	}

//	curl -X GET https://2886795281-9080-kitek05.environments.katacoda.com/job/JavaTemplate/config.xml -u admin:admin -o mylocalconfig.xml
	public String downloadFile(ProjectType projectType) throws IOException {
		L.info("Start : JenkinsService.downloadFile() : strProjectName = {}", projectType);
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		String strURL = CustomGlobalContext.getJenkinsUrl() + JENKINS_URI_JAVA_TEMPLATE.replace("<TEMPLATE_NAME>", env.getProperty("git.templatename." + projectType));
		L.info("94 : JenkinsService.downloadFile(...) : strURL = {}", strURL);

		String auth = CustomGlobalContext.getJenkinsUserName() + ":" + CustomGlobalContext.getJenkinsToken();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);

		Request request = new Request.Builder()
				.url(strURL)
				.method("GET", null)
				.addHeader("Authorization", authHeader).build();
		Response response = client.newCall(request).execute();
		L.info("End : JenkinsService.downloadFile() : strProjectName = {}", projectType);
		return response.body().string();
	}

//	@Autowired
//	private RestTemplateBuilder restTemplate;

//	CRUMB=$(curl -s 'https://2886795287-9080-frugo04.environments.katacoda.com/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,":",//crumb)' -u admin:admin)
//	public String getCrumb() {
//		HttpHeaders headers = new HttpHeaders();
//		headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
//		String auth = CustomGlobalContext.getJenkinsUserName() + ":" + CustomGlobalContext.getJenkinsPwd();
//		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
//		String authHeader = "Basic " + new String(encodedAuth);
//		headers.set("Authorization", authHeader);
//
//		HttpEntity<String> entity = new HttpEntity<>(headers);
//		ResponseEntity<String> responseEntity = restTemplate.build().exchange(CustomGlobalContext.getJenkinsUrl() + JENKINS_URI_CRUMB, HttpMethod.GET, entity, String.class);
//		String strCrumb = responseEntity.getBody();
//
//		CustomGlobalContext.setJenkinsCrumb(strCrumb);
//		return strCrumb;
//	}
}