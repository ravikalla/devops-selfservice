package in.ravikalla.devopsselfserv.util;

public class CustomGlobalContext {
	private static String jenkinsUrl;
	private static String jenkinsUserName;
	private static String jenkinsPwd; // TODO : User String[]
	private static String jenkinsToken;
	private static String jenkinsCrumb;
	private static String gitToken;

	public static String getJenkinsUrl() {
		return jenkinsUrl;
	}
	public static void setJenkinsUrl(String jenkinsUrl) {
		CustomGlobalContext.jenkinsUrl = jenkinsUrl;
	}
	public static String getJenkinsToken() {
		return jenkinsToken;
	}
	public static void setJenkinsToken(String jenkinsToken) {
		CustomGlobalContext.jenkinsToken = jenkinsToken;
	}
	public static String getGitToken() {
		return gitToken;
	}
	public static void setGitToken(String gitToken) {
		CustomGlobalContext.gitToken = gitToken;
	}
	public static String getJenkinsUserName() {
		return jenkinsUserName;
	}
	public static void setJenkinsUserName(String jenkinsUserName) {
		CustomGlobalContext.jenkinsUserName = jenkinsUserName;
	}
	public static String getJenkinsPwd() {
		return jenkinsPwd;
	}
	public static void setJenkinsPwd(String jenkinsPwd) {
		CustomGlobalContext.jenkinsPwd = jenkinsPwd;
	}
	public static String getJenkinsCrumb() {
		return jenkinsCrumb;
	}
	public static void setJenkinsCrumb(String jenkinsCrumb) {
		CustomGlobalContext.jenkinsCrumb = jenkinsCrumb;
	}
}
