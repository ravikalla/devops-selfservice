package in.ravikalla.devopsselfserv.util;

public class Util {
	public static String createDefectBody(String strTemplate, ProjectType projectType, OrgName newOrgName) {
		return strTemplate.replace("<LOB>", newOrgName.toString()).replace("<TECHNOLOGY>", projectType.toString());
	}
}
