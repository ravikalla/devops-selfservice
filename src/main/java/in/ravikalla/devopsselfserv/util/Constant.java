package in.ravikalla.devopsselfserv.util;

public class Constant {
	public static final String TICKET_ORG_NAME = "ravikalla";
	public static final String TICKET_REPO_NAME = "devops-tickets";
	public static final String TICKET_JOB_CREATE_TITLE = "Create new DevOps workflow";
	public static final String TICKET_JOB_CREATE_STARTED_LABEL = "Pipeline requested";
	public static final String TICKET_JOB_CREATE_BODY = "Creating a new DEVOPS pipeline in LOB=\"<LOB>\" and Technology=\"<TECHNOLOGY>\"";
	public static final String TICKET_JOB_CREATE_COMPLETED_LABEL = "Pipeline created";

	public static final String JENKINS_URI_JAVA_TEMPLATE = "/job/<TEMPLATE_NAME>/config.xml"; // TODO : Get it from properties file
	public static final String JENKINS_URI_CREATEORG = "/job/<ORG_NAME>"; // TODO : Get it from properties file
	public static final String JENKINS_URI_CREATEPROJECT = "/createItem?name=<PROJECT_NAME>"; // TODO : Get it from properties file
	public static final String JENKINS_URI_CRUMB = "/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)"; // TODO : Get it from properties file
}
