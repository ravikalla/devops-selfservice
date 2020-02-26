package in.ravikalla.devopsselfserv.util;

public class CustomGlobalContext {
	private static String jenkinsToken;
	private static String gitToken;

	public static String getJenkinsToken() {
		return jenkinsToken;
	}
	public static void setJenkinsToken(String jenkinsToken1) {
		jenkinsToken = jenkinsToken1;
	}

	public static String getGitToken() {
		return gitToken;
	}
	public static void setGitToken(String gitToken1) {
		gitToken = gitToken1;
	}
}
